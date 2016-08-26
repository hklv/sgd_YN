package com.ztesoft.zsmartcity.sgd.config;

import lodsve.mybatis.configs.annotations.EnableMyBatis;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午1:44
 */
@Configuration
@EnableMyBatis(
        dataSource = "sgd",
        basePackages = "com.ztesoft.zsmartcity.sgd.*.repository",
        enumsLocations = "com.ztesoft.zsmartcity.sgd.*.domain.enums"
)
@ComponentScan({
        "com.ztesoft.zsmartcity.sgd.*.service",
        "com.ztesoft.zsmartcity.sgd.*.repository",
        "com.ztesoft.zsmartcity.sgd.security",
})
@ImportResource("classpath*:/META-INF/spring/*.xml")
public class SgdConfig {
}
