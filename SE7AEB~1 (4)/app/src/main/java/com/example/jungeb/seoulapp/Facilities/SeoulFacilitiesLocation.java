package com.example.jungeb.seoulapp.Facilities;

public class SeoulFacilitiesLocation {
    double lat=0.0;
    double lon=0.0;

    SeoulFacilitiesLocation(double lat, double lon){
        this.lat=lat;
        this.lon=lon;

    }
    public SeoulFacilitiesLocation(){

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
