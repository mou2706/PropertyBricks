package com.openwebsolutions.propertybricks;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.os.Bundle;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nightonke.boommenu.BoomMenuButton;
import com.nightonke.boommenu.Types.BoomType;
import com.nightonke.boommenu.Types.ButtonType;
import com.nightonke.boommenu.Types.PlaceType;
import com.nightonke.boommenu.Util;
import com.openwebsolutions.propertybricks.AddComplexActivity.AddComplex;
import com.openwebsolutions.propertybricks.AddPropertyActivity.AddProperty;
import com.openwebsolutions.propertybricks.AddPropertyActivity.SubmitYourProperty;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.Model.AddSubmitProperty;
import com.openwebsolutions.propertybricks.Profile_Update.ProfileUpdate;
import com.openwebsolutions.propertybricks.SearchingActivity.SeachingProperty;
import com.openwebsolutions.propertybricks.ShowTenants.Tenants;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;
import com.openwebsolutions.propertybricks.fragment.Fragment_Complex;
import com.openwebsolutions.propertybricks.fragment.Fragment_Property;

import es.dmoral.toasty.Toasty;


public class MainActivity extends BaseActivity {

    BoomMenuButton boomMenuButton;
    private boolean init = false;

    TextView tv_property,tv_complex,tv_complexname,tv_billing;
    String name_per,phone_per,email_per,address_per,password_per,image;
    String name_perg,phone_perg,email_perg,address_perg,password_perg,token_perg,id_g,logged;
    Bitmap _bitmap_per;
    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String email=null;
    String password=null;
    String token=null;
    String device_token=null;
    String profile_image=null;
    String name=null;

    int flag=0;
    String value="";
    String tenant_update="";

    ProgressDialog pd;


    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        LinearLayout contentLinearlayout = (LinearLayout) findViewById(R.id.container_layout);
        getLayoutInflater().inflate(R.layout.activity_main, contentLinearlayout);

        init_main();


        drawer.closeDrawer(GravityCompat.START);

        name_per=getIntent().getStringExtra("name");
        phone_per=getIntent().getStringExtra("phone");
        email_per=getIntent().getStringExtra("email");
        address_per=getIntent().getStringExtra("address");
        password_per=getIntent().getStringExtra("password");

