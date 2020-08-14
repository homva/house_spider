package com.flowingbit.data.collect.house_spider.service.processor;

import com.flowingbit.data.collect.house_spider.model.Config;
import com.flowingbit.data.collect.house_spider.model.House;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;
import us.codecraft.webmagic.Spider;

@NoArgsConstructor
@Service
public class CommonProcessorStarter {

    /**
     * 开启房源详情页爬取
     * @param nowHouse
     */
    public void startHouseDetail(String url, House nowHouse){
        HouseDetailProcessor houseDetailProcessor;
        if(nowHouse == null){
            houseDetailProcessor = new HouseDetailProcessor(Config.TABLE_NAME);
        }else{
            houseDetailProcessor = new HouseDetailProcessor(Config.TABLE_NAME,nowHouse);
        }
        Spider.create(houseDetailProcessor)
//                .addUrl("https://sh.lianjia.com/ershoufang/107102624498.html")
                .addUrl(url)
                //开启2个线程抓取
                .thread(1)
                //启动爬虫
                .run();
    }

    /**
     * 开启房源列表页爬取
     */
    public void startHousePageList(String url){
        HousePageProcessor housePageProcessor = new HousePageProcessor("上海", "", Config.TABLE_NAME);
        Spider.create(housePageProcessor)
//                .addUrl("https://sh.lianjia.com/ershoufang/bt2y3y4y5f2f3f5lc2sf1l2l3a2a3p3/")
                .addUrl(url)
                //开启1个线程抓取
                .thread(1)
                //启动爬虫
                .run();
    }
}
