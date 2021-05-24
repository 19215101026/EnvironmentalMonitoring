package com.example.test_demo1;

/*
* 历史数据显示页面，采用adapter适配器。
* */
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import java.util.List;

public class DateActivity extends AppCompatActivity{

    //参数声明
    private UserDao userDao;    //用户数据库操作实例
    private List<Roadinfo> roadinfoList;    //采集数据集合
    private LvRoadinfoAdapter lvRoadinfoAdapter;    //采集信息数据 的适配器（列表模块）

    private ListView lv_date;
    private Handler mainHandler;

    private static final String TAG = "DateActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        //实例化各组件
        initView();

        loadUserDb();

    }



    private void initView() {

        Log.d(TAG,"initView");
        lv_date = findViewById(R.id.lv_data);

        mainHandler = new Handler(getMainLooper());

        userDao = new UserDao();    //实例化
        Log.d(TAG,"initView_over");


    }

    //列表展示函数
    private void loadUserDb() {

        Log.d(TAG,"loadUserDb_begin");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{

                        Log.d(TAG,"loadDb_begin");
                        roadinfoList = userDao.getShowInfo();

                        Log.d(TAG,"loadDb_over"+roadinfoList);

                        //进入主线程进行显示
                        mainHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG,"showLvData_begin");
                                showLvData();
                                Log.d(TAG,"showLvData_over");
                            }
                        });
                        Thread.sleep(3000);
                        //开启线程操作，获取要展示的数据
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();
    }

    private void showLvData(){
        if(lvRoadinfoAdapter == null){  //适配器为空，即首次加载的时候

            Log.d(TAG,"lvRoadinfoAdapter_load");

            lvRoadinfoAdapter = new LvRoadinfoAdapter(this, roadinfoList);

            Log.d(TAG,"lv_date_set");
            lv_date.setAdapter(lvRoadinfoAdapter);
            Log.d(TAG,"lv_date_set_over"+lv_date);
        }else{  //更新数据时的操作
            //1、放数据
            lvRoadinfoAdapter.setRoadinfoList(roadinfoList);
            //2、更新数据
            lvRoadinfoAdapter.notifyDataSetChanged();
        }

    }

}