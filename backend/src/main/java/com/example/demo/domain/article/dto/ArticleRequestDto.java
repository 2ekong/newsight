package com.example.demo.domain.article.dto;

import lombok.Getter;

@Getter
public class ArticleRequestDto {
    private String title;
    private String originalUrl;
    private String summary;
    private String emotionJson;
}
