package com.example.demo.domain.article;

import com.example.demo.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByUser(User user);

    List<Article> findByUserEmail(String userEmail);
}
