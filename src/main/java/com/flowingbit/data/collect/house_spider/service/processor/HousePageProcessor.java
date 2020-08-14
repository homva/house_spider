package com.flowingbit.data.collect.house_spider.service.processor;

import com.alibaba.fastjson.JSON;
import com.flowingbit.data.collect.house_spider.dao.HouseDao;
import com.flowingbit.data.collect.house_spider.model.House;
import com.flowingbit.data.collect.house_spider.service.SpiderService;
import com.flowingbit.data.collect.house_spider.utils.IOUtil;
import com.flowingbit.data.collect.house_spider.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

import java.util.ArrayList;
import java.util.List;

@Component
public class HousePageProcessor implements PageProcessor {

    private String city;

    private String region;

    private int count = 1;

    private String tableName;

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Lazy
    @Autowired
    private SpiderService spiderService;

    public void setConfig(String city, String region, String tableName){
        this.city = city;
        this.region = region;
        this.count = 1;
        this.tableName = tableName;
    }

//    @Autowired
    private HouseDao houseDao = new HouseDao();

    // 部分一：抓取网站的相关配置，包括编码、抓取间隔、重试次数等
    private Site site = Site.me()
            .setRetryTimes(3)
            .setSleepTime(1000)
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
            //Thread.sleep(500);
            // 部分二：定义如何抽取页面信息，并保存下来
            if (!page.getHtml().xpath("//ul[@class='sellListContent']").match()) {
                page.setSkip(true);
            } else{
                String htmlContent = page.getHtml().toString();
//                logger.info("htmlContent:"+htmlContent);
                int total = Integer.valueOf(page.getHtml().xpath("//div[@class='resultDes clear']/h2/span/text()").toString().strip());
                int totalPage = total/30 + 1;
                System.out.println("==================总页数：" + totalPage + "  当前页：" + count + "===================");
                if((count<=totalPage) && (count<=100)){
                    count++;
                    //将html输出到文件
                    // C:/Users/flowi/Desktop/lianjia.html
                    //IOUtil.outFile(page.getHtml().toString(), "C:/Users/EDZ/Desktop/Object-Detection/house-spider/"+count+".html");

                    List<House> houseList = new ArrayList<>();
                    //开始提取页面信息
                    System.out.println("parse page: " + page.getUrl().toString());
                    List<Selectable> targets = page.getHtml().xpath("//li[@class='clear LOGVIEWDATA LOGCLICKDATA']").nodes();
                    targets.forEach(e -> {
                        try {
                            House house = new House();
                            String title = e.xpath("//div[@class='title']/a[1]/text()").toString();
                            String url = e.xpath("//a[@class='noresultRecommend img LOGCLICKDATA']/@href").toString();
                            String image = null;
                            if (e.xpath("//img[@class='lj-lazy']/@data-original").match()) {
                                image = e.xpath("//img[@class='lj-lazy']/@data-original").toString();
                            }
                            String s = e.xpath("//div[@class='houseInfo']/text()").toString();
                            String community = e.xpath("//div[@class='positionInfo']/a[1]/text()").toString();
                            String street = e.xpath("//div[@class='positionInfo']/a[2]/text()").toString();
                            String totolPrice = e.xpath("//div[@class='totalPrice']/span[1]/text()").toString();
                            String averagePrice = StringUtils.strip(StringUtils.strip(e.xpath("//div[@class='unitPrice']/span[1]/text()").toString(), "单价"), "元/平米");
                            String followInfo = e.xpath("//div[@class='followInfo']/text()").toString();

                            String tags = "";
                            Selectable tagsSel = e.xpath("//div[@class='tag']/span");
                            if(tagsSel.match()){
                                for (Selectable node : tagsSel.nodes()) {
                                    tags += node.xpath("//span/text()").toString()+" ";
                                }
                            }
                            String[] sl = followInfo.split("/");
                            String watch = StringUtil.collectStringNumber(sl[0]);
                            //现在取消了带看次数
                            //String view = StringUtil.collectStringNumber(sl[1]);
                            String releaseDate = sl[1].strip();
                            String ss = StringUtils.strip(s.strip(), "|").strip();
                            String[] houseInfo = StringUtils.split(ss, "|");
                            String roomCount = houseInfo[0].strip();
                            Double houseArea = Double.valueOf(houseInfo[1].strip().split("平米")[0]);
                            String towards = houseInfo[2].strip();
                            String decoration = null;
                            String floor = null;
                            String houseAge;
                            house.setHouseAge(0);
                            try{
                                decoration = houseInfo[3].strip();
                                floor = houseInfo[4].strip();
                                houseAge = StringUtils.strip(houseInfo[5].strip(), "年建");
                                if(houseAge!=null||houseAge.length()>0){
                                    if(StringUtils.isNumeric(houseAge)){
                                        house.setHouseAge(Integer.valueOf(houseAge));
                                    }
                                }
                            }catch (ArrayIndexOutOfBoundsException ae){
                                logger.warn("ArrayIndexOutOfBoundsException",ae);
                            }
                            house.setId(StringUtil.collectStringNumber(url));
                            house.setTitle(title);
                            house.setUrl(url);
                            house.setCommunity(community);
                            house.setStreet(street);
                            house.setRegion(region);
                            house.setCity(city);
                            house.setFloor(floor);
                            house.setTotalPrice(Double.valueOf(totolPrice));
                            house.setAveragePrice(Double.valueOf(averagePrice));
                            house.setImage(image);
                            house.setWatch(Integer.valueOf(watch));
                            house.setReleaseDate(releaseDate);
                            house.setRoomCount(roomCount);
                            house.setHouseArea(houseArea);
                            house.setTowards(towards);
                            house.setDecoration(decoration);
                            house.setTags(tags);
                            //System.out.println(house.toString());
                            //houseDao.insert(house);
                            houseList.add(house);
                            //将结果存到key：houses中


                        } catch (Exception ex) {
                            String jsonstr = e.xpath("//a[@class='noresultRecommend img LOGCLICKDATA']/@href").toString();
                            IOUtil.toFile(jsonstr, jsonstr + ".json");
                            //EmailService.sendMail("769010256@qq.com", page.getUrl().toString(), ex.getMessage() + "\n>>>>" + e.toString());
                            logger.error("Function process() >> targets.forEach() Exception,details:",ex);
                        }
                    });
                    if(houseList.size() == 0){
                        System.out.println("=============houseList.size()==0 ================");
                    }else{
                        try{
                            houseDao.batchInsert(houseList, tableName);
                            // 解析房源详情
                            houseList.forEach( house -> spiderService.submitHouseDetailTask(house));
                        }catch (Exception ee){
                            logger.error("page batch insert error,houseList:{}", JSON.toJSONString(houseList),ee);
                            return;
//                            houseList.forEach(g-> houseDao.insert(g, tableName));
                            //将houseList存到文件
                            //String jsonstr = JSONArray.toJSONString(houseList);
                            //IOUtil.outFile(jsonstr, "houseList_" + city + region + ".json");
                            //发送邮件
                            //EmailService.sendMail("769010256@qq.com", page.getUrl().toString(), jsonstr);
                        }
                    }

                    //page.putField("houses", houseList);
                    // 部分三：从页面发现后续的url地址来抓取
                    String newPage = calcNewPageUrl(page,count);
                    page.addTargetRequest(newPage);

                }else {
                    page.setSkip(true);
                }
            }
        } catch (Exception eee){
            logger.error("Function process() Exception,details:",eee);
            //EmailService.sendMail("769010256@qq.com", page.getUrl().toString(), "Function process() Exception,details:" + eee.getMessage());
        }
    }

    /**
     * 计算新页面url
     * @param page
     * @param count
     * @return
     */
    private String calcNewPageUrl(Page page, int count) {
        String url = page.getUrl().toString();
        int index = url.indexOf("/pg");
        String newPageUrl;
        if (index < 0) {
            StringBuffer sb = new StringBuffer(url);
            String keyword = "ershoufang";
            index = url.indexOf(keyword);
            sb.insert(index + keyword.length() + 1, "pg" + count);
            newPageUrl = sb.toString();
        } else {
//            ershoufang/pg2
            String pageKwd = "pg" + count;
            String pageKwdOld = "pg" + (count - 1);
            newPageUrl = url.substring(0, index) + "/" + pageKwd + url.substring(index + 1 + pageKwdOld.length(), url.length());
        }
        return newPageUrl;
    }


    @Override
    public Site getSite() {
        return site;
    }

//    public void startProcessor(String url, String city, String region, String tableName){
//        Spider.create(new HousePageProcessor(city, region, tableName))
//                //从"https://github.com/code4craft"开始抓
//                .addUrl(url)
//                //开启1个线程抓取
//                .thread(1)
//                //启动爬虫
//                .run();
//    }



//    public static void main(String[] args){
//        Spider.create(new HousePageProcessor("上海", "", Config.TABLE_NAME))
//                //从"https://github.com/code4craft"开始抓
//                .addUrl("https://sh.lianjia.com/ershoufang/bt2y3y4y5f2f3f5lc2sf1l2l3a2a3p3/")
//                //开启1个线程抓取
//                .thread(1)
//                //启动爬虫
//                .run();
//    }

}
