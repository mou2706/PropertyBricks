package com.openwebsolutions.propertybricks.fragment.Occupied_Fragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.CubeGrid;
import com.openwebsolutions.propertybricks.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Personal extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    ImageView personal_img_change,img_personal,iv_pro_date;
    TextView tv_pro_date,tv_img_upload;

    ProgressBar progressBar;

    public static final int TXT_CAMERA=1;
    public static final int TXT_STORAGE=2;
    public static final int REQUEST_GROUP_PERMISSION=425;
    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int CAMERA = 2;
    private static final int MAX_WIDTH = 1024;
    private static final int MAX_HEIGHT = 768;

    DatePickerDialog datePickerDialog;
    int year;
    int month;
    int dayOfMonth;
    Calendar calendar;

    String path="";
    String imageTempName;
    Uri tempUri;

    Spinner spinner;
    String[] spinnerItems = new String[]{
            "Select Month(s)",
            "1",
            "3",
            "6",
            "12"
    };
    List<String> spinnerlist;
    ArrayAdapter<String> arrayadapter;

    private EditText et_personal_name,et_personal_phone,et_personal_email,et_personal_address;
    String text="";

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_personal, container, false);

        img_personal=v.findViewById(R.id.img_personal);
        personal_img_change=v.findViewById(R.id.personal_img_change);
        iv_pro_date=v.findViewById(R.id.iv_pro_date);
        tv_pro_date=v.findViewById(R.id.tv_pro_date);

        spinner=v.findViewById(R.id.payment_spinner);
        init_spinner();//spinner

        et_personal_name=v.findViewById(R.id.et_personal_name);
        et_personal_phone=v.findViewById(R.id.et_personal_phone);
        et_personal_email=v.findViewById(R.id.et_personal_email);
        et_personal_address=v.findViewById(R.id.et_personal_address);

        tv_img_upload=v.findViewById(R.id.tv_img_upload);

        progressBar = v.findViewById(R.id.spinkit3_vacant_personal);

        tv_pro_date.setOnClickListener(this);
        iv_pro_date.setOnClickListener(this);
        personal_img_change.setOnClickListener(this);

        return v;
    }


    private void init_spinner() {

        spinnerlist = new ArrayList<>(Arrays.asList(spinnerItems));

        arrayadapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,spinnerlist){
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
            public View getDropDownView(int position, View convertView,ViewGroup parent)
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
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

                    final Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), contentURI);
                    path=getRealPathFromURI(getActivity(),contentURI);
                    final int size = (int) Math.ceil(Math.sqrt(MAX_WIDTH * MAX_HEIGHT));

                    Picasso.get().load(contentURI).resize(size,size).into(img_personal, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                            tv_img_upload.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {

                        }
                    });



                    img_personal.setDrawingCacheEnabled(true);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA) {
            progressBar.setVisibility(View.VISIBLE);
            CubeGrid cubeGrid = new CubeGrid();
            progressBar.setIndeterminateDrawable(cubeGrid);
            final Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            tempUri = getImageUri(getActivity(), thumbnail,imageTempName);
            path=getRealPathFromURI(getActivity(),tempUri);
            Handler handler=new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.GONE);
                    tv_img_upload.setVisibility(View.GONE);

                    img_personal.setImageBitmap(thumbnail);
                }
            },1000);

            img_personal.setDrawingCacheEnabled(true);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personal_img_change:
                if(checkpermission(TXT_CAMERA)!= PackageManager.PERMISSION_GRANTED) {
                    if (checkpermission(TXT_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                        ArrayList<String> permissionNeeded = new ArrayList<>();
                        ArrayList<String> permissionAvailable = new ArrayList<>();
                        permissionAvailable.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                        permissionAvailable.add(Manifest.permission.CAMERA);
                        for (String permission : permissionAvailable) {

                            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED)
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

            case R.id.iv_pro_date:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_pro_date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                datePickerDialog.show();
                break;

            case R.id.tv_pro_date:
                calendar = Calendar.getInstance();
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(getActivity(),R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tv_pro_date.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, dayOfMonth);
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

                datePickerDialog.show();
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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

             text = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public String getPersonalpicturepath_Signup(){
        return path;
    }
    public EditText getTextPersonalName(){
     return et_personal_name;
    }
    public EditText getTextPersonalPhone(){
        return et_personal_phone;
    }
    public EditText getTextPersonalEmail(){
        return et_personal_email;
    }
    public EditText getTextPersonalAddress(){
        return et_personal_address;
    }

    public String getTextPersonalpaymentCycle(){
        return text;
    }
    public TextView getTextPersonalEntryDate(){
        return tv_pro_date;
    }
    public Bitmap getPersonalpicture(){
        Bitmap bmap = img_personal.getDrawingCache();
        return bmap;
    }


}
