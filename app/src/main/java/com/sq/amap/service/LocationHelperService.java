package com.sq.amap.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;

import com.sq.amap.ILocationHelperServiceAIDL;
import com.sq.amap.ILocationServiceAIDL;
import com.sq.amap.base.Constants;

/**
 * Created by liangchao_suxun on 17/1/18.
 */

public class LocationHelperService extends Service {


    private Utils.CloseServiceReceiver mCloseReceiver;
    private ServiceConnection mInnerConnection;
    private HelperBinder mBinder;

    @Override
    public void onCreate() {
        super.onCreate();
        startBind();
        mCloseReceiver = new Utils.CloseServiceReceiver(this);
        registerReceiver(mCloseReceiver, Utils.getCloseServiceFilter());
    }

    @Override
    public void onDestroy() {
        if (mInnerConnection != null) {
            unbindService(mInnerConnection);
            mInnerConnection = null;
        }

        if (mCloseReceiver != null) {
            unregisterReceiver(mCloseReceiver);
            mCloseReceiver = null;
        }

        super.onDestroy();
    }

    private void startBind() {
        final String locationServiceName = Constants.action.LOCATION_SERVICE;
        mInnerConnection = new ServiceConnection() {

            @Override
            public void onServiceDisconnected(ComponentName name) {
                Intent intent = new Intent();
                intent.setAction(locationServiceName);
                startService(Utils.getExplicitIntent(getApplicationContext(), intent));
            }

            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ILocationServiceAIDL l = ILocationServiceAIDL.Stub.asInterface(service);
                try {
                    l.onFinishBind();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        };

        Intent intent = new Intent();
        intent.setAction(locationServiceName);
        bindService(Utils.getExplicitIntent(getApplicationContext(), intent), mInnerConnection, Service.BIND_AUTO_CREATE);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        if (mBinder == null) {
            mBinder = new HelperBinder();
        }

        return mBinder;
    }

    private class HelperBinder extends ILocationHelperServiceAIDL.Stub {
        @Override
        public void onFinishBind(int notiId) throws RemoteException {
            startForeground(notiId, Utils.buildNotification(LocationHelperService.this.getApplicationContext()));
            stopForeground(true);
        }
    }
}
