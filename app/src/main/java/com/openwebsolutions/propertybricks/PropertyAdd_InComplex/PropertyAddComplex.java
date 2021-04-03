package com.openwebsolutions.propertybricks.PropertyAdd_InComplex;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Adapter.RecyclerPropertyInComplex;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.ListOfProperty_By_Location.ListofPropertyByLocation;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.RemovePropertyUnderComplex.RemoveProperty;
import com.openwebsolutions.propertybricks.Model.RemovePropertyUnderComplex.RemovePropertyUnderComplex;
import com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex.DatumViewProperty;
import com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex.ViewPropertyUnderComplex;
import com.openwebsolutions.propertybricks.Property_Details_Page.PropertyDetailsActivity;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyAddComplex extends AppCompatActivity implements View.OnClickListener {
    int property_id=0;
    ProgressDialog pd;
    int complex_id=0;
    RelativeLayout rel_propertylist11;
    ImageView iv_propertyaddcomplex_back;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;

    TextView text_propertylist1;

    RecyclerView recycler_list1;
    LinearLayoutManager layoutManager;
    ArrayList<DatumViewProperty> arrayList=new ArrayList<>();
    RecyclerPropertyInComplex adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_add_complex);

        init();
        pd = new ProgressDialog(PropertyAddComplex.this,R.style.AppCompatAlertDialogStyle);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            pd.setMessage("loading...");
            pd.show();
            getProperty_inComplex();
        }
        else{
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getProperty_inComplex() {
        token=sh.getString("_Token","");

        MainApplication.apiManager.getPropertylist_IN_Complex(AppData.complex_id,"Bearer"+" "+token, new Callback<ViewPropertyUnderComplex>() {
            @Override
            public void onResponse(Call<ViewPropertyUnderComplex> call, Response<ViewPropertyUnderComplex> response) {

                ViewPropertyUnderComplex responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(PropertyAddComplex.this,"Successfull"+response.message(),Toast.LENGTH_LONG).show();

                    try{

                        arrayList=responseUser.getData();
                        if(arrayList.size()!=0) {
                            pd.dismiss();
                            String a=String.valueOf(arrayList.size());
                            recycler_list1.setVisibility(View.VISIBLE);
                            adapter=new RecyclerPropertyInComplex(arrayList,PropertyAddComplex.this);
                            layoutManager = new LinearLayoutManager(PropertyAddComplex.this, LinearLayoutManager.VERTICAL, false);
                            recycler_list1.setLayoutManager(layoutManager);
                            recycler_list1.setItemAnimator(new DefaultItemAnimator());
                            recycler_list1.setAdapter(adapter);

                            adapter.SetOnItemClickListener(new RecyclerPropertyInComplex.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    if (view.getId() == R.id.deleteproperty){

                                         property_id=arrayList.get(position).getId();
                                         complex_id=arrayList.get(position).getComplexId();
                                         AppData.complex_id=complex_id;
                                        Dialogbox(property_id,position);
                                        getProperty_inComplex();
                                    }
                                    if (view.getId() == R.id.add_property){
                                        complex_id=arrayList.get(position).getComplexId();
                                        int complex_id=arrayList.get(position).getId();
                                        AppData.complex_id=complex_id;
                                        Intent intent = new Intent(getApplicationContext(), ListofPropertyByLocation.class);
                                        Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                        startActivity(intent,bundle);
                                    }

                                    if (view.getId() == R.id.tenant_viewdetails){

                                        Dialog_show_Tenant(position);
                                    }
                                    if (view.getId() == R.id.lin_property_underComplex){

                                        Dialog_show_property(position);
                                    }
                                }
                                @Override
                                public void onItemLongClick(View view, int position) {

                                }
                            });
                        }
                        else{
                            pd.dismiss();
                            rel_propertylist11.setVisibility(View.VISIBLE);
                            recycler_list1.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(PropertyAddComplex.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(PropertyAddComplex.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(PropertyAddComplex.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(PropertyAddComplex.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }
                    pd.dismiss();
                }
            }
            @Override
            public void onFailure(Call<ViewPropertyUnderComplex> call, Throwable t) {
                Toast.makeText(PropertyAddComplex.this,"Internal Error",Toast.LENGTH_LONG).show();

                pd.dismiss();
            }
        });

    }

    private void Dialog_show_property(int pos) {
        final Dialog dialog=new Dialog(this);

        dialog.setContentView(R.layout.dialog_property_show);
        TextView property_name_detail=dialog.findViewById(R.id.property_name_detail);
        TextView property_location_detail=dialog.findViewById(R.id.property_location_detail);
        TextView property_price_detail=dialog.findViewById(R.id.property_price_detail);
        TextView property_des_detail=dialog.findViewById(R.id.property_des_detail);
        CircleImageView iv_pro=dialog.findViewById(R.id.iv_pro);

        ImageView iv_property_show_back=dialog.findViewById(R.id.iv_property_show_back);

        property_name_detail.setText(arrayList.get(pos).getPropertyName());
        property_location_detail.setText(arrayList.get(pos).getPropertyLocation());
        property_price_detail.setText(arrayList.get(pos).getPropertyPrice());
        property_des_detail.setText(arrayList.get(pos).getPropertyDes());
        Picasso.get().load(AppData.image_url + arrayList.get(pos).getImage()).into(iv_pro);

        iv_property_show_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private void Dialog_show_Tenant(int pos) {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.dialog_tenant_show);

        TextView tenant_name_details=dialog.findViewById(R.id.tenant_name_details);
        TextView tenant_location_details=dialog.findViewById(R.id.tenant_location_details);
        TextView tenant_phone_details=dialog.findViewById(R.id.tenant_phone_details);
        TextView tenant_email_details=dialog.findViewById(R.id.tenant_email_details);
        TextView tenant_entrydate_details=dialog.findViewById(R.id.tenant_entrydate_details);
        TextView tenant_paymentcycle_details=dialog.findViewById(R.id.tenant_paymentcycle_details);
        TextView tenant_gstin_details=dialog.findViewById(R.id.tenant_gstin_details);
        TextView tenant_billingadd_details=dialog.findViewById(R.id.tenant_billingadd_details);

        LinearLayout lin_billing=dialog.findViewById(R.id.lin_billing);
        LinearLayout lin_gstin=dialog.findViewById(R.id.lin_gstin);

        CircleImageView iv_pro_tenant=dialog.findViewById(R.id.iv_pro_tenant);
        ImageView iv_tenant_show_back=dialog.findViewById(R.id.iv_tenant_show_back);

        tenant_name_details.setText(arrayList.get(pos).getTenant().getTenantOwnerName());
        tenant_location_details.setText(arrayList.get(pos).getTenant().getTenantOwnerAddress());
        tenant_phone_details.setText(arrayList.get(pos).getTenant().getTenantOwnerPhone());
        tenant_email_details.setText(arrayList.get(pos).getTenant().getTenantOwnerEmail());
        tenant_entrydate_details.setText(arrayList.get(pos).getTenant().getTenantOwnerEntrydate());
        tenant_paymentcycle_details.setText(String.valueOf(arrayList.get(pos).getTenant().getTenantOwnerPaymentcycle()));

        if(arrayList.get(pos).getTenant().getBillingAddress()!=null) {
            lin_billing.setVisibility(View.VISIBLE);
            lin_gstin.setVisibility(View.VISIBLE);

            tenant_billingadd_details.setText(arrayList.get(pos).getTenant().getBillingAddress());
            tenant_gstin_details.setText(arrayList.get(pos).getTenant().getCompGstin());
        }

        Picasso.get().load(AppData.image_url_tenant + arrayList.get(pos).getTenant().getImage()).into(iv_pro_tenant);

        iv_tenant_show_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
            }
        });

        dialog.setCancelable(true);
        dialog.show();
    }

    private void Dialogbox(final int property_id, final int pos) {
        final Dialog dialog=new Dialog(PropertyAddComplex.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Remove from Complex ?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    dialog.dismiss();
                    deleteProperty_fromComplex(property_id,pos);
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

    private void deleteProperty_fromComplex(int property_id, final int pos) {
        token=sh.getString("_Token","");
        try {
            MainApplication.apiManager.deletePropertyfromComplex(property_id, "Bearer" + " " + token, new Callback<RemovePropertyUnderComplex>() {
                @Override
                public void onResponse(Call<RemovePropertyUnderComplex> call, Response<RemovePropertyUnderComplex> response) {

                    RemovePropertyUnderComplex responseUser = response.body();
                    if (response.isSuccessful() && responseUser != null) {
                        RemoveProperty property = responseUser.getData();

                        Toasty.success(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT, true).show();

                        arrayList.remove(pos);
                        adapter.notifyDataSetChanged();

                        if(arrayList.size()==0){
                            recycler_list1.setVisibility(View.GONE);
                            rel_propertylist11.setVisibility(View.VISIBLE);
                            text_propertylist1.setText("There is no added property to show");
                        }

                    } else {
                        switch (response.code()) {
                            case 401:
                                Toast.makeText(PropertyAddComplex.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                Intent in =new Intent(PropertyAddComplex.this, SigninActivity.class);
                                startActivity(in);
                                break;
                            default:
                                Toasty.error(PropertyAddComplex.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();
                                break;
                        }


                    }
                }

                @Override
                public void onFailure(Call<RemovePropertyUnderComplex> call, Throwable t) {
                    Toasty.error(getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT, true).show();

                }
            });
        }
        catch (Exception e){

        }

    }

    private void init() {

        text_propertylist1=findViewById(R.id.text_propertylist1);
        iv_propertyaddcomplex_back=findViewById(R.id.iv_propertyaddcomplex_back);
        iv_propertyaddcomplex_back.setOnClickListener(this);

        Toasty.Config.getInstance()
                .apply(); // required
        rel_propertylist11=findViewById(R.id.rel_propertylist11);
        pd = new ProgressDialog(PropertyAddComplex.this,R.style.AppCompatAlertDialogStyle);


        recycler_list1=findViewById(R.id.recycler_list1);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");

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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_propertyaddcomplex_back:
                Intent in = new Intent(PropertyAddComplex.this, MainActivity.class);
                in.putExtra("flag","012");
                startActivity(in);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
                break;
        }

    }
    @Override
    public void onBackPressed() {
        Intent in = new Intent(PropertyAddComplex.this, MainActivity.class);
        in.putExtra("flag","012");
        startActivity(in);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
