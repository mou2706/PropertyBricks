package com.openwebsolutions.propertybricks;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;



public class BillingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_billing);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        startActivity(new Intent(BillingActivity.this, CustomDialog.class));
//        finish();
    }
}