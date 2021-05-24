package com.example.test_demo1;

/*yxp:
MySql数据库的连接辅助类--父类
 * */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;


public class DBOpenHelper {
    private static final String CLS = "com.mysql.jdbc.Driver";
    private static final String URL ="jdbc:mysql://8.140.139.288:3306/navigation?useUnicode=true&characterEncoding=utf8";//这里更改为你自己的URL
    private static final String USER ="test_user";//这里更改为你自己的管理员账号，要有权限
    private static final String PWD ="123456";//这里更改为你自己的管理员密码，要有权限

    public static Connection conn; //连接对象
    public static Statement stmt;   //命令集
    public static PreparedStatement pStmt;  //预编译命令集
    public static ResultSet rs; //结果集

    //取得连接的方法
    public static void getConnection(){
        try{
            Class.forName(CLS);
            conn = DriverManager.getConnection(URL,USER,PWD);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    //关闭数据库操作对象
    public static void closeALL(){
        try{
            if(rs!= null){
                rs.close();
                rs=null;
            }
            if(stmt!= null){
                stmt.close();
                stmt=null;
            }
            if(pStmt!= null){
                pStmt.close();
                pStmt=null;
            }
            if(conn!= null){
                conn.close();
                conn=null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }



}
