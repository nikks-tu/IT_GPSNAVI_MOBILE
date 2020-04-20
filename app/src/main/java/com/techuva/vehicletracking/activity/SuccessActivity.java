package com.techuva.vehicletracking.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.techuva.vehicletracking.R;

public class SuccessActivity extends AppCompatActivity {

    Context context;
    Toolbar toolbar;
    TextView tv_title;
    ImageView iv_success, iv_failed;
    TextView tv_hurrah, tv_text_one, tv_text_two, tv_back_to_login;
    String msg="";
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success);
        init();
        if(getIntent()!=null)
        {
           msg = getIntent().getStringExtra("MSG");
        }
        assert msg != null;
        if(!msg.equals(""))
        {
            if(msg.equals("success"))
            {
                //Toast.makeText(context, "successs", Toast.LENGTH_SHORT ).show();
                iv_success.setVisibility(View.VISIBLE);
                iv_failed.setVisibility(View.GONE);
                tv_hurrah.setText(context.getResources().getString(R.string.hurrah));
                tv_text_one.setText(context.getResources().getString(R.string.your_password_reset_link));
                tv_text_two.setText(context.getResources().getString(R.string.is_sent_to_email_id));
            }
            if(msg.equals("failed"))
            {
                //Toast.makeText(context, "fail", Toast.LENGTH_SHORT ).show();
                iv_success.setVisibility(View.GONE);
                iv_failed.setVisibility(View.VISIBLE);
                tv_hurrah.setText(context.getResources().getString(R.string.oops));
                tv_text_one.setText(context.getResources().getString(R.string.invalid_username));
                tv_text_two.setText(context.getResources().getString(R.string.provide_valid_user));
            }
        }

        tv_back_to_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void init() {
        context = SuccessActivity.this;
        toolbar = findViewById(R.id.toolbar);
        tv_title = toolbar.findViewById(R.id.tv_title);
        iv_success = findViewById(R.id.iv_success);
        iv_failed = findViewById(R.id.iv_failed);
        tv_hurrah = findViewById(R.id.tv_hurrah);
        tv_text_one = findViewById(R.id.tv_text_one);
        tv_text_two = findViewById(R.id.tv_text_two);
        tv_back_to_login = findViewById(R.id.tv_back_to_login);
        exitToast = Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.exit_text), Toast.LENGTH_SHORT);
        Typeface faceMedium  = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_title.setTypeface(faceDenmark);
        tv_hurrah.setTypeface(faceMedium);
        tv_text_one.setTypeface(faceLight);
        tv_text_two.setTypeface(faceLight);
        tv_back_to_login.setTypeface(faceLight);

    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
            finishAffinity();
            super.onBackPressed();
        }
    }
}
