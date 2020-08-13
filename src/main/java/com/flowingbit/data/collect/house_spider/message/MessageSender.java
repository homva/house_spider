package com.flowingbit.data.collect.house_spider.message;

import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.flowingbit.data.collect.house_spider.message.bean.MsgData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


/**
 * Server酱接入
 * @author homva
 * @date 2020/8/13
 *
 */
@Slf4j
@Service
public class MessageSender {

    private final String SECRET_KEY = "SCU26525T974542c312643fa831fb8ffa1eaf35675afbd62fc6ad8";

    /**
     * 发送Server酱消息（链家）
     * @param msg
     * @return
     */
    public boolean sendLianJiaMsg(MsgData msg){
        String url = "https://sc.ftqq.com/"+SECRET_KEY+".send";
        String title = "您有一条新房源可以瞄一眼！";
        /**
         * 模板如下：
         * ##### 房源信息（链家）
         *
         * **[点此去往链家房源页面](https://image1.ljcdn.com/110000-inspection/pc1_kGtHNT9UV_1.jpg.296x216.jpg)**
         *
         * 板块：杨思
         *
         * 内中外环：内环内
         *
         * 均价：50000元/平
         *
         * 总价：**300**万
         *
         * 图片：
         *
         * ![图片：](https://image1.ljcdn.com/110000-inspection/pc1_kGtHNT9UV_1.jpg.296x216.jpg)
         */
        String desp = "##### 房源信息（链家）\n" +
                "\n" +
                "**[点此去往链家房源页面]("+msg.getLianjiaUrl()+")**\n" +
                "\n" +
                "板块："+msg.getBanKuai()+"\n" +
                "\n" +
                "内中外环："+msg.getHuanPosition()+"\n" +
                "\n" +
                "均价："+msg.getAvgPrice()+"元/平\n" +
                "\n" +
                "总价：**"+msg.getTotalPrice()+"**万\n" +
                "\n" +
                "图片：\n" +
                "\n" +
                "![图片：]("+msg.getMainPic()+")\n" +
                "\n" +
                "\n" +
                "\n";
        try {
            String resp = HttpUtil.createPost(url).form("text",title).form("desp",desp).timeout(10*1000).execute().body();
            log.info("msg send success,lianjiaUrl:{},response:{}",msg.getLianjiaUrl(),resp);
        } catch (HttpException e) {
            log.error("请求server酱异常",e);
            return false;
        }
        return true;
    }
}
