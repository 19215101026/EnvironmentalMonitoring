package com.example.test_demo1;

/**
 * 地图显示页面的代码：
 */

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.mysql.jdbc.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class BaiduMapActivity extends AppCompatActivity implements View.OnClickListener{
    /*
    * 定义控件
    * */

    //地图部分
    //客户端定位
    public LocationClient mLocationClient;
    //位置信息
    private TextView positionText;
    //地图展示
    private MapView mapView;
    //百度地图
    private BaiduMap baiduMap;
    //首次定位标志位
    private boolean isFirstLocate = true;
    private TextView mTVinformation;
    //设备位置数据辅助类
    private EquipDao equipDao;
    //设备位置信息集合
    List<EquipInfo> equipInfos;

    //车辆环境监测部分：风速、降雨、烟雾

    private TextView tv_info_fengsu, tv_info_jiangyu, tv_info_yanwu,tv_info_createDt;
    private ImageView imv_res;
    //数据处理辅助类
    private UserDao userDao;

    //主线程的定义
    private Handler mainHandler;

    private static final String TAG = "BaiduMapActivity";



    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
//            super.handleMessage(msg);
            if(msg.what == 0){
                Log.d(TAG,"handler_begin");
                Roadinfo item  = (Roadinfo) msg.obj;
                Log.d(TAG,"handler_over"+item.getCreateDt());

                tv_info_fengsu.setText("风速："+ item.getFengsu()+"m/s");
                //降雨情况
                tv_info_jiangyu.setText("降雨："+item.getJiangyu());
                //烟雾浓度
                tv_info_yanwu.setText("烟雾："+item.getYanwu());
                //数据采取时间
                tv_info_createDt.setText("数据采集时间为："+item.getCreateDt());

            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化参数
        init();

        //从mysql数据库进行数据查询（每隔2S更新一次）

        //延迟两秒！！！！！（代码桩，后续更新）
        //查询操作
        doQuery();


    }//ACCESS_FINE_LOCATION、ACCESS_COARSE_LOCATION、READ_PHONE_STATE、WRITE_EXTERNAL_STORAGE这4个权限是危险权限

    private void doQuery() {
        //采用getInfo进行数据查询并显示！
        new Thread(new Runnable() {
            @Override
            public void run() {

                while (true){
                    try{
                        //利用数据库操作类进行数据查询，只查询最后一次数据用来显示
                        Roadinfo item = userDao.getLastInfo();
                        Log.d(TAG,"getLastInfo_over:"+item.getYanwu());
//                int count = MySqlHelp.getUserSize();
                        //结果保存在item里，现在将数据进行显示
                        Message msg = Message.obtain();
                        msg.what = 0;    //查询结果标志位
                        msg.obj = item;
                        //向主线程 传递得到的数据（可以用Handler）
                        handler.sendMessage(msg);
                        Thread.sleep(3000);
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }

            }
        }).start();
        Log.d(TAG,"doQuery_over");
    }


    /*各控件初始化
    *   1、地图部分
    *   2、信息监测部分
    * */
    private void init(){
        Log.d(TAG,"init_begin");



        mainHandler = new Handler(getMainLooper());
        userDao = new UserDao();
        equipDao = new EquipDao();
        //初始化设备位置
        equipInfos = new ArrayList<EquipInfo>();
        //1、地图控件以及参数
        mTVinformation = findViewById(R.id.tv_Information);

        //创建一个LocationClient的实例，
        // LocationClient的构建函数接收一个Context参数，这里调用getApplicationContext()方法来获取一个全局的Context参数并传入。
        mLocationClient = new LocationClient(getApplicationContext());
        // 然后调用LocationClient的registerLocationListener（）方法来注册一个定位监听器，当获取到位置信息的时候，就会回调这个定位监听器。
        mLocationClient.registerLocationListener(new MyLocationListener());
        SDKInitializer.initialize(getApplicationContext());

        //创建地图页面
        setContentView(R.layout.activity_baidu_map);
        mapView = (MapView) findViewById(R.id.bmapView);
        mapView.removeViewAt(1);
        //通过api调用地图，并启用监听器
        baiduMap = mapView.getMap();
        baiduMap.setTrafficEnabled(true);
        baiduMap.setMyLocationEnabled(true);
        //当前位置显示-文本（后续可以添加位置的使用，现在单纯是用来定位）
        positionText = (TextView) findViewById(R.id.position_text_view);


        //初始化设备位置信息
        initData();


        //权限
        List<String> permissionList = new ArrayList<>();
        //检测用户是否授权了需要的权限，摄像头，定位，麦克风之类的
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {            //判断权限是否为空，如果非空则执行

            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this, permissions, 1);
        } else {        //如果权限为空，则进行权限申请
            requestLocation();
        }
        //风速、降雨情况、烟雾浓度显示初始化
        tv_info_fengsu = findViewById(R.id.tv_info_fengsu);
        tv_info_jiangyu = findViewById(R.id.tv_info_jiangyu);
        tv_info_yanwu = findViewById(R.id.tv_info_yanwu);
        tv_info_createDt = findViewById(R.id.tv_info_createDt);
//        imv_res = findViewById(R.id.imv_res);


    }
    //初始化覆盖物信息
    public void initData(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                //开启线程操作，获取要展示的数据
                Log.d(TAG,"loadDb_begin");
                equipInfos = equipDao.getAllEquipList();
                Log.d(TAG,"loadDb_over"+equipInfos);
            }
        }).start();
    }

    // 需要进行运行时权限处理，不过ACCESS_FINE_LOCATION、ACCESS_COARSE_LOCATION属于同一个权限组，两者申请其一就可以了。
    // 这里运用了一比较新的用法在运行时一次性申请3个权限。首先创建了一个List集合，然后依次判断这3个权限有没有被授权，
    // 如果没有被授权，就添加到List集合中，最后将List转换成数组，在调用ActivityCompat.requestPermissions()方法一次性申请。
    private void requestLocation() {
        initLocation();
        mLocationClient.start();
    }


    //根据定位结果进行显示
    private void navigateTo(BDLocation location) {
        //初次定位，要根据经纬度，初始化地图
        //初次视角  写死，116.417854,39.921988
        if (isFirstLocate) {
            //LatLng类： 地理坐标点，用于存放经纬度
            //ll  =  getLatitude--纬度   +  getLongitude----经度
            LatLng ll = new LatLng(38.820583, 	115.492563);
            // 第一个参数是纬度值，第二个参数是精度值。这里输入的是本地位置。
            MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);//将LatLng对象传入
            baiduMap.animateMapStatus(update);
            //百度地图缩放范围，限定在3-19之间，可以去小数点位值
            update = MapStatusUpdateFactory.zoomTo(16f);
            // 值越大，地图显示的信息越精细
            baiduMap.animateMapStatus(update);
            isFirstLocate = false;//防止多次调用animateMapStatus()方法，以为将地图移动到我们当前位置只需在程序
            // 第一次定位的时候调用一次就可以了。
        }
        //接下来就正常更新位置(经纬度)
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.latitude(38.820583);
        locationBuilder.longitude(115.492563);
        MyLocationData locationData = locationBuilder.build();
        baiduMap.setMyLocationData(locationData);//获取我们的当地位置
    }

    /**初始化定位：
     * 可以设定定位模式
     */
    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setScanSpan(1000);//表示每5秒更新一下当前位置
        option.setIsNeedAddress(true);
        option.setCoorType("bd09ll");
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        // Hight_Accuracy表示高精确度模式，会在GPS信号正常的情况下优先使用GPS定位，在无法接收GPS信号的时候使用网络定位。
        // Battery_Saving表示节电模式，只会使用网络进行定位。
        // Device_Sensors表示传感器模式，只会使用GPS进行定位。
        mLocationClient.setLocOption(option);
    }


    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();//销毁之前，用stop()来停止定位
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(this, "必须同意所有权限才能使用本程序", Toast.LENGTH_LONG).show();
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                } else {
                    Toast.makeText(this, "发生未知错误", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            default:
        }//onRequestPermissionsResult()方法中，对权限申请结果进行逻辑判断。这里使用一个循环对每个权限进行判断，
        // 如果有任意一个权限被拒绝了，那么就会直接调用finish()方法关闭程序，只有当所有的权限被用户同意了，才会
        // 调用requestPermissions()方法开始地理位置定位。
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_Information:
                Intent intent1 = new Intent(this, DateActivity.class);
                startActivity(intent1);
                break;
            case R.id.iv_mapctivity_back: //返回登录页面
                Intent intent2 = new Intent(this, MainActivity.class);
                startActivity(intent2);
                break;

        }
    }


    //onReceiveLocation，来回调定位结果
    public class MyLocationListener implements BDLocationListener {

        @Override
        //location 就是定位结果
        public void onReceiveLocation(BDLocation location) {
            //验证定位类型：GPS或者网络
            if (location.getLocType() == BDLocation.TypeGpsLocation || location.getLocType() == BDLocation.TypeNetWorkLocation) {
                //获取位置信息
                navigateTo(location);
                //添加覆盖物，覆盖物包括列表信息
                addInfosOverlay();
            }
        }

        @Override
        public void onConnectHotSpotMessage(String s, int i) {
            String a = s;
        }
    }


    /**
     * 添加设备标志
     */
    public void addInfosOverlay() {
        baiduMap.clear();
        LatLng latLng = null;
        OverlayOptions overlayOptions = null;
        Marker marker = null;
        //显示数据库中存在的所有的设备
        for( EquipInfo info : equipInfos ){
            latLng = new LatLng( Double.parseDouble(info.getWeidu()),
                    Double.parseDouble(info.getJingdu()) );
            //生成自定义标签
            /*BitmapDescriptor bdA = BitmapDescriptorFactory.fromBitmap(GLFont
                    .getImage(300, 100, info.getNumber(), 30, Color.WHITE));// 此处就是生成一个带文字的mark图片*/
            BitmapDescriptor bdA = BitmapDescriptorFactory.fromBitmap(GLFont
                    .getImage(200, 80, info.getNumber(), 30, Color.WHITE));// 此处就是生成一个带文字的mark图片
            overlayOptions = new MarkerOptions().position(latLng).icon(bdA)
                    .zIndex(5).perspective(true).title(info.getNumber());
            //图标
            marker = (Marker) (baiduMap.addOverlay(overlayOptions));
            Bundle bundle = new Bundle();
            bundle.putSerializable("info", info);
            marker.setExtraInfo(bundle);
        }
        }

}
