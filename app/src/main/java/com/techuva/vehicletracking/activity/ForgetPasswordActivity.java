package com.techuva.vehicletracking.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.api_interface.ForgotPasswordDataInterface;
import com.techuva.vehicletracking.model.ForgotPassPostParameters;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ForgetPasswordActivity extends AppCompatActivity {
    LinearLayout ll_root_view_forgot, ll_layout_entry, ll_button_submit, ll_signin;
    ImageView iv_userNameforgot;
    EditText edt_username_forgot;
    String EmailId ="";
    Context context;
    TextView tv_forgot_heading;
    TextView tv_submit;
    TextView tv_already_have_account, tv_forgotPassword;
    TextView tv_gps, tv_navi, tv_from, tv_tech, tv_uva;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        InitViews();

        ll_signin.setOnClickListener(v -> {
            Intent intent = new Intent(context, LoginActivity.class);
            startActivity(intent);
        });

        ll_button_submit.setOnClickListener(v -> {
            getInputData();
        });

    }

    private void getInputData() {
        EmailId = edt_username_forgot.getText().toString().trim();
        if(EmailId.length() > 0)
        {
            serviceCall();
        }
        else
        {
            // Toast.makeText(context, R.string.enter_email, Toast.LENGTH_SHORT ).show();
            Toast toast = Toast.makeText(context, R.string.enter_reg_email, Toast.LENGTH_LONG);
            View view = toast.getView();
            view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
            toast.show();
        }
    }


    private void serviceCall() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ForgotPasswordDataInterface service = retrofit.create(ForgotPasswordDataInterface .class);

        Call<JsonElement> call = service.getStringScalar(new ForgotPassPostParameters(EmailId));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if(response.body()!=null){

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();


                    if(info.get("errorCode").getAsInt()==0)
                    {
                        //showCustomDialog(info.get("errorMessage").getAsString());
                        Intent intent = new Intent(context, SuccessActivity.class);
                        intent.putExtra("MSG", "success");
                        startActivity(intent);
                    }
                    else if(info.get("errorCode").getAsInt()==1)
                    {
                        /*Toast toast = Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_LONG);
                        View view = toast.getView();
                        view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                        toast.show();*/
                        // Toast.makeText(loginContext, response.body().getInfo().getErrorMessage(), Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(context, SuccessActivity.class);
                        intent.putExtra("MSG", "failed");
                        startActivity(intent);
                    }
                }
                else {
                    Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                    View view = toast.getView();
                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                    toast.show();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast toast = Toast.makeText(context, R.string.something_went_wrong, Toast.LENGTH_LONG);
                View view = toast.getView();
                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.toast_back_red));
                toast.show();

            }
        });

    }

    private void InitViews() {
        context = ForgetPasswordActivity.this;
        ll_root_view_forgot = findViewById(R.id.ll_root_view_forgot);
        ll_layout_entry = findViewById(R.id.ll_layout_entry);
        ll_button_submit = findViewById(R.id.ll_button_submit);
        ll_signin = findViewById(R.id.ll_signin);
        iv_userNameforgot = findViewById(R.id.iv_userNameforgot);
        edt_username_forgot = findViewById(R.id.edt_username_forgot);
        tv_forgot_heading = findViewById(R.id.tv_forgot_heading);
        tv_submit = findViewById(R.id.tv_submit);
        tv_already_have_account = findViewById(R.id.tv_already_have_account);
        tv_gps = findViewById(R.id.tv_gps);
        tv_navi = findViewById(R.id.tv_navi);
        tv_from = findViewById(R.id.tv_from);
        tv_tech = findViewById(R.id.tv_tech);
        tv_uva = findViewById(R.id.tv_uva);
        tv_forgotPassword = findViewById(R.id.tv_forgotPassword);
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        edt_username_forgot.setTypeface(faceLight);
        tv_submit.setTypeface(faceLight);
        tv_already_have_account.setTypeface(faceLight);
        Typeface faceDenmark = Typeface.createFromAsset(context.getAssets(),
                "fonts/films.DENMARK.ttf");
        Typeface faceMedium = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        tv_forgot_heading.setTypeface(faceLight);
        tv_submit.setTypeface(faceLight);
        tv_already_have_account.setTypeface(faceLight);
        tv_from.setTypeface(faceLight);
        tv_gps.setTypeface(faceDenmark);
        tv_navi.setTypeface(faceDenmark);
        tv_tech.setTypeface(faceDenmark);
        tv_uva.setTypeface(faceDenmark);
        tv_forgotPassword.setTypeface(faceMedium);

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }



    private void showCustomDialog(String text) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = findViewById(android.R.id.content);

        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(this).inflate(R.layout.alert_dialog, viewGroup, false);


        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        TextView tv_alertText = dialogView.findViewById(R.id.tv_alertText);
        TextView button_ok = dialogView.findViewById(R.id.button_ok);
        tv_alertText.setText(text);

        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent
                );
            }
        });

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}
