package com.openwebsolutions.propertybricks;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.Adapter.BillingListAdapter;
import com.openwebsolutions.propertybricks.Adapter.RecyclerAdapter_invoice;
import com.openwebsolutions.propertybricks.AddPropertyActivity.AddProperty;
import com.openwebsolutions.propertybricks.AddPropertyActivity.SubmitYourProperty;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.Model.AddSubmitProperty;
import com.openwebsolutions.propertybricks.Model.BillingModel;
import com.openwebsolutions.propertybricks.Model.GetComplex_ByID.ComplexEdit;
import com.openwebsolutions.propertybricks.Model.GetInvoiceModel.GetInvoiceModel;
import com.openwebsolutions.propertybricks.Model.UpdateInvoice.UpdateInvoiceModel;
import com.openwebsolutions.propertybricks.Occupied_Activity.Occupied_Activity;
import com.openwebsolutions.propertybricks.Property_Details_Page.PropertyDetailsActivity;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;
import com.openwebsolutions.propertybricks.Vacant_Activity.Vacant_Activity;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InvoiceActivity extends AppCompatActivity {
    Spinner spinner;

//    ListView listView;
    int tenant_id=0;
    String tenants_id_string="";
    String image="";



    BillingListAdapter billingListAdapter;
    RecyclerAdapter_invoice recyclerAdapter_invoice;
    String[] month;
    SharedPreferences sh;
    String token = "";

    SharedPreferences.Editor editor;
    String email=null;
    String password=null;
    ImageView iv_showtenant_back;

    RecyclerView invoice_recycler;
    LinearLayoutManager layoutManager;
    ArrayList<BillingModel> arrayList = new ArrayList<>();

    ArrayList<String> monthsName = new ArrayList<>();

    private static final int TXT_CAMERA = 1;
    private static final int TXT_STORAGE = 2;
    private static final int REQUEST_GROUP_PERMISSION = 425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    ProgressBar progressBar;
    TextView txt_uploading, txt_successfull_personal,status;
    Bitmap _bitmap;
    Bitmap _bitmap_bus;
    RelativeLayout img_rel_b;
    TextView browse,tv_paid;


    String path = "";
    String imageTempName;
    String property_image="";

    Uri tempUri;
    RelativeLayout rel_add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);


//        listView = findViewById(R.id.list1);
//        spinner = findViewById(R.id.spinner1);
        invoice_recycler=findViewById(R.id.invoice_recycler);
        rel_add=findViewById(R.id.rel_add);

        browse=findViewById(R.id.browse);
        status=findViewById(R.id.status);
        img_rel_b=findViewById(R.id.img_rel_b);
        tv_paid=findViewById(R.id.tv_paid);
        browse=findViewById(R.id.browse);

//        progressBar = findViewById(R.id.spin_kit3_business);
        txt_uploading = findViewById(R.id.txt_uploading_bus);
        txt_successfull_personal=findViewById(R.id.txt_successfull_upload);

//        progressBar = findViewById(R.id.spin_kit3_business);
        txt_uploading = findViewById(R.id.txt_uploading_bus);
        txt_successfull_personal=findViewById(R.id.txt_successfull_upload);
//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            image = extras.getString("image");
//
//
//            if(!image.equals("")){
//                rel_add.setVisibility(View.GONE);
//            }
//        }



        iv_showtenant_back=findViewById(R.id.iv_showtenant_back);
        iv_showtenant_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



                startActivity(new Intent(InvoiceActivity.this,MainActivity.class));
                finish();
            }
        });


        init();

        Log.d("month",""+"fjffffffffffffffffm");

        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

