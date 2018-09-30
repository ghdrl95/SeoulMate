package com.example.jungeb.seoulapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class CheckPermissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_permission);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int result1 = new PermissionRequester.Builder(CheckPermissionActivity.this)
                .setTitle("권한 요청")
                .setMessage("권한을 요청합니다.")
                .setPositiveButtonName("네")
                .setNegativeButtonName("아니오")
                .create()
                .request(Manifest.permission.ACCESS_FINE_LOCATION, 1000, new PermissionRequester.OnClickDenyButtonListener() {
                    @Override
                    public void onClick(Activity activity) {
                        Log.d("xxx", "취소함");
                    }
                });


        if (result1 == PermissionRequester.ALREADY_GRANTED) {
            Log.d("RESULT", "권한이 이미 존재함");
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                finish();
            } else if (result1 == PermissionRequester.NOT_SUPPORT_VERSION) {
                Log.d("RESULT", "마시멜로우 이상 버전이 아님");
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                finish();
            } else if (result1 == PermissionRequester.REQUEST_PERMISSION) {
                Log.d("RESULT", "요청함. 응답을 기다림");
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }

    //권한 요청에 대한 응답은 이곳에서 가져온다
    //@param requestCode 요청코드
    //@param permissions 사용자가 요청한 권한들
    //@param grantResults 권한에 대한 응답들(인덱스별로 매칭)

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 1000) {

            //요청한 권한을 사용자가 허용했다면
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(getApplicationContext(), SplashActivity.class);


                //Add Check Permission
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        ) {
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, "권한 요청을 거부했습니다", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
