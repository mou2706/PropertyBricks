package com.openwebsolutions.propertybricks.Profile_Update;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.UpdateUserDemo.UpdateUser;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdate extends AppCompatActivity implements View.OnClickListener {

    ImageView iv_back_profile,iv_more,iv_personal_pic_edit;
    EditText name_edit,phone_edit,email_edit,address_edit,gstin_edit;
    TextView tv_update;
    RelativeLayout rel_update;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String property_image="";
    String user_name="";
    String user_email="";
    String user_phn="";

    String user_gstin="";
    String user_address="";
    String device_token="";
    String id="";

    String email=null;
    String password=null;

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
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String email1=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_update);


        init();

        property_image=sh.getString("profile_image","");
        user_name=sh.getString("user_name","");
        user_email=sh.getString("user_email","");
        user_phn=sh.getString("user_phn","");
        user_gstin=sh.getString("user_gstin","");
        user_address=sh.getString("user_address","");

        name_edit.setText(user_name);
        phone_edit.setText(user_phn);
        email_edit.setText(user_email);
        address_edit.setText(user_address);

//        if(property_image!=null && !property_image.equals("")){
//            Picasso.get().load(AppData.image_url_user + property_image).into(iv_personal_pic_edit);
//        }
        if(user_gstin!=null && !user_gstin.equals("0")){
            gstin_edit.setVisibility(View.VISIBLE);
            gstin_edit.setText(user_gstin);
        }
    }

    private void init() {

        rel_update=findViewById(R.id.rel_update);
        iv_personal_pic_edit=findViewById(R.id.iv_personal_pic_edit);

        iv_back_profile=findViewById(R.id.iv_back_profile);
        iv_more=findViewById(R.id.iv_more);
        name_edit=findViewById(R.id.name_edit);
        phone_edit=findViewById(R.id.phone_edit);
        email_edit=findViewById(R.id.email_edit);
        address_edit=findViewById(R.id.address_edit);
        gstin_edit=findViewById(R.id.gstin_edit);
        tv_update=findViewById(R.id.tv_update);
        tv_update.setOnClickListener(this);

        iv_back_profile.setOnClickListener(this);
        iv_more.setOnClickListener(this);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
        device_token=sh.getString("device_token","");
        token = sh.getString("_Token", "");


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

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_back_profile:
                super.onBackPressed();
                ProfileUpdate.this.overridePendingTransition(R.anim.left_to_right,
                        R.anim.right_to_left);
                finish();
                break;
            case R.id.iv_more:
                getmenu_option();
                break;

            case R.id.iv_personal_pic_edit:
                if(checkpermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(ProfileUpdate.this, permission) != PackageManager.PERMISSION_GRANTED)
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
            case R.id.tv_update:
                email1=email_edit.getText().toString().trim();
                SharedPreferences sh1=getSharedPreferences("user", Context.MODE_PRIVATE);
                device_token=sh1.getString("device_token","");



                if(valid()){

                    File file = new File(path);
                    if (path.equals("")) {
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);

                        final RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name_edit.getText().toString().trim());
                        RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone_edit.getText().toString().trim());
                        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email_edit.getText().toString().trim());
                        RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address_edit.getText().toString().trim());
                        RequestBody image = RequestBody.create(MediaType.parse("text/plain"), property_image);
                        RequestBody gstin = RequestBody.create(MediaType.parse("text/plain"), gstin_edit.getText().toString().trim());
                       final RequestBody device_token1 = RequestBody.create(MediaType.parse("text/plain"), device_token);
                       final RequestBody id1 = RequestBody.create(MediaType.parse("text/plain"), id);



                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            token = sh.getString("_Token", "");


                            try{

                                MainApplication.apiManager.update_user_wtoutid("Bearer"+" "+token,name1,phone1,email1,address1,gstin,id1,image, new Callback<UpdateUser>() {
                                    @Override
                                    public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {

                                        UpdateUser responseUser = response.body();
                                        try {


                                            if (response.isSuccessful() && responseUser != null) {

                                                Toasty.success(ProfileUpdate.this, "Successfully edited", Toasty.LENGTH_SHORT, true).show();
//                                            sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                editor = sh.edit();
                                                editor.putString("_Token", token);
                                                editor.putString("Email_id", email);
                                                editor.putString("Password", password);
                                                editor.putString("device_token", device_token);
//
//
                                                editor.putString("profile_image", property_image);
//                                            editor.putString("profile_image", name_edit.getText().toString());
                                                editor.putString("user_name", name_edit.getText().toString());
//                                            editor.putString("user_email",user_address);
//                                            editor.putString("user_phn",user_phn);
//                                            editor.putString("user_address",user_address);
//                                            editor.putString("user_email",responseUser.getUser().getEmail());
                                            editor.putString("user_phn",phone_edit.getText().toString());
                                            editor.putString("user_gstin",gstin_edit.getText().toString());
                                            editor.putString("user_address",address_edit.getText().toString());
                                                editor.commit();

//                                                    Intent intent=new Intent(ProfileUpdate.this, MainActivity.class);
//                                                    intent.putExtra("profile_image",property_image);
//                                                    intent.putExtra("user_name",user_name);
                                                Intent intent = new Intent(ProfileUpdate.this, MainActivity.class);
//                                                    Bundle bundle = ActivityOptions.makeCustomAnimation(ProfileUpdate.this, R.anim.fade_in, R.anim.fade_out).toBundle();
//                                                    startActivity(intent,bundle);


                                                startActivity(intent);
                                                finish();


                                            } else {
//                                            Toasty.error(ProfileUpdate.this,
//                                                    "Email id already exists"
//                                                    , Toast.LENGTH_LONG).show();
//                                            switch (response.code()) {
//                                                case 401:
//                                                    Toast.makeText(ProfileUpdate.this, "Token Expired", Toast.LENGTH_SHORT).show();
//                                                    Intent in =new Intent(ProfileUpdate.this, MainActivity.class);
//                                                    startActivity(in);
//                                                    break;
//                                                default:
                                                Toast.makeText(ProfileUpdate.this,
                                                        "Email id already exists"
                                                        , Toast.LENGTH_LONG).show();
//                                                    break;
//                                            }

//                                            Log.v("Error code 400", response.errorBody().toString());
                                            }
                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateUser> call, Throwable t) {

                                        Toast.makeText(ProfileUpdate.this,
                                                "Error is " + t.getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                });

                            }catch(Exception e){

                            }
                        }
                        else {
                            Toast.makeText(ProfileUpdate.this, "Please check Internet connection", Toast.LENGTH_LONG).show();

                        }
                    }
                    else {

                        File file1 = new File(path);
                        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file1);

                        final MultipartBody.Part fileToUpload = MultipartBody.Part.createFormData("image", file1.getName(), requestBody);
                        RequestBody name1 = RequestBody.create(MediaType.parse("text/plain"), name_edit.getText().toString().trim());
                        RequestBody phone1 = RequestBody.create(MediaType.parse("text/plain"), phone_edit.getText().toString().trim());
                        RequestBody email1 = RequestBody.create(MediaType.parse("text/plain"), email_edit.getText().toString().trim());
                        RequestBody address1 = RequestBody.create(MediaType.parse("text/plain"), address_edit.getText().toString().trim());
                        RequestBody id1 = (RequestBody) RequestBody.create(MediaType.parse("text/plain"), id);
                        final RequestBody device_token1 = RequestBody.create(MediaType.parse("text/plain"), device_token);


                        RequestBody gstin = RequestBody.create(MediaType.parse("text/plain"), gstin_edit.getText().toString().trim());

                        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
                            token = sh.getString("_Token", "");



                            try{

                                MainApplication.apiManager.update_user("Bearer"+" "+token,name1,phone1,email1,address1,gstin,id1,fileToUpload, new Callback<UpdateUser>() {
                                    @Override
                                    public void onResponse(Call<UpdateUser> call, Response<UpdateUser> response) {

                                        UpdateUser responseUser = response.body();
                                        try {

                                            if (response.isSuccessful() && responseUser != null) {

                                                Toasty.success(ProfileUpdate.this, "Successfully edited", Toasty.LENGTH_SHORT, true).show();
                                                sh = getSharedPreferences("user", Context.MODE_PRIVATE);
                                                editor = sh.edit();
                                                editor.putString("_Token", token);

                                                editor.putString("Email_id", email);
                                                editor.putString("Password", password);
                                                editor.putString("device_token", device_token);
////                                            editor.putString("device_token",responseUser.getUser().getDeviceToken());
//
//
////                                            editor.putString("profile_image", responseUser.getUser().getImage());
                                                editor.putString("profile_image", String.valueOf(fileToUpload));
                                                editor.putString("user_name", name_edit.getText().toString());
                                                editor.putString("user_phn",phone_edit.getText().toString());
                                                editor.putString("user_gstin",gstin_edit.getText().toString());
                                                editor.putString("user_address",address_edit.getText().toString());
//                                            editor.putString("user_email",user_address);
//                                            editor.putString("user_phn",user_phn);
//                                            editor.putString("user_address",user_address);
//                                            editor.putString("user_email",responseUser.getUser().getEmail());
//                                            editor.putString("user_phn",responseUser.getUser().getPhone());
//                                            editor.putString("user_gstin",String.valueOf(responseUser.getUser().getCompGstin()));
//                                            editor.putString("user_address",responseUser.getUser().getAddress());
                                                editor.commit();

                                                Intent in = new Intent(ProfileUpdate.this, MainActivity.class);

                                                startActivity(in);

                                                finish();

                                            } else {
//                                            switch (response.code()) {
//                                                case 401:
//                                                    Toast.makeText(ProfileUpdate.this, "Token Expired", Toast.LENGTH_SHORT).show();
//                                                    Intent in =new Intent(ProfileUpdate.this, MainActivity.class);
//                                                    startActivity(in);
//                                                    break;
//                                                default:
                                                Toast.makeText(ProfileUpdate.this,
                                                        "Email id already exists"
                                                        , Toast.LENGTH_LONG).show();
//                                                    break;
//                                            }

                                            }
                                        }
                                        catch (Exception e)
                                        {

                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UpdateUser> call, Throwable t) {

                                        Toast.makeText(ProfileUpdate.this,
                                                "Error is " + t.getMessage()
                                                , Toast.LENGTH_LONG).show();
                                    }
                                });

                            }catch(Exception e){

                            }
                        }
                        else {
                            Toast.makeText(ProfileUpdate.this, "Please check Internet connection", Toast.LENGTH_LONG).show();

                        }
                    }
                }


                break;
        }

    }

    private boolean valid() {
        boolean isvalid=true;

        if(name_edit.getText().toString().trim().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please enter name", Toast.LENGTH_SHORT, true).show();
            name_edit.requestFocus();
            return isvalid;
        }
        if(phone_edit.getText().toString().trim().isEmpty() && phone_edit.length()<10){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please Enter phone number", Toast.LENGTH_SHORT, true).show();

            phone_edit.requestFocus();
            return isvalid;
        }
        if(email_edit.getText().toString().trim().isEmpty()){
            isvalid=false;
            Toasty.warning(getApplicationContext(), "Please Enter email id", Toast.LENGTH_SHORT, true).show();

            email_edit.requestFocus();
            return isvalid;
        }
        if(address_edit.getText().toString().trim().isEmpty()){
            isvalid=false;
//            Toast.makeText(this,"Enter property name",Toast.LENGTH_LONG).show();
            Toasty.warning(getApplicationContext(), "Please Enter address", Toast.LENGTH_SHORT, true).show();

            address_edit.requestFocus();
            return isvalid;
        }
        else if(!email1.matches(emailPattern)){
            isvalid=false;
//            Toast.makeText(this,"Invalid email id",Toast.LENGTH_LONG).show();

            Toasty.warning(getApplicationContext(), "Please enter valid email id", Toast.LENGTH_SHORT, true).show();

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

    private void requestGroupPermission(ArrayList<String> permissions) {
        String[] permissionList =new String[permissions.size()];
        permissions.toArray(permissionList);

        ActivityCompat.requestPermissions(ProfileUpdate.this,permissionList,REQUEST_GROUP_PERMISSION);
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

    private void getmenu_option() {
        PopupMenu popup1 = new PopupMenu(ProfileUpdate.this, iv_more);
        popup1.inflate(R.menu.menu);

        popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.edit:

                        try {
                            iv_more.setVisibility(View.GONE);
                            rel_update.setVisibility(View.VISIBLE);
                            name_edit.setEnabled(true);
                            phone_edit.setEnabled(true);
                            email_edit.setEnabled(true);
                            address_edit.setEnabled(true);
                            gstin_edit.setEnabled(true);
                            iv_personal_pic_edit.setClickable(true);
                            iv_personal_pic_edit.setOnClickListener(ProfileUpdate.this);
                        }
                        catch (Exception e){

                        }
                        break;

                }
                return false;
            }
        });
        popup1.show();

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
                    path=getRealPathFromURI(ProfileUpdate.this,contentURI);

                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

                    Picasso.get().load(contentURI).resize(size,size).into(iv_personal_pic_edit);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(ProfileUpdate.this, "Failed to save!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");

            tempUri = getImageUri(getApplicationContext(), thumbnail,imageTempName);
            path=getRealPathFromURI(this,tempUri);


            iv_personal_pic_edit.setImageBitmap(thumbnail);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ProfileUpdate.this.overridePendingTransition(R.anim.left_to_right,
                R.anim.right_to_left);
        finish();
    }
}
