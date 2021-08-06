package com.lemon.testcases;

import com.lemon.common.BaseTest;
import com.lemon.data.Environment;
import com.lemon.encryption.RSAManager;
import com.lemon.pojo.CaseInfo;
import com.lemon.utils.ExcelUtil;
import com.lemon.utils.RandomDataUtil;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;

/**
 * 注册功能模块的测试类
 */
public class GetUserInfoTest extends BaseTest {

    @BeforeClass
    public void setup(){
        //准备三个未注册的手机号码
        String mobile_phone = RandomDataUtil.getUnregisterPhone();
        //保存到环境变量中
        Environment.envMap.put("mobile_phone",mobile_phone);
    }

    @Test(dataProvider = "getUserInfoDatas")
    public void user_info(CaseInfo caseInfo) throws Exception {
        if(caseInfo.getCaseId()>2){
            v3Author();
        }
        //参数化替换
        caseInfo = paramsReplace(caseInfo);
        //发起接口请求
        Response res = request(caseInfo);
        //断言
        assertResponse(res,caseInfo);
        //提取响应结果
        extractToEnvironment(res,caseInfo);
    }

    public void v3Author(){
        //得到时间戳timestamp(接口要求是：秒级的时间戳)
        long timestamp = System.currentTimeMillis()/1000;
        //sign参数获取（取 token 前 50 位再拼接上 timestamp 值，然后通过 RSA 公钥加密得到字符串）
        String tokenValue = (String)Environment.envMap.get("token");
        String token_50 = tokenValue.substring(0,50);
        String str = token_50+timestamp;
        //加密代码--》找开发去获取-->一般是一个jar包
        //使用加密包里面加密算法（RSA）
        String encryptStr = null;
        try {
            encryptStr = RSAManager.encryptWithBase64(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Environment.envMap.put("sign",encryptStr);
        Environment.envMap.put("timestamp",timestamp);
    }

    @DataProvider
    public Object[] getUserInfoDatas(){
        List<CaseInfo> listDatas = ExcelUtil.readExcelSheetAllDatas(3);
        return listDatas.toArray();
    }

}
