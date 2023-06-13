package com.yyf.testipc;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.yyf.testipc.databinding.ActivityMainBinding;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IDateTimeService dateTimeService;
    private boolean isBound = false;
    public native String stringFromJNI();
    private Button btnGetDateTime;
    private TextView tvDateTime;
    private List<ActivityManager.RunningServiceInfo> mServices;
   // private ServiceListAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGetDateTime = findViewById(R.id.btn_get_datetime);
        tvDateTime = findViewById(R.id.tv_datetime);

        btnGetDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isBound) {
                    try {
                        String dateTime = dateTimeService.getCurrentDateTime();
                        tvDateTime.setText(dateTime);
                        getRunningServices();
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Intent intent = new Intent(this, DateTimeService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }
    private void getRunningServices() {
        ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        mServices = am.getRunningServices(Integer.MAX_VALUE);
        List<ActivityManager.RunningServiceInfo> list = mServices;

        if (list != null && !list.isEmpty()) {
            for (ActivityManager.RunningServiceInfo service : list) {
                Log.i("RunningServices", service.service.toString());
                Log.i("RunningServices", "PID: " + service.pid + ", UID: " + service.uid);
                Log.i("RunningServices", service.process + ", Foreground: " + service.foreground);
            }
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isBound) {
            unbindService(connection);
            isBound = false;
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            dateTimeService = IDateTimeService.Stub.asInterface(service);
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }



    };




}