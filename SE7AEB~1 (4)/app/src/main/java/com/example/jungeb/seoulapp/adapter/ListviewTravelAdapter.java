package com.example.jungeb.seoulapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jungeb.seoulapp.DetailPageActivity;
import com.example.jungeb.seoulapp.Items.ListviewKpopItems;
import com.example.jungeb.seoulapp.Items.ListviewTravelItems;
import com.example.jungeb.seoulapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class ListviewTravelAdapter extends BaseAdapter {

    private ArrayList<ListviewTravelItems> TravelItems = new ArrayList<>();

    @Override
    public int getCount() {
        return TravelItems.size();
    }

    @Override
    public ListviewTravelItems getItem(int position) {
        return TravelItems.get(position);
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
            convertView = inflater.inflate(R.layout.listview_travel, parent, false);
        }

        TextView tvTravelDate = (TextView) convertView.findViewById(R.id.tvTravelDate);
        TextView tvTravelTitle = (TextView) convertView.findViewById(R.id.tvTravelTitle);
        TextView tvTravelAddress = (TextView) convertView.findViewById(R.id.tvTravelAddress);
        TextView tvTravelKinds = (TextView) convertView.findViewById(R.id.tvTravelKinds);
        ImageView img = (ImageView) convertView.findViewById(R.id.ivTravelImage);

        final ListviewTravelItems travelItems = getItem(position);

        tvTravelDate.setText(travelItems.getTravelDate());
        tvTravelTitle.setText(travelItems.getTravelTitle());
        tvTravelAddress.setText(travelItems.getTravelAddress());
        tvTravelKinds.setText(travelItems.getBookmarkKinds());

        AssetManager assetManager = context.getAssets();
        img.setImageBitmap(getBitmapFromAsset(travelItems.getURL()+".jpg",assetManager));
        final Context con=convertView.getContext();





        convertView.findViewById(R.id.travelItem).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("ListViewBookmarkAdapter","Detail Activity 생성");
                Intent intent = new Intent(con, DetailPageActivity.class);
                intent.putExtra("title",travelItems.getTravelTitle());
                intent.putExtra("ID", travelItems.getID());
                intent.putExtra("PlaceID", travelItems.getPlaceID());
                intent.putExtra("Category", travelItems.getCategory());
                intent.putExtra("BookMark", travelItems.getBookMark());
                intent.putExtra("Latitude", travelItems.getLat());
                intent.putExtra("Logitude", travelItems.getLon());
                intent.putExtra("Content", travelItems.getContent());
                intent.putExtra("URL", travelItems.getURL());
                con.startActivity(intent);
            }
        });
        return convertView;
    }
    private Bitmap getBitmapFromAsset(String strName, AssetManager assetManager)
    {
        if(strName.equals("x.jpg")) return null;
        InputStream istr = null;
        try {
            istr = assetManager.open(strName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(istr);
        return bitmap;
    }
    public void addItem(String title, String address, String ID, String PlaceID, int category, String BookMark, double lat, double lon,String URL, String content,String date) {

        ListviewTravelItems customitem = new ListviewTravelItems();

        /* CustomListItem에 아이템을 setting한다. */
        customitem.setTravelDate(date);
        customitem.setTravelTitle(title);
        customitem.setTravelAddress(address);
        customitem.setID(ID);
        customitem.setPlaceID(PlaceID);
        customitem.setCategory(category);
        customitem.setBookMark(BookMark);
        customitem.setLat(lat);
        customitem.setLon(lon);
        customitem.setURL(URL);
        customitem.setContent(content);

        /* customItems에 customitem을 추가한다. */
        TravelItems.add(customitem);

    }

    public ListviewTravelAdapter() {
        super();
    }
}
