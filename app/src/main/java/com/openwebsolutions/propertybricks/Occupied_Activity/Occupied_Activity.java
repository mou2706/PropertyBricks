package com.openwebsolutions.propertybricks.Occupied_Activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.ListOfProperty_By_Location.ListofPropertyByLocation;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty.DeleteTenantInProperty;
import com.openwebsolutions.propertybricks.Model.Tenants_Details.Data;
import com.openwebsolutions.propertybricks.Model.Tenants_Details.TenantDetailsPerProductId;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Occupied_Activity extends AppCompatActivity implements View.OnClickListener{
    ImageView img_dress;
    TextView garment_title,phone1,email1,address1,entrydate1,payment_cycle1,gstin_occupied,billing_address_occupied;

    ImageView iv_occupied_back;
    TextView tv_reset,tv_invoice;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;

    int tenant_id=0;
    int property_id=0;
    String Gstin="";
    String billing_address="";

    ImageView menu_button_occupied;
    ProgressDialog pd;


    ArrayList<Data> arrayList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_occupied_);

        init();

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            getTenantUser();
        }
        else{
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getTenantUser() {
        token=sh.getString("_Token","");
        MainApplication.apiManager.getTenantUser_id(AppData.tenant_id,"Bearer"+" "+token, new Callback<TenantDetailsPerProductId>() {
            @Override
            public void onResponse(Call<TenantDetailsPerProductId> call, Response<TenantDetailsPerProductId> response) {

                TenantDetailsPerProductId responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {
                        Data data=responseUser.getData();
                        garment_title.setText(data.getTenantOwnerName());
                        phone1.setText(data.getTenantOwnerPhone());
                        email1.setText(data.getTenantOwnerEmail());
                        address1.setText(data.getTenantOwnerAddress());
                        entrydate1.setText(data.getTenantOwnerEntrydate());
                        payment_cycle1.setText(String.valueOf(data.getTenantOwnerPaymentcycle()));
                        Picasso.get().load(AppData.image_url_tenant + data.getImage()).into(img_dress);

                        tenant_id=data.getId();
                        property_id=data.getPropertyId();
                        if(data.getCompGstin()!=null && data.getBillingAddress()!=null){
                            Gstin=data.getCompGstin();
                            billing_address=data.getBillingAddress();
                            gstin_occupied.setVisibility(View.VISIBLE);
                            gstin_occupied.setText(data.getCompGstin());
                            billing_address_occupied.setVisibility(View.VISIBLE);
                            billing_address_occupied.setText(data.getBillingAddress());
                        }
                    }
                    catch (Exception e){

                        Log.d("Error","eroor");
                    }

                } else {
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(Occupied_Activity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(Occupied_Activity.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(Occupied_Activity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }

//                    Toast.makeText(Occupied_Activity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<TenantDetailsPerProductId> call, Throwable t) {
                Toast.makeText(Occupied_Activity.this,"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }


    private void init() {
        pd = new ProgressDialog(Occupied_Activity.this,R.style.AppCompatAlertDialogStyle);

        menu_button_occupied=findViewById(R.id.menu_button_occupied);
        menu_button_occupied.setOnClickListener(this);
        iv_occupied_back=findViewById(R.id.iv_occupied_back);
        iv_occupied_back.setOnClickListener(this);
        tv_invoice=findViewById(R.id.tv_invoice);
        tv_reset=findViewById(R.id.tv_reset);

        img_dress=findViewById(R.id.img_dress_occupied);
        garment_title=findViewById(R.id.garment_title);
        phone1=findViewById(R.id.phone1);
        email1=findViewById(R.id.email1);
        address1=findViewById(R.id.address1);
        entrydate1=findViewById(R.id.entrydate1);
        payment_cycle1=findViewById(R.id.payment_cycle1);

        billing_address_occupied=findViewById(R.id.billing_address_occupied);
        gstin_occupied=findViewById(R.id.gstin_occupied);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
        String device_token=sh.getString("device_token","");

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            LoginCheck loginCheck = new LoginCheck(this);
            loginCheck.getLoginCheck(email, password,device_token);
        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }

        tv_reset.setOnClickListener(this);
        tv_invoice.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Occupied_Activity.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_occupied_back:
                super.onBackPressed();
                Occupied_Activity.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
                break;
            case R.id.tv_reset:

                Dialog_confirmation();
                break;

            case R.id.tv_invoice:

                Toast.makeText(Occupied_Activity.this,"invoiced successfully",Toast.LENGTH_SHORT).show();
                break;

            case R.id.menu_button_occupied:
                getMenuOption_tenant();
                break;

        }
    }

    private void getMenuOption_tenant() {
        PopupMenu popup1 = new PopupMenu(Occupied_Activity.this, menu_button_occupied);
        popup1.inflate(R.menu.menu_show_tenant);
        popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.edit_tenant:
                        //handle menu3 click
                        pd.setMessage("loading...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Intent intent=new Intent(Occupied_Activity.this, TenantEdit.class);
                                intent.putExtra("tenant_id",String.valueOf(tenant_id));
                                intent.putExtra("Gstin",Gstin);
                                intent.putExtra("Billing_address",billing_address);
                                intent.putExtra("PropertyID",String.valueOf(property_id));

                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        },1000);
                        break;

                }
                return false;
            }
        });
        popup1.show();
    }

    private void Dialog_confirmation() {
        final Dialog dialog=new Dialog(Occupied_Activity.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Reset ?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    delete_tenant();
                }
            }

        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    private void delete_tenant() {
        token=sh.getString("_Token","");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "");
        MainApplication.apiManager.deletetenantByIdinProperty(AppData.tenant_id,"Bearer"+" "+token,name, new Callback<DeleteTenantInProperty>() {
            @Override
            public void onResponse(Call<DeleteTenantInProperty> call, Response<DeleteTenantInProperty> response) {

                DeleteTenantInProperty responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

                    final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3_reset);
                    progressBar.setVisibility(View.VISIBLE);
                    tv_reset.setText("");
                    CubeGrid cubeGrid = new CubeGrid();
                    progressBar.setIndeterminateDrawable(cubeGrid);
//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                Toasty.success(getApplicationContext(),"Successfully Reset",Toasty.LENGTH_SHORT).show();
                                Intent in =new Intent(Occupied_Activity.this, MainActivity.class);
                                startActivity(in);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();

                            }
                        }, 1000);



                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(Occupied_Activity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(Occupied_Activity.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(Occupied_Activity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }


                }
            }
            @Override
            public void onFailure(Call<DeleteTenantInProperty> call, Throwable t) {
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

}
