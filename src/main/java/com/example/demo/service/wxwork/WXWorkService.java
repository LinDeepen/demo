package com.example.demo.service.wxwork;

import com.alibaba.fastjson.JSONObject;
import com.alipay.easysdk.kernel.util.JsonUtil;
import com.example.demo.entity.global.IResult;
import com.example.demo.entity.global.JsonResult;
import com.example.demo.util.wxwork.AesException;
import com.example.demo.util.wxwork.WXBizMsgCrypt;
import com.example.demo.util.wxwork.XMLParse;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.*;


/**
 * 企业微信的相关业务处理
 */
@Slf4j
@Service
public class WXWorkService {

    //region 服务注入
    @Autowired
    @Qualifier("contactsService")
    private WxCpService contactsService;//企微通讯录服务

    @Autowired
    @Qualifier("customerRelationsService")
    private WxCpService customerRelationsService;//企微通讯录服务
    //endregion

    //企微公司主体ID
    private String corpId = "ww9ea411c0a26472ba";
    //通讯录回调token
    private String contactsToken = "4VumAEQ7fhbTCBxnz8";
    //通讯录回调AesKey
    private String contactsEncodingAesKey = "BTFGPdW1bzlSxtSWvsViY1oLeyTTgdOOLUAP9x2smpr";

    /**
     * 回调接口验证，成功返回明文
     *
     * @param msgSignature 企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体
     * @param timestamp    时间戳，防止请求重放攻击
     * @param nonce        随机数，防止请求重放攻击
     * @param echoStr      加密的字符串，解密可得到明文信息
     * @return
     * @throws AesException
     */
    public String checkCallback(String msgSignature, String timestamp, String nonce, String echoStr) throws AesException {

        //根据配置构建实例
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(contactsToken, contactsEncodingAesKey, corpId);
		/*
		------------使用示例一：验证回调URL---------------
		*企业开启回调模式时，企业微信会向验证url发送一个get请求
		假设点击验证时，企业收到类似请求：
		* GET /cgi-bin/wxpush?msg_signature=5c45ff5e21c57e6ad56bac8758b79b1d9ac89fd3&timestamp=1409659589&nonce=263014780&echostr=P9nAzCzyDtyTWESHep1vC5X9xho%2FqYX3Zpb4yKa9SKld1DsH3Iyt3tP3zNdtp%2B4RPcs8TgAE7OaBO%2BFZXvnaqQ%3D%3D
		* HTTP/1.1 Host: qy.weixin.qq.com

		接收到该请求时，企业应		1.解析出Get请求的参数，包括消息体签名(msg_signature)，时间戳(timestamp)，随机数字串(nonce)以及企业微信推送过来的随机加密字符串(echostr),
		这一步注意作URL解码。
		2.验证消息体签名的正确性
		3. 解密出echostr原文，将原文当作Get请求的response，返回给企业微信
		第2，3步可以用企业微信提供的库函数VerifyURL来实现。
		*/

        // 解析出url上的参数值如下：(框架解析参数时，已自动转码)
//        String sVerifyMsgSig = URLDecoder.decode(msg_signature, "utf-8");
//        String sVerifyTimeStamp = URLDecoder.decode(timestamp, "utf-8");
//        String sVerifyNonce = URLDecoder.decode(nonce, "utf-8");
//        String sVerifyEchoStr = URLDecoder.decode(echostr, "utf-8");
        String sEchoStr; //需要返回的明文
        try {
            // 验证URL成功，将sEchoStr返回
            sEchoStr = wxcpt.VerifyURL(msgSignature, timestamp, nonce, echoStr);
            System.out.println("解密后的明文 sEchoStr: " + sEchoStr);
            return sEchoStr;
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 通讯录变动回调服务
     *
     * @param msg_signature 企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体
     * @param timestamp     时间戳，防止请求重放攻击
     * @param nonce         随机数，防止请求重放攻击
     * @param postData      加密的请求数据，解密可得到明文信息
     * @return
     */
    public IResult processCallback(String msg_signature, String timestamp, String nonce, String postData) throws AesException {

        //根据配置构建实例
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(contactsToken, contactsEncodingAesKey, corpId);

        // 回调的key值
        String sEchoStr;
        try {
            //检验消息，解密获取明文
            sEchoStr = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);
            System.out.println("解密后的明文 sEchoStr: " + sEchoStr);
            Map<String, String> map = XMLParse.readStringXml(sEchoStr);
            System.out.println("转换后的明文 map：" + map.toString());

            //处理成json字符
            String json = JsonUtil.toJsonString(map);
            System.out.println(json);
            // TODO: 2023/5/26   发送队列处理回调的相关业务

            // 验证URL成功，将sEchoStr返回
            return JsonResult.success("回调成功", map);
        } catch (Exception e) {
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }
        return JsonResult.error("回调失败");
    }

    /**
     * 批量任务回调服务
     *
     * @param msg_signature 企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体
     * @param timestamp     时间戳，防止请求重放攻击
     * @param nonce         随机数，防止请求重放攻击
     * @param postData      加密的请求数据，解密可得到明文信息
     * @return
     * @throws AesException
     */
    public IResult batchJobCallback(String msg_signature, String timestamp, String nonce, String postData) throws AesException {
        //根据配置构建实例
        WXBizMsgCrypt wxcpt = new WXBizMsgCrypt(contactsToken, contactsEncodingAesKey, corpId);

        // 回调的key值
        String sEchoStr;

        try {
            //检验消息，解密获取明文
            sEchoStr = wxcpt.DecryptMsg(msg_signature, timestamp, nonce, postData);
            log.info("【解密后的明文 sEchoStr】: " + sEchoStr);
            Map map = XMLParse.xmlToMap(sEchoStr);
            log.info("【转换后的明文 map】：" + JsonUtil.toJsonString(map));

            JSONObject json = new JSONObject(map);
            String batchJob = json.getString("BatchJob");
            String str = batchJob.substring(1, batchJob.length() - 1); //去除{}
            Map<String, String> strMap = XMLParse.getStringToMap(str);

            if (strMap != null && "0".equals(strMap.get(" ErrCode"))) {

                log.info("【企业微信覆盖数据执行成功】" + map.toString());
                return JsonResult.success("【企业微信覆盖数据执行成功】", map);
            }

        } catch (Exception e) {
            log.info("【企业微信覆盖数据执行失败】");
            //验证URL失败，错误原因请查看异常
            e.printStackTrace();
        }

        return JsonResult.success("【企业微信覆盖数据执行成功】");
    }

    public IResult getOneById(String id) throws WxErrorException {
        WxCpUser wxCpUser = contactsService.getUserService().getById(id);
        return JsonResult.success("获取成功", wxCpUser);
    }
}
