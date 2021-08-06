package com.lemon.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.lemon.pojo.CaseInfo;

import java.io.File;
import java.util.List;

public class ExcelUtil {
    /**
     * 读取Excel指定sheet里面的全部数据
     * @return list集合
     */
    public static List<CaseInfo> readExcelSheetAllDatas(int sheetNum){
        //从Excel里面读取到的文件
        File file = new File(Constants.EXCEL_PATH);
        //读取/导入Excel的一些参数配置
        ImportParams importParams = new ImportParams();
        //设置读取第几个sheet
        importParams.setStartSheetIndex(sheetNum-1);
        //读取Excel里面的数据（Easy POI）
        List<CaseInfo> listDatas =  ExcelImportUtil.importExcel(file,CaseInfo.class,importParams);
        return listDatas;
    }

    /**
     * 读取Excel指定sheet里面的指定行
     * @param sheetNum sheet位置（从1开始）
     * @param startRows 读取开始行号（从1开始）
     * @param readRows 读取行总数
     * @return CaseInfo实体类集合
     */
    public static List<CaseInfo> readExcelSpecifyDatas(int sheetNum,int startRows, int readRows){
        //从Excel里面读取到的文件
        File file = new File(Constants.EXCEL_PATH);
        //读取/导入Excel的一些参数配置
        ImportParams importParams = new ImportParams();
        //设置读取第几个sheet
        importParams.setStartSheetIndex(sheetNum-1);
        importParams.setStartRows(startRows-1);
        importParams.setReadRows(readRows);
        //读取Excel里面的数据（Easy POI）
        List<CaseInfo> listDatas =  ExcelImportUtil.importExcel(file,CaseInfo.class,importParams);
        return listDatas;
    }

    /**
     * 读取Excel指定sheet里面的指定行
     * @param sheetNum sheet位置（从1开始）
     * @param startRows 读取开始行号（从1开始）
     * @return CaseInfo实体类集合
     */
    public static List<CaseInfo> readExcelSpecifyDatas(int sheetNum,int startRows){
        //从Excel里面读取到的文件
        File file = new File(Constants.EXCEL_PATH);
        //读取/导入Excel的一些参数配置
        ImportParams importParams = new ImportParams();
        //设置读取第几个sheet
        //setStartSheetIndex 从0开始，0-->第一个sheet  1-->第二个sheet
        importParams.setStartSheetIndex(sheetNum-1);
        //setStartRows 从0开始，0-->数据第一行 1-->数据第二行
        //setReadRows 读取总共的行数
        importParams.setStartRows(startRows-1);
        //读取Excel里面的数据（Easy POI）
        List<CaseInfo> listDatas =  ExcelImportUtil.importExcel(file,CaseInfo.class,importParams);
        return listDatas;
    }
}
