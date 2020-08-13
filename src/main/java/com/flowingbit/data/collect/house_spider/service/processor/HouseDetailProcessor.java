package com.flowingbit.data.collect.house_spider.service.processor;

import com.alibaba.fastjson.JSON;
import com.flowingbit.data.collect.house_spider.dao.HouseDao;
import com.flowingbit.data.collect.house_spider.model.House;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Html;

/**
 * 房源详情
 */
@Slf4j
@Component
public class HouseDetailProcessor implements PageProcessor {

    private String tableName;

    private static HouseDao houseDao = new HouseDao();

    public HouseDetailProcessor(){}

    public HouseDetailProcessor(String tableName){
        this.tableName = tableName;
    }

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(500)
            .addHeader("Accept-Encoding", "gzip, deflate, br")
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9")
            .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3")
            .addHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_13_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/73.0.3683.103 Safari/537.36");

    /**
     * process是定制爬虫逻辑的核心接口，在这里编写抽取逻辑
     */
    @Override
    public void process(Page page) {
        try {
//            String html = page.getHtml().toString();
//            log.info("html:{]",html);
            Html htmlDoc = page.getHtml();

            //环线位置
            String qu = htmlDoc.xpath("//div[@class='areaName']/span[2]/a[1]/text()").toString();
            String jiedao = htmlDoc.xpath("//div[@class='areaName']/span[2]/a[2]/text()").toString();
            String huan = htmlDoc.xpath("//div[@class='areaName']/span[2]/text()").toString();
            String huanLocation = qu+" "+jiedao+huan;

            //链家编号
            String url = page.getUrl().toString();
            int endIndex = url.indexOf(".html");
            String kwd = "ershoufang/";
            int startIndex = url.indexOf(kwd);
            String id = url.substring(startIndex+kwd.length(),endIndex);

            //梯户比例
            String tihu = htmlDoc.xpath("//*[@id=\"introduction\"]/div/div/div[1]/div[2]/ul/li[10]/text()").toString();
            //产权
            String chanQuan = htmlDoc.xpath("//*[@id=\"introduction\"]/div/div/div[2]/div[2]/ul/li[6]/span[2]/text()").toString();
            //税费
            String shuiFei = htmlDoc.xpath("/html/body/div[7]/div[1]/div[2]/div/div[4]/div[2]/text()").toString();
            //交通情况
            String jiaoTong = htmlDoc.xpath("/html/body/div[7]/div[1]/div[2]/div/div[2]/div[2]/text()").toString();
            //挂牌时间
            String guaPaiTime = htmlDoc.xpath("//*[@id=\"introduction\"]/div/div/div[2]/div[2]/ul/li[1]/span[2]/text()").toString();
            //核心卖点
            String sellMsg = htmlDoc.xpath("/html/body/div[7]/div[1]/div[2]/div/div[7]/div[2]/text()").toString();

            House house = new House();
            house.setId(id);
            house.setHuanXian(huanLocation);
            house.setTihuRate(tihu);
            house.setChanQuan(chanQuan);
            house.setShuiFei(shuiFei);
            house.setJiaoTong(jiaoTong);
            house.setGuaPaiTime(guaPaiTime);
            house.setSellMsg(sellMsg);

//            System.out.println(JSON.toJSONString(house));
            houseDao.updateById(house,tableName);
            System.out.println("----------- 房源明细已更新：id："+house.getId()+"，url："+url+" ----------");
        } catch (Exception eee){
            log.error("HouseDetailProcessor.process error",eee);
            //EmailService.sendMail("769010256@qq.com", page.getUrl().toString(), "Function process() Exception,details:" + eee.getMessage());
        }
    }


    @Override
    public Site getSite() {
        return site;
    }


    public void startProcessor(String url, String tableName){
        Spider.create(new HouseDetailProcessor(tableName))
                //从"https://github.com/code4craft"开始抓
                .addUrl(url)
                //开启1个线程抓取
                .thread(1)
                //启动爬虫
                .run();
    }

    public static void main(String[] args){
        Spider.create(new HouseDetailProcessor("shanghai_20200813"))
                .addUrl("https://sh.lianjia.com/ershoufang/107102624498.html")
                //开启2个线程抓取
                .thread(1)
                //启动爬虫
                .run();
    }

}
