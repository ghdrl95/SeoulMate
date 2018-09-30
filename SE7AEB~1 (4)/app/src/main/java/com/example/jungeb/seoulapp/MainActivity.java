package com.example.jungeb.seoulapp;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jungeb.seoulapp.Facilities.SeoulFacilitiesAsyncTask;
import com.example.jungeb.seoulapp.Facilities.SeoulFacilitiesLocation;
import com.example.jungeb.seoulapp.Facilities.SeoulSetPointAsyncTask;
import com.example.jungeb.seoulapp.PushAlarm.GPSLocation;
import com.example.jungeb.seoulapp.PushAlarm.PushAlarm;
import com.example.jungeb.seoulapp.fragment.BookmarkTourFragment;
import com.example.jungeb.seoulapp.fragment.CultureTourFragment;
import com.example.jungeb.seoulapp.fragment.FestivalTourFragment;
import com.example.jungeb.seoulapp.fragment.KpopTourFragment;
import com.example.jungeb.seoulapp.fragment.TodayWeatherFragment;
import com.example.jungeb.seoulapp.fragment.TomorrowWeatherFragment;
import com.example.jungeb.seoulapp.sqliteC.SqliteTour;
import com.example.jungeb.seoulapp.weather.WeatherTC;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public class MainActivity extends AppCompatActivity {

    LinearLayout linearMainTour, linearMainFacilities;
    LinearLayout main1, main2;
    ImageView ivTourinfoBar, ivFacilitiesBar;
    TextView tvTourinfo, tvFacilities;
    Button btnTravelMore;
    ImageButton imbFold;
    ViewPager vpWeather, vpTourInfo;
    TabLayout tabTourInfo;
    PagerTabAdapter pagerTabAdapter;
    PagerWeatherAdapter pagerWeatherAdapter;
    LinearLayout linearFoldTab, linearFoldTab2;
    FrameLayout linearUnFold;
    TextView weather_name_unfold, weather_tc_unfold, weather_tmax_unfold, weather_tmin_unfold;
    double myLatitude = 0.0;
    double myLongitude = 0.0;
    LocationManager locationManager;

    ImageView last_visited_images[]= new ImageView[3];

    ImageView circle_1, circle_2;
    public static String language =""; //언어 가져옴 ko or en
    //성현이꺼
    String newTag="";
    RelativeLayout linMapView;
    TMapView tMapView;
    ImageButton ibtnSubwayIcon;
    ImageButton ibtnBusIcon;
    ImageButton ibtnTaxiIcon;
    ImageButton ibtnMedicineIcon;
    ImageButton ibtnWCICON;

    TextView tvSubwayText;
    TextView tvBusText;
    TextView tvTaxiText;
    TextView tvMedicineText;
    TextView tvWCText;

    SeoulFacilitiesAsyncTask seoulFacilitiesAsyncTask = null;
    HashMap<String, String> seoulFac = null;
    HashMap<String, String> seoulFac2 = null;
    JSONObject jsonObjectLocation;

    ArrayList<JSONArray> allLocation = null;

    //SeoulFacilitiesLocation[] toiletLocations;
    ArrayList<SeoulFacilitiesLocation> seoulFacilitiesLocations;

    ArrayList<TMapMarkerItem> tMapMarkerItems;
    //TMapMarkerItem[] tMapMarkerItemsCCTV;
    TMapPoint tMapPointCCTV;
    Bitmap bitmapCCTV;

    Geocoder geocoder;
    SeoulSetPointAsyncTask seoulSetPointAsyncTask;

    //성현이꺼
    int myLocationCheck=1;
    ImageView ivMyLocate;
    ImageView ivScopeUp;
    ImageView ivScopeDown;

    SqliteTour sqlite;

    PushAlarm pushAlarm;

    Intent GPSintent;
    public static double Latitude, Longitude;

    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewSetting_top(); //상단 뷰세팅
        viewSetting_weather(); //날씨 및 나의 여행기록 뷰세팅
        viewSetting_tab(); //문화 탭 뷰세팅
        setMyLocation();//GPS사용
        viewSetting_facilities();//편의시설 사용 세팅
        buttonClickEvent_facilities();//편의시설 이벤트 처리
        setting_init();//초기 편의시설 실행
        setHereLocation();
        sqlite = new SqliteTour(this, "Tour"); //DB 생성
        checkLocale();//언어설정 language

        pushAlarm = new PushAlarm(getApplicationContext());
        GPSintent = new Intent(getApplicationContext(), GPSLocation.class); //GPS 서비스 시작
        startService(GPSintent);

        int iamge_ids[] = {R.id.mainImage1,R.id.mainImage2,R.id.mainImage3};
        String urls[] = sqlite.selectTourVisitedLastThree();

        AssetManager assetManager = getAssets();


        for(int i=0;i<3;i++)
        {
            ImageView img = (ImageView) findViewById(iamge_ids[i]);
            if (urls[i] != null)
            {
                img.setImageBitmap(getBitmapFromAsset(".jpg",assetManager));
            }
            else
            {
                img.setImageResource(R.drawable.plus);
            }
        }

    }
    private Bitmap getBitmapFromAsset(String strName, AssetManager assetManager)
    {
        if(strName.equals("x.jpg")) return null;
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        stopService(GPSintent);
        unregisterReceiver(broadcastReceiver);
    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(broadcastReceiver, new IntentFilter(GPSLocation.str_receiver));
    }

    public void checkLocale() {
        Locale systemLocale = getApplicationContext().getResources().getConfiguration().locale;
        String strLanguage = systemLocale.getLanguage();
        language = strLanguage; //ko or en
    }

    // 상단 탭 기능
    View.OnClickListener toptabListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.linearMainTour:
                    main1.setVisibility(View.VISIBLE);
                    main2.setVisibility(View.GONE);
                    vpWeather.setCurrentItem(0);
                    vpTourInfo.setCurrentItem(0);
                    ivTourinfoBar.setImageResource(R.drawable.rectangle_purple);
                    ivFacilitiesBar.setImageResource(R.drawable.rectangle_gray);

                    tvTourinfo.setTextColor(Color.parseColor("#4170f2"));
                    tvFacilities.setTextColor(Color.parseColor("#949494"));
                    break;
                case R.id.linearMainFacilities:
                    main1.setVisibility(View.GONE);
                    main2.setVisibility(View.VISIBLE);
                    ivTourinfoBar.setImageResource(R.drawable.rectangle_gray);
                    ivFacilitiesBar.setImageResource(R.drawable.rectangle_purple);

                    tvTourinfo.setTextColor(Color.parseColor("#949494"));
                    tvFacilities.setTextColor(Color.parseColor("#4170f2"));
                    break;
            }
        }
    };

    //날씨 폴더식 기능
    View.OnClickListener foldListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imbFold:

                    linearFoldTab.setVisibility(linearFoldTab.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    linearFoldTab2.setVisibility(linearFoldTab2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    break;
                case R.id.linearUnfold:

                    linearFoldTab.setVisibility(linearFoldTab.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    linearFoldTab2.setVisibility(linearFoldTab2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);

                    break;
            }
        }
    };


    //날씨 뷰페이저 어댑터
    private class PagerWeatherAdapter extends FragmentStatePagerAdapter {
        public PagerWeatherAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new TodayWeatherFragment();
                case 1:
                    return new TomorrowWeatherFragment();
                default:

                    return null;
            }

        }
        @Override
        public int getCount() {
            return 2;
        }

    }


    //탭 뷰페이저 어댑터 (문화)
    private class PagerTabAdapter extends FragmentStatePagerAdapter {
        public PagerTabAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:

                    return (new BookmarkTourFragment());
                case 1:
                    return new CultureTourFragment();
                case 2:
                    return new KpopTourFragment();
                case 3:
                    return new FestivalTourFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    //상단 뷰세팅
    private void viewSetting_top() {
        main1 = (LinearLayout) findViewById(R.id.main1);
        main2 = (LinearLayout) findViewById(R.id.main2);
        linearMainTour = (LinearLayout) findViewById(R.id.linearMainTour);
        linearMainFacilities = (LinearLayout) findViewById(R.id.linearMainFacilities);
        ivTourinfoBar = (ImageView) findViewById(R.id.ivTourinfoBar);
        ivFacilitiesBar = (ImageView) findViewById(R.id.ivFacilitiesBar);
        tvTourinfo = (TextView) findViewById(R.id.tvTourinfo);
        tvFacilities = (TextView) findViewById(R.id.tvFacilities);

        linearMainTour.setOnClickListener(toptabListener);
        linearMainFacilities.setOnClickListener(toptabListener);
    }

    //날씨 및 나의 여행기록 뷰세팅
    private void viewSetting_weather() {
        imbFold = (ImageButton) findViewById(R.id.imbFold);
        linearUnFold = (FrameLayout) findViewById(R.id.linearUnfold);
        linearFoldTab = (LinearLayout) findViewById(R.id.linearFoldTab);
        linearFoldTab2 = (LinearLayout) findViewById(R.id.linearFoldTab2);

        imbFold.setOnClickListener(foldListener);
        linearUnFold.setOnClickListener(foldListener);

        weather_name_unfold = (TextView) findViewById(R.id.weather_name_unfold);
        weather_tc_unfold = (TextView) findViewById(R.id.weather_tc_unfold);
        weather_tmax_unfold = (TextView) findViewById(R.id.weather_tmax_unfold);
        weather_tmin_unfold = (TextView) findViewById(R.id.weather_tmin_unfold);

        circle_1 = (ImageView)findViewById(R.id.circle_1);
        circle_2 = (ImageView)findViewById(R.id.circle_2);

        //날씨 뷰페이저
        vpWeather = (ViewPager) findViewById(R.id.vpWeather);

        pagerWeatherAdapter = new PagerWeatherAdapter(getSupportFragmentManager());
        vpWeather.setAdapter(pagerWeatherAdapter);
        vpWeather.setCurrentItem(0);
        vpWeather.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        circle_1.setImageResource(R.drawable.circle_purple);
                        circle_2.setImageResource(R.drawable.circle_gray);
                        break;
                    case 1:
                        circle_1.setImageResource(R.drawable.circle_gray);
                        circle_2.setImageResource(R.drawable.circle_purple);
                        break;
                    default:
                        break;

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {


            }
        });

        btnTravelMore = (Button) findViewById(R.id.btnTravelMore);

        //나의 여행기록 이동기능
        btnTravelMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), TravelActivity.class);
                startActivity(i);
            }
        });


    }

    //문화 탭 뷰세팅
    private void viewSetting_tab() {
        //관광정보 탭 레이아웃
        tabTourInfo = (TabLayout) findViewById(R.id.tabTourInfo);
        vpTourInfo = (ViewPager) findViewById(R.id.vpTourInfo);

        pagerTabAdapter = new PagerTabAdapter(getSupportFragmentManager());
        vpTourInfo.setAdapter(pagerTabAdapter);
        vpTourInfo.setCurrentItem(0);
        tabTourInfo.setupWithViewPager(vpTourInfo);

        TextView bookmark = new TextView(getApplicationContext());
        TextView culture = new TextView(getApplicationContext());
        TextView kpop = new TextView(getApplicationContext());
        TextView festival = new TextView(getApplicationContext());

        bookmark.setText(R.string.asd4);
        culture.setText(R.string.asd5);
        kpop.setText(R.string.asd6);
        festival.setText(R.string.asd7);

        bookmark.setTextSize(12);
        culture.setTextSize(12);
        kpop.setTextSize(12);
        festival.setTextSize(12);

        bookmark.setGravity(Gravity.CENTER);
        culture.setGravity(Gravity.CENTER);
        kpop.setGravity(Gravity.CENTER);
        festival.setGravity(Gravity.CENTER);

        bookmark.setTextColor(Color.parseColor("#4170f2"));
        culture.setTextColor(Color.parseColor("#929292"));
        kpop.setTextColor(Color.parseColor("#929292"));
        festival.setTextColor(Color.parseColor("#929292"));

        tabTourInfo.getTabAt(0).setCustomView(bookmark);
        tabTourInfo.getTabAt(1).setCustomView(culture);
        tabTourInfo.getTabAt(2).setCustomView(kpop);
        tabTourInfo.getTabAt(3).setCustomView(festival);

        tabTourInfo.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int pos = tab.getPosition();
                switch (pos) {
                    case 0:
                        TextView tvBookmark0 = (TextView) tab.getCustomView();
                        tvBookmark0.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture0 = (TextView) tabTourInfo.getTabAt(1).getCustomView();
                        tvCulture0.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop0 = (TextView) tabTourInfo.getTabAt(2).getCustomView();
                        tvKpop0.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival0 = (TextView) tabTourInfo.getTabAt(3).getCustomView();
                        tvFestival0.setTextColor(Color.parseColor("#929292"));

                        break;
                    case 1:
                        TextView tvBookmark1 = (TextView) tab.getCustomView();
                        tvBookmark1.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture1 = (TextView) tabTourInfo.getTabAt(0).getCustomView();
                        tvCulture1.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop1 = (TextView) tabTourInfo.getTabAt(2).getCustomView();
                        tvKpop1.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival1 = (TextView) tabTourInfo.getTabAt(3).getCustomView();
                        tvFestival1.setTextColor(Color.parseColor("#929292"));

                        break;
                    case 2:
                        TextView tvBookmark2 = (TextView) tab.getCustomView();
                        tvBookmark2.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture2 = (TextView) tabTourInfo.getTabAt(0).getCustomView();
                        tvCulture2.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop2 = (TextView) tabTourInfo.getTabAt(1).getCustomView();
                        tvKpop2.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival2 = (TextView) tabTourInfo.getTabAt(3).getCustomView();
                        tvFestival2.setTextColor(Color.parseColor("#929292"));

                        break;
                    case 3:
                        TextView tvBookmark3 = (TextView) tab.getCustomView();
                        tvBookmark3.setTextColor(Color.parseColor("#4170f2"));

                        TextView tvCulture3 = (TextView) tabTourInfo.getTabAt(0).getCustomView();
                        tvCulture3.setTextColor(Color.parseColor("#929292"));

                        TextView tvKpop3 = (TextView) tabTourInfo.getTabAt(1).getCustomView();
                        tvKpop3.setTextColor(Color.parseColor("#929292"));

                        TextView tvFestival3 = (TextView) tabTourInfo.getTabAt(2).getCustomView();
                        tvFestival3.setTextColor(Color.parseColor("#929292"));

                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    //현재위치를 받아오는 코드
    public void setMyLocation() {
        //현재위치를 받아온다 GPS와 NETWORK둘다 사용
//        locationManager = (LocationManager) view.getSystemService(Context.LOCATION_SERVICE);
        locationManager = (LocationManager) getApplication().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplication(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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
        Toast.makeText(getApplication(), "현재위치 트래킹 시작", Toast.LENGTH_SHORT).show();
    }


    // retrofit api 통신 인터페이스 Key
    private interface WeatherApiInterface {
        String BASEURL = "https://api2.sktelecom.com/";
        //String APPKEY = "9f83e3e1-8412-4ee9-9857-97db3279aa66"; //도영쓰
        String APPKEY = "e9b1d69e-88e5-48be-8e1c-9489793fc462"; //성현쓰
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
        Call<WeatherTC> call = apiService.getMinutely(WeatherApiInterface.APPKEY, 1, latitude, longitude);
        call.enqueue(new Callback<WeatherTC>() { //retrodit callback 구현시 비동기/동기 둘다가능
            @Override
            public void onResponse(@NonNull Call<WeatherTC> call, @NonNull Response<WeatherTC> response) {
                if (response.isSuccessful()) {
                    //날씨데이터를 받아옴
                    Log.d("dda", "통신은 함");
                    WeatherTC object = response.body();
                    if (object != null) {
                        Log.d("dda", "데이터가 널은아님");
                        //데이터가 null 이 아니라면 날씨 데이터를 텍스트뷰로 보여주기
                        int tc = (int) Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTc());
                        int tmax = (int) Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTmax());
                        int tmin = (int) Double.parseDouble(object.getWeather().getMinutely().get(0).getTemperature().getTmin());

                        if(language.equals("ko")){
                            weather_name_unfold.setText(object.getWeather().getMinutely().get(0).getSky().getName());
                        }else if(language.equals("en")){
                            weather_name_unfold.setText(weatherLang(object.getWeather().getMinutely().get(0).getSky().getCode()));
                        }
                        weather_tc_unfold.setText(tc + "");
                        weather_tmax_unfold.setText(tmax + "");
                        weather_tmin_unfold.setText(tmin + "");
                    } else {
                        Log.d("dda", "데이터가 널");
                    }

                }else{
                    Toast.makeText(getApplicationContext(),"요청횟수 초과 / appKey 변경요망",Toast.LENGTH_SHORT);
                }
            }

            @Override
            public void onFailure(@NonNull Call<WeatherTC> call, @NonNull Throwable t) {
            }
        });
    }//retrofit api 통신
    public String weatherLang(String code) {

        switch (code) {
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

    private void viewSetting_facilities() {

        linMapView =(RelativeLayout) findViewById(R.id.linMapView);
        ibtnSubwayIcon = findViewById(R.id.ibtnSubwayIcon);
        ibtnBusIcon = findViewById(R.id.ibtnBusIcon);
        ibtnTaxiIcon = findViewById(R.id.ibtnTaxiIcon);
        ibtnMedicineIcon = findViewById(R.id.ibtnMedicineIcon);
        ibtnWCICON = findViewById(R.id.ibtnWCIcon);
        tvSubwayText = findViewById(R.id.tvSubwayText);
        tvBusText = findViewById(R.id.tvBusText);
        tvTaxiText = findViewById(R.id.tvTaxiText);
        tvMedicineText = findViewById(R.id.tvMedicineText);
        tvWCText = findViewById(R.id.tvWCIcon);
        ivMyLocate=(ImageView)findViewById(R.id.ivMyLocate);
        ivScopeUp=findViewById(R.id.ivScopeUp);
        ivScopeDown=findViewById(R.id.ivScopeDown);

        tMapView = new TMapView(getApplicationContext());
        tMapView.setSKTMapApiKey("8c01e13b-978f-41bc-8629-b223245f9203");
        linMapView.addView(tMapView);

        tMapMarkerItems = new ArrayList<TMapMarkerItem>();
        seoulFacilitiesLocations = new ArrayList<SeoulFacilitiesLocation>();
    }

    private void buttonClickEvent_facilities() {
        ibtnSubwayIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_activation);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#5d38db"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#929292"));

                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "StationAdresTelno");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();
                newTag="StationAdresTelno";

                connectCheck(newItem, newItem.get("SERVICE"), "RESULT2");

            }
        });


        ibtnBusIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_activation);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#5d38db"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));

                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "busStopLocationXyInfo");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"), "RESULT2");


            }
        });

        ibtnTaxiIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_activation);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#5d38db"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));

                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "Mgistaxistop");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"), "row3");
            }
        });

        ibtnWCICON.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_activation);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_disable);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#5d38db"));
                tvMedicineText.setTextColor(Color.parseColor("#929292"));
                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "MgisToilet");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();

                connectCheck(newItem, newItem.get("SERVICE"), "RESULT4");
            }
        });

        ibtnMedicineIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ibtnSubwayIcon.setImageResource(R.drawable.subway_disable);
                ibtnBusIcon.setImageResource(R.drawable.bus_disable);
                ibtnTaxiIcon.setImageResource(R.drawable.taxi_disable);
                ibtnWCICON.setImageResource(R.drawable.wc_disable);
                ibtnMedicineIcon.setImageResource(R.drawable.medicine_activation);
                tvSubwayText.setTextColor(Color.parseColor("#929292"));
                tvBusText.setTextColor(Color.parseColor("#929292"));
                tvTaxiText.setTextColor(Color.parseColor("#929292"));
                tvWCText.setTextColor(Color.parseColor("#929292"));
                tvMedicineText.setTextColor(Color.parseColor("#5d38db"));
                HashMap<String, String> newItem = new HashMap<String, String>();
                newItem.put("SERVICE", "parmacyBizInfo");
                newItem.put("START_INDEX", "1");
                newItem.put("END_INDEX", "1000");
                seoulFacilitiesLocations.clear();
                tMapView.removeAllMarkerItem();
                newTag="parmacyBizInfo";

                connectCheck(newItem, newItem.get("SERVICE"), "RESULT5");

            }
        });
    }

    public void setLocation(ArrayList<JSONArray> location, String dataSet) {
        Log.i("마이체크", "" + location);
        allLocation = location;
        seoulFacilitiesLocations = new ArrayList<SeoulFacilitiesLocation>();
        String lonType = "COT_COORD_X";
        String latType = "COT_COORD_Y";

        if (dataSet.equals("busStopLocationXyInfo")) {
            lonType = "XCODE";
            latType = "YCODE";
            extraction(lonType, latType, dataSet);
        } else if (dataSet.equals("MgisToilet")) {
            lonType = "COT_COORD_X";
            latType = "COT_COORD_Y";
            extraction(lonType, latType, dataSet);
        } else if (dataSet.equals("Mgistaxistop")) {
            lonType = "COT_COORD_X";
            latType = "COT_COORD_Y";
            extraction(lonType, latType, dataSet);
        } else if (dataSet.equals("parmacyBizInfo")) {

            seoulSetPointAsyncTask=new SeoulSetPointAsyncTask(location,MainActivity.this);
            seoulSetPointAsyncTask.execute("parmacyBizInfo");

//            Thread thread = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    extraction_other("ADDR_OLD", "parmacyBizInfo");
//                }
//            });
//            thread.start();

        } else if(dataSet.equals("StationAdresTelno")){
            seoulSetPointAsyncTask=new SeoulSetPointAsyncTask(location,MainActivity.this);
            seoulSetPointAsyncTask.execute("StationAdresTelno");
        }

//        for (int cnt = 0; cnt < allLocation.size(); cnt++) {
//            JSONArray locationTab1 = allLocation.get(cnt);
//            Log.i("잘왔닝", "" + locationTab1);
//
//            Log.i("분리각", "" + locationTab1.length());
//            try {
//                for (int setCnt = 0; setCnt < locationTab1.length(); setCnt++) {
//
//                    JSONObject temp = (JSONObject) locationTab1.get(setCnt);
//                    SeoulFacilitiesLocation seoulFacilitiesLocationItem=new SeoulFacilitiesLocation();
//                    seoulFacilitiesLocationItem.setLon(Double.parseDouble(temp.get("" +lonType)+""));
//                    seoulFacilitiesLocationItem.setLat(Double.parseDouble(temp.get(""+latType)+""));
//
//                    seoulFacilitiesLocations.add(seoulFacilitiesLocationItem);
//                }
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        Log.i("전체는?",""+seoulFacilitiesLocations.size());
//
//        markPoint(dataSet);


    }

    public void extraction(String lonType, String latType, String dataSet) {
        for (int cnt = 0; cnt < allLocation.size(); cnt++) {
            JSONArray locationTab1 = allLocation.get(cnt);
            Log.i("잘왔닝", "" + locationTab1);

            Log.i("분리각", "" + locationTab1.length());
            try {
                for (int setCnt = 0; setCnt < locationTab1.length(); setCnt++) {

                    JSONObject temp = (JSONObject) locationTab1.get(setCnt);
                    SeoulFacilitiesLocation seoulFacilitiesLocationItem = new SeoulFacilitiesLocation();
                    seoulFacilitiesLocationItem.setLon(Double.parseDouble(temp.get("" + lonType) + ""));
                    seoulFacilitiesLocationItem.setLat(Double.parseDouble(temp.get("" + latType) + ""));

                    seoulFacilitiesLocations.add(seoulFacilitiesLocationItem);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("전체는?", "" + seoulFacilitiesLocations.size());

        markPoint(dataSet);
    }


    public void setOnePoint(TMapPoint tMapPoint){
        if(newTag.equals("parmacyBizInfo")){
            bitmapCCTV = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.medicine_spot);
        }else if(newTag.equals("StationAdresTelno")){
            bitmapCCTV = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.subway_spot);
        }

        int tSize = seoulFacilitiesLocations.size();
        SeoulFacilitiesLocation seoulFacilitiesLocationItem = new SeoulFacilitiesLocation();
        seoulFacilitiesLocationItem.setLon(tMapPoint.getLongitude());
        seoulFacilitiesLocationItem.setLat(tMapPoint.getLatitude());
        seoulFacilitiesLocations.add(seoulFacilitiesLocationItem);

        for (int tCount = 0; tCount < tSize; tCount++) {
            TMapMarkerItem tempMarker = new TMapMarkerItem();
            //TMapPoint tMapPoint=new TMapPoint();
            tempMarker.setTMapPoint(new TMapPoint(seoulFacilitiesLocations.get(tCount).getLat(), seoulFacilitiesLocations.get(tCount).getLon()));
            tempMarker.setIcon(bitmapCCTV);
            tempMarker.setPosition(0.5f, 1.0f);
            tMapView.addMarkerItem(tCount + "", tempMarker);
            Log.i("거리거리", "" + seoulFacilitiesLocations.get(tCount).getLat());
            Log.i("찍혔냐", "찍?" + tCount);

        }

    }

    public List<Address> addrToLatLon(String addrName) {
        List<Address> list = null;
        try {
            list = geocoder.getFromLocationName(addrName, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (list != null) {
            if (list.size() == 0) {
                Log.i("문제야문제", "해당되는 주소 정보가 없다");
            } else {
                list.get(0).getLatitude();
                list.get(0).getLongitude();

                Log.i("찾았다요놈", "" + list.get(0).getLatitude() + " / " + list.get(0).getLongitude());
            }
        }

        return list;

    }


    public void connectCheck(HashMap<String, String> location, String where, String result) {
        ConnectivityManager connectivityManager = (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if ((networkInfo != null) && networkInfo.isConnected()) {
            seoulFacilitiesAsyncTask = new SeoulFacilitiesAsyncTask(location, this);
            seoulFacilitiesAsyncTask.execute("" + where, result);


        } else {
            Toast.makeText(getApplicationContext(), "인터넷 연결상태를 확인해 주세요", Toast.LENGTH_SHORT).show();
        }
    }

    public void markPoint(String dataSet) {
        int tCount = 0;
        int tSize = seoulFacilitiesLocations.size();


        if (dataSet.equals("busStopLocationXyInfo")) {
            bitmapCCTV = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.bus25);
        } else if (dataSet.equals("MgisToilet")) {
            bitmapCCTV = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.wc_spot);
        } else if (dataSet.equals("Mgistaxistop")) {
            bitmapCCTV = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.taxi_spot);
        } else if (dataSet.equals("parmacyBizInfo")) {
            bitmapCCTV = BitmapFactory.decodeResource(getApplication().getResources(), R.drawable.medicine_spot);

        }

        for (tCount = 0; tCount < tSize; tCount++) {
            TMapMarkerItem tempMarker = new TMapMarkerItem();
            //TMapPoint tMapPoint=new TMapPoint();
            tempMarker.setTMapPoint(new TMapPoint(seoulFacilitiesLocations.get(tCount).getLat(), seoulFacilitiesLocations.get(tCount).getLon()));
            tempMarker.setIcon(bitmapCCTV);
            tempMarker.setPosition(0.5f, 1.0f);
            tMapView.addMarkerItem(tCount + "", tempMarker);
            Log.i("거리거리", "" + seoulFacilitiesLocations.get(tCount).getLat());
            Log.i("찍혔냐", "찍?" + tCount);

        }

    }

    public void setting_init() {
        geocoder = new Geocoder(this);
        seoulFac = new HashMap<String, String>();
        seoulFac.put("SERVICE", "MgisToilet");
        seoulFac.put("START_INDEX", "1");
        seoulFac.put("END_INDEX", "1000");
        connectCheck(seoulFac, "MgisToilet", "row1");

    }
    //LocationManager
    public final LocationListener mLocationListener=new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Log.d("locationListener","onLocationChanged, Location : "+location);
                double longitude=location.getLongitude();//경도
                double latitude=location.getLatitude();//위도
                double altitude=location.getAltitude();//고도
                float accuracy=location.getAccuracy();//정확도
                String provider=location.getProvider();//위치제공자

                myLatitude=latitude;
                myLongitude=longitude;

                getWeather(myLatitude, myLongitude);



                tMapView.setLocationPoint(longitude,latitude);
                tMapView.setCenterPoint(longitude,latitude);
                //GPS위치제공자에 의한 위치변화는 오차범위가 좁다. Network위치 제공자에 의한 위치변화는 GPS에 비해 정확도가 떨어진다.

                //tv.setText("위치정보 : "+ provider+"\n위도 : "+longitude+"\n경도 : "+latitude+"\n고도 : "+altitude+"\n정확도 : "+accuracy );
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

    public void setHereLocation(){
        //현재위치로 포커싱
        ivMyLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myLocationCheck == 1) {
                    locationManager.removeUpdates(mLocationListener);
                    Toast.makeText(MainActivity.this, "현재위치 트래킹 종료", Toast.LENGTH_SHORT).show();
                    tMapView.setIconVisibility(false);
                    tMapView.setTrackingMode(false);
                    tMapView.setCompassMode(false);
                    myLocationCheck = 2;

                } else if (myLocationCheck == 2) {
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,100,1,mLocationListener);
                    Toast.makeText(MainActivity.this, "현재위치 트래킹 시작", Toast.LENGTH_SHORT).show();
                    tMapView.setIconVisibility(true);
                    tMapView.setTrackingMode(true);
                    myLocationCheck=3;

                }else if(myLocationCheck==3){
                    tMapView.setCompassMode(true);
                    Toast.makeText(MainActivity.this, "컴퍼스 모드 시작", Toast.LENGTH_SHORT).show();
                    myLocationCheck=1;
                }

            }
        });
        ivScopeUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tMapView.MapZoomIn();
                Log.i("hereisMuZoom",""+tMapView.getZoomLevel());

            }
        });
       ivScopeDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tMapView.MapZoomOut();
                Log.i("hereisMuZoom",""+tMapView.getZoomLevel());

            }
        });

    }
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("MainActivity","Locaiton 수신 완료");
            Latitude = Double.valueOf(intent.getStringExtra("latutide"));
            Longitude = Double.valueOf(intent.getStringExtra("longitude")); //GPS 서비스에서 값 가져옴

            List resultlist = sqlite.CheckVisit(Latitude, Longitude, language);

            if (!resultlist.isEmpty()){
                pushAlarm.VisitMessages(getApplicationContext(), language, resultlist); //관광지 방문시 언어에 따른 알림 출력
            }
        }
    };

    Handler m_Handler=new Handler()
    {
        //항목 리스트 처리
        @Override
        public void handleMessage(Message msg) {

        }
    };

}