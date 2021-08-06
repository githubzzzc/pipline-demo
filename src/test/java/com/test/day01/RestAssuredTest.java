package com.test.day01;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.io.File;

public class RestAssuredTest {

    @Test
    public void register(){
        //1、请求方式
        //2、请求地址
        //3、请求头字段
        //4、请求参数
        String jsonData="{\"mobile_phone\":\"13323231200\",\"pwd\":\"lemon123456\",\"type\":1}";
        RestAssured.
                given().
                    header("X-Lemonban-Media-Type","lemonban.v1").
                    header("Content-Type","application/json").
                    body(jsonData).
                when().
                    post("http://api.lemonban.com/futureloan/member/register").
                then().
                    log().all();
    }

    @Test
    public void getRequest(){
        RestAssured.
                given().
                header("X-Lemonban-Media-Type","lemonban.v1").
                header("Content-Type","application/json").
                queryParam("mobilephone","13323234545").
                queryParam("pwd","123456").
        when().
                get("http://httpbin.org/get").
        then().
                log().all();
    }

    @Test
    public void postRequest01(){
        //post请求（form表单传参方女士）
        RestAssured.
                given().
                    header("Content-Type","application/x-www-form-urlencoded").
                    body("mobilephone=1332323445&pwd=123456").
                when().
                    post("http://httpbin.org/post").
                then().
                    log().all();
    }

    @Test
    public void postRequest02(){
        //post请求（json传参）
        String jsonData="{\"mobile_phone\":\"13323231234\",\"pwd\":\"lemon123456\",\"type\":1}";
        RestAssured.
                given().
                    header("Content-Type","application/json").
                    body(jsonData).
                when().
                    post("http://httpbin.org/post").
                then().
                    log().all();
    }

    @Test
    public void postRequest03(){
        //post请求（xml传参）
        String xmlData="<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<suite>\n" +
                "    <class>测试xml</class>\n" +
                "</suite>";
        RestAssured.
                given().
                    header("Content-Type","application/xml").
                    body(xmlData).
                when().
                    post("http://httpbin.org/post").
                then().
                    log().all();
    }

    @Test
    public void postRequest04(){
        //post请求（传输大文件-上传文件）
        File file1 = new File("D:\\test.json");
        File file2 = new File("D:\\test.json");
        RestAssured.
                given().
                    header("Content-Type","multipart/form-data").
                    multiPart(file1).
                    multiPart(file2).
                when().
                    post("http://httpbin.org/post").
                then().
                    log().all();
    }
}
