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

import com.openwebsolutions.propertybricks.Adapter.RecyclerAdapter_Complex;
import com.openwebsolutions.propertybricks.AddComplexActivity.AddComplex;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Complex_Details_Page.ComplexDetailsActivity;
import com.openwebsolutions.propertybricks.ListOfProperty_By_Location.ListofPropertyByLocation;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.Model.ComplexDelete.ComplexDelete;
import com.openwebsolutions.propertybricks.Model.GetComplexModel.Datum_Complex;
import com.openwebsolutions.propertybricks.Model.GetComplexModel.GetComplexModel;
import com.openwebsolutions.propertybricks.Model.GetComplexModel.PropertyDetails;
import com.openwebsolutions.propertybricks.PropertyAdd_InComplex.PropertyAddComplex;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Fragment_Complex extends Fragment implements View.OnClickListener{

    String email="";
    String password="";

    RelativeLayout rel_errorshow1;
    TextView tv_error_msg1;

    ImageView iv_addcomplex;
    TextView tv_addcomplex;
     TextView tv_count_complex;

    SharedPreferences sh;
    String token="";
    RecyclerView recycler_complex;
    LinearLayoutManager layoutManager;
    RecyclerAdapter_Complex adapter_complex;
    ArrayList<Datum_Complex> arrayList=new ArrayList<>();

    ArrayList<PropertyDetails> property=new ArrayList<>();

    ProgressDialog pd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__complex, container, false);

        tv_addcomplex=v.findViewById(R.id.tv_addcomplex);
        iv_addcomplex=v.findViewById(R.id.iv_addcomplex);
        tv_count_complex=v.findViewById(R.id.tv_count_complex);
        tv_addcomplex.setOnClickListener(this);
        iv_addcomplex.setOnClickListener(this);

        tv_error_msg1=v.findViewById(R.id.tv_error_msg1);
        rel_errorshow1=v.findViewById(R.id.rel_errorshow1);
        Toasty.Config.getInstance()
                .apply(); // required

        sh= this.getActivity().getSharedPreferences("user", Context.MODE_PRIVATE);
        token=sh.getString("_Token","");

        recycler_complex=v.findViewById(R.id.recycler_complex);

        ConnectivityManager conMgr = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            pd = new ProgressDialog(getActivity(),R.style.AppCompatAlertDialogStyle);
