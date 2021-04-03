package com.openwebsolutions.propertybricks.ForgetPassword_Page;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.Model.Forget_Password.ForgetPassword;
import com.openwebsolutions.propertybricks.R;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgetPasswordActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_forget_back;
    EditText et_email_check;
    TextView tv_continue;
    RelativeLayout rel_forgot_password;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email=null;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        init();

    }

    private void init() {
        iv_forget_back=findViewById(R.id.iv_forget_back);
        et_email_check=findViewById(R.id.et_email_check);
        tv_continue=findViewById(R.id.tv_continue);
        rel_forgot_password=findViewById(R.id.rel_forgot_password);
        iv_forget_back.setOnClickListener(this);
        tv_continue.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.iv_forget_back:
                super.onBackPressed();
                ForgetPasswordActivity.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                break;

            case R.id.tv_continue:
                email=et_email_check.getText().toString();
                if(valid()){

                    progressBar= (ProgressBar) findViewById(R.id.spin_kit2_forget);
                    progressBar.setVisibility(View.VISIBLE);
                    ThreeBounce threeBounce = new ThreeBounce();
                    progressBar.setIndeterminateDrawable(threeBounce);

                    getForget_password(email);
                    LayoutInflater inflater = getLayoutInflater();
                    View layout = inflater.inflate(R.layout.custom_toast,
                            (ViewGroup) findViewById(R.id.custom_toast_container));

                    TextView text = (TextView) layout.findViewById(R.id.text);
                    text.setText("Link send Successfully.. ");

                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setView(layout);
                    toast.show();
//                    Toast.makeText(this,"Link send to your valid email address",Toast.LENGTH_SHORT).show();
                    rel_forgot_password.setVisibility(View.INVISIBLE);

                }
                break;
        }
    }

    private void getForget_password(String email) {

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            try{

                MainApplication.apiManager.getforget_password(email, new Callback<ForgetPassword>() {
                    @Override
                    public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {

                        ForgetPassword responseUser = response.body();
//                        response.isSuccessful();
                        if (response.isSuccessful() && responseUser != null) {

                            tv_continue.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                            try {


                                if (responseUser.getSuccess().equals(false)) {

                                    Toasty.error(ForgetPasswordActivity.this, "" + responseUser.getMessage(), Toast.LENGTH_SHORT, true).show();

                                } else {
                                    Toast.makeText(ForgetPasswordActivity.this, "" + responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                et_email_check.setText("");
                            }
                            catch (Exception e)
                            {

                            }
                        }
                        else{
                                Toasty.error(ForgetPasswordActivity.this, "" + responseUser.getMessage(), Toast.LENGTH_SHORT, true).show();
                            }
                        }


                    @Override
                    public void onFailure(Call<ForgetPassword> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT, true).show();
                    }
                });

            }
            catch (Exception e){

            }
        }
        else {
            Toasty.error(ForgetPasswordActivity.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean valid() {
        boolean isvalid=true;

        if(email.equalsIgnoreCase("")){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Please Enter email address", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }

        else if(!email.matches(emailPattern) ){
            isvalid=false;
            Toasty.error(getApplicationContext(), "Please Enter valid email address", Toast.LENGTH_SHORT, true).show();
            return isvalid;
        }
        return isvalid;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ForgetPasswordActivity.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }
}
