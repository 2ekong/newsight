package com.example.demo.domain.comment;

import com.example.demo.domain.article.Article;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticle(Article article);

    Collection<Object> findByArticleId(Long articleId);
}
