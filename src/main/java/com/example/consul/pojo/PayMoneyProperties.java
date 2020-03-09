package com.example.consul.pojo;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * @Author Mr.Kong
 * @Description TODO
 * @Date 2020-03-07 21:44
 */
@ConfigurationProperties(prefix = "company.pay")
@RefreshScope
@Data
@Component
public class PayMoneyProperties {

    Integer money;

}
