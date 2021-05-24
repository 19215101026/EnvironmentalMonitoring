package com.example.test_demo1;

import java.io.Serializable;


/*
* 公路信息辅助类（包括id、风速、降雨情况、烟雾、创建时间）
* */
public class Roadinfo implements Serializable {
    private int id;
    private String duche;
    private String fengsu;
    private String jiangyu;
    private String yanwu;
    private String createDt;


    public Roadinfo() {
    }

    public Roadinfo(int id, String duche, String fengsu, String jiangyu, String yanwu, String createDt) {
        this.id = id;
        this.duche = duche;
        this.fengsu = fengsu;
        this.jiangyu = jiangyu;
        this.yanwu = yanwu;
        this.createDt = createDt;
    }

    public String getDuche() {
        return duche;
    }

    public void setDuche(String duche) {
        this.duche = duche;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFengsu() {
        return fengsu;
    }

    public void setFengsu(String fengsu) {
        this.fengsu = fengsu;
    }

    public String getJiangyu() {
        return jiangyu;
    }

    public void setJiangyu(String jiangyu) {
        this.jiangyu = jiangyu;
    }

    public String getYanwu() {
        return yanwu;
    }

    public void setYanwu(String yanwu) {
        this.yanwu = yanwu;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }

}
