package com.openwebsolutions.propertybricks.AddPropertyActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;


import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

import com.openwebsolutions.propertybricks.Adapter.AutoComplete.PlaceAutocompleteAdapter;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.AddPropertyModel;
import com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty.DeleteTenantInProperty;
import com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id.Data_Property;
import com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id.GetPropertyEdit;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.squareup.picasso.Picasso;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddProperty extends AppCompatActivity
        implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks,View.OnClickListener , OnMapReadyCallback {

    ImageView iv_addproperty_back;

    String property_id_edit=null;
    int property_id=0;
    String add=null;
    TextView txt_designer_name_pro;

    RelativeLayout rel_delete_tenant;
    TextView txt_tennat_holder,tv_delete_tenant_edit,tv_sucessfull_delete;
    int tenant_id=0;

    Context mContext;//AutoComplete
    GoogleApiClient mGoogleApiClient;//AutoComplete
    LinearLayout mParent;//AutoComplete
    private RecyclerView mRecyclerView;//AutoComplete
    LinearLayoutManager llm;//AutoComplete
    PlaceAutocompleteAdapter mAdapter;//AutoComplete
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(//AutoComplete
            new LatLng(-0, 0), new LatLng(0, 0));
    EditText mSearchEdittext;//AutoComplete

    TextView tv_upload;
    ImageView camera,uploaded_imagae;
    public static final int TXT_CAMERA=1;
    public static final int TXT_STORAGE=2;
    public static final int REQUEST_GROUP_PERMISSION=425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    String imageTempName;
    Uri tempUri;
    String path="";

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String property_image="";

    String email=null;
    String password=null;

    Double p1 = null;
    Double p2 = null;
    private GoogleMap mMap;
    private FragmentManager mFragmentManager;
    LinearLayout lin_map;

    LinearLayout lin_details_property;

    int REQUEST_CODE= 100;
    String TAG="GoogleMapAct";

    TextView submit_pro;
    EditText et_property_name,et_property_details,et_property_price;

    int flag=0;

    SwipeRefreshLayout swiperefresh;
    @Override
    public void onStart() {
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            mGoogleApiClient.connect();
            super.onStart();
    }//AutoComplete

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }//AutoComplete

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_property);

        initViews();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFragmentManager = getSupportFragmentManager();

        mContext = AddProperty.this;//AutoComplete
        mGoogleApiClient = new GoogleApiClient.Builder(this)//AutoComplete
                .enableAutoManage(this, 0 /* clientId */, this)//AutoComplete
                .addApi(Places.GEO_DATA_API)
                .build();

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // notify user you are online
            Geocoder geo = new Geocoder(this, new Locale("fa"));//AutoComplete
            initVi();//AutoComplete

        }
        else {
            Toast.makeText(AddProperty.this,"No internet",Toast.LENGTH_SHORT).show();
        }
        property_id_edit=getIntent().getStringExtra("property_id_edit");


        if(property_id_edit!=null){

            property_id=Integer.valueOf(property_id_edit);
            lin_details_property.setVisibility(View.VISIBLE);
            lin_map.setVisibility(View.VISIBLE);
            token=sh.getString("_Token","");

            if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

                getProperty_By_Id();//getting property w.r.t "id"
            }
            else {
                Toast.makeText(AddProperty.this,"Please check internet connection",Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void getProperty_By_Id() {
        MainApplication.apiManager.getPropertyByIdUser(property_id,"Bearer"+" "+token, new Callback<GetPropertyEdit>() {
            @Override
            public void onResponse(Call<GetPropertyEdit> call, Response<GetPropertyEdit> response) {

                GetPropertyEdit responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

                    try {
                        AppData.flag_edit=1;
                        Data_Property data_property=responseUser.getData();
                        et_property_name.setText(data_property.getPropertyName());
                        et_property_details.setText(data_property.getPropertyDes());

                        et_property_price.setText(data_property.getPropertyPrice());
                        mSearchEdittext.setText(data_property.getPropertyLocation());
                        add=mSearchEdittext.getText().toString().trim();
                        tv_upload.setVisibility(View.GONE);
                        camera.setVisibility(View.GONE);
                        uploaded_imagae.setVisibility(View.VISIBLE);
                        txt_designer_name_pro.setText("Edit Property");
                        tv_sucessfull_delete.setVisibility(View.GONE);

                        if(data_property.getTenant()!=null){
                            rel_delete_tenant.setVisibility(View.VISIBLE);
                            tenant_id=data_property.getTenant().getId();

                            Log.d("tenant_ID",String.valueOf(tenant_id));
                            txt_tennat_holder.setText(data_property.getTenant().getTenantOwnerName());

                            tv_delete_tenant_edit.setOnClickListener(AddProperty.this);
                        }

                        Picasso.get().load(AppData.image_url +data_property.getImage()).resize(100,80).into(uploaded_imagae);
                        editor.putString("property_image_name",data_property.getImage());

                        uploaded_imagae.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showPictureDialog();
                            }
                        });
                        getMap();
                    }
                    catch (Exception e){

                        Log.d("Error","eroor");
                    }

                } else {
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(AddProperty.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(AddProperty.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toast.makeText(AddProperty.this,
                                    "Something wrong"
                                    , Toast.LENGTH_LONG).show();
                            break;
                    }

                }
            }
            @Override
            public void onFailure(Call<GetPropertyEdit> call, Throwable t) {
                Toast.makeText(AddProperty.this,"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getMap() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        Geocoder coder = new Geocoder(AddProperty.this);
        List<Address> address;

        try {
            address = coder.getFromLocationName(add, 5);
            if (address == null) {
                return;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                p1 = (double) (location.getLatitude());
                p2 = (double) (location.getLongitude());
            } else {
                Toast.makeText(AddProperty.this, "No internet", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            LatLng sydney = new LatLng(p1, p2);

            AppData.latitude=String.valueOf(p1);
            AppData.longitude=String.valueOf(p2);

            mMap.addMarker(new MarkerOptions().position(sydney)
                    .title(add)
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)).draggable(false));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
            mMap.setMaxZoomPreference(30);
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(p1, p2), 50.0f));

            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            mMap.setBuildingsEnabled(true);
            mMap.setIndoorEnabled(true);
            mMap.setTrafficEnabled(true);
            mMap.getUiSettings().setCompassEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            mMap.getUiSettings().setMapToolbarEnabled(true);
            mMap.getUiSettings().setZoomControlsEnabled(true);
        }
        else {
            Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();

        }
    }

    private void initVi() {
        mRecyclerView = (RecyclerView) findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = (EditText) findViewById(R.id.search_et);
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {


            mAdapter = new PlaceAutocompleteAdapter(this, R.layout.view_placesearch,
                    mGoogleApiClient, BOUNDS_INDIA, null);
            mRecyclerView.setAdapter(mAdapter);
        }
        else {
            Toast.makeText(AddProperty.this,"No internet",Toast.LENGTH_SHORT).show();
        }

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {

                    if (mAdapter != null) {
                        mRecyclerView.setAdapter(mAdapter);
                        if (mRecyclerView.getVisibility() == View.GONE)
                            mRecyclerView.setVisibility(View.VISIBLE);
                    }
                } else {

                    mRecyclerView.setVisibility(View.GONE);
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                    if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                            || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

                        mAdapter.getFilter().filter(s.toString());
                    }

                } else if (!mGoogleApiClient.isConnected()) {
//                    Toast.makeText(getApplicationContext(), Constants.API_NOT_CONNECTED, Toast.LENGTH_SHORT).show();
                    Log.e("", "NOT CONNECTED");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void initViews() {
        iv_addproperty_back=findViewById(R.id.iv_addproperty_back);
        iv_addproperty_back.setOnClickListener(this);
        txt_designer_name_pro=findViewById(R.id.txt_designer_name_pro);
        tv_sucessfull_delete=findViewById(R.id.tv_sucessfull_delete);
        tv_delete_tenant_edit=findViewById(R.id.tv_delete_tenant_edit);
        txt_tennat_holder=findViewById(R.id.txt_tennat_holder);
        rel_delete_tenant=findViewById(R.id.rel_delete_tenant);
        rel_delete_tenant.setVisibility(View.GONE);
        swiperefresh=findViewById(R.id.swiperefresh);
        tv_upload=findViewById(R.id.tv_upload);
        camera=findViewById(R.id.camera);
        uploaded_imagae=findViewById(R.id.uploaded_imagae);

        camera.setOnClickListener(this);
        tv_upload.setOnClickListener(this);

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

        lin_map=findViewById(R.id.lin_map);
        lin_details_property=findViewById(R.id.lin_details_property);


        submit_pro=findViewById(R.id.submit_pro);
        submit_pro.setOnClickListener(AddProperty.this);

        et_property_price=findViewById(R.id.et_property_price);
        et_property_name=findViewById(R.id.et_property_name);
        et_property_details=findViewById(R.id.et_property_details);
        int c1 = getResources().getColor(R.color.light_colorText);
        int c2 = getResources().getColor(R.color.dark_colorText);
        int c3 = getResources().getColor(R.color.dark_colorText);
        swiperefresh.setColorSchemeColors(c1, c2, c3);

        swiperefresh.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        doYourUpdate();
                    }
                }
        );

    }

    private void doYourUpdate() {

        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swiperefresh.setRefreshing(false);

                    Toast.makeText(AddProperty.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }, 1000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swiperefresh.setRefreshing(false);

                    Toast.makeText(AddProperty.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    }

    @Override
    public void onBackPressed() {
       super.onBackPressed();
        AddProperty.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_addproperty_back:
                super.onBackPressed();
                AddProperty.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                break;
            case R.id.camera:

                    if(checkpermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ArrayList<String> permissionNeeded = new ArrayList<>();
                            ArrayList<String> permissionAvailable = new ArrayList<>();
                            permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            permissionAvailable.add(Manifest.permission.CAMERA);
                            for (String permission : permissionAvailable) {

                                if (ContextCompat.checkSelfPermission(AddProperty.this, permission) != PackageManager.PERMISSION_GRANTED)
                                    permissionNeeded.add(permission);
                            }
                            requestGroupPermission(permissionNeeded);
                            showPictureDialog();
                        }
                    }
                    else {
                        showPictureDialog();
                    }
                break;
            case R.id.tv_upload:
                    if(checkpermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                        if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                            ArrayList<String> permissionNeeded = new ArrayList<>();
                            ArrayList<String> permissionAvailable = new ArrayList<>();
                            permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            permissionAvailable.add(Manifest.permission.CAMERA);
                            for (String permission : permissionAvailable) {

                                if (ContextCompat.checkSelfPermission(AddProperty.this, permission) != PackageManager.PERMISSION_GRANTED)
                                    permissionNeeded.add(permission);
                            }
                            requestGroupPermission(permissionNeeded);
                            showPictureDialog();
                        }
                    }
                    else {
                        showPictureDialog();
                    }
                break;
            case R.id.submit_pro:
                if(AppData.flag_edit==0) {

                    if (valid()) {

                        addproperty();//adding property in database
                    }
                }
                else if(AppData.flag_edit==1){

                    File file = new File(path);

                    if(!path.equals("")){
                        if (valid()) {
                           addpropertyById_Path();//editing property in database
                       }
                    }
                    else if(path.equals("")){
//                        Toast.makeText(AddProperty.this,"No image",Toast.LENGTH_SHORT).show();
                        if (valid()) {
                            addpropertyById();
                        }
                    }
                }
                break;

            case R.id.tv_delete_tenant_edit:

                deleteTenant();//delete tenant from property_edit
                break;
        }
    }

    private void deleteTenant() {
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
                        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit2_delete);
                        progressBar.setVisibility(View.VISIBLE);
                        tv_delete_tenant_edit.setText("");
                        ThreeBounce threeBounce = new ThreeBounce();
                        progressBar.setIndeterminateDrawable(threeBounce);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                progressBar.setVisibility(View.GONE);
                                rel_delete_tenant.setVisibility(View.GONE);
                                tv_sucessfull_delete.setVisibility(View.VISIBLE);
                            }
                        }, 2000);



                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(AddProperty.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(AddProperty.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toast.makeText(AddProperty.this,
                                    "Something wrong"
                                    , Toast.LENGTH_LONG).show();
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

    private void addpropertyById() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_property_name.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mSearchEdittext.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_property_details.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), et_property_price.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), AppData.latitude);
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), AppData.longitude);

        property_image=sh.getString("property_image_name","");
        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), property_image);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");
            try{
                MainApplication.apiManager.addpropertyUserById(property_id,"Bearer" + " " + token, location, name, description, price,lat,longitude, new Callback<AddPropertyModel>() {
                    @Override
                    public void onResponse(Call<AddPropertyModel> call, Response<AddPropertyModel> response) {

                        final AddPropertyModel responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3_pro);
                            progressBar.setVisibility(View.VISIBLE);
                            submit_pro.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(responseUser.getSuccess()!=null){
                                        Toasty.error(AddProperty.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toasty.success(AddProperty.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    submit_pro.setText("Submit");
                                    progressBar.setVisibility(View.GONE);
                                    Intent in = new Intent(AddProperty.this, MainActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    finish();
                                }
                            }, 2000);
                        } else {
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(AddProperty.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(AddProperty.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toast.makeText(AddProperty.this,
                                            "Already exists"
                                            , Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddPropertyModel> call, Throwable t) {
                        Toast.makeText(AddProperty.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        Log.d("Error is", t.getMessage());
                    }
                });

            }
            catch (Exception e){

            }

        }
        else {
            Log.d("Error","No internet connection");
        }

    }

    private void addpropertyById_Path() {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_property_name.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mSearchEdittext.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_property_details.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), et_property_price.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), AppData.latitude);
        RequestBody longtitude = RequestBody.create(MediaType.parse("text/plain"), AppData.longitude);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");
            try{

                MainApplication.apiManager.addpropertyUserById_Path(property_id,"Bearer" + " " + token, fileToUpload, location, name, description, price,lat,longtitude, new Callback<AddPropertyModel>() {
                    @Override
                    public void onResponse(Call<AddPropertyModel> call, Response<AddPropertyModel> response) {

                        final AddPropertyModel responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3_pro);
                            progressBar.setVisibility(View.VISIBLE);
                            submit_pro.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    submit_pro.setText("Submit");
                                    if(responseUser.getSuccess()!=null){
                                        Toasty.error(AddProperty.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toasty.success(AddProperty.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }

                                    progressBar.setVisibility(View.GONE);
                                    Intent in = new Intent(AddProperty.this, MainActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    finish();
                                }
                            }, 2000);
                        } else {

                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(AddProperty.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(AddProperty.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toast.makeText(AddProperty.this,
                                            "Already exists"
                                            , Toast.LENGTH_LONG).show();
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AddPropertyModel> call, Throwable t) {
                        Toast.makeText(AddProperty.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        Log.d("Error is", t.getMessage());
                    }
                });

            }
            catch (Exception e){

            }
        }
        else {
            Log.d("Error","No internet connection");
        }
    }

    private void addproperty() {

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_property_name.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mSearchEdittext.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_property_details.getText().toString());
        RequestBody price = RequestBody.create(MediaType.parse("text/plain"), et_property_price.getText().toString());
        RequestBody latitude = RequestBody.create(MediaType.parse("text/plain"), AppData.latitude);
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), AppData.longitude);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");

            try {
                MainApplication.apiManager.addpropertyUser("Bearer" + " " + token, fileToUpload, location, name, description, price,latitude,longitude, new Callback<AddPropertyModel>() {
                    @Override
                    public void onResponse(Call<AddPropertyModel> call, Response<AddPropertyModel> response) {

                        final AddPropertyModel responseUser = response.body();

                        if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                            Log.d("mhg",""+responseUser.getMessage()+responseUser.getSuccess());
                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3_pro);
                            progressBar.setVisibility(View.VISIBLE);
                            submit_pro.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    submit_pro.setText("Submit");
                                    progressBar.setVisibility(View.GONE);

                                    if(responseUser.getSuccess()!=null){
                                        Toasty.error(AddProperty.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toasty.success(AddProperty.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    }
                                    Intent in = new Intent(AddProperty.this, MainActivity.class);
                                    startActivity(in);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    finish();
                                }
                            }, 2000);
                        }

                        else {
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(AddProperty.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(AddProperty.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toast.makeText(AddProperty.this,
                                            "Already exists"
                                            , Toast.LENGTH_LONG).show();
                                    break;
                            }
//                            Toast.makeText(AddProperty.this,
//                                    "Already exists"
//                                    , Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<AddPropertyModel> call, Throwable t) {
                        Toasty.error(AddProperty.this,
                                "Internal Error " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        Log.d("Error is", t.getMessage());
                    }
                });
            }
            catch (Exception e){

            }
            }
        else {
           Log.d("Error","No internet connection");
        }
    }

    private boolean valid() {
        boolean isvalid=true;
        if(tv_upload.getVisibility()==View.VISIBLE && camera.getVisibility()==View.VISIBLE){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Select property picture", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        if(mSearchEdittext.getText().toString().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please give location", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        if(et_property_details.getText().toString().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please Enter property details", Toast.LENGTH_SHORT, true).show();

            et_property_details.requestFocus();
            return isvalid;
        }
        if(et_property_price.getText().toString().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please Enter property price", Toast.LENGTH_SHORT, true).show();

            et_property_price.requestFocus();
            return isvalid;
        }
        if(et_property_name.getText().toString().isEmpty()){
            isvalid=false;
//            Toast.makeText(this,"Enter property name",Toast.LENGTH_LONG).show();
            Toasty.warning(getApplicationContext(), "Please Enter property name", Toast.LENGTH_SHORT, true).show();

            et_property_name.requestFocus();
            return isvalid;
        }

        return isvalid;
    }

    private void requestGroupPermission(ArrayList<String> permissions) {
        String[] permissionList =new String[permissions.size()];
        permissions.toArray(permissionList);

        ActivityCompat.requestPermissions(AddProperty.this,permissionList,REQUEST_GROUP_PERMISSION);
    }

    private int checkpermission(int permission) {
        int status= PackageManager.PERMISSION_DENIED;

        switch (permission){

            case TXT_CAMERA:
                status= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                break;
            case TXT_STORAGE:
                status= ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;

        }
        return status;
    }

    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary(which);
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;

                        }
                    }
                });
        pictureDialog.show();
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    private void choosePhotoFromGallary(int which) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            int position;

            if (data != null) {

                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    tempUri=contentURI;
                      path=getRealPathFromURI(AddProperty.this,contentURI);

                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
                    uploaded_imagae.setVisibility(View.VISIBLE);
                    camera.setVisibility(View.GONE);
                    tv_upload.setVisibility(View.GONE);
                    Picasso.get().load(contentURI).resize(size,size).into(uploaded_imagae);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddProperty.this, "Failed to save!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

             tempUri = getImageUri(getApplicationContext(), thumbnail,imageTempName);
             path=getRealPathFromURI(this,tempUri);

            uploaded_imagae.setVisibility(View.VISIBLE);
            camera.setVisibility(View.GONE);
            tv_upload.setVisibility(View.GONE);
            uploaded_imagae.setImageBitmap(thumbnail);
        }
    }

    private String getRealPathFromURI(Context context, Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    private Uri getImageUri(Context inContext, Bitmap thumbnail, String imageTempName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), thumbnail, imageTempName, null);
        return Uri.parse(path);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
        }
        catch (Exception e){

        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onPlaceClick(ArrayList<PlaceAutocompleteAdapter.PlaceAutocomplete> mResultList, final int position) {
        if (mResultList != null) {
            try {
                final String placeId = String.valueOf(mResultList.get(position).placeId);

                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);

                ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
                    placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                        @Override
                        public void onResult(PlaceBuffer places) {
                            if (places.getCount() == 1) {
                                //Do the things here on Click.....
                                Intent data = new Intent();
                                data.putExtra("lat", String.valueOf(places.get(0).getLatLng().latitude));
                                data.putExtra("lng", String.valueOf(places.get(0).getLatLng().longitude));
                                setResult(AddProperty.RESULT_OK, data);
//                            Toast.makeText(getApplicationContext(), " "+places.get(0).getAddress(), Toast.LENGTH_SHORT).show();
                                hideSoftInput1(AddProperty.this);
                                mSearchEdittext.setText(places.get(0).getAddress());
                                mRecyclerView.setVisibility(View.GONE);

                                lin_map.setVisibility(View.VISIBLE);

                                lin_details_property.setVisibility(View.VISIBLE);

                                 add = mSearchEdittext.getText().toString().trim();

                                 getMap();//getting map*********


                            } else {
                                Toast.makeText(getApplicationContext(), "something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(getApplicationContext(), "No internet", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "something wrong", Toast.LENGTH_SHORT).show();
            }

        }
    }

    public static void hideSoftInput1(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view == null) view = new View(activity);
            InputMethodManager imm = (InputMethodManager) activity
                    .getSystemService(Activity.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        catch (Exception e){

        }
    }
}
