package com.example.demo.controller.wxwork;


import com.example.demo.component.BaseController;
import com.example.demo.entity.global.IResult;
import com.example.demo.entity.global.JsonResult;
import com.example.demo.service.wxwork.WXWorkService;
import com.example.demo.util.wxwork.AesException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@Slf4j
@Api(tags = {"企业微信的相关接口",})
@RestController
@RequestMapping("/WXWork")
public class WXWorkController extends BaseController {

    ///region 服务注入
    @Autowired
    private WXWorkService wxWorkService;
    //endregion


    @ApiOperation(value = "通讯录变动回调验证接口", httpMethod = "GET", response = JsonResult.class, notes = "")
    @GetMapping(value = "/")
    public String Get(@RequestParam("msg_signature") String msg_signature, @RequestParam("timestamp") String timestamp,
                      @RequestParam("nonce") String nonce, @RequestParam("echostr") String echostr) throws AesException {
        //msg_signature：企业微信加密签名，msg_signature计算结合了企业填写的token、请求中的timestamp、nonce、加密的消息体
        //timestamp,nonce：时间戳和随机数，防止请求重放攻击
        //echostr：加密的字符串，解密可得到明文信息
        log.info("【传入的 msg_signature】：" + msg_signature);
        log.info("【传入的 timestamp】：" + timestamp);
        log.info("【传入的 nonce】：" + nonce);
        log.info("【传入的 echostr】：" + echostr);
        //验证成功返回明文
        return wxWorkService.checkCallback(msg_signature, timestamp, nonce, echostr);
    }

    @ApiOperation(value = "通讯录变动回调服务", httpMethod = "POST", response = JsonResult.class, notes = "")
    @PostMapping(value = "/")
    public IResult Post(@RequestParam("msg_signature") String msg_signature, @RequestParam("timestamp") String timestamp,
                        @RequestParam("nonce") String nonce, @RequestBody(required = false) String postData) throws AesException {

        log.info("【传入的 msg_signature】：" + msg_signature);
        log.info("【传入的 timestamp】：" + timestamp);
        log.info("【传入的 nonce】：" + nonce);
        log.info("【传入的 postData】：" + postData);
        //验证成功返回明文
        return wxWorkService.processCallback(msg_signature, timestamp, nonce, postData);
        //变动回调功能暂不启用
        //return JsonResult.success("回调成功");
    }


    @ApiOperation(value = "异步任务完成通知", httpMethod = "POST", response = JsonResult.class, notes = "")
    @PostMapping(value = "/batchJobCallback")
    public IResult batchJobCallback(@RequestParam("msg_signature") String msg_signature, @RequestParam("timestamp") String timestamp,
                                    @RequestParam("nonce") String nonce, @RequestBody(required = false) String postData) throws AesException {

        log.info("【传入的 msg_signature】：" + msg_signature);
        log.info("【传入的 timestamp】：" + timestamp);
        log.info("【传入的 nonce】：" + nonce);
        log.info("【传入的 postData】：" + postData);

        return wxWorkService.batchJobCallback(msg_signature, timestamp, nonce, postData);
    }


    @ApiOperation(value = "获取指定企微id的账号", httpMethod = "GET", response = JsonResult.class, notes = "")
    @GetMapping(value = "getOneById")
    public IResult getOneById(@RequestParam("id") String id) throws WxErrorException {
        if (StringUtils.isBlank(id)) {
            return JsonResult.error("参数缺失");
        }
        //获取指定用户并返回
        return wxWorkService.getOneById(id);
    }

}
