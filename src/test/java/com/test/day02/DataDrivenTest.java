package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.alibaba.fastjson.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DataDrivenTest {


    @Test(dataProvider = "getRegisterDatas")
    public void register(CaseInfo caseInfo){
        //json字符串转Map
        String requestHeaders = caseInfo.getRequestHeader();
        Map requestHeaderMap = JSONObject.parseObject(requestHeaders);
        //获取接口请求地址
        String url = caseInfo.getUrl();
        //获取请求参数
        String params = caseInfo.getInputParams();
        Response res = RestAssured.
                given().
                    headers(requestHeaderMap).
                    body(params).
                when().
                    post("http://api.lemonban.com/futureloan"+url).
                then().
                    log().all().extract().response();
        //断言？？？
        //从表格数据里面取出来期望响应结果数据（json格式，键名：Gpath表达式，键值：期望结果）
        String expected = caseInfo.getExpected();
        //转成Map
        Map<String,Object> expectedMap = JSONObject.parseObject(expected);
        Set<String> allKeySet = expectedMap.keySet();
        for (String key: allKeySet){
            //key就是我们对应的Gpath表达式
            //获取实际响应结果
            Object actualResult = res.jsonPath().get(key);
            //获取期望结果
            Object expectedResult = expectedMap.get(key);
            Assert.assertEquals(actualResult,expectedResult);

        }
        /*//1、断言code业务码
        int actualCode = res.jsonPath().get("code");
        Assert.assertEquals(actualCode,0);
        //2、断言msg
        String msg = res.jsonPath().get("msg");
        Assert.assertEquals(msg,"OK");
        //3、断言reg_name
        String regName =res.jsonPath().get("data.reg_name");
        Assert.assertEquals(regName,"小柠檬");
        //4、断言mobile_phone
        String mobilePhone = res.jsonPath().get("data.mobile_phone");
        Assert.assertEquals(mobilePhone,"13323234545");*/
        //1、通过Easy POI技术读取用例数据
        //2、通过TestNG DataPprovider实现数据驱动测试
        //3、通过REST-assured发起接口请求
    }

    @DataProvider
    public Object[] getRegisterDatas(){
        List<CaseInfo> listDatas = readExcel();
        //关键问题：怎么把集合数据转换为数组？？？
        return listDatas.toArray();
    }

    /**
     * 读取Excel里面的数据
     * @return list集合
     */
    public List<CaseInfo> readExcel(){
        //从Excel里面读取到的文件
        File file = new File("src\\test\\resources\\api_testcases_futureloan_v2.xls");
        //读取/导入Excel的一些参数配置
        ImportParams importParams = new ImportParams();
        //设置读取第几个sheet
        importParams.setStartSheetIndex(0);
        //读取Excel里面的数据（Easy POI）
        List<CaseInfo> listDatas =  ExcelImportUtil.importExcel(file,CaseInfo.class,importParams);
        return listDatas;
    }


    public static void main(String[] args) {
        File file = new File("src\\test\\resources\\api_testcases_futureloan_v2.xls");
        //读取/导入Excel的一些参数配置
        ImportParams importParams = new ImportParams();
        //设置读取第几个sheet
        //importParams.setSheetNum(0);
        importParams.setStartSheetIndex(0);
        //设置读取开始行
        importParams.setStartRows(0);
        //设置读取的行数
        importParams.setReadRows(2);
        //读取Excel里面的数据（Easy POI）
        List<CaseInfo> listDatas =  ExcelImportUtil.importExcel(file,CaseInfo.class,importParams);
        System.out.println(listDatas);
    }

}
