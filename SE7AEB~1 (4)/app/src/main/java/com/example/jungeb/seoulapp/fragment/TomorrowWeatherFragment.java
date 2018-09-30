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

import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.weather.WeatherData;

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

public class TomorrowWeatherFragment extends Fragment {
    ImageView weather_image_tomorrow; //내일 이미지
    TextView weather_name_tomorrow; //내일 날씨명
    TextView weather_tmax_tomorrow; //내일 최고기온
    TextView weather_tmin_tomorrow; //내일 최저기온

    ImageView weather_image_dayafter; //모레 이미지
    TextView weather_name_dayafter; //모레 날씨명
    TextView weather_tmax_dayafter; //모레 최고기온
    TextView weather_tmin_dayafter; //모레 최저기온

    TextView tv_tomorrow;
    TextView tv_dayafter;


    LocationManager locationManager;
    String strLanguage;
    double lat; //위도
    double lon; //경도

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_tomorrow_weather, container, false);

        weather_image_tomorrow = (ImageView) view.findViewById(R.id.weather_image_tomorrow);
        weather_name_tomorrow = (TextView) view.findViewById(R.id.weather_name_tomorrow);
        weather_tmax_tomorrow = (TextView) view.findViewById(R.id.weather_tmax_tomorrow);
        weather_tmin_tomorrow = (TextView) view.findViewById(R.id.weather_tmin_tomorrow);

        weather_image_dayafter = (ImageView) view.findViewById(R.id.weather_image_dayafter);
        weather_name_dayafter = (TextView) view.findViewById(R.id.weather_name_dayafter);
        weather_tmax_dayafter = (TextView) view.findViewById(R.id.weather_tmax_dayafter);
        weather_tmin_dayafter = (TextView) view.findViewById(R.id.weather_tmin_dayafter);

        setMyLocation();

        tv_tomorrow = (TextView)view.findViewById(R.id.tv_tomorrow);
        tv_dayafter = (TextView)view.findViewById(R.id.tv_dayafter);
        Date d_today = new Date ( );
        Date d_tomorrow = new Date ( d_today.getTime ( ) + (long) ( 1000 * 60 * 60 * 24 ) );
        Date d_dayafter = new Date ( d_tomorrow.getTime ( ) + (long) ( 1000 * 60 * 60 * 24 ) );
        SimpleDateFormat sdf3 = new SimpleDateFormat("E");

        String tomorrow = sdf3.format(d_tomorrow);
        String dayafter = sdf3.format(d_dayafter);
        tv_tomorrow.setText(tomorrow+"");
        tv_dayafter.setText(dayafter+"");
        strLanguage = Locale.getDefault().toString();

        return view;
    }

    private interface WeatherApiInterface{
        String BASEURL = "https://api2.sktelecom.com/";
        //String APPKEY = "9f83e3e1-8412-4ee9-9857-97db3279aa66";//도영쓰
        String APPKEY = "e9b1d69e-88e5-48be-8e1c-9489793fc462";//성현쓰
        //String APPKEY = "67e8fa68-9576-4391-b52c-9d2520980202";//성현쓰2
        //get 메소드를 통한 http rest api 통신
        @GET("weather/summary")
        Call<WeatherData> getMinutely(@Header("appKey") String appKey, @Query("version") int version,
                                      @Query("lat") double lat, @Query("lon") double lon);
    }
    private void getWeather(double latitude, double longitude) {
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(WeatherApiInterface.BASEURL)
                .build();
        WeatherApiInterface apiService = retrofit.create(WeatherApiInterface.class);
        Call<WeatherData> call = apiService.getMinutely(WeatherApiInterface.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback<WeatherData>() { //retrodit callback 구현시 비동기/동기 둘다가능
            @Override
            public void onResponse(@NonNull Call<WeatherData> call, @NonNull Response<WeatherData> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    Log.d("ddd","통신은 함");
                    WeatherData object = response.body();
                    if (object != null) {
                        Log.d("ddd","데이터가 널은아님");
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기

                        weatherCode_tomorrow(object.getWeather().getSummary().get(0).getTomorrow().getSky().getCode());

                        weatherCode_dayafter(object.getWeather().getSummary().get(0).getDayAftertomorrow().getSky().getCode());

                        int tmax_tomorrow = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getTomorrow().getTemperature().getTmax());
                        int tmin_tomorrow = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getTomorrow().getTemperature().getTmin());


                        if(strLanguage.equals("en_US")){
                            weather_name_tomorrow.setText(weatherLang_tomorrow(object.getWeather().getSummary().get(0).getTomorrow().getSky().getCode()));
                        }else if(strLanguage.equals("ko_KR")){
                            weather_name_tomorrow.setText(object.getWeather().getSummary().get(0).getTomorrow().getSky().getName().toString());
                        }
                        weather_tmax_tomorrow.setText(tmax_tomorrow+"");
                        weather_tmin_tomorrow.setText(tmin_tomorrow+"");

                        int tmax_dayafter = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getDayAftertomorrow().getTemperature().getTmax());
                        int tmin_dayafter = (int)Double.parseDouble(object.getWeather().getSummary().get(0).getDayAftertomorrow().getTemperature().getTmin());

                        if(strLanguage.equals("en_US")){
                            weather_name_dayafter.setText(weatherLang_dayafter(object.getWeather().getSummary().get(0).getDayAftertomorrow().getSky().getCode()));
                        }else if(strLanguage.equals("ko_KR")){
                            weather_name_dayafter.setText(object.getWeather().getSummary().get(0).getDayAftertomorrow().getSky().getName().toString());
                        }
                        weather_tmax_dayafter.setText(tmax_dayafter+"");
                        weather_tmin_dayafter.setText(tmin_dayafter+"");
                    }else {
                        Log.d("ddd","데이터가 널");
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherData> call, @NonNull Throwable t) {
            }
        });
    }
    public void weatherCode_tomorrow(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_M01":
                weather_image_tomorrow.setImageResource(R.drawable.weather_sun);
                break;
            case "SKY_M02":
                weather_image_tomorrow.setImageResource(R.drawable.weather_cloud);
                break;
            case "SKY_M03":
                weather_image_tomorrow.setImageResource(R.drawable.weather_cloud_more);
                break;
            case "SKY_M04":
                weather_image_tomorrow.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_M05":
                weather_image_tomorrow.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_M06":
                weather_image_tomorrow.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_M07":
                weather_image_tomorrow.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            default:
                break;
        }
    }
    public String weatherLang_tomorrow(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_M01":
                return "Sunny";
            case "SKY_M02":
                return "Cloudy";
            case "SKY_M03":
                return "Cloudy";
            case "SKY_M04":
                return "Cloudy";
            case "SKY_M05":
                return "Rainy";
            case "SKY_M06":
                return "Snow";
            case "SKY_M07":
                return "Snow or Rain";
            default:
                return null;
        }
    }

    public void weatherCode_dayafter(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_M01":
                weather_image_dayafter.setImageResource(R.drawable.weather_sun);

                break;
            case "SKY_M02":
                weather_image_dayafter.setImageResource(R.drawable.weather_cloud);
                break;
            case "SKY_M03":
                weather_image_dayafter.setImageResource(R.drawable.weather_cloud_more);
                break;
            case "SKY_M04":
                weather_image_dayafter.setImageResource(R.drawable.weather_blur);
                break;
            case "SKY_M05":
                weather_image_dayafter.setImageResource(R.drawable.weather_rain);
                break;
            case "SKY_M06":
                weather_image_dayafter.setImageResource(R.drawable.weather_snow);
                break;
            case "SKY_M07":
                weather_image_dayafter.setImageResource(R.drawable.weather_snow_or_rain);
                break;
            default:
                break;
        }
    }
    public String weatherLang_dayafter(String code){
        Log.d("ddd",code+"");
        switch (code){
            case "SKY_M01":
                return "Sunny";
            case "SKY_M02":
                return "Cloudy";
            case "SKY_M03":
                return "Cloudy";
            case "SKY_M04":
                return "Cloudy";
            case "SKY_M05":
                return "Rainy";
            case "SKY_M06":
                return "Snow";
            case "SKY_M07":
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
            getWeather(lat, lon);

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
