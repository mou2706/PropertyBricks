package com.openwebsolutions.propertybricks.LoginCheck;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.Settings;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.Model.Login;
import com.openwebsolutions.propertybricks.SigninActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginCheck {
    Context context;
    static boolean value=true;

    SharedPreferences sh;
    SharedPreferences.Editor editor;


    public LoginCheck(Context context) {
        this.context = context;
    }

    public void getLoginCheck(final String email, final String password,final String device_token){
//        MainApplication.apiManager.loginUser(email, password,device_token, new Callback<Login>() {
////            @Override
////            public void onResponse(Call<Login> call, Response<Login> response) {
////
////                Login responseUser = response.body();
////                if (response.isSuccessful() && responseUser != null) {
////
////                    sh=context.getSharedPreferences("user", Context.MODE_PRIVATE);
////                    editor=sh.edit();
////                    editor.putString("Email_id",email);
////                    editor.putString("Password",password);
////                    editor.putString("_Token",responseUser.getToken());
////                    editor.putString("device_token",responseUser.getUser().getDeviceToken());
////
////                    editor.putString("profile_image", responseUser.getUser().getImage());
////                    editor.putString("user_name", responseUser.getUser().getName());
////                    editor.putString("user_email",responseUser.getUser().getEmail());
////                    editor.putString("user_phn",responseUser.getUser().getPhone());
////                    editor.putString("user_gstin",String.valueOf(responseUser.getUser().getCompGstin()));
////                    editor.putString("user_address",responseUser.getUser().getAddress());
////                    editor.commit();
////
////                } else {
////                    Toast.makeText(context,
////                            "Token Expired Please Login"
////                            , Toast.LENGTH_LONG).show();
////                    sh=context.getSharedPreferences("user", Context.MODE_PRIVATE);
////                    editor=sh.edit();
////                    editor.putString("Email_id","");
////                    editor.putString("Password","");
////                    editor.putString("_Token","");
////                    editor.putString("profile_image", "");
////                    editor.putString("user_name", "");
////                    editor.putString("user_email","");
////                    editor.putString("user_phn","");
////                    editor.putString("user_gstin",String.valueOf(""));
////                    editor.putString("user_address","");
////                    editor.putString("device_token","");
////                    editor.commit();
////                    Intent intent=new Intent(context,SigninActivity.class);
////                    context.startActivity(intent);
////                }
////            }
//
//            @Override
//            public void onFailure(Call<Login> call, Throwable t) {
//                Toast.makeText(context,
//                        "Error is " + t.getMessage()
//                        , Toast.LENGTH_LONG).show();
//            }
//        });


    }
}
