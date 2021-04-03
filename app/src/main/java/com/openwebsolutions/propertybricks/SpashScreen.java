package com.openwebsolutions.propertybricks;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SpashScreen extends AppCompatActivity {
    public static final int delaytime=5000;
    View overlay_view;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash_screen);
        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();

        overlay_view=findViewById(R.id.overlay_view);

        token=sh.getString("_Token","");

        Handler handler=new Handler();

        if(token.equals("")) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    Intent in = new Intent(SpashScreen.this, SigninActivity.class);
                    startActivity(in);
                    finish();

                }
            }, delaytime);
        }
        else {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent in = new Intent(SpashScreen.this, MainActivity.class);
                    startActivity(in);
                    finish();

                }
            }, delaytime);
        }
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, importance);
                mChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mNotificationManager.createNotificationChannel(mChannel);
        }
    }
}
