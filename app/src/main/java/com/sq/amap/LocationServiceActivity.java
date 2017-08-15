package com.sq.amap;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Connection;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sq.amap.service.LocationService;
import com.sq.amap.service.LocationStatusManager;
import com.sq.amap.service.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 通过后台服务持续定位
 */
public class LocationServiceActivity extends AppCompatActivity {

    public static final String RECEIVER_ACTION = "location_in_background";
    @BindView(R.id.button_start_service)
    Button buttonStartService;
    @BindView(R.id.tv_result)
    TextView tvResult;
    private Connection mLocationServiceConn = null;
    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RECEIVER_ACTION)) {
                String locationResult = intent.getStringExtra("result");
                if (null != locationResult && !locationResult.trim().equals("")) {
                    tvResult.setText(locationResult);
                }
            }
        }
    };

    public static void start(Context context) {
        Intent starter = new Intent(context, LocationServiceActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        ButterKnife.bind(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION);
        registerReceiver(locationChangeBroadcastReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (locationChangeBroadcastReceiver != null)
            unregisterReceiver(locationChangeBroadcastReceiver);

        super.onDestroy();
    }

    /**
     * 启动或者关闭定位服务
     */
    public void startService(View v) {
        if (buttonStartService.getText().toString().equals(getResources().getString(R.string.startLocation))) {
            startLocationService();

            buttonStartService.setText(R.string.stopLocation);
            tvResult.setText("正在定位...");
        } else {
            stopLocationService();

            buttonStartService.setText(R.string.startLocation);
            tvResult.setText("");
        }
        LocationStatusManager.getInstance().resetToInit(getApplicationContext());
    }

    /**
     * 开始定位服务
     */
    private void startLocationService() {
        getApplicationContext().startService(new Intent(this, LocationService.class));
    }

    /**
     * 关闭服务
     * 先关闭守护进程，再关闭定位服务
     */
    private void stopLocationService() {
        sendBroadcast(Utils.getCloseBrodecastIntent());
    }

}
