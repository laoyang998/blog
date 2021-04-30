package com.yjw.blog.Service;

import com.yjw.blog.Dao.ArticleTypeDao;
import com.yjw.blog.Pojo.Article_type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleTypeService {
    @Autowired
    private ArticleTypeDao articleTypeDao;

    public void addOrUpdate(Article_type articleType){
        articleTypeDao.save(articleType);
    }

    public List<Article_type> list(){
        return articleTypeDao.findAll();
    }
}
