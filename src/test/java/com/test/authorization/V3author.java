package com.test.authorization;

import com.lemon.encryption.RSAManager;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class V3author {

    @Test
    public void testAuthor() throws Exception {
        //================发起登录接口请求====================
        String jsonData2="{\"mobile_phone\":\"13323231329\",\"pwd\":\"lemon123456\"}";
        Response res = RestAssured.
                given().log().all().
                header("X-Lemonban-Media-Type","lemonban.v3").
                header("Content-Type","application/json").
                body(jsonData2).
                when().
                post("http://api.lemonban.com/futureloan/member/login").
                then().
                log().all().
                extract().response();
        //获取token值
        String tokenValue = (String)res.jsonPath().get("data.token_info.token");
        //获取user id
        Object userId = res.jsonPath().get("data.id");

        //================发起充值接口请求====================
        //得到时间戳timestamp(接口要求是：秒级的时间戳)
        long timestamp = System.currentTimeMillis()/1000;
        //sign参数获取（取 token 前 50 位再拼接上 timestamp 值，然后通过 RSA 公钥加密得到字符串）
        String token_50 = tokenValue.substring(0,50);
        String str = token_50+timestamp;
        //加密代码--》找开发去获取-->一般是一个jar包
        //使用加密包里面加密算法（RSA）
        String encryptStr = RSAManager.encryptWithBase64(str);


        String jsonData="{\"member_id\": "+userId+",\"amount\":1000.0,\"timestamp\":"+timestamp+",\"sign\":\""+encryptStr+"\"}";
        Response res2 = RestAssured.
                given().log().all().
                header("X-Lemonban-Media-Type","lemonban.v3").
                header("Content-Type","application/json").
                contentType("application/json").
                header("Authorization","Bearer "+tokenValue).
                body(jsonData).
                when().
                post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                log().all().
                extract().response();
    }

    public static void main(String[] args) throws Exception {
        /*String token="eyJhbGciOiJIUzUxMiJ9.eyJtZW1iZXJfaWQiOjI1NjY1LCJleHAiOjE2MjczMDE5MDN9.N1zGUYeKQu479zzD8YlUlZaj9V6k5W4jyq0Sxwt6D6fUX-d91pKL4eUFjcwYZukxVjTQqJO8gMR5f-LejD7AVA\"";
        String result = token.substring(0,50);
        System.out.println(result);*/
        System.out.println(RSAManager.encryptWithBase64("123456"));
    }
}
