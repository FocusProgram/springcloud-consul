package com.example.consul.controller;

import com.example.consul.pojo.PayMoneyProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author Mr.Kong
 * @Description TODO
 * @Date 2020-03-07 21:49
 */
@RestController
@RequestMapping("consul")
@RefreshScope
public class ConsulConfigController {

    //第一种注入值的方法
    @Value("${company.pay.money}")
    private String money;

    //第二种注入值的方法
    @Autowired
    private PayMoneyProperties payMoneyProperties;

    @RequestMapping("/pay/money")
    public Object getConfig() {
        String result = "第一种注入值获取的值为：" + money + ",第二种注入值获取的值为：" + payMoneyProperties.getMoney();
        return result;
    }

}
