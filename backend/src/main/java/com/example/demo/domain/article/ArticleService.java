package com.example.demo.domain.article;

import com.example.demo.domain.article.dto.ArticleRequestDto;
import com.example.demo.domain.article.dto.ArticleResponseDto;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.global.ai.AiService;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final AiService aiService;

    public ArticleResponseDto createArticle(ArticleRequestDto dto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        String summary = aiService.getSummaryFromUrl(dto.getOriginalUrl());
        String emotionJson = aiService.analyzeEmotion(summary);

        Article article = Article.builder()
                .user(user)
                .title(dto.getTitle())
                .originalUrl(dto.getOriginalUrl())
                .content(summary)
                .summary(summary)
                .emotionJson(emotionJson)
                .build();

        articleRepository.save(article);
        return ArticleResponseDto.from(article);
    }

    public List<ArticleResponseDto> searchArticles(String email, String sort, String keyword, String emotion, Double min) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        List<Article> articles = articleRepository.findByUser(user);
        return articles.stream()
                .map(ArticleResponseDto::from)
                .collect(Collectors.toList());
    }

    public List<ArticleResponseDto> getPublicArticles(String sort, String keyword, String emotion, Double min) {
        List<Article> articles = articleRepository.findAll();

        return articles.stream()
                .filter(a -> keyword == null || a.getSummary().contains(keyword))
                .filter(a -> emotion == null || a.getEmotionJson().contains(emotion))
                .filter(a -> min == null || extractEmotionValue(a.getEmotionJson(), emotion) >= min)
                .sorted((a, b) -> {
                    // 안전한 createdAt 비교
                    var ca = a.getCreatedAt();
                    var cb = b.getCreatedAt();

                    if (ca == null && cb == null) return 0;
                    if (ca == null) return 1;  // null은 뒤로
                    if (cb == null) return -1;

                    if ("oldest".equalsIgnoreCase(sort)) {
                        return ca.compareTo(cb);   // 오래된 순
                    }
                    return cb.compareTo(ca);       // 기본: 최신순
                })
                .map(ArticleResponseDto::from)
                .collect(Collectors.toList());
    }


    public ArticleResponseDto getArticleById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));
        return ArticleResponseDto.from(article);
    }

    public void deleteArticle(Long id, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        if (!article.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        articleRepository.delete(article);
    }

    public void updateArticle(Long id, String email, ArticleRequestDto dto) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        if (!article.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.INVALID_AUTHORITY);
        }

        article.update(dto.getTitle(), dto.getSummary(), dto.getEmotionJson());
    }

    private double extractEmotionValue(String emotionJson, String emotion) {
        try {
            String[] parts = emotionJson.replaceAll("[{}\" ]", "").split(",");
            for (String part : parts) {
                String[] kv = part.split(":");
                if (kv[0].equalsIgnoreCase(emotion)) {
                    return Double.parseDouble(kv[1]);
                }
            }
        } catch (Exception ignored) {}
        return 0.0;
    }
}
