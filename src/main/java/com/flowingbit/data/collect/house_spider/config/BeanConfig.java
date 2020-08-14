package com.flowingbit.data.collect.house_spider.config;

import com.flowingbit.data.collect.house_spider.dao.HouseDao;
import com.flowingbit.data.collect.house_spider.dao.RedisDAO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfig {

//    @Bean
//    public HouseDao houseDao(){
//        HouseDao houseDao = new HouseDao();
//        return houseDao;
//    }

    @Bean
    public RedisDAO redisDAO(){
        RedisDAO redisDAO = new RedisDAO();
        return redisDAO;
    }


}
