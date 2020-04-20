package com.techuva.vehicletracking.activity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.MainActivity;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.api_interface.AllVehicleInfoDataInterface;
import com.techuva.vehicletracking.model.AllVehiclesDataModel;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    GoogleMap googleMap;
    SupportMapFragment supportMapFragment;
    double latitude;
    double longitude;
    Toolbar toolbar;
    TextView tv_received_time, tv_vehicle_reg_num, tv_address, tv_received_time_txt, tv_address_txt, tv_vehicle_num;
    String recievedTime;
    String regNum;
    ImageView iv_back_btn, iv_refresh;
    String fromActivity="";
    Geocoder geocoder;
    List<Address> addresses;
    Context mContext;
    int userId;
    String authorityKey;
    String registerNum;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    MarkerOptions marker;
    LatLng mark;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);
        if(getIntent()!=null)
        {
            fromActivity = getIntent().getStringExtra("From");
          //  Toast.makeText(getApplicationContext(), fromActivity, Toast.LENGTH_SHORT).show();
        }
        toolbar = findViewById(R.id.toolbar);
        mContext = MapActivity.this;
        userId = Integer.parseInt(MApplication.getString(mContext, Constants.UserIDSelected));
        authorityKey = "Bearer "+MApplication.getString(mContext, Constants.AccessToken);
        registerNum  = MApplication.getString(mContext, Constants.RegistrationNum);

        Typeface faceDenmark = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/films.DENMARK.ttf");
        Typeface faceMedium = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        TextView tv_title = findViewById(R.id.tv_title);
        tv_title.setText(mContext.getResources().getString(R.string.app_name));
        tv_title.setTypeface(faceDenmark);
        tv_received_time = findViewById(R.id.tv_received_time);
        tv_vehicle_reg_num = findViewById(R.id.tv_vehicle_reg_num);
        tv_address_txt = findViewById(R.id.tv_address_txt);
        tv_received_time_txt = findViewById(R.id.tv_received_time_txt);
        tv_address = findViewById(R.id.tv_address);
        tv_vehicle_num = findViewById(R.id.tv_vehicle_num);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        iv_refresh = findViewById(R.id.iv_refresh);
        iv_refresh.setVisibility(View.VISIBLE);
        //toolbar.setTitle("Vehicle Current Location");
        tv_received_time.setTypeface(faceMedium);
        tv_vehicle_reg_num.setTypeface(faceMedium);
        tv_address.setTypeface(faceMedium);
        tv_received_time_txt.setTypeface(faceMedium);
        tv_address_txt.setTypeface(faceMedium);
        tv_vehicle_num.setTypeface(faceMedium);
        regNum = MApplication.getString(getApplicationContext(), Constants.RegistrationNum);
        recievedTime = MApplication.getString(getApplicationContext(), Constants.ReceivedTime);
        tv_vehicle_reg_num.setText(regNum);
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
        //2020-03-03 09:32:09.0
        Date date1 = null;

        if(!recievedTime.equals("") && !recievedTime.isEmpty())
        {
            try {
                date1 = sdf1.parse(recievedTime);
            } catch (ParseException | java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            if(date1!=null){
                tv_received_time.setText(sdf3.format(date1));
            }
            else {
                try {
                    date1 = sdf.parse(recievedTime);
                } catch (ParseException | java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                tv_received_time.setText(sdf3.format(date1));
            }
        }

        else {
            tv_received_time.setText("");
        }
        supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);

        if(!fromActivity.equals(Constants.Home))
        {
            iv_refresh.setVisibility(View.GONE);
        }
        else {
            iv_refresh.setVisibility(View.VISIBLE);
        }


        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        iv_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLoaderNew();
                serviceCallForVehicleData();

                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(marker!= null)
                {
                    googleMap.clear();
                }
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy hh:mm a");
                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
                SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
                //2020-03-03 09:32:09.0
                Date date1 = null;

                if(!recievedTime.equals("") && !recievedTime.isEmpty())
                {
                    try {
                        date1 = sdf1.parse(recievedTime);
                    } catch (ParseException | java.text.ParseException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    if(date1!=null){
                        tv_received_time.setText(sdf3.format(date1));
                    }
                    else {
                        try {
                            date1 = sdf.parse(recievedTime);
                        } catch (ParseException | java.text.ParseException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        tv_received_time.setText(sdf3.format(date1));
                    }
                }


                if(addresses.size()>0)
                {
                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
                    String state = addresses.get(0).getAdminArea();
                    String country = addresses.get(0).getCountryName();
                    String postalCode = addresses.get(0).getPostalCode();
                    String knownName = addresses.get(0).getFeatureName();
                    String addressLine = address+", \n"+city+", "+state +", \n"+country+", "+postalCode;
                    marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(addressLine);
                    mark = new LatLng(latitude, longitude);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 10));
                    tv_address.setText(addressLine);

                    if(marker!= null)
                    {
                        googleMap.clear();
                    }
                    googleMap.addMarker(marker).showInfoWindow();
                }
                else {
                    //Toast.makeText(getApplicationContext(), ""+latitude+", "+longitude, Toast.LENGTH_SHORT).show();
                    marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Unnamed Location");
                    mark = new LatLng(latitude, longitude);
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 10));
                    // adding marker
                    if(marker!= null)
                    {
                        googleMap.clear();
                    }
                    googleMap.addMarker(marker).showInfoWindow();
                }


            }
        });



        // Getting a reference to the map
        assert supportMapFragment != null;
        // supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        googleMap = map;
        latitude = Double.parseDouble(MApplication.getString(getApplicationContext(), Constants.Latitude));
        longitude = Double.parseDouble(MApplication.getString(getApplicationContext(), Constants.Longitude));

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(addresses.size()>0)
        {
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            String addressLine = address+", \n"+city+", "+state +", \n"+country+", "+postalCode;
            marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title(addressLine);
            mark = new LatLng(latitude, longitude);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 10));
            tv_address.setText(addressLine);
            googleMap.addMarker(marker).showInfoWindow();
        }
        else {
            //Toast.makeText(getApplicationContext(), ""+latitude+", "+longitude, Toast.LENGTH_SHORT).show();
            marker = new MarkerOptions().position(new LatLng(latitude, longitude)).title("Unnamed Location");
            mark = new LatLng(latitude, longitude);
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(mark, 10));
            // adding marker
            googleMap.addMarker(marker).showInfoWindow();
        }


    }


    @Override
    protected void onResume() {
        supportMapFragment.getMapAsync(this);
        super.onResume();
    }


    @Override
    public void onBackPressed() {

        if(!fromActivity.equals("")){
            if(fromActivity.equals(Constants.History)){
                Intent intent = new Intent(MapActivity.this, History_View_Activity.class);
                startActivity(intent);
            }  else if(fromActivity.equals(Constants.Home)) {
                Intent intent = new Intent(MapActivity.this, MainActivity.class);
                startActivity(intent);
            }
        }

        else {
            Intent intent = new Intent(MapActivity.this, MainActivity.class);
            startActivity(intent);
        }
        super.onBackPressed();
    }

    public void showLoaderNew() {
        runOnUiThread(new MapActivity.Runloader(getResources().getString(R.string.loading)));
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
                    dialog = new Dialog(mContext,R.style.Theme_AppCompat_Light_DarkActionBar);
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


    //marker.setPosition(newLatLng);
    private void serviceCallForVehicleData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface .class);
        Call<JsonElement> call = service.getSingleVehicleDetails(userId, authorityKey, registerNum);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                hideloader();
                if (response.code()==401)
                {
                  /*  MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);*/
                }
                else if(response.body()!=null){
                    JsonObject jsonObject =response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();
                    if(info.get("errorCode").getAsInt()==0)
                    {
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                        if(jsonArray.size()>0)
                        {
                            for (int i=0; i<jsonArray.size(); i++)
                            {
                                JsonObject jObj = jsonArray.get(i).getAsJsonObject();
                                AllVehiclesDataModel model = new AllVehiclesDataModel();
                                if(!jObj.get("VEHICLE_ID").isJsonNull()){
                                    model.setVEHICLEID(jObj.get("VEHICLE_ID").getAsInt());
                                   /// VEHICLE_MASTER_ID = model.getVEHICLEID();
                                }
                                if(!jObj.get("RegistrationNumber").isJsonNull())
                                {
                                    model.setRegistrationNumber(jObj.get("RegistrationNumber").getAsString());
                                }
                                if(!jObj.get("VehicleType").isJsonNull())
                                {
                                    model.setVehicleType(jObj.get("VehicleType").getAsString());
                                }
                                if(!jObj.get("DRIVER_NAME").isJsonNull())
                                {
                                    model.setDRIVERNAME(jObj.get("DRIVER_NAME").getAsString());
                                }
                                if(!jObj.get("StatusID").isJsonNull())
                                {
                                    model.setStatusID(jObj.get("StatusID").getAsInt());
                                }
                                if(!jObj.get("Status").isJsonNull())
                                {
                                    model.setStatus(jObj.get("Status").getAsString());
                                }
                                if(!jObj.get("INVENTORY_ID").isJsonNull())
                                {
                                    model.setINVENTORYID(jObj.get("INVENTORY_ID").getAsInt());
                                   // INVENTORY_ID = model.getINVENTORYID();
                                }
                                if(!jObj.get("DeviceSerial").isJsonNull())
                                {
                                    model.setDeviceSerial(jObj.get("DeviceSerial").getAsString());
                                   // DeviceID = model.getDeviceSerial();
                                }
                                else {
                                   // DeviceID = "";
                                }
                                if(!jObj.get("TOTAL_DISTANCE").isJsonNull() && !jObj.get("TOTAL_DISTANCE").getAsString().equals("null"))
                                {
                                    model.settOTALDISTANCE(jObj.get("TOTAL_DISTANCE").getAsDouble());
                                    // DeviceID = "";
                                }
                                if(!jObj.get("ENTIRE_DISTANCE").isJsonNull() && !jObj.get("ENTIRE_DISTANCE").getAsString().equals("null"))
                                {
                                    model.seteNTIRE_DISTANCE(jObj.get("ENTIRE_DISTANCE").getAsDouble());
                                }
                                if(!jObj.get("VEHICLE_TAG_MAP_ID").isJsonNull())
                                {
                                    model.setVEHICLETAGMAPID(jObj.get("VEHICLE_TAG_MAP_ID").getAsInt());
                                   // VEHICLE_TAG_MAP_ID = model.getVEHICLETAGMAPID();
                                }
                                if(!jObj.get("VEHICLE_DRIVER_ID").isJsonNull())
                                {
                                    model.setVEHICLEDRIVERID(jObj.get("VEHICLE_DRIVER_ID").getAsInt());
                                   // VEHICLE_DRIVER_ID = model.getVEHICLEDRIVERID();
                                }
                                if(!jObj.get("COMPANY_ID").isJsonNull())
                                {
                                    model.setCOMPANYID(jObj.get("COMPANY_ID").getAsInt());
                                }
                                if(!jObj.get("LAT").isJsonNull())
                                {
                                    model.setLAT(jObj.get("LAT").getAsDouble());
                                    latitude = model.getLAT();
                                }
                                if(!jObj.get("LANG").isJsonNull())
                                {
                                    model.setLANG(jObj.get("LANG").getAsDouble());
                                    longitude = model.getLANG();
                                }
                                if(!jObj.get("LastSyncOn").isJsonNull())
                                {
                                    model.setLastSyncOn(jObj.get("LastSyncOn").getAsString());
                                    recievedTime = model.getLastSyncOn();
                                }
                                if(!jObj.get("ConditionId").isJsonNull())
                                {
                                    model.setConditionId(jObj.get("ConditionId").getAsInt());
                                }
                                if(!jObj.get("Condition").isJsonNull())
                                {
                                    model.setCondition(jObj.get("Condition").getAsString());
                                }
                                mark = new LatLng(latitude, longitude);

                              //  marker.setPosition(mark);

                            }
                        }
                    }

                }else {
                    // Log.println(1, "Empty", "Else");
                    hideloader();
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                // Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                /*ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);*/
            }

        });

    }

}
