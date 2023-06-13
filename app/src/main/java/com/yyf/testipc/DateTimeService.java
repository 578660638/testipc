package com.yyf.testipc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateTimeService extends Service {
    private final IDateTimeService.Stub binder = new IDateTimeService.Stub() {
        @Override
        public String getCurrentDateTime() throws RemoteException {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            return sdf.format(new Date());
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        stackTrace();
        return binder;
    }
    public  void stackTrace(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            Log.i("yyff",element.getClassName()+element.getMethodName());
        }

    }
}