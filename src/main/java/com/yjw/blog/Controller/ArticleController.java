package com.yjw.blog.Controller;

import com.yjw.blog.Pojo.Article;
import com.yjw.blog.Pojo.Article_type;
import com.yjw.blog.Pojo.User;
import com.yjw.blog.Service.ArticleService;
import com.yjw.blog.Service.ArticleTypeService;
import com.yjw.blog.Service.UserService;
import com.yjw.blog.Util.IpUtil;
import com.yjw.blog.Util.JwtUtil;
import com.yjw.blog.VO.ResArticleList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.stream.Collectors;


@RestController
public class ArticleController {
    @Autowired
    ArticleTypeService articleTypeService;
    @Autowired
    ArticleService articleService;
    @Autowired
    UserService userService;

    @CrossOrigin
    @PostMapping("/get_article_types")
    public List<Article_type> GetArticleTypes(HttpServletRequest request) {
        String IPAddress = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userId = JwtUtil.verify(token, IPAddress);
        if (userId != null) {
            return articleTypeService.list();
        } else {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/save_article")
    public Article saveArticle(@RequestBody Article article, HttpServletRequest request) {
        String IP = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userid = JwtUtil.verify(token, IP);
        if (userid != null) {

            User user = userService.getByUserName(userid);
            article.setUserid(user.getId());
            article.setUserName(user.getUserName());
            return articleService.AddOrUpdate(article);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/article_list")
    public List<Article> getArticleList(HttpServletRequest request) {
        return articleService.list();
    }

    @CrossOrigin
    @PostMapping("/author_article_list")
    public ResArticleList getAuthorArticleList(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            String IP = IpUtil.getIpAddr(request);
            String token = request.getHeader("token");
            String userid = JwtUtil.verify(token, IP);
            if (userid != null) {
                int page = (int) map.get("page");
                int pageSize = (int) map.get("pageSize");
                boolean released = (boolean) map.get("released");  //是否发布
                Page<Article> pageArticle = articleService.getListOfAuthor(userid, released, page, pageSize);
                ResArticleList articleList = new ResArticleList();
                articleList.setPageCount(pageArticle.getTotalPages());
                articleList.setItemCount(pageArticle.getTotalElements());
                articleList.setArticleList(pageArticle.getContent());
                return articleList;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }

    }

    @CrossOrigin
    @PostMapping("/get_author_articles")
    public Map<String, Object> getAuthorArticleList(@RequestBody Map<String, Object> map) {
        try {
            int page = (int) map.get("page");
            int pageSize = (int) map.get("pageSize");
            String userid=(String)map.get("userid");
            boolean released = true;  //是否发布
            Page<Article> pageArticle = articleService.getListOfAuthor(userid, released, page, pageSize);
            Map<String,Object> resMap = new HashMap<String,Object>();
            resMap.put("totalPages",pageArticle.getTotalPages());
            resMap.put("totalItems",pageArticle.getTotalElements());
            List<Article> list=pageArticle.getContent();
            list.forEach(item->{
                if(item.getTxt()!=null&&item.getTxt().length()>200){
                    item.setTxt(item.getTxt().substring(0, 200) + "...");
                }
            });
            resMap.put("articleList",pageArticle.getContent());

            return resMap;
        } catch (Exception e) {
            return null;
        }

    }

    @CrossOrigin
    @PostMapping("/get_articlelist_by_type")
    public Map<String,Object> getListByType(@RequestBody Map<String,Object> map){
        try{
            String typeName=(String)map.get("typeName");
            int pageNum=(int)map.get("pageNum");
            int pageSize=(int)map.get("pageSize");
            Page<Article> pageArticle=articleService.getListByType(typeName,true,pageNum,pageSize);
            Map<String,Object> resMap=new HashMap<String,Object>();
            resMap.put("totalPages",pageArticle.getTotalPages());
            resMap.put("totalItems",pageArticle.getTotalElements());
            List<Article> list=pageArticle.getContent();
            list.forEach(item->{
                if(item.getTxt()!=null&&item.getTxt().length()>200){
                    item.setTxt(item.getTxt().substring(0, 200) + "...");
                }
            });
            resMap.put("articleList",pageArticle.getContent());
            return resMap;
        }catch (Exception e){
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/delete_article")
    public String DeleteArticle(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        String IP = IpUtil.getIpAddr(request);
        String token = request.getHeader("token");
        String userid = JwtUtil.verify(token, IP);
        if (userid != null) {
            Integer id = (Integer) map.get("id");
            return articleService.DeleteArticle(id);
        } else {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("/get_article")
    public Article getArticle(@RequestBody Map<String, Object> map) {
        Integer id = Integer.valueOf(map.get("id").toString());
        return articleService.getById((Integer) id);
    }

    @CrossOrigin
    @PostMapping("get_rel_articlelist")
    public ResArticleList getArticleListByReleased(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int pageNum = (int) map.get("page");
            int pageSize = (int) map.get("pagesize");
            boolean released = (boolean) map.get("released");
            Page<Article> page = articleService.getListByReleased(released, pageNum, pageSize);
            ResArticleList articleList = new ResArticleList();
            articleList.setPageCount(page.getTotalPages());
            articleList.setItemCount(page.getTotalElements());
            List<Article> lst = page.getContent();
            lst.forEach(item -> {
                if (item.getTxt() != null && item.getTxt().length() > 200) {
                    item.setTxt(item.getTxt().substring(0, 200) + "...");
                }
            });
            articleList.setArticleList(page.getContent());
            return articleList;
        } catch (Exception e) {
            return null;
        }
    }

    @CrossOrigin
    @PostMapping("search_article")
    public ResArticleList searchArticle(@RequestBody Map<String, Object> map, HttpServletRequest request) {
        try {
            int pageNum = (int) map.get("page");
            int pageSize = (int) map.get("pagesize");
            String keyWork = (String) map.get("search");
            Page<Article> page = articleService.search(keyWork, pageNum, pageSize);
            ResArticleList articleList = new ResArticleList();
            articleList.setPageCount(page.getTotalPages());
            articleList.setItemCount(page.getTotalElements());
            //取已发布的集合
            List<Article> list = page.getContent().stream().filter(s -> s.getReleased() == true).collect(Collectors.toList());
            list.forEach(item -> {
                if (item.getTxt() != null && item.getTxt().length() > 200) {
                    item.setTxt(item.getTxt().substring(0, 200) + "...");
                }
            });
            articleList.setArticleList(list);
            return articleList;
        } catch (Exception e) {
            return null;
        }
    }
}
