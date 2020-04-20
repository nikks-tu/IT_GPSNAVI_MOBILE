package com.techuva.vehicletracking.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techuva.vehicletracking.MainActivity;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.utils.MPreferences;
import com.techuva.vehicletracking.api_interface.AccountDetailsInterface;
import com.techuva.vehicletracking.api_interface.LoginDataInterface;
import com.techuva.vehicletracking.model.AccountListResultObject;
import com.techuva.vehicletracking.model.AccountsListMainObject;
import com.techuva.vehicletracking.model.LoginMainObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class LoginActivity extends AppCompatActivity {

    LinearLayout ll_root_login, ll_button_login, ll_layout_forgot_password;
    EditText edt_userName, edt_userPassword;
    Context loginContext;
    TextView tv_forgotPassword, tv_sign_up, tv_login_heading, tv_gps, tv_navi, tv_login_btn, tv_forgot, tv_from, tv_tech, tv_uva, tv_dont_have_account;
    MPreferences preferences;
    List<AccountListResultObject> accountListResultObjects = new ArrayList<>();
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    Toast exitToast;
    Boolean doubleBackToExitPressedOnce = true;
    private String EmailId;
    private String Password;
    int UserId;
    String authorityKey ="";
    String authorityKeyAccount ="";
    String grantType = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitViews();
        exitToast = Toast.makeText(getApplicationContext(), "Press back again to exit GPS_NAVI", Toast.LENGTH_SHORT);
        edt_userName.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ll_button_login.setEnabled(true);
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                ll_button_login.setEnabled(true);
                // TODO Auto-generated method stub
            }
        });

        tv_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginContext, SignUpActivity.class);
                startActivity(intent);
            }
        });

        edt_userPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ll_button_login.setEnabled(true);
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable s) {
                ll_button_login.setEnabled(true);
                // TODO Auto-generated method stub
            }
        });

        tv_forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(loginContext, ForgetPasswordActivity.class);
                startActivity(intent);
            }
        });


        ll_button_login.setOnClickListener(v -> {
          /*  Intent intent = new Intent(loginContext, Dashboard.class);
            startActivity(intent);
*/
            ll_button_login.setEnabled(false);
            getTextInputs();

            if(MApplication.isNetConnected(loginContext))
            {
                if(EmailId.length() > 0 || Password.length() > 0)
                {
                    if(EmailId.length()>0)
                    {

                        if(Password.length()>0)
                        {
                            showLoaderNew();
                            serviceCall();
                        }
                        else
                        {
                            Toast toast = Toast.makeText(loginContext, "Please enter password" , Toast.LENGTH_LONG);
                            View view = toast.getView();
                            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                            toast.show();
                        }
                    }
                    else
                    {
                        //  Toast.makeText(loginContext, "Please enter email ID", Toast.LENGTH_SHORT ).show();
                        Toast toast = Toast.makeText(loginContext, R.string.enter_email, Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();
                    }
                }
                else
                {
                    Toast toast = Toast.makeText(loginContext, "Please enter email ID and password", Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }
            }
            else
            {
                Toast toast = Toast.makeText(loginContext, "No Internet Connection!", Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();
            }
        });

    }

    private void getTextInputs() {
        EmailId = edt_userName.getText().toString().trim();
        Password = edt_userPassword.getText().toString();
    }



    private void serviceCallForAccounts() {

        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AccountDetailsInterface service = retrofit.create(AccountDetailsInterface.class);

        //Call<AccountsListMainObject> call = service.getUserAccountDetails(UserId);
        Call<AccountsListMainObject> call = service.getUserAccountDetailsWithSession(UserId, authorityKeyAccount, UserId);
        call.enqueue(new Callback<AccountsListMainObject>() {
            @Override
            public void onResponse(Call<AccountsListMainObject> call, Response<AccountsListMainObject> response) {
                if(response.body()!=null){
                    // Toast.makeText(getBaseContext(),response.body().getInfo().getErrorMessage(),Toast.LENGTH_SHORT).show();

                    if(response.body().getInfo().getErrorCode()==0)
                    { hideloader();
                        accountListResultObjects = response.body().getResult();
                        MApplication.setString(loginContext, Constants.NotificationCount, "0");
                        proceedToNextScreen();
                    }
                    else if(response.body().getInfo().getErrorCode().equals(1))
                    {
                        hideloader();
                        Toast toast = Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();
                    }
                }else {
                    hideloader();
                }
            }
            @Override
            public void onFailure(Call<AccountsListMainObject> call, Throwable t) {
                hideloader();
                Toast toast = Toast.makeText(loginContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });
    }

    private void proceedToNextScreen() {
        if(accountListResultObjects.size()<=1)
        {
            int i =  accountListResultObjects.get(0).getCompanyId();
            MApplication.setBoolean(loginContext, Constants.SingleAccount, true);
            MApplication.setString(loginContext, Constants.UserIDSelected, String.valueOf(UserId));
            MApplication.setString(loginContext, Constants.UserID, String.valueOf(UserId));
            MApplication.setString(loginContext, Constants.CompanyID, String.valueOf(i));
            MApplication.setBoolean(loginContext, Constants.IsDefaultDeviceSaved, false);
            Intent intent = new Intent(loginContext, MainActivity.class);
            startActivity(intent);
        }
        else
        {
            MApplication.setBoolean(loginContext, Constants.SingleAccount, false);
            MApplication.setBoolean(loginContext, Constants.IsDefaultDeviceSaved, false);
            Intent intent = new Intent(loginContext, UserAccountsActivity.class);
            startActivity(intent);
        }

    }


    private void serviceCall() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100,TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        LoginDataInterface service = retrofit.create(LoginDataInterface.class);

        Call<LoginMainObject> call = service.loginCall(authorityKey, EmailId, Password, grantType);
        call.enqueue(new Callback<LoginMainObject>() {
            @Override
            public void onResponse(Call<LoginMainObject> call, Response<LoginMainObject> response) {
                if(response.body()!=null){
                    if(response.body().getInfo().getErrorCode()==0)
                    {
                        hideloader();
                        MApplication.setString(loginContext, Constants.AccessToken, response.body().getResult().getAccessToken());
                        MApplication.setString(loginContext, Constants.RefreshToken, response.body().getResult().getRefreshToken());
                        MApplication.setBoolean(loginContext, Constants.IsLoggedIn, true);
                        MApplication.setString(loginContext, Constants.UserID, String.valueOf(response.body().getResult().getUserId()));
                        MApplication.setString(loginContext, Constants.UserMailId, String.valueOf(response.body().getResult().getEmail()));
                        MApplication.setString(loginContext, Constants.SecondsToExpireToken, response.body().getResult().getExpiresIn());
                        MApplication.setString(loginContext, Constants.DateToExpireToken, response.body().getResult().getAccess_expires_in());
                        UserId = Integer.parseInt(MApplication.getString(loginContext, Constants.UserID));
                        MApplication.setString(loginContext, Constants.UserName, String.valueOf(response.body().getResult().getUserName()));
                        authorityKeyAccount = "Bearer "+MApplication.getString(loginContext, Constants.AccessToken);
                        MApplication.setBoolean(loginContext, Constants.IsSessionExpired, false);
                        preferences.setLoginStatus(true);
                        checkAccountList();
                    }
                    else if(response.body().getInfo().getErrorCode().equals(1))
                    {
                        hideloader();
                        Toast toast = Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_LONG);
                        ll_button_login.setEnabled(true);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();
                        // Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_SHORT).show();
                    }
                }else {
                    hideloader();
                    ll_button_login.setEnabled(true);
                    Toast toast = Toast.makeText(loginContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<LoginMainObject> call, Throwable t) {
                hideloader();
                ll_button_login.setEnabled(true);
                Toast toast = Toast.makeText(loginContext, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });

    }

    private void checkAccountList() {
        serviceCallForAccounts();
    }

    private void InitViews() {
        loginContext = LoginActivity.this;
        preferences = new MPreferences(loginContext);
        ll_root_login  = findViewById(R.id.ll_root_login);
        tv_forgotPassword  = findViewById(R.id.tv_forgotPassword);
        tv_sign_up  = findViewById(R.id.tv_sign_up);
        ll_button_login = findViewById(R.id.ll_button_login);
        ll_layout_forgot_password = findViewById(R.id.ll_layout_forgot_password);
        edt_userName = findViewById(R.id.edt_userName);
        edt_userPassword = findViewById(R.id.edt_userPassword);
        tv_login_heading = findViewById(R.id.tv_login_heading);
        tv_dont_have_account = findViewById(R.id.tv_dont_have_account);
        tv_gps = findViewById(R.id.tv_gps);
        tv_navi = findViewById(R.id.tv_navi);
        tv_login_btn = findViewById(R.id.tv_login_btn);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_from = findViewById(R.id.tv_from);
        tv_tech = findViewById(R.id.tv_tech);
        tv_uva = findViewById(R.id.tv_uva);

        ll_button_login.setEnabled(true);
        authorityKey = Constants.AuthorizationKey;
        grantType = Constants.GrantType;
        setTypfaces();
    }

    private void setTypfaces() {
        Typeface faceLight = Typeface.createFromAsset(loginContext.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceMedium = Typeface.createFromAsset(loginContext.getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceDenmark = Typeface.createFromAsset(loginContext.getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_login_heading.setTypeface(faceLight);
        tv_login_btn.setTypeface(faceLight);
        tv_forgot.setTypeface(faceLight);
        edt_userName.setTypeface(faceLight);
        edt_userPassword.setTypeface(faceLight);
        tv_dont_have_account.setTypeface(faceLight);
        tv_from.setTypeface(faceLight);
        tv_gps.setTypeface(faceDenmark);
        tv_navi.setTypeface(faceDenmark);
        tv_tech.setTypeface(faceDenmark);
        tv_uva.setTypeface(faceDenmark);
        tv_sign_up.setTypeface(faceMedium);
        tv_forgotPassword.setTypeface(faceMedium);
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce){

            // Do what ever you want
            exitToast.show();
            doubleBackToExitPressedOnce = false;
        } else{
            finishAffinity();
            finish();
            // Do exit app or back press here
            super.onBackPressed();
        }
    }

    public void showLoaderNew() {
        runOnUiThread(new LoginActivity.Runloader(getResources().getString(R.string.loading)));
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
                    dialog = new Dialog(loginContext,R.style.Theme_AppCompat_Light_DarkActionBar);
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

}
