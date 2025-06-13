package com.example.demo.global.ai;

import com.example.demo.global.ai.dto.SummaryResponseDto;
import com.example.demo.global.ai.dto.GptResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${openai.api.key}")
    private String openAiApiKey;

    @Value("${openai.api.url}")
    private String openAiApiUrl;

    @PostConstruct
    public void init() {
        if (!openAiApiKey.startsWith("Bearer ")) {
            openAiApiKey = "Bearer " + openAiApiKey;
        }
    }

    public String getSummaryFromUrl(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            String text = doc.body().text();

            // 텍스트 길이 제한
            if (text.length() > 1500) {
                text = text.substring(0, 1500);
            }

            String prompt = "다음 뉴스 본문을 3~4문장으로 요약해줘:\n" + text;
            return callGptApi(prompt);
        } catch (Exception e) {
            return "요약 실패: " + e.getMessage();
        }
    }

    public String analyzeEmotion(String summary) {
        try {
            String prompt = String.format(
                    "다음 뉴스 요약을 보고 감정을 분석해서 JSON 형식으로 반환해줘. " +
                            "각 감정은 joy, anger, sad, fear, surprise 다섯 가지고, 값은 0.0 ~ 1.0 사이 숫자야.\n\n" +
                            "뉴스 요약: \"%s\"", summary
            );

            String content = callGptApi(prompt).trim();
            int jsonStart = content.indexOf("{");
            return jsonStart != -1 ? content.substring(jsonStart) : content;

        } catch (Exception e) {
            return "{\"joy\":0.0,\"anger\":0.0,\"sad\":0.0,\"fear\":0.0,\"surprise\":0.0}";
        }
    }

    private String callGptApi(String prompt) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(openAiApiKey.replace("Bearer ", "").trim());

        Map<String, Object> body = new HashMap<>();
        body.put("model", "gpt-3.5-turbo");
        body.put("messages", List.of(Map.of("role", "user", "content", prompt)));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);
        ResponseEntity<String> response = restTemplate.postForEntity(openAiApiUrl, entity, String.class);

        GptResponse gptResponse = objectMapper.readValue(response.getBody(), GptResponse.class);
        List<GptResponse.Choice> choices = gptResponse.getChoices();

        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("GPT 응답에 choices가 없음");
        }

        return choices.get(0).getMessage().getContent();
    }

    public SummaryResponseDto analyzeAndSummarize(String url) {
        String summary = getSummaryFromUrl(url);
        String emotionJson = analyzeEmotion(summary);
        return new SummaryResponseDto(summary, emotionJson);
    }


}
