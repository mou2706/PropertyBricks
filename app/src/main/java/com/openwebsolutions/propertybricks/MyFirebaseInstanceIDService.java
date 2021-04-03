package com.openwebsolutions.propertybricks;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
//import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.openwebsolutions.propertybricks.AppData.AppData;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    Context context;

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    private static final String Rec_token="Recent_token";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        try {
            String push_token = FirebaseInstanceId.getInstance().getToken();
            Log.d(Rec_token, "Refreshed token: " + push_token);

            sh=getSharedPreferences("user", Context.MODE_PRIVATE);
            editor=sh.edit();
            editor.putString("device_token",push_token);
            editor.commit();

            AppData.push_token=push_token;
        }
        catch (Exception e){

        }





//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor= sharedPreferences.edit();
//        editor.putString("push_tkn",push_token);
//        editor.commit();
    }
}