//            pd.setMessage("loading...");
//            pd.show();
            getComplex();//getComplex from server
        }
        else {
            rel_errorshow1.setVisibility(View.VISIBLE);

//            Toast.makeText(getActivity(),"Please check your internet connection",Toast.LENGTH_SHORT).show();
        }
        return v;
    }

    private void getComplex() {
        MainApplication.apiManager.getcomplexyUser("Bearer"+" "+token, new Callback<GetComplexModel>() {
            @Override
            public void onResponse(Call<GetComplexModel> call, Response<GetComplexModel> response) {

                GetComplexModel responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {

                        arrayList = responseUser.getData();
//                        Tenant tenant=arrayList.get(0).getTenant();
                        if(arrayList.size()!=0) {
                            rel_errorshow1.setVisibility(View.GONE);

                            String a=String.valueOf(arrayList.size());
                            tv_count_complex.setText(a);
                            adapter_complex = new RecyclerAdapter_Complex(arrayList, getContext());
                            layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                            recycler_complex.setLayoutManager(layoutManager);
                            recycler_complex.setItemAnimator(new DefaultItemAnimator());
                            recycler_complex.setAdapter(adapter_complex);
                            pd.dismiss();

                            adapter_complex.SetOnItemClickListener(new RecyclerAdapter_Complex.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, int position) {
                                    if (view.getId() == R.id.iv_addproperty_complex){
                                        property=arrayList.get(position).getProperty();
                                        int complex_id=arrayList.get(position).getId();
                                        AppData.complex_id=complex_id;
                                        Intent intent = new Intent(getActivity(), ListofPropertyByLocation.class);
                                        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                        startActivity(intent,bundle);
                                    }
                                    if (view.getId() == R.id.tv_addproperty_complex){
                                        property=arrayList.get(position).getProperty();
                                        int complex_id=arrayList.get(position).getId();
                                        AppData.complex_id=complex_id;
                                        Intent intent = new Intent(getActivity(), ListofPropertyByLocation.class);
                                        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                        startActivity(intent,bundle);
                                    }
                                    if (view.getId() == R.id.view_property){
                                        property=arrayList.get(position).getProperty();
                                        int complex_id=arrayList.get(position).getId();
                                        AppData.complex_id=complex_id;
                                        Intent intent = new Intent(getActivity(), PropertyAddComplex.class);
                                        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                        startActivity(intent,bundle);
                                    }
                                    if (view.getId() == R.id.lin_itemview1){
                                        Intent intent = new Intent(getActivity(), ComplexDetailsActivity.class);
                                        AppData.complex_id=arrayList.get(position).getId();
                                        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                                        startActivity(intent,bundle);
                                    }
                                }

                                @Override
                                public void onItemLongClick(View view, int position) {
                                    if(view.getId() == R.id.lin_itemview1) {
                                        property = arrayList.get(position).getProperty();
                                        DialogBox(position);
                                        AppData.complex_id = arrayList.get(position).getId();
                                    }
                                }
                            });
                        }
                        else {
                            try {
                                pd.dismiss();
                                Toast.makeText(getActivity(), "No Complex Available to show", Toast.LENGTH_SHORT).show();
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
                            rel_errorshow1.setVisibility(View.VISIBLE);
                            tv_error_msg1.setText("Something wrong");
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
            public void onFailure(Call<GetComplexModel> call, Throwable t) {
                try {
                    rel_errorshow1.setVisibility(View.VISIBLE);
                    tv_error_msg1.setText("Internal Error");
//                    Toast.makeText(getActivity(), "Internal Error", Toast.LENGTH_LONG).show();
                    pd.dismiss();
                }
                catch (Exception e){

                }
            }
        });
    }

    private void DialogBox(final int position) {

        final Dialog dialog=new Dialog(getContext());
        dialog.setContentView(R.layout.dialog_edit_delete);
        RelativeLayout rel_edit=dialog.findViewById(R.id.rel_edit);
        RelativeLayout rel_remove=dialog.findViewById(R.id.rel_remove);
        if(property.size()==0){

            rel_edit.setVisibility(View.VISIBLE);
            rel_remove.setVisibility(View.VISIBLE);
        }
        else {
            rel_edit.setVisibility(View.VISIBLE);
            rel_remove.setVisibility(View.GONE);
        }

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
                        Intent intent = new Intent(getActivity(), AddComplex.class);
                        intent.putExtra("complex_id_edit",String.valueOf(arrayList.get(position).getId()));
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

                Dialog_confirmation(position);
            }
        });
        dialog.setCancelable(true);
        dialog.show();
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

                    delete_complex(pos);
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

    private void delete_complex(final int pos) {
        token=sh.getString("_Token","");

        MainApplication.apiManager.getComplexDeleteIdUser(AppData.complex_id,"Bearer"+" "+token, new Callback<ComplexDelete>() {
            @Override
            public void onResponse(Call<ComplexDelete> call, Response<ComplexDelete> response) {

                ComplexDelete responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

                    try {
                        pd.setMessage("loading...");
                        pd.show();

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

//                                getComplex();
                                pd.dismiss();
                                arrayList.remove(pos);
                                adapter_complex.notifyDataSetChanged();
                                tv_count_complex.setText(String.valueOf(arrayList.size()));
                                Toasty.success(getActivity(), "Successfully Deleted", Toast.LENGTH_SHORT, true).show();

                            }
                        },1000);

                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();

                }
            }
            @Override
            public void onFailure(Call<ComplexDelete> call, Throwable t) {
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.tv_addcomplex:

                Intent intent = new Intent(getActivity(), AddComplex.class);
                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                startActivity(intent,bundle);
                break;
            case R.id.iv_addcomplex:
                Intent intent1 = new Intent(getActivity(), AddComplex.class);
                Bundle bundle1 = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
                startActivity(intent1,bundle1);
                break;
        }

    }
}
