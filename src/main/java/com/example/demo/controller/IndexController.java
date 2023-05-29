package com.example.demo.controller;

import com.example.demo.component.BaseController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.InetAddress;
import java.net.UnknownHostException;

@RestController
public class IndexController extends BaseController {

    @Value("${spring.application.name}")
    private String applicationName;


    public String getLocalIp() {
        try {
            InetAddress address = InetAddress.getLocalHost();
            return address.getHostAddress(); //返回IP地址
        } catch (UnknownHostException e) {
            return null;
        }
    }

    @GetMapping("/")
    public String home() {
        response.addHeader("stags", getLocalIp());
        return applicationName;
    }
}
