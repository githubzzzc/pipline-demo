package com.test.day01;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.File;

public class GetResponse {

    @Test
    public void getJsonData(){
        //1、请求方式
        //2、请求地址
        //3、请求头字段
        //4、请求参数
        String jsonData="{\"mobile_phone\":\"13323231219\",\"pwd\":\"lemon123456\",\"type\":1}";

        Response res = RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v1").
                    header("Content-Type","application/json").
                    body(jsonData).
                when().
                    post("http://api.lemonban.com/futureloan/member/register").
                then().
                    log().all().
                    extract().response();
        //获取http状态码
        System.out.println(res.statusCode());
        //获取请求的响应时间(单位ms)
        System.out.println(res.time());
        //获取响应头某一个字段对应的值
        System.out.println(res.getHeader("Content-Type"));
        //获取接口响应体里面的数据
        //通过json路径表达式获取到json某一个节点的值
        Object userId = res.jsonPath().get("data.id");
        System.out.println("用户id为："+userId);
        Object msg = res.jsonPath().get("msg");
        System.out.println("msg为："+msg);
    }

    @Test
    public void getHtmlData(){
        Response res = RestAssured.
                given().
                when().
                    get("http://www.baidu.com").
                then().
                    log().all().
                    extract().response();
        //获取百度title标签
        //获取标签的文本值
        String titleStr = res.htmlPath().get("html.head.title");
        System.out.println(titleStr);
        //获取标签的属性值
        String str = res.htmlPath().get("html.head.meta[0].@content");
        System.out.println(str);
    }

    @Test
    public void getXmlData(){
        Response res = RestAssured.
                given().
                when().
                    get("http://www.httpbin.org/xml").
                then().
                    log().all().
                    extract().response();
        String str = res.xmlPath().get("slideshow.slide[1].@type");
        System.out.println(str);
    }


    @Test
    public void registerLogin(){
        //================发起注册接口请求====================
        String jsonData="{\"mobile_phone\":\"13323231329\",\"pwd\":\"lemon123456\",\"type\":1}";
        Response res = RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v1").
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
        String jsonData2="{\"mobile_phone\":\""+mobilephone+"\",\"pwd\":\"lemon123456\"}";
        RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v1").
                    header("Content-Type","application/json").
                    body(jsonData2).
                when().
                    post("http://api.lemonban.com/futureloan/member/login").
                then().
                    log().all().
                    extract().response();

    }

    @Test
    public void authorTest(){

        //================发起登录接口请求====================
        String jsonData2="{\"mobile_phone\":\"13323231329\",\"pwd\":\"lemon123456\"}";
        Response res = RestAssured.
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
        Object tokenValue = res.jsonPath().get("data.token_info.token");
        //获取user id
        Object userId = res.jsonPath().get("data.id");

        //================发起充值接口请求====================
        String jsonData="{\"member_id\":\""+userId+"\",\"amount\":10000.0}";
        Response res2 = RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v2").
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

/*    public static void main(String[] args) {
        Object mobilephone ="13323234545";
        String jsonData2="{\"mobile_phone\":"+mobilephone+",\"pwd\":\"lemon123456\"}";
        System.out.println(jsonData2);
    }*/


}
