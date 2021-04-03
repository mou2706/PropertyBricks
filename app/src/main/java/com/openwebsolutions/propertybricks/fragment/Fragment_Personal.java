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


public class Fragment_Personal extends Fragment implements View.OnClickListener{
    EditText name,phone,email,address,password,confirm_password;
    TextView tv_personal_pic;
    private static final int TXT_CAMERA=1;
    private static final int TXT_STORAGE=2;
    private static final int REQUEST_GROUP_PERMISSION=425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;
    ProgressBar progressBar;
    TextView txt_uploading,txt_successfull_personal;
    Bitmap _bitmap;
    String path="";
    String imageTempName;
    Uri tempUri;

    ImageView iv_personal_pic;
    RelativeLayout img_rel;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_fragment__personal, container, false);

        name=v.findViewById(R.id.name);
        phone=v.findViewById(R.id.phone);
        email=v.findViewById(R.id.email);
        address=v.findViewById(R.id.address);
        password=v.findViewById(R.id.password);
        confirm_password=v.findViewById(R.id.confirm_password);

        tv_personal_pic=v.findViewById(R.id.tv_personal_pic);
        progressBar = v.findViewById(R.id.spin_kit3_personal);
        txt_uploading = v.findViewById(R.id.txt_uploading);
        txt_successfull_personal=v.findViewById(R.id.txt_successfull_personal);


        tv_personal_pic.setOnClickListener(this);
        iv_personal_pic=v.findViewById(R.id.iv_personal_pic);
        img_rel=v.findViewById(R.id.img_rel);


        return v;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.tv_personal_pic:
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
                     _bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);

                    int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

                    path=getRealPathFromURI(getActivity(),contentURI);
                    progressBar.setVisibility(View.VISIBLE);
                    tv_personal_pic.setText("");
                    txt_uploading.setVisibility(View.VISIBLE);
                    CubeGrid cubeGrid = new CubeGrid();
                    progressBar.setIndeterminateDrawable(cubeGrid);

                    Handler handler=new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setVisibility(View.GONE);
                            txt_uploading.setVisibility(View.GONE);
                            tv_personal_pic.setVisibility(View.GONE);
                            txt_successfull_personal.setVisibility(View.VISIBLE);

                            img_rel.setVisibility(View.VISIBLE);
                            iv_personal_pic.setVisibility(View.VISIBLE);
                            iv_personal_pic.setImageBitmap(_bitmap);
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
                _bitmap = (Bitmap) data.getExtras().get("data");
                tempUri = getImageUri(getActivity(), _bitmap, imageTempName);
                path = getRealPathFromURI(getActivity(), tempUri);
                progressBar.setVisibility(View.VISIBLE);
                tv_personal_pic.setText("");
                txt_uploading.setVisibility(View.VISIBLE);
                CubeGrid cubeGrid = new CubeGrid();
                progressBar.setIndeterminateDrawable(cubeGrid);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        txt_uploading.setVisibility(View.GONE);
                        tv_personal_pic.setVisibility(View.GONE);
                        txt_successfull_personal.setVisibility(View.VISIBLE);

                        img_rel.setVisibility(View.VISIBLE);
                        iv_personal_pic.setVisibility(View.VISIBLE);
                        iv_personal_pic.setImageBitmap(_bitmap);
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

    public Bitmap getpicturePersonal_Signup(){
        return _bitmap;
    }
    public String getpicturePersonalpath_Signup(){
        return path;
    }
    public EditText getNamePersonal_signup(){
        return name;
    }
    public EditText getPhonePersonal_signup(){
        return phone;
    }
    public EditText getEmailPersonal_signup(){
        return email;
    }
    public EditText getAddressPersonal_signup(){
        return address;
    }
    public EditText getPasswordPersonal_signup(){
        return password;
    }
    public EditText getConfirm_PasswordPersonal_signup(){
        return confirm_password;
    }

}
