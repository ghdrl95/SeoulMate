package com.example.jungeb.seoulapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.jungeb.seoulapp.adapter.ListviewKpopAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewTravelAdapter;
import com.example.jungeb.seoulapp.sqliteC.SqliteTour;

public class TravelActivity extends AppCompatActivity implements View.OnClickListener{

    ImageButton imbTravelBack;
    ListView lvTravel;
    SqliteTour m_sqlite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travel);

        imbTravelBack = (ImageButton)findViewById(R.id.imbTravelBack);


        //뒤로가기 버튼 눌렀을 때
        imbTravelBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        m_sqlite = new SqliteTour(this);
        //커스텀 리스트뷰 확인용
        lvTravel = (ListView)findViewById(R.id.lvTravel);

        ListviewTravelAdapter listviewTravelAdapter = new ListviewTravelAdapter();
        lvTravel.setAdapter(listviewTravelAdapter);
        m_sqlite.select_visited_list(listviewTravelAdapter);
        /*
        for (int i=1; i<11; i++) {
            listviewTravelAdapter.addItem("18.09.20. 목요일 PM 03:00","남산서울타워" + i, "서울특별시 용산구 남산공원길 105", "한류");
        }*/

    }

    @Override
    public void onClick(View v) {

    }
}
