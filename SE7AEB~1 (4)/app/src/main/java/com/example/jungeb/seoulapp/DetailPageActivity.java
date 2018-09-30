package com.example.jungeb.seoulapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.jungeb.seoulapp.PushAlarm.getPhotoThread;
import com.example.jungeb.seoulapp.WebRequest.RequestHttpURLConnection;
import com.example.jungeb.seoulapp.fragment.DetailFirstFragment;
import com.example.jungeb.seoulapp.fragment.DetailSecondFragment;
import com.example.jungeb.seoulapp.fragment.DetailThirdFragment;
import com.example.jungeb.seoulapp.sqliteC.SqliteTour;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DetailPageActivity extends AppCompatActivity {
    String Content;
    double lat;
    double lon;
    ImageButton imbDetailBack;
    ViewPager vpDetailImages;
    PagerDetailAdapter pagerDetailAdapter;
    String isBookMark, title;
    String m_id,place_ID;
    int  category;
    ImageView ivDetailFirst, ivDetailSecond, ivDetailThird;
    ImageView detailImage;
    TextView tvDetailTitle,tvDetailContent,tvDetailAddress,tvDetailDate,tvDetailTime,tvDetailNumber;
    RatingBar ratingStar;
    SqliteTour sq;
    String URL ;
    @Override
    protected void onDestroy(){
        super.onDestroy();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_page);

        //뒤로가기 버튼 눌렀을 때
        imbDetailBack = (ImageButton) findViewById(R.id.imbDetailBack);

        imbDetailBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        m_id = intent.getStringExtra("ID");
        place_ID = intent.getStringExtra("PlaceID");
        category = intent.getIntExtra("Category",1) - 1;
        isBookMark = intent.getStringExtra("BookMark");
        lat = intent.getDoubleExtra("Latitude",0);
        lon = intent.getDoubleExtra("Longitude",0);
        Content = intent.getStringExtra("Content");
        URL = intent.getStringExtra("URL");

        //사진 뷰페이저
        vpDetailImages = (ViewPager) findViewById(R.id.vpDetailImages);

        pagerDetailAdapter = new PagerDetailAdapter(getSupportFragmentManager(),place_ID);
        vpDetailImages.setAdapter(pagerDetailAdapter);
        vpDetailImages.setCurrentItem(0);


        ratingStar = findViewById(R.id.ratingStar); //별점
        tvDetailNumber = findViewById(R.id.tvDetailNumber); //전화번호
        tvDetailAddress = findViewById(R.id.tvDetailAddress); //주소
        tvDetailDate = findViewById(R.id.tvDetailDate); //운영 시간

        ivDetailFirst = (ImageView)findViewById(R.id.ivDetailFirst);
        ivDetailSecond =(ImageView) findViewById(R.id.ivDetailSecond);
        ivDetailThird = (ImageView)findViewById(R.id.ivDetailThird);

        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailContent = findViewById(R.id.tvDetailContent);
        tvDetailContent.setText(Content);
        tvDetailTitle.setText(title);

        detailImage = (ImageView)findViewById(R.id.detailImage);
        AssetManager assetManager = getAssets();
        if(URL.equals("x"))
        {
            detailImage.setImageResource(R.drawable.icon);
        }
        else{
            detailImage.setImageBitmap(getBitmapFromAsset(URL+".jpg",assetManager));
        }


        sq = new SqliteTour(this);
        /*
        bundle.putString("ID",bookmarkItems.getID());
        bundle.putString("PlaceID",bookmarkItems.getPlaceID());
        bundle.putInt("Category",bookmarkItems.getCategory());
        bundle.putString("BookMark",bookmarkItems.getBookMark());
        bundle.putDouble("Latitude",bookmarkItems.getLat());
        bundle.putDouble("Longitude",bookmarkItems.getLon());
        */
        final ImageButton imgBu = (ImageButton)findViewById(R.id.imgbutDetail);

        if ( isBookMark.equals("Y"))
        {
            imgBu.setImageResource(R.drawable.bookmark_activation);
        }
        imgBu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( isBookMark.equals("Y"))
                {
                    imgBu.setImageResource(R.drawable.bookmark_disable);
                    isBookMark= "N";

                    sq.updateBookmark(isBookMark,"N");
                }
                else
                {
                    imgBu.setImageResource(R.drawable.bookmark_activation);
                    isBookMark="Y";
                    sq.updateBookmark(isBookMark,"Y");
                }
            }
        });
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
    private class PagerDetailAdapter extends FragmentStatePagerAdapter {
        GooglePlaceRequest googlePlaceRequest;

        public PagerDetailAdapter(FragmentManager fragmentManager,  String place_ID) {
            super(fragmentManager);
            Log.e("pager", place_ID + "aaa");
            googlePlaceRequest = new GooglePlaceRequest();
            googlePlaceRequest.execute();
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return new DetailFirstFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

        }
        private class GooglePlaceRequest extends AsyncTask<Void, Void, String> {

            final String API = "AIzaSyBNcrLP0WNGsICB3aUy7Up9g5yC3f-XX38"; //웹 테스트용 키 : AIzaSyBlzb9XBS0o-AshssTlleIvaBnKXj0pr8s
            private String REQUEST_GOOGLEPLACE = "https://maps.googleapis.com/maps/api/place/details/json";
            private URL url;
            private ContentValues values;

            GooglePlaceRequest() {
                Log.e("플래이스",place_ID + "sss");
                values = new ContentValues();
                values.put("key", API);
                values.put("placeid", place_ID);

               //fields=address_component,opening_hours,geometry
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.e("플래이스", s + "aaaa");
                Log.e("플래이스","Json");

                //JSON 처리
                JSONObject json = null;
                try {
                    json = new JSONObject(s);
                    //가져오는 키 이름, 옆의 0d은 처리한 데이터
                    //result - rating 0 , formatted_address 0, formatted_phone_number 0
                    //result - reviews 0 - [0~4] author_name, rating, text, time
                    //result - opening_hours - weekday_text
                    //result - photo 0 - [0~5] width, height, photo_reference
                    //JSONParsing -> JSONObject 저장 -> JSONArray에 항목별로 저장
                    //JSONObject {} key,value

                    JSONObject result = json.getJSONObject("result"); //결과 가져오기

                    String address = result.get("formatted_address").toString(); //주소 가져오기
                    tvDetailAddress.setText(address);

                    String phone_number = result.get("formatted_phone_number").toString(); //전화번호
                    tvDetailNumber.setText(phone_number);

                   Float rating = Float.parseFloat(result.get("rating").toString()); //별점
                   ratingStar.setRating(rating);


                    // JSONArray review_array = result.getJSONArray("reviews"); //리뷰 가져오기

                    JSONObject opening_Object = result.getJSONObject("opening_hours"); //여는 시간 가져오기
                    JSONArray weekday_Array = opening_Object.getJSONArray("weekday_text"); //여는 요일과 시간 0~6 0 = 일요일 ~ 6 = 토요일
                    int weekday_cnt = 7;
                    String weekday_str = "";
                    for (int i = 0; i < weekday_cnt; i++) {
                        weekday_str = weekday_str + weekday_Array.get(i).toString();
                        if (i < weekday_cnt - 1) {
                            weekday_str += "\n";
                        }
                    }
                    tvDetailDate.setText(weekday_str);
                    Bitmap bitmap;
                    JSONArray photo_array = result.getJSONArray("photos"); //사진 가져오기
                    for (int i = 0; i<1;i++){
                        getPhotoThread getphoto = new getPhotoThread(API, photo_array, i);
                        getphoto.start();
                        try
                        {
                            getphoto.join();
                        }
                        catch (Exception e){
                            e.printStackTrace();
                        }
                        bitmap = getphoto.getResult();

                        /*
                        if (i==0){

                            ivDetailFirst.setImageBitmap(bitmap);
                        }else if (i==1){
                            ivDetailSecond.setImageBitmap(bitmap);
                        }else{
                            ivDetailThird.setImageBitmap(bitmap);
                        }*/

                    }
                } catch (JSONException e) {
                    Log.e("플래이스", "Json error");
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(Void... strings) {
                String result;
                Log.e("플래이스","background");
                RequestHttpURLConnection request = new RequestHttpURLConnection();
                result = request.request(REQUEST_GOOGLEPLACE, values);
                return result;
            }

        }
    }
}
