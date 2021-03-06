package com.sq.amap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sq.amap.cluster.ClusterActivity;
import com.sq.amap.info_window.PoiInfoActivity;
import com.sq.amap.location_circle.LocationCircleActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.rotation_location)
    public void onRotationLocationClicked() {
        RotationLocationActivity.start(this);
    }

    @OnClick(R.id.location_service)
    public void onLocationServiceClicked() {
        LocationServiceActivity.start(this);
    }

    @OnClick(R.id.path_record)
    public void onPathRecordClicked() {
        PathRecordActivity.start(this);
    }

    @OnClick(R.id.initial_center)
    public void onInitialCenterClicked() {
        InitialWelcomeActivity.start(this);
    }

    @OnClick(R.id.info_window)
    public void onInfoWindowClicked() {
        PoiInfoActivity.start(this);
    }

    @OnClick(R.id.location_circle)
    public void onLocationCircleClicked() {
        LocationCircleActivity.start(this);
    }

    @OnClick(R.id.cluster)
    public void onViewClicked() {
        ClusterActivity.start(this);
    }
}
