package com.example.jungeb.seoulapp.PushAlarm;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;

import com.example.jungeb.seoulapp.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class getPhotoThread extends Thread {
    String API;
    JSONArray photoArray;
    int i;
    Bitmap bitmap;
    public getPhotoThread(String API, JSONArray photoArray, int i)
    {
        this.API = API;
        this.photoArray = photoArray;
        this.i = i;
    }
    public void run(){
        JSONObject photo_Object;
        String photo_reference;
        try {
            photo_Object = photoArray.getJSONObject(i); //JSON array에서 JSON Object가져옴
            photo_reference = photo_Object.get("photo_reference").toString(); //거기서 photo_reference 가져옴
            bitmap = getPhoto(photo_reference);
            Log.e("플래이스",Integer.toString(bitmap.getByteCount())+"aaa");

        }catch (Exception e)
        {

            Log.e("플래이스",e.toString());
        }
    }
    public Bitmap getResult(){
        return bitmap;
    }
    private Bitmap getPhoto(String photo_reference) { //사진 가져오기 함수
        HttpURLConnection urlConn = null;
        String _url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=";
        _url = _url + photo_reference + "&key=" + API;
        try {
            URL url = new URL(_url);
            urlConn = (HttpURLConnection) url.openConnection();

            // [2-1]. urlConn 설정.
            urlConn.setRequestMethod("GET"); // URL 요청에 대한 메소드 설정 : POST.
            urlConn.setRequestProperty("Accept-Charset", "UTF-8"); // Accept-Charset 설정.

            if (urlConn.getResponseCode() != HttpURLConnection.HTTP_OK)
                return null;

            // [2-4]. 읽어온 결과물 리턴.
            InputStream inputStream = urlConn.getInputStream();
            Bitmap bitmap;
            final BitmapFactory.Options options = new BitmapFactory.Options();
            bitmap = BitmapFactory.decodeStream(inputStream);
            Log.e("플래이스","bitmap");
            return bitmap;
        } catch (MalformedURLException e) { // for URL.
            e.printStackTrace();
        } catch (IOException e) { // for openConnection().
            e.printStackTrace();
        } finally {
            if (urlConn != null)
                urlConn.disconnect();
        }
        return null;
    }
}
