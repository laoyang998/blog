package com.yjw.blog.Dao;

import com.yjw.blog.Pojo.Article_type;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleTypeDao extends JpaRepository<Article_type,Integer> {
    Article_type findByTypeName(String t);
}
