package com.techuva.vehicletracking.ui.home;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.activity.MapActivity;
import com.techuva.vehicletracking.activity.MonthHistoryActivity;
import com.techuva.vehicletracking.activity.TokenExpireActivity;
import com.techuva.vehicletracking.activity.VehicleDetailsActivity;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.adapter.AllVehicleDataAdapter;
import com.techuva.vehicletracking.api_interface.AllVehicleInfoDataInterface;
import com.techuva.vehicletracking.listeners.EndlessScrollListener;
import com.techuva.vehicletracking.listeners.GenerateMap;
import com.techuva.vehicletracking.listeners.GoToHistory;
import com.techuva.vehicletracking.model.AllVehiclesDataModel;
import com.techuva.vehicletracking.model.GetAllVehicleInfoPostParameters;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class HomeFragment extends Fragment implements GoToHistory , GenerateMap {

    private HomeViewModel homeViewModel;
    private LinearLayout ll_top_view, ll_total_vehicles, ll_running, ll_idle, ll_offline, ll_unallocated;
    private TextView tv_total_vehicles,tv_total_vehicles_txt, tv_running, tv_running_txt, tv_idle,
            tv_unallocated_txt, tv_unallocated, tv_idle_txt,tv_offline, tv_offline_txt;
    ListView lv_vehicles;
    Context context;
    int userId;
    String authorityKey;
    int pagePerCount, pageNum;
    ArrayList<AllVehiclesDataModel> allVehiclesDataList;
    AllVehicleDataAdapter adapter;
    private EndlessScrollListener scrollListener;
    int listCount=0;
    int idleCount, totalCount, runningCount, offlineCount, unallocatedCount;
    String searchKey="";

    //Views for Internet check
    LinearLayout ll_main, ll_no_internet;
    TextView tv_no_internet, tv_retry;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        context = getContext();
        assert context != null;
        ll_top_view = root.findViewById(R.id.ll_top_view);
        ll_total_vehicles = root.findViewById(R.id.ll_total_vehicles);
        ll_running = root.findViewById(R.id.ll_running);
        ll_idle = root.findViewById(R.id.ll_idle);
        ll_unallocated = root.findViewById(R.id.ll_unallocated);
        ll_offline = root.findViewById(R.id.ll_offline);
        tv_total_vehicles = root.findViewById(R.id.tv_total_vehicles);
        tv_total_vehicles_txt = root.findViewById(R.id.tv_total_vehicles_txt);
        tv_running = root.findViewById(R.id.tv_running);
        tv_running_txt = root.findViewById(R.id.tv_running_txt);
        tv_idle = root.findViewById(R.id.tv_idle);
        tv_idle_txt = root.findViewById(R.id.tv_idle_txt);
        tv_offline = root.findViewById(R.id.tv_offline);
        tv_offline_txt = root.findViewById(R.id.tv_offline_txt);
        tv_unallocated = root.findViewById(R.id.tv_unallocated);
        tv_unallocated_txt = root.findViewById(R.id.tv_unallocated_txt);
        lv_vehicles = root.findViewById(R.id.lv_vehicles);
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(), "fonts/AvenirLTStd-Light.otf");

        ll_main = root.findViewById(R.id.ll_main);
        ll_no_internet = root.findViewById(R.id.ll_no_internet);
        tv_no_internet = root.findViewById(R.id.tv_no_internet);
        tv_retry = root.findViewById(R.id.tv_retry);
        tv_retry.setTypeface(faceLight);
        tv_no_internet.setTypeface(faceLight);
        tv_total_vehicles_txt.setTypeface(faceLight);
        tv_running.setTypeface(faceLight);
        tv_running_txt.setTypeface(faceLight);
        tv_idle.setTypeface(faceLight);
        tv_idle_txt.setTypeface(faceLight);
        tv_offline.setTypeface(faceLight);
        tv_offline_txt.setTypeface(faceLight);
        tv_total_vehicles.setTypeface(faceLight);
        tv_unallocated_txt.setTypeface(faceLight);
        tv_unallocated.setTypeface(faceLight);
        userId = Integer.parseInt(MApplication.getString(context, Constants.UserIDSelected));
        authorityKey = "Bearer "+MApplication.getString(context, Constants.AccessToken);
        pageNum = 1;
        pagePerCount = 20;
        allVehiclesDataList = new ArrayList<>();
        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (pageNum < listCount) {
                    pageNum = pageNum + 1;
                    loadNextDataFromApi(pageNum, 200);
                } else {
                    pageNum = 1;
                }
                return true;
            }
        };

        adapter = new AllVehicleDataAdapter(R.layout.item_all_vehicle_data, getContext(), allVehiclesDataList, HomeFragment.this, HomeFragment.this);
        lv_vehicles.setAdapter(adapter);
        lv_vehicles.setOnScrollListener(scrollListener);
        /*serviceCallForVehicleCount();
        serviceCallForVehicleData();*/

        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callServices();
            }
        });

        ll_idle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idleCount>0){
                    searchKey = "Idle";
                    allVehiclesDataList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Idle", Toast.LENGTH_SHORT).show();
                    serviceCallForVehicleData();
                }
            }
        });

        ll_running.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idleCount>0){
                    searchKey = "Idle";
                    allVehiclesDataList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Running", Toast.LENGTH_SHORT).show();
                    serviceCallForVehicleData();
                }
            }
        });

        ll_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idleCount>0){
                    searchKey = "Idle";
                    allVehiclesDataList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Offline", Toast.LENGTH_SHORT).show();
                    serviceCallForVehicleData();
                }
            }
        });

        ll_total_vehicles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idleCount>0){
                    searchKey = "Idle";
                    allVehiclesDataList.clear();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(context, "Total Vehicles", Toast.LENGTH_SHORT).show();
                    serviceCallForVehicleData();
                }
            }
        });

        return root;
    }


    private void loadNextDataFromApi(int page, int delay) {
        pageNum = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                serviceCallForVehicleData();
            }
        }, delay);
    }

    private void serviceCallForVehicleData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface .class);
        Call<JsonElement> call = service.getStringScalarWithSession(userId, authorityKey, new GetAllVehicleInfoPostParameters(searchKey, String.valueOf(pagePerCount), String.valueOf(pageNum)));
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
                    listCount = info.get("listCount").getAsInt();
                    pageNum = info.get("pageNumber").getAsInt();
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
                                }
                                if(!jObj.get("RegistrationNumber").isJsonNull())
                                {
                                    model.setRegistrationNumber(jObj.get("RegistrationNumber").getAsString());
                                }
                                if(!jObj.get("VehicleType").isJsonNull())
                                {
                                    model.setVehicleType(jObj.get("VehicleType").getAsString());
                                }
                                if(!jObj.get("DRIVER_NAME").isJsonNull() && !jObj.get("DRIVER_NAME").getAsString().equals("") && !jObj.get("DRIVER_NAME").getAsString().equals(""))
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
                                }
                                if(!jObj.get("DeviceSerial").isJsonNull())
                                {
                                    model.setDeviceSerial(jObj.get("DeviceSerial").getAsString());
                                }
                                if(!jObj.get("TOTAL_DISTANCE").isJsonNull() && !jObj.get("TOTAL_DISTANCE").getAsString().equals("null"))
                                {
                                    model.settOTALDISTANCE(jObj.get("TOTAL_DISTANCE").getAsDouble());
                                }
                                if(!jObj.get("ENTIRE_DISTANCE").isJsonNull() && !jObj.get("ENTIRE_DISTANCE").getAsString().equals("null"))
                                {
                                    model.seteNTIRE_DISTANCE(jObj.get("ENTIRE_DISTANCE").getAsDouble());
                                }
                                if(!jObj.get("VEHICLE_TAG_MAP_ID").isJsonNull())
                                {
                                    model.setVEHICLETAGMAPID(jObj.get("VEHICLE_TAG_MAP_ID").getAsInt());
                                }
                                if(!jObj.get("VEHICLE_DRIVER_ID").isJsonNull())
                                {
                                    model.setVEHICLEDRIVERID(jObj.get("VEHICLE_DRIVER_ID").getAsInt());
                                }
                                if(!jObj.get("COMPANY_ID").isJsonNull())
                                {
                                    model.setCOMPANYID(jObj.get("COMPANY_ID").getAsInt());
                                }
                                if(!jObj.get("LAT").isJsonNull() && !jObj.get("LAT").equals("") && !jObj.get("LAT").getAsString().isEmpty())
                                {
                                    model.setLAT(jObj.get("LAT").getAsDouble());
                                }
                                if(!jObj.get("LANG").isJsonNull() && !jObj.get("LANG").equals("") && !jObj.get("LANG").getAsString().isEmpty())
                                {
                                    model.setLANG(jObj.get("LANG").getAsDouble());
                                }
                                if(!jObj.get("LastSyncOn").isJsonNull())
                                {
                                    model.setLastSyncOn(jObj.get("LastSyncOn").getAsString());
                                }
                                if(!jObj.get("ConditionId").isJsonNull() && !jObj.get("ConditionId").getAsString().equals(""))
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

    private void serviceCallForVehicleCount() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface.class);
        Call<JsonElement> call = service.getCurrentVehicleCount(userId, authorityKey);
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
                    if(info.get("errorCode").getAsInt()==0)
                    {
                        JsonObject object = jsonObject.get("result").getAsJsonObject();
                        if(!object.isJsonNull())
                        {
                            if(!object.get("Total").isJsonNull())
                            {
                                tv_total_vehicles.setText(String.valueOf(object.get("Total").getAsInt()));
                                totalCount = object.get("Total").getAsInt();
                            }
                            if(!object.get("Running").isJsonNull())
                            {
                                tv_running.setText(String.valueOf(object.get("Running").getAsInt()));
                                runningCount = object.get("Running").getAsInt();
                            }
                            if(!object.get("Idle").isJsonNull())
                            {
                                tv_idle.setText(String.valueOf(object.get("Idle").getAsInt()));
                                idleCount = object.get("Idle").getAsInt();
                            }
                            if(!object.get("Offline").isJsonNull())
                            {
                                tv_offline.setText(String.valueOf(object.get("Offline").getAsInt()));
                                offlineCount = object.get("Offline").getAsInt();
                            }
                            if(!object.get("Unallocated").isJsonNull())
                            {
                                tv_unallocated.setText(String.valueOf(object.get("Unallocated").getAsInt()));
                                unallocatedCount = object.get("Unallocated").getAsInt();
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


    @Override
    public void generateMap(String num, String latitude, String longitude) {
       if(MApplication.isWorkingInternetPersent(context))
       {
           if(num.equals("map"))
           {
               MApplication.setString(context, Constants.Latitude, latitude);
               MApplication.setString(context, Constants.Longitude, longitude);
               Intent intent = new Intent(context, MapActivity.class);
               intent.putExtra("From", Constants.Home);
               startActivity(intent);
           }
       }
       else {
           ll_main.setVisibility(View.GONE);
           ll_no_internet.setVisibility(View.VISIBLE);
       }
    }

    @Override
    public void update(String num) {
       /* if(!num.equals(""))
        {
            MApplication.setString(context, Constants.DeviceID, num);
            Intent intent = new Intent(context, History_View_Activity.class);
            startActivity(intent);
        }*/

       if(MApplication.isWorkingInternetPersent(context))
       {
           if(num.equals("sum"))
           {
               Intent intent = new Intent(context, MonthHistoryActivity.class);
               startActivity(intent);
           }
           else if(num.equals("details")){
               Intent intent = new Intent(context, VehicleDetailsActivity.class);
               startActivity(intent);
           }
       }
       else {
           ll_main.setVisibility(View.GONE);
           ll_no_internet.setVisibility(View.VISIBLE);
       }
    }

    @Override
    public void updateYear(String year) {

    }

    public static HomeFragment newInstance() {

        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }





    @Override
    public void onResume() {

       callServices();
       super.onResume();
    }

    private void callServices() {
        if( MApplication.isWorkingInternetPersent(context))
        {
            pageNum = 1;
            allVehiclesDataList.clear();
            if(adapter!=null)
            {
                adapter.notifyDataSetChanged();
            }
            ll_main.setVisibility(View.VISIBLE);
            ll_no_internet.setVisibility(View.GONE);
            loadNextDataFromApi(pageNum, 100);
            serviceCallForVehicleCount();
        }
        else {
            ll_main.setVisibility(View.GONE);
            ll_no_internet.setVisibility(View.VISIBLE);
        }
    }

}