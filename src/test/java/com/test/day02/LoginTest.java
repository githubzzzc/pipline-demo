package com.test.day02;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.encryption.RSAManager;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;

import static io.restassured.RestAssured.given;

public class LoginTest {

    @Test(dataProvider = "getDatas")
    public void login(String username){
        System.out.println("用户名："+username);
    }

    @DataProvider
    public Object[] getDatas(){
        Object[] datas  = {"jack","rose"};
        //通过Easy poi读取数据
        return datas;
    }

}
