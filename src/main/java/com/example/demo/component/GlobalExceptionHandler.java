package com.example.demo.component;

import cn.hutool.core.util.IdUtil;
import com.example.demo.entity.global.IResult;
import com.example.demo.entity.global.JsonResult;
import com.example.demo.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private RedisUtil redisUtil;
    @Value("${spring.application.name}")
    private String applicationName;

    public static String retrieveClientIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    /**
     * 全局异常处理
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseEntity handleException(HttpServletRequest httpServletRequest, Exception e) {
        JsonResult jsonResult = new JsonResult();

        //到队列，记录异常
        String uniqueId = httpServletRequest.getHeader("logid");
        if (StringUtils.isBlank(uniqueId)) {
            uniqueId = IdUtil.simpleUUID();
        }

        Map<String, Object> map = new HashMap<>();

        map.put("ip", retrieveClientIp(httpServletRequest));
        map.put("methodName", httpServletRequest.getMethod());
        map.put("port", httpServletRequest.getServerPort());
        map.put("project", applicationName);
        map.put("exceptionType", e.getClass());
        map.put("errorMsg", e.getMessage());
        map.put("trace", e.getStackTrace());
        map.put("url", httpServletRequest.getRequestURL());
        String accessToken = httpServletRequest.getHeader("Authorization");
        map.put("accessToken", accessToken == null ? "" : accessToken);
        map.put("requestid", jsonResult.requestId);

        redisUtil.setCacheMap("errorLog:" + uniqueId, map, 300);

        log.error("Exception: {}", e.toString(), e);

        jsonResult.code = INTERNAL_SERVER_ERROR.value();
        jsonResult.data = "ErrorLogId:" + uniqueId;
        jsonResult.message = e.getMessage();
        return new ResponseEntity<IResult>(jsonResult, INTERNAL_SERVER_ERROR);
    }

    /**
     * 在controller里面内容执行之前，校验一些参数不匹配，Get post方法不对之类的
     */
    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
        System.out.println("请求参数或者请求方式错误:");
        ex.printStackTrace();

        return super.handleExceptionInternal(ex, JsonResult.error(status.value(), "请求参数或者请求方式错误",
                status.getReasonPhrase()), headers, status, request);
    }
}

