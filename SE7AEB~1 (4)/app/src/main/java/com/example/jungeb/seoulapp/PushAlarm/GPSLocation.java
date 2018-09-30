package com.example.jungeb.seoulapp.PushAlarm;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class GPSLocation extends Service implements LocationListener {
    boolean isGPSEnable = false;
    boolean isNetworkEnable = false;
    double latitude, longitude;
    LocationManager locationManager;
    Location location;
    private Handler mHandler = new Handler();
    private Timer mTimer = null;
    long notify_interval = 10000; //갱신 시간
    public static String str_receiver = "com.example.jungeb.seoulapp.receiver";
    Intent intent;
    public GPSLocation() {
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        Log.d("GPSLocation","start");
        super.onCreate();
        mTimer = new Timer();
        mTimer.schedule(new TimerTaskToGetLocation(), 5, notify_interval);
        intent = new Intent(str_receiver);
    }
    @Override
    public void onDestroy() {
        Log.d("GPSLocation","Destroy");
        super.onDestroy();
    }
    @Override
    public void onLocationChanged(Location location) {
        fn_getlocation();
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }
    @Override
    public void onProviderEnabled(String provider) {
    }
    @Override
    public void onProviderDisabled(String provider) {
    }

    private void fn_locationupdate(String provider){ //실제 위도 경도 호출 부분
        location = null;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } //퍼미션 있는지 확인
        locationManager.requestLocationUpdates(provider, 1000, 10, this);
        if (locationManager != null) {
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) { //로케이션 가져와서 브로드캐스트로 넘김
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                Log.e("latitude", location.getLatitude() + "");
                Log.e("longitude", location.getLongitude() + "");
                intent.putExtra("latutide",location.getLatitude()+"");
                intent.putExtra("longitude",location.getLongitude()+"");
                sendBroadcast(intent);
            }
        }
    }

    private void fn_getlocation() { //GPS 정보 가져오기
        locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
        isGPSEnable = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER); //network, gps 사용 여부 확인
        isNetworkEnable = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (isNetworkEnable) {
            fn_locationupdate(LocationManager.NETWORK_PROVIDER); //NETWORK_PROVIDER 가능하면 가져오기
        }
        if (isGPSEnable){
            fn_locationupdate(LocationManager.GPS_PROVIDER);//GPS_PROVIDER 가능하면 가져오기
        }
    }
    private class TimerTaskToGetLocation extends TimerTask {
        @Override
        public void run() {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    fn_getlocation();
                }
            }); //타이머 시간 마다 GPS 정보 가져오는 함수 실행

        }
    }


}