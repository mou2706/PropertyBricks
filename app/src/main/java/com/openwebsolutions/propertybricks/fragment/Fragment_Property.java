package com.openwebsolutions.propertybricks.fragment;

import android.app.ActivityOptions;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.Target;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.openwebsolutions.propertybricks.Adapter.RecyclerAdapter_Property;
import com.openwebsolutions.propertybricks.AddPropertyActivity.AddProperty;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;

import com.openwebsolutions.propertybricks.Model.DeleteModel.Delete;
import com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty.DeleteTenantInProperty;
import com.openwebsolutions.propertybricks.Model.GetProperty.Datum;
import com.openwebsolutions.propertybricks.Model.GetProperty.GetPropertyModel;
import com.openwebsolutions.propertybricks.Model.GetProperty.Tenant;
import com.openwebsolutions.propertybricks.Occupied_Activity.Occupied_Activity;
import com.openwebsolutions.propertybricks.Property_Details_Page.PropertyDetailsActivity;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.openwebsolutions.propertybricks.Vacant_Activity.Vacant_Activity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Fragment_Property extends Fragment implements View.OnClickListener{

    ShowcaseView.Builder showCaseView;

    final int SHOWCASEVIEW_ID = 28;

    RelativeLayout rel_errorshow;
    TextView tv_error_msg;

    int tenant_id=0;
    String email="";
    String password="";
    ImageView iv_addproperty;
    TextView tv_addproperty;

    RecyclerView recycler_property;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    RecyclerAdapter_Property adapter_property;
    LinearLayoutManager layoutManager;
    ProgressDialog pd;
    int pos;
    ArrayList<Datum> arrayList=new ArrayList<>();

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__property, container, false);
        iv_addproperty=v.findViewById(R.id.iv_addproperty);
        tv_addproperty=v.findViewById(R.id.tv_addproperty);

        tv_addproperty.setOnClickListener(this);
        iv_addproperty.setOnClickListener(this);
        rel_errorshow=v.findViewById(R.id.rel_errorshow);
        tv_error_msg=v.findViewById(R.id.tv_error_msg);

        recycler_property=v.findViewById(R.id.recycler_property);
         sh= this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();


        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            pd = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
//            pd.setMessage("loading...");
//            pd.setCancelable(true);
//            pd.show();

            getProperty();//getProperty from server
        }
        else {
            rel_errorshow.setVisibility(View.VISIBLE);
//            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }

        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        final Target viewTarget = new ViewTarget(R.id.iv_addproperty, getActivity());
        getActivity().findViewById(R.id.iv_addproperty).post(new Runnable() {

            public void run() {
                new ShowcaseView.Builder(getActivity(), false)
                        .setTarget(viewTarget)
                        .setContentTitle("Welcome")
                        .blockAllTouches()
                        .hideOnTouchOutside()
                        .setContentText("Click this button to Add New Property")
                        .setStyle(R.style.ShowCaseViewStyle)
                        .singleShot(SHOWCASEVIEW_ID)
                        .build();
            }
        });
    }

    private void getProperty() {
        token=sh.getString("_Token","");
        MainApplication.apiManager.getPropertyUser("Bearer"+" "+token, new Callback<GetPropertyModel>() {
            @Override
            public void onResponse(Call<GetPropertyModel> call, Response<GetPropertyModel> response) {

                GetPropertyModel responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {
//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {
                        arrayList = responseUser.getData();
                        if(arrayList.size()!=0) {
                            rel_errorshow.setVisibility(View.GONE);
                            final Tenant tenant = arrayList.get(0).getTenant();
                            adapter_property = new RecyclerAdapter_Property(tenant, arrayList, getContext());
                            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recycler_property.setLayoutManager(layoutManager);
                            recycler_property.setItemAnimator(new DefaultItemAnimator());
                            recycler_property.setAdapter(adapter_property);
                            pd.dismiss();

                    adapter_property.SetOnItemClickListener(new RecyclerAdapter_Property.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            if (view.getId() == R.id.tv_occupied){
                                AppData.tenant_id=Integer.valueOf(arrayList.get(position).getTenant().getId());
                                Intent intent = new Intent(getActivity(), Occupied_Activity.class);
                                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                startActivity(intent,bundle);
                            }

                            if (view.getId() == R.id.tv_vacant){
                                AppData.position=position;
                                Intent intent = new Intent(getActivity(), Vacant_Activity.class);
                                AppData.property_id=arrayList.get(position).getId();
                                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                startActivity(intent,bundle);
                            }
                            if (view.getId() == R.id.lin_itemview){
                                Intent intent = new Intent(getActivity(), PropertyDetailsActivity.class);
                                AppData.property_id=arrayList.get(position).getId();
                                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                startActivity(intent,bundle);
                            }

//
                        }

                        @Override
                        public void onItemLongClick(View view, final int position) {

                            if(view.getId() == R.id.lin_itemview) {
//                            tenant_id=Integer.valueOf(arrayList.get(position).getTenant().getId());
                                DialogBox(position, tenant_id);
                                AppData.property_id = arrayList.get(position).getId();
                            }
                        }
                    });
                        }

                        else {
                            try {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "No Property Available to show", Toast.LENGTH_SHORT).show();
                            }
                            catch (Exception e){

                            }
                        }
                    }
                    catch (Exception e){

                    }
                } else {

                    switch (response.code()) {
                        case 401:
                            rel_errorshow.setVisibility(View.VISIBLE);
                            tv_error_msg.setText("Something wrong");
                            Toast.makeText(getActivity(), "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(getActivity(), SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toast.makeText(getActivity(),
                                    "Already exists"
                                    , Toast.LENGTH_LONG).show();
                            break;
                    }
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
            }
            @Override
            public void onFailure(Call<GetPropertyModel> call, Throwable t) {
                rel_errorshow.setVisibility(View.VISIBLE);
                tv_error_msg.setText("Internal Error");
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();
                pd.dismiss();
            }
        });
    }


    private void DialogBox(final int pos, final int tenant_id) {
        final Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_delete);
        RelativeLayout rel_edit=dialog.findViewById(R.id.rel_edit);
        RelativeLayout rel_remove=dialog.findViewById(R.id.rel_remove);
        RelativeLayout rel_remove_tenant=dialog.findViewById(R.id.rel_remove_tenant);
