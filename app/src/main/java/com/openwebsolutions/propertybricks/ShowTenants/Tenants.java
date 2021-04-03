package com.openwebsolutions.propertybricks.ShowTenants;

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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Adapter.RecyclerAdapter_Tenant;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty.DeleteTenantInProperty;
import com.openwebsolutions.propertybricks.Model.GetTenantDetails.Datum_Tenant;
import com.openwebsolutions.propertybricks.Model.GetTenantDetails.GetTenantModel;
import com.openwebsolutions.propertybricks.Occupied_Activity.Occupied_Activity;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SearchingActivity.SeachingProperty;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;


import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Tenants extends AppCompatActivity implements View.OnClickListener {

    ArrayList<Datum_Tenant>arrayList=new ArrayList<>();

    RecyclerView tenants_recycler;
    RecyclerAdapter_Tenant adapter_tenant;
    LinearLayoutManager layoutManager;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;
    ProgressDialog pd;
    ImageView iv_showtenant_back;

    RelativeLayout rel_no_tenant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenants);

        tenants_recycler=findViewById(R.id.tenants_recycler);
        init();
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            getTenant();
        }
    }

     private void getTenant() {
            token=sh.getString("_Token","");
            MainApplication.apiManager.gettenantyUser("Bearer"+" "+token, new Callback<GetTenantModel>() {
                @Override
                public void onResponse(Call<GetTenantModel> call, Response<GetTenantModel> response) {

                    GetTenantModel responseUser = response.body();
                    if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(Tenants.this,"Successfull",Toast.LENGTH_LONG).show();
                        try {
                            arrayList = responseUser.getData();
                            if(arrayList.size()!=0) {
                                pd.dismiss();
                                adapter_tenant = new RecyclerAdapter_Tenant(arrayList, Tenants.this);
                                layoutManager = new LinearLayoutManager(Tenants.this, LinearLayoutManager.VERTICAL, false);
                                tenants_recycler.setLayoutManager(layoutManager);
                                tenants_recycler.setItemAnimator(new DefaultItemAnimator());
                                tenants_recycler.setAdapter(adapter_tenant);

                                adapter_tenant.SetOnItemClickListener(new RecyclerAdapter_Tenant.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (view.getId() == R.id.tv_view_details_tenants){

                                            AppData.tenant_id=Integer.valueOf(arrayList.get(position).getId());
                                            Intent intent = new Intent(Tenants.this, Occupied_Activity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                                        }
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {

                                        DialogBox(position);
                                        AppData.tenant_id=arrayList.get(position).getId();

                                    }
                                });
                            }
                            else {
                                rel_no_tenant.setVisibility(View.VISIBLE);
                                tenants_recycler.setVisibility(View.GONE);
                            }
                        }
                        catch (Exception e){

                        }


                    } else {

                        switch (response.code()) {
                            case 401:
                                Toast.makeText(Tenants.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                Intent in =new Intent(Tenants.this, SigninActivity.class);
                                startActivity(in);
                                break;
                            default:
                                Toasty.error(Tenants.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                                break;
                        }


                    }
                }
                @Override
                public void onFailure(Call<GetTenantModel> call, Throwable t) {
                    Toast.makeText(Tenants.this,"Internal Error",Toast.LENGTH_LONG).show();

                }
            });

        }

    private void DialogBox(final int pos) {
        final Dialog dialog=new Dialog(Tenants.this);
        dialog.setContentView(R.layout.dialog_edit_delete);
        RelativeLayout rel_edit=dialog.findViewById(R.id.rel_edit);
        RelativeLayout rel_remove=dialog.findViewById(R.id.rel_remove);

        rel_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                pd.setMessage("loading...");
                pd.setCancelable(false);
                pd.show();

                Handler handler=new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                        dialog.dismiss();

                        Intent intent=new Intent(Tenants.this, TenantEdit.class);
                        intent.putExtra("tenant_id",String.valueOf(arrayList.get(pos).getId()));
                        intent.putExtra("Gstin",arrayList.get(pos).getCompGstin());
                        intent.putExtra("Billing_address",arrayList.get(pos).getBillingAddress());
                        intent.putExtra("PropertyID",String.valueOf(arrayList.get(pos).getPropertyId()));

                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

                    }
                },2000);


            }
        });
        rel_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                Dialog_confirmation(pos);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
    }

    private void Dialog_confirmation(final int pos) {
        final Dialog dialog=new Dialog(Tenants.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Delete ?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    dialog.dismiss();
                    delete_tenant(pos);
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

    private void delete_tenant(final int pos) {
        token=sh.getString("_Token","");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "");

        MainApplication.apiManager.deletetenantByIdinProperty(AppData.tenant_id,"Bearer"+" "+token,name, new Callback<DeleteTenantInProperty>() {
            @Override
            public void onResponse(Call<DeleteTenantInProperty> call, Response<DeleteTenantInProperty> response) {

                DeleteTenantInProperty responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

                    try {
                        pd.setMessage("deleting...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                pd.dismiss();
                                Toasty.success(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT, true).show();

                                arrayList.remove(pos);
                                adapter_tenant.notifyDataSetChanged();

                                if(arrayList.size()==0){
                                    rel_no_tenant.setVisibility(View.VISIBLE);
                                    tenants_recycler.setVisibility(View.GONE);
                                }

                            }
                        }, 1000);
                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(Tenants.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(Tenants.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(Tenants.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

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


    private void init() {
        rel_no_tenant=findViewById(R.id.rel_no_tenant);
        iv_showtenant_back=findViewById(R.id.iv_showtenant_back);
        iv_showtenant_back.setOnClickListener(Tenants.this);
        pd = new ProgressDialog(Tenants.this,R.style.AppCompatAlertDialogStyle);
        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
//        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            String device_token=sh.getString("device_token","");

            LoginCheck loginCheck = new LoginCheck(this);
            loginCheck.getLoginCheck(email, password,device_token);
        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
         Intent intent=new Intent(this,MainActivity.class);
         startActivity(intent);
         overridePendingTransition(R.anim.left_to_right,
                 R.anim.right_to_left);
         finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_showtenant_back:
                Intent intent=new Intent(this,MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
                break;
        }
    }
}
