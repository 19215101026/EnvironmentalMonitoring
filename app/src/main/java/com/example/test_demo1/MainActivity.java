package com.example.test_demo1;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/*登录界面的业务逻辑代码
 * */

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    //登录按钮
    private Button  btn_login;

    //textview包括忘记密码 和 注册
    private TextView tv_forget, tv_register;

    //用户名和密码
    private EditText et_uname, et_upass;


    private UserDao userDao;  //数据库的操作类




    private static final String TAG = "MainActivity";

    private Handler mainHandler;    //主线程的定义

/*    //handler 暂时测试用
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            if(msg.what == 0){
                int count  = (Integer)msg.obj;
                tv_forget.setText("数据库中的用户数量为：" + count);
            }
        }
    };*/




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        init();

    }

    private void init(){

        /*
        * 控件实例化：
        *   1、用户名和密码输入
        *   2、登录按钮
        *   3、忘记密码和注册textview
        * */
        et_uname = findViewById(R.id.et_uname);
        et_upass = findViewById(R.id.et_upass);
        btn_login = findViewById(R.id.btn_login);
        tv_forget = findViewById(R.id.tv_forget);
        tv_register = findViewById(R.id.tv_register);

        mainHandler = new Handler(getMainLooper()); //主线程 实例化

        btn_login.setOnClickListener(this);
        tv_forget.setOnClickListener(this);
        tv_register.setOnClickListener(this);

        //实例化 数据库操作类UserDao
        userDao = new UserDao();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_login://登录按钮  登录
                doLogin();
                break;

            case R.id.tv_register://执行查询动作
                doRegister();
                break;

            case R.id.tv_forget://执行查询动作
                Log.d(TAG,"tv_forget_begin");
                doForget();
                Log.d(TAG,"tv_forget_over");
                break;
        }
    }

    /*//查询实现（测试用）
    private void doQueryCount(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                int count = MySqlHelp.getUserSize();
                Message msg = Message.obtain();
                msg.what = 0;    //查询结果标志位
                msg.obj = count;
                //向主线程 传递得到的数据（可以用Handler）
                handler.sendMessage(msg);
            }
        }).start();
    }*/

    //登录行为的实现
    /*
    * 登录过程
    *   1、首先查询mysql数据库所有数据
    *   2、进行非空校验
    *   3、查询    uname   和  upass 是否匹配
    *   4、匹配后进入数据显示页面
    * */
    private void doLogin(){
        final String uname = et_uname.getText().toString().trim();
        final String upass = et_upass.getText().toString().trim();

        //判断数据是否为空
        if (TextUtils.isEmpty(uname)){
            CommonUtils.showShortMsg(this,"请输入用户名");
            et_uname.requestFocus();
        }else if(TextUtils.isEmpty(upass)){
            CommonUtils.showShortMsg(this,"请输入密码");
            et_uname.requestFocus();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //登录线程的操作---先查询
                    final Userinfo item = userDao.getUserByUnameAndUpass(uname, upass);
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (item == null){
                                CommonUtils.showLongMsg(MainActivity.this,"用户名或密码错误");
                            }else{
//                              CommonUtils.showDlgMsg(MainActivity.this,"登录成功，进入用户管理");
                                CommonUtils.showLongMsg(MainActivity.this,"登录成功，进入显示页面");
                                //登录成功进入主界面
                                Intent intent = new Intent(MainActivity.this, BaiduMapActivity.class);
                                startActivity(intent);
                            }
                        }
                    });
                }
            }).start();
        }
    }


    //注册操作
    private void doRegister(){
        Intent intent = new Intent(this,UserAddActivity.class);
        startActivityForResult(intent, 1);
    }


    //忘记密码操作
    private void doForget(){
        Intent intent = new Intent(MainActivity.this, ForgetActivity.class);
        startActivity(intent);
    }
}