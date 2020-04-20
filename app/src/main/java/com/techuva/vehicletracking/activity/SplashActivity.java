package com.techuva.vehicletracking.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.BuildConfig;
import com.techuva.vehicletracking.MainActivity;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.utils.MPreferences;
import com.techuva.vehicletracking.api_interface.VersionInfoDataInterface;
import com.techuva.vehicletracking.model.VersionInfoMainObject;
import com.techuva.vehicletracking.model.VersionInfoPostParameters;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class SplashActivity extends AppCompatActivity {

    ImageView iv_app_logo;
    Context context;
    MPreferences preferences;
    Boolean fromDeny= false;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    String inventoryId = "70";
    String appVersion;
    String fontVersion;
    String FontUrl="";
    String AppLink="";
    public Dialog dialog;
    TextView tv_from, tv_tech;
    private AnimationDrawable animationDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Init();

        new Handler().postDelayed(new Runnable() {

        // Using handler with postDelayed called runnable run method

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override

            public void run() {
                serviceCallforVersionInfo();
            }

        }, 500);


    }

    private void goto_login_activity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void checkTokenValidity() {
        String my_date = MApplication.getString(context, Constants.DateToExpireToken);
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        Date strDate = null;
        try {
            strDate = sdf.parse(my_date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (new Date().after(strDate)) {
            goto_login_activity();
        }
        else{
            checkIfLoggedIn();
        }
    }

    private void checkIfLoggedIn() {

            if(preferences.isLoggedIn() && preferences.isDefaultDeviceSelected())
            {
                Intent i = new Intent(context, MainActivity.class);

                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else if(preferences.isLoggedIn())
            {
                Intent i = new Intent(context, MainActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
            else
            {
                Intent i = new Intent(context, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        }

    private void Init() {
        context = SplashActivity.this;
        preferences = new MPreferences(context);
        iv_app_logo = findViewById(R.id.iv_app_logo);
        appVersion = String.valueOf(BuildConfig.VERSION_CODE);
        //appVersion = "0";
       // appVersion = preferences.getStringFromPreference(Constants.AppVersion, "1");
        fontVersion = preferences.getStringFromPreference(Constants.FontVersion, "0");
        Typeface faceMedium = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_from = findViewById(R.id.tv_from);
        tv_tech = findViewById(R.id.tv_tech);
        tv_from.setTypeface(faceMedium);
        tv_tech.setTypeface(faceMedium);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public boolean checkAndRequestPermissions() {
        int permissionSendMessage = context.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
        int locationPermission = context.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();
        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        Log.d("", "Permission callback called-------");
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                // Initialize the map with both permissions
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);
                    // Check for both permissions
                    if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        Log.d("", "Storage services permission granted");
                        // process the normal flow
                        //else any one or both the permissions are not granted
                        // Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                        serviceCallforVersionInfo();
                        checkIfLoggedIn();
                    } else {
                        Log.d("", "Some permissions are not granted ask again ");
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        //shouldShowRequestPermissionRationale will return true
                        //show the dialog or snackbar saying its necessary and try again otherwise proceed with setup.
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            showDialogOK("Storage Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @RequiresApi(api = Build.VERSION_CODES.M)
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(this, "Please enable the permission from settings", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }
                }
            }
        }

    }
    */



    private void serviceCallforVersionInfo(){
        Retrofit retrofit = new Retrofit.Builder()
                //.baseUrl(Constants.LIVE_BASE_URL)
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        VersionInfoDataInterface service = retrofit.create(VersionInfoDataInterface.class);

        Call<JsonElement> call = service.getStringScalar(new VersionInfoPostParameters(appVersion));
        //Call<VersionInfoMainObject> call = service.getStringScalarWithSession(UserId, authorityKey, new VersionInfoPostParameters(inventoryId, appVersion, fontVersion));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )

                if(response.body()!=null){
                    JsonObject object = response.body().getAsJsonObject();
                    JsonObject info = object.get("info").getAsJsonObject();
                    if(info.get("errorCode").getAsInt()==0) {
                        hideloader();
                        JsonArray result1 = object.get("result").getAsJsonArray();
                        if (result1.size() > 0)
                        {
                            JsonObject result = result1.get(0).getAsJsonObject();

                            AppLink = result.get("AppLink").getAsString();
                            boolean appStatus = result.get("AppVersionStatus").getAsBoolean();
                            if(appStatus)
                            {
                                hideloader();
                                String s = MApplication.getString(context,  Constants.DateToExpireToken);
                                if(s.equals(""))
                                {
                                    goto_login_activity();
                                }
                                else
                                    checkTokenValidity();
                            }
                            else
                            {
                                context = SplashActivity.this;
                                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(SplashActivity.this);
                                final View view = inflater.inflate(R.layout.versionpopup, null);
                                alertDialog.setView(view);
                                Typeface faceMedium = Typeface.createFromAsset(context.getAssets(),
                                        "fonts/AvenirLTStd-Light.otf");
                                final String updateUrl = result.get("AppLink").getAsString();
                                final androidx.appcompat.app.AlertDialog alert = alertDialog.show();
                                TextView updateText =  view.findViewById(R.id.id_updateText);
                                TextView head =  view.findViewById(R.id.head);
                                updateText.setTypeface(faceMedium);
                                head.setTypeface(faceMedium);
                                TextView update =  view.findViewById(R.id.id_update);
                                update.setTypeface(faceMedium);
                                TextView cancel =  view.findViewById(R.id.cancel);
                                update.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(!updateUrl.isEmpty() && !updateUrl.equals(""))
                                        {
                                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl)));
                                        }
                                    }
                                });
                                alert.show();
                                alert.setCancelable(false);

                            }
                        }
                    }
                    else
                    {
                    }

                      //  JsonObject result = object.get("result").getAsJsonObject();

                }else {
                    hideloader();
                    //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                    //  Toast.makeText(getBaseContext(), "Data Error",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                //  Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
            }

        });


    }



    public void showLoaderNew() {
        runOnUiThread(new SplashActivity.Runloader(getResources().getString(R.string.loading)));
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

    /*  private void download () {

          try {
              File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Download");
              //  File file = new File(Dir,"nahk.txt");
              File file = new File(Dir,  "/icomoon.ttf");
              if(file.exists()){
                  file.delete();
              }
          }
          catch (Exception e)
          {
              e.printStackTrace();
          }
          new DownloadFileFromURL().execute(FontUrl); // Download LINK !
      }
  */
    private void download () {

        //File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "My_IOT_Files");
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), "/icomoon.ttf");

        if (!mediaStorageDir.exists()) {

            if (!mediaStorageDir.mkdirs()) {

            }
            else {
                //  Toast.makeText(context, "Folder created", Toast.LENGTH_SHORT).show();
            }
        }


        if(mediaStorageDir.exists())
        {
            //Toast.makeText(context, "Downloading", Toast.LENGTH_SHORT).show();
            try {
                //   File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/My_IOT_Files");
                File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
                File file = new File(Dir,  "/icomoon.ttf");
                if(file.exists()){
                    file.delete();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            new DownloadFileFromURL().execute(FontUrl);
        }
    }


    // File download process from URL
    private class DownloadFileFromURL extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                int lenghtOfFile = conection.getContentLength();
                InputStream input = new BufferedInputStream(url.openStream(), 8192);
                OutputStream output = new FileOutputStream("/storage/emulated/0/icomoon.ttf");
                byte data[] = new byte[1024];
                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;
                    publishProgress(""+(int)((total*100)/lenghtOfFile));
                    output.write(data, 0, count);
                }
                output.flush();
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }
            return null;
        }
        @Override
        protected void onPostExecute(String file_url) {
            // Display the custom font after the File was downloaded !
            //  loadfont();
        }
    }
    public boolean isFontFileExist()
    {
        File Dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
        File file = new File(Dir,  "/icomoon.ttf");

        if(file.exists()){
            return true;
        }
        else {
            return false;
        }
    }


    @Override
    protected void onResume() {
       /* if (!isFontFileExist())
        {
            fontVersion = "0";
            serviceCallforVersionInfo();
        }*/
        super.onResume();
    }
}
