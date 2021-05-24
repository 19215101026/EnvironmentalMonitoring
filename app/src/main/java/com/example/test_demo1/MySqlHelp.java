package com.example.test_demo1;


/*查询数据个数用来验证是否能够连接到数据库
* */
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static com.example.test_demo1.DBOpenHelper.closeALL;

public class MySqlHelp {
    private static final String TAG = "MySqlHelp";
    public static PreparedStatement pStmt;  //预编译命令集
    public static ResultSet rs; //结果集


    public static int getUserSize(){
        final String CLS = "com.mysql.jdbc.Driver";
        final String URL ="jdbc:mysql://8.140.139.188:3306/navigatemap?useUnicode=true&characterEncoding=utf8";
        final String USER ="testuser";
        final String PWD ="123456";

        int count = 0;  //用户的数量
        try{
            Class.forName(CLS);
            Connection conn = DriverManager.getConnection(URL,USER,PWD);
            String sql = "select count(1) as sl from userinfo";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                count = rs.getInt("sl");
            }

        }catch(Exception ex){
            Log.d(TAG,"catch:debug");
            ex.printStackTrace();
        }
        return count;
    }

    public static int getDateSize(){
        final String CLS = "com.mysql.jdbc.Driver";
        final String URL ="jdbc:mysql://8.140.139.188:3306/navigatemap";
        final String USER ="testuser";
        final String PWD ="123456";

        int count = 0;  //用户的数量
        try{
            Class.forName(CLS);
            Connection conn = DriverManager.getConnection(URL,USER,PWD);
            String sql = "select count(1) as sl from road_info";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while(rs.next()){
                count = rs.getInt("sl");
            }

        }catch(Exception ex){
            Log.d(TAG,"catch:debug");
            ex.printStackTrace();
        }
        return count;
    }

    public static String getUpass(String email, String uname){
        final String CLS = "com.mysql.jdbc.Driver";
        final String URL ="jdbc:mysql://8.140.139.188:3306/navigatemap";
        final String USER ="testuser";
        final String PWD ="123456";

        String upass = null;
        try{
            Class.forName(CLS);
            Connection conn = DriverManager.getConnection(URL,USER,PWD);

            String sql = "select upass from userinfo where email=? and uname=?";

            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, email);
            pStmt.setString(2, uname);
            rs = pStmt.executeQuery();
            if(rs.next()){
                upass = rs.getString("upass");
            }else{
                upass = null;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return upass;
    }
}
