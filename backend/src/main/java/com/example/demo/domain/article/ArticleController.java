package com.example.demo.domain.article;

import com.example.demo.domain.article.dto.ArticleRequestDto;
import com.example.demo.domain.article.dto.ArticleResponseDto;
import com.example.demo.domain.user.User;
import com.example.demo.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @PostMapping
    public ResponseEntity<ArticleResponseDto> createArticle(
            @RequestBody ArticleRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        ArticleResponseDto responseDto = articleService.createArticle(dto, user.getEmail());
        return ResponseEntity.ok(responseDto);
    }

    @GetMapping
    public ResponseEntity<List<ArticleResponseDto>> getMyArticles(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String emotion,
            @RequestParam(required = false) Double min
    ) {
        User user = userDetails.getUser();
        List<ArticleResponseDto> articles = articleService.searchArticles(user.getEmail(), sort, keyword, emotion, min);
        return ResponseEntity.ok(articles);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArticleResponseDto> getArticle(@PathVariable Long id) {
        ArticleResponseDto dto = articleService.getArticleById(id);
        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        articleService.deleteArticle(id, user.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(
            @PathVariable Long id,
            @RequestBody ArticleRequestDto dto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        articleService.updateArticle(id, user.getEmail(), dto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/public")
    public ResponseEntity<List<ArticleResponseDto>> getPublicArticles(
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String emotion,
            @RequestParam(required = false) Double min
    ) {
        List<ArticleResponseDto> articles = articleService.getPublicArticles(sort, keyword, emotion, min);
        return ResponseEntity.ok(articles);
    }
}
