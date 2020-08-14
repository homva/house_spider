package com.flowingbit.data.collect.house_spider.controller;

import cn.hutool.http.HttpUtil;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class SpiderControllerTest {

    @Test
    public void startHousePageTask() {
        String response = HttpUtil.post("http://localhost:8080/city/startHousePageTask",
                "https://sh.lianjia.com/ershoufang/bt2y4y5f2f3f5sf1l2a3p3/",10000);
    }
}