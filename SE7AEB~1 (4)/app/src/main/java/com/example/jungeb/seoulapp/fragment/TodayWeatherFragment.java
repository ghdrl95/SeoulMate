package com.example.jungeb.seoulapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.weather.WeatherTC;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class TodayWeatherFragment extends Fragment{
    ImageView weather_image; //날씨 이미지
    TextView weather_now; // 현재온도
    TextView weather_name; //현재날씨이름
    TextView weather_tmax; //최고기온
    TextView weather_tmin; //최저기온

    LocationManager locationManager;
    TextView tvWeek;
    String strLanguage;
    double lat; //위도
    double lon; //경도
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_weather, container, false);

        //위도 경도를 받아와서 getWeather에 넣어주기


        weather_image = (ImageView)view.findViewById(R.id.weather_image);
        weather_now = (TextView) view.findViewById(R.id.weather_now);
        weather_name = (TextView) view.findViewById(R.id.weather_name);
        weather_tmax = (TextView) view.findViewById(R.id.weather_tmax);
        weather_tmin = (TextView) view.findViewById(R.id.weather_tmin);



        setMyLocation();

        tvWeek = (TextView)view.findViewById(R.id.tvWeek);
        Date d_today = new Date ( );
        SimpleDateFormat sdf3 = new SimpleDateFormat("E");
        String strtoday = sdf3.format(d_today);
        tvWeek.setText(strtoday+"");

        strLanguage = Locale.getDefault().toString();
        return view;


    }



    private interface WeatherApiInterface{
        String BASEURL = "https://api2.sktelecom.com/";
        //String APPKEY = "9f83e3e1-8412-4ee9-9857-97db3279aa66";//도영쓰
        String APPKEY = "e9b1d69e-88e5-48be-8e1c-9489793fc462";//성현쓰
        //String APPKEY = "67e8fa68-9576-4391-b52c-9d2520980202";//성현쓰2
        //get 메소드를 통한 http rest api 통신
        @GET("weather/current/minutely")
        Call<WeatherTC> getMinutely(@Header("appKey") String appKey, @Query("version") int version,
                                    @Query("lat") double lat, @Query("lon") double lon);
    }
    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WeatherApiInterface.BASEURL)
                .build();
        WeatherApiInterface apiService = retrofit.create(WeatherApiInterface.class);
        Call< WeatherTC> call = apiService.getMinutely(WeatherApiInterface.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback< WeatherTC>() { //retrodit callback 구현시 비동기/동기 둘다가능
            @Override
            public void onResponse(@NonNull Call< WeatherTC> call, @NonNull Response< WeatherTC> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    Log.d("dda","통신은 함");
                    WeatherTC object = response.body();
                    if (object != null) {
                        Log.d("dda","데이터가 널은아님");
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기
                        weatherCode(object.getWeather().getMinutely().get(0).getSky().getCode());
                        int tc =(int)Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTc());
                        int tmax = (int)Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTmax());
                        int tmin = (int)Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTmin());
                        //;


                        if(strLanguage.equals("ko_KR")){
                            weather_name.setText(object.getWeather().getMinutely().get(0).getSky().getName());
                        }else if(strLanguage.equals("en_US")){
                            weather_name.setText(weatherLang(object.getWeather().getMinutely().get(0).getSky().getCode()));
                        }
                        weather_now.setText(tc+"");
                        weather_tmax.setText(tmax+"");
                        weather_tmin.setText(tmin+"");
                    }else {
                        Log.d("dda","데이터가 널");
                    }

                }else{
                }
            }

            @Override
            public void onFailure(@NonNull Call< WeatherTC> call, @NonNull Throwable t) {
            }
        });
    }
    public void weatherCode(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_A01":
                weather_image.setImageResource(R.drawable.weather_sun);
                break;
            case "SKY_A02":
                weather_image.setImageResource(R.drawable.weather_cloud);
                break;
            case "SKY_A03":
                weather_image.setImageResource(R.drawable.weather_cloud_more);
                break;
            case "SKY_A04":
                weather_image.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_A05":
                weather_image.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_A06":
                weather_image.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            case "SKY_A07":
                weather_image.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_A08":
                weather_image.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_A09":
                weather_image.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_A10":
                weather_image.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            case "SKY_A11":
                weather_image.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_A12":
                weather_image.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_A13":
                weather_image.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_A14":
                weather_image.setImageResource(R.drawable.weather_snow_or_rain);
                break;
                default:
                    break;
        }

    }
    public String weatherLang(String code){

        switch (code){
            case "SKY_A01":
                return "Sunny";
            case "SKY_A02":
                return "Cloudy";
            case "SKY_A03":
                return "Cloudy";
            case "SKY_A04":
                return "Rainy";
            case "SKY_A05":
                return "Snow";
            case "SKY_A06":
                return "Snow or Rain";
            case "SKY_A07":
                return "Cloudy";
            case "SKY_A08":
                return "Rainy";
            case "SKY_A09":
                return "Snow";
            case "SKY_A10":
                return "Snow or Rain";
            case "SKY_A11":
                return "Cloudy";
            case "SKY_A12":
                return "Rainy";
            case "SKY_A13":
                return "Snow";
            case "SKY_A14":
                return "Snow or Rain";
            default:
                return null;
        }
    }

    public void setMyLocation() {
        //현재위치를 받아온다 GPS와 NETWORK둘다 사용
//        locationManager = (LocationManager) view.getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager)getActivity().getApplication().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity().getApplication(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity().getApplication(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
        }
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
//        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
    }
    public final LocationListener mLocationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.d("locationListener","onLocationChanged, Location : "+location);
            lon=location.getLongitude();//경도
            lat=location.getLatitude();//위도
            double altitude=location.getAltitude();//고도
            float accuracy=location.getAccuracy();//정확도
            String provider=location.getProvider();//위치제공자

            getWeather(lat,lon);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d("locationListener", "onStatusChanged, provider:" + provider + ", status:" + status + " ,Bundle:" + extras);

        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d("locationListener", "onProviderEnabled, provider:" + provider);
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d("locationListener", "onProviderDisabled, provider:" + provider);
        }
    };
}

