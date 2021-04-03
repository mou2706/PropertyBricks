package com.openwebsolutions.propertybricks.TenantEditActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.Model.AddTenantModel;
import com.openwebsolutions.propertybricks.Model.Tenants_Details.Data;
import com.openwebsolutions.propertybricks.Model.Tenants_Details.TenantDetailsPerProductId;
import com.openwebsolutions.propertybricks.Property_Details_Page.PropertyDetailsActivity;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.ShowTenants.Tenants;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TenantEdit extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemSelectedListener{
    ProgressBar progressBar;

    int flag=0;
    ImageView iv_tenantedit_back;

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    ImageView img_tenant,img_tenant_change,iv_bus_date_edit;
    LinearLayout lin_business_tenant;
    EditText et_tenant_name_edit,et_tenant_phone_edit,et_tenant_email_edit,et_tenant_address_edit,et_tenant_billing_address,business_tenant_company_gstn;
    TextView tv_tenant_date_edit,tv_tenant_submit;

    String gstin="";
    String billing_address="";
    String tenants_id_string="";
    String property_id_string="";
    String flagg_add=null;

    int tenant_id=0;
    int property_id=0;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String device_token;

    String email=null;
    String password=null;

    String path="";
    String imageTempName;
    Uri tempUri;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    Spinner spinner;
    String text="";
    String[] spinnerItems = new String[]{
            "Select Month(s)",
            "1",
            "3",
            "6",
            "12"
    };
    List<String> spinnerlist;
    ArrayAdapter<String> arrayadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_edit);

        init();

        flagg_add=getIntent().getStringExtra("flagg_add");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           gstin = extras.getString("Gstin");
           billing_address=extras.getString("Billing_address");
           tenants_id_string=extras.getString("tenant_id");
            property_id_string=extras.getString("PropertyID");

            if(!tenants_id_string.equals("")){
                property_id=Integer.valueOf(property_id_string);
                tenant_id=Integer.valueOf(tenants_id_string);
                getTenantsDetails_Persnal(tenant_id);
            }
        }
        init_spinner();
    }

    private void init_spinner() {

        spinnerlist = new ArrayList<>(Arrays.asList(spinnerItems));

        arrayadapter = new ArrayAdapter<String>(TenantEdit.this,android.R.layout.simple_spinner_dropdown_item,spinnerlist){
            @Override
            public boolean isEnabled(int position){
                if(position == 0)
                {
                    //Disable the  item of spinner.
                    return false;
                }
                else
                {
                    return true;
                }
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent)
            {
                View spinnerview = super.getDropDownView(position, convertView, parent);

                TextView spinnertextview = (TextView) spinnerview;

                if(position == 0) {

                    //Set the disable spinner item color fade .
                    spinnertextview.setTextColor(Color.parseColor("#bcbcbb"));
                }
                else {

                    spinnertextview.setTextColor(Color.BLACK);

                }
                return spinnerview;
            }
        };

        arrayadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayadapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void getTenantsDetails_Persnal(int tenant_id) {
        token=sh.getString("_Token","");

        try {

            MainApplication.apiManager.getTenantUser_id(tenant_id, "Bearer" + " " + token, new Callback<TenantDetailsPerProductId>() {
                @Override
                public void onResponse(Call<TenantDetailsPerProductId> call, Response<TenantDetailsPerProductId> response) {

                    TenantDetailsPerProductId responseUser = response.body();
                    if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                        try {
//                            flag=2;
                            Data data = responseUser.getData();
                            et_tenant_name_edit.setText(data.getTenantOwnerName());
                            et_tenant_phone_edit.setText(data.getTenantOwnerPhone());
                            et_tenant_email_edit.setText(data.getTenantOwnerEmail());
                            et_tenant_address_edit.setText(data.getTenantOwnerAddress());
                            tv_tenant_date_edit.setText(data.getTenantOwnerEntrydate());

                            Picasso.get().load(AppData.image_url_tenant + data.getImage()).resize(600, 200).into(img_tenant);

                            int payment_cycle=data.getTenantOwnerPaymentcycle();
                            if(payment_cycle==1){
                                spinner.setSelection(1);
                            }
                            else if(payment_cycle==3){
                                spinner.setSelection(2);
                            }
                            else if(payment_cycle==6){
                                spinner.setSelection(3);
                            }
                            else if(payment_cycle==12){
                                spinner.setSelection(4);
                            }

                            String b_add=data.getBillingAddress();
                            String gstn=data.getCompGstin();
                            if(!b_add.equals("") && !gstn.equals("")){
                                lin_business_tenant.setVisibility(View.VISIBLE);
                                et_tenant_billing_address.setText(data.getBillingAddress());
                                business_tenant_company_gstn.setText(data.getCompGstin());
                            }
                        }
                        catch (Exception e) {

                            Log.d("Error", "eroor");
                        }

                    } else {
//                        Toast.makeText(TenantEdit.this, "Something wrong" + response.message(), Toast.LENGTH_LONG).show();
                        switch (response.code()) {
                            case 401:
                                Toast.makeText(TenantEdit.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                Intent in =new Intent(TenantEdit.this, SigninActivity.class);
                                startActivity(in);
                                break;
                            default:
                                Toasty.error(TenantEdit.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                                break;
                        }

                    }
                }

                @Override
                public void onFailure(Call<TenantDetailsPerProductId> call, Throwable t) {
                    Toast.makeText(TenantEdit.this, "Internal Error", Toast.LENGTH_LONG).show();

                }
            });
        }
        catch (Exception e){

        }
    }

    private void init() {
        iv_tenantedit_back=findViewById(R.id.iv_tenantedit_back);
        iv_tenantedit_back.setOnClickListener(this);
        tv_tenant_submit=findViewById(R.id.tv_tenant_submit);
        iv_bus_date_edit=findViewById(R.id.iv_bus_date_edit);
        progressBar = findViewById(R.id.spinkit3_vacant_business_edit);
        spinner=findViewById(R.id.payment_spinner_tenant_edit);

        lin_business_tenant=findViewById(R.id.lin_business_tenant);
        et_tenant_name_edit=findViewById(R.id.et_tenant_name_edit);
        et_tenant_phone_edit=findViewById(R.id.et_tenant_phone_edit);
        et_tenant_email_edit=findViewById(R.id.et_tenant_email_edit);
        et_tenant_address_edit=findViewById(R.id.et_tenant_address_edit);
        et_tenant_billing_address=findViewById(R.id.et_tenant_billing_address);
        business_tenant_company_gstn=findViewById(R.id.business_tenant_company_gstn);
        tv_tenant_date_edit=findViewById(R.id.tv_tenant_date_edit);
        img_tenant=findViewById(R.id.img_tenant);
        img_tenant_change=findViewById(R.id.img_tenant_change);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
        device_token=sh.getString("device_token","");
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            LoginCheck loginCheck = new LoginCheck(this);
            loginCheck.getLoginCheck(email, password,device_token);
        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
        img_tenant_change.setOnClickListener(this);
        img_tenant.setOnClickListener(this);
        iv_bus_date_edit.setOnClickListener(this);
        tv_tenant_date_edit.setOnClickListener(this);

        tv_tenant_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_tenantedit_back:
                super.onBackPressed();
                TenantEdit.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
                break;
            case R.id.img_tenant_change:
                showPictureDialog();
                break;

            case R.id.img_tenant:
                showPictureDialog();
                break;

            case R.id.iv_bus_date_edit:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(TenantEdit.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_tenant_date_edit.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;
            case R.id.tv_tenant_date_edit:

                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(TenantEdit.this,R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_tenant_date_edit.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
                break;
            case R.id.tv_tenant_submit:

                    if (valid()) {

                        File file = new File(path);
                        if(!path.equals("")){

                            addTenant_ID_Path();
                        }
                        else{

                            addTenant_ID();
                        }
                    }
                break;
        }
    }

    private void addTenant_ID() {
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_tenant_name_edit.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), et_tenant_address_edit.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), et_tenant_email_edit.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), et_tenant_phone_edit.getText().toString());
        RequestBody entry_date = RequestBody.create(MediaType.parse("text/plain"), tv_tenant_date_edit.getText().toString());
        RequestBody payment_cycle = RequestBody.create(MediaType.parse("text/plain"), text);
        RequestBody billing_add = RequestBody.create(MediaType.parse("text/plain"), et_tenant_billing_address.getText().toString());
        RequestBody comp_gstn = RequestBody.create(MediaType.parse("text/plain"), business_tenant_company_gstn.getText().toString());
        RequestBody propertyid = RequestBody.create(MediaType.parse("text/plain"), property_id_string);

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

            token = sh.getString("_Token", "");
            try{
                MainApplication.apiManager.addtenantUserByID(tenant_id,"Bearer" + " " + token, name, phone, email,location,entry_date,payment_cycle,billing_add,comp_gstn,propertyid, new Callback<AddTenantModel>() {
                    @Override
                    public void onResponse(Call<AddTenantModel> call, Response<AddTenantModel> response) {

                        AddTenantModel responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit2_tenant_edit);
                            progressBar.setVisibility(View.VISIBLE);
                            tv_tenant_submit.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_tenant_submit.setText("Submit");
                                    progressBar.setVisibility(View.GONE);
                                    try {
                                        if (flagg_add != null && flagg_add.equalsIgnoreCase("8013")) {
                                            Intent in = new Intent(TenantEdit.this, PropertyDetailsActivity.class);
                                            startActivity(in);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        } else {
                                            Intent in = new Intent(TenantEdit.this, Tenants.class);
                                            startActivity(in);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        }
                                    }
                                    catch (Exception e){

                                        Log.d("Error",""+e.getMessage());
                                    }
                                }
                            }, 2000);
                        } else {

                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(TenantEdit.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(TenantEdit.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Log.d("Response error: ",""+  responseUser.getMessage());
                                    break;
                            }


                        }
                    }

                    @Override
                    public void onFailure(Call<AddTenantModel> call, Throwable t) {
                        Toast.makeText(TenantEdit.this,
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
            Toast.makeText(TenantEdit.this,"No internet",Toast.LENGTH_SHORT).show();
        }

    }

    private void addTenant_ID_Path() {
        File file = new File(path);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), et_tenant_name_edit.getText().toString());
        RequestBody location = RequestBody.create(MediaType.parse("text/plain"), et_tenant_address_edit.getText().toString());
        RequestBody email = RequestBody.create(MediaType.parse("text/plain"), et_tenant_email_edit.getText().toString());
        RequestBody phone = RequestBody.create(MediaType.parse("text/plain"), et_tenant_phone_edit.getText().toString());
        RequestBody entry_date = RequestBody.create(MediaType.parse("text/plain"), tv_tenant_date_edit.getText().toString());
        RequestBody payment_cycle = RequestBody.create(MediaType.parse("text/plain"), text);
        RequestBody billing_add = RequestBody.create(MediaType.parse("text/plain"), et_tenant_billing_address.getText().toString());
        RequestBody comp_gstn = RequestBody.create(MediaType.parse("text/plain"), business_tenant_company_gstn.getText().toString());
        RequestBody propertyid = RequestBody.create(MediaType.parse("text/plain"), property_id_string);


        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            token = sh.getString("_Token", "");
            try{
                MainApplication.apiManager.addtenantUserByID_Path(tenant_id,"Bearer" + " " + token, fileToUpload,  name, phone, email,location,entry_date,payment_cycle,billing_add,comp_gstn,propertyid, new Callback<AddTenantModel>() {
                    @Override
                    public void onResponse(Call<AddTenantModel> call, Response<AddTenantModel> response) {

                        AddTenantModel responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {
//                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
                            final ProgressBar progressBar = (ProgressBar) findViewById(R.id.spin_kit2_tenant_edit);
                            progressBar.setVisibility(View.VISIBLE);
                            tv_tenant_submit.setText("");
                            CubeGrid cubeGrid = new CubeGrid();
                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    tv_tenant_submit.setText("Submit");
                                    progressBar.setVisibility(View.GONE);
                                    try {
                                        if (flagg_add != null && flagg_add.equalsIgnoreCase("8013")) {
                                            Intent in = new Intent(TenantEdit.this, PropertyDetailsActivity.class);
                                            startActivity(in);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        } else {
                                            Intent in = new Intent(TenantEdit.this, Tenants.class);
                                            startActivity(in);
                                            overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                            finish();
                                        }
                                    }
                                    catch (Exception e){
                                        Log.d("Error",""+e.getMessage());
                                    }
                                }
                            }, 2000);
                        } else {
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(TenantEdit.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(TenantEdit.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Log.d("Response error: ",""+  responseUser.getMessage());
                                    break;
                            }

                        }
                    }

                    @Override
                    public void onFailure(Call<AddTenantModel> call, Throwable t) {
                        Toast.makeText(TenantEdit.this,
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
            Toast.makeText(TenantEdit.this,"No internet",Toast.LENGTH_SHORT).show();
        }

    }

    private boolean valid() {
        boolean isvalid=true;
        if(et_tenant_name_edit.getText().toString().isEmpty()){
            isvalid=false;
            Toast.makeText(TenantEdit.this,"Enter name",Toast.LENGTH_LONG).show();
            et_tenant_name_edit.requestFocus();
            return isvalid;
        }
        if(et_tenant_phone_edit.getText().toString().isEmpty()){
            isvalid=false;
            Toast.makeText(TenantEdit.this,"Enter phone number",Toast.LENGTH_LONG).show();
            et_tenant_phone_edit.requestFocus();
            return isvalid;
        }
        if(et_tenant_email_edit.getText().toString().isEmpty()){
            isvalid=false;
            Toast.makeText(TenantEdit.this,"Enter email id",Toast.LENGTH_LONG).show();
            et_tenant_email_edit.requestFocus();
            return isvalid;
        }
        if(et_tenant_address_edit.getText().toString().isEmpty()){
            isvalid=false;
            Toast.makeText(TenantEdit.this,"Enter address",Toast.LENGTH_LONG).show();
            et_tenant_address_edit.requestFocus();
            return isvalid;
        }
        if(text==""){
            isvalid=false;
            Toast.makeText(TenantEdit.this,"Choose payment cycle",Toast.LENGTH_LONG).show();
            return isvalid;
        }
        if(tv_tenant_date_edit.getText().toString().isEmpty()){
            isvalid=false;
            Toast.makeText(TenantEdit.this,"Select entry date",Toast.LENGTH_LONG).show();
            return isvalid;
        }

        if(lin_business_tenant.getVisibility()==View.VISIBLE){
            if(et_tenant_billing_address.getText().toString().isEmpty()  ) {
                isvalid = false;
                Toast.makeText(TenantEdit.this, "Enter billing address", Toast.LENGTH_LONG).show();
                return isvalid;
            }
            if(business_tenant_company_gstn.getText().toString().isEmpty()) {
                isvalid = false;
                Toast.makeText(TenantEdit.this, "Enter GSTIN", Toast.LENGTH_LONG).show();
                return isvalid;
            }
        }


        return isvalid;

    }

    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(TenantEdit.this);
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
        if (resultCode == TenantEdit.this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            int position;

            if (data != null) {

                final Uri contentURI = data.getData();
                try {

                    progressBar.setVisibility(View.VISIBLE);
                    CubeGrid cubeGrid = new CubeGrid();
                    progressBar.setIndeterminateDrawable(cubeGrid);

                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(TenantEdit.this.getContentResolver(), contentURI);
                    path=getRealPathFromURI(TenantEdit.this,contentURI);
                    final int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

                    Picasso.get().load(contentURI).resize(size,size).into(img_tenant, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });



                    img_tenant.setDrawingCacheEnabled(true);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(TenantEdit.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }

        else if (requestCode == CAMERA) {
            progressBar.setVisibility(View.VISIBLE);
            CubeGrid cubeGrid = new CubeGrid();
            progressBar.setIndeterminateDrawable(cubeGrid);
            final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            tempUri = getImageUri(TenantEdit.this, thumbnail,imageTempName);
            path=getRealPathFromURI(TenantEdit.this,tempUri);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);

                    img_tenant.setImageBitmap(thumbnail);
                }
            },1000);

            img_tenant.setDrawingCacheEnabled(true);
        }


    }

    private Uri getImageUri(Context inContext, Bitmap thumbnail, String imageTempName) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), thumbnail, imageTempName, null);
        return Uri.parse(path);
    }

    private String getRealPathFromURI(FragmentActivity context, Uri uri) {
        Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":")+1);
        cursor.close();

        cursor = context.getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        TenantEdit.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
        finish();
    }
}