        sh=getSharedPreferences("user", Context.MODE_PRIVATE);
        editor=sh.edit();
        email=sh.getString("Email_id","");
        password=sh.getString("Password","");
        device_token=sh.getString("device_token","");
        token=sh.getString("_Token","");
        email_perg=sh.getString("Email","");
        token_perg=sh.getString("id_token","");
        id_g=sh.getString("id","");
        logged=sh.getString("logged","");
        name_perg=sh.getString("Name","");


//        profile_image=sh.getString("profile_image","");
//        name=sh.getString("user_name","");

        ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

//            LoginCheck loginCheck = new LoginCheck(this);
//            loginCheck.getLoginCheck(email, password,device_token);
        }
        else {
            Toast.makeText(this,"Please check internet connection",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // Use a param to record whether the boom button has been initialized
        // Because we don't need to init it again when onResume()
        if (init) return;
        init = true;

        Drawable[] subButtonDrawables = new Drawable[3];
        int[] drawablesResource = new int[]{
                R.drawable.home,
                R.drawable.home,
                R.drawable.man
        };
        for (int i = 0; i < 3; i++)
            try {
                subButtonDrawables[i] = ContextCompat.getDrawable(this, drawablesResource[i]);
            }
        catch (Exception e){

        }

        String[] subButtonTexts = new String[]{"BoomMenuButton", "View source code", "Follow me"};

        int[][] subButtonColors = new int[3][2];
        for (int i = 0; i < 3; i++) {
            subButtonColors[i][1] = ContextCompat.getColor(this, R.color.light_colorText);
            subButtonColors[i][0] =  ContextCompat.getColor(this, R.color.black);
        }

        // Now with Builder, you can init BMB more convenient
        new BoomMenuButton.Builder()
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.home), subButtonColors[0], "Add Property")
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.home), subButtonColors[0], "Add Complex")
                .addSubButton(ContextCompat.getDrawable(this, R.drawable.man), subButtonColors[0], "View Tenants")
                .button(ButtonType.HAM)
                .boom(BoomType.PARABOLA)
                .place(PlaceType.HAM_3_1)
                .subButtonTextColor(ContextCompat.getColor(this, R.color.black))
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {

                        if(buttonIndex==0){
                            Intent intent=new Intent(MainActivity.this, AddProperty.class);
                            startActivity(intent);
                        }
                        if(buttonIndex==1){
                            Intent intent=new Intent(MainActivity.this, AddComplex.class);
                            startActivity(intent);
                        }
                        if(buttonIndex==2){
                            Intent intent=new Intent(MainActivity.this, Tenants.class);
                            startActivity(intent);
                        }
                    }
                })
                .init(boomMenuButton);


    }

    private void init_main() {

        boomMenuButton = (BoomMenuButton)findViewById(R.id.boom);

        tv_property=findViewById(R.id.tv_property);
        tv_complex=findViewById(R.id.tv_complex);
        tv_complexname=findViewById(R.id.tv_complexname);
        tv_billing=findViewById(R.id.tv_billing);

        tv_complex.setOnClickListener(this);
        tv_property.setOnClickListener(this);
        tv_billing.setOnClickListener(this);

        tv_properties_menu.setOnClickListener(MainActivity.this);//from nav menu to click
        tv_complex_menu.setOnClickListener(this);//from nav menu to click
        tv_logout_menu.setOnClickListener(this);
        tv_tanents_menu.setOnClickListener(this);
        iv_settings.setOnClickListener(this);
        iv_search_btn.setOnClickListener(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
           value = extras.getString("flag");
            tenant_update=extras.getString("tenant_update");
        }
        if(value!=null &&value.equals("012")){

            tv_complex.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_colorText));
            tv_complex.setTextColor(ContextCompat.getColor(this,R.color.white));
            tv_property.setBackground(ContextCompat.getDrawable(this,R.drawable.counter_design));
            tv_property.setTextColor(ContextCompat.getColor(this,R.color.dark_colorText));
            tv_billing.setTextColor(ContextCompat.getColor(this,R.color.dark_colorText));

            Fragment fragment = new Fragment_Complex();
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.main_fragment, fragment);
            ft.commit();
        }

        else {

            if(tenant_update!=null && tenant_update.equalsIgnoreCase("update")){
                Fragment fragment = new Fragment_Property();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment, fragment);
                ft.commit();
                Toasty.success(getApplicationContext(),"Tenant Added Successfully",Toasty.LENGTH_SHORT).show();

            }
            else {
                Fragment fragment = new Fragment_Property();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.main_fragment, fragment);
                ft.commit();
            }

        }
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_property:
                changefragment(v);
                tv_complexname.setText("Lists of Properties");

                tv_property.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_colorText));
                tv_complex.setBackground(ContextCompat.getDrawable(this,R.drawable.counter_design));
                tv_complex.setTextColor(ContextCompat.getColor(this,R.color.dark_colorText));
                tv_property.setTextColor(ContextCompat.getColor(this,R.color.white));

                break;
            case R.id.tv_complex:
                changefragment(v);
                tv_complexname.setText("Lists of Complexes");
                tv_complex.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_colorText));
                tv_complex.setTextColor(ContextCompat.getColor(this,R.color.white));
                tv_property.setBackground(ContextCompat.getDrawable(this,R.drawable.counter_design));
                tv_property.setTextColor(ContextCompat.getColor(this,R.color.dark_colorText));
                break;

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

            case R.id.tv_properties_menu:
                drawer.closeDrawer(GravityCompat.START);
                flag=1;


                Intent intent=new Intent(MainActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_complex_menu:

                drawer.closeDrawer(GravityCompat.START);
                Intent intent1=new Intent(MainActivity.this,MainActivity.class);
                intent1.putExtra("flag","012");
                startActivity(intent1);

                break;
            case R.id.tv_logout_menu:
                DialogBox();
                break;
            case R.id.tv_tanents_menu:
                Intent in=new Intent(MainActivity.this, Tenants.class);
                startActivity(in);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.tv_billing:
                Intent billing=new Intent(MainActivity.this, InvoiceActivity.class);
                startActivity(billing);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
            case R.id.iv_search_btn:
                Intent in1=new Intent(MainActivity.this, SeachingProperty.class);
                startActivity(in1);
                break;
            case R.id.iv_settings:
                drawer.closeDrawer(GravityCompat.START);
                Intent inte=new Intent(MainActivity.this, ProfileUpdate.class);
                startActivity(inte);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }

    private void DialogBox() {
        final Dialog dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView yes1=dialog.findViewById(R.id.yes);
        TextView no1=dialog.findViewById(R.id.no);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);

        txt_dia.setText("Are You Sure to Logout ?");
        txt_dia.setBackgroundColor(Color.parseColor("#2e8b87"));
        txt_dia.setTextColor(getResources().getColor(R.color.white));

        yes1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                drawer.closeDrawer(GravityCompat.START);
                editor.putString("Email_id","");
                editor.putString("Password","");
                editor.putString("_Token","");
                editor.putString("profile_image", "");
                editor.putString("user_name", "");
                editor.commit();
                Intent intent=new Intent(MainActivity.this,SigninActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                finish();
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

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                finishAffinity();

                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Double click to exit", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;

                }
            }, 2000);
    }


}
