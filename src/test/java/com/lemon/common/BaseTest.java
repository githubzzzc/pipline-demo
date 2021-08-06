package com.lemon.common;

import com.alibaba.fastjson.JSONObject;
import com.lemon.data.Environment;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.Constants;
import com.lemon.utils.JDBCUtil;
import io.qameta.allure.Allure;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.config.JsonConfig.jsonConfig;
import static io.restassured.path.json.config.JsonPathConfig.NumberReturnType.BIG_DECIMAL;

public class BaseTest {

    @BeforeSuite
    public void beforeSuite() throws FileNotFoundException {
        //把json小数的返回类型配置成BigDecimal类型
        RestAssured.config=RestAssured.config().jsonConfig(jsonConfig().numberReturnType(BIG_DECIMAL));
        //REST-assured基础 baseurl设置
        RestAssured.baseURI= Constants.BASE_URI;
        //全局日志配置：将所有日志保存到文件中（重定向）
        /*PrintStream fileOutPutStream = new PrintStream(new File("test_all.log"));
        RestAssured.filters(new RequestLoggingFilter(fileOutPutStream),new ResponseLoggingFilter(fileOutPutStream));*/
    }

    /**
     * 接口请求方法封装
     * @param caseInfo 请求数据（实体类）
     * @return 响应结果
     */
    public Response request(CaseInfo caseInfo){
        //给每个请求添加日志
        //long timestamp = System.currentTimeMillis();
        //创建文件夹
        String logFilepath="";
        if(!Constants.SHOW_CONSOLE_LOG) {
            File dirFile = new File("logs\\" + caseInfo.getInterfaceName());
            if (!dirFile.exists()) {
                dirFile.mkdirs();
            }
            PrintStream fileOutPutStream = null;
            //日志文件路径
            logFilepath = "logs\\" + caseInfo.getInterfaceName() + "\\" + caseInfo.getInterfaceName() + "_" + caseInfo.getCaseId() + ".log";
            try {
                fileOutPutStream = new PrintStream(new File(logFilepath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            RestAssured.config = RestAssured.config().logConfig(LogConfig.logConfig().defaultStream(fileOutPutStream));
        }
        //获取请求头
        String requestHeaders = caseInfo.getRequestHeader();
        Map rHMap = JSONObject.parseObject(requestHeaders);
        //获取接口请求地址
        String url = caseInfo.getUrl();
        //获取请求参数
        String params = caseInfo.getInputParams();
        //获取请求方式
        String method = caseInfo.getMethod();

        //respsonse定义
        Response res = null;
        //对不同的请求方式做封装（get/post/patch/put/delete）
        if("get".equalsIgnoreCase(method)){
            res = RestAssured.given().log().all().headers(rHMap).when().get(url).then().log().all().extract().response();
        }else if("post".equalsIgnoreCase(method)){
            res = RestAssured.given().log().all().headers(rHMap).body(params).when().post(url).then().log().all().extract().response();
        }else if("patch".equalsIgnoreCase(method)){
            res = RestAssured.given().log().all().headers(rHMap).body(params).when().patch(url).then().log().all().extract().response();
        }else if("put".equalsIgnoreCase(method)){
            res = RestAssured.given().log().all().headers(rHMap).body(params).when().put(url).then().log().all().extract().response();
        }

        //这里执行添加XX信息到日志文件中（logFilepath）
        //请求结束之后将接口日志添加到allure报表中
        if(!Constants.SHOW_CONSOLE_LOG) {
            try {
                Allure.addAttachment("接口请求响应日志", new FileInputStream(logFilepath));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        return res;
    }

    /**
     * 响应断言
     * @param res 实际响应结果
     * @param caseInfo 请求数据（实体类）
     */
    public void assertResponse(Response res,CaseInfo caseInfo){
        String expected = caseInfo.getExpected();
        if(expected != null) {
            //转成Map
            Map<String, Object> expectedMap = JSONObject.parseObject(expected);
            Set<String> allKeySet = expectedMap.keySet();
            for (String key : allKeySet) {
                //获取实际响应结果
                Object actualResult = res.jsonPath().get(key);
                //获取期望结果
                Object expectedResult = expectedMap.get(key);
                Assert.assertEquals(actualResult, expectedResult);
            }
        }
    }

    /**
     * 数据库断言统一封装
     * @param caseInfo 用例数据
     */
    public void assertDB(CaseInfo caseInfo){
        String dbAssertInfo = caseInfo.getDbAssert();
        if(dbAssertInfo != null) {
            Map<String, Object> mapDbAssert = JSONObject.parseObject(dbAssertInfo);
            Set<String> allKeys = mapDbAssert.keySet();
            for (String key : allKeys) {
                //key为对应要执行的sql语句
                Object dbActual = JDBCUtil.querySingleData(key);
                //根据数据库中读取实际返回类型做判断
                //1、Long类型
                if(dbActual instanceof Long){
                    Integer dbExpected = (Integer) mapDbAssert.get(key);
                    Long expected = dbExpected.longValue();
                    Assert.assertEquals(dbActual, expected);
                }else {
                    Object expected = mapDbAssert.get(key);
                    Assert.assertEquals(dbActual, expected);
                }
            }
        }
    }

    /**
     * 通过【提取表达式】将对应响应值保存到环境变量中
     * @param res 响应信息
     * @param caseInfo 实体类对象
     */
    public void extractToEnvironment(Response res,CaseInfo caseInfo){
        String extractStr = caseInfo.getExtractExper();
        if(extractStr != null) {
            //把提取表达式转成Map
            Map<String, Object> map = JSONObject.parseObject(extractStr);
            Set<String> allKeySets = map.keySet();
            for (String key : allKeySets) {
                //key为变量名，value是为提取的gpath表达式
                Object value = map.get(key);
                Object actualValue = res.jsonPath().get((String) value);
                //将对应的键和值保存到环境变量中
                Environment.envMap.put(key, actualValue);
            }
        }
    }

    /**
     * 正则替换功能，比如：
     * 原始字符串 {
     *   "mobile_phone": "{{mobile_phone}}"
     * }
     * 替换为
     * {
     *   "mobile_phone": "13323234545"
     * }
     * 13323234545是为环境变量中mobile_phone变量名对应的变量值
     * @param orgStr
     * @return
     */
    public String regexReplace(String orgStr){
        if(orgStr != null) {
            //匹配器
            Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
            //匹配对象
            Matcher matcher = pattern.matcher(orgStr);
            String result = orgStr;
            //循环遍历匹配对象
            while (matcher.find()) {
                //获取整个匹配正则的字符串 {{mobile_phone}}
                String allFindStr = matcher.group(0);
                //找到{{XXX}}内部的匹配的字符串 mobile_phone
                String innerStr = matcher.group(1);
                //找到手机号码：13323234545
                //具体的要替换的值（从环境变量中去找到的）
                Object replaceValue = Environment.envMap.get(innerStr);
                //要替换{{mobile_phone}} --> 13323234545
                //第二次替换的时候应该是基于第一次替换的结果的基础上再去替换
                result = result.replace(allFindStr, replaceValue + "");
            }
            return result;
        }else{
            return orgStr;
        }
    }

    /**
     * 整条用例数据的参数化替换，只要在对应的用例数据里面有{{XXXX}},那么就会从环境变量中找XXXX，如果找到的话就去替换，否则不会
     * @param caseInfo
     */
    public CaseInfo paramsReplace(CaseInfo caseInfo){
        //1、请求头
        String requestHeader = caseInfo.getRequestHeader();
        caseInfo.setRequestHeader(regexReplace(requestHeader));
        //2、接口地址
        String url = caseInfo.getUrl();
        caseInfo.setUrl(regexReplace(url));
        //3、参数输入
        String inputParams = caseInfo.getInputParams();
        caseInfo.setInputParams(regexReplace(inputParams));
        //4、期望结果
        String expected = caseInfo.getExpected();
        caseInfo.setExpected(regexReplace(expected));
        return caseInfo;
    }
}
