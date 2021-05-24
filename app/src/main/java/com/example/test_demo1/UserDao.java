package com.example.test_demo1;


import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/*用户对数据库的操作类---继承DBOpenHelper的子类（DBOpenHelper指定URL，这里指定table以及操作）
实现用户的C,R,U,D操作（操作都写了，但是暂时登录注册功能只用到了查询和添加函数）
* */
public class UserDao extends DBOpenHelper{

    private static final String TAG = "UserDao";


    //查询所有用户的信息             R
    public List<Userinfo> getAllUserList(){
        List<Userinfo> list = new ArrayList<>();
        try{
            getConnection();//取得连接信息
            String sql = "select * from userinfo";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while(rs.next()){
                Userinfo item = new Userinfo();
                item.setId(rs.getInt("id"));
                item.setUname(rs.getString("uname"));
                item.setUpass(rs.getString("upass"));
//                item.setCreateDt(rs.getString("createDt"));
                item.setCreateDt(rs.getString("createDt"));

                list.add(item);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }

        return list;
    }


    /*按照用户名和密码查询用户信息    R
    *@param uname 用户名
    *@param upass 密码
    *@return Userinfo 示例
    *  */

    public Userinfo getUserByUnameAndUpass(String uname, String upass){
        Userinfo item = null;
        try{
            getConnection();//取得连接信息
            String sql = "select * from userinfo where uname=? and upass = ?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,uname);
            pStmt.setString(2,upass);
            rs = pStmt.executeQuery();
            if(rs.next()){
                item =new Userinfo();
                item.setId(rs.getInt("id"));
                item.setUname(uname);
                item.setUpass(upass);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return item;
    }

    /*按照用户名和密码查询用户信息    R
     *@param uname 用户名
     *@param upass 密码
     *@return Userinfo 示例
     *  */

    public Userinfo getUserByUname(String uname){
        Userinfo item = null;
        try{
            getConnection();//取得连接信息
            String sql = "select * from userinfo where uname=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,uname);
            rs = pStmt.executeQuery();
            if(rs.next()){
                item =new Userinfo();
                item.setEmail(rs.getString("email"));
                item.setUname(uname);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return item;
    }

    /*添加用户信息                    C
    * @param item  要添加的用户
    * @return int   影响的行数
    * */
    public int addUser(Userinfo item){
        int iRow = 0;
        try{
            getConnection();//取得连接信息
            String sql = "insert into userinfo(uname, upass,email) values(?,?,?)";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1,item.getUname());
            pStmt.setString(2,item.getUpass());
            pStmt.setString(3,item.getEmail());
            iRow = pStmt.executeUpdate();
            Log.d(TAG,"addUser_begin");
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return iRow;
    }



    /*  更改用户信息                         U
     * @param item  要添加的用户
     * @return int   影响的行数
     * */
    public int editUser(Userinfo item){
        int iRow = 0;
        try{
            getConnection();   // 取得连接信息
            String sql = "update userinfo set uname=?, upass=? where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, item.getUname());
            pStmt.setString(2, item.getUpass());
            pStmt.setInt(3, item.getId());
            iRow = pStmt.executeUpdate();

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return iRow;
    }


    /*  根据id，删除用户信息                         U
     * @param id  要删除的的用户id
     * @return int   影响的行数
     * */
    public int delUser(int id){
        int iRow = 0;
        try{
            getConnection();//取得连接信息
            String sql = "delete from userinfo where id=?";
            pStmt = conn.prepareStatement(sql);
            pStmt.setInt(1,id);
            iRow = pStmt.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return iRow;
    }

    //查询地图页面要展示的数据
    public Roadinfo getLastInfo(){
        Roadinfo item = null;
        try{
//            Log.d(TAG,"getConnection_begin");
            getConnection();//取得连接信息
            String sql = "select * from road_info order by created desc limit 1";
            pStmt = conn.prepareStatement(sql);
//            pStmt.setInt(1,id);
//            Log.d(TAG,"executeQuery_begin");
            rs = pStmt.executeQuery();
//            Log.d(TAG,"executeQuery"+rs);
            if(rs.next()){
                item =new Roadinfo();
                item.setDuche(rs.getString("duche"));
                item.setFengsu(rs.getString("fengsu"));
                item.setJiangyu(rs.getString("yudi"));
                item.setYanwu(rs.getString("yanwu"));
                item.setCreateDt(rs.getString("created"));
            }
            Log.d(TAG,"executeQuery"+item);
        }catch (Exception ex){
            Log.d(TAG,"Exception");
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return item;
    }

    //查询所有数据
    public List<Roadinfo> getShowInfo(){

        List<Roadinfo> list = new ArrayList<>();

        try{
//            Log.d(TAG,"getConnection_begin");
            getConnection();//取得连接信息
            String sql = "select * from road_info order by created desc limit 20 ";
            pStmt = conn.prepareStatement(sql);
//            Log.d(TAG,"executeQuery_begin");
            rs = pStmt.executeQuery();
//            Log.d(TAG,"executeQuery"+rs);
            while(rs.next()){
                Roadinfo item =new Roadinfo();
                item.setId(rs.getInt("model_number"));
                item.setFengsu(rs.getString("fengsu"));
                item.setJiangyu(rs.getString("yudi"));
                item.setYanwu(rs.getString("yanwu"));
                item.setCreateDt(rs.getString("created"));

                Log.d(TAG,"info_item"+item);


                //将查询到的数据，添加到list里
                list.add(item);

            }

        }catch (Exception ex){
            ex.printStackTrace();
        }finally {
            closeALL();
        }
        return list;
    }


}
