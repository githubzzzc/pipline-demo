package com.test.day01;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

public class HomeWork_20210707 {


    @Test
    public void rechargeFlow(){
        //================发起注册接口请求====================
        String jsonData="{\"mobile_phone\":\"13323231331\",\"pwd\":\"lemon123456\",\"type\":1}";
        Response res = RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v2").
                    header("Content-Type","application/json").
                    body(jsonData).
                when().
                    post("http://api.lemonban.com/futureloan/member/register").
                then().
                    log().all().
                    extract().response();
        //从注册响应结果里面取到成功注册的手机号
        Object mobilephone = res.jsonPath().get("data.mobile_phone");

        //================发起登录接口请求====================
        String jsonData2="{\"mobile_phone\":\"13323231329\",\"pwd\":\"lemon123456\"}";
        Response res2 = RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v2").
                    header("Content-Type","application/json").
                    body(jsonData2).
                when().
                    post("http://api.lemonban.com/futureloan/member/login").
                then().
                    log().all().
                    extract().response();
        //获取token值
        Object tokenValue = res2.jsonPath().get("data.token_info.token");
        //获取user id
        Object userId = res2.jsonPath().get("data.id");

        //================发起充值接口请求====================
        String jsonData3="{\"member_id\":\""+userId+"\",\"amount\":10000.0}";
        RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v2").
                    header("Content-Type","application/json").
                    header("Authorization","Bearer "+tokenValue).
                    body(jsonData3).
                when().
                    post("http://api.lemonban.com/futureloan/member/recharge").
                then().
                    log().all();
    }

}
