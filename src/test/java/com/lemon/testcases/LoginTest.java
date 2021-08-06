package com.lemon.testcases;

import com.alibaba.fastjson.JSONObject;
import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.ExcelUtil;
import com.lemon.utils.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 注册功能模块的测试类
 */
public class LoginTest extends BaseTest {

    @BeforeClass
    public void beforeClass(){
        //生成一个未注册的手机号码
        String mobile_phone = RandomDataUtil.getUnregisterPhone();
        Environment.envMap.put("mobile_phone",mobile_phone);
        //只需要读取第一条数据
        List<CaseInfo> datas = ExcelUtil.readExcelSpecifyDatas(2,1,1);
        CaseInfo registerInfo = datas.get(0);
        //替换mobile_phone
        registerInfo = paramsReplace(registerInfo);
        //发起接口请求，注册一个用户
        Response res = request(registerInfo);
        /*String mobilephone = res.jsonPath().get("data.mobile_phone");
        //将手机号码保存至环境变量
        Environment.envMap.put("mobile_phone",mobilephone);*/
        extractToEnvironment(res,registerInfo);
        //测试数据生成
        //1、调用接口生成 被调用正常
        //2、读取文件
        //3、数据库操作 必须要对数据库表字段\结构 要求高
        //4、通过UI
    }

    //调用驱动方法
    @Test(dataProvider = "getLoginDatas")
    public void login(CaseInfo caseInfo){
        caseInfo = paramsReplace(caseInfo);
        //发起接口请求
        Response res = request(caseInfo);
        //断言
        assertResponse(res,caseInfo);
    }
    //标记方法为数据驱动，读取文件数据转成一维数组
    @DataProvider
    public Object[] getLoginDatas(){
        List<CaseInfo> listDatas = ExcelUtil.readExcelSpecifyDatas(2,2);
        return listDatas.toArray();
    }

}
