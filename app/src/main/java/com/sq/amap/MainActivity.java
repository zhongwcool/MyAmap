package com.sq.amap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.rotation_location, R.id.location_service, R.id.path_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rotation_location: {
                RotationLocationActivity.start(this);
            }
            break;
            case R.id.location_service: {
                LocationServiceActivity.start(this);
            }
            break;
            case R.id.path_record: {
                PathRecordActivity.start(this);
            }
            break;
        }
    }
}
