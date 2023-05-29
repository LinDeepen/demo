package com.example.demo.config;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConfig;
import com.alipay.api.DefaultAlipayClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@Lazy
public class AliPayConfig {

    //私钥、公钥、appId
    private String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCXX6BXbxPMDJPtMxycW+4N5RjgKedcLA9oAw1JX+iY7DI/DsJHe8tjodRsS0s8wX8UuG/ipnTWGsYIZ7epYVWocxJj9LxlN57lpoxTzoJ86zLDAki3SxYQeUUd4COCbCXrrhtj6pcHn1GXg8zwlhFWeNAn2yiTAft839z/oJPuNzEO1D/MaWdu8V4Mblc3vIY6QddTNW+cC2Ct6pizu8Q7HO7YezDVJk3PraBZOGjlegiPvjSyth8lZlORz/lMb53BAOHDleb8/wTyDrJDEkEX47h5gHZlRNhDn4tRy+XoHpA3leEaoBjmvZwfIhibYKrscjpMn5yuKx6N6PeoNasjAgMBAAECggEAfdZIBAdaDMnz+z7/LNibWD+4YzlSlOQn1o6a8AFj6iSITLVnOmemF+hDH9ej7xjoHQCKOn+oJwjQ0dHu80bwH32xrxiXuQj49e5zP4QwI8qeQ1DVxFIOQJmfpItc2aIwD/GH4ZL2BKcj+/HcX7BRL3J1kHx3CWafexrarEMAVwmNopHJk/MYIcX98uvYrARJwGr/vsP9lrsXy1cJwVx24dMNG7r/+guhiliDVnWINwjFMlikDyP6AjePPXhonDfFKDJiAOFE/GLufuoxuTfwjU6fG2iiUZcyPddYV6pTEzvDekuEqUWQ/54pYu9xaiPFjBNCmEnb7rYpXJFaTLSFKQKBgQDk14KoKd1pcYWyD82ZoXFfgYWlfgiYLBFNIuTwni/QVyayzQfYHobOuGnhjiVWbl/eAYudyzWUx1GAbk9KBnYJ2BmFYbmiQFXq8OICMgASDYphM64pFkqx+lA9d0vgC+R3sPai5UgP9JQwML3TfgBSb+zvo031bsn/O4dkfF/CrwKBgQCpVok/+7Dh+c1HREEES+t21/0Ur8cpI2QmtmTC9vEavoRJcTklw10R+5yHC7oiFhzR7K3K15UFsYy4KYhyZEHn7Q9KgtwCvO74ST4qJEzqjF3Iu12GzF6Di4Oq6jb4dNfCRdkRVI65WJBZxjZZz4PS3p7Gdw67EcpUfuPtY7rLzQKBgQDYfq5ES38Fj3mNcHfxHRP158I5Q5f75szjbdyrauwPLTQWOfRu2MpubXb2BZV7RGZ70Hh2atYhD8QiPbnTbXNkcsYKMt79CeJ+py8aJpV8fsTomozsoANAh3A8Bk2JA5c8SPW8PC/FsHw1bxdXK0BAvVSpjZRmWk7NoVLAlQ2/TwKBgHLtX2NEcA0+W9r1cOb4m8th1cr3bcT21x8LBSHpNUPAb8nkhTOi/wSYbuicxewic9bpuh4D6IkfsnC/B3MTpkNA+fLJE4bsMYfjNcSmNGmvcGsu9s/o3nCK/IbARSSlMFYSz7raV69lczrqsrKLi11PaBUscpv1/AsSv6C64ek1AoGAKJS9ZUvvKmbKvut+AvCcSkUe3Nr9YU8OdZqrAVoDn/E1kPMSn08hFj9AyrqPzRj5mBcpjOiWTHuCItRTLPY1VO49w2iRWmCfdG+YHCFkKr236MtMn51rbOuMXnRpe1cYB7mHCBpoHOT7BsPj4bRpJYO5TzCBinvsQ2cxSHEiNLQ=";
    private String alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnFPlpvRPTiq+xv0k7BoKknJHzjL+dt5SyfHh/4n1Pb6+a1YxbREqUCmY4lqx1N8+nAj18lhVT6CFd+CIEHHUtev/bbDcLeEsNTIGs9Q/e08mZFO+CUngEi+BYNyV3ifqvz/DRy7XvlaplmmsIXbXSLZC47aPvR/ZMqFQE+uBezJDdjbOk4PPOX3XOikuNcy4Ayi8ds0yAccs2C2ywPunkWnNV/zAP/7Z5Jx9lQ6Caldn4XQnCaH9KSuqBhNco1Scl6FZ948byj0qalnjg0u7Fz+fvqtL5/W1OtwwVzBz/qeGXrIDkD8I1W8EGEtzoIaZAm3W5Qa3KSVmxiaZB6bYrwIDAQAB";
    private String appId = "2021003167625554";

    /**
     * 获取阿里接口的客户端
     *
     * @return
     */
    public AlipayClient getClient() throws AlipayApiException {

        //配置信息
        AlipayConfig alipayConfig = new AlipayConfig();
        alipayConfig.setServerUrl("https://openapi.alipay.com/gateway.do");
        alipayConfig.setAppId(appId);
        alipayConfig.setPrivateKey(privateKey);
        alipayConfig.setAlipayPublicKey(alipayPublicKey);
        alipayConfig.setFormat("json");
        alipayConfig.setCharset("UTF8");
        alipayConfig.setSignType("RSA2");
        return new DefaultAlipayClient(alipayConfig);
    }
}
