package com.example.mapdemo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LineFragment extends Fragment implements View.OnClickListener {
    private EditText etLineName;
    /**
     * 载入
     */
    private Button btnLoad;
    /**
     * 新建线段
     */
    private Button btnNew;
    /**
     * 开始画线
     */
    private Button btnDraw;
    /**
     * 移除
     */
    private Button btnRemove;
    private ListView lvPoint;
    private List<Point> mPointList;
    private SpinnerAdapter spinnerAdapter;
    private TextView tvSelectPoint;
    private ListPopupWindow mPopupWindow;
    private List<Point> lvPointList;
    private SpinnerAdapter listAdapter;
    private List<Line> mLineList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_line, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        etLineName = (EditText) view.findViewById(R.id.et_line_name);
        btnLoad = (Button) view.findViewById(R.id.btn_load);
        btnLoad.setOnClickListener(this);
        btnNew = (Button) view.findViewById(R.id.btn_new);
        btnNew.setOnClickListener(this);
        btnDraw = (Button) view.findViewById(R.id.btn_draw);
        btnDraw.setOnClickListener(this);
        btnRemove = (Button) view.findViewById(R.id.btn_remove);
        btnRemove.setOnClickListener(this);
        lvPoint = (ListView) view.findViewById(R.id.lv_point);
        tvSelectPoint = (TextView) view.findViewById(R.id.tv_select_point);
    }

    @Override
    public void onClick(View v) {
        MainActivity mainActivity = (MainActivity) getActivity();
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_load:
                break;
            case R.id.btn_new:
                etLineName.setText("");
                tvSelectPoint.setText("点击选点");
                PointFragment pointFragment = (PointFragment) mainActivity.getmFragmentList().get(0);
                mPointList = new ArrayList<>(pointFragment.getmPointList());
                spinnerAdapter = new SpinnerAdapter(mPointList);
                mPopupWindow = new ListPopupWindow(getActivity());
                mPopupWindow.setAdapter(spinnerAdapter);
                mPopupWindow.setAnchorView(tvSelectPoint);
                lvPointList = new ArrayList<>();
                listAdapter = new SpinnerAdapter(lvPointList);
                lvPoint.setAdapter(listAdapter);
                mPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        tvSelectPoint.setText(((TextView) view).getText().toString().trim());
                        lvPointList.add(mPointList.get(position));
                        listAdapter.notifyDataSetChanged();
                        //这里是一条线上不能选择同一点，所以remove
                        mPointList.remove(position);
                        spinnerAdapter.notifyDataSetChanged();
                    }
                });
                tvSelectPoint.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mPopupWindow.show();
                    }
                });
                break;
            case R.id.btn_draw:
                if (etLineName.getText().toString().trim().isEmpty()) {
                    Toast.makeText(getActivity(), "请填写线号", Toast.LENGTH_SHORT).show();
                    break;
                }
                for (Line line : mLineList) {
                    if (etLineName.getText().toString().trim().equals(line.getLineName())) {
                        Toast.makeText(getActivity(), "该线号已经使用", Toast.LENGTH_SHORT).show();
                        break;
                    }
                }
                if (lvPointList.size() < 2) {
                    Toast.makeText(getActivity(), "至少选择两个点", Toast.LENGTH_SHORT).show();
                    break;
                }
                //在地图上画线
                GoogleMapFragment googleMapFragment = (GoogleMapFragment) mainActivity.getmFragmentList().get(2);
                List<LatLng> latLngList = new ArrayList<>();
                for (Point p : lvPointList) {
                    latLngList.add(p.getLatLng());
                }
                LatLng[] latLngs = new LatLng[latLngList.size()];
                Polyline polyline = googleMapFragment.addPolyline(latLngList.toArray(latLngs));
                //包存线数据
                Line line = new Line();
                line.setLineName(etLineName.getText().toString().trim());
                line.setPointList(lvPointList);
                line.setPolyline(polyline);
                mLineList.add(line);
                //清空界面和数据
                etLineName.setText("");
                tvSelectPoint.setText("");
                mPointList.clear();
                spinnerAdapter.notifyDataSetChanged();
                lvPointList.clear();
                listAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_remove:
                GoogleMapFragment googleMapFragmentRemove = (GoogleMapFragment) mainActivity.getmFragmentList().get(2);
                String lineName = etLineName.getText().toString().trim();
                Iterator<Line> it = mLineList.iterator();
                while (it.hasNext()) {
                    Line line1 = it.next();
                    if (line1.getLineName().equals(lineName)) {
                        if (line1.getPolyline() != null) {
                            googleMapFragmentRemove.removePolyline(line1.getPolyline());
                        }
                        it.remove();
                        Toast.makeText(getActivity(), "线号为" + lineName + "的线已经被移除", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    Toast.makeText(getActivity(), "不存在线号为" + lineName + "的线", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 由于结构一致，所以做了listPopupWindow和ListView的适配器，如果不同可新建不同的适配器
     */
    class SpinnerAdapter extends BaseAdapter {
        private List<Point> pointList;

        public SpinnerAdapter(List<Point> pointList) {
            this.pointList = pointList;
        }

        @Override
        public int getCount() {
            return pointList == null ? 0 : pointList.size();
        }

        @Override
        public Object getItem(int position) {
            return pointList == null ? null : pointList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, parent, false);
            ((TextView) view).setText(pointList.get(position).getPointName());
            return view;
        }
    }
}
