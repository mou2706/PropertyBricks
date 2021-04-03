package com.openwebsolutions.propertybricks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.provider.Settings.Secure;


import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.Model.Register;

import com.openwebsolutions.propertybricks.fragment.Fragment_Business;
import com.openwebsolutions.propertybricks.fragment.Fragment_Personal;

import org.json.JSONObject;

import java.io.File;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity  implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener {

    String password_bis="";
    private GoogleApiClient googleApiClient;
    TextView textView;
    private static final int RC_SIGN_IN = 1;

    private static final int REQ_CODE= 9001;
//    SignInButton iv_gmail;

    int flag1=1;
    TextView tv_signup;
    private TextView tv_personal,tv_business;
    Fragment personal_signup,business_signup;

    String et_personal_name_signup,et_personal_phone_signup,et_personal_email_signup,et_personal_address_signup,et_personal_password_signup,et_personal_confirmpassword_signup;
    Bitmap personal_bitmap;
    String business_name_signup,business_phone_signup,business_email_signup,business_address_signup,business_password_signup,business_confirmpassword_signup,business_gstn_signup;
    Bitmap business_bitmap_signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    SharedPreferences sh;
    SharedPreferences.Editor editor;

    String path="";
    String device_token="";
    CallbackManager callbackManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_signup);
        printKeyHash();

        init();

//        initializeControls();
//        loginWithFb();

        personal_signup=new Fragment_Personal();
        FragmentManager fm=getSupportFragmentManager();
        FragmentTransaction ft=fm.beginTransaction();
        ft.replace(R.id.fragment_place,personal_signup);
        ft.commit();

        business_signup=new Fragment_Business();

