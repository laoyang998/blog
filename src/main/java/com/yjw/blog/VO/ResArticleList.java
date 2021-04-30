package com.yjw.blog.VO;

import com.yjw.blog.Pojo.Article;

import java.util.List;

public class ResArticleList {
    int pageCount;
    long itemCount;
    List<Article> articleList;

    public long getItemCount() {
        return itemCount;
    }

    public void setItemCount(long itemCount) {
        this.itemCount = itemCount;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<Article> getArticleList() {
        return articleList;
    }

    public void setArticleList(List<Article> articleList) {
        this.articleList = articleList;
    }
}
