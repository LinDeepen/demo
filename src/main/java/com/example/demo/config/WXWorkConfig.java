package com.example.demo.config;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.api.impl.WxCpServiceImpl;
import me.chanjar.weixin.cp.config.impl.WxCpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Slf4j
@Configuration
@ConditionalOnClass(WxCpService.class)
@NoArgsConstructor
public class WXWorkConfig {

    //企微公司主体ID
    private String corpId = "ww9ea411c0a26472ba";
    //通讯录秘钥
    private String contactsSecret = "mB8o-3L-o7ZBJLkMmkdxuRZ5Oe56Jlf7D5nWO1HUcN4";
    //客户关系秘钥
    private String customerRelationsSecret = "zPjxRSj6ASCNCC2erUCXIVA0-j--Qk5mOK1sXwVHblw";

    /**
     * 创建企微skd-调用Bean对象
     * 企微通讯录服务
     */
    @ConditionalOnMissingBean
    @Bean(name = "contactsService")
    @Qualifier("contactsService")
    public WxCpService initContactsService() {
        //设置通讯录配置
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(corpId);
        config.setCorpSecret(contactsSecret);

        WxCpService wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

    /**
     * 创建企微skd-调用Bean对象
     * 客户关系
     */
    @Bean(name = "customerRelationsService")
    @Qualifier("customerRelationsService")
    @Primary
    public WxCpService initCustomerRelationsService() {
        //设置客户关系配置
        WxCpDefaultConfigImpl config = new WxCpDefaultConfigImpl();
        config.setCorpId(corpId);
        config.setCorpSecret(customerRelationsSecret);

        WxCpService wxCpService = new WxCpServiceImpl();
        wxCpService.setWxCpConfigStorage(config);
        return wxCpService;
    }

}