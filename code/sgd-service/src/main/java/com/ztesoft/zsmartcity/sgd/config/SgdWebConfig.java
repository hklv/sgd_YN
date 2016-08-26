package com.ztesoft.zsmartcity.sgd.config;

import lodsve.mvc.annotation.EnableLodsveMvc;
import lodsve.security.config.SecurityConfig;
import lodsve.springfox.annotations.EnableSpringFox;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.web.config.EnableSpringDataWebSupport;

/**
 * .
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version 1.0 16/6/30 下午1:44
 */
@Configuration
@EnableSpringFox(prefix = "/rest")
@EnableSpringDataWebSupport
@EnableLodsveMvc
@ComponentScan("com.ztesoft.zsmartcity.sgd.*.rest")
@Import(SecurityConfig.class)
@ImportResource({"classpath*:/META-INF/springWeb/*.xml"})
public class SgdWebConfig {
}
