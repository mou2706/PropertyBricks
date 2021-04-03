package com.openwebsolutions.propertybricks.SearchingActivity;

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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.Adapter.RecyclerRecentSearch;
import com.openwebsolutions.propertybricks.Adapter.SearchingAdapter;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.Complex_Details_Page.ComplexDetailsActivity;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.Model.Search_Item.Search;
import com.openwebsolutions.propertybricks.Model.Search_Item.Searchitem;
import com.openwebsolutions.propertybricks.Model.Sqlite_ModelDemo.ModelDemo;
import com.openwebsolutions.propertybricks.PropertyAdd_InComplex.PropertyAddComplex;
import com.openwebsolutions.propertybricks.Property_Details_Page.PropertyDetailsActivity;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.openwebsolutions.propertybricks.Sqlitedata_Recycler.Sqlitedata;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SeachingProperty extends AppCompatActivity implements View.OnClickListener {
    ImageView iv_back;
    RelativeLayout rel_cancel_btn;
    EditText et_search;
//    TextView tv_go;

    ProgressDialog pd;
    String search_result="";
    int val;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String property_image="";
    String email=null;
    String password=null;

    private ArrayList<Search> property = new ArrayList<>();
    RecyclerView search_list;
    SearchingAdapter adapter;
    LinearLayoutManager layoutManager;

    ModelDemo modelDemo;
    Sqlitedata sqlitedata;
    RecyclerView recycler_recenthistory;
    RecyclerRecentSearch adapter_recent;
    ArrayList<ModelDemo> arrayList_recent=new ArrayList<>();
    LinearLayout lin_no_recent;
    TextView tv_clear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seaching_property);

        init();

        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {

                    search_result=String.valueOf(s);
                    getSearchResult(search_result);

                } else {

                    search_list.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        arrayList_recent=sqlitedata.getAllUser();

        if(arrayList_recent.size()!=0){

            recycler_recenthistory.setVisibility(View.VISIBLE);
            lin_no_recent.setVisibility(View.GONE);
            adapter_recent=new RecyclerRecentSearch(arrayList_recent,SeachingProperty.this);
            layoutManager = new LinearLayoutManager(SeachingProperty.this, LinearLayoutManager.VERTICAL, false);
            recycler_recenthistory.setLayoutManager(layoutManager);
            recycler_recenthistory.setItemAnimator(new DefaultItemAnimator());
            recycler_recenthistory.setAdapter(adapter_recent);
        }
        et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // Your piece of code on keyboard search click
                    return true;
                }
                return false;
            }
        });
    }

    private void init() {

        tv_clear=findViewById(R.id.tv_clear);
        tv_clear.setOnClickListener(this);
        lin_no_recent=findViewById(R.id.lin_no_recent);
        recycler_recenthistory=findViewById(R.id.recycler_recenthistory);

        modelDemo=new ModelDemo();
        sqlitedata=new Sqlitedata(this);

        search_list=findViewById(R.id.search_list);
        pd = new ProgressDialog(SeachingProperty.this,R.style.AppCompatAlertDialogStyle);

        iv_back=findViewById(R.id.iv_back);
        rel_cancel_btn=findViewById(R.id.rel_cancel_btn);
        et_search=findViewById(R.id.et_search);

        iv_back.setOnClickListener(this);
        rel_cancel_btn.setOnClickListener(this);

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
    }

    private void getSearchResult(String search_) {
        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            try{
                MainApplication.apiManager.getSearchItem(search_, "Bearer" + " " + token, new Callback<Searchitem>() {
                    @Override
                    public void onResponse(Call<Searchitem> call, Response<Searchitem> response) {

                        Searchitem responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {

                            property=responseUser.getSearch();
                            if(property!=null) {
                                search_list.setVisibility(View.VISIBLE);
                                adapter = new SearchingAdapter(property, SeachingProperty.this);
                                layoutManager = new LinearLayoutManager(SeachingProperty.this, LinearLayoutManager.VERTICAL, false);
                                search_list.setLayoutManager(layoutManager);
                                search_list.setItemAnimator(new DefaultItemAnimator());
                                search_list.setAdapter(adapter);

                                adapter.SetOnItemClickListener(new SearchingAdapter.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        search_list.setVisibility(View.GONE);
                                        et_search.setText(property.get(position).getName());
                                        String type=property.get(position).getType();

                                        if(type.equalsIgnoreCase("property")){

                                            modelDemo.setName(property.get(position).getName());
                                            modelDemo.setLoc(property.get(position).getLocation());
                                            modelDemo.setType(property.get(position).getType());
                                            modelDemo.setPro_id(String.valueOf(property.get(position).getId()));
                                            sqlitedata.insert(modelDemo);

                                            AppData.property_id=property.get(position).getId();
                                            Intent in =new Intent(SeachingProperty.this, PropertyDetailsActivity.class);
                                            startActivity(in);
                                        }
                                        else {

                                            modelDemo.setName(property.get(position).getName());
                                            modelDemo.setLoc(property.get(position).getLocation());
                                            modelDemo.setType(property.get(position).getType());
                                            modelDemo.setPro_id(String.valueOf(property.get(position).getId()));
                                            sqlitedata.insert(modelDemo);

                                            AppData.complex_id=property.get(position).getId();
                                            Intent in =new Intent(SeachingProperty.this, ComplexDetailsActivity.class);
                                            startActivity(in);
                                        }
                                    }

                                    @Override
                                    public void onItemLongClick(View view, int position) {

                                    }
                                });
                            }

                        } else {
//                            Toast.makeText(SeachingProperty.this, "Something wrong" + response.message(), Toast.LENGTH_SHORT).show();
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(SeachingProperty.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(SeachingProperty.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toasty.error(SeachingProperty.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<Searchitem> call, Throwable t) {
                        Toasty.error(getApplicationContext(), "Internal Error", Toast.LENGTH_SHORT, true).show();

                    }
                });

            }
            catch (Exception e){

            }

        }
        else {
            Toasty.error(SeachingProperty.this, "Please check internet connection", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_back:
                super.onBackPressed();

                finish();
                break;

            case R.id.rel_cancel_btn:
                et_search.setText("");
                break;

            case R.id.tv_clear:

                Dialog_show();

                break;
        }
    }

    private void Dialog_show() {
        final Dialog dialog=new Dialog(SeachingProperty.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView yes1=dialog.findViewById(R.id.yes);
        TextView no1=dialog.findViewById(R.id.no);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);

        yes1.setText("Clear");
        no1.setText("Cancel");
        txt_dia.setText("Clear search history?");

        yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                if(arrayList_recent.size()!=0) {
                    sqlitedata.delete(arrayList_recent);
                    recycler_recenthistory.setVisibility(View.GONE);
                    lin_no_recent.setVisibility(View.VISIBLE);
                }
                else {

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        SeachingProperty.this.overridePendingTransition(R.anim.left_to_right,
//                R.anim.right_to_left);
        finish();
    }
}
