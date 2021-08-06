package com.lemon.utils;

import com.lemon.data.Environment;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    public static void main(String[] args) {
        //现在的情况是：
        //在我们的环境变量中存在一个mobile_phone:13323234545


        String str = "{\n" +
                "    \"mobile_phone\": {{mobile_phone}},\n" +
                "    \"pwd\": {{pwd}},\n" +
                "    \"id\":{{id}}\n" +
                "}";
        System.out.println("原始的字符串："+str);
        String regxExpr = "{{(.*?)}}";
        //匹配器
        Pattern pattern = Pattern.compile("\\{\\{(.*?)\\}\\}");
        //匹配对象
        Matcher matcher = pattern.matcher(str);
        String result = str;
        //循环遍历匹配对象
        while(matcher.find()){
            //获取整个匹配正则的字符串 {{mobile_phone}}
            String allFindStr =  matcher.group(0);
            System.out.println("找到的完整字符串:"+allFindStr);
            //找到{{XXX}}内部的匹配的字符串 mobile_phone
            String innerStr = matcher.group(1);
            System.out.println("找到的内部字符串:"+innerStr);
            //找到手机号码：13323234545
            //具体的要替换的值（从环境变量中去找到的）
            Environment.envMap.put("mobile_phone","13323234545");
            Environment.envMap.put("pwd","123456");
            Environment.envMap.put("id","111");
            Object replaceValue = Environment.envMap.get(innerStr);
            //要替换{{mobile_phone}} --> 13323234545
            //第二次替换的时候应该是基于第一次替换的结果的基础上再去替换
            result = result.replace(allFindStr,replaceValue+"");
            System.out.println("替换之后的效果："+result);
        }
    }


}
