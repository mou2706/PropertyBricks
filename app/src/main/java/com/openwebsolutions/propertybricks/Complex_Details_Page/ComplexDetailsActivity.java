package com.openwebsolutions.propertybricks.Complex_Details_Page;

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
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Adapter.RecyclerPropertyInComplex;
import com.openwebsolutions.propertybricks.AddComplexActivity.AddComplex;
import com.openwebsolutions.propertybricks.AddPropertyActivity.AddProperty;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.ComplexDelete.ComplexDelete;
import com.openwebsolutions.propertybricks.Model.GetComplex_ByID.ComplexEdit;
import com.openwebsolutions.propertybricks.Model.GetComplex_ByID.GetComplexByID;
import com.openwebsolutions.propertybricks.Model.RemovePropertyUnderComplex.RemoveProperty;
import com.openwebsolutions.propertybricks.Model.RemovePropertyUnderComplex.RemovePropertyUnderComplex;
import com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex.DatumViewProperty;
import com.openwebsolutions.propertybricks.Model.ViewPropertyUnderComplex.ViewPropertyUnderComplex;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ComplexDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView complex_image_details,iv_complexdetails_back,menu_button_complex;
    TextView complex_name_details,complex_des_details;
    LinearLayout property_de;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String property_image="";
    String email=null;
    String password=null;

    ProgressDialog pd;

    ArrayList<DatumViewProperty> arrayList=new ArrayList<>();

    RecyclerView recycler_complex_details;
    LinearLayoutManager layoutManager;
    RecyclerPropertyInComplex adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complex_details);

        init();
        getComplex_By_Id();
        getProperty_UnderComplex();
    }

    private void init() {
        property_de=findViewById(R.id.property_de);
        recycler_complex_details=findViewById(R.id.recycler_complex_details);

        pd = new ProgressDialog(ComplexDetailsActivity.this,R.style.AppCompatAlertDialogStyle);

        complex_image_details=findViewById(R.id.complex_image_details);
        complex_name_details=findViewById(R.id.complex_name_details);
        complex_des_details=findViewById(R.id.complex_des_details);
        iv_complexdetails_back=findViewById(R.id.iv_complexdetails_back);
        iv_complexdetails_back.setOnClickListener(this);
        menu_button_complex=findViewById(R.id.menu_button_complex);
        menu_button_complex.setOnClickListener(this);

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

    private void getComplex_By_Id() {
        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            try{
                MainApplication.apiManager.getComplexByIdUser(AppData.complex_id,"Bearer"+" "+token, new Callback<GetComplexByID>() {
                    @Override
                    public void onResponse(Call<GetComplexByID> call, Response<GetComplexByID> response) {

                        GetComplexByID responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {

                            try {
                                ComplexEdit data_complex=responseUser.getData();

                                complex_name_details.setText(data_complex.getComplexName());
                                complex_des_details.setText(data_complex.getComplexDes());
                                Picasso.get().load(AppData.image_url_complex +data_complex.getImage()).resize(600,200).into(complex_image_details);
                            }
                            catch (Exception e){


                            }
                        } else {
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(ComplexDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(ComplexDetailsActivity.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toasty.error(ComplexDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                                    break;
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<GetComplexByID> call, Throwable t) {
                        Toasty.error(ComplexDetailsActivity.this, "Internal Error", Toast.LENGTH_LONG).show();

                    }
                });

            }
            catch (Exception e){

            }
        }
        else {
            Toast.makeText(ComplexDetailsActivity.this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getProperty_UnderComplex() {
        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            try{
                MainApplication.apiManager.getPropertylist_IN_Complex(AppData.complex_id,"Bearer"+" "+token, new Callback<ViewPropertyUnderComplex>() {
                    @Override
                    public void onResponse(Call<ViewPropertyUnderComplex> call, Response<ViewPropertyUnderComplex> response) {

                        ViewPropertyUnderComplex responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {

                            try{
                                arrayList=responseUser.getData();
                                if(arrayList.size()!=0) {

                                    String a=String.valueOf(arrayList.size());
                                    property_de.setVisibility(View.VISIBLE);
                                    recycler_complex_details.setVisibility(View.VISIBLE);
                                    adapter=new RecyclerPropertyInComplex(arrayList,ComplexDetailsActivity.this);
                                    layoutManager = new LinearLayoutManager(ComplexDetailsActivity.this, LinearLayoutManager.VERTICAL, false);
                                    recycler_complex_details.setLayoutManager(layoutManager);
                                    recycler_complex_details.setItemAnimator(new DefaultItemAnimator());
                                    recycler_complex_details.setAdapter(adapter);

                                    adapter.SetOnItemClickListener(new RecyclerPropertyInComplex.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            if (view.getId() == R.id.deleteproperty){

                                                int property_id=arrayList.get(position).getId();
                                                int complex_id=arrayList.get(position).getComplexId();
                                                Dialogbox(property_id,position);
                                            }
                                            if (view.getId() == R.id.lin_property_underComplex){
//                                                int property_id=arrayList.get(position).getId();
//                                                AppData.property_id=property_id;
//                                                Intent intent = new Intent(ComplexDetailsActivity.this, PropertyDetailsActivity.class);
//                                                startActivity(intent);
//                                                overridePendingTransition(R.anim.slide_in,
//                                                        R.anim.slide_out);
                                                Dialog_show_property(position);
                                            }
                                            if (view.getId() == R.id.tenant_viewdetails){


                                                Dialog_show_Tenant(position);

                                            }
                                        }

                                        @Override
                                        public void onItemLongClick(View view, int position) {

                                        }
                                    });

                                }
                                else{

                                }
                            }
                            catch (Exception e){

                            }
                        } else {
//                            Toast.makeText(ComplexDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            pd.dismiss();
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(ComplexDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(ComplexDetailsActivity.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toasty.error(ComplexDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                                    break;
                            }

                        }
                    }
                    @Override
                    public void onFailure(Call<ViewPropertyUnderComplex> call, Throwable t) {
                        Toast.makeText(ComplexDetailsActivity.this,"Internal Error",Toast.LENGTH_LONG).show();

                        pd.dismiss();
                    }
                });

            }
            catch (Exception e){

            }
        }
        else {
            Toast.makeText(ComplexDetailsActivity.this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }

    }

    private void Dialog_show_Tenant(int pos) {
        final Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

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

    private void Dialog_show_property(int pos) {
        final Dialog dialog=new Dialog(this);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        lp.windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setAttributes(lp);

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

    private void Dialogbox(final int property_id, final int pos) {
        final Dialog dialog=new Dialog(ComplexDetailsActivity.this);
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


                        arrayList.remove(pos);
                        adapter.notifyDataSetChanged();

//                   Toast.makeText(PropertyAddComplex.this,"Successfully Deleted",Toast.LENGTH_LONG).show();
                        Toasty.success(getApplicationContext(), "Successfully Deleted", Toast.LENGTH_SHORT, true).show();

//                    getProperty_inComplex();

                    } else {
//                        Toast.makeText(ComplexDetailsActivity.this, "Something wrong" + response.message(), Toast.LENGTH_LONG).show();
                        switch (response.code()) {
                            case 401:
                                Toast.makeText(ComplexDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                Intent in =new Intent(ComplexDetailsActivity.this, SigninActivity.class);
                                startActivity(in);
                                break;
                            default:
                                Toasty.error(ComplexDetailsActivity.this,"Something wrong to Delete"+response.message(),Toast.LENGTH_LONG).show();

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_complexdetails_back:
                Intent in = new Intent(ComplexDetailsActivity.this, MainActivity.class);
                in.putExtra("flag","012");
                startActivity(in);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
                break;
            case R.id.menu_button_complex:
                getmenu_option();
                break;

        }
    }

    private void getmenu_option() {
        PopupMenu popup1 = new PopupMenu(ComplexDetailsActivity.this, menu_button_complex);
        popup1.inflate(R.menu.menu);
        MenuItem item = popup1.getMenu().findItem(R.id.remove);

        if(arrayList.isEmpty()==true){
            item.setVisible(true);
        }
        popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.edit:

                        pd.setMessage("loading...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Intent intent = new Intent(ComplexDetailsActivity.this, AddComplex.class);
                                intent.putExtra("complex_id_edit",String.valueOf(AppData.complex_id));
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in,
                                        R.anim.fade_out);
                                finish();

                            }
                        },1000);
                        break;

                    case R.id.remove:
                        Dialog_confirmation();
                        break;
                }
                return false;
            }
        });
        popup1.show();

    }

    private void Dialog_confirmation() {
        final Dialog dialog=new Dialog(ComplexDetailsActivity.this);
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
                    delete_property();
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

    private void delete_property() {
        token=sh.getString("_Token","");
        MainApplication.apiManager.getComplexDeleteIdUser(AppData.complex_id,"Bearer"+" "+token, new Callback<ComplexDelete>() {
            @Override
            public void onResponse(Call<ComplexDelete> call, Response<ComplexDelete> response) {

                ComplexDelete responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {
                        pd.setMessage("loading...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toasty.success(ComplexDetailsActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT, true).show();
                                Intent in = new Intent(ComplexDetailsActivity.this, MainActivity.class);
                                in.putExtra("flag","012");
                                startActivity(in);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();

                            }
                        },1000);

                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(ComplexDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(ComplexDetailsActivity.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
//                            Toasty.error(ComplexDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }


                }
            }
            @Override
            public void onFailure(Call<ComplexDelete> call, Throwable t) {
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(ComplexDetailsActivity.this, MainActivity.class);
        in.putExtra("flag","012");
        startActivity(in);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
