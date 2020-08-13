package com.flowingbit.data.collect.house_spider.service.processor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.Assert.*;

public class HouseDetailProcessorTest {

    @Test
    public void process() {
        String html = "<span class=\"info\"><a href=\"/ershoufang/songjiang/\" target=\"_blank\">松江</a>&nbsp;<a href=\"/ershoufang/jiuting/\" target=\"_blank\">九亭</a>&nbsp;外环外</span>";
        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher matcher = p.matcher(html);
        List<String> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(matcher.group());
        }
        StringBuffer result = new StringBuffer();
        list.forEach(result::append);
        System.out.println(result.toString());



//        <div class="areaName">
//      <i></i>
//      <span class="label">所在区域</span>
//      <span class="info"><a href="/ershoufang/songjiang/" target="_blank">松江</a>&nbsp;<a href="/ershoufang/jiuting/" target="_blank">九亭</a>&nbsp;外环外</span>
//      <a href="" class="supplement" title="" style="color:#394043;"></a>
//     </div>


        //div[@class='areaName']/span[2]/text()
        //div[@class='areaName']/span[2]/a

    }

    @Test
    public void getSite() {
        String url = "https://sh.lianjia.com/ershoufang/107102624498.html";
        int endIndex = url.indexOf(".html");
        String kwd = "ershoufang/";
        int startIndex = url.indexOf(kwd);
        String id = url.substring(startIndex+kwd.length(),endIndex);
        System.out.println(id);
    }
}