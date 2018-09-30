package com.example.jungeb.seoulapp.Facilities;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

public class SeoulFacilities {
    URL url = null;//url 주소로 프로토콜, 도메인, 포트, 경로 등을 알 수 있다.
    HttpURLConnection httpURLConnection = null;
    BufferedReader bufferedReader = null;//문자입력 스트림으로부터 문자를 읽어 들이거나 문자
    //출력 스트림으로 문자를 보낼 때 버퍼링을 함으로써 문자, 문자배열,
    //문자열, 라인 등을 보다 효율적으로 처리할 수 있도록 해준다.
    HashMap<String, String> location;
    String params = "";


    String data = "";
    String reData = "";
    static final String REQUEST_SEOULTOILET_URL = "http://openapi.seoul.go.kr:8088/4d5042734f64616e3235715a787163/json/";

    public SeoulFacilities(HashMap<String, String> location) {
        this.location = location;
        String service = location.get("SERVICE").toString();
        int start_index = Integer.parseInt(location.get("START_INDEX").toString());
        int end_index = Integer.parseInt(location.get("END_INDEX").toString());

        params = service + "/" + start_index + "/" + end_index;


        try {
            url = new URL(REQUEST_SEOULTOILET_URL + params);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SeoulFacilities() {

    }


    public String connect() {

        String line = "";

        try {
            httpURLConnection.connect();

            InputStream is = httpURLConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            while ((line = bufferedReader.readLine()) != null) {
                data += line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;

    }

    public String reConnect(HashMap<String, String> location) {
        String service = location.get("SERVICE").toString();
        int start_index = Integer.parseInt(location.get("START_INDEX").toString());
        int end_index = Integer.parseInt(location.get("END_INDEX").toString());

        String reParams = service + "/" + start_index + "/" + end_index;

        try {
            Log.i("리터넥트 날림","url호출하기 전");
            url = new URL(REQUEST_SEOULTOILET_URL + reParams);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            Log.i("리터넥트 날림","url호출한 후");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String line = "";

        try {
            httpURLConnection.connect();
            Log.i("리터넥트 날림","연결한다잉");
            InputStream is = httpURLConnection.getInputStream();
            Log.i("리터넥트 날림","연결한다잉/여기야?");
            bufferedReader = new BufferedReader(new InputStreamReader(is));
            Log.i("리터넥트 날림","연결한다잉/여기가문제야?");
            while ((line = bufferedReader.readLine()) != null) {
                reData = line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    Log.i("리터넥트 날림","연결한다잉/여기니?");
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return reData;

////
////        String line="";
////
////        try {
////            httpURLConnection.connect();
////
////            InputStream is =httpURLConnection.getInputStream();
////            bufferedReader=new BufferedReader(new InputStreamReader(is));
////            while ((line=bufferedReader.readLine())!=null){
////                data+=line;
////            }
////        } catch (IOException e) {
////            e.printStackTrace();
////        }finally {
////            if(bufferedReader!=null){
////                try{
////                    bufferedReader.close();
////                }catch (IOException e){
////                    e.printStackTrace();
////                }
////            }
////        }
////        return data;
    }


}
