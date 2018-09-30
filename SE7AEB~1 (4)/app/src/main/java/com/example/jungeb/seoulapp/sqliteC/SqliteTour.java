package com.example.jungeb.seoulapp.sqliteC;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.jungeb.seoulapp.MainActivity;
import com.example.jungeb.seoulapp.adapter.ListviewBookmarkAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewCultureAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewFestivalAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewKpopAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewTravelAdapter;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SqliteTour {
    SQLiteDatabase DBinstance;
    String m_dbName;
    Activity m_activity;
    Context m_context;
    InputStream is;
    public SqliteTour(Activity _activity)
    {
        m_activity = _activity;
        m_dbName = "Tour";
        try {
            DBinstance = _activity.openOrCreateDatabase(m_dbName, Context.MODE_PRIVATE, null);
            createTableAndInsertData(); //DB 열기
            Log.d("DB : ","DB 열기 완료");
        }catch(Exception e)
        {
            Log.d("DB : ","DB 열기 실패");
        }
    }
    public SqliteTour(Context _activity)
    {
        m_context = _activity;
        m_dbName = "Tour";
        try {
            DBinstance = m_context.openOrCreateDatabase(m_dbName, Context.MODE_PRIVATE, null);
            createTableAndInsertData(); //DB 열기
            Log.d("DB : ","DB 열기 완료");
        }catch(Exception e)
        {
            Log.d("DB : ","DB 열기 실패");
        }
    }
    public SqliteTour(Activity _activity, String _dbName)
    {
        m_activity = _activity;
        try {
            is = m_activity.getAssets().open("TourData.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }
        m_dbName = _dbName;
        try {
            DBinstance = _activity.openOrCreateDatabase(m_dbName, Context.MODE_PRIVATE, null);
            createTableAndInsertData(); //DB 열기
            Log.d("DB : ","DB 열기 완료");
        }catch(Exception e)
        {
            Log.d("DB : ","DB 열기 실패");
        }
    }


    //카테고리별 결과 출력
    public void select_bookmark_list(ListviewBookmarkAdapter _adapter)
    {
        String sql = "select a.TourID, a.TourCategori, a.GooglePlaceID, a.URL, b.NAME, a.Latitude, a.Longitude, b.DetailInfo, (abs(a.Latitude - ?) + abs(a.Longitude - ?)) AS distance from TourInfo a, " + (MainActivity.language.equals("ko") ?"TourDetailInfo_KOR":"TourDetailInfo_ENG") +" b where a.TourID = b.TourID and a.BookMark = 'Y' order by distance" ;
        Cursor c=DBinstance.rawQuery(sql,new String[]{String.valueOf(MainActivity.Latitude), String.valueOf(MainActivity.Longitude)});
        if(c.moveToFirst())
        {
            while(c.moveToNext())
            {
                _adapter.addItem(c.getString(4),"",c.getString(0),c.getString(2), c.getInt(1),"Y", c.getDouble(5), c.getDouble(6), c.getString(3),c.getString(7));
            }
        }
    }
    //카테고리별 결과 출력
    public void select_category_list(ListviewBookmarkAdapter _adapter,int category)
    {
        String sql = "select a.TourID, a.TourCategori, a.GooglePlaceID, a.URL, b.NAME, a.Latitude, a.Longitude, a.BookMark, b.DetailInfo, (abs(a.Latitude - ?) + abs(a.Longitude - ?)) AS distance from TourInfo a, " + (MainActivity.language.equals("ko") ?"TourDetailInfo_KOR":"TourDetailInfo_ENG") +" b where a.TourID = b.TourID and a.TourCategori = ? order by distance" ;
        Cursor c = DBinstance.rawQuery(sql,new String[]{String.valueOf(MainActivity.Latitude), String.valueOf(MainActivity.Longitude),String.valueOf(category)});
        if(c.moveToFirst())
        {
            while(c.moveToNext())
            {
                _adapter.addItem(c.getString(4),"",c.getString(0),c.getString(2), c.getInt(1),c.getString(7), c.getDouble(5), c.getDouble(6), c.getString(3),c.getString(8));
            }
        }
    }
    //카테고리별 결과 출력
    public void select_visited_list(ListviewTravelAdapter _adapter)
    {
        String sql = "select a.TourID, a.TourCategori, a.GooglePlaceID, a.URL, b.NAME, a.Latitude, a.Longitude, a.BookMark, b.DetailInfo, c.VisitedDate  from TourInfo a, " + (MainActivity.language.equals("ko") ?"TourDetailInfo_KOR":"TourDetailInfo_ENG") +" b, VisitedTour c where a.TourID = b.TourID  and a.TourID = c.TourID order by c.VisitedDate DESC" ;
        Cursor c = DBinstance.rawQuery(sql,null);
        if(c.moveToFirst())
        {
            while(c.moveToNext())
            {
                _adapter.addItem(c.getString(4),"",c.getString(0),c.getString(2), c.getInt(1),c.getString(7), c.getDouble(5), c.getDouble(6), c.getString(3),c.getString(8),c.getString(9));
            }
        }
    }

    public int updateBookmark(String id, String BookMarking)
    {
        ContentValues values = new ContentValues();
        values.put("BookMark",BookMarking);
        int cnt = DBinstance.update("TourInfo",values,"TourID = ?", new String[]{id});

        return cnt;
    }
    //
    public String[] selectTourVisitedLastThree()
    {
        String[] pictures = new String[3];
        Cursor c = DBinstance.rawQuery("select a.URL from TourInfo a, VisitedTour b where a.TourID = b.TourID order by b.VisitedDate DESC limit 3",null);
        if(c.moveToFirst())
        {
            for(int i=0;i<3;i++)
            {
                pictures[i] = c.getString(0);
                if(!c.moveToNext()) break;

            }
        }
        return pictures;
    }
    //여행지 이름 DB에서 불러오는 부분
    private String SelectTourName(String language, int tourID){
        String tourName = "";
        String sql, name;
        if (language == "ko"){ //시스템 언어 한글이면 DetailInfo_kor에서 이름 값 가져옴
            sql = "Select NAME From TourDetailInfo_KOR Where TourID = " + Integer.toString(tourID);
        }else{ //그외 언어면 TourDetailInfo_ENG
            sql = "Select NAME From TourDetailInfo_ENG Where TourID" + Integer.toString(tourID);
        }
        Cursor resultset = DBinstance.rawQuery(sql, null);
        int count = resultset.getCount();
        if (count == 0){
            Log.e("Select", "tourID에 Name이 DetailInfo에 없음");
            return null;
        }else {
            resultset.moveToNext();
            name = resultset.getString(0);
            Log.d("Select", "Name 정상적으로 가져옴");
            return name;
        }
    }
    //방분 여부 체크 부분, MainActivity에 Broadcast에서 위도,경도 가져옴
    public List CheckVisit(double Latitude, double Longitude, String language){
        String sql = "Select Latitude, Longitude, TourID From TourInfo Where VisitCode = 0";
        Cursor resultset = DBinstance.rawQuery(sql, null);
        Log.d("Select","TourInfo Selete");

        int tourID = 0, cnt = 0;
        int temp_Tour = 0;
        double tourlat, tourlong;
        String name = "";
        List resultlist = new ArrayList();
        if(resultset.moveToFirst())
        {
           while(resultset.moveToNext()){
                   // 첫번째에서 다음 레코드가 없을때까지 읽음
                tourlat = resultset.getDouble(0);
                tourlong = resultset.getDouble(1);
                tourID = resultset.getInt(2);
                if (Math.sqrt(Math.pow(tourlat-Latitude,2)+Math.pow(tourlong-Longitude,2)) < 0.001){ //약 100m
                    UpdateVisitCode(tourID); //방문시 DB에 업데이트
                    cnt++;
                    if (temp_Tour == 0) {temp_Tour = tourID;}
                }
            }
            if (cnt>0){ //방문지역 하나라도 있으면 출력
                resultlist.add(cnt);
                name = SelectTourName(language, tourID); //여행지 이름 DB에서 불러오기
                if (name != null){ //name이 존재할때만 값 반환
                    Log.d("Select", "result 반환");
                    resultlist.add(name);
                    return resultlist;
                }
            }
        }
        Log.d("Select", "result 미존재");
        return resultlist;
    }
    //VisitCode 업데이트 부분
    public void UpdateVisitCode(int TourID){
        try{
            String sql;
            long now = System.currentTimeMillis();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            //방문 코드 업데이트
            sql = "UPDATE TourInfo SET VisitCode = 1 where TourID = " + Integer.toString(TourID)+";";
            DBinstance.execSQL(sql);

            //방문한 장소 테이블에 추가
            sql = "INSERT INTO VisitedTour (TourID, VisitedDate) VALUES ("+ Integer.toString(TourID) + " ,DATETIME('"+ date.toString() +"'));";
            DBinstance.execSQL(sql);

        }catch(Exception e){
            Log.e("UpdateVisitCode : ","VisitCode Update 실패");
        }
    }

    //최초 생성 시 CSV에서 값 읽어와 데이터 입력
    private void FirstInsertCSV(){
        InsertCSVData();
        //InsertCSV("VisitedTour.csv","VisitedTour");
    }

    //CSV 읽어오는 부분
    private void InsertCSV(String csvFileName, String tableName){

        String sql;
        try{
            CSVReader reader = new CSVReader(new FileReader(csvFileName), ',');
            String []nextLine;
            int cnt=1;
            sql = "INSERT INTO (" + tableName + ") VALUES (";
            while ((nextLine = reader.readNext())!= null){

                for(int i=0;i<nextLine.length;i++){
                    sql = sql + nextLine[i] + ",";
                }
                sql = sql.substring(0,sql.length()-2) + ");";
                DBinstance.execSQL(sql);
            }
            Log.e("CSVFile", "Insert success");
        }catch(Exception e){
            Log.e("CSVFile", "Error");
        }

    }
    //CSV 읽어오는 부분

    private void InsertCSVData(){
        //데이터 개수 찾기
        String sql = "SELECT count(*) from TourInfo";
        Cursor c = DBinstance.rawQuery(sql,null);
        if (c.moveToFirst())
        {
            int datacnt = c.getInt(0);
            if (datacnt > 0)
            {
                return;
            }
        }
        try{
            CSVReader reader = new CSVReader(new InputStreamReader(is, Charset.forName("EUC-KR")));
            String []nextLine;
            int cnt=1;

            while ((nextLine = reader.readNext())!= null){

                ContentValues values = new ContentValues();
                values.put("TourID",String.valueOf(cnt));
                values.put("Latitude",nextLine[3]);
                values.put("Longitude",nextLine[4]);
                values.put("TourCategori",nextLine[0]);
                values.put("GooglePlaceID",nextLine[8]);
                values.put("URL", (nextLine[5].equals("") ? "x" :nextLine[5] ) );

                ContentValues values1 = new ContentValues();
                values1.put("TourID",String.valueOf(cnt));
                values1.put("NAME", nextLine[1]);
                values1.put("DetailInfo", (nextLine[6].equals("") ? "x" :nextLine[6] ) );
                ContentValues values2 = new ContentValues();
                values1.put("TourID",String.valueOf(cnt));
                values1.put("NAME",nextLine[2]);
                values1.put("DetailInfo", (nextLine[7].equals("") ? "x" :nextLine[7] ) );
                DBinstance.insert("TourInfo",null, values);
                DBinstance.insert("TourDetailInfo_KOR",null, values1);
                DBinstance.insert("TourDetailInfo_ENG",null, values2);

                cnt++;
            }
            Log.e("CSVFile", "Insert success");
        }catch(Exception e){
            Log.e("CSVFile", "Error");
        }

    }



    //테이블 생성 부분
    private void createTableAndInsertData()
    {
        try {
            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourInfo (" +
                    "TourID INTEGER Primary Key AUTOINCREMENT, " +
                    "Latitude double, Longitude double, " +
                    "TourCategori INTEGER, " +
                    "GooglePlaceID String, " +
                    "URL String, " +
                    "VisitCode String DEFAULT 'N', " +
                    "BookMark String DEFAULT 'N');");

            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourDetailInfo_KOR(" +
                    "TourID INTEGER," +
                    "NAME String," +
                    "DetailInfo String," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID));");

            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS TourDetailInfo_ENG(" +
                    "TourID INTEGER," +
                    "NAME String," +
                    "DetailInfo String," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID));");

            DBinstance.execSQL("CREATE TABLE IF NOT EXISTS VisitedTour(" +
                    "TourID INTEGER," +
                    "VisitedDate Date," +
                    "CONSTRAINT TourID_fk FOREIGN KEY(TourID)" +
                    "REFERENCES TourInfo(TourID)" +
                    ");");
            Log.d("DB : ","DB 생성 완료");
            FirstInsertCSV(); //DB CSV 파일 읽어서 입력
        }catch(Exception e)
        {
            Log.d("DB : ","DB 생성 실패");
        }
    }
}
