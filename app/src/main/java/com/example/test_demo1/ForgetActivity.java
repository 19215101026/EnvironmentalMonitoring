package com.example.test_demo1;

/*
* 忘记密码页面，这里调用数据库的查询功能，根据ID以及用户名进行查询，查询密码
* */

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText et_forget_email, et_forget_uname;
    private Button btn_forget_doquery;
    private TextView tv_forget_upass;

    private static final String TAG = "ForgetActivity";

    private Handler mainHandler;    //主线程的定义

    //handler 暂时测试用
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            if(msg.what == 0){
                //查询完毕，显示查询结果
                String end_upass = (String) msg.obj;
                if(end_upass == null){
                    tv_forget_upass.setText("您输入的ID或用户名不匹配，请重新输入！");
                }else{
                    tv_forget_upass.setText("该用户密码为： "+ end_upass);
                }
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        //实例化控件
        et_forget_email = findViewById(R.id.et_forget_email);
        et_forget_uname = findViewById(R.id.et_forget_uname);

        tv_forget_upass = findViewById(R.id.tv_forget_upass);

        btn_forget_doquery = findViewById(R.id.btn_forget_doquery);

        btn_forget_doquery.setOnClickListener(this);

        Log.d(TAG,"init_over");

    }


    //点击，则查询密码
    @Override
    public void onClick(View v) {
        Log.d(TAG,"onClick_begin");
        switch (v.getId()){
            case R.id.btn_forget_doquery:   //查询按钮，则进行密码查询
                doQueryUpass();
                break;
        }
    }

    private void doQueryUpass(){

        final String email = et_forget_email.getText().toString().trim();
        final String uname = et_forget_uname.getText().toString().trim();


        if (TextUtils.isEmpty(email)){
            CommonUtils.showShortMsg(this,"请输入验证ID");
            et_forget_email.requestFocus();
        }else if(TextUtils.isEmpty(uname)){
            CommonUtils.showShortMsg(this,"请输入用户名");
            et_forget_uname.requestFocus();
        }else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Log.d(TAG,"run_begin");
                    String upass = MySqlHelp.getUpass(email, uname);
                    Log.d(TAG,"upass : "+upass);
                    Message msg = Message.obtain();
                    msg.what = 0;    //查询结果标志位
                    msg.obj = upass;
                    //向主线程 传递得到的数据（可以用Handler）
                    handler.sendMessage(msg);
                }
            }).start();
        }


    }


}