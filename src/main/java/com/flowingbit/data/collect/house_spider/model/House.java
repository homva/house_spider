package com.flowingbit.data.collect.house_spider.model;


import lombok.Data;

import java.io.Serializable;
import java.util.Date;
/**
 * @author Lyon
 * @date 2019/4/25 18:42
 * @description House
 **/
@Data
public class House implements Serializable {
    private String id;
    private String title;
    private String url;
    private String city;
    private String region;
    private String street;
    private String community;
    private String floor;
    private Double totalPrice;
    private Double averagePrice;
    private String image;
    private Integer watch;
    private Integer view;
    private String releaseDate;
    private String roomCount;
    private String towards;
    private Double houseArea;
    private String decoration;
    private Integer houseAge;
    private Date createDate;

    private String huanXian;    //环线
    private String tihuRate;    //梯户比例
    private String chanQuan;    //产权
    private String shuiFei;     //税费
    private String jiaoTong;    //交通
    private String guaPaiTime;  //挂牌时间，格式yyyy-MM-dd
    private String sellMsg;     //核心卖点

}
