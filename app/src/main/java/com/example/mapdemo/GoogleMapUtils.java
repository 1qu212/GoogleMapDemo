package com.example.mapdemo;

import android.graphics.Color;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapUtils {
    /**
     * 添加marker并移动相机(画点)
     *
     * @param googleMap
     * @param position
     * @param title
     * @return 返回了Marker，删除时需要用到
     */
    public static Marker addMapMarker(GoogleMap googleMap, LatLng position, String title) {
        return addMapMarker(googleMap, position, title, 15);//15和默认定位等级一致
    }

    public static Marker addMapMarker(GoogleMap googleMap, LatLng position, String title, float zoom) {
        Marker marker = googleMap.addMarker(new MarkerOptions().position(position).title(title));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
        return marker;
    }

    /**
     * 添加polyline
     *
     * @param googleMap
     * @param latLngs
     * @return
     */
    public static Polyline addPolyline(GoogleMap googleMap, LatLng... latLngs) {
        return addPolyline(googleMap, 5, Color.BLUE, true, latLngs);
    }

    public static Polyline addPolyline(GoogleMap googleMap, float width, int color, boolean clickable, LatLng... latLngs) {
        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .width(width)
                .color(color)
                .clickable(clickable)
                .add(latLngs));
        return polyline;
    }
}
