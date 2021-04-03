package com.openwebsolutions.propertybricks;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.SortedList;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.Model.UpdateUserDemo.UpdateUser;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfileActivity extends AppCompatActivity {
    private EditText name, email,phone,address,gstin;
    private ImageView backButton;
    private RelativeLayout relative;
    private TextView submit;
    String path="";

    private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private String na_me,ph,e_mail;
    private String token;
    SharedPreferences sh;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        phone=findViewById(R.id.phone_edit);
        name = findViewById(R.id.name_edit);
        email = findViewById(R.id.email_edit);
        submit = findViewById(R.id.tv_update);
        relative = findViewById(R.id.rel_update);
        address = findViewById(R.id.address_edit);
        backButton=findViewById(R.id.iv_back_profile);
        gstin=findViewById(R.id.gstin_edit);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        SharedPreferences sharedPreferences=getSharedPreferences("user_login", Context.MODE_PRIVATE);
        token=sharedPreferences.getString("token","");



        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (valid()) {
                    e_mail=email.getText().toString().trim();
                    if (e_mail.matches(emailPattern)) {
                        checkUserUpdatedProfile();
                    } else {
                        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                        Toast.makeText(UserProfileActivity.this,"Password mismatched",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void checkUserUpdatedProfile() {

        File file1 = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);
        RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone.getText().toString().trim());
        RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name.getText().toString().trim());
        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email.getText().toString().trim());
        RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address.getText().toString().trim());
//        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), property_image);
        RequestBody gstin1 = RequestBody.create(MediaType.parse("text/plain"), gstin.getText().toString().trim());


        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file1.getName(), requestBody);
        token = sh.getString("_Token", "");

//        MainApplication.apiManager.update_user("Bearer"+" "+token,name1,phone1,email1,address1,gstin1, new Callback<UpdateUser>() {
// @Override
//            public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {
//                UpdateUser responseUser= response.body();
//                if (response.isSuccessful() && responseUser != null) {
//                    Toast.makeText(UserProfileActivity.this,"Profile updated successfully", Toast.LENGTH_SHORT).show();
//                }
//                else {
//                    Toast.makeText(UserProfileActivity.this,
//                            String.format("Response is %s", String.valueOf(response.code()))
//                            , Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<UpdateUser> call, Throwable t) {
//
//            }
//
////            @Override
////            public void onFailure(Call<UserProfileUpdateModel> call, Throwable t) {
////                Log.d("error",t.getMessage());
////                Toast.makeText(UserProfileActivity.this, "Please enter valid email and password", Toast.LENGTH_LONG).show();
////
////            }
//        });

    }

    private boolean valid() {
        boolean isvalid = true;
        if (name.getText().toString().trim().isEmpty()) {
            isvalid = false;
            Toast.makeText(UserProfileActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return isvalid;
        }
        if (phone.getText().toString().trim().isEmpty()) {
            isvalid = false;
            Toast.makeText(UserProfileActivity.this, "Please phone number", Toast.LENGTH_SHORT).show();
            phone.requestFocus();
            return isvalid;
        }
        if (email.getText().toString().trim().isEmpty()) {
            isvalid = false;
            Toast.makeText(UserProfileActivity.this, "Please email", Toast.LENGTH_SHORT).show();
            email.requestFocus();
            return isvalid;
        }
        if (name.getText().toString().trim().isEmpty()&&email.getText().toString().trim().isEmpty() && phone.getText().toString().trim().isEmpty()) {
            isvalid = false;
            Toast.makeText(UserProfileActivity.this, "Please enter name", Toast.LENGTH_SHORT).show();
            name.requestFocus();
            return isvalid;
        }
        return isvalid;
    }
}
