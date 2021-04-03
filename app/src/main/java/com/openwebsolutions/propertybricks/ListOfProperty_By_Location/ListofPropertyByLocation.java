package com.openwebsolutions.propertybricks.ListOfProperty_By_Location;

import android.app.Dialog;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Adapter.ListofPropertyByLocation_Adapter;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Complex_Details_Page.ComplexDetailsActivity;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.PropertyAddedINComplex.PropertyAddedINComplex;
import com.openwebsolutions.propertybricks.Model.PropertyByLocation.Property;
import com.openwebsolutions.propertybricks.Model.PropertyByLocation.PropertyByLocation;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListofPropertyByLocation extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recycler_list_property;

    ImageView iv_propertylist_back;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;

    int property_id=0;
    int complex_id=0;

    ArrayList<Property> arrayList =new ArrayList<>();
    ListofPropertyByLocation_Adapter adapter;

    LinearLayoutManager layoutManager;
    RelativeLayout rel_propertylist;
    TextView text_propertylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listof_property_by_location);

        init();

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            getPropertByLocation();
        }
        else{
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    private void getPropertByLocation() {
        token=sh.getString("_Token","");

        MainApplication.apiManager.getlist_PropertyInComplexIdUser(AppData.complex_id,"Bearer"+" "+token, new Callback<PropertyByLocation>() {
            @Override
            public void onResponse(Call<PropertyByLocation> call, Response<PropertyByLocation> response) {

                PropertyByLocation responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(ListofPropertyByLocation.this,"Successfull"+response.message(),Toast.LENGTH_LONG).show();
                    try {
                        arrayList=responseUser.getData();
                        if(arrayList.size()!=0) {
                            String a=String.valueOf(arrayList.size());
                            adapter=new ListofPropertyByLocation_Adapter(arrayList,ListofPropertyByLocation.this);
                            recycler_list_property.setVisibility(View.VISIBLE);
                            layoutManager = new LinearLayoutManager(ListofPropertyByLocation.this, LinearLayoutManager.VERTICAL, false);
                            recycler_list_property.setLayoutManager(layoutManager);
                            recycler_list_property.setItemAnimator(new DefaultItemAnimator());
                            try {
                                recycler_list_property.setAdapter(adapter);
                            }
                            catch (Exception e){

                            }
                            adapter.SetOnItemClickListener(new ListofPropertyByLocation_Adapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {

                                    if (view.getId() == R.id.iv_add_propetylist){
                                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                                        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                                            property_id=arrayList.get(position).getId();
                                            complex_id=AppData.complex_id;
                                            addpropertyInComplex(property_id,complex_id,position);

                                        }
                                        else{
                                            Toast.makeText(ListofPropertyByLocation.this,"Please check internet connection",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                    if (view.getId() == R.id.lin_pro_details){
                                        Dialog_show_property(position);
                                    }
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {

                                }
                            });
                            rel_propertylist.setVisibility(View.GONE);
                        }
                        else {
                            rel_propertylist.setVisibility(View.VISIBLE);
                            recycler_list_property.setVisibility(View.GONE);
                        }
                    }
                    catch (Exception e){
                        Log.d("Error","eroor");
                    }

                } else {
//                    Toast.makeText(ListofPropertyByLocation.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(ListofPropertyByLocation.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(ListofPropertyByLocation.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(ListofPropertyByLocation.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }


                }
            }
            @Override
            public void onFailure(Call<PropertyByLocation> call, Throwable t) {
                Toast.makeText(ListofPropertyByLocation.this,"Internal Error",Toast.LENGTH_LONG).show();
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

    private void addpropertyInComplex(int property_id, int complex_id, final int pos) {
        token=sh.getString("_Token","");

        MainApplication.apiManager.add_PropertyInComplexUser(property_id,complex_id,"Bearer"+" "+token, new Callback<PropertyAddedINComplex>() {
            @Override
            public void onResponse(Call<PropertyAddedINComplex> call, Response<PropertyAddedINComplex> response) {

                PropertyAddedINComplex responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {
                    Toasty.success(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT, true).show();

                   arrayList.remove(pos);
                   adapter.notifyDataSetChanged();

                   if(arrayList.size()==0){
                       rel_propertylist.setVisibility(View.VISIBLE);
                       text_propertylist.setText("No property left to show");

                   }
//                   getPropertByLocation();

                } else {
//                    Toast.makeText(ListofPropertyByLocation.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(ListofPropertyByLocation.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(ListofPropertyByLocation.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(ListofPropertyByLocation.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }


                }
            }
            @Override
            public void onFailure(Call<PropertyAddedINComplex> call, Throwable t) {
                Toast.makeText(ListofPropertyByLocation.this,"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void init() {

        text_propertylist=findViewById(R.id.text_propertylist);
        iv_propertylist_back=findViewById(R.id.iv_propertylist_back);
        iv_propertylist_back.setOnClickListener(ListofPropertyByLocation.this);

        Toasty.Config.getInstance()
                .apply(); // required

        recycler_list_property=findViewById(R.id.recycler_list_property);
        rel_propertylist=findViewById(R.id.rel_propertylist);
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
    public void onBackPressed() {
        Intent in = new Intent(ListofPropertyByLocation.this, MainActivity.class);
        in.putExtra("flag","012");
        startActivity(in);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_propertylist_back:
                Intent in = new Intent(ListofPropertyByLocation.this, MainActivity.class);
                in.putExtra("flag","012");
                startActivity(in);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
                break;
        }
    }
}
