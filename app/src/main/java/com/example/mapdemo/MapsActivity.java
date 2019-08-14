package com.example.mapdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapsActivity extends AppCompatActivity implements
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-34, 151);
        GoogleMapUtils.addMapMarker(mMap, sydney, "Marker in Sydney");
    }

    /**
     * 点击定位按钮
     *
     * @return
     */
    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        return false;
    }

    /**
     * 点击了地图上我的位置
     *
     * @param location
     */
    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
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
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
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
        Toast.makeText(this, "tapped, point=\n" + latLng, Toast.LENGTH_LONG).show();
        GoogleMapUtils.addMapMarker(mMap, latLng, "tapped");
    }

    /**
     * 长击地图
     *
     * @param latLng
     */
    @Override
    public void onMapLongClick(LatLng latLng) {
        Toast.makeText(this, "long pressed, point=\n" + latLng, Toast.LENGTH_LONG).show();
        GoogleMapUtils.addMapMarker(mMap, latLng, "long pressed");
    }

    /**
     * 检测地图是否准备完成
     *
     * @return
     */
    private boolean checkReady() {
        if (mMap == null) {
            Toast.makeText(this, R.string.map_not_ready, Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * marker点击事件
     * 这里点击后移除该marker
     *
     * @param marker
     * @return
     */
    @Override
    public boolean onMarkerClick(Marker marker) {
//        if (checkReady()) {
//            marker.remove();
//        }
        return true;
    }
}
