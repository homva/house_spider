package com.flowingbit.data.collect.house_spider.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {


    /**
     * 爬取指定城市的链家二手房,存到tableName的mysql表中
     */
    @GetMapping(path = "/hello")
    public String hello(){
        return "ok";
    }
}
