package com.sq.amap;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Connection;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.sq.amap.base.Constants;
import com.sq.amap.service.LocationService;
import com.sq.amap.service.LocationStatusManager;
import com.sq.amap.utils.LocationUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 通过后台服务持续定位
 */
public class LocationServiceActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private static final String TAG = LocationServiceActivity.class.getSimpleName();
    //private static final int RC_STORAGE_PERM = 124;
    private static final int RC_LOCATION_STORAGE_PERM = 123;

    @BindView(R.id.button_start_service)
    Button buttonStartService;
    @BindView(R.id.tv_result)
    TextView tvResult;

    private Connection mLocationServiceConn = null;
    private BroadcastReceiver locationChangeBroadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Constants.RECEIVER_ACTION)) {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @AfterPermissionGranted(RC_LOCATION_STORAGE_PERM)
    private boolean isPermissionGranted() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            return true;
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(
                    this,
                    getString(R.string.storage_and_location_rationale),
                    RC_LOCATION_STORAGE_PERM,
                    perms
            );
            return false;
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //init();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d(TAG, "onPermissionsDenied:" + requestCode + ":" + perms.size());

        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            // Do something after user returned from app settings screen, like showing a Toast.
            Toast.makeText(this, R.string.returned_from_app_settings_to_activity, Toast.LENGTH_SHORT)
                    .show();
            isPermissionGranted();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_service);
        ButterKnife.bind(this);

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.RECEIVER_ACTION);
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
        if (!isPermissionGranted()) {
            return;
        }
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
        sendBroadcast(LocationUtil.getCloseBrodecastIntent());
    }

}