//        if(tenant_id!=0) {
//
//        rel_remove_tenant.setVisibility(View.VISIBLE);
//                    rel_remove_tenant.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Dialog_confirmation1(pos,tenant_id);
//            }
//        });
//        }

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
                        Intent intent = new Intent(getActivity(), AddProperty.class);
                        intent.putExtra("property_id_edit",String.valueOf(arrayList.get(pos).getId()));
                        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fade_in, R.anim.fade_out).toBundle();
                        startActivity(intent,bundle);
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

    private void Dialog_confirmation1(final int pos, final int tenant_id) {
        final Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Delete Tenant?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    dialog.dismiss();
                    String device_token=sh.getString("device_token","");

                    email=sh.getString("Email_id","");
                    password=sh.getString("Password","");
                    LoginCheck loginCheck = new LoginCheck(getActivity());
                    loginCheck.getLoginCheck(email, password,device_token);
                    delete_tenant(pos,tenant_id);
                    pd.setMessage("loading...");
                    pd.show();

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

    private void delete_tenant(final int pos, int tenant_id) {
        token=sh.getString("_Token","");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "");
        MainApplication.apiManager.deletetenantByIdinProperty(tenant_id,"Bearer"+" "+token,name, new Callback<DeleteTenantInProperty>() {
            @Override
            public void onResponse(Call<DeleteTenantInProperty> call, Response<DeleteTenantInProperty> response) {

                DeleteTenantInProperty responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {
//
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
//                                Toast.makeText(getActivity(),"Tenant deleted")
                                arrayList.get(pos).setTenant(null);
                                adapter_property.notifyDataSetChanged();
                                System.out.println(arrayList);
//                                getProperty();
                            }
                        }, 2000);



                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<DeleteTenantInProperty> call, Throwable t) {
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void Dialog_confirmation(final int pos) {
        final Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Delete ?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    dialog.dismiss();

                    email=sh.getString("Email_id","");
                    password=sh.getString("Password","");
                    String device_token=sh.getString("device_token","");

                    LoginCheck loginCheck = new LoginCheck(getActivity());
                    loginCheck.getLoginCheck(email, password,device_token);
                    delete_property(pos);
                    pd.setMessage("loading...");
                    pd.show();

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

    private void delete_property(final int pos) {
        token=sh.getString("_Token","");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "");

        MainApplication.apiManager.deletepropertyUserById(AppData.property_id,"Bearer"+" "+token,name, new Callback<Delete>() {
            @Override
            public void onResponse(Call<Delete> call, Response<Delete> response) {

                Delete responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                arrayList.remove(pos);
                                adapter_property.notifyDataSetChanged();
                                pd.dismiss();
                                getProperty();
                                Toasty.success(getActivity(), "Successfully Deleted", Toast.LENGTH_SHORT).show();
                            }
                        },1000);

                    }
                    catch (Exception e){
                        pd.dismiss();
                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<Delete> call, Throwable t) {
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }


    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.tv_addproperty:
                Intent intent = new Intent(getActivity(), AddProperty.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                startActivity(intent,bundle);
                break;
            case R.id.iv_addproperty:
                Intent intent1 = new Intent(getActivity(), AddProperty.class);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                startActivity(intent1,bundle1);
                break;
        }
    }
}