package com.yjw.blog.Service;

import com.yjw.blog.Dao.ArticleDao;
import com.yjw.blog.Dao.ArticleTypeDao;
import com.yjw.blog.Dao.UserDao;
import com.yjw.blog.Pojo.Article;
import com.yjw.blog.Pojo.Article_type;
import com.yjw.blog.Pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    @Autowired
    ArticleDao articleDao;
    @Autowired
    UserDao userDao;
    @Autowired
    ArticleTypeDao articleTypeDao;

    public Article AddOrUpdate(Article article) {
        Date date = new Date();
        if (article.getId() == 0) {
            article.setAddDate(date);
            article.setUpdateDate(date);
        } else {
            Article dbArticle=articleDao.findById(article.getId()).get();
            article.setAddDate(dbArticle.getAddDate());
            article.setUpdateDate(date);
        }
        return articleDao.save(article);
    }

    public Article getById(Integer id) {
        Article article = articleDao.findById(id).get();
        return article;
    }

    public List<Article> list() {
        return articleDao.findAll();
    }

    public Page<Article> getListOfAuthor(String userid,boolean released,int pageNum,int pageSize) {
        User user = userDao.findByUserId(userid);
        Pageable pageable=PageRequest.of(pageNum,pageSize,Sort.by("addDate").descending());
        Page<Article> pageArticle = articleDao.findAllByUseridAndReleased(user.getId(),released,pageable);
        return pageArticle;
    }

    public String DeleteArticle(Integer id) {
        try {
            articleDao.deleteById(id);
            return "OK";
        } catch (Exception e) {
            return "NG";
        }
    }

    public Page<Article> getListByReleased(boolean released,int pageNum,int pageSize){
        Pageable pageable= PageRequest.of(pageNum,pageSize, Sort.by("addDate").descending());
        return articleDao.findAllByReleased(released,pageable);
    }

    public Page<Article> search(String keyWord,int pageNum,int pageSize){
        Pageable pageable= PageRequest.of(pageNum,pageSize, Sort.by("addDate").descending());
        String searchWord="%"+keyWord+"%";
        return articleDao.findAllByUserNameLikeOrTitleLikeOrTxtLike(searchWord,searchWord,searchWord,pageable);
    }

    public Page<Article> getListByType(String typeName,boolean released,int pageNum,int pageSize) throws Exception{
        Pageable pageable=PageRequest.of(pageNum,pageSize,Sort.by("addDate").descending());
        Article_type articleType=articleTypeDao.findByTypeName(typeName);
        return articleDao.findAllByArticleTypeAndReleased(articleType,released,pageable);
    }
}
