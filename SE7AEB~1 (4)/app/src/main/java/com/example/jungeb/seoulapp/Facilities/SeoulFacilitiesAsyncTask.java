package com.example.jungeb.seoulapp.Facilities;


import android.os.AsyncTask;
import android.util.Log;

import com.example.jungeb.seoulapp.MainActivity;
import com.example.jungeb.seoulapp.fragment.FacilitiesFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class SeoulFacilitiesAsyncTask extends AsyncTask<String, Integer, String> {

    private SeoulFacilities seoulFacilities = null;
    private String myFac = null;
    private String myCheck = "";
    int myListCount = 0;
    int myStartIndex = 1;
    int myEndIndex = 1000;
    JSONObject aTemp = null;
    JSONObject aTemp2 = null;
    private String myCheck2 = null;
    private int jsonCount = 0;
    private MainActivity mainActivity = null;
    String dataSet="";

    JSONArray getAllItem;

    ArrayList<JSONArray> getAllItemList;


    public SeoulFacilitiesAsyncTask(HashMap<String, String> location, MainActivity mainActivity) {
        seoulFacilities = new SeoulFacilities(location);
        this.mainActivity = mainActivity;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... strings) {

        String[] str = strings;
        getAllItemList=new ArrayList<JSONArray>();
        dataSet=str[0];

        Log.i("뭐가넘어오니", "" + str[0]);
        Log.i("뭐가넘어오니", "" + str[1]);

        try {
            aTemp = new JSONObject(seoulFacilities.connect());

            JSONObject aTempCountTotal = new JSONObject(String.valueOf(aTemp.getJSONObject("" + str[0])));
            myListCount = Integer.parseInt("" + (aTempCountTotal.get("list_total_count")));
            Log.i("다시확인해보겠습니다", "" + aTempCountTotal);

            getAllItem = (JSONArray) aTempCountTotal.get("row");
            Log.i("끝??", "" + getAllItem);
            Log.i("진짜 길이는?", "" + getAllItem.length());
            getAllItemList.add(getAllItem);

            Log.i("넘겨줄쪽카운트",""+getAllItemList.size());
            myListCount -= 1000;
            myStartIndex += 1000;
            myEndIndex += 1000;
            Log.i("어디까지왔다","한번 했습니다.");


            do {
                Log.i("어디까지왔다","두번 들어갑니다.");
                HashMap<String, String> reGetItem = new HashMap<String, String>();
                reGetItem.put("SERVICE", "" + str[0]);
                reGetItem.put("START_INDEX", "" + myStartIndex);
                reGetItem.put("END_INDEX", "" + myEndIndex);
                Log.i("어디까지왔다","집어넣습니다");
                //Log.i("언제까지가끝인가", "" + seoulFacilities.reConnect(reGetItem));

                aTemp=new JSONObject(seoulFacilities.reConnect(reGetItem));
                Log.i("리터넥트 날림","여기가 제일 문제인거같은데");
                JSONObject aTempCountTotalTemp = new JSONObject(String.valueOf(aTemp.getJSONObject("" + str[0])));
                Log.i("리터넥트 날림","연결한다잉/여기 오래걸려?");

                Log.i("다시확인해보겠습니다", "" +aTempCountTotalTemp);

                //getAllItem = (JSONArray) aTempCountTotalTemp.get("row");
                getAllItem= (JSONArray) aTempCountTotalTemp.get("row");
                getAllItemList.add(getAllItem);
                Log.i("끝??", "" + getAllItem);
                Log.i("진짜 길이는?", "" + getAllItem.length());
                Log.i("넘겨줄쪽카운트",""+getAllItemList.size());
                myListCount -= 1000;
                myStartIndex += 1000;
                myEndIndex += 1000;

            } while (!(myListCount < 0));
        } catch (JSONException e) {
            e.printStackTrace();
        }


//        myFac = seoulFacilities.connect();
//        Log.i("json 결과 : ", myFac + "");
//
//        try {
//            JSONObject aTemp=new JSONObject(myFac);
//            Log.i("확인해보겠습니다",""+aTemp);
//            Log.i("다시 확읺내보겠습니다",""+aTemp.getJSONObject("MgisToilet"));
//            JSONObject aTempMgis=new JSONObject(String.valueOf(aTemp.getJSONObject("MgisToilet")));
//            Log.i("총 리스트의 갯수",""+aTempMgis.get("list_total_count"));
//
//            if(Integer.parseInt(aTempMgis.getString("list_total_count"))>1000){
//                Log.i("리스트 갯수가","존나많아요");
//                HashMap<String,String> reFac=new HashMap<String,String>();
//                reFac.put("SERVICE","MgisToilet");
//                reFac.put("START_INDEX","1001");
//                reFac.put("END_INDEX","2000");
//
//                myCheck=seoulFacilities.reConnect(reFac);
//                Log.i("오긴오니?",""+myCheck);
//
//
//                HashMap<String,String> reFac2=new HashMap<String,String>();
//                reFac2.put("SERVICE","MgisToilet");
//                reFac2.put("START_INDEX","2001");
//                reFac2.put("END_INDEX","3000");
//
//                myCheck2=seoulFacilities.reConnect(reFac2);
//                Log.i("오긴오니?",""+myCheck2);
//            }
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        return myFac;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        mainActivity.setLocation(getAllItemList,dataSet);
//        try {
//            JSONArray features = null;
//            JSONObject total = new JSONObject(s);
//            Log.i("왔니?" +
//                    "", "" + total);
//            System.out.println(s);
//            //features=total.getJSONArray("row");
//
//            //mapViewActivity.setLocation(features);
//            //mapViewActivity.setLocation2(total);
//            facilitiesFragment.setLocation(total);
//            //Log.i("구구구구구",""+features);
//
//        } catch (JSONException e) {
//            Log.e("구구구구구", "보행자 경로 요청 에러");
//            e.printStackTrace();
//        }

    }
}

