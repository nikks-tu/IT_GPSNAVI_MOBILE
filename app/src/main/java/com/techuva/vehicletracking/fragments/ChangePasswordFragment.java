package com.techuva.vehicletracking.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.activity.LoginActivity;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.api_interface.ChangePasswordDataInterface;
import com.techuva.vehicletracking.model.LoginPostParameters;

import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ChangePasswordFragment extends Fragment {
    EditText edt_oldPassword, edt_newPassword, edt_confPassword;
    LinearLayout ll_update_password;
    TextView tv_change_password, tv_update_password;
    Context context;
    String oldPassword="";
    String newPassword="";
    String confirmPassword="";
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    String userId ="";
    String accessToken= "";
    Activity mActivity;
    Toolbar toolbar;
    FrameLayout fl_icon_cart;
    String email="";


    public static ChangePasswordFragment newInstance() {
        Bundle args = new Bundle();

        ChangePasswordFragment fragment = new ChangePasswordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_change_password, null, false);
        // Inflate the layout for this fragment
        initialize(contentView);


        ll_update_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInputs();

                if(oldPassword.length()>0 || newPassword.length()>0 || confirmPassword.length()>0)
                {
                    if(oldPassword.length()>0)
                    {
                       if (newPassword.length()>0)
                       {
                           if (confirmPassword.length()>0)
                           {
                               if(newPassword.equals(confirmPassword))
                               {
                                   if(newPassword.length()<15)
                                   {
                                       if(isValidPassword(newPassword))
                                       {
                                            showLoaderNew();
                                            serviceCallforChangePassword();
                                       }
                                       else {
                                           Toast.makeText(context, "Password should contain minimum 8 characters combination of uppercase letter, special character and number.", Toast.LENGTH_SHORT).show();
                                       }
                                   }

                                   else {
                                       Toast.makeText(context, "Password length exceeded from 15 characters", Toast.LENGTH_SHORT).show();
                                   }

                               }
                               else {
                                   Toast.makeText(context, "New password and confirm password does not match", Toast.LENGTH_SHORT).show();
                               }
                           }
                           else {
                               Toast.makeText(context, "Please enter confirm password", Toast.LENGTH_SHORT).show();
                           }
                       }
                       else {
                           Toast.makeText(context, "Please enter new password", Toast.LENGTH_SHORT).show();
                       }
                    }
                    else {
                        Toast.makeText(context, "Please enter old password", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(context, "Please enter all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return contentView;
    }

    public void getInputs()
    {
        oldPassword = edt_oldPassword.getText().toString();
        newPassword = edt_newPassword.getText().toString();
        confirmPassword = edt_confPassword.getText().toString();
    }

    private void initialize(View view) {
        context = getActivity();
        edt_oldPassword = view.findViewById(R.id.edt_oldPassword);
        edt_newPassword = view.findViewById(R.id.edt_newPassword);
        edt_confPassword = view.findViewById(R.id.edt_confPassword);
        tv_update_password = view.findViewById(R.id.tv_update_password);
        ll_update_password = view.findViewById(R.id.ll_update_password);
        Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        TextView tv_title = toolbar.findViewById(R.id.tv_title);
        tv_title.setText(context.getResources().getString(R.string.change_password));
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer ");
        sb.append( MApplication.getString(context, Constants.AccessToken));
        accessToken = sb.toString();
        userId = MApplication.getString(context, Constants.UserID);
        toolbar =  getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(context.getResources().getString(R.string.change_password));
        Typeface facebold= Typeface.createFromAsset(view.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        edt_oldPassword.setTypeface(facebold);
        edt_newPassword.setTypeface(facebold);
        edt_confPassword.setTypeface(facebold);
        tv_update_password.setTypeface(facebold);
        tv_title.setTypeface(facebold);
        email = MApplication.getString(context, Constants.UserMailId);
    }


    public static boolean isValidPassword(String s) {
        Pattern PASSWORD_PATTERN = Pattern.compile("((?=.*\\d)(?=.*[A-Z])(?=.*[@#$%]).{8,15})");
        return !TextUtils.isEmpty(s) && PASSWORD_PATTERN.matcher(s).matches();
    }

    private void serviceCallforChangePassword(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ChangePasswordDataInterface service = retrofit.create(ChangePasswordDataInterface.class);
         Call<JsonElement> call = service.getStringScalar(Integer.parseInt(userId), accessToken, new LoginPostParameters(email, oldPassword, newPassword));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                if(response.body()!=null){
                    hideloader();

                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    int errorCode = infoObject.get("errorCode").getAsInt();

                    if(errorCode==0)
                    {
                        Toast.makeText(context, "Password changed successfully!", Toast.LENGTH_SHORT).show();
                        gotologout();
                    }
                    else
                    {
                        Toast.makeText(context, "Old password does not matched!", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    hideloader();
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                    //  Toast.makeText(getBaseContext(), "Data Error",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                //fl_main.setVisibility(View.GONE);
                //rl_serverError.setVisibility(View.VISIBLE);
            }

        });
    }
    public void showLoaderNew() {
        mActivity.runOnUiThread(new ChangePasswordFragment.Runloader(getResources().getString(R.string.loading)));
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
                    dialog = new Dialog(context, R.style.Theme_AppCompat_Light_DarkActionBar);
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
        mActivity.runOnUiThread(() -> {
            try
            {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }
    private void gotologout() {
        MApplication.setString(context, Constants.AccessToken, "");
        MApplication.setString(context, Constants.UserID, "");
        MApplication.setString(context, Constants.UserName, "");
        MApplication.setBoolean(context, Constants.IsLoggedIn, false);
        MApplication.setString(context, Constants.CompanyID, "");
        MApplication.setString(context, Constants.COMPANY_ID, "");
        MApplication.setBoolean(context, Constants.IsDefaultDeviceSaved, false);
        Intent intent = new Intent(context, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
