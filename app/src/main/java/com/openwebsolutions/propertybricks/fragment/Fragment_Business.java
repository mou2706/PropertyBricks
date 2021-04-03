package com.openwebsolutions.propertybricks.fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;


public class Fragment_Business extends Fragment implements View.OnClickListener{
    EditText name_bus,phone_bus,email_bus,address_bus,password_bus,confirm_password_bus,gstn_business;
    private static final int TXT_CAMERA=1;
    private static final int TXT_STORAGE=2;
    private static final int REQUEST_GROUP_PERMISSION=425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    ProgressBar progressBar;
    TextView txt_uploading,txt_successfull_personal,tv_prifle_business;
    Bitmap _bitmap_bus;

    String path="";
    String imageTempName;
    Uri tempUri;

    ImageView iv_personal_pic_b;
    RelativeLayout img_rel_b;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__business, container, false);

        name_bus=v.findViewById(R.id.name_bus);
        phone_bus=v.findViewById(R.id.phone_bus);
        email_bus=v.findViewById(R.id.email_bus);
        address_bus=v.findViewById(R.id.address_bus);
        password_bus=v.findViewById(R.id.password_bus);
        confirm_password_bus=v.findViewById(R.id.confirm_password_bus);
        gstn_business=v.findViewById(R.id.gstn_business);

        tv_prifle_business=v.findViewById(R.id.tv_prifle_business);
        progressBar = v.findViewById(R.id.spin_kit3_business);
        txt_uploading = v.findViewById(R.id.txt_uploading_bus);
        txt_successfull_personal=v.findViewById(R.id.txt_successfull_business);

        tv_prifle_business.setOnClickListener(this);
        iv_personal_pic_b=v.findViewById(R.id.iv_personal_pic_b);
        img_rel_b=v.findViewById(R.id.img_rel_b);

        return v;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

//            case R.id.tv_sign_bis:
//                if(valid()) {
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
//                    startActivity(intent, bundle);
//
//                }
//                break;
//            case R.id.tv_skip_bus:
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                Bundle bundle = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in, R.anim.slide_out).toBundle();
//                startActivity(intent, bundle);
//                break;
            case R.id.tv_prifle_business:
                if(checkpermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED)
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
        }
    }

    private void showPictureDialog() {
        android.app.AlertDialog.Builder pictureDialog = new android.app.AlertDialog.Builder(getActivity());
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

        ActivityCompat.requestPermissions(getActivity(),permissionList,REQUEST_GROUP_PERMISSION);
    }

    private int checkpermission(int permission) {
        int status= PackageManager.PERMISSION_DENIED;

        switch (permission){

            case TXT_CAMERA:
                status= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA);
                break;
            case TXT_STORAGE:
                status= ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
                break;

        }
        return status;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_REQUEST) {
            int position;

            if (data != null) {

                Uri contentURI = data.getData();
                try {
                     _bitmap_bus = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    path=getRealPathFromURI(getActivity(),contentURI);
                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

                    progressBar.setVisibility(View.VISIBLE);
                    tv_prifle_business.setText("");
                    txt_uploading.setVisibility(View.VISIBLE);
                    CubeGrid cubeGrid = new CubeGrid();
                    progressBar.setIndeterminateDrawable(cubeGrid);

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            txt_uploading.setVisibility(View.GONE);
                            tv_prifle_business.setVisibility(View.GONE);
                            txt_successfull_personal.setVisibility(View.VISIBLE);

                            img_rel_b.setVisibility(View.VISIBLE);
                            iv_personal_pic_b.setVisibility(View.VISIBLE);
                            iv_personal_pic_b.setImageBitmap(_bitmap_bus);
                        }
                    },2000);

//                    Picasso.get().load(contentURI).resize(size,size).into(uploaded_imagae);
//                    Toast.makeText(AddProperty.this, "Image Saved!", Toast.LENGTH_SHORT).show();

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            try {
                _bitmap_bus = (Bitmap) data.getExtras().get("data");
                tempUri = getImageUri(getActivity(), _bitmap_bus, imageTempName);
                path = getRealPathFromURI(getActivity(), tempUri);
                progressBar.setVisibility(View.VISIBLE);
                tv_prifle_business.setText("");
                txt_uploading.setVisibility(View.VISIBLE);
                CubeGrid cubeGrid = new CubeGrid();
                progressBar.setIndeterminateDrawable(cubeGrid);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        txt_uploading.setVisibility(View.GONE);
                        tv_prifle_business.setVisibility(View.GONE);
                        txt_successfull_personal.setVisibility(View.VISIBLE);

                        img_rel_b.setVisibility(View.VISIBLE);
                        iv_personal_pic_b.setVisibility(View.VISIBLE);
                        iv_personal_pic_b.setImageBitmap(_bitmap_bus);
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


    public EditText getNameBusiness_signup(){
        return name_bus;
    }
    public EditText getPhoneBusiness_signup(){
        return phone_bus;
    }
    public EditText getEmailBusiness_signup(){
        return email_bus;
    }
    public EditText getAddressBusiness_signup(){
        return address_bus;
    }
    public EditText getPasswordBusiness_signup(){
        return password_bus;
    }
    public EditText getConfirm_PasswordBusiness_signup(){
        return confirm_password_bus;
    }
    public String getpictureBusinesspath_Signup(){
        return path;
    }

    public EditText getGSTNBusiness_signup(){
        return gstn_business;
    }
    public Bitmap getpictureBusiness_signup(){
        return _bitmap_bus;
    }

}
