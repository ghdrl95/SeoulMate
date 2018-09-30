package com.example.jungeb.seoulapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.adapter.ListviewBookmarkAdapter;
import com.example.jungeb.seoulapp.adapter.ListviewKpopAdapter;
import com.example.jungeb.seoulapp.sqliteC.SqliteTour;


public class KpopTourFragment extends Fragment {

    ListView lvKpop;
    SqliteTour m_sqlite;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        m_sqlite = new SqliteTour(getActivity());

        View view = inflater.inflate(R.layout.fragment_kpop_tour, container, false);


        //커스텀 리스트뷰 확인용
        lvKpop = (ListView)view.findViewById(R.id.lvKpop);

        ListviewBookmarkAdapter listviewBookmarkAdapter = new ListviewBookmarkAdapter();
        lvKpop.setAdapter(listviewBookmarkAdapter);
        m_sqlite.select_category_list(listviewBookmarkAdapter,2);
        /*
        for (int i=1; i<11; i++) {
            listviewKpopAdapter.addItem("남산서울타워" + i, "서울특별시 용산구 남산공원길 105", "한류");
        }*/

        return view;
    }
}
