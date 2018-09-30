package com.example.jungeb.seoulapp.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jungeb.seoulapp.R;
import com.skt.Tmap.TMapView;

public class FacilitiesFragment extends Fragment {
    LinearLayout linMapView;
    TMapView tMapView;
    double myLatitude=0.0;
    double myLongitude=0.0;
    LocationManager locationManager;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_facilities, container, false);

        linMapView=(LinearLayout)view.findViewById(R.id.linMapView);
        ibtnSubwayIcon=view.findViewById(R.id.ibtnSubwayIcon);
        ibtnBusIcon=view.findViewById(R.id.ibtnBusIcon);
        ibtnTaxiIcon=view.findViewById(R.id.ibtnTaxiIcon);
        ibtnMedicineIcon=view.findViewById(R.id.ibtnMedicineIcon);
        ibtnWCICON=view.findViewById(R.id.ibtnWCIcon);
        tvSubwayText=view.findViewById(R.id.tvSubwayText);
        tvBusText=view.findViewById(R.id.tvBusText);
        tvTaxiText=view.findViewById(R.id.tvTaxiText);
        tvMedicineText=view.findViewById(R.id.tvMedicineText);
        tvWCText=view.findViewById(R.id.tvWCIcon);

        tMapView=new TMapView(getContext());
        tMapView.setSKTMapApiKey("8c01e13b-978f-41bc-8629-b223245f9203");
        linMapView.addView(tMapView);

        //현재위치를 받아온다 GPS와 NETWORK둘다 사용
//        locationManager = (LocationManager) view.getSystemService(Context.LOCATION_SERVICE);
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return view;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, mLocationListener);
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 100, 1, mLocationListener);
        Toast.makeText(getContext(), "현재위치 트래킹 시작", Toast.LENGTH_SHORT).show();



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
            }
        });



        return view;

    }

    //현재위치로 가는 메소드
    private final LocationListener mLocationListener=new LocationListener() {
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
}
