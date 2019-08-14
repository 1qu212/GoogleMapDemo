package com.example.mapdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    /**
     * 点
     */
    private RadioButton tab0;
    /**
     * 线
     */
    private RadioButton tab1;
    /**
     * 地图
     */
    private RadioButton tab2;
    private RadioGroup radioGroup;
    private FrameLayout fragmentContainer;
    private List<Fragment> mFragmentList;
    private int mLastTab = 0;
    private int mCurTab = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mFragmentList = new ArrayList<>();
        mFragmentList.add(new PointFragment());
        mFragmentList.add(new LineFragment());
        mFragmentList.add(new GoogleMapFragment());
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.add(R.id.fragmentContainer, mFragmentList.get(0));
        ft.add(R.id.fragmentContainer, mFragmentList.get(1));
        ft.add(R.id.fragmentContainer, mFragmentList.get(2));
        ft.hide(mFragmentList.get(1));
        ft.hide(mFragmentList.get(2));
        ft.commit();
        radioGroup.check(R.id.tab0);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                mLastTab = mCurTab;
                switch (checkedId) {
                    case R.id.tab0:
                        mCurTab = 0;
                        break;
                    case R.id.tab1:
                        mCurTab = 1;
                        break;
                    case R.id.tab2:
                        mCurTab = 2;
                        break;
                }
                if (mLastTab != mCurTab) {
                    changeFragment(mLastTab, mCurTab);
                }
            }
        });
    }

    private void initView() {
        tab0 = (RadioButton) findViewById(R.id.tab0);
        tab1 = (RadioButton) findViewById(R.id.tab1);
        tab2 = (RadioButton) findViewById(R.id.tab2);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        fragmentContainer = (FrameLayout) findViewById(R.id.fragmentContainer);
    }

    private void changeFragment(int from, int to) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (!mFragmentList.get(to).isAdded()) {
            ft.add(R.id.fragmentContainer, mFragmentList.get(to));
            ft.show(mFragmentList.get(to));
            ft.hide(mFragmentList.get(from));
        } else {
            ft.show(mFragmentList.get(to)).hide(mFragmentList.get(from));
        }
        ft.commit();
    }

    public List<Fragment> getmFragmentList() {
        return mFragmentList;
    }
}
