# 公路环境监测app-说明文档

​	本应用基于android studio 4.1.2版本开发，sdk版本最低为23，即android 6.0以上版本。涉及两大主要部分：百度地图api调用实现定位功能、远程连接云服务器的mysql数据库实现数据库CUDR操作（包括安卓端的登录验证、用户注册、密码查看、实时数据显示以及历史数据查询）。

其中数据库连接函数为本人服务器ip，端口和账号密码已经更改，使用时需改成你自己的。数据库操作类和函数需要根据个人需要进行调整。

### 本代码为本人个人开发，转载请标注出处，谢谢合作！

## 准备工作：首先要引入网络权限以及需要用的jar包（msql和baidumap）

##### 1、在文件AndroidManifest.xml中添加

```xml
<!--网络权限-->
<uses-permission android:name="android.permission.INTERNET" />

<!--百度地图需要的权限-->
 <uses-permission android:name="com.android.launcher.permission.READ_SETTINGS" />
    <uses-permission android:name="android.permission.GET_ACCOUNTS" />
    <uses-permission android:name="android.permission.READ_PROFILE" />
    <uses-permission android:name="android.permission.READ_CONTACTS" />
    <uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
    <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
    <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
    <uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
    <uses-permission android:name="android.permission.READ_PHONE_STATE" />
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
    <uses-permission
        android:name="android.permission.MOUNT_UNMOUNT_FILESYSTEMS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="android.permission.CAMERA" />
    <uses-permission android:name="android.permission.INTERNET" />
    <uses-permission android:name="android.permission.VIBRATE" />
    <uses-permission android:name="android.permission.FLASHLIGHT" />
<!--百度地图api的key，自己申请-->
	<meta-data
            android:name="com.baidu.lbsapi.API_KEY"
            android:value="百度地图api的key" />

<!--百度地图服务调用-->
	<service
            android:name="com.baidu.location.f"
            android:enabled="true"
            android:process=":remote" />


```

##### 2、在文件build.gradle(app)中引入jar包

```java
    implementation fileTree(include: ['*.jar'], dir: 'libs')

	//mysql数据库jar包    
	implementation files('libs/mysql-connector-java-5.1.7-bin.jar')
```

##### 3、按照mysql驱动包和百度地图api驱动包存放规则放好

##### 4、java源文件

界面程序：

​	（1）登录页面-->MainActivity

​	（2）注册页面-->UserAddActivity

​	（3）忘记密码页面-->ForgetActivity

​	（4）数据实时显示页面-->BaiduMapActivity

​	（5）历史数据查询-->DateActivity

辅助程序：

​	（1）CommonUtils-->通用辅助类（一些函数）

​	（2）DBOpenHelper-->数据库连接辅助类

​	（3）LvRoadinfoAdapter-->历史数据显示适配器

​	（4）MysqlHelp--> 数据库操作类（部分功能）

​	（5）UserDao-->数据库操作类（主要功能）

​	（6）Userinfo和Roadinfo-->分别是用户和公路采集信息的辅助类
