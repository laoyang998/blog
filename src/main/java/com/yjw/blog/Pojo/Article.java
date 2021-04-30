package com.yjw.blog.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.Date;

@Entity
//@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "type_id")
    private Article_type articleType;

    String title;

    @Lob
    @Column(columnDefinition = "text")
    String content;
    @Lob
    @Column(columnDefinition = "text")
    String txt;
    Date addDate;
    Date updateDate;
    boolean released;
    int viewCount;
    int userid;
    String userName;

    public String getTxt() {
        return txt;
    }

    public void setTxt(String txt) {
        this.txt = txt;
    }

    public boolean getReleased() {
        return this.released;
    }

    public void setReleased(boolean released) {
        this.released = released;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Article_type getArticleType() {
        return articleType;
    }

    public void setArticleType(Article_type articleType) {
        this.articleType = articleType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
}
