package com.example.test_demo1;

import java.io.Serializable;

public class EquipInfo implements Serializable {
    private int id;     //用户的id
    private String number;       //设备序号
    private String jingdu;       //经度
    private String weidu;    // 维度

    public EquipInfo() {
    }

    public EquipInfo(int id, String number, String jingdu, String weidu) {
        this.id = id;
        this.number = number;
        this.jingdu = jingdu;
        this.weidu = weidu;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getJingdu() {
        return jingdu;
    }

    public void setJingdu(String jingdu) {
        this.jingdu = jingdu;
    }

    public String getWeidu() {
        return weidu;
    }

    public void setWeidu(String weidu) {
        this.weidu = weidu;
    }
}
