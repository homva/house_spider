package com.flowingbit.data.collect.house_spider.message;

import com.flowingbit.data.collect.house_spider.message.bean.MsgData;
import org.junit.Test;

public class MessageSenderTest {

    @Test
    public void sendLianJiaMsg() {
        MsgData msg = new MsgData();
        msg.setAvgPrice("50000");
        msg.setTotalPrice("350");
        msg.setBanKuai("杨思");
        msg.setCommunity("三十而已，全明两房，拎包入住，有家才有好生活~");
        msg.setHuanPosition("内环内");
        msg.setLianjiaUrl("https://sh.lianjia.com/ershoufang/107102624498.html");
        msg.setMainPic("https://vrlab-image4.ljcdn.com/release/auto3dhd/672151578a543e13f268162c088937d1/screenshot/1597029223_11/pc0_hnCm8ZNk0.jpg?imageMogr2/quality/70/thumbnail/1024x");
        MessageSender.sendLianJiaMsg(msg);
    }
}