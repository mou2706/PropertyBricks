package com.openwebsolutions.propertybricks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.ForgetPassword_Page.ForgetPasswordActivity;
import com.openwebsolutions.propertybricks.Model.GmailFacebook;
import com.openwebsolutions.propertybricks.Model.Login;
import com.openwebsolutions.propertybricks.Model.Register;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener {
    EditText et_email, et_password;
    Button signup, signin;
    LoginButton loginButton;

   String email,password=null;
   String path="";
    ImageView iv_gmail;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE= 9001;

    int flag1=1;


    boolean doubleBackToExitPressedOnce = false;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    TextView tv_forget_pass;

    String device_token=null;
    CallbackManager callbackManager;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        GoogleSignInOptions signInOptions= new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail().build();

        googleApiClient= new GoogleApiClient.Builder(this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();
        callbackManager=CallbackManager.Factory.create();

        printKeyHash();


        init_signin();
         loginButton=findViewById(R.id.iv_facebook);
        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                progressDialog=new ProgressDialog(SigninActivity.this);
                progressDialog.setMessage("Retrieving data...");
                progressDialog.show();
                String accesstoken=loginResult.getAccessToken().getToken();
                GraphRequest request=GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        progressDialog.dismiss();
                        Log.d("response",response.toString());
                        getData(object);

                    }
                });
                Bundle parameters=new Bundle();
                parameters.putString("fields","id,email");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if(AccessToken.getCurrentAccessToken()!=null)
        {
//            txtEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
        }





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

    private void init_signin() {
        iv_gmail=findViewById(R.id.iv_gmail);
        iv_gmail.setOnClickListener(this);
        et_email = findViewById(R.id.email);
        et_password = findViewById(R.id.password);
        signin = findViewById(R.id.signin);
        signup = findViewById(R.id.signup);
        tv_forget_pass = findViewById(R.id.tv_forget_pass);

        et_email.addTextChangedListener(new MyTextWatcher(et_email));
        et_password.addTextChangedListener(new MyTextWatcher(et_password));

        tv_forget_pass.setOnClickListener(this);
        signin.setOnClickListener(this);
        signup.setOnClickListener(this);
    }

    private void submitForm() {


        if (!validateemail()) {
            return;
        }
        else {
            email=et_email.getText().toString();
        }

        if (!validatePassword()) {
            return;
        }
        else {
            password=et_password.getText().toString();
        }


//        Toast.makeText(getApplicationContext(), "Invalid Emailid & Password!", Toast.LENGTH_SHORT).show();
    }

    private boolean validatePassword() {


        if (et_password.getText().toString().trim().isEmpty()) {
            requestFocus(et_password);
            et_password.setError("Enter valid password");
        }
        return true;
    }


    private boolean validateemail() {
        String emailadd = et_email.getText().toString().trim();

        if (et_email.getText().toString().isEmpty() || !isValidemail(emailadd)) {
            requestFocus(et_email);
            et_email.setError("Enter valid email");

        }


        return true;
    }

    private boolean isValidemail(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }



    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.signin:
                submitForm();
                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

//                    device_token = Settings.Secure.getString(getApplicationContext().getContentResolver(),
//                            Settings.Secure.ANDROID_ID);
                    SharedPreferences sh1=getSharedPreferences("user", Context.MODE_PRIVATE);
                    device_token=sh1.getString("device_token","");
