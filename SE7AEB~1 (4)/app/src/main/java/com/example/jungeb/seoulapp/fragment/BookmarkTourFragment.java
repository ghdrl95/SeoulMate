package com.example.jungeb.seoulapp.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.LocaleList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.jungeb.seoulapp.DetailPageActivity;
import com.example.jungeb.seoulapp.R;
import com.example.jungeb.seoulapp.adapter.ListviewBookmarkAdapter;
import com.example.jungeb.seoulapp.sqliteC.SqliteTour;


public class BookmarkTourFragment extends Fragment{

    ListView lvBookmark;
    SqliteTour m_sqlite;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bookmark_tour, container, false);

        m_sqlite = new SqliteTour(getActivity());
        //커스텀 리스트뷰 확인용
        lvBookmark = (ListView)view.findViewById(R.id.lvBookmark);

        ListviewBookmarkAdapter listviewBookmarkAdapter = new ListviewBookmarkAdapter();
        lvBookmark.setAdapter(listviewBookmarkAdapter);
        //database call
        m_sqlite.select_bookmark_list(listviewBookmarkAdapter);


        return view;
    }

}
