package com.wristband.yt_b_4.wristbandclient.app;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.widget.Toast;

/**
 * Created by Mike on 10/30/2017.
 */

public class PhoneController extends Application {
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    @Override
    public void onCreate() {
        super.onCreate();
    }

    @SuppressWarnings("deprecation")
    public void sendSMS(Context context, String phoneNumber, String message)
    {

        try
        {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);

        }
        catch(Exception e)
        {
            Toast.makeText(context, "Error" + e,Toast.LENGTH_LONG).show();
        }
    }
}
