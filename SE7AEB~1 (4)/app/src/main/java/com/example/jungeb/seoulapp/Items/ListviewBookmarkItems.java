package com.example.jungeb.seoulapp.Items;

import com.example.jungeb.seoulapp.MainActivity;

public class ListviewBookmarkItems {
    final String CATEGORY[] ={"문화재","볼거리","한류"};
    final String CATEGORY_ENG[] ={"Culture","Attraction","Korean wave"};
    private String ID;
    private String PlaceID;
    private int category;
    private String BookMark;
    private double lat,lon;
    private String URL;
    private String Content;
    private String BookmarkTitle;
    private String BookmarkAddress;

    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public String getBookmarkTitle() {
        return BookmarkTitle;
    }

    public void setBookmarkTitle(String bookmarkTitle) {
        BookmarkTitle = bookmarkTitle;
    }

    public String getBookmarkAddress() {
        return BookmarkAddress;
    }

    public void setBookmarkAddress(String bookmarkAddress) {
        BookmarkAddress = bookmarkAddress;
    }

    public String getBookmarkKinds() {
        return (MainActivity.language.equals("ko")?CATEGORY[category-1] : CATEGORY_ENG[category-1]);
    }



    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getPlaceID() {
        return PlaceID;
    }

    public void setPlaceID(String placeID) {
        PlaceID = placeID;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getBookMark() {
        return BookMark;
    }

    public void setBookMark(String bookMark) {
        BookMark = bookMark;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }
}
