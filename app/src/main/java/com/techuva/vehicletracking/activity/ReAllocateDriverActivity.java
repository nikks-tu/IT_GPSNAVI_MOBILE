package com.techuva.vehicletracking.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.adapter.DriverListRcvAdapter;
import com.techuva.vehicletracking.api_interface.AllVehicleInfoDataInterface;
import com.techuva.vehicletracking.model.AllVehiclesDataModel;
import com.techuva.vehicletracking.model.AvailableDriversListModel;
import com.techuva.vehicletracking.model.DeviceAllocationHistoryPostParameters;
import com.techuva.vehicletracking.model.DriverAllocationHistoryModel;
import com.techuva.vehicletracking.model.DriverAllocationPostParameters;
import com.techuva.vehicletracking.model.GetDeviceListPostParameters;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ReAllocateDriverActivity extends AppCompatActivity implements DriverListRcvAdapter.OnItemClicked {

    Context context;
    LinearLayout ll_back_btn, ll_data, ll_remarks;
    TextView tv_title, tv_vehicle_num, tv_allocated_on_text, tv_device_serial_num, tv_allocated_on;
    TextView tv_enter_remarks, tv_letter_count, tv_choose_device, tv_data_not_found;
    TextView tv_clear, tv_allocate;
    EditText edt_remarks;
    RecyclerView rcv_device_list;
    public Dialog dialog;
    FrameLayout fl_data;
    private AnimationDrawable animationDrawable;
    ArrayList<AvailableDriversListModel> driversList;

    ArrayList<DriverAllocationHistoryModel> historyList;
    int userId;
    String authorityKey;
    String registerNum;
    int VEHICLE_MASTER_ID;
    int VEHICLE_DRIVER_ID=0;
    String REMARKS ="";
    String searchKey="";
    String pagePerCount="40";
    int pageNumber=1;
    DriverListRcvAdapter adapter;

   int  vehicleMasterId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allocate_driver);
        init();

        setTypeface();

        showLoaderNew();
        serviceCallForInfo();
        serviceCallForVehicleData();
        serviceCallForDriverList();


        tv_allocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(MApplication.getString(context, Constants.VEHICLE_DRIVER_ID).equals(""))
                {
                    VEHICLE_DRIVER_ID =0;
                    Toast.makeText(context, "Please select the driver.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    VEHICLE_DRIVER_ID = Integer.parseInt(MApplication.getString(context, Constants.VEHICLE_DRIVER_ID));
                }
                REMARKS = edt_remarks.getText().toString();
                if(VEHICLE_DRIVER_ID!=0){
                    serviceCallForReAllocateDriver();
                }
            }
        });

        edt_remarks.addTextChangedListener(mTextEditorWatcher);


        tv_clear.setOnClickListener(v -> {
            showLoaderNew();
            edt_remarks.setText("");
            MApplication.setString(context, Constants.VEHICLE_DRIVER_ID, "");
            driversList.clear();
            driversList = new ArrayList<>();
            adapter = new DriverListRcvAdapter(context, driversList, new DriverListRcvAdapter.OnItemClicked () {
                @Override
                public void onItemClick(View view, int position) {
                }
            });
            rcv_device_list.setAdapter(adapter);
            serviceCallForDriverList();
        });

        ll_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void setTypeface() {
        Typeface faceLight = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        tv_vehicle_num.setTypeface(faceLight);
        tv_title.setTypeface(faceLight);
        tv_allocated_on_text.setTypeface(faceLight);
        tv_device_serial_num.setTypeface(faceLight);
        tv_allocated_on.setTypeface(faceLight);
        tv_enter_remarks.setTypeface(faceLight);
        tv_letter_count.setTypeface(faceLight);
        tv_choose_device.setTypeface(faceLight);
        tv_data_not_found.setTypeface(faceLight);
        edt_remarks.setTypeface(faceLight);
        tv_clear.setTypeface(faceLight);
        tv_allocate.setTypeface(faceLight);

    }

    private void init() {
        context = ReAllocateDriverActivity.this;
        ll_back_btn = findViewById(R.id.ll_back_btn);
        ll_data = findViewById(R.id.ll_data);
        ll_remarks = findViewById(R.id.ll_remarks);
        tv_title = findViewById(R.id.tv_title);
        tv_vehicle_num = findViewById(R.id.tv_vehicle_num);
        tv_allocated_on_text = findViewById(R.id.tv_allocated_on_text);
        tv_device_serial_num = findViewById(R.id.tv_device_serial_num);
        tv_allocated_on = findViewById(R.id.tv_allocated_on);
        tv_enter_remarks = findViewById(R.id.tv_enter_remarks);
        tv_letter_count = findViewById(R.id.tv_letter_count);
        tv_letter_count = findViewById(R.id.tv_letter_count);
        tv_choose_device = findViewById(R.id.tv_choose_device);
        tv_data_not_found = findViewById(R.id.tv_data_not_found);
        tv_clear = findViewById(R.id.tv_clear);
        edt_remarks = findViewById(R.id.edt_remarks);
        rcv_device_list = findViewById(R.id.rcv_device_list);
        fl_data = findViewById(R.id.fl_data);
        tv_allocate = findViewById(R.id.tv_allocate);
        historyList = new ArrayList<>();

        userId = Integer.parseInt(MApplication.getString(context, Constants.UserIDSelected));
        authorityKey = "Bearer "+MApplication.getString(context, Constants.AccessToken);
        registerNum  = MApplication.getString(context, Constants.RegistrationNum);
        driversList= new ArrayList<>();

        tv_title.setText(context.getResources().getString(R.string.driver_re_allocation));
        tv_allocate.setText(context.getResources().getString(R.string.re_allocate));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcv_device_list.setLayoutManager(linearLayoutManager);
        vehicleMasterId = Integer.parseInt(MApplication.getString(context, Constants.VehicleMasterID));
        adapter = new DriverListRcvAdapter(context, driversList, new DriverListRcvAdapter.OnItemClicked () {
            @Override
            public void onItemClick(View view, int position) {
            }
        });
        rcv_device_list.setAdapter(adapter);
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
                                }
                                if(!jObj.get("RegistrationNumber").isJsonNull())
                                {
                                    model.setRegistrationNumber(jObj.get("RegistrationNumber").getAsString());
                                    //tv_vehicle_num.setText(context.getResources().getString(R.string.vehicle_num)+" "+model.getRegistrationNumber());
                                }
                                if(!jObj.get("VehicleType").isJsonNull())
                                {
                                    model.setVehicleType(jObj.get("VehicleType").getAsString());
                                }
                                if(!jObj.get("DRIVER_NAME").isJsonNull())
                                {
                                    model.setDRIVERNAME(jObj.get("DRIVER_NAME").getAsString());
                                    //tv_device_serial_num.setText(context.getResources().getString(R.string.driver_name_)+" "+model.getDRIVERNAME());
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
                                    //INVENTORY_ID = model.getINVENTORYID();
                                }
                                if(!jObj.get("DeviceSerial").isJsonNull())
                                {
                                    model.setDeviceSerial(jObj.get("DeviceSerial").getAsString());
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
                                    //VEHICLE_TAG_MAP_ID = model.getVEHICLETAGMAPID();
                                }
                                if(!jObj.get("VEHICLE_DRIVER_ID").isJsonNull())
                                {
                                    model.setVEHICLEDRIVERID(jObj.get("VEHICLE_DRIVER_ID").getAsInt());
                                    //VEHICLE_DRIVER_ID = model.getVEHICLEDRIVERID();
                                }
                                if(!jObj.get("COMPANY_ID").isJsonNull())
                                {
                                    model.setCOMPANYID(jObj.get("COMPANY_ID").getAsInt());
                                }
                                if(!jObj.get("LAT").isJsonNull() && !jObj.get("LAT").getAsString().isEmpty()
                                && !jObj.get("LAT").getAsString().equals(""))
                                {
                                    model.setLAT(jObj.get("LAT").getAsDouble());
                                }
                                if(!jObj.get("LANG").isJsonNull()  && !jObj.get("LANG").getAsString().isEmpty()
                                        && !jObj.get("LANG").getAsString().equals(""))
                                {
                                    model.setLANG(jObj.get("LANG").getAsDouble());
                                }
                                if(!jObj.get("LastSyncOn").isJsonNull())
                                {
                                    model.setLastSyncOn(jObj.get("LastSyncOn").getAsString());
                                }
                                if(!jObj.get("ConditionId").isJsonNull()  && !jObj.get("ConditionId").getAsString().isEmpty()
                                        && !jObj.get("ConditionId").getAsString().equals(""))
                                {
                                    model.setConditionId(jObj.get("ConditionId").getAsInt());
                                }
                                if(!jObj.get("Condition").isJsonNull())
                                {
                                    model.setCondition(jObj.get("Condition").getAsString());
                                }

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

    private void serviceCallForDriverList() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface .class);
        Call<JsonElement> call = service.getDriversList(userId, authorityKey, new GetDeviceListPostParameters(searchKey, pagePerCount, pageNumber));
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
                    hideloader();
                    JsonObject jsonObject =response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();
                    if(info.get("errorCode").getAsInt()==0)
                    {
                        tv_data_not_found.setVisibility(View.GONE);
                        ll_data.setVisibility(View.VISIBLE);
                        ll_remarks.setVisibility(View.VISIBLE);
                        fl_data.setVisibility(View.VISIBLE);
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                        if(jsonArray.size()>0)
                        {
                            for (int i=0; i<jsonArray.size(); i++)
                            {
                                JsonObject jObj = jsonArray.get(i).getAsJsonObject();
                                AvailableDriversListModel model = new AvailableDriversListModel();
                                if(!jObj.get("VEHICLE_DRIVER_ID").isJsonNull()){
                                    model.setVEHICLE_DRIVER_ID(jObj.get("VEHICLE_DRIVER_ID").getAsInt());
                                }
                                if(!jObj.get("NAME").isJsonNull()){
                                    model.setNAME(jObj.get("NAME").getAsString());
                                }
                                if(!jObj.get("MOBILE_NO").isJsonNull()){
                                    model.setMOBILE_NO(jObj.get("MOBILE_NO").getAsString());
                                }
                                driversList.add(model);
                            }
                            if(driversList.size()>0)
                            {
                                adapter.notifyDataSetChanged();
                            }

                        }
                    }
                    else {
                        tv_data_not_found.setVisibility(View.VISIBLE);
                        ll_data.setVisibility(View.GONE);
                        fl_data.setVisibility(View.GONE);
                    }

                }else {
                    // Log.println(1, "Empty", "Else");
                }
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                ll_data.setVisibility(View.GONE);
                fl_data.setVisibility(View.GONE);
                tv_data_not_found.setVisibility(View.VISIBLE);
            }

        });

    }

    private void serviceCallForReAllocateDriver() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface .class);
        Call<JsonElement> call = service.reAllocateDriver(userId, authorityKey, new DriverAllocationPostParameters(VEHICLE_MASTER_ID, VEHICLE_DRIVER_ID, REMARKS));

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
                        Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else {
                        Toast.makeText(context, info.get("errorMessage").getAsString(), Toast.LENGTH_SHORT).show();
                        finish();
                    }

                }else {
                    // Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                hideloader();
                ll_data.setVisibility(View.GONE);
                tv_data_not_found.setVisibility(View.VISIBLE);
            }

        });

    }

    private void serviceCallForInfo() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface.class);
        Call<JsonElement> call = service.getDriverAllocationHistory(userId, authorityKey, new DeviceAllocationHistoryPostParameters(vehicleMasterId, searchKey, String.valueOf(pagePerCount), pageNumber));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                if (response.code()==401)
                {
                    MApplication.setBoolean(context, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(context, TokenExpireActivity.class);
                    startActivity(intent);
                }
                else if(response.body()!=null){
                    JsonObject jsonObject =response.body().getAsJsonObject();
                    JsonObject info = jsonObject.get("info").getAsJsonObject();
                    //listCount = info.get("listCount").getAsInt();
                    pageNumber = info.get("pageNumber").getAsInt();
                    if(info.get("errorCode").getAsInt()==0)
                    {
                        // Toast.makeText(context, "Respo", Toast.LENGTH_SHORT).show();
                        tv_data_not_found.setVisibility(View.GONE);
                       // lv_data_list.setVisibility(View.VISIBLE);
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                        if(jsonArray.size()>0)
                        {
                            for (int i=0; i<jsonArray.size(); i++)
                            {
                                JsonObject jObj = jsonArray.get(i).getAsJsonObject();
                                DriverAllocationHistoryModel model = new DriverAllocationHistoryModel();
                                if(!jObj.get("VEHICLE_DRIVER_MAP_ID").isJsonNull())
                                {
                                    model.setVEHICLEDRIVERMAPID(jObj.get("VEHICLE_DRIVER_MAP_ID").getAsInt());
                                }
                                if(!jObj.get("VEHICLE_MASTER_ID").isJsonNull())
                                {
                                    model.setVEHICLEMASTERID(jObj.get("VEHICLE_MASTER_ID").getAsInt());
                                }
                                /*if(!jObj.get("INVENTORY_ID").isJsonNull())
                                {
                                    model.setINVENTORYID(jObj.get("INVENTORY_ID").getAsInt());
                                }*/
                                if(!jObj.get("ASSIGNED_ON").isJsonNull())
                                {
                                    model.setASSIGNEDON(jObj.get("ASSIGNED_ON").getAsString());
                                }
                                if(!jObj.get("REVOKED_ON").isJsonNull())
                                {
                                    model.setREVOKEDON(jObj.get("REVOKED_ON").getAsString());
                                }
                                if(!jObj.get("IS_ACTIVE").isJsonNull())
                                {
                                    model.setISACTIVE(jObj.get("IS_ACTIVE").getAsBoolean());
                                }
                                if(!jObj.get("CREATED_ON").isJsonNull())
                                {
                                    model.setCREATEDON(jObj.get("CREATED_ON").getAsString());
                                }
                                if(!jObj.get("LAST_MODIFIED_ON").isJsonNull())
                                {
                                    model.setLASTMODIFIEDON(jObj.get("LAST_MODIFIED_ON").getAsString());
                                }
                                if(!jObj.get("DRIVER_NAME").isJsonNull())
                                {
                                    model.setDRIVERNAME(jObj.get("DRIVER_NAME").getAsString());
                                }
                                if(!jObj.get("REGISTRATION_NO").isJsonNull())
                                {
                                    model.setREGISTRATIONNO(jObj.get("REGISTRATION_NO").getAsString());
                                    tv_vehicle_num.setText(context.getResources().getString(R.string.vehicle_num)+" "+model.getREGISTRATIONNO());
                                }
                              /*  if(!jObj.get("VEHICLE_MODEL_DESCRIPTION").isJsonNull())
                                {
                                    model.setVEHICLEMODELDESCRIPTION(jObj.get("VEHICLE_MODEL_DESCRIPTION").getAsString());
                                }*/
                                if(!jObj.get("CREATED_BY_NAME").isJsonNull())
                                {
                                    model.setCREATEDBYNAME(jObj.get("CREATED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("LAST_MODIFIED_BY_NAME").isJsonNull())
                                {
                                    model.setLASTMODIFIEDBYNAME(jObj.get("LAST_MODIFIED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("ASSIGNED_BY_NAME").isJsonNull())
                                {
                                    model.setASSIGNEDBYNAME(jObj.get("ASSIGNED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("REVOKED_BY_NAME").isJsonNull())
                                {
                                    model.setREVOKEDBYNAME(jObj.get("REVOKED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("REMARKS").isJsonNull())
                                {
                                    model.setREMARKS(jObj.get("REMARKS").getAsString());
                                }
                                historyList.add(model);
                            }
                            if(historyList.size()>0)
                            {
                                tv_vehicle_num.setText(context.getResources().getString(R.string.vehicle_num)+" "+historyList.get(0).getREGISTRATIONNO());
                                tv_device_serial_num.setText(context.getResources().getString(R.string.driver_name_)+""+historyList.get(0).getDRIVERNAME());
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

                                Date date = null;
                                try {
                                    date = sdf.parse(historyList.get(0).getASSIGNEDON());
                                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                                    String dateToStr = format.format(date);
                                    //System.out.println(dateToStr);
                                    //holder.tv_date_value.setText(dateToStr);
                                } catch (ParseException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                } catch (java.text.ParseException e) {
                                    e.printStackTrace();
                                }
                                SimpleDateFormat timeFormatter = new SimpleDateFormat("dd-MM-yyyy HH:mm");
                                String sa = timeFormatter.format(date);
                                tv_allocated_on.setText(sa);
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }

                }else {
                    // Log.println(1, "Empty", "Else");
                }

            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
               /* hideloader();
                // Toast.makeText(context, "Error connecting server" , Toast.LENGTH_SHORT).show();
                ll_main.setVisibility(View.GONE);
                rl_serverError.setVisibility(View.VISIBLE);*/
            }

        });

    }


    public void showLoaderNew() {
        runOnUiThread(new ReAllocateDriverActivity.Runloader(getResources().getString(R.string.loading)));
    }

    @Override
    public void onItemClick(View view, int position) {

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


    @Override
    public void onBackPressed() {

        finish();
        super.onBackPressed();
    }

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tv_letter_count.setText(s.length()+"/80");
        }

        public void afterTextChanged(Editable s) {
        }
    };

}
