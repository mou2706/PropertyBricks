package com.openwebsolutions.propertybricks;

import android.Manifest;
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
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;
import com.openwebsolutions.propertybricks.fragment.Fragment_Complex;
import com.openwebsolutions.propertybricks.fragment.Fragment_Property;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    CircleImageView header_img;
    ImageView iv_user_camera;
    DrawerLayout drawer;
    NavigationView navigationView;/*For navigationView*/
    private ImageButton img_nav;/*For navigationView*/
    LinearLayout lin_nav;/*For navigationView*/

    TextView tv_tanents_menu,tv_properties_menu,tv_complex_menu,tv_billing,tv_help,tv_logout_menu;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String profile_image=null;
    String name=null;
    String email=null;
    String password=null;
    String device_token="";
    String token="";
    Uri tempUri;
    String imageTempName;





    public static final int TXT_CAMERA=1;
    public static final int TXT_STORAGE=2;
    public static final int REQUEST_GROUP_PERMISSION=425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;


    TextView txt_welcome_user;
    String path="";


    ImageView iv_search_btn;
    ImageView iv_settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        img_nav=findViewById(R.id.img_nav);/*For navigationView*/
        lin_nav=findViewById(R.id.lin_nav);/*For navigationView*/

        header_img=findViewById(R.id.header_img);

        txt_welcome_user=findViewById(R.id.txt_welcome_user);
        iv_user_camera=findViewById(R.id.iv_user_camera);
        iv_search_btn=findViewById(R.id.iv_search_btn);
        img_nav.setOnClickListener(BaseActivity.this);/*For navigationView*/
        lin_nav.setOnClickListener(BaseActivity.this);/*For navigationView*/

        header_img.setOnClickListener(BaseActivity.this);

        prepareNavigationMenu();/*For navigationView*/

        init_nav_item();
        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
        device_token=sh.getString("device_token","");

        token=sh.getString("_Token","");

        profile_image=sh.getString("profile_image","");
//        name=sh.getString("user_name","");

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            token=sh.getString("_Token","");

//            LoginCheck loginCheck = new LoginCheck(this);
//            loginCheck.getLoginCheck(email, password,device_token);
        }

        profile_image=sh.getString("profile_image","");
        name=sh.getString("user_name","");
//        device_token=sh.getString("device_token","");


        txt_welcome_user.setText(name);
        if(profile_image.equals("")){

        }else {
            Picasso.get().load(AppData.image_url_user + profile_image).into(header_img);
        }

        try{

        }
        catch (Exception e){

        }
    }

    private void init_nav_item() {
        iv_settings=findViewById(R.id.iv_settings);
        iv_settings.setOnClickListener(this);
        tv_tanents_menu=findViewById(R.id.tv_tanents_menu);
        tv_properties_menu=findViewById(R.id.tv_properties_menu);
        tv_complex_menu=findViewById(R.id.tv_complex_menu);
        tv_billing=findViewById(R.id.tv_billing);
        tv_help=findViewById(R.id.tv_help);
        tv_logout_menu=findViewById(R.id.tv_logout_menu);
        header_img=findViewById(R.id.header_img);
        header_img.setOnClickListener(this);
        iv_user_camera=findViewById(R.id.iv_user_camera);
        iv_user_camera.setOnClickListener(this);
        iv_user_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPictureDialog();
            }
        });





        tv_properties_menu.setOnClickListener(this);

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        txt_welcome_user=findViewById(R.id.txt_welcome_user);
    }

    private void prepareNavigationMenu() {

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.img_nav:
                InputMethodManager iManager1 = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                iManager1.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);
                break;

            case R.id.lin_nav:
                InputMethodManager iManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);
                iManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                if (drawer.isDrawerOpen(GravityCompat.START)) {
                    drawer.closeDrawer(GravityCompat.START);
                } else
                    drawer.openDrawer(GravityCompat.START);
                break;
            case R.id.iv_user_camera:
                if (checkpermission(TXT_CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(BaseActivity.this, permission) != PackageManager.PERMISSION_GRANTED)
                                permissionNeeded.add(permission);
                        }
                        requestGroupPermission(permissionNeeded);
                        showPictureDialog();
                    }
                }

                break;
            case R.id.header_img:
                showPictureDialog();


                break;


        }

        }
    private void requestGroupPermission(ArrayList<String> permissions) {
        String[] permissionList =new String[permissions.size()];
        permissions.toArray(permissionList);

        ActivityCompat.requestPermissions(BaseActivity.this,permissionList,REQUEST_GROUP_PERMISSION);
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
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(BaseActivity.this);
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
    private void choosePhotoFromGallary(int which) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }
    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
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
                    path=getRealPathFromURI(BaseActivity.this,contentURI);
                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));
//                    uploaded_imagae_com.setVisibility(View.VISIBLE);
//                    i.setVisibility(View.GONE);
//                    tv_upload_com.setVisibility(View.GONE);
                    Picasso.get().load(contentURI).resize(size,size).into(header_img);
//                    Toast.makeText(AddProperty.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(BaseActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            tempUri = getImageUri(getApplicationContext(), thumbnail,imageTempName);
            path=getRealPathFromURI(this,tempUri);
//            uploaded_imagae_com.setVisibility(View.VISIBLE);
//            camera_com.setVisibility(View.GONE);
//            tv_upload_com.setVisibility(View.GONE);
            header_img.setImageBitmap(thumbnail);
            Toast.makeText(BaseActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
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




    void changefragment(View view) {
        Fragment fragment;

        if(view==findViewById(R.id.tv_property)){
            fragment=new Fragment_Property();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_fragment,fragment);
            ft.commit();

        }
        if(view==findViewById(R.id.tv_complex)){
            fragment=new Fragment_Complex();
            FragmentManager fm=getSupportFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            ft.replace(R.id.main_fragment,fragment);
            ft.commit();

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
