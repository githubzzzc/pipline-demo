package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.ExcelUtil;
import com.lemon.utils.JDBCUtil;
import com.lemon.utils.RandomDataUtil;
import io.restassured.RestAssured;
import io.restassured.config.LogConfig;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 注册功能模块的测试类
 */
public class RegisterTest extends BaseTest {

    @BeforeClass
    public void setup(){
        //准备三个未注册的手机号码
        String mobile_phone1 = RandomDataUtil.getUnregisterPhone();
        String mobile_phone2 = RandomDataUtil.getUnregisterPhone();
        String mobile_phone3 = RandomDataUtil.getUnregisterPhone();
        //保存到环境变量中
        Environment.envMap.put("mobile_phone1",mobile_phone1);
        Environment.envMap.put("mobile_phone2",mobile_phone2);
        Environment.envMap.put("mobile_phone3",mobile_phone3);
    }

    @Test(dataProvider = "getRegisterDatas")
    public void register(CaseInfo caseInfo) throws FileNotFoundException {
        //参数化替换
        caseInfo = paramsReplace(caseInfo);
        //发起接口请求
        Response res = request(caseInfo);
        //响应断言
        assertResponse(res,caseInfo);
        //数据库断言
        assertDB(caseInfo);
    }


    @DataProvider
    public Object[] getRegisterDatas(){
        List<CaseInfo> listDatas = ExcelUtil.readExcelSheetAllDatas(1);
        return listDatas.toArray();
    }




}
