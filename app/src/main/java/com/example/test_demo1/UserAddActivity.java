package com.example.test_demo1;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


/*
* 用户注册的业务代码
* */


public class UserAddActivity extends AppCompatActivity {

    private EditText et_uname,et_upass,et_email;
    private Handler mainHandler;

    private UserDao  userDao;

    private static final String TAG = "UserAddActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);

        //控件初始化
        et_uname = findViewById(R.id.et_uname);
        et_upass = findViewById(R.id.et_upass);
        et_email = findViewById(R.id.et_email);
        userDao = new UserDao();

        mainHandler = new Handler(getMainLooper());

    }

    //确定按钮的点击事件处理
    public void btn_ok_click(View view){

        final String uname = et_uname.getText().toString().trim();
        final String upass = et_upass.getText().toString().trim();
        final String email = et_email.getText().toString().trim();

        //判断数据是否为空
        if (TextUtils.isEmpty(email)){
            CommonUtils.showShortMsg(this,"请输入邮箱！");
            et_uname.requestFocus();
        }else if(TextUtils.isEmpty(uname)){
            CommonUtils.showShortMsg(this,"请输入用户名！");
            et_uname.requestFocus();
        }else if(TextUtils.isEmpty(upass)){
            CommonUtils.showShortMsg(this,"请输入密码！");
            et_uname.requestFocus();
        }else if(upass.length() <8||uname.length() <8){
            CommonUtils.showShortMsg(this,"用户名或密码长度应不小于八位数！");
        }else{

                //非空-则添加信息到数据库
                final Userinfo item = new Userinfo();
                item.setEmail(email);
                item.setUname(uname);
                item.setUpass(upass);
                item.setCreateDt(CommonUtils.getDateStrFromNow());

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        /*final boolean regd = userDao.getUserByuname(uname);
                        //判断数据库是否存在该用户
                        if(regd){   //判断数据库是否存在该用户（代码桩）
                            //通过验证mysql数据库是否存在输入的 uname 变量即可，后续补全
                            CommonUtils.showLongMsg(UserAddActivity.this,"该用户名已存在！");
                        }else{  //不存在，则添加
                        //添加数据，此时数据在item
                        final int iRow = userDao.addUser(item);
//                    Log.d(TAG,"iRow"+iRow);
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                setResult(1);   //使用参数，表示当前界面操作成功，并返回user管理界面
                                CommonUtils.showLongMsg(UserAddActivity.this, "注册成功，请登录！");
                                finish();
                            }
                        });
                    }*/
                        //判断数据是否存在

                            //添加数据，此时数据在item
                            final int iRow = userDao.addUser(item);
                            Log.d(TAG,"iRow"+iRow);
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    setResult(1);   //使用参数，表示当前界面操作成功，并返回user管理界面
                                    CommonUtils.showLongMsg(UserAddActivity.this, "注册成功，请登录！");
                                    finish();
                                }
                            });
                        }
                }).start();


        }

    }


}