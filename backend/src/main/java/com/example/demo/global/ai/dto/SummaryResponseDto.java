package com.example.demo.global.ai.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SummaryResponseDto {
    private String summary;
    private String emotionJson;
}
