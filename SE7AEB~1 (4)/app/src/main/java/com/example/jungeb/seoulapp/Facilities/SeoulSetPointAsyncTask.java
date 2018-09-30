package com.example.jungeb.seoulapp.Facilities;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.example.jungeb.seoulapp.MainActivity;
import com.skt.Tmap.TMapPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SeoulSetPointAsyncTask extends AsyncTask<String,TMapPoint,String> {
    MainActivity mainActivity;
    ArrayList<JSONArray> location;
    Geocoder geocoder;

    public SeoulSetPointAsyncTask(ArrayList<JSONArray> location, MainActivity mainActivity){
        this.mainActivity=mainActivity;
        this.location=location;

    }
    @Override
    protected String doInBackground(String... strings) {
        String[] str=strings;
        String setTagS="";
        if(str[0].equals("parmacyBizInfo")){
            setTagS="ADDR_OLD";

        }else if(str[0].equals("StationAdresTelno")){
            setTagS="ADRES";
        }

        for (int cnt = 0; cnt < location.size(); cnt++) {
            JSONArray locationTab1 = location.get(cnt);
            Log.i("잘왔닝", "" + locationTab1);

            Log.i("분리각", "" + locationTab1.length());
            try {
                for (int setCnt = 0; setCnt < locationTab1.length(); setCnt++) {

                    JSONObject temp = (JSONObject) locationTab1.get(setCnt);
                    SeoulFacilitiesLocation seoulFacilitiesLocationItem = new SeoulFacilitiesLocation();
                    Log.i("찍찍찍", "" + temp.get(setTagS).toString() + " / " + setCnt);


                    //addrToLatLon(temp.get("ADDR_OLD").toString()+"");
                    geocoder=new Geocoder(mainActivity);
                    List<Address> list = null;
                    try {
                        list = geocoder.getFromLocationName(temp.get(setTagS).toString(), 1);
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

                            TMapPoint tMapPoint=new TMapPoint(list.get(0).getLatitude(),list.get(0).getLongitude());
                            publishProgress(tMapPoint);
                        }
                    }

//                    seoulFacilitiesLocationItem.setLon(Double.parseDouble(list.get(0).getLongitude()+""));
//                    seoulFacilitiesLocationItem.setLat(Double.parseDouble(list.get(0).getLatitude()+""));


//                    try {
//                        seoulFacilitiesLocationItem.setLon(Double.parseDouble(list.get(0).getLongitude() + ""));
//                        seoulFacilitiesLocationItem.setLat(Double.parseDouble(list.get(0).getLatitude() + ""));
//                        seoulFacilitiesLocations.add(seoulFacilitiesLocationItem);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//
//                    }


//                    seoulFacilitiesLocationItem.setLon(Double.parseDouble(temp.get("" +lonType)+""));
//                    seoulFacilitiesLocationItem.setLat(Double.parseDouble(temp.get(""+latType)+""));
//
//                    TMapMarkerItem tempMarker = new TMapMarkerItem();
//                    //TMapPoint tMapPoint=new TMapPoint();
//                    tempMarker.setTMapPoint(new TMapPoint(seoulFacilitiesLocationItem.getLat(),seoulFacilitiesLocationItem.getLat()));
//                    tempMarker.setIcon(bitmapCCTV);
//                    tempMarker.setPosition(0.5f, 1.0f);
//                    tMapView.addMarkerItem(setCnt+"",tempMarker);


                    //markPoint(dataSet);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

            return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

    @Override
    protected void onProgressUpdate(TMapPoint...tMapPoints) {
        mainActivity.setOnePoint(tMapPoints[0]);
    }


}
