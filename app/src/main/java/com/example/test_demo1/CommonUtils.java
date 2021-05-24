package com.example.test_demo1;

/*自定义的通用工具类
* */

import android.app.AlertDialog;
import android.content.Context;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {

    //调整一下
    /*
    * 获取当前时间的  字符串格式  返回"yyyy-MM--dd HH:mm:ss"格式的  时间字符串
    * */
    public static String getDateStrFromNow(){
        DateFormat df = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
        return df.format(new Date());
    }


    /*
    * 从日期时间中 获取  时间字符串
    * */
    public static String getStrFromDate(Date dt){
        DateFormat df = new SimpleDateFormat("yyyy-MM--dd HH:mm:ss");
        return df.format(dt);
    }

    /*显示短消息
    @param context上下文
    @param msg 显示的消息
    * */
    public static void showShortMsg(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    /*显示长消息
    @param context上下文
    @param msg 显示的消息
    * */
    public static void showLongMsg(Context context, String msg){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }

    /*显示对话框
    @param context上下文
    @param msg 显示的消息
    * */
    public static void showDlgMsg(Context context, String msg){
        new AlertDialog.Builder(context)
                .setTitle("提示信息")
                .setMessage(msg)
                .setNegativeButton("取消",null)
                .setPositiveButton("确定",null)
                .create().show();


    }

}
