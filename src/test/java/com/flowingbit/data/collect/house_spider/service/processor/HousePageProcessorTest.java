package com.flowingbit.data.collect.house_spider.service.processor;

import org.junit.Test;

public class HousePageProcessorTest {

    @Test
    public void process() {
        int count = 100;
//        String url = "https://sh.lianjia.com/ershoufang/bt2y3y4y5f2f3f5sf1l3a2a3p1/";
        String url = "https://sh.lianjia.com/ershoufang/pg99bt2y3y4y5f2f3f5sf1l3a2a3p1/";
        int index = url.indexOf("/pg");
        String newPageUrl;
        if(index < 0){
            StringBuffer sb = new StringBuffer(url);
            String keyword = "ershoufang";
            index = url.indexOf(keyword);
            sb.insert(index+keyword.length()+1,"pg"+count);
            newPageUrl = sb.toString();
        }else{
//            ershoufang/pg2
            String pageKwd = "pg"+count;
            String pageKwdOld = "pg"+(count-1);
            newPageUrl = url.substring(0, index) + "/"+pageKwd+url.substring(index+1+pageKwdOld.length(),url.length());
        }
        System.out.println(newPageUrl);
    }
}