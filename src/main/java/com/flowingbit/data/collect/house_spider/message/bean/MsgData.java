package com.flowingbit.data.collect.house_spider.message.bean;

import lombok.Data;

/**
 * 通知模板
 * @author homva
 * @date 2020/8/13
 *
 */
@Data
public class MsgData {
    /**
     * 链家url
     */
    private String lianjiaUrl;
    /**
     * 总价
     */
    private String totalPrice;
    /**
     * 内环内or外环内
     */
    private String huanPosition;
    /**
     * 主图url
     */
    private String mainPic;
    /**
     * 描述
     */
    private String desc;

    /**
     * 板块
     */
    private String banKuai;
    /**
     * 均价
     */
    private String avgPrice;

}