//            if(!tenants_id_string.equals("")){
//                tenant_id=Integer.valueOf(tenants_id_string);
//                getPaidMonth(tenant_id);
//            }
            Log.d("month",""+"fjffffffffffffffffm");
            getPaidMonth();


        }




    }




    private void init() {
        tv_paid=findViewById(R.id.tv_paid);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
//        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            String device_token=sh.getString("device_token","");
//
//            LoginCheck loginCheck = new LoginCheck(this);
//            loginCheck.getLoginCheck(email, password,device_token);
        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }


    private void getPaidMonth() {
        token=sh.getString("_Token","");
        try {

            MainApplication.apiManager.getinvoicedata(AppData.tenant_id, "Bearer" + " " + token, new Callback<GetInvoiceModel>() {
                @Override
                public void onResponse(Call<GetInvoiceModel> call, Response<GetInvoiceModel> response) {

                    GetInvoiceModel responseUser = response.body();

                    if (response.isSuccessful() && responseUser != null) {
                        Log.d("month", "" + arrayList);

//                   Toast.makeText(getApplicationContext(),"Successfull"+response,Toast.LENGTH_LONG).show();
                        try {

                            arrayList = responseUser.getData();
                            Toast.makeText(getApplicationContext(), "Successfull" + responseUser.getData(), Toast.LENGTH_LONG).show();
                            final int i = 0;

                            if (arrayList.size() != 0) {


                                recyclerAdapter_invoice = new RecyclerAdapter_invoice(arrayList, InvoiceActivity.this);
                                layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                                invoice_recycler.setLayoutManager(layoutManager);
                                invoice_recycler.setItemAnimator(new DefaultItemAnimator());
                                invoice_recycler.setAdapter(recyclerAdapter_invoice);


                                recyclerAdapter_invoice.SetOnItemClickListener(new RecyclerAdapter_invoice.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {
                                        if (view.getId() == R.id.tv_paid) {
                                            tv_paid=findViewById(R.id.tv_paid);
                                            tv_paid.setVisibility(View.GONE);

                                            DialogBox();
//                                        AppData.tenant_id=Integer.valueOf(arrayList.get(position).getTenant().getId());
//                                        DialogBox();
//                                        AppData.tenant_id = arrayList.get(position).getId();
                                        }

//                                    if (view.getId() == R.id.tv_vacant){
//                                        AppData.position=position;
//                                        Intent intent = new Intent(getApplicationContext(), Vacant_Activity.class);
//                                        AppData.property_id=arrayList.get(position).getId();
//                                        Bundle bundle = ActivityOptions.makeCustomAnimation(getApplicationContext(), R.anim.slide_in, R.anim.slide_out).toBundle();
//                                        startActivity(intent,bundle);
//                                    }


//
                                    }

                                    @Override
                                    public void onItemLongClick(View view, final int position) {

                                        if (view.getId() == R.id.linear_item) {
//                            tenant_id=Integer.valueOf(arrayList.get(position).getTenant().getId());
                                            DialogBox();
                                            AppData.tenant_id = arrayList.get(position).getId();
                                        }
                                    }


                                });


//
                            }

                        } catch (Exception e) {
                            Log.d("month", "" + "fjffffffffffffffffm");
                        }
                    } else {


                        switch (response.code()) {
                            case 401:

                                Toast.makeText(getApplicationContext(), "Token Expired", Toast.LENGTH_SHORT).show();
                                Intent in = new Intent(getApplicationContext(), SigninActivity.class);
                                startActivity(in);
                                break;
                            default:
                                Toast.makeText(getApplicationContext(),
                                        "Already exists"
                                        , Toast.LENGTH_LONG).show();
                                break;
                        }

//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<GetInvoiceModel> call, Throwable t) {
                    try {

                        Toast.makeText(getApplicationContext(), "Internal Error", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {

                        Log.d("month", "" + "fjffffffffffffffffm");
                    }
                }
            });
        }
        catch (Exception e)
        {

        }
    }
    private void DialogBox() {
        final Dialog dialog=new Dialog(InvoiceActivity.this);
        dialog.setContentView(R.layout.receipt_dialog);
        LinearLayout apply=dialog.findViewById(R.id.apply);
        RelativeLayout rel_remove=dialog.findViewById(R.id.rel_cancel);
        RelativeLayout rel_pic_upload=dialog.findViewById(R.id.rel_pic_upload);
         final TextView browse=dialog.findViewById(R.id.browse);
//        RelativeLayout rel_remove_tenant=dialog.findViewById(R.id.rel_remove_tenant);
//        if(tenant_id!=0) {
//
          rel_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                browse.setText("Paid");


//                rel_add.setVisibility(View.GONE);
                changetext();



            }


        });

        rel_pic_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkpermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(getApplicationContext(), permission) != PackageManager.PERMISSION_GRANTED)
                                permissionNeeded.add(permission);
                        }
                        requestGroupPermission(permissionNeeded);
                        showPictureDialog();
                    }
                }
                else {
                    showPictureDialog();
                }

            }

            private void showPictureDialog() {
                android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(InvoiceActivity.this);
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




                        });
                pictureDialog.show();
            }
            private void requestGroupPermission(ArrayList<String> permissions) {
                String[] permissionList =new String[permissions.size()];
                permissions.toArray(permissionList);

                ActivityCompat.requestPermissions(InvoiceActivity.this,permissionList,REQUEST_GROUP_PERMISSION);
            }
            private int checkpermission(int permission) {
                int status= PackageManager.PERMISSION_DENIED;

                switch (permission){

                    case TXT_CAMERA:
                        status= ContextCompat.checkSelfPermission(InvoiceActivity.this, Manifest.permission.CAMERA);
                        break;
                    case TXT_STORAGE:
                        status= ContextCompat.checkSelfPermission(InvoiceActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        break;

                }
                return status;
            }


        });



//        }

