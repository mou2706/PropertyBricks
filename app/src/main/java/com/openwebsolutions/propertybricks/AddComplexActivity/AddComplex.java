package com.openwebsolutions.propertybricks.AddComplexActivity;

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
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
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
import com.openwebsolutions.propertybricks.AddPropertyActivity.AddProperty;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.AddComplexModel;
import com.openwebsolutions.propertybricks.Model.GetComplex_ByID.ComplexEdit;
import com.openwebsolutions.propertybricks.Model.GetComplex_ByID.GetComplexByID;
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

public class AddComplex extends AppCompatActivity  implements PlaceAutocompleteAdapter.PlaceAutoCompleteInterface, GoogleApiClient.OnConnectionFailedListener,
        GoogleApiClient.ConnectionCallbacks, View.OnClickListener, OnMapReadyCallback {

    String latitude="";
    String longitude="";

    ImageView iv_addcomplex_back;

    String complex_id_edit=null;
    int complex_id=0;
    String add=null;
    TextView txt_designer_name_com;
    String complex_image="";

    Context mContext;
    GoogleApiClient mGoogleApiClient;
    LinearLayout mParent;
    private RecyclerView mRecyclerView;
    LinearLayoutManager llm;
    PlaceAutocompleteAdapter mAdapter;
    private static final LatLngBounds BOUNDS_INDIA = new LatLngBounds(
            new LatLng(-0, 0), new LatLng(0, 0));
    private EditText mSearchEdittext;

    SwipeRefreshLayout swiperefresh;

    TextView tv_upload_com;
    ImageView camera_com;
    ImageView uploaded_imagae_com;
    public static final int TXT_CAMERA=1;
    public static final int TXT_STORAGE=2;
    public static final int REQUEST_GROUP_PERMISSION=425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    Double p1 = null;
    Double p2 = null;
    private GoogleMap mMap;
    private FragmentManager mFragmentManager;
    LinearLayout lin_map_com;

    String imageTempName;
    Uri tempUri;
    String path="";

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;

    LinearLayout lin_details_complex;

    int REQUEST_CODE= 100;
    String TAG="GoogleMapAct";

    TextView submit_com;
    EditText et_complex_name,et_complex_details;

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();

    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_complex);

        refresh();//refreshActivity
        init();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFragmentManager = getSupportFragmentManager();

        mContext = AddComplex.this;
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, 0 /* clientId */, this)
                .addApi(Places.GEO_DATA_API)
                .build();
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

            // notify user you are online
            Geocoder geo = new Geocoder(this, new Locale("fa"));
            initViews();
        }
        else {
            Toast.makeText(AddComplex.this,"No internet",Toast.LENGTH_SHORT).show();
        }
        complex_id_edit=getIntent().getStringExtra("complex_id_edit");
        if(complex_id_edit!=null){

            complex_id = Integer.valueOf(complex_id_edit);
            lin_details_complex.setVisibility(View.VISIBLE);
            lin_map_com.setVisibility(View.VISIBLE);
            token=sh.getString("_Token","");
            if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {

                getComplex_By_Id();//getting complex w.r.t "id"
            }
            else {
                Toast.makeText(AddComplex.this,"Please check internet connection",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getComplex_By_Id() {
        MainApplication.apiManager.getComplexByIdUser(complex_id,"Bearer"+" "+token, new Callback<GetComplexByID>() {
            @Override
            public void onResponse(Call<GetComplexByID> call, Response<GetComplexByID> response) {

                GetComplexByID responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

                    try {
                        AppData.flag_edit_com=1;
                        ComplexEdit data_property=responseUser.getData();

                        et_complex_name.setText(data_property.getComplexName());
                        et_complex_details.setText(data_property.getComplexDes());

                        txt_designer_name_com.setText("Edit Complex");
                        mSearchEdittext.setText(data_property.getComplexLocation());
                        add=mSearchEdittext.getText().toString().trim();
                        tv_upload_com.setVisibility(View.GONE);
                        camera_com.setVisibility(View.GONE);
                        uploaded_imagae_com.setVisibility(View.VISIBLE);

                        Picasso.get().load(AppData.image_url_complex +data_property.getImage()).resize(600,200).into(uploaded_imagae_com);
                        editor.putString("complex_image_name",data_property.getImage());

                        uploaded_imagae_com.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                showPictureDialog();
                            }
                        });
                        try {
                            getMap();
                        }
                        catch (Exception e){

                        }
                    }
                    catch (Exception e){

                        Log.d("Error","eroor");
                    }

                } else {

                    switch (response.code()) {
                        case 401:
                            Toast.makeText(AddComplex.this, "Token Expired Please Login", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(AddComplex.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toast.makeText(AddComplex.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }
                }
            }
            @Override
            public void onFailure(Call<GetComplexByID> call, Throwable t) {
                Toast.makeText(AddComplex.this,"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    private void getMap() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

        Geocoder coder = new Geocoder(AddComplex.this);
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
                Toast.makeText(AddComplex.this, "No internet", Toast.LENGTH_SHORT).show();
            }


        } catch (IOException e) {
            e.printStackTrace();

        }
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            LatLng sydney = new LatLng(p1, p2);

            AppData.latitude_complex=String.valueOf(p1);
            AppData.longitude_complex=String.valueOf(p2);

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

    private void init() {

        iv_addcomplex_back=findViewById(R.id.iv_addcomplex_back);
        iv_addcomplex_back.setOnClickListener(this);
        txt_designer_name_com=findViewById(R.id.txt_designer_name_com);
        tv_upload_com=findViewById(R.id.tv_upload_com);
        uploaded_imagae_com=findViewById(R.id.uploaded_imagae_com);
        camera_com=findViewById(R.id.camera_com);
        camera_com.setOnClickListener(this);
        tv_upload_com.setOnClickListener(this);

        lin_map_com=findViewById(R.id.lin_map_com);
        lin_details_complex=findViewById(R.id.lin_details_complex);

        submit_com=findViewById(R.id.submit_com);
        submit_com.setOnClickListener(this);
        et_complex_details=findViewById(R.id.et_complex_details);
        et_complex_name=findViewById(R.id.et_complex_name);
    }

    private void refresh() {
        swiperefresh=findViewById(R.id.swiperefresh_com);
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

                    Toast.makeText(AddComplex.this, "Refresh", Toast.LENGTH_SHORT).show();
                }
            }, 1000);
        }
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    swiperefresh.setRefreshing(false);

                    Toast.makeText(AddComplex.this, "No internet", Toast.LENGTH_SHORT).show();
                }
            }, 3000);
        }
    }

    private void initViews() {

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

        mRecyclerView = (RecyclerView) findViewById(R.id.list_search);
        mRecyclerView.setHasFixedSize(true);
        llm = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(llm);

        mSearchEdittext = (EditText) findViewById(R.id.search_et_com);

        mAdapter = new PlaceAutocompleteAdapter(this, R.layout.view_placesearch,
                mGoogleApiClient, BOUNDS_INDIA, null);
        mRecyclerView.setAdapter(mAdapter);

        mSearchEdittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count > 0) {

                    if (mAdapter != null) {
                        try {
                            mRecyclerView.setAdapter(mAdapter);
                            if (mRecyclerView.getVisibility() == View.GONE)
                                mRecyclerView.setVisibility(View.VISIBLE);
                        }
                        catch (Exception e){

                        }
                    }
                } else {

                    try {
                        mRecyclerView.setVisibility(View.GONE);
                    }
                    catch (Exception e){

                    }
                }
                if (!s.toString().equals("") && mGoogleApiClient.isConnected()) {
                    try {
                        mAdapter.getFilter().filter(s.toString());
                    }
                    catch (Exception e){

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AddComplex.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_addcomplex_back:
                super.onBackPressed();
                AddComplex.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                break;
            case R.id.camera_com:

                if (checkpermission(TXT_CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(AddComplex.this, permission) != PackageManager.PERMISSION_GRANTED)
                                permissionNeeded.add(permission);
                        }
                        requestGroupPermission(permissionNeeded);
                        showPictureDialog();
                    }
                } else {
                    showPictureDialog();
                }
                break;
            case R.id.tv_upload_com:

                if (checkpermission(TXT_CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(AddComplex.this, permission) != PackageManager.PERMISSION_GRANTED)
                                permissionNeeded.add(permission);
                        }
                        requestGroupPermission(permissionNeeded);
                        showPictureDialog();
                    }
                } else {
                    showPictureDialog();
                }
                break;
            case R.id.submit_com:
                if(AppData.flag_edit_com==0) {

                    if (valid()) {

                        addComplex();//adding complex in database
                    }
                }
                else if(AppData.flag_edit_com==1){
                    File file = new File(path);
                    if(!path.equals("")){
                        if (valid()) {

                            addcomplexById_Path();//editing complex in database with path
                        }
                    }
                    else if(path.equals("")){
//                        Toast.makeText(AddComplex.this,"No image",Toast.LENGTH_SHORT).show();
                        if (valid()) {
                            addcomplexById();
                        }
                    }
                }
                break;
        }

    }

    private void addcomplexById() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_complex_name.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mSearchEdittext.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_complex_details.getText().toString());

        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), AppData.latitude_complex);
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), AppData.longitude_complex);
        complex_image=sh.getString("property_image_name","");
        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), complex_image);
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");
            try{
                MainApplication.apiManager.addcomplexUserById(complex_id,"Bearer" + " " + token,image, location, name, description, lat,longitude, new Callback<AddComplexModel>() {
                    @Override
                    public void onResponse(Call<AddComplexModel> call, Response<AddComplexModel> response) {

                        final AddComplexModel responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {

                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3);
                            progressBar.setVisibility(View.VISIBLE);
                            submit_com.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(responseUser.getSuccess()!=null){
                                        Toasty.error(AddComplex.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    else {

                                        Toasty.success(AddComplex.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    }

                                    submit_com.setText("Submit");
                                    progressBar.setVisibility(View.GONE);
                                    Intent in = new Intent(AddComplex.this, MainActivity.class);
                                    in.putExtra("flag","012");
                                    startActivity(in);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    finish();
                                }
                            }, 2000);
                        } else {

                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(AddComplex.this, "Token Expired Please Login", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(AddComplex.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toast.makeText(AddComplex.this,
                                            "Already exists"
                                            , Toast.LENGTH_LONG).show();
                                    break;
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<AddComplexModel> call, Throwable t) {
                        Toast.makeText(AddComplex.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        Log.d("Error is", t.getMessage());
                    }
                });

            }
            catch (Exception e){

            }
        }

    }

    private void addcomplexById_Path() {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_complex_name.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mSearchEdittext.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_complex_details.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), AppData.latitude_complex);
        RequestBody longtitude = RequestBody.create(MediaType.parse("text/plain"), AppData.longitude_complex);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");

            try{
                MainApplication.apiManager.addcomplexUserById_Path(complex_id,"Bearer" + " " + token, fileToUpload, location, name, description, lat,longtitude, new Callback<AddComplexModel>() {
                    @Override
                    public void onResponse(Call<AddComplexModel> call, Response<AddComplexModel> response) {

                        final AddComplexModel responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3);
                            progressBar.setVisibility(View.VISIBLE);
                            submit_com.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if(responseUser.getSuccess()!=null){
                                        Toasty.error(AddComplex.this, ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                    }
                                    else {
                                        Toasty.success(AddComplex.this, "Successfully edited", Toast.LENGTH_SHORT).show();
                                    }

                                    submit_com.setText("Submit");
                                    progressBar.setVisibility(View.GONE);
                                    Intent in = new Intent(AddComplex.this, MainActivity.class);
                                    in.putExtra("flag","012");
                                    startActivity(in);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
//                                    AddComplex.super.onBackPressed();
//                                    AddComplex.this.overridePendingTransition(R.anim.left_to_right,
//                                            R.anim.right_to_left);
                                    finish();
                                }
                            }, 2000);
                        } else {

                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(AddComplex.this, "Token Expired Please Login", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(AddComplex.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toast.makeText(AddComplex.this,
                                            "Already exists"
                                            , Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddComplexModel> call, Throwable t) {
//                        Toast.makeText(AddComplex.this,
//                                "Error is " + t.getMessage()
//                                , Toast.LENGTH_LONG).show();
                        Log.d("Error is", t.getMessage());
                    }
                });

            }
            catch (Exception e){

            }
        }
    }

    private void addComplex() {

        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_complex_name.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), mSearchEdittext.getText().toString());
        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), et_complex_details.getText().toString());
        RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), AppData.latitude_complex);
        RequestBody longitude = RequestBody.create(MediaType.parse("text/plain"), AppData.longitude_complex);


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");
            MainApplication.apiManager.addcomplexUser("Bearer" + " " + token, fileToUpload, name, description, location,lat,longitude, new Callback<AddComplexModel>() {
                @Override
                public void onResponse(Call<AddComplexModel> call, Response<AddComplexModel> response) {

                    final AddComplexModel responseUser = response.body();
                    if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3);
                        progressBar.setVisibility(View.VISIBLE);
                        submit_com.setText("");
                        CubeGrid cubeGrid = new CubeGrid();
                        progressBar.setIndeterminateDrawable(cubeGrid);

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                submit_com.setText("Submit");
                                Log.d("suc",""+responseUser.getSuccess());
                                if(responseUser.getSuccess()!=null){
                                    Toasty.error(getApplicationContext(), ""+responseUser.getMessage(), Toast.LENGTH_SHORT).show();

                                }else {
                                    Toasty.success(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT, true).show();
                                }
                                Intent in = new Intent(AddComplex.this, MainActivity.class);
                                in.putExtra("flag","012");
                                startActivity(in);
                                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                finish();
                            }
                        }, 2000);
                    } else {
                        switch (response.code()) {
                            case 401:
                                Toast.makeText(AddComplex.this, "Token Expired Please Login", Toast.LENGTH_SHORT).show();
                                Intent in =new Intent(AddComplex.this, SigninActivity.class);
                                startActivity(in);
                                break;
                            default:
                                Toast.makeText(AddComplex.this,
                                        "Already exists"
                                        , Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                }

                @Override
                public void onFailure(Call<AddComplexModel> call, Throwable t) {
                    Toast.makeText(AddComplex.this,
                            "Error is " + t.getMessage()
                            , Toast.LENGTH_LONG).show();
                    Log.d("Error is", t.getMessage());
                }
            });

        }

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit3);
        progressBar.setVisibility(View.VISIBLE);
        submit_com.setText("");
        CubeGrid cubeGrid = new CubeGrid();
        progressBar.setIndeterminateDrawable(cubeGrid);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                finish();
                AddComplex.this.overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
            }
        }, 2000);
    }

    private boolean valid() {
        boolean isvalid=true;
        if(tv_upload_com.getVisibility()==View.VISIBLE && camera_com.getVisibility()==View.VISIBLE){
            isvalid=false;

            Toasty.warning(getApplicationContext(), "Select complex picture", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        if(mSearchEdittext.getText().toString().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please give location", Toast.LENGTH_SHORT, true).show();

            return isvalid;
        }
        if(et_complex_name.getText().toString().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please Enter Complex name", Toast.LENGTH_SHORT, true).show();

            et_complex_name.requestFocus();
            return isvalid;
        }
        if(et_complex_details.getText().toString().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please Enter Complex details", Toast.LENGTH_SHORT, true).show();

            et_complex_details.requestFocus();
            return isvalid;
        }

        return isvalid;
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
                    path=getRealPathFromURI(AddComplex.this,contentURI);
                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
                    uploaded_imagae_com.setVisibility(View.VISIBLE);
                    camera_com.setVisibility(View.GONE);
                    tv_upload_com.setVisibility(View.GONE);
                    Picasso.get().load(contentURI).resize(size,size).into(uploaded_imagae_com);
//                    Toast.makeText(AddProperty.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(AddComplex.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            try {
                Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
                tempUri = getImageUri(getApplicationContext(), thumbnail, imageTempName);
                path = getRealPathFromURI(this, tempUri);
                uploaded_imagae_com.setVisibility(View.VISIBLE);
                camera_com.setVisibility(View.GONE);
                tv_upload_com.setVisibility(View.GONE);
                uploaded_imagae_com.setImageBitmap(thumbnail);
                Toast.makeText(AddComplex.this, "Image Saved!", Toast.LENGTH_SHORT).show();
            }
            catch (Exception e){

            }
        }

    }

    private Uri getImageUri(Context inContext, Bitmap thumbnail, String imageTempName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), thumbnail, imageTempName, null);
        return Uri.parse(path);
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

    private void takePhotoFromCamera() {
        try {
            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, CAMERA);
        }
        catch (Exception e){

        }
    }

    private void choosePhotoFromGallary(int which) {
        try {
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        }
        catch (Exception e){

        }
    }

    private void requestGroupPermission(ArrayList<String> permissions) {
        String[] permissionList =new String[permissions.size()];
        permissions.toArray(permissionList);

        ActivityCompat.requestPermissions(AddComplex.this,permissionList,REQUEST_GROUP_PERMISSION);
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
                        /*
                             Issue a request to the Places Geo Data API to retrieve a Place object with additional details about the place.
                         */

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

                                try {
                                    Intent data = new Intent();
                                    data.putExtra("lat", String.valueOf(places.get(0).getLatLng().latitude));
                                    data.putExtra("lng", String.valueOf(places.get(0).getLatLng().longitude));
                                    setResult(AddComplex.RESULT_OK, data);
//                            Toast.makeText(getApplicationContext(), " "+places.get(0).getAddress(), Toast.LENGTH_SHORT).show();
                                    hideSoftInput(AddComplex.this);
                                    mSearchEdittext.setText(places.get(0).getAddress());
                                    mRecyclerView.setVisibility(View.GONE);

                                    lin_map_com.setVisibility(View.VISIBLE);
                                    lin_details_complex.setVisibility(View.VISIBLE);

                                    add = mSearchEdittext.getText().toString().trim();

                                    getMap();
                                }
                                catch (Exception e){

                                }

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

            }

        }
    }

    public static void hideSoftInput(Activity activity) {
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        try {
            mMap = googleMap;
        }
        catch (Exception e){

        }
    }
}
