package test.com.baidumap;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;


public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks{


    /*
    qweqweqwe
    qweqweqwe
    qwe
    qw
    eqw
    e
    qwe
    qw
    e
    qwe
    q
    we
    qweqwe
     */



    private static final int BAIDU_READ_PHONE_STATE =100;
    private LocationClient mLocClient;
    public MyLocationListenner myListener = new MyLocationListenner();
    private MapView mMapView = null;
    private BaiduMap mBaiduMap = null;

    String[] s = new String[]{Manifest.permission.READ_PHONE_STATE,Manifest.permission.ACCESS_COARSE_LOCATION ,
            Manifest.permission.ACCESS_FINE_LOCATION ,Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，传入ApplicationContext
        // 注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
        mMapView = (MapView) findViewById(R.id.id_bmapView);
        mBaiduMap = mMapView.getMap();
        //普通地图

        methodRequiresTwoPermission();



    }
    @AfterPermissionGranted(500)
    private void methodRequiresTwoPermission() {

        if (EasyPermissions.hasPermissions(this, s)) {
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            mLocClient = new LocationClient(this);  //定位用到的一个类
            mLocClient.registerLocationListener(myListener); //注册监听
            ///LocationClientOption类用来设置定位SDK的定位方式，
            LocationClientOption option = new LocationClientOption(); //以下是给定位设置参数
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            option.setIsNeedLocationDescribe(true);
            option.setIsNeedAddress(true);
            mLocClient.setLocOption(option);
            mLocClient.start();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this,"dsafdf",
                    500, s);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    boolean isFirstLoc = true; // 是否首次定位

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        if(perms.size()==5){
            mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);



            // 开启定位图层
            mBaiduMap.setMyLocationEnabled(true);
            mLocClient = new LocationClient(this);  //定位用到的一个类
            mLocClient.registerLocationListener(myListener); //注册监听
            ///LocationClientOption类用来设置定位SDK的定位方式，
            LocationClientOption option = new LocationClientOption(); //以下是给定位设置参数
            option.setOpenGps(true); // 打开gps
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(1000);
            option.setIsNeedLocationDescribe(true);
            option.setIsNeedAddress(true);
            mLocClient.setLocOption(option);
            mLocClient.start();
        }
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }

    public class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            //检测状态码
            int locType = location.getLocType();
            String locTypeDescription = location.getLocTypeDescription();
            String addrStr = location.getAddrStr();


            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                       location.getLocationDescribe();

                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

}
