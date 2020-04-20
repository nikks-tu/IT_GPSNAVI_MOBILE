package com.techuva.vehicletracking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.api_interface.ForgotPasswordDataInterface;
import com.techuva.vehicletracking.api_interface.LoginDataInterface;
import com.techuva.vehicletracking.api_interface.SignUpDataInterface;
import com.techuva.vehicletracking.model.ForgotPassPostParameters;
import com.techuva.vehicletracking.model.LoginMainObject;
import com.techuva.vehicletracking.model.SignUpPostParameters;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SignUpActivity extends AppCompatActivity {

    Context context;
    LinearLayout ll_layout_entry, ll_button_signup, ll_text_copyright;
    TextView tv_gps, tv_navi, tv_tech, tv_uva, tv_login_heading, tv_sign_up, tv_already_have_account, tv_login_btn, tv_from;
    EditText edt_userName, edt_userEmail, edt_userMobile, edt_deviceSerial, edt_regCode;
    String firstName="";
    String lastName="";
    String mobileNo="";
    String emailId="";
    String devSerialNo="";
    String regdCode="";
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        setTypeface();

        exitToast = Toast.makeText(getApplicationContext(), context.getResources().getString(R.string.exit_text), Toast.LENGTH_SHORT);
        ll_button_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();


                if(!firstName.equals(""))
                {
                   if(!emailId.equals(""))
                   {
                       if(emailId.matches(emailPattern) && emailId.length() > 0)
                       {
                           if(mobileNo.length()>9 && validCellPhone(mobileNo))
                           {
                               if(!devSerialNo.equals(""))
                               {
                                   if(!regdCode.equals(""))
                                   {
                                       showLoaderNew();
                                       serviceCall();
                                   }
                                   else {
                                       Toast.makeText(context, "Please enter registration code!", Toast.LENGTH_SHORT).show();
                                   }
                               }
                               else {
                                   Toast.makeText(context, "Please enter device serial!", Toast.LENGTH_SHORT).show();
                               }
                           }
                           else {
                               Toast.makeText(context, "Please enter valid mobile number!", Toast.LENGTH_SHORT).show();
                           }
                       }
                       else {
                           Toast.makeText(context, "Please enter valid email-id!", Toast.LENGTH_SHORT).show();
                       }
                   }
                   else {
                       Toast.makeText(context, "Please enter email-id!", Toast.LENGTH_SHORT).show();
                   }
                }
                else {
                    Toast.makeText(context, "Please enter user name!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(context, LoginActivity.class);
                startActivity(in);
            }
        });

    }

    private void getInputs() {
      firstName = edt_userName.getText().toString().trim();
      lastName = "";
      emailId = edt_userEmail.getText().toString().trim();
      mobileNo = edt_userMobile.getText().toString();
      devSerialNo = edt_deviceSerial.getText().toString();
      regdCode = edt_regCode.getText().toString();

    }


    public boolean validCellPhone(String number)
    {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    private void init() {
        context = SignUpActivity.this;
        ll_layout_entry = findViewById(R.id.ll_layout_entry);
        ll_button_signup = findViewById(R.id.ll_button_signup);
        ll_text_copyright = findViewById(R.id.ll_text_copyright);
        tv_gps = findViewById(R.id.tv_gps);
        tv_navi = findViewById(R.id.tv_navi);
        tv_tech = findViewById(R.id.tv_tech);
        tv_uva = findViewById(R.id.tv_uva);
        tv_login_heading = findViewById(R.id.tv_login_heading);
        tv_sign_up = findViewById(R.id.tv_sign_up);
        tv_already_have_account = findViewById(R.id.tv_already_have_account);
        tv_login_btn = findViewById(R.id.tv_login_btn);
        tv_from = findViewById(R.id.tv_from);
        edt_userName = findViewById(R.id.edt_userName);
        edt_userEmail = findViewById(R.id.edt_userEmail);
        edt_userMobile = findViewById(R.id.edt_userMobile);
        edt_deviceSerial = findViewById(R.id.edt_deviceSerial);
        edt_regCode = findViewById(R.id.edt_regCode);
    }
    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceMedium = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_gps.setTypeface(faceDenmark);
        tv_navi.setTypeface(faceDenmark);
        tv_tech.setTypeface(faceDenmark);
        tv_uva.setTypeface(faceDenmark);
        tv_login_heading.setTypeface(faceLight);
        tv_sign_up.setTypeface(faceLight);
        tv_already_have_account.setTypeface(faceLight);
        tv_login_btn.setTypeface(faceMedium);
        tv_from.setTypeface(faceLight);
        edt_userName.setTypeface(faceLight);
        edt_userEmail.setTypeface(faceLight);
        edt_userMobile.setTypeface(faceLight);
        edt_deviceSerial.setTypeface(faceLight);
        edt_regCode.setTypeface(faceLight);

    }

    public void showLoaderNew() {
        runOnUiThread(new SignUpActivity.Runloader(getResources().getString(R.string.loading)));
    }

    class Runloader implements Runnable {
        private String strrMsg;

        public Runloader(String strMsg) {
            this.strrMsg = strMsg;
        }

        @SuppressWarnings("ResourceType")
        @Override
        public void run() {
            try {
                if (dialog == null)
                {
                    dialog = new Dialog(context,R.style.Theme_AppCompat_Light_DarkActionBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                dialog.show();

                ImageView imgeView = dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = dialog
                        .findViewById(R.id.tvLoading);
                if (!strrMsg.equalsIgnoreCase(""))
                    tvLoading.setText(strrMsg);

                imgeView.setBackgroundResource(R.drawable.frame);

                animationDrawable = (AnimationDrawable) imgeView
                        .getBackground();
                imgeView.post(new Runnable() {
                    @Override
                    public void run() {
                        if (animationDrawable != null)
                            animationDrawable.start();
                    }
                });
            } catch (Exception e)
            {

            }
        }
    }

    public void hideloader() {
        runOnUiThread(() -> {
            try
            {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }); }


    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        SignUpDataInterface service = retrofit.create(SignUpDataInterface.class);

        Call<JsonElement> call = service.getStringScalar(new SignUpPostParameters(firstName, lastName, mobileNo, emailId, devSerialNo, regdCode));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){

                    hideloader();
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();


                    if(info.get("errorCode").getAsInt()==0)
                    {
                        //showCustomDialog(info.get("errorMessage").getAsString());
                        Intent intent = new Intent(context, SignUpSuccessActivity.class);
                        intent.putExtra("MSG", "success");
                        startActivity(intent);
                    }
                    else if(info.get("errorCode").getAsInt()==1)
                    {
                        Toast toast = Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();
                        // Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_SHORT).show();
                       /* Intent intent = new Intent(context, SuccessActivity.class);
                        intent.putExtra("MSG", "failed");
                        startActivity(intent);*/
                    }
                }
                else {
                    hideloader();
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });

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
