package com.yjw.blog.Dao;

import com.yjw.blog.Pojo.Article;
import com.yjw.blog.Pojo.Article_type;
import com.yjw.blog.Pojo.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ArticleDao extends JpaRepository<Article,Integer> {
   Page<Article> findAllByUseridAndReleased(Integer id,boolean released,Pageable pageable);
   Page<Article> findAllByReleased(boolean released, Pageable pageable);
   Page<Article> findAllByUserNameLikeOrTitleLikeOrTxtLike(String uname,String title,String txt,Pageable pageable);
   //按分类查询
   Page<Article> findAllByArticleTypeAndReleased(Article_type articleType,boolean released,Pageable pageable);
}
