package com.techuva.vehicletracking.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.api_interface.DeallocationDataInterface;
import com.techuva.vehicletracking.model.DeviceDeallocationPostParameters;
import com.techuva.vehicletracking.model.DriverDeallocationPostParameters;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.adapter.SevenDaySummaryRcvAdapter;
import com.techuva.vehicletracking.api_interface.AllVehicleInfoDataInterface;
import com.techuva.vehicletracking.model.AllVehiclesDataModel;
import com.techuva.vehicletracking.model.SevenDaysDistanceModel;
import com.techuva.vehicletracking.utils.CustomTypefaceSpan;

import java.sql.Driver;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class VehicleDetailsActivity extends AppCompatActivity {

    Context context;
    LinearLayout toolbar_layout, ll_back_btn, ll_top_view, ll_vehicle_history, ll_bottom;
    LinearLayout ll_current_location, ll_driver_allocation, ll_device_allocation, ll_travel_summary;
    LinearLayout ll_total_travelled, ll_today_travelled;
    ImageView iv_back_btn, iv_vehicle_type;
    Toolbar toolbar;
    TextView tv_title, tv_vehicle_reg_num, tv_driver_name, tv_total_distance, tv_status_symbol;
    TextView tv_status, tv_tag_text, tv_tag, tv_current_location, tv_driver_allocation, tv_device_allocation, tv_travel_summary;
    TextView tv_total_txt, tv_total_travelled, tv_today_txt, tv_today_travelled, tv_data_not_found;
    RecyclerView rcv_data;
    LinearLayout ll_main, ll_no_internet;
    TextView tv_no_internet, tv_retry;
    int userId;
    String authorityKey;
    String registerNum;
    ArrayList<SevenDaysDistanceModel> dataList;
    ArrayList<AllVehiclesDataModel> allVehiclesDataList;
    public Dialog dialog;
    private AnimationDrawable animationDrawable;
    String vehicle_type, vehicle_status, vehicle_condition, driver_name, deviceId, title;
    ArrayList<String> popupItem;
    String fromButton="";
    SevenDaySummaryRcvAdapter adapter;
    String alertMsg="";
    String DeviceID;
    int VEHICLE_TAG_MAP_ID, INVENTORY_ID, VEHICLE_MASTER_ID, VEHICLE_DRIVER_ID;
    String VEHICLE_REG_NUM;
    String REMARKS;
    androidx.appcompat.app.AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_details);
        init();
    }

    private void init() {
        context = VehicleDetailsActivity.this;
        toolbar_layout = findViewById(R.id.toolbar_layout);
        toolbar = findViewById(R.id.toolbar);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        ll_top_view = findViewById(R.id.ll_top_view);
        ll_vehicle_history = findViewById(R.id.ll_vehicle_history);
        ll_bottom = findViewById(R.id.ll_bottom);
        ll_current_location = findViewById(R.id.ll_current_location);
        ll_driver_allocation = findViewById(R.id.ll_driver_allocation);
        ll_travel_summary = findViewById(R.id.ll_travel_summary);
        ll_device_allocation = findViewById(R.id.ll_device_allocation);
        ll_today_travelled = findViewById(R.id.ll_today_travelled);
        ll_total_travelled = findViewById(R.id.ll_total_travelled);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        iv_vehicle_type = findViewById(R.id.iv_vehicle_type);
        tv_title = findViewById(R.id.tv_title);
        tv_vehicle_reg_num = findViewById(R.id.tv_vehicle_reg_num);
        tv_driver_name = findViewById(R.id.tv_driver_name);
        tv_total_distance = findViewById(R.id.tv_total_distance);
        tv_status_symbol = findViewById(R.id.tv_status_symbol);
        tv_status = findViewById(R.id.tv_status);
        tv_tag_text = findViewById(R.id.tv_tag_text);
        tv_tag = findViewById(R.id.tv_tag);
        ll_main = findViewById(R.id.ll_main);
        ll_no_internet = findViewById(R.id.ll_no_internet);
        tv_no_internet = findViewById(R.id.tv_no_internet);
        tv_retry = findViewById(R.id.tv_retry);
        tv_current_location = findViewById(R.id.tv_current_location);
        tv_driver_allocation = findViewById(R.id.tv_driver_allocation);
        tv_device_allocation = findViewById(R.id.tv_device_allocation);
        tv_travel_summary = findViewById(R.id.tv_travel_summary);
        tv_total_txt = findViewById(R.id.tv_total_txt);
        tv_total_travelled = findViewById(R.id.tv_total_travelled);
        tv_today_txt = findViewById(R.id.tv_today_txt);
        tv_today_travelled = findViewById(R.id.tv_today_travelled);
        tv_data_not_found = findViewById(R.id.tv_data_not_found);
        rcv_data = findViewById(R.id.rcv_data);
        userId = Integer.parseInt(MApplication.getString(context, Constants.UserIDSelected));
        authorityKey = "Bearer "+MApplication.getString(context, Constants.AccessToken);
        registerNum  = MApplication.getString(context, Constants.RegistrationNum);
        allVehiclesDataList = new ArrayList<>();
        title = "Vehicle Details - "+registerNum;
        tv_title.setText(title);

        setTypeface();
        /*showLoaderNew();
        serviceCallForVehicleData();
        serviceCall();*/
        dataList = new ArrayList<>();
        popupItem = new ArrayList<>();
       // Toast.makeText(context, "OnCreate", Toast.LENGTH_SHORT).show();

        ll_device_allocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                fromButton = "Device";
               if(MApplication.isWorkingInternetPersent(context))
               {
                   popupItem = new ArrayList<>();
                   if (DeviceID.equals("null"))
                   {
                       DeviceID="";
                   }
                   if(DeviceID.equals("")){
                       popupItem.add("Allocate");
                       popupItem.add("History");
                   }
                   else {
                       popupItem.add("Reallocate");
                       popupItem.add("Deallocate");
                       popupItem.add("History");
                   }
                   showPopupMenu(popupItem, fromButton, ll_device_allocation);
               }
               else {
                   ll_main.setVisibility(View.GONE);
                   ll_no_internet.setVisibility(View.VISIBLE);
               }
            }
        });

        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        ll_driver_allocation.setOnClickListener(v -> {

            fromButton = "Driver";
            if(MApplication.isWorkingInternetPersent(context))
            {
                popupItem = new ArrayList<>();

                if(String.valueOf(VEHICLE_DRIVER_ID).equals("0")){
                    popupItem.add("Allocate");
                    popupItem.add("History");
                }
                else {
                    popupItem.add("Reallocate");
                    popupItem.add("Deallocate");
                    popupItem.add("History");
                }

                showPopupMenu(popupItem, fromButton, ll_driver_allocation);
            }

            else {
                ll_main.setVisibility(View.GONE);
                ll_no_internet.setVisibility(View.VISIBLE);
            }



        });

        ll_current_location.setOnClickListener(v -> {

            if(MApplication.isWorkingInternetPersent(context))
            {
                Intent intent = new Intent(context, MapActivity.class);
                if(allVehiclesDataList.size()>0)
                {

                    if(allVehiclesDataList.get(0).getLAT()!=null && allVehiclesDataList.get(0).getLANG()!=null)
                    {
                        intent.putExtra("From", Constants.VehicleDetails);
                        MApplication.setString(context, Constants.Latitude, allVehiclesDataList.get(0).getLAT().toString());
                        MApplication.setString(context, Constants.Longitude, allVehiclesDataList.get(0).getLANG().toString());
                        MApplication.setString(context, Constants.ReceivedTime, allVehiclesDataList.get(0).getLastSyncOn());
                        startActivity(intent);
                    }
                    else {
                     Toast.makeText(context, context.getResources().getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                    }

                }

            }

            else {
                ll_main.setVisibility(View.GONE);
                ll_no_internet.setVisibility(View.VISIBLE);
            }


        });


        ll_travel_summary.setOnClickListener(v -> {

            if(MApplication.isWorkingInternetPersent(context))
            {
                MApplication.setString(context, Constants.DeviceID, String.valueOf(INVENTORY_ID));
                MApplication.setString(context, Constants.RegistrationNum, VEHICLE_REG_NUM);
                Intent intent = new Intent(context, History_View_Activity.class);
                startActivity(intent);

            }
            else {
                ll_main.setVisibility(View.GONE);
                ll_no_internet.setVisibility(View.VISIBLE);
            }

        });

    }



    private void showPopupMenu(ArrayList<String> popupItem, String fButton, LinearLayout viewClick) {
        PopupMenu popup = new PopupMenu(VehicleDetailsActivity.this, viewClick);
        //Inflating the Popup using xml file


        for(int x=0; x<popupItem.size(); x++)
        {
            popup.getMenu().add(popupItem.get(x));
        }
        popup.getMenuInflater()
                .inflate(R.menu.popup_menu, popup.getMenu());

        popup.setOnMenuItemClickListener(item -> {

            String itemTitle = String.valueOf(item.getTitle());
            if(!itemTitle.equals(""))
            {
                switch (itemTitle)
                {
                    case "Allocate":
                        if(fButton.equals("Device"))
                        {
                            //MApplication.setString(context, Constants.VehicleMasterID, String.valueOf(VEHICLE_MASTER_ID));
                            Intent intent = new Intent(context, AllocateDeviceActivity.class);
                            startActivity(intent);
                        }
                        else
                        if(fButton.equals("Driver"))
                        {
                            //MApplication.setString(context, Constants.VehicleMasterID, String.valueOf(VEHICLE_MASTER_ID));
                            Intent intent = new Intent(context, AllocateDriverActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "Reallocate":
                        if(fButton.equals("Device"))
                        {
                            MApplication.setString(context, Constants.INVENTORY_ID, "");
                            Intent intent = new Intent(context, ReAllocateDeviceActivity.class);
                            startActivity(intent);
                        }
                        else
                        if(fButton.equals("Driver"))
                        {
                            MApplication.setString(context, Constants.VEHICLE_DRIVER_ID, "");
                            Intent intent = new Intent(context, ReAllocateDriverActivity.class);
                            startActivity(intent);
                        }
                        break;
                    case "Deallocate":
                        if(fButton.equals("Device"))
                        {
                            alertMsg = context.getResources().getString(R.string.device_deallocation_text);

                            showCustomDialog(alertMsg,registerNum,"",deviceId, fButton);
                        }
                        else if(fButton.equals("Driver"))
                        {
                            alertMsg = context.getResources().getString(R.string.driver_deallocation_text);
                            showCustomDialog(alertMsg,registerNum,driver_name,"", fButton);
                        }
                        break;
                    case "History":
                        if(fButton.equals("Device"))
                        {
                            MApplication.setString(context, Constants.VehicleMasterID, String.valueOf(VEHICLE_MASTER_ID));
                            Intent intent = new Intent(context, DeviceAllocationHistory.class);
                            startActivity(intent);

                        }
                        else  if(fButton.equals("Driver"))
                        {
                            MApplication.setString(context, Constants.VehicleMasterID, String.valueOf(VEHICLE_MASTER_ID));
                            Intent intent = new Intent(context, DriverAllocationHistory.class);
                            startActivity(intent);

                        }
                        break;
                }
            }
            return true;
        });
        Menu m = popup.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }

            //the method we have create in activity
            applyFontToMenuItem(mi);
        }
        popup.show();
    }

    private void setTypeface() {
        Typeface faceMedium = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceLight = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/films.DENMARK.ttf");

        tv_title.setTypeface(faceLight);
        tv_vehicle_reg_num.setTypeface(faceLight);
        tv_driver_name.setTypeface(faceLight);
        tv_total_distance.setTypeface(faceLight);
        tv_status_symbol.setTypeface(faceLight);
        tv_status.setTypeface(faceLight);
        tv_tag_text.setTypeface(faceLight);
        tv_tag.setTypeface(faceLight);
        tv_current_location.setTypeface(faceLight);
        tv_driver_allocation.setTypeface(faceLight);
        tv_device_allocation.setTypeface(faceLight);
        tv_travel_summary.setTypeface(faceLight);
        tv_total_txt.setTypeface(faceLight);
        tv_total_travelled.setTypeface(faceLight);
        tv_today_txt.setTypeface(faceLight);
        tv_today_travelled.setTypeface(faceLight);
        tv_data_not_found.setTypeface(faceLight);
        tv_retry.setTypeface(faceLight);
        tv_no_internet.setTypeface(faceLight);
    }

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
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
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
                                    VEHICLE_MASTER_ID = model.getVEHICLEID();
                                    MApplication.setString(context, Constants.VehicleMasterID, String.valueOf(VEHICLE_MASTER_ID));
                                }
                                if(!jObj.get("RegistrationNumber").isJsonNull() && !jObj.get("RegistrationNumber").getAsString().equals("")
                                && !jObj.get("RegistrationNumber").getAsString().isEmpty())
                                {
                                    model.setRegistrationNumber(jObj.get("RegistrationNumber").getAsString());
                                    VEHICLE_REG_NUM = model.getRegistrationNumber();
                                    MApplication.setString(context, Constants.RegistrationNum, VEHICLE_REG_NUM);
                                }
                                if(!jObj.get("VehicleType").isJsonNull())
                                {
                                    model.setVehicleType(jObj.get("VehicleType").getAsString());
                                }
                                if(!jObj.get("DRIVER_NAME").isJsonNull() && !jObj.get("DRIVER_NAME").getAsString().equals("") && !jObj.get("DRIVER_NAME").getAsString().equals(" "))
                                {
                                    model.setDRIVERNAME(jObj.get("DRIVER_NAME").getAsString());
                                }
                                else {
                                    model.setDRIVERNAME("--");
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
                                    INVENTORY_ID = model.getINVENTORYID();
                                }
                                if(!jObj.get("DeviceSerial").isJsonNull())
                                {
                                    model.setDeviceSerial(jObj.get("DeviceSerial").getAsString());
                                    DeviceID = model.getDeviceSerial();
                                }
                                else {
                                    DeviceID = "";
                                    model.setDeviceSerial("--");
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
                                if(!jObj.get("VEHICLE_TAG_MAP_ID").isJsonNull() && !jObj.get("VEHICLE_TAG_MAP_ID").getAsString().equals("")
                                && !jObj.get("VEHICLE_TAG_MAP_ID").getAsString().isEmpty())
                                {
                                    model.setVEHICLETAGMAPID(jObj.get("VEHICLE_TAG_MAP_ID").getAsInt());
                                    VEHICLE_TAG_MAP_ID = model.getVEHICLETAGMAPID();
                                    MApplication.setBoolean(context, Constants.IsDeviceAllocated, true);
                                }
                                else{
                                    VEHICLE_TAG_MAP_ID = 0;
                                    MApplication.setBoolean(context, Constants.IsDeviceAllocated, false);
                                }
                                if(!jObj.get("VEHICLE_DRIVER_ID").isJsonNull() && !jObj.get("VEHICLE_DRIVER_ID").getAsString().equals("")
                                && !jObj.get("VEHICLE_DRIVER_ID").getAsString().isEmpty())
                                {
                                    model.setVEHICLEDRIVERID(jObj.get("VEHICLE_DRIVER_ID").getAsInt());
                                    VEHICLE_DRIVER_ID = model.getVEHICLEDRIVERID();
                                    MApplication.setBoolean(context, Constants.IsDriverAllocated, true);
                                }
                                else {
                                    VEHICLE_DRIVER_ID= 0;
                                    MApplication.setBoolean(context, Constants.IsDriverAllocated, false);
                                }
                                if(!jObj.get("COMPANY_ID").isJsonNull())
                                {
                                    model.setCOMPANYID(jObj.get("COMPANY_ID").getAsInt());
                                }
                                if(!jObj.get("LAT").isJsonNull() && !jObj.get("LAT").getAsString().isEmpty()  && !jObj.get("LAT").getAsString().equals(""))
                                {
                                    model.setLAT(jObj.get("LAT").getAsDouble());
                                }
                                if(!jObj.get("LANG").isJsonNull()  && !jObj.get("LANG").getAsString().isEmpty()  && !jObj.get("LANG").getAsString().equals(""))
                                {
                                    model.setLANG(jObj.get("LANG").getAsDouble());
                                }
                                if(!jObj.get("LastSyncOn").isJsonNull())
                                {
                                    model.setLastSyncOn(jObj.get("LastSyncOn").getAsString());
                                }
                                if(!jObj.get("ConditionId").isJsonNull() && !jObj.get("ConditionId").getAsString().isEmpty() &&
                                        !jObj.get("ConditionId").getAsString().equals(""))
                                {
                                    model.setConditionId(jObj.get("ConditionId").getAsInt());
                                }
                                if(!jObj.get("Condition").isJsonNull())
                                {
                                    model.setCondition(jObj.get("Condition").getAsString());
                                }
                                allVehiclesDataList.add(model);
                            }
                            if(allVehiclesDataList.size()>0)
                            {
                                setData(allVehiclesDataList);
                            }
                        }
                    }

                }else {
                    // Log.println(1, "Empty", "Else");
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

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/AvenirLTStd-Light.otf");
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , font), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }



    private void showCustomDialog(String msg, String vehicleNum, String driverName, String deviceSerial, String fromButton) {
        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        Activity activity = this;
        ViewGroup viewGroup = activity.findViewById(android.R.id.content);
        //then we will inflate the custom alert dialog xml that we created
        View dialogView = LayoutInflater.from(context).inflate(R.layout.alert_for_popup, viewGroup, false);
        //Now we need an AlertDialog.Builder object
        androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //setting the view of the builder to our custom view that we already inflated
        builder.setView(dialogView);
        TextView tv_alertText = dialogView.findViewById(R.id.tv_alertText);
        TextView tv_heading = dialogView.findViewById(R.id.tv_heading);
        TextView button_yes = dialogView.findViewById(R.id.button_yes);
        TextView button_no = dialogView.findViewById(R.id.button_no);
        TextView tv_vehicle_num_text = dialogView.findViewById(R.id.tv_vehicle_num_text);
        TextView tv_vehicle_num = dialogView.findViewById(R.id.tv_vehicle_num);
        TextView tv_driver_name_text = dialogView.findViewById(R.id.tv_driver_name_text);
        TextView tv_driver_name = dialogView.findViewById(R.id.tv_driver_name);
        EditText edt_remarks = dialogView.findViewById(R.id.edt_remarks);
        REMARKS = edt_remarks.getText().toString();
        Typeface faceMedium = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceLight = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        tv_heading.setTypeface(faceMedium);
        tv_alertText.setTypeface(faceLight);
        button_yes.setTypeface(faceLight);
        button_no.setTypeface(faceLight);
        tv_driver_name_text.setTypeface(faceLight);
        tv_driver_name.setTypeface(faceLight);
        tv_vehicle_num_text.setTypeface(faceLight);
        tv_vehicle_num.setTypeface(faceLight);
        button_no.setText(context.getResources().getString(R.string.cancel));
        button_yes.setText(context.getResources().getString(R.string.deallocate));
        tv_alertText.setText(msg);
        tv_vehicle_num.setText(registerNum);
        if(fromButton.equals("Device"))
        {
            tv_driver_name_text.setText(context.getResources().getString(R.string.device_serial_num));
            tv_driver_name.setText(deviceSerial);
        }
        else {
            tv_driver_name_text.setText(context.getResources().getString(R.string.driver_name_));
            tv_driver_name.setText(driverName);
        }
        alertDialog = builder.create();
        alertDialog.show();
        button_yes.setOnClickListener(v -> {
            REMARKS = edt_remarks.getText().toString();
            if(!REMARKS.equals(""))
            {
                showLoaderNew();
                if(fromButton.equals("Device"))
                {
                    serviceCallForDeallocateDevice();
                }
                else {
                    serviceCallForDeallocateDriver();
                }
            }
            else
            {
                Toast.makeText(context, "Please enter remarks.", Toast.LENGTH_SHORT).show();
            }


        });

        button_no.setOnClickListener(v -> {
            alertDialog.dismiss();
        });

        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    protected void onResume() {
        if(MApplication.isWorkingInternetPersent(context))
        {
            callServices();
        }
        else {
            ll_main.setVisibility(View.GONE);
            ll_no_internet.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    private void callServices() {
      if(MApplication.isWorkingInternetPersent(context))
      {
          ll_main.setVisibility(View.VISIBLE);
          ll_no_internet.setVisibility(View.GONE);
          allVehiclesDataList.clear();
          dataList.clear();
          showLoaderNew();
          serviceCallForVehicleData();
          dataList = new ArrayList<>();
          serviceCall();
      }
      else {
          ll_main.setVisibility(View.GONE);
          ll_no_internet.setVisibility(View.VISIBLE);
      }
    }

    private void setData(ArrayList<AllVehiclesDataModel> allVehiclesDataList) {

        vehicle_type = allVehiclesDataList.get(0).getVehicleType();
        if(!vehicle_type.equals(""))
        {
            switch (vehicle_type)
            {
                case "CAR":
                    iv_vehicle_type.setImageDrawable(context.getResources().getDrawable(R.drawable.car));
                    break;
                case "VAN":
                    iv_vehicle_type.setImageDrawable(context.getResources().getDrawable(R.drawable.van));
                    break;
                case "TRUCK":
                    iv_vehicle_type.setImageDrawable(context.getResources().getDrawable(R.drawable.truck));
                    break;
            }
        }
        vehicle_condition = allVehiclesDataList.get(0).getCondition();
        if(!vehicle_condition.equals(""))
        {
            tv_status.setText(vehicle_condition);
            switch (vehicle_condition)
            {
                case "Running":
                    tv_status_symbol.setBackground(context.getResources().getDrawable(R.drawable.circle_background_running));
                    break;
                case "Idle":
                    tv_status_symbol.setBackground(context.getResources().getDrawable(R.drawable.circle_background_idle));
                    break;
                case "Offline":
                    tv_status_symbol.setBackground(context.getResources().getDrawable(R.drawable.circle_background_offline));
                    break;
                case "Unallocated":
                    tv_status_symbol.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
                    break;
            }
        }
        else{
            tv_status.setText("--");
            tv_status_symbol.setBackground(context.getResources().getDrawable(R.drawable.circle_background));
        }
        vehicle_status = allVehiclesDataList.get(0).getStatus();
        tv_tag.setText(vehicle_status);
        driver_name =allVehiclesDataList.get(0).getDRIVERNAME();
        tv_driver_name.setText( "Driver : "+driver_name);
        deviceId = allVehiclesDataList.get(0).getDeviceSerial();
        tv_total_distance.setText( "Device : "+deviceId);
        tv_vehicle_reg_num.setText(allVehiclesDataList.get(0).getRegistrationNumber());
        if(!String.valueOf(allVehiclesDataList.get(0).geteNTIRE_DISTANCE()).equals("null"))
        {
            tv_total_travelled.setText(allVehiclesDataList.get(0).geteNTIRE_DISTANCE()+allVehiclesDataList.get(0).gettOTALDISTANCE()+" Kms");
        }
        else {
            tv_total_travelled.setText("--");
        }
        if(!String.valueOf(allVehiclesDataList.get(0).gettOTALDISTANCE()).equals("null"))
        {
            tv_today_travelled.setText(allVehiclesDataList.get(0).gettOTALDISTANCE()+" Kms");
        }
        else {
            tv_today_travelled.setText("--");
        }
       /* if(!String.valueOf(allVehiclesDataList.get(0).gettOTALDISTANCE()).equals("null"))
        {
            tv_travel_summary.setText(allVehiclesDataList.get(0).gettOTALDISTANCE()+" Kms");
        }
        else {
            tv_travel_summary.setText("--");
        }*/


    }


    public void showLoaderNew() {
        runOnUiThread(new VehicleDetailsActivity.Runloader(getResources().getString(R.string.loading)));
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



    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface.class);
        Call<JsonElement> call = service.getSvenDaysRecords(userId, authorityKey, registerNum);
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){
                    JsonObject object = response.body().getAsJsonObject();
                    JsonObject info = object.get("info").getAsJsonObject();
                    if (info.get("errorCode").getAsInt()==0)
                    {
                        rcv_data.setVisibility(View.VISIBLE);
                        tv_data_not_found.setVisibility(View.GONE);
                        JsonArray result = object.get("result").getAsJsonArray();
                        if(result.size()>0)
                        {
                            dataList = new ArrayList<>();
                            for (int i=0; i<result.size(); i++)
                            {
                                SevenDaysDistanceModel model = new SevenDaysDistanceModel();
                                JsonObject jsonObject = result.get(i).getAsJsonObject();
                                if(jsonObject.has("REGISTRATION_NO") && !jsonObject.get("REGISTRATION_NO").isJsonNull()){
                                    model.setREGISTRATIONNO(jsonObject.get("REGISTRATION_NO").getAsString());
                                }
                                else {
                                    model.setREGISTRATIONNO("");
                                }
                                if(jsonObject.has("TOTAL_DISTANCE") && !jsonObject.get("TOTAL_DISTANCE").isJsonNull()){
                                    model.setTOTALDISTANCE(jsonObject.get("TOTAL_DISTANCE").getAsDouble());
                                }
                                if(jsonObject.has("TRAVELLED_ON") && !jsonObject.get("TRAVELLED_ON").isJsonNull())
                                {
                                    model.setTRAVELLEDON(jsonObject.get("TRAVELLED_ON").getAsString());
                                }
                                else {
                                    model.setTRAVELLEDON("");
                                }
                                dataList.add(model);
                            }
                        }
                        if(dataList.size()>0)
                        {
                            adapter = new SevenDaySummaryRcvAdapter(context, dataList, 0);
                            LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                            //manager.scrollToPosition(Integer.parseInt(month)-1);
                            rcv_data.setLayoutManager(manager);
                            rcv_data.setAdapter(adapter);
                        }
                    }
                    else {
                        rcv_data.setVisibility(View.GONE);
                        tv_data_not_found.setVisibility(View.VISIBLE);
                    }

                }else {
                    //Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
            }

        });

    }

    private void serviceCallForDeallocateDevice(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DeallocationDataInterface service = retrofit.create(DeallocationDataInterface.class);
        Call<JsonElement> call = service.deviceDeallocationCall(userId, authorityKey, new DeviceDeallocationPostParameters(VEHICLE_TAG_MAP_ID, INVENTORY_ID, REMARKS));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){
                    JsonObject object = response.body().getAsJsonObject();
                    JsonObject info = object.get("info").getAsJsonObject();
                    if (info.get("errorCode").getAsInt()==0)
                    {
                        Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_SHORT).show();
                       /* showLoaderNew();
                        dataList.clear();
                        allVehiclesDataList.clear();
                        showLoaderNew();
                        serviceCall();*/
                        MApplication.setString(context, Constants.INVENTORY_ID, "");
                        serviceCallForVehicleData();
                        if(MApplication.isWorkingInternetPersent(context))
                        {
                            callServices();
                        }
                        else {
                            ll_main.setVisibility(View.GONE);
                            ll_no_internet.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }else {

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
            }

        });

    }

    private void serviceCallForDeallocateDriver(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        DeallocationDataInterface service = retrofit.create(DeallocationDataInterface.class);

        Call<JsonElement> call = service.driverDeallocationCall(userId, authorityKey, new DriverDeallocationPostParameters(VEHICLE_MASTER_ID, VEHICLE_DRIVER_ID, REMARKS));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){
                    JsonObject object = response.body().getAsJsonObject();
                    JsonObject info = object.get("info").getAsJsonObject();
                    if (info.get("errorCode").getAsInt()==0)
                    {
                        Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_SHORT).show();
                        /*showLoaderNew();
                        dataList.clear();
                        allVehiclesDataList.clear();
                        serviceCallForVehicleData();
                        showLoaderNew();
                        serviceCall();*/
                        VEHICLE_DRIVER_ID = 0;
                        MApplication.setString(context,  Constants.VEHICLE_DRIVER_ID, "");
                        if(MApplication.isWorkingInternetPersent(context))
                        {
                            callServices();
                        }
                        else {
                            ll_main.setVisibility(View.GONE);
                            ll_no_internet.setVisibility(View.VISIBLE);
                        }
                    }
                    else {
                        Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_SHORT).show();
                    }
                    alertDialog.dismiss();
                }else {

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
            }

        });

    }


    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }



}
