package com.example.mapdemo;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class Point {
    private String pointName;
    private double latitude;
    private double longitude;
    private double altitude;
    private LatLng latLng;
    private Marker marker;

    public Point(){}

    public Point(String pointName, double latitude, double longitude, double altitude) {
        this.pointName = pointName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
    }

    public String getPointName() {
        return pointName;
    }

    public void setPointName(String pointName) {
        this.pointName = pointName;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getAltitude() {
        return altitude;
    }

    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    public LatLng getLatLng() {
        if (latLng == null) {
            latLng = new LatLng(latitude, longitude);
        }
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public void setLatLng(double latitude, double longitude) {
        latLng = new LatLng(latitude, longitude);
    }

    public Marker getMarker() {
        return marker;
    }

    public void setMarker(Marker marker) {
        this.marker = marker;
    }
}
