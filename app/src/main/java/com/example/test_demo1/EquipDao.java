package com.example.test_demo1;

import java.util.ArrayList;
import java.util.List;

/*
* 设备信息操作类，主要是查询设备坐标位置
* */
public class EquipDao extends DBOpenHelper{

    public List<EquipInfo> getAllEquipList(){
        List<EquipInfo> list = new ArrayList<>();
        try{
            getConnection();//取得连接信息
            String sql = "select * from equipment_info";
            pStmt = conn.prepareStatement(sql);
            rs = pStmt.executeQuery();
            while(rs.next()){
                EquipInfo item = new EquipInfo();
                item.setNumber(rs.getString("name"));
                item.setJingdu(rs.getString("chartX"));
//                item.setCreateDt(rs.getString("createDt"));
                item.setWeidu(rs.getString("chartY"));

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
