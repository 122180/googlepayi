package com.example.googlepayint;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.googlepayint.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding mBinding;
    public static final String GOOGLE_PAY_PACKAGE_NAME="com.google.android.apps.nbu.paisa.user";
   private String amount;
   private String name="AAkash kumar";
   private String upiid="9523286311@ybl";
   private String transactionNote="for test";
   private String status;
   Uri mUri;

   private  int REQUEST_CODE=123;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding=ActivityMainBinding.inflate(getLayoutInflater());

        setContentView(mBinding.getRoot());
        mBinding.price.setFocusable(true);
        mBinding.googlepay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                amount=mBinding.price.getText().toString();
               if(!amount.isEmpty())
               {
                   mUri=getPaymentUri(name,upiid,transactionNote,amount);
                   payWithPay();
               }else
               {
                   Toast.makeText(MainActivity.this, "please enter the amount", Toast.LENGTH_SHORT).show();
                   mBinding.price.setFocusable(true);
               }
            }
        });


    }

    private void payWithPay() {
        if(isAppinstalled(this,GOOGLE_PAY_PACKAGE_NAME))
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            intent.setData(mUri);
            intent.setPackage(GOOGLE_PAY_PACKAGE_NAME);
            startActivityForResult(intent,REQUEST_CODE);


        }else
        {
            Toast.makeText(this, "app is not installed", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null)
        {
            status=data.getStringExtra("status").toString();
        }
        if(RESULT_OK==resultCode && status.equals("success"))
        {
            Toast.makeText(this, "transaction is successful", Toast.LENGTH_SHORT).show();

        }else
        {
            Toast.makeText(this, "transaction failed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAppinstalled(MainActivity mainActivity, String googlePayPackageName) {
        try {
            mainActivity.getPackageManager().getApplicationInfo(googlePayPackageName,0);


            return true;

        }catch(Exception e)
        {
            e.printStackTrace();
            return false;
        }

    }

    private Uri getPaymentUri(String name, String upiid, String transactionNote, String amount) {
        return new Uri.Builder()
                .scheme("upi")
                .authority("pay")
                .appendQueryParameter("pa",upiid)
                .appendQueryParameter("pn",name)
                .appendQueryParameter("tn",transactionNote)
                .appendQueryParameter("am",amount)
                .appendQueryParameter("cu","INR")
                .build();
    }
}