//
//        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail().build();
//        googleApiClient= new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
//        googleApiClient=new GoogleApiClient.Builder(this)
//                .enableAutoManage(this,this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions)
//                .build();

    }

    private void printKeyHash() {
        try {
            PackageInfo info=getPackageManager().getPackageInfo("com.openwebsolutions.propertybricks", PackageManager.GET_SIGNATURES);
          for(Signature signature:info.signatures)
          {
              MessageDigest md=MessageDigest.getInstance("SHA");
              md.update(signature.toByteArray());
              Log.d("KeyHash", Base64.encodeToString(md.digest(),Base64.DEFAULT));

          }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }


    private void init() {
//        iv_gmail=findViewById(R.id.sign_in_button);
//        iv_gmail.setOnClickListener(this);
        tv_personal=findViewById(R.id.tv_personal);
        tv_business=findViewById(R.id.tv_business);

        tv_signup=findViewById(R.id.tv_signup);

        tv_signup.setOnClickListener(this);
        tv_personal.setOnClickListener(this);
        tv_business.setOnClickListener(this);

        Toasty.Config.getInstance()
        .apply(); // required
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_personal:
                changefragment(v);
                flag1=1;
                tv_personal.setBackgroundColor(ContextCompat.getColor(this, R.color.colorText));
                tv_business.setBackgroundColor(ContextCompat.getColor(this,R.color.light_colorText));
                break;

            case R.id.tv_business:
                changefragment(v);
                flag1=2;
                tv_business.setBackgroundColor(ContextCompat.getColor(this, R.color.colorText));
                tv_personal.setBackgroundColor(ContextCompat.getColor(this,R.color.light_colorText));
                break;

            case R.id.tv_signup:
                try {
                    if (flag1 == 2) {
                        business_name_signup = ((Fragment_Business) business_signup).getNameBusiness_signup().getText().toString().trim();
                        String name = business_name_signup;
                        business_phone_signup = ((Fragment_Business) business_signup).getPhoneBusiness_signup().getText().toString().trim();
                        String phone = business_phone_signup;
                        business_email_signup = ((Fragment_Business) business_signup).getEmailBusiness_signup().getText().toString().trim();
                        String email = business_email_signup;
                        business_address_signup = ((Fragment_Business) business_signup).getAddressBusiness_signup().getText().toString().trim();
                        String address = business_address_signup;
                        business_password_signup = ((Fragment_Business) business_signup).getPasswordBusiness_signup().getText().toString().trim();
                        final String password = business_password_signup;
                        business_confirmpassword_signup = ((Fragment_Business) business_signup).getConfirm_PasswordBusiness_signup().getText().toString().trim();
                        business_gstn_signup = ((Fragment_Business) business_signup).getGSTNBusiness_signup().getText().toString().trim();
                        business_bitmap_signup = ((Fragment_Business) business_signup).getpictureBusiness_signup();
                        path = ((Fragment_Business) business_signup).getpictureBusinesspath_Signup();

                        SharedPreferences sh1=getSharedPreferences("user", Context.MODE_PRIVATE);
                        device_token=sh1.getString("device_token","");

                        if (valid_business()) {
                            File file = new File(path);
                            if (path.equals("")) {
                                RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name);
                                RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone);
                                RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email);
                                RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address);
                                RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), password);
                                RequestBody comp_gstin = RequestBody.create(MediaType.parse("text/plain"), business_gstn_signup);
                                RequestBody image = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody device_token1 = RequestBody.create(MediaType.parse("text/plain"), device_token);

                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                                    MainApplication.apiManager.createUserWithoutPath(name1, phone1, email1, address1, password1, comp_gstin,device_token1, image, new Callback<Register>() {
                                        @Override
                                        public void onResponse(Call<Register> call, Response<Register> response) {

                                            Register responseUser = response.body();
                                            if (response.isSuccessful() && responseUser != null) {
//                                                Toast.makeText(SignupActivity.this, responseUser.getUser().getName() + " " + responseUser.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                                                sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                editor = sh.edit();
                                                editor.putString("_Token", responseUser.getToken());
                                                editor.putString("Email_id",responseUser.getUser().getEmail());
                                                editor.putString("Password",password);
                                                editor.putString("profile_image", responseUser.getUser().getImage());
                                                editor.putString("user_name", responseUser.getUser().getName());
                                                editor.putString("device_token",responseUser.getUser().getDeviceToken());

                                                editor.putString("user_email",responseUser.getUser().getEmail());
                                                editor.putString("user_phn",responseUser.getUser().getPhone());
                                                editor.putString("user_gstin",String.valueOf(responseUser.getUser().getCompGstin()));
                                                editor.putString("user_address",responseUser.getUser().getAddress());
                                                editor.commit();

                                                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit2);
                                                progressBar.setVisibility(View.VISIBLE);
                                                tv_signup.setVisibility(View.GONE);
                                                ThreeBounce threeBounce = new ThreeBounce();
                                                progressBar.setIndeterminateDrawable(threeBounce);

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressBar.setVisibility(View.GONE);
                                                        tv_signup.setVisibility(View.VISIBLE);
                                                        Intent intent_per = new Intent(SignupActivity.this, MainActivity.class);
                                                        startActivity(intent_per);
                                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                        finish();
                                                    }
                                                }, 2000);
                                            } else {
                                                Toasty.error(SignupActivity.this,
                                                       "Email id already exists"
                                                        , Toast.LENGTH_LONG).show();
                                                Log.v("Error code 400", response.errorBody().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Register> call, Throwable t) {

                                            Toast.makeText(SignupActivity.this,
                                                    "Error is " + t.getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(SignupActivity.this, "Please check Internet connection", Toast.LENGTH_LONG).show();

                                }

                            }

                            else {
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                                RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name);
                                RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone);
                                RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email);
                                RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address);
                                RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), password);
                                RequestBody comp_gstin = RequestBody.create(MediaType.parse("text/plain"), business_gstn_signup);
                                RequestBody device_token1 = RequestBody.create(MediaType.parse("text/plain"), device_token);


                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                                    MainApplication.apiManager.createUser(name1, phone1, email1, address1, password1, comp_gstin,device_token1, fileToUpload, new Callback<Register>() {
                                        @Override
                                        public void onResponse(Call<Register> call, Response<Register> response) {

                                            Register responseUser = response.body();
                                            if (response.isSuccessful() && responseUser != null) {
//                                                Toast.makeText(SignupActivity.this, responseUser.getUser().getName() + " " + responseUser.getUser().getEmail(), Toast.LENGTH_SHORT).show();
                                                sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                editor = sh.edit();
                                                editor.putString("_Token", responseUser.getToken());
                                                editor.putString("Email_id",responseUser.getUser().getEmail());
                                                editor.putString("Password",password);

                                                editor.putString("profile_image", responseUser.getUser().getImage());
                                                editor.putString("user_name", responseUser.getUser().getName());
                                                editor.putString("device_token",responseUser.getUser().getDeviceToken());

                                                editor.putString("user_email",responseUser.getUser().getEmail());
                                                editor.putString("user_phn",responseUser.getUser().getPhone());
                                                editor.putString("user_gstin",String.valueOf(responseUser.getUser().getCompGstin()));
                                                editor.putString("user_address",responseUser.getUser().getAddress());

                                                editor.commit();

                                                final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit2);
                                                progressBar.setVisibility(View.VISIBLE);
                                                tv_signup.setVisibility(View.GONE);
                                                ThreeBounce threeBounce = new ThreeBounce();
                                                progressBar.setIndeterminateDrawable(threeBounce);

                                                Handler handler = new Handler();
                                                handler.postDelayed(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        progressBar.setVisibility(View.GONE);
                                                        tv_signup.setVisibility(View.VISIBLE);
                                                        Intent intent_per = new Intent(SignupActivity.this, MainActivity.class);
                                                        startActivity(intent_per);
                                                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                        finish();
                                                    }
                                                }, 2000);
                                            } else {
                                                Toasty.error(SignupActivity.this,
                                                        "Email id already exists"
                                                        , Toast.LENGTH_LONG).show();
                                                Log.v("Error code 400", response.errorBody().toString());
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<Register> call, Throwable t) {

                                            Toast.makeText(SignupActivity.this,
                                                    "Error is " + t.getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                        }
                                    });
                                } else {
                                    Toast.makeText(SignupActivity.this, "Please check Internet connection", Toast.LENGTH_LONG).show();

                                }
                            }

                        }

                    } else {
                        et_personal_name_signup = ((Fragment_Personal) personal_signup).getNamePersonal_signup().getText().toString().trim();
                        String name = et_personal_name_signup;
                        et_personal_phone_signup = ((Fragment_Personal) personal_signup).getPhonePersonal_signup().getText().toString().trim();
                        String phone = et_personal_phone_signup;
                        et_personal_email_signup = ((Fragment_Personal) personal_signup).getEmailPersonal_signup().getText().toString().trim();
                        String email = et_personal_email_signup;
                        et_personal_address_signup = ((Fragment_Personal) personal_signup).getAddressPersonal_signup().getText().toString().trim();
                        String address = et_personal_address_signup;
                        et_personal_password_signup = ((Fragment_Personal) personal_signup).getPasswordPersonal_signup().getText().toString().trim();
                        final String password = et_personal_password_signup;
                        et_personal_confirmpassword_signup = ((Fragment_Personal) personal_signup).getConfirm_PasswordPersonal_signup().getText().toString().trim();
                        personal_bitmap = ((Fragment_Personal) personal_signup).getpicturePersonal_Signup();
                        path = ((Fragment_Personal) personal_signup).getpicturePersonalpath_Signup();

                        SharedPreferences sh1=getSharedPreferences("user", Context.MODE_PRIVATE);
                        device_token=sh1.getString("device_token","");

                        if (valid_personal()) {

                            File file = new File(path);
                            if (path.equals("")) {

                                RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name);
                                RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone);
                                RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email);
                                RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address);
                                RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), password);
                                RequestBody comp_gstin = RequestBody.create(MediaType.parse("text/plain"), "0");
                                RequestBody image = RequestBody.create(MediaType.parse("text/plain"), "");
                                RequestBody device_token1 = RequestBody.create(MediaType.parse("text/plain"), device_token);

                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                                    try {
                                        MainApplication.apiManager.createUserWithoutPath(name1, phone1, email1, address1, password1, comp_gstin,device_token1, image, new Callback<Register>() {
                                            @Override
                                            public void onResponse(Call<Register> call, Response<Register> response) {

                                                Register responseUser = response.body();
                                                if (response.isSuccessful() && responseUser != null) {
//                                                Toast.makeText(SignupActivity.this,responseUser.getUser().getName()+" "+responseUser.getUser().getEmail(),Toast.LENGTH_SHORT).show();
                                                    sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                    editor = sh.edit();
                                                    editor.putString("_Token", responseUser.getToken());
                                                    editor.putString("Email_id",responseUser.getUser().getEmail());
                                                    editor.putString("Password",password);
                                                    editor.putString("device_token",responseUser.getUser().getDeviceToken());


                                                    editor.putString("profile_image", responseUser.getUser().getImage());
                                                    editor.putString("user_name", responseUser.getUser().getName());
                                                    editor.putString("user_email",responseUser.getUser().getEmail());
                                                    editor.putString("user_phn",responseUser.getUser().getPhone());
                                                    editor.putString("user_address",responseUser.getUser().getAddress());
                                                    editor.commit();

                                                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit2);
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    tv_signup.setVisibility(View.GONE);
                                                    ThreeBounce threeBounce = new ThreeBounce();
                                                    progressBar.setIndeterminateDrawable(threeBounce);

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressBar.setVisibility(View.GONE);
                                                            tv_signup.setVisibility(View.VISIBLE);
                                                            Intent intent_per = new Intent(SignupActivity.this, MainActivity.class);
                                                            startActivity(intent_per);
                                                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                            finish();
                                                        }
                                                    }, 2000);
                                                } else {
                                                    Toasty.error(SignupActivity.this,
                                                            "Email id already exists"
                                                            , Toast.LENGTH_LONG).show();
                                                    Log.v("Error code 400", response.errorBody().toString());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Register> call, Throwable t) {

                                                Toast.makeText(SignupActivity.this,
                                                        "Error is " + t.getMessage()
                                                        , Toast.LENGTH_LONG).show();
                                            }
                                        });

                                    } catch (Exception e) {

                                    }
                                } else {
                                    Toast.makeText(SignupActivity.this, "Please check Internet connection", Toast.LENGTH_LONG).show();

                                }

                            }

                            else {
                                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                                MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                                RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name);
                                RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone);
                                RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email);
                                RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address);
                                RequestBody password1 = RequestBody.create(MediaType.parse("text/plain"), password);
                                RequestBody comp_gstin = RequestBody.create(MediaType.parse("text/plain"), "0");
                                RequestBody device_token1 = RequestBody.create(MediaType.parse("text/plain"), device_token);


                                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                                    try {
                                        MainApplication.apiManager.createUser(name1, phone1, email1, address1, password1, comp_gstin,device_token1, fileToUpload, new Callback<Register>() {
                                            @Override
                                            public void onResponse(Call<Register> call, Response<Register> response) {

                                                Register responseUser = response.body();
                                                if (response.isSuccessful() && responseUser != null) {
//                                            Toast.makeText(SignupActivity.this,responseUser.getUser().getName()+" "+responseUser.getUser().getEmail(),Toast.LENGTH_SHORT).show();
                                                    sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                    editor = sh.edit();
                                                    editor.putString("_Token", responseUser.getToken());
                                                    editor.putString("Email_id",responseUser.getUser().getEmail());
                                                    editor.putString("Password",password);
                                                    editor.putString("device_token",responseUser.getUser().getDeviceToken());

                                                    editor.putString("profile_image", responseUser.getUser().getImage());
                                                    editor.putString("user_name", responseUser.getUser().getName());
                                                    editor.putString("user_email",responseUser.getUser().getEmail());
                                                    editor.putString("user_phn",responseUser.getUser().getPhone());
                                                    editor.putString("user_address",responseUser.getUser().getAddress());
                                                    editor.commit();

                                                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit2);
                                                    progressBar.setVisibility(View.VISIBLE);
                                                    tv_signup.setVisibility(View.GONE);
                                                    ThreeBounce threeBounce = new ThreeBounce();
                                                    progressBar.setIndeterminateDrawable(threeBounce);

                                                    Handler handler = new Handler();
                                                    handler.postDelayed(new Runnable() {
                                                        @Override
                                                        public void run() {
                                                            progressBar.setVisibility(View.GONE);
                                                            tv_signup.setVisibility(View.VISIBLE);
                                                            Intent intent_per = new Intent(SignupActivity.this, MainActivity.class);
                                                            startActivity(intent_per);
                                                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                                            finish();
                                                        }
                                                    }, 2000);
                                                } else {
                                                    Toasty.error(SignupActivity.this,
                                                           "Email id already exists"
                                                            , Toast.LENGTH_LONG).show();
                                                    Log.v("Error code 400", response.errorBody().toString());
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Register> call, Throwable t) {

                                                Toast.makeText(SignupActivity.this,
                                                        "Error is " + t.getMessage()
                                                        , Toast.LENGTH_LONG).show();
                                            }
                                        });
                                    } catch (Exception e) {

                                    }
                                } else {
                                    Toast.makeText(SignupActivity.this, "Please check Internet connection", Toast.LENGTH_LONG).show();

                                }
                            }
                        }
                    }
                }
                catch (Exception e){

                }
                break;
