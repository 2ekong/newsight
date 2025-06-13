package com.example.demo.domain.comment;

import com.example.demo.domain.article.Article;
import com.example.demo.domain.article.ArticleRepository;
import com.example.demo.domain.comment.dto.CommentRequestDto;
import com.example.demo.domain.comment.dto.CommentResponseDto;
import com.example.demo.domain.user.User;
import com.example.demo.domain.user.UserRepository;
import com.example.demo.global.exception.CustomException;
import com.example.demo.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    // 댓글 저장
    public void createComment(CommentRequestDto requestDto, User user) {
        Article article = articleRepository.findById(requestDto.getArticleId())
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        Comment comment = Comment.builder()
                .article(article)
                .user(user)
                .content(requestDto.getContent())
                .build();

        commentRepository.save(comment);
    }

    // 게시글별 댓글 목록 조회
    public List<CommentResponseDto> getCommentsByArticleId(Long articleId) {
        Article article = articleRepository.findById(articleId)
                .orElseThrow(() -> new CustomException(ErrorCode.ARTICLE_NOT_FOUND));

        return commentRepository.findByArticle(article)
                .stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        commentRepository.delete(comment);
    }


}
