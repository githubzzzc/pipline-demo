package com.lemon.utils;

import java.util.Random;

public class RandomDataUtil {
    /**
     * 生成一个随机的手机号码
     * @return 随机的手机号码
     */
    public static String randomPhone(){
        //1、先去查询数据库里面手机号码最大的那一个，在其基础上+1
        //2、先去生成一个随机手机号码，再查询数据库，如果数据库中存在->再继续生成一个随机，如果不存在的话->满足要求
        String phonePrefix="133";
        Random random =new Random();
        //随机生成【0-9】范围内的整数
        for(int i=0 ; i<8; i++){
            int num = random.nextInt(9);
            phonePrefix = phonePrefix+num;
        }
        return phonePrefix;
    }

    public static String getUnregisterPhone(){
        String phone = "";
        while(true) {
            phone=randomPhone();
            //查询数据库
            Long result = (Long) JDBCUtil.querySingleData("select count(*) from member where mobile_phone='" + phone + "';");
            if (1 == result) {
                //说明存在，继续生成一个随机
            }else {
                //说明不存在->满足要求->退出循环
                break;
            }
        }
        return phone;
    }


    public static void main(String[] args) {
        //生成一个随机的手机号码
        System.out.println(getUnregisterPhone());
    }
}
