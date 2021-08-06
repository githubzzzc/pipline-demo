package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.ExcelUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 注册功能模块的测试类
 */
public class InvestFlowTest extends BaseTest {

    @BeforeClass
    public void beforeClass(){
        //生成三个随机的手机号码
    }

    @Test(dataProvider = "getLoginDatas")
    public void investFlow(CaseInfo caseInfo){
        caseInfo = paramsReplace(caseInfo);
        //发起接口请求
        Response res = request(caseInfo);
        //断言
        assertResponse(res,caseInfo);
        //提取响应结果
        extractToEnvironment(res,caseInfo);
    }
    

    @DataProvider
    public Object[] getLoginDatas(){
        List<CaseInfo> listDatas = ExcelUtil.readExcelSheetAllDatas(3);
        return listDatas.toArray();
    }

}
