package com.openwebsolutions.propertybricks.Vacant_Activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.AddTenantModel;
import com.openwebsolutions.propertybricks.Model.ModelDemo_tenant.ModelPersonal;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;
import com.openwebsolutions.propertybricks.fragment.Occupied_Fragment.Business;
import com.openwebsolutions.propertybricks.fragment.Occupied_Fragment.Personal;

import java.io.File;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Vacant_Activity extends AppCompatActivity implements View.OnClickListener{
    TextView tv_personal1,tv_business1;
    Fragment personal,business;
    int flag=1;

    String value="";

    ImageView iv_vacant_back;
    String path="";

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;

    Bitmap personal_image;
    String et_personal_name,et_personal_phone,et_personal_email,et_personal_address,personalEntryDate,personal_payment_cycle;
    String et_business_name,et_business_phone,et_business_email,et_business_address,businessEntryDate,business_payment_cycle,business_billing_address,business_gstn;
    Bitmap business_image;
    TextView tv_personal_submit;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    ModelPersonal modelPersonal=new ModelPersonal();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacant_);

        init();

            try {
                personal = new Personal();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.fram_layout, personal);
                ft.commit();
                business = new Business();
            } catch (Exception e) {

            }

    }


    private void init() {

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            value = extras.getString("flagg_add");
//        }

        iv_vacant_back=findViewById(R.id.iv_vacant_back);
        iv_vacant_back.setOnClickListener(this);
        tv_personal1=findViewById(R.id.tv_personal1);
        tv_business1=findViewById(R.id.tv_business1);

        tv_personal1.setOnClickListener(this);
        tv_business1.setOnClickListener(this);

        tv_personal_submit=findViewById(R.id.tv_personal_submit);
        tv_personal_submit.setOnClickListener(this);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
        String device_token=sh.getString("device_token","");
//        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            LoginCheck loginCheck = new LoginCheck(this);
            loginCheck.getLoginCheck(email, password,device_token);
        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Vacant_Activity.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
        finish();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.iv_vacant_back:
                super.onBackPressed();
                Vacant_Activity.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
                break;
            case R.id.tv_personal1:
                changefragment(v);
                flag=1;
                tv_personal1.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_colorText));
                tv_business1.setBackgroundColor(ContextCompat.getColor(this, R.color.white));

                tv_business1.setTextColor(ContextCompat.getColor(this,R.color.dark_colorText));
                tv_personal1.setTextColor(ContextCompat.getColor(this,R.color.white));
                break;

            case R.id.tv_business1:
                changefragment(v);
                flag=2;
                tv_business1.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_colorText));
                tv_business1.setTextColor(ContextCompat.getColor(this,R.color.white));
                tv_personal1.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                tv_personal1.setTextColor(ContextCompat.getColor(this,R.color.dark_colorText));
                break;

            case R.id.tv_personal_submit:

                if(flag==2) {

                    et_business_name = ((Business) business).getTextBusinessName().getText().toString().trim();
                    et_business_phone = ((Business) business).getTextBusinessPhone().getText().toString();
                    et_business_email = ((Business) business).getTextBusinessEmail().getText().toString();
                    et_business_address = ((Business) business).getTextBusinessAddress().getText().toString().trim();
                    businessEntryDate = ((Business) business).getTextBusinessEntryDate().getText().toString();
                    business_payment_cycle = ((Business) business).getTextBusinessPaymentCycle();
                    business_image = ((Business) business).getTextBusinessPicture();
                    business_billing_address=((Business)business).getTextBusinessBillingAddress().getText().toString();
                    business_gstn=((Business)business).getTextBusinessGstn().getText().toString();
                    path=((Business)business).getBusinesspicturepath_Signup();

                    if(valid_bus()) {
                        AppData.submit_signal_vacant=1;
                        DialogBox();
                    }
                }
                else {
                    et_personal_name=((Personal) personal).getTextPersonalName().getText().toString().trim();
                    et_personal_phone=((Personal) personal).getTextPersonalPhone().getText().toString();
                    et_personal_email=((Personal) personal).getTextPersonalEmail().getText().toString();
                    et_personal_address=((Personal) personal).getTextPersonalAddress().getText().toString().trim();
                    personalEntryDate=((Personal)personal).getTextPersonalEntryDate().getText().toString();
                    personal_payment_cycle=((Personal)personal).getTextPersonalpaymentCycle();
                    personal_image=((Personal)personal).getPersonalpicture();
                    path=((Personal)personal).getPersonalpicturepath_Signup();

                    if(valid_per()) {
                        AppData.submit_signal_vacant=2;
                        DialogBox();
                    }
                }
                break;
        }

    }

    private void DialogBox() {
        final Dialog dialog=new Dialog(Vacant_Activity.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView yes1=dialog.findViewById(R.id.yes);
        TextView no1=dialog.findViewById(R.id.no);

        yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(Vacant_Activity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
                if(AppData.submit_signal_vacant==2){


                    File file = new File(path);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                    RequestBody tenantOwner_name = RequestBody.create(MediaType.parse("text/plain"), et_personal_name);
                    RequestBody tenantOwner_phone=RequestBody.create(MediaType.parse("text/plain"), et_personal_phone);
                    RequestBody tenantOwner_email=RequestBody.create(MediaType.parse("text/plain"), et_personal_email);
                    RequestBody tenantOwner_address=RequestBody.create(MediaType.parse("text/plain"), et_personal_address);
                    RequestBody tenantOwner_entrydate=RequestBody.create(MediaType.parse("text/plain"), personalEntryDate);
                    RequestBody tenantOwner_paymentcycle=RequestBody.create(MediaType.parse("text/plain"),personal_payment_cycle);
                    RequestBody billing_address=RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody comp_Gstin=RequestBody.create(MediaType.parse("text/plain"), "");
                    RequestBody property_id=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AppData.property_id));

                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                        token=sh.getString("_Token","");
                        MainApplication.apiManager.addtenantUser("Bearer"+" "+token,image, tenantOwner_name,tenantOwner_phone,tenantOwner_email,tenantOwner_address,tenantOwner_entrydate,tenantOwner_paymentcycle,billing_address,comp_Gstin,property_id, new Callback<AddTenantModel>() {
                            @Override
                            public void onResponse(Call<AddTenantModel> call, Response<AddTenantModel> response) {

                                AddTenantModel responseUser = response.body();
                                if (response.isSuccessful() && responseUser != null) {
                                    final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit2_jj);
                                    progressBar.setVisibility(View.VISIBLE);
                                    tv_personal_submit.setText("");
                                    CubeGrid cubeGrid = new CubeGrid();
                                    progressBar.setIndeterminateDrawable(cubeGrid);

                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            Intent in = new Intent(Vacant_Activity.this, MainActivity.class);
                                            in.putExtra("tenant_update","update");
                                            startActivity(in);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        }
                                    },1000);
                                } else {

                                    switch (response.code()) {
                                        case 401:
                                            Toast.makeText(Vacant_Activity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                            Intent in =new Intent(Vacant_Activity.this, SigninActivity.class);
                                            startActivity(in);
                                            break;
                                        default:
                                            Toasty.error(Vacant_Activity.this,
                                                    ""+responseUser.getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                            break;
                                    }

                                }
                            }
                            @Override
                            public void onFailure(Call<AddTenantModel> call, Throwable t) {
                                Toasty.error(Vacant_Activity.this,
                                        "Error is " + t.getMessage()
                                        , Toast.LENGTH_LONG).show();
                                Log.d("Error is",t.getMessage());
                            }
                        });

                    }

                }

                if(AppData.submit_signal_vacant==1){


                    File file = new File(path);
                    RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                    MultipartBody.Part image = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
                    RequestBody tenantOwner_name = RequestBody.create(MediaType.parse("text/plain"), et_business_name);
                    RequestBody tenantOwner_phone=RequestBody.create(MediaType.parse("text/plain"), et_business_phone);
                    RequestBody tenantOwner_email=RequestBody.create(MediaType.parse("text/plain"), et_business_email);
                    RequestBody tenantOwner_address=RequestBody.create(MediaType.parse("text/plain"), et_business_address);
                    RequestBody tenantOwner_entrydate=RequestBody.create(MediaType.parse("text/plain"), businessEntryDate);
                    RequestBody tenantOwner_paymentcycle=RequestBody.create(MediaType.parse("text/plain"),business_payment_cycle);
                    RequestBody billing_address=RequestBody.create(MediaType.parse("text/plain"), business_billing_address);
                    RequestBody comp_Gstin=RequestBody.create(MediaType.parse("text/plain"), business_gstn);
                    RequestBody property_id=RequestBody.create(MediaType.parse("text/plain"), String.valueOf(AppData.property_id));
                    ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                        token=sh.getString("_Token","");
                        MainApplication.apiManager.addtenantUser("Bearer"+" "+token,image, tenantOwner_name,tenantOwner_phone,tenantOwner_email,tenantOwner_address,tenantOwner_entrydate,tenantOwner_paymentcycle,billing_address,comp_Gstin,property_id, new Callback<AddTenantModel>() {
                            @Override
                            public void onResponse(Call<AddTenantModel> call, Response<AddTenantModel> response) {

                                AddTenantModel responseUser = response.body();
                                if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(Vacant_Activity.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                                    final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit2_jj);
                                    progressBar.setVisibility(View.VISIBLE);
                                    tv_personal_submit.setText("");
                                    CubeGrid cubeGrid = new CubeGrid();
                                    progressBar.setIndeterminateDrawable(cubeGrid);

                                    Handler handler=new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            progressBar.setVisibility(View.GONE);
                                            Intent in = new Intent(Vacant_Activity.this, MainActivity.class);
                                            in.putExtra("tenant_update","update");
                                            startActivity(in);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        }
                                    },1000);
                                } else {
                                    switch (response.code()) {
                                        case 401:
                                            Toast.makeText(Vacant_Activity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                            Intent in =new Intent(Vacant_Activity.this, SigninActivity.class);
                                            startActivity(in);
                                            break;
                                        default:
                                            Toasty.error(Vacant_Activity.this,
                                                    ""+responseUser.getMessage()
                                                    , Toast.LENGTH_LONG).show();
                                            break;
                                    }

                                }
                            }
                            @Override
                            public void onFailure(Call<AddTenantModel> call, Throwable t) {
                                Toasty.error(Vacant_Activity.this,
                                        "Error is " + t.getMessage()
                                        , Toast.LENGTH_LONG).show();
                                Log.d("Error is",t.getMessage());
                            }
                        });

                    }
                }

                }
        });
        no1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();
    }

    private boolean valid_per() {
        boolean isvalid=true;


        if(et_personal_name.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(et_personal_phone.equals("")|| et_personal_phone.length()<10){
            isvalid=false;
            Toast.makeText(this,"Enter phone number correctly",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(et_personal_email.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(et_personal_address.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter address",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(personalEntryDate.equals("")){
            isvalid=false;
            Toast.makeText(this,"Select entry date",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(personal_payment_cycle.equals("Select Month(s)")){
            isvalid=false;
            Toast.makeText(this,"Choose payment cycle",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(personal_image==null){
            isvalid=false;
            Toast.makeText(this,"Upload image",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        else if(!et_personal_email.matches(emailPattern)){
            isvalid=false;
            Toast.makeText(this,"Enter valid email",Toast.LENGTH_LONG).show();
            return isvalid;
        }

        return isvalid;
    }

    private boolean valid_bus() {

        boolean isvalid=true;
        if(et_business_name.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter name",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(et_business_phone.equals("")|| et_business_phone.length()<10){
            isvalid=false;
            Toast.makeText(this,"Enter phone number correctly",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(et_business_email.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter email",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(et_business_address.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter address",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(businessEntryDate.equals("")){
            isvalid=false;
            Toast.makeText(this,"Select entry date",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(business_payment_cycle.equals("Select Month(s)")){
            isvalid=false;
            Toast.makeText(this,"Choose payment cycle",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(path.equals("")){
            isvalid=false;
            Toast.makeText(this,"Upload image",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(business_billing_address.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter billing address",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
        if(business_gstn.equals("")){
            isvalid=false;
            Toast.makeText(this,"Enter GSTIN",Toast.LENGTH_SHORT).show();
            return isvalid;
        }
         if(!et_business_email.matches(emailPattern)){
            isvalid=false;
            Toast.makeText(this,"Enter valid email",Toast.LENGTH_LONG).show();
            return isvalid;
        }

        return isvalid;
    }

    private void changefragment(View view) {
        Fragment fragment;

        if(view==findViewById(R.id.tv_personal1)){
            fragment=new Personal();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.fram_layout,personal);
            ft.commit();

        }
        if(view==findViewById(R.id.tv_business1)){
            fragment=new Business();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.fram_layout,business);
            ft.commit();

        }
    }
}
