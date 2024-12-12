package com.yuyou.zizaiyou.serverarticle.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/article")
public class ArticleController {
    @GetMapping("/get")
    public String getArticle(){
        return "This is controller..";
    }
}
