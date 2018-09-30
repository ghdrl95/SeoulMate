package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jungeb.seoulapp.DetailPageActivity;
import com.example.jungeb.seoulapp.Items.ListviewBookmarkItems;
import com.example.jungeb.seoulapp.Items.ListviewCultureItems;
import com.example.jungeb.seoulapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListviewCultureAdapter extends BaseAdapter{

    private ArrayList<ListviewBookmarkItems> cultureItems = new ArrayList<>();

    @Override
    public int getCount() {
        return cultureItems.size();
    }

    @Override
    public ListviewBookmarkItems getItem(int position) {
        return cultureItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listview_bookmark, parent, false);
        }

        TextView tvBookmarkTitle = (TextView) convertView.findViewById(R.id.tvBookmarkTitle) ;
        TextView tvBookmarkAddress = (TextView) convertView.findViewById(R.id.tvBookmarkAddress) ;
        TextView tvBookmarkKinds = (TextView) convertView.findViewById(R.id.tvBookmarkKinds) ;
        LinearLayout ivBookmark = (LinearLayout) convertView.findViewById(R.id.ivBookmark);
        //아직 그림 없음
        //ImageView
        final ListviewBookmarkItems bookmarkItems = getItem(position);

        tvBookmarkTitle.setText(bookmarkItems.getBookmarkTitle());
        tvBookmarkAddress.setText(bookmarkItems.getBookmarkAddress());
        tvBookmarkKinds.setText(bookmarkItems.getBookmarkKinds());
        ImageView img = (ImageView)convertView.findViewById(R.id.ivBookmarkImage);
        AssetManager assetManager = context.getAssets();

        img.setImageBitmap(getBitmapFromAsset(bookmarkItems.getURL()+".jpg",assetManager));

        final Context con=convertView.getContext();



        ivBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ListViewCultureAdapter","Detail Activity 생성");
                Intent intent = new Intent(con, DetailPageActivity.class);
                /*Bundle bundle= new Bundle();
                bundle.putString("ID",bookmarkItems.getID());
                bundle.putString("PlaceID",bookmarkItems.getPlaceID());
                bundle.putInt("Category",bookmarkItems.getCategory());
                bundle.putString("BookMark",bookmarkItems.getBookMark());
                bundle.putDouble("Latitude",bookmarkItems.getLat());
                bundle.putDouble("Longitude",bookmarkItems.getLon());

                con.startActivity(intent, bundle);*/
                intent.putExtra("ID", bookmarkItems.getID());
                intent.putExtra("PlaceID", bookmarkItems.getPlaceID());
                intent.putExtra("Category", bookmarkItems.getCategory());
                intent.putExtra("BookMark", bookmarkItems.getBookMark());
                intent.putExtra("Latitude", bookmarkItems.getLat());
                intent.putExtra("Logitude", bookmarkItems.getLon());
                intent.putExtra("Content", bookmarkItems.getContent());
                intent.putExtra("URL", bookmarkItems.getURL());
                con.startActivity(intent);
            }
        });



        return convertView;
    }
    private Bitmap getBitmapFromAsset(String strName, AssetManager assetManager)
    {
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
    public void addItem(String title, String address, String ID, String PlaceID, int category, String BookMark, double lat, double lon,String URL) {

        ListviewBookmarkItems customitem = new ListviewBookmarkItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setBookmarkTitle(title);
        customitem.setBookmarkAddress(address);
        customitem.setID(ID);
        customitem.setPlaceID(PlaceID);
        customitem.setCategory(category);
        customitem.setBookMark(BookMark);
        customitem.setLat(lat);
        customitem.setLon(lon);
        customitem.setURL(URL);
        /* customItems에 customitem을 추가한다. */
        cultureItems.add(customitem);

    }

    public ListviewCultureAdapter() {
        super();
    }
}