//                    device_token= AppData.push_token;

                    if(!email.equals("") && !password.equals("") && device_token!=null) {
                        MainApplication.apiManager.loginUser(email, password,device_token, new Callback<Login>() {
                            @Override
                            public void onResponse(Call<Login> call, Response<Login> response) {

                                Login responseUser = response.body();
                                if (response.isSuccessful() && responseUser != null) {
//                                    Toast.makeText(SigninActivity.this, responseUser.getToken(), Toast.LENGTH_SHORT).show();

                                    String de=responseUser.getUser().getDeviceToken();
                                    Log.d("hjjh",""+de);
                                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit2);
                                progressBar.setVisibility(View.VISIBLE);
                                signin.setVisibility(View.GONE);
                                ThreeBounce threeBounce = new ThreeBounce();
                                progressBar.setIndeterminateDrawable(threeBounce);

                                    sh=getSharedPreferences("user", Context.MODE_PRIVATE);
                                    editor=sh.edit();
                                    editor.putString("Email_id",email);
                                    editor.putString("Password",password);
                                    editor.putString("_Token",responseUser.getToken());


                                    editor.putString("profile_image", responseUser.getUser().getImage());
                                    editor.putString("user_name", responseUser.getUser().getName());
                                    editor.putString("user_email",responseUser.getUser().getEmail());
                                    editor.putString("user_phn",responseUser.getUser().getPhone());
                                    editor.putString("user_gstin",String.valueOf(responseUser.getUser().getCompGstin()));
                                    editor.putString("user_address",responseUser.getUser().getAddress());

                                    editor.commit();

                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            signin.setVisibility(View.VISIBLE);
                                            Intent intent_per = new Intent(SigninActivity.this, MainActivity.class);
                                            startActivity(intent_per);
                                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                                            finish();
                                        }
                                    }, 2000);
                                } else {
                                    Toast.makeText(SigninActivity.this,
                                            "Invalid emailid and password"
                                            , Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Login> call, Throwable t) {
                                Toast.makeText(SigninActivity.this,
                                        "Error is " + t.getMessage()
                                        , Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                }
                break;

            case R.id.signup:
                Intent intent=new Intent(SigninActivity.this,SignupActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
            case R.id.tv_forget_pass:
                Intent intent1=new Intent(SigninActivity.this, ForgetPasswordActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                break;
            case R.id.iv_gmail:
                SignIn();
                break;

        }
    }
    private void SignIn() {
        Intent intent= Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    private void SignOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });

    }
    private void handleResult(GoogleSignInResult result){

        if (result.isSuccess()){
            GoogleSignInAccount account= result.getSignInAccount();
            String Name= account.getDisplayName();
            final String Email= account.getEmail();
            String id=account.getId();
            String id_token= account.getIdToken();
            Uri img_url = account.getPhotoUrl();

//            GoogleSignInAccount account= result.getSignInAccount();


//            String Name= account.getDisplayName();
//            String Email= account.getEmail();
//            String id=account.getId();
//            String id_token= account.getIdToken();
//            Uri img_url = account.getPhotoUrl();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SigninActivity.this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("logged","logged");
            editor.putString("Name",Name);
            editor.putString("Email",Email);
            editor.putString("id",id);
            editor.putString("id_token",id_token);
            editor.commit();

            updateUI(true);




        }
        else {
            Log.e("FACEBOOK ERROR",result.getStatus().toString());
            updateUI(false);

        }

    }
    private void updateUI(boolean isLogin){

        if (isLogin){
            startActivity(new Intent(SigninActivity.this,MainActivity.class));
            finish();

        }
        else {

        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
//        loginButton.setReadPermissions(Arrays.asList("public_profile","email","user_friends"));
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                progressDialog=new ProgressDialog(SigninActivity.this);
//                progressDialog.setMessage("Retrieving data...");
//                progressDialog.show();
//                String accesstoken=loginResult.getAccessToken().getToken();
//                Log.d("response",loginResult.getAccessToken().getUserId());
//                GraphRequest request= (GraphRequest) GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
//                    @Override
//                    public void onCompleted(JSONObject object, GraphResponse response) {
//                        progressDialog.dismiss();
//                        getData(object);
//
//
//                    }
//
//                });
//                Bundle parameters= new Bundle();
//
////                parameters.putString("fields","id, email, birthday, friends");
//                parameters.putString("fields","id, email, friends");
//                request.setParameters(parameters);
//                request.executeAsync();
//            }
//
//            @Override
//            public void onCancel() {
//
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//
//            }
//        });
//        if (AccessToken.getCurrentAccessToken() != null){
////            txtEmail.setText(AccessToken.getCurrentAccessToken().getUserId());
//        }



        if (requestCode==REQ_CODE){
            GoogleSignInResult result= Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);





        }
    }
    private void getData(JSONObject object) {
        try {
            URL profile_picture = new URL("https://graph.facebook.com/" + object.getString("id") + "/picture?width=250&height=250");
//            Picasso.get().load(profile_picture.toString()).into(imageView);
            String fb_email = object.getString("email");
            String fbk_id = object.getString("id");
//            txtEmail.setText(object.getString("email"));
//            txtBirthday.setText(object.getString("birthday"));
//            txtFriends.setText("Friends" +object.getJSONObject("friends").getJSONObject("summary").getString("total_count"));
//            checkFacebook();

//            sendUserToGoogleMapActivity();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }




    @Override
    public void onBackPressed() {

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            finishAffinity();

            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Double click to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }


    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;

        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.email:
                    validateemail();
                    break;

                case R.id.password:
                    validatePassword();
                    break;
            }
        }

    }
}


