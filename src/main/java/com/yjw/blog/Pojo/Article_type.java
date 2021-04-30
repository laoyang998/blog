package com.yjw.blog.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;

@Entity
@Table(name="article_type")
//@JsonIgnoreProperties({ "handler","hibernateLazyInitializer" })
public class Article_type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    String typeName;
    int orderNum;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }
}
