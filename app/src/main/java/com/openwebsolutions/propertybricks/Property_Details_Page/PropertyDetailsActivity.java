package com.openwebsolutions.propertybricks.Property_Details_Page;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.PopupMenu;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.openwebsolutions.propertybricks.AddPropertyActivity.AddProperty;
import com.openwebsolutions.propertybricks.Api.MainApplication;
import com.openwebsolutions.propertybricks.AppData.AppData;
import com.openwebsolutions.propertybricks.LoginCheck.LoginCheck;
import com.openwebsolutions.propertybricks.MainActivity;
import com.openwebsolutions.propertybricks.Model.DeleteModel.Delete;
import com.openwebsolutions.propertybricks.Model.DeleteTenantInProperty.DeleteTenantInProperty;
import com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id.Data_Property;
import com.openwebsolutions.propertybricks.Model.PropertyEditBy_Id.GetPropertyEdit;
import com.openwebsolutions.propertybricks.Occupied_Activity.Occupied_Activity;
import com.openwebsolutions.propertybricks.R;
import com.openwebsolutions.propertybricks.SigninActivity;
import com.openwebsolutions.propertybricks.TenantEditActivity.TenantEdit;
import com.openwebsolutions.propertybricks.Vacant_Activity.Vacant_Activity;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PropertyDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView propety_image_details,iv_propertydetails_back,menu_button,iv_menu_tenant;
    TextView property_name_details,property_location_details,property_price_details,property_des_details;

    int tenant_id=0;
    int property_id=0;
    String Gstin="";
    String billing_address="";

    CircleImageView circularImageView;

    SharedPreferences sh;
    SharedPreferences.Editor editor;
    String token="";
    String email=null;
    String password=null;

    TextView tv_tenantadd;
    LinearLayout rel_bellow;
    TextView tenant_name_details,tenant_location_details,tenant_phone_details,tenant_email_details,tenant_billingadd_details,tenant_gstin_details,
            tenant_entrydate_details,tenant_paymentcycle_details;
    CardView card_tenant_details;
    LinearLayout lin_billing,lin_gstin;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_details);

        init();
        init_tenant();

        getProperty();
    }

    private void init_tenant() {
        circularImageView=findViewById(R.id.profile_image);
        tenant_name_details=findViewById(R.id.tenant_name_details);
        tenant_location_details=findViewById(R.id.tenant_location_details);
        tenant_phone_details=findViewById(R.id.tenant_phone_details);
        tenant_email_details=findViewById(R.id.tenant_email_details);
        tenant_billingadd_details=findViewById(R.id.tenant_billingadd_details);
        tenant_gstin_details=findViewById(R.id.tenant_gstin_details);
        tenant_entrydate_details=findViewById(R.id.tenant_entrydate_details);
        tenant_paymentcycle_details=findViewById(R.id.tenant_paymentcycle_details);
        card_tenant_details=findViewById(R.id.card_tenant_details);

        lin_billing=findViewById(R.id.lin_billing);
        lin_gstin=findViewById(R.id.lin_gstin);
    }

    private void init() {
        rel_bellow=findViewById(R.id.rel_bellow);
        pd = new ProgressDialog(PropertyDetailsActivity.this,R.style.AppCompatAlertDialogStyle);
        iv_menu_tenant=findViewById(R.id.iv_menu_tenant);
        iv_menu_tenant.setOnClickListener(this);
        menu_button=findViewById(R.id.menu_button);
        menu_button.setOnClickListener(this);
        tv_tenantadd=findViewById(R.id.tv_tenantadd);
        iv_propertydetails_back=findViewById(R.id.iv_propertydetails_back);
        iv_propertydetails_back.setOnClickListener(this);
        propety_image_details=findViewById(R.id.propety_image_details);
        property_name_details=findViewById(R.id.property_name_details);
        property_location_details=findViewById(R.id.property_location_details);
        property_price_details=findViewById(R.id.property_price_details);
        property_des_details=findViewById(R.id.property_des_details);

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
    }

    private void getProperty() {
        token=sh.getString("_Token","");
        ConnectivityManager conMgr = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if ( conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED ) {
            try {
                MainApplication.apiManager.getPropertyByIdUser(AppData.property_id,"Bearer"+" "+token, new Callback<GetPropertyEdit>() {
                    @Override
                    public void onResponse(Call<GetPropertyEdit> call, Response<GetPropertyEdit> response) {

                        GetPropertyEdit responseUser = response.body();
                        if (response.isSuccessful() && responseUser != null) {

                            try {
                                final Data_Property data_property=responseUser.getData();
                                property_name_details.setText(data_property.getPropertyName());
                                property_location_details.setText(data_property.getPropertyLocation());
                                property_des_details.setText(data_property.getPropertyDes());
                                property_price_details.setText(data_property.getPropertyPrice());

                                String img=data_property.getImage();
                                Picasso.get().load(AppData.image_url + img).resize(600,200).into(propety_image_details);

                                if(data_property.getTenant()==null){

                                    tv_tenantadd.setVisibility(View.VISIBLE);
                                    rel_bellow.setVisibility(View.VISIBLE);
                                    tv_tenantadd.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            AppData.property_id=data_property.getId();
                                            Intent intent=new Intent(PropertyDetailsActivity.this, Vacant_Activity.class);
                                            startActivity(intent);
                                            overridePendingTransition(R.anim.slide_in,
                                                    R.anim.slide_out);
                                        }
                                    });
                                }
                                else{
                                    tenant_id=data_property.getTenant().getId();

                                    property_id=data_property.getTenant().getPropertyId();
                                    Gstin=data_property.getTenant().getCompGstin();
                                    billing_address=data_property.getTenant().getBillingAddress();

                                    card_tenant_details.setVisibility(View.VISIBLE);
                                    tenant_name_details.setText(data_property.getTenant().getTenantOwnerName());
                                    tenant_location_details.setText(data_property.getTenant().getTenantOwnerAddress());
                                    tenant_phone_details.setText(data_property.getTenant().getTenantOwnerPhone());
                                    tenant_email_details.setText(data_property.getTenant().getTenantOwnerEmail());
                                    tenant_entrydate_details.setText(data_property.getTenant().getTenantOwnerEntrydate());
                                    if(data_property.getTenant().getTenantOwnerPaymentcycle()==1) {
                                        tenant_paymentcycle_details.setText(data_property.getTenant().getTenantOwnerPaymentcycle() + " " + "/ Month");
                                    }
                                    else {
                                        tenant_paymentcycle_details.setText(data_property.getTenant().getTenantOwnerPaymentcycle() + " " + "/ Months");
                                    }

                                    Picasso.get().load(AppData.image_url_tenant + data_property.getTenant().getImage()).into(circularImageView);

                                    if(data_property.getTenant().getBillingAddress()!=null && data_property.getTenant().getCompGstin()!=null){
                                        lin_billing.setVisibility(View.VISIBLE);
                                        lin_gstin.setVisibility(View.VISIBLE);
                                        tenant_billingadd_details.setText(data_property.getTenant().getBillingAddress());
                                        tenant_gstin_details.setText(data_property.getTenant().getCompGstin());
                                    }

                                }
                            }
                            catch (Exception e){

                                Log.d("Error","eroor");
                            }

                        } else {
                            switch (response.code()) {
                                case 401:
                                    Toast.makeText(PropertyDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                                    Intent in =new Intent(PropertyDetailsActivity.this, SigninActivity.class);
                                    startActivity(in);
                                    break;
                                default:
                                    Toasty.error(PropertyDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                                    break;
                            }

//                            Toast.makeText(PropertyDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                        }
                    }
                    @Override
                    public void onFailure(Call<GetPropertyEdit> call, Throwable t) {
                        Toast.makeText(PropertyDetailsActivity.this,"Internal Error",Toast.LENGTH_LONG).show();

                    }
                });

            } catch (Exception e) {

            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_propertydetails_back:
                Intent in = new Intent(this, MainActivity.class);
                startActivity(in);
                overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
                finish();
                break;
            case R.id.menu_button:
                getMenuOption();
                break;
            case R.id.iv_menu_tenant:
                getMenuOption_tenant();
                break;
        }

    }

    private void getMenuOption_tenant() {

         PopupMenu popup1 = new PopupMenu(PropertyDetailsActivity.this, iv_menu_tenant);

        popup1.inflate(R.menu.menu_tenant);
        popup1.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.edit_t:
                        //handle menu3 click
                        pd.setMessage("loading...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Intent intent=new Intent(PropertyDetailsActivity.this, TenantEdit.class);
                                intent.putExtra("tenant_id",String.valueOf(tenant_id));
                                intent.putExtra("Gstin",Gstin);
                                intent.putExtra("Billing_address",billing_address);
                                intent.putExtra("PropertyID",String.valueOf(property_id));
                                intent.putExtra("flagg_add","8013");

                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                                finish();
                            }
                        },1000);
                        break;
                    case R.id.remove_t:
                        //handle menu3 click
                        Dialog_confirmation_tenant();
                        break;
                }
                return false;
            }
        });
        popup1.show();

    }

    private void Dialog_confirmation_tenant() {
        final Dialog dialog=new Dialog(PropertyDetailsActivity.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Delete Tenant?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    dialog.dismiss();
                    pd.setMessage("loading...");
                    pd.show();
                    delete_tenant();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    private void delete_tenant() {
        token=sh.getString("_Token","");
        final RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "");
        MainApplication.apiManager.deletetenantByIdinProperty(tenant_id,"Bearer"+" "+token,name, new Callback<DeleteTenantInProperty>() {
            @Override
            public void onResponse(Call<DeleteTenantInProperty> call, Response<DeleteTenantInProperty> response) {

                DeleteTenantInProperty responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {

                        pd.setMessage("loading...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Toasty.success(getApplicationContext(), "Successfully deleted", Toast.LENGTH_SHORT, true).show();

                                Intent intent=new Intent(PropertyDetailsActivity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
//                                getTenant();

                            }
                        }, 1000);



                    }
                    catch (Exception e){

                    }
                } else {
//                    Toast.makeText(getActivity(),"Something wrong",Toast.LENGTH_LONG).show();
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(PropertyDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(PropertyDetailsActivity.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
//                            Toasty.error(PropertyDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

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

    private void getMenuOption() {
        //creating a popup menu
        final PopupMenu popup = new PopupMenu(PropertyDetailsActivity.this, menu_button);
        //inflating menu from xml resource
        popup.inflate(R.menu.menu_property);
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.edit_pro:
                        //handle menu3 click
                        pd.setMessage("loading...");
                        pd.setCancelable(false);
                        pd.show();

                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                pd.dismiss();
                                Intent intent = new Intent(PropertyDetailsActivity.this, AddProperty.class);
                                intent.putExtra("property_id_edit",String.valueOf(AppData.property_id));
                                startActivity(intent);
                                overridePendingTransition(R.anim.fade_in,
                                        R.anim.fade_out);
                                finish();

                            }
                        },1000);
                        break;
                    case R.id.remove_pro:
                        //handle menu3 click
                        Dialog_confirmation();
                        break;
                }
                return false;
            }
        });
        //displaying the popup
        popup.show();
    }

    private void Dialog_confirmation() {
        final Dialog dialog=new Dialog(PropertyDetailsActivity.this);
        dialog.setContentView(R.layout.dialog_logout);
        TextView txt_dia=dialog.findViewById(R.id.txt_dia);
        TextView yes=dialog.findViewById(R.id.yes);
        TextView no=dialog.findViewById(R.id.no);

        txt_dia.setText("Are you sure to Delete ?");
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                if (conMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED
                        || conMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {

                    dialog.dismiss();
                    pd.setMessage("loading...");
                    pd.setCancelable(false);
                    pd.show();
                    delete_property();
                }
            }
        });
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
        dialog.show();

    }

    private void delete_property() {
        token=sh.getString("_Token","");
        RequestBody name = RequestBody.create(MediaType.parse("text/plain"), "");
        MainApplication.apiManager.deletepropertyUserById(AppData.property_id,"Bearer"+" "+token,name, new Callback<Delete>() {
            @Override
            public void onResponse(Call<Delete> call, Response<Delete> response) {

                Delete responseUser = response.body();
                if (response.isSuccessful() && responseUser != null) {

//                   Toast.makeText(getActivity(),"Successfull",Toast.LENGTH_LONG).show();
                    try {


                        Handler handler=new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                               pd.dismiss();
//                                finish();
                                Toasty.success(PropertyDetailsActivity.this, "Successfully Deleted", Toast.LENGTH_SHORT).show();

                                Intent intent=new Intent(PropertyDetailsActivity.this, MainActivity.class);
                                startActivity(intent);
                                overridePendingTransition(R.anim.left_to_right,
                                        R.anim.right_to_left);
                                finish();
                            }
                        },1000);

                    }
                    catch (Exception e){

                    }
                } else {
                    switch (response.code()) {
                        case 401:
                            Toast.makeText(PropertyDetailsActivity.this, "Token Expired", Toast.LENGTH_SHORT).show();
                            Intent in =new Intent(PropertyDetailsActivity.this, SigninActivity.class);
                            startActivity(in);
                            break;
                        default:
                            Toasty.error(PropertyDetailsActivity.this,"Something wrong"+response.message(),Toast.LENGTH_LONG).show();

                            break;
                    }


                }
            }
            @Override
            public void onFailure(Call<Delete> call, Throwable t) {
//                Toast.makeText(getActivity(),"Internal Error",Toast.LENGTH_LONG).show();

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent in = new Intent(this, MainActivity.class);
        startActivity(in);
        overridePendingTransition(R.anim.left_to_right, R.anim.right_to_left);
        finish();
    }
}
