package com.example.demo.domain.comment;

import com.example.demo.domain.comment.dto.CommentRequestDto;
import com.example.demo.domain.comment.dto.CommentResponseDto;
import com.example.demo.domain.user.User;
import com.example.demo.global.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    //  JwtTokenProvider, UserRepository 필요 없어짐

    @PostMapping
    public ResponseEntity<?> createComment(
            @RequestBody @Valid CommentRequestDto requestDto,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();  // 실제 User 꺼내기
        commentService.createComment(requestDto, user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{articleId}")
    public ResponseEntity<?> getComments(@PathVariable Long articleId) {
        return ResponseEntity.ok(commentService.getCommentsByArticleId(articleId));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<?> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        User user = userDetails.getUser();
        commentService.deleteComment(commentId, user);
        return ResponseEntity.noContent().build();
    }

}
