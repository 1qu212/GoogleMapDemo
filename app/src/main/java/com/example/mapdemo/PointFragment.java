package com.example.mapdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PointFragment extends Fragment implements View.OnClickListener {
    private EditText etPointName;
    private EditText etLatitude;
    private EditText etLongitude;
    private EditText etAltitude;
    /**
     * 标记
     */
    private Button btnMark;
    /**
     * 移除
     */
    private Button btnRemove;
    private List<Point> mPointList = new ArrayList<Point>() {{
        add(new Point("01", 29.0, 115.0, 5.0));
        add(new Point("02", 29.1, 115.5, 5.0));
        add(new Point("03", 29.2, 115.0, 5.0));
        add(new Point("04", 29.3, 115.5, 5.0));
        add(new Point("05", 29.4, 115.0, 5.0));
        add(new Point("06", 29.5, 115.5, 5.0));
    }};
    /**
     * 载入
     */
    private Button btnLoad;
    /**
     * 清空
     */
    private Button btnClear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_point, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etPointName = (EditText) view.findViewById(R.id.et_point_name);
        etLatitude = (EditText) view.findViewById(R.id.et_latitude);
        etLongitude = (EditText) view.findViewById(R.id.et_longitude);
        etAltitude = (EditText) view.findViewById(R.id.et_altitude);
        btnMark = (Button) view.findViewById(R.id.btn_mark);
        btnMark.setOnClickListener(this);
        btnRemove = (Button) view.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(this);
        btnClear = (Button) view.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(this);
        btnLoad = (Button) view.findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_mark:
                if (etPointName.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请填写点号", Toast.LENGTH_SHORT).show();
                    break;
                }
                for (Point point : mPointList) {
                    if (etPointName.getText().toString().trim().equals(point.getPointName())) {
                        Toast.makeText(getActivity(), "该点号已经使用", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (etLatitude.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请填写纬度", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (etLongitude.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请填写经度", Toast.LENGTH_SHORT).show();
                    break;
                }
                if (etAltitude.getText().toString().isEmpty()) {
                    Toast.makeText(getActivity(), "请填写海拔", Toast.LENGTH_SHORT).show();
                    break;
                }
                MainActivity mainActivity = (MainActivity) getActivity();
                GoogleMapFragment googleMapFragment = (GoogleMapFragment) mainActivity.getmFragmentList().get(2);
                if (googleMapFragment.checkReady()) {
                    Point point = new Point();
                    point.setPointName(etPointName.getText().toString().trim());
                    point.setLatitude(Double.parseDouble(etLatitude.getText().toString().trim()));
                    point.setLongitude(Double.parseDouble(etLongitude.getText().toString().trim()));
                    point.setAltitude(Double.parseDouble(etAltitude.getText().toString().trim()));
                    Marker marker = googleMapFragment.addPoint(point);
                    point.setMarker(marker);
                    mPointList.add(point);
                }
                break;
            case R.id.btn_remove:
                String pointName = etPointName.getText().toString().trim();
                Iterator<Point> it = mPointList.iterator();
                while (it.hasNext()) {
                    Point point = it.next();
                    if (point.getPointName().equals(pointName)) {
                        MainActivity mainActivity1 = (MainActivity) getActivity();
                        GoogleMapFragment googleMapFragment1 = (GoogleMapFragment) mainActivity1.getmFragmentList().get(2);
                        if (point.getMarker() != null) {
                            googleMapFragment1.removeMarker(point.getMarker());
                        }
                        it.remove();
                        Toast.makeText(getActivity(), "点号为" + pointName + "的点已经被移除", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Toast.makeText(getActivity(), "不存在点号为" + pointName + "的点", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_clear:
                etPointName.setText("");
                etLatitude.setText("");
                etLongitude.setText("");
                etAltitude.setText("");
                break;
            case R.id.btn_load:
                loadData();
                break;
        }
    }

    /**
     * 加载marker并缩放到全部可视的比例
     */
    private void loadData() {
        MainActivity mainActivity = (MainActivity) getActivity();
        GoogleMapFragment googleMapFragment = (GoogleMapFragment) mainActivity.getmFragmentList().get(2);
        if (googleMapFragment.checkReady()) {
            List<LatLng> latLngList = new ArrayList<>();
            for (Point point : mPointList) {
                Marker marker = googleMapFragment.addPoint(point);
                point.setMarker(marker);
                latLngList.add(point.getLatLng());
            }
            LatLng[] latLngs = new LatLng[latLngList.size()];
            googleMapFragment.autoZoom(latLngList.toArray(latLngs));
        }
    }

    public void mapClick(LatLng latLng) {
        etPointName.setText("");
        etLatitude.setText(String.valueOf(latLng.latitude));
        etLongitude.setText(String.valueOf(latLng.longitude));
        etAltitude.setText("");
    }

    public List<Point> getmPointList() {
        return mPointList;
    }
}