//        rel_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//
//                pd.setMessage("loading...");
//                pd.setCancelable(false);
//                pd.show();
//
//                Handler handler=new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//
//                        Intent intent = new Intent(getActivity(), AddProperty.class);
//                        intent.putExtra("property_id_edit",String.valueOf(arrayList.get(pos).getId()));
//                        Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.fade_in, R.anim.fade_out).toBundle();
//                        startActivity(intent,bundle);
//                    }
//                },2000);
//            }
//        });
//        rel_remove.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });

        dialog.setCancelable(true);
        dialog.show();
    }
    private void changetext() {

            File file = new File(path);
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file.getName(), requestBody);

            ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                    || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                token=sh.getString("_Token","");

                MainApplication.apiManager.update_invoice(AppData.id,"Bearer"+" "+token,fileToUpload,new Callback<UpdateInvoiceModel>() {
                    @Override
                    public void onResponse(Call<UpdateInvoiceModel> call, Response<UpdateInvoiceModel> response) {

                        UpdateInvoiceModel responseUser = response.body();
                        Log.d("month",""+"fjffffffffffffffffm");

                        if (response.isSuccessful() && responseUser != null) {
                            Log.d("month",""+"fjffffffffffffffffm");
                            arrayList=responseUser.getData();

//                                            editor.putString("profile_image", name_edit.getText().toString());


////                                        Toast.makeText(AddProperty.this, responseUser.getMessage(), Toast.LENGTH_SHORT).show();
//                            final ProgressBar progressBar = (ProgressBar)findViewById(R.id.spin_kit3_pro);
//                            progressBar.setVisibility(View.VISIBLE);
//                            submit_pro.setText("");
//                            CubeGrid cubeGrid = new CubeGrid();
//                            progressBar.setIndeterminateDrawable(cubeGrid);

                            Handler handler=new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

//                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(InvoiceActivity.this, "Successfully added", Toast.LENGTH_SHORT).show();
                                    Intent in = new Intent(InvoiceActivity.this, InvoiceActivity.class);
                                    in.putExtra("image",image);

                                    startActivity(in);
                                    overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                                    finish();
                                }
                            },2000);
                        } else {
                            Toast.makeText(InvoiceActivity.this,
                                    "Response error: "+responseUser.getMessage()
                                    , Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<UpdateInvoiceModel> call, Throwable t) {
                        Toast.makeText(InvoiceActivity.this,
                                "Error is " + t.getMessage()
                                , Toast.LENGTH_LONG).show();
                        Log.d("Error is",t.getMessage());
                    }
                });
            }







    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);



        if (resultCode == InvoiceActivity.this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            int position;

            if (data != null) {

                Uri contentURI = data.getData();
                try {
                    _bitmap = MediaStore.Images.Media.getBitmap(InvoiceActivity.this.getContentResolver(), contentURI);
                    path=getRealPathFromURI(InvoiceActivity.this,contentURI);
                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

//                    progressBar.setVisibility(View.VISIBLE);
//                    browse.setText("");
//                    txt_uploading.setVisibility(View.VISIBLE);
//                    CubeGrid cubeGrid = new CubeGrid();
//                    progressBar.setIndeterminateDrawable(cubeGrid);



                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {

//                            progressBar.setVisibility(View.GONE);
//                            txt_uploading.setVisibility(View.GONE);
//                            browse.setVisibility(View.INVISIBLE);
//                            txt_successfull_personal.setVisibility(View.VISIBLE);

//                            img_rel_b.setVisibility(View.VISIBLE);
//                            iv_personal_pic_b.setVisibility(View.VISIBLE);
//                            iv_personal_pic_b.setImageBitmap(_bitmap_bus);
                        }

                    },2000);

//                    Picasso.get().load(contentURI).resize(size,size).into(uploaded_imagae);
//                    Toast.makeText(AddProperty.this, "Image Saved!", Toast.LENGTH_SHORT).show();
//                    startActivity(new Intent(InvoiceActivity.this,InvoiceActivity.class));


                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(InvoiceActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }

            }


        }

        else if (requestCode == CAMERA) {
            try {
                _bitmap = (Bitmap) data.getExtras().get("data");
                tempUri = getImageUri(InvoiceActivity.this, _bitmap, imageTempName);
                path = getRealPathFromURI(InvoiceActivity.this, tempUri);
//                progressBar.setVisibility(View.VISIBLE);
//                browse.setText("");
//                txt_uploading.setVisibility(View.VISIBLE);
                CubeGrid cubeGrid = new CubeGrid();
                progressBar.setIndeterminateDrawable(cubeGrid);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//                        progressBar.setVisibility(View.GONE);
//                        txt_uploading.setVisibility(View.GONE);
//                        browse.setVisibility(View.GONE);
//                        txt_successfull_personal.setVisibility(View.VISIBLE);

//                        img_rel_b.setVisibility(View.VISIBLE);

//                        iv_personal_pic_b.setVisibility(View.VISIBLE);
//                        iv_personal_pic_b.setImageBitmap(_bitmap_bus);
                    }
                }, 2000);
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


}

