package com.example.demo.domain.article.dto;

import com.example.demo.domain.article.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ArticleResponseDto {
    private String title;
    private Long id;
    private String summary;
    private String emotionJson;
    private Long userId;
    private LocalDateTime createdAt;

    public static ArticleResponseDto from(Article article) {
        return new ArticleResponseDto(
                article.getTitle(),
                article.getId(),
                article.getSummary(),
                article.getEmotionJson(),
                article.getUser().getId(),
                article.getCreatedAt()
        );
    }
}
