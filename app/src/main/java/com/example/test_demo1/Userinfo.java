package com.example.test_demo1;

import java.io.Serializable;

/*
 * 用户信息辅助类，用户信息包括（id、用户名、密码、创建时间）
 * */

public class Userinfo implements Serializable {
    private int id;     //用户的id
    private String email;
    private String uname;       //用户名
    private String upass;       //用户密码
    private String createDt;    // 创建时间

    public Userinfo() {
    }

    public Userinfo(int id, String email, String uname, String upass, String createDt) {
        this.id = id;
        this.email = email;
        this.uname = uname;
        this.upass = upass;
        this.createDt = createDt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUpass() {
        return upass;
    }

    public void setUpass(String upass) {
        this.upass = upass;
    }

    public String getCreateDt() {
        return createDt;
    }

    public void setCreateDt(String createDt) {
        this.createDt = createDt;
    }
}