//            case R.id.sign_in_button:
//                SignIn();
//                break;


        }
    }

    private void SignIn() {
        Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }

    private boolean valid_personal() {
        boolean isvalid=true;

        if(et_personal_name_signup.equals("") ){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Please Enter proper name", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(et_personal_phone_signup.equals("")||et_personal_phone_signup.length()<10 ){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Please Enter phone number correctly", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(et_personal_email_signup.equals("")){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Please Enter email id", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(et_personal_address_signup.equals("") ){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Please Enter proper address", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(et_personal_password_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please Enter password", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(et_personal_confirmpassword_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter confirm password",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please confirm password", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }

        else if(!et_personal_password_signup.equals(et_personal_confirmpassword_signup)){
            isvalid=false;
//            Toast.makeText(this,"Password mismatch",Toast.LENGTH_LONG).show();

            Toasty.error(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }

        else if(et_personal_password_signup.length()<6){
            isvalid=false;
//            Toast.makeText(this,"Password must contain minimum 6 characters",Toast.LENGTH_LONG).show();
            Toasty.info(getApplicationContext(), "Password must contain minimum 6 characters", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(!et_personal_email_signup.matches(emailPattern)){
            isvalid=false;
//            Toast.makeText(this,"Invalid email id",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }

        return isvalid;
    }

    private boolean valid_business() {
        boolean isvalid=true;

        if(business_name_signup.equals("") ){
            isvalid=false;
//
            Toasty.warning(getApplicationContext(), "Please Enter proper name", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_phone_signup.equals("")|| business_phone_signup.length()<10){
            isvalid=false;
//            Toast.makeText(this,"Enter phone number correctly",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please Enter phone number correctly", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_email_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter email",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please Enter email id", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_address_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter address",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please Enter proper address", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_password_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter password",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please Enter password", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_password_signup.length()<6){
            isvalid=false;
//            Toast.makeText(this,"Password must contain minimum 6 characters",Toast.LENGTH_LONG).show();
            Toasty.info(getApplicationContext(), "Password must contain minimum 6 characters", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_confirmpassword_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter confirm password",Toast.LENGTH_LONG).show();
            Toasty.warning(getApplicationContext(), "Please confirm password", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(business_gstn_signup.equals("")){
            isvalid=false;
//            Toast.makeText(this,"Enter GSTN",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please Enter GSTIN", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }

        else if(!business_password_signup.equals(business_confirmpassword_signup)){
            isvalid=false;

            Toasty.error(getApplicationContext(), "Password mismatch", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        else if(!business_email_signup.matches(emailPattern)){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }

        return isvalid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        SignupActivity.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
        finish();
    }

    public void changefragment( View view){
        Fragment fragment;

        if(view==findViewById(R.id.tv_personal)){
            fragment=new Fragment_Personal();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.fragment_place,personal_signup);
            ft.commit();

        }
        if(view==findViewById(R.id.tv_business)){
            fragment=new Fragment_Business();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.fragment_place,business_signup);
            ft.commit();

        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    private void SignOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
//                updateUI(false);
            }
        });

    }

    private void handleResult(GoogleSignInResult result){

        if (result.isSuccess()){
//            gotoProfile();
            GoogleSignInAccount account= result.getSignInAccount();
            String Name= account.getDisplayName();
            String Email= account.getEmail();
            String id=account.getId().toString();
            String id_token= account.getIdToken();
            Uri img_url = account.getPhotoUrl();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("logged","logged");
            editor.putString("Name",Name);
            editor.putString("Email",Email);
            editor.putString("id",id);
            editor.putString("id_token",id_token);
            editor.putString("device_token",device_token);
            editor.commit();
//
//            updateUI(true);
            startActivity(new Intent(SignupActivity.this,MainActivity.class));
            finish();


        }
        else {
            String email=null;
            String password="";

            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SignupActivity.this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("Email_id",email);
            editor.putString("Password",password);
            editor.putString("device_token",device_token);
            editor.commit();
            startActivity(new Intent(SignupActivity.this,MainActivity.class));
            finish();

        }

    }

    private void gotoProfile() {

            Intent intent=new Intent(SignupActivity.this,MainActivity.class);
            startActivity(intent);
        }


    //    private void updateUI(boolean isLogin){
//
//        if (isLogin){
////            sendUserToGoogleMapActivity();
//        }
//        else {
//
//        }
//
//    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager=CallbackManager.Factory.create();
        LoginButton loginButton=findViewById(R.id.iv_facebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog=new ProgressDialog(SignupActivity.this);
                progressDialog.setMessage("Retrieving data...");
                progressDialog.show();
                String accesstoken=loginResult.getAccessToken().getToken();
                GraphRequest request= (GraphRequest) GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();
//                        Bundle facebookdata=getFacebookData(object);

                    }
                });
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });



        if (requestCode==REQ_CODE){
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
}
