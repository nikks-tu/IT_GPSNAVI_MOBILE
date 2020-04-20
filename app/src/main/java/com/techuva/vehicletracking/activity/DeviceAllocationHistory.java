package com.techuva.vehicletracking.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.adapter.AllocationHistoryAdapter;
import com.techuva.vehicletracking.api_interface.AllVehicleInfoDataInterface;
import com.techuva.vehicletracking.listeners.EndlessScrollListener;
import com.techuva.vehicletracking.model.AllVehiclesDataModel;
import com.techuva.vehicletracking.model.DeviceAllocationHistoryModel;
import com.techuva.vehicletracking.model.DeviceAllocationHistoryPostParameters;
import com.techuva.vehicletracking.model.GetAllVehicleInfoPostParameters;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class DeviceAllocationHistory extends AppCompatActivity {

    LinearLayout ll_back_btn;
    Context context;
    TextView tv_vehicle_num, tv_allocated_on_text, tv_device_serial_num, tv_allocated_on, tv_data_not_found;
    TextView tv_device_serial_heading, tv_allocated_on_heading, tv_allocated_by_heading, tv_title;
    ListView lv_data_list;
    String  pagePerCount="30";
    int pageNumber = 1;
    int listCount = 0;
    int UserId;
    String authorityKey = "";
    int vehicleMasterId;
    String searchKey = "";
    AllocationHistoryAdapter adapter;
    ArrayList<DeviceAllocationHistoryModel> historyList;
    private EndlessScrollListener scrollListener;
    TextView tv_end_of_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_allocation_history);
        init();
        setTypeface();
        scrollListener = new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                if (pageNumber < listCount) {
                    pageNumber = pageNumber + 1;
                    loadNextDataFromApi(pageNumber, 200);
                    //tabChanged= false;
                } else {
                    pageNumber = 1;
                }
                //Mistake
                return true;
            }
        };

        adapter = new AllocationHistoryAdapter(R.layout.item_allocation_history, this, historyList);
        lv_data_list.setAdapter(adapter);

        lv_data_list.setOnScrollListener(scrollListener);
        serviceCall();

        ll_back_btn.setOnClickListener(v -> finish());

    }

    private void setTypeface() {
        Typeface faceMedium = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceLight = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getResources().getAssets(),
                "fonts/films.DENMARK.ttf");

        tv_vehicle_num.setTypeface(faceLight);
        tv_allocated_on_text.setTypeface(faceLight);
        tv_device_serial_num.setTypeface(faceLight);
        tv_allocated_on.setTypeface(faceLight);
        tv_data_not_found.setTypeface(faceLight);
        tv_device_serial_heading.setTypeface(faceLight);
        tv_allocated_on_heading.setTypeface(faceLight);
        tv_allocated_by_heading.setTypeface(faceLight);
        tv_title.setTypeface(faceLight);
        tv_end_of_list.setTypeface(faceLight);
    }


    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                serviceCall();
            }
        }, delay);
    }


    private void init() {
        context = DeviceAllocationHistory.this;
        ll_back_btn  = findViewById(R.id.ll_back_btn);
        lv_data_list  = findViewById(R.id.lv_data_list);
        tv_title  = findViewById(R.id.tv_title);
        tv_vehicle_num  = findViewById(R.id.tv_vehicle_num);
        tv_allocated_on_text  = findViewById(R.id.tv_allocated_on_text);
        tv_device_serial_num  = findViewById(R.id.tv_device_serial_num);
        tv_allocated_on  = findViewById(R.id.tv_allocated_on);
        tv_data_not_found  = findViewById(R.id.tv_data_not_found);
        tv_device_serial_heading  = findViewById(R.id.tv_device_serial_heading);
        tv_allocated_on_heading  = findViewById(R.id.tv_allocated_on_heading);
        tv_allocated_by_heading  = findViewById(R.id.tv_allocated_by_heading);

        View footerView = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer_layout, null, false);
        tv_end_of_list = footerView.findViewById(R.id.tv_end_of_list);
        lv_data_list.addFooterView(footerView);
        lv_data_list.setDivider(null);
        lv_data_list.setDivider(new ColorDrawable(context.getResources().getColor(android.R.color.transparent)));
        lv_data_list.setDividerHeight(0);
        authorityKey = "Bearer " + MApplication.getString(context, Constants.AccessToken);
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        historyList = new ArrayList<>();
        vehicleMasterId = Integer.parseInt(MApplication.getString(context, Constants.VehicleMasterID));
        tv_title.setText(context.getResources().getString(R.string.device_allocation_history));
    }


    private void serviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        AllVehicleInfoDataInterface service = retrofit.create(AllVehicleInfoDataInterface .class);
        Call<JsonElement> call = service.getDeviceAllocationHistory(UserId, authorityKey, new DeviceAllocationHistoryPostParameters(vehicleMasterId, searchKey, String.valueOf(pagePerCount), pageNumber));
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
                    pageNumber = info.get("pageNumber").getAsInt();
                    if(info.get("errorCode").getAsInt()==0)
                    {
                       // Toast.makeText(context, "Respo", Toast.LENGTH_SHORT).show();
                        tv_data_not_found.setVisibility(View.GONE);
                        lv_data_list.setVisibility(View.VISIBLE);
                        JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                        if(jsonArray.size()>0)
                        {
                            for (int i=0; i<jsonArray.size(); i++)
                            {
                                JsonObject jObj = jsonArray.get(i).getAsJsonObject();
                                DeviceAllocationHistoryModel model = new DeviceAllocationHistoryModel();
                                if(!jObj.get("VEHICLE_TAG_MAP_ID").isJsonNull())
                                {
                                    model.setVEHICLETAGMAPID(jObj.get("VEHICLE_TAG_MAP_ID").getAsInt());
                                }
                                if(!jObj.get("VEHICLE_MASTER_ID").isJsonNull())
                                {
                                    model.setVEHICLEMASTERID(jObj.get("VEHICLE_MASTER_ID").getAsInt());
                                }
                                if(!jObj.get("INVENTORY_ID").isJsonNull())
                                {
                                    model.setINVENTORYID(jObj.get("INVENTORY_ID").getAsInt());
                                }
                                if(!jObj.get("TAG_ASSIGNED_ON").isJsonNull())
                                {
                                    model.setTAGASSIGNEDON(jObj.get("TAG_ASSIGNED_ON").getAsString());
                                }
                                if(!jObj.get("TAG_REVOKED_ON").isJsonNull())
                                {
                                    model.setTAGREVOKEDON(jObj.get("TAG_REVOKED_ON").getAsString());
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
                                if(!jObj.get("TAG_NAME").isJsonNull())
                                {
                                    model.setTAGNAME(jObj.get("TAG_NAME").getAsString());
                                }
                                if(!jObj.get("REGISTRATION_NO").isJsonNull())
                                {
                                    model.setREGISTRATIONNO(jObj.get("REGISTRATION_NO").getAsString());
                                }
                                if(!jObj.get("VEHICLE_MODEL_DESCRIPTION").isJsonNull())
                                {
                                    model.setVEHICLEMODELDESCRIPTION(jObj.get("VEHICLE_MODEL_DESCRIPTION").getAsString());
                                }
                                if(!jObj.get("CREATED_BY_NAME").isJsonNull())
                                {
                                    model.setCREATEDBYNAME(jObj.get("CREATED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("LAST_MODIFIED_BY_NAME").isJsonNull())
                                {
                                    model.setLASTMODIFIEDBYNAME(jObj.get("LAST_MODIFIED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("TAG_ASSIGNED_BY_NAME").isJsonNull())
                                {
                                    model.setTAGASSIGNEDBYNAME(jObj.get("TAG_ASSIGNED_BY_NAME").getAsString());
                                }
                                if(!jObj.get("TAG_REVOKED_BY_NAME").isJsonNull())
                                {
                                    model.setTAGREVOKEDBYNAME(jObj.get("TAG_REVOKED_BY_NAME").getAsString());
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
                                tv_device_serial_num.setText(context.getResources().getString(R.string.device_serial_num)+""+historyList.get(0).getTAGNAME());
                                SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

                                Date date = null;
                                try {
                                    date = sdf.parse(historyList.get(0).getTAGASSIGNEDON());
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

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
