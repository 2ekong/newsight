package com.example.demo.global.ai;

import com.example.demo.global.ai.dto.SummaryRequestDto;
import com.example.demo.global.ai.dto.SummaryResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;

    @PostMapping("/summarize")
    public SummaryResponseDto summarize(@RequestBody SummaryRequestDto request) {
        String summary = aiService.getSummaryFromUrl(request.getOriginalUrl());
        String emotionJson = aiService.analyzeEmotion(summary);
        return new SummaryResponseDto(summary, emotionJson);
    }
}
