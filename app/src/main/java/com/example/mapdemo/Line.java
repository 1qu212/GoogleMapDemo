package com.example.mapdemo;

import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public class Line {
    private String lineName;
    private List<Point> pointList;
    private Polyline polyline;

    public String getLineName() {
        return lineName;
    }

    public void setLineName(String lineName) {
        this.lineName = lineName;
    }

    public Polyline getPolyline() {
        return polyline;
    }

    public void setPolyline(Polyline polyline) {
        this.polyline = polyline;
    }

    public List<Point> getPointList() {
        return pointList;
    }

    public void setPointList(List<Point> pointList) {
        this.pointList = pointList;
    }
}
