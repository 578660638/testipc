package com.yyf.testipc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yyf.testipc.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private IDateTimeService dateTimeService;
    private boolean isBound = false;
    public native String stringFromJNI();
    private Button btnGetDateTime;
    private TextView tvDateTime;

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
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Intent intent = new Intent(this, DateTimeService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
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