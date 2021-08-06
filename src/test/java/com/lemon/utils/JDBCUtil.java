package com.lemon.utils;


import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class JDBCUtil {
    /**
     * 和数据库建立连接
     * @return 数据库连接对象
     */
    public static Connection getConnection()  {
        //定义数据库连接
        //Oracle：jdbc:oracle:thin:@localhost:1521:DBName
        //SqlServer：jdbc:microsoft:sqlserver://localhost:1433; DatabaseName=DBName
        //MySql：jdbc:mysql://localhost:3306/DBName
        String url="jdbc:mysql://api.lemonban.com/futureloan?useUnicode=true&characterEncoding=utf-8";
        String user="future";
        String password="123456";
        //定义数据库连接对象
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user,password);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return conn;
    }

    public static void main(String[] args) throws SQLException {
        //1、建立连接
        Connection connection = getConnection();
        //2、插入数据的操作
        /*QueryRunner qr = new QueryRunner();
        String sql = "insert into member values (3456778,'jack','7BB4880520192D282D68659D9FF084DC','13323234719',1,1000.0,'2021-07-09 21:06:20');";
        qr.update(connection,sql);*/
        //3、修改数据的操作
        /*QueryRunner qr = new QueryRunner();
        String sql = "update member set mobile_phone='13345454720' where id=3456778";
        qr.update(connection,sql);*/
        //4、删除数据的操作
        /*QueryRunner qr = new QueryRunner();
        String sql = "delete from member where  id=3456778";
        qr.update(connection,sql);*/

        //查询数据的操作
        //1、MapHandler --》保存结果集中的第一条数据
        /*QueryRunner qr = new QueryRunner();
        String sql="select * from member where id<10";
        Map<String,Object> map = qr.query(connection,sql,new MapHandler());
        System.out.println(map);*/
        //2、MapListHandler --》保存结果集里面的所有数据
        /*QueryRunner qr = new QueryRunner();
        String sql="select * from member where id<10";
        List<Map<String,Object>> datas = qr.query(connection,sql,new MapListHandler());
        System.out.println(datas);*/
        //3、ScalarHandler --》保存单个字段的数据
        /*QueryRunner qr = new QueryRunner();
        String sql="select reg_name from member where id = 10";
        Object data = qr.query(connection,sql,new ScalarHandler<String>());
        System.out.println(data);*/
    }

    /**
     * 修改数据库数据操作（插入、修改、删除）
     * @param sql 要执行的sql语句
     */
    public static void updateData(String sql){
        //1、建立连接
        Connection conn = getConnection();
        //2、QueryRunner对象生成
        QueryRunner queryRunner = new QueryRunner();
        //3、执行sql
        try {
            queryRunner.update(conn,sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            //关闭连接
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    /**
     * 查询单个字段的数据
     * @param sql 要执行的sql语句
     * @return 返回查询结果
     */
    public static Object querySingleData(String sql){
        //1、建立连接
        Connection conn = getConnection();
        //2、QueryRunner对象生成
        QueryRunner queryRunner = new QueryRunner();
        //3、执行sql
        Object data =null ;
        try {
            data = queryRunner.query(conn,sql,new ScalarHandler<Object>());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 查询所有的数据
     * @param sql 要执行的sql语句
     * @return 返回查询结果
     */
    public static List<Map<String,Object>> queryAllDatas(String sql){
        //1、建立连接
        Connection conn = getConnection();
        //2、QueryRunner对象生成
        QueryRunner queryRunner = new QueryRunner();
        //3、执行sql
        List<Map<String,Object>> data = null;
        try {
            data = queryRunner.query(conn,sql,new MapListHandler());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                conn.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        return data;
    }
}
