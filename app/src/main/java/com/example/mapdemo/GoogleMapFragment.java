package com.example.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;

public class GoogleMapFragment extends Fragment implements
        OnMapReadyCallback//谷歌地图准备完成监听
        , GoogleMap.OnMyLocationButtonClickListener//点击定位按钮监听
        , GoogleMap.OnMyLocationClickListener//点击了地图上我的位置
        , ActivityCompat.OnRequestPermissionsResultCallback//动态申请权限
        , GoogleMap.OnMapClickListener//单击地图，获取单击的坐标
        , GoogleMap.OnMapLongClickListener//长击地图，获取长击的坐标
        , GoogleMap.OnMarkerClickListener//marker点击事件
{
    private GoogleMap mMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private UiSettings mUiSettings;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        enableMyLocation();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_google_map, container, false);
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment == null) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            mapFragment = SupportMapFragment.newInstance();
            ft.replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);
        return view;
    }

    /**
     * 谷歌地图准备完成
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        //一系列监听
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);

        mUiSettings = mMap.getUiSettings();
        mUiSettings.setZoomControlsEnabled(true);

        enableMyLocation();
    }

    /**
     * 点击定位按钮
     *
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(getActivity(), "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 点击了地图上我的位置
     *
     * @param location
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(getActivity(), "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    /**
     * 处理权限申请的结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        }
    }

    /**
     * 开启地图地位
     */
    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(), LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true,
                    getResources().getString(R.string.permission_rationale_location), getResources().getString(R.string.permission_required_toast));
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);//开启地图上的定位
        }
    }

    /**
     * 单击地图
     *
     * @param latLng
     */
    @Override
    public void onMapClick(LatLng latLng) {
        Toast.makeText(getActivity(), "tapped, point=\n" + latLng, Toast.LENGTH_LONG).show();
        MainActivity mainActivity = (MainActivity) getActivity();
        PointFragment pointFragment = (PointFragment) mainActivity.getmFragmentList().get(0);
        pointFragment.mapClick(latLng);
    }

    /**
     * 长击地图
     *
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(getActivity(), "long pressed, point=\n" + latLng, Toast.LENGTH_LONG).show();
    }

    /**
     * 检测地图是否准备完成
     *
     * @return
     */
    public boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(getActivity(), R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * marker点击事件
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
        Toast.makeText(getActivity(), "marker, point=\n" + marker.getPosition(), Toast.LENGTH_LONG).show();
        marker.showInfoWindow();
        return true;
    }

    public Marker addPoint(Point point) {
        return GoogleMapUtils.addMapMarker(mMap, point.getLatLng(), point.getPointName());
    }

    public void removeMarker(Marker marker) {
        marker.remove();
    }

    /**
     * 谷歌地图缩放
     * @param latLngs
     */
    public void autoZoom(LatLng... latLngs) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder.include(latLng);
        }
        LatLngBounds latLngBounds = builder.build();
        final int zoomWidth = getResources().getDisplayMetrics().widthPixels;
        final int zoomHeight = getResources().getDisplayMetrics().heightPixels;
        final int zoomPadding = (int) (zoomWidth * 0.10);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, zoomWidth, zoomHeight, zoomPadding));
    }

    public Polyline addPolyline(LatLng... latLngs) {
        Polyline polyline = GoogleMapUtils.addPolyline(mMap, latLngs);
        autoZoom(latLngs);
        return polyline;
    }

    public void removePolyline(Polyline polyline) {
        polyline.remove();
    }
}
