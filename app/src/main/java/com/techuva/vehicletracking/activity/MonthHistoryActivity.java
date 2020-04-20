package com.techuva.vehicletracking.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.MainActivity;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.adapter.ListAdapter;
import com.techuva.vehicletracking.adapter.MonthDataRcvAdapter;
import com.techuva.vehicletracking.adapter.VehicleSummaryRcvAdapter;
import com.techuva.vehicletracking.api_interface.HistoryDataInterface;
import com.techuva.vehicletracking.listeners.GoToHistory;
import com.techuva.vehicletracking.listeners.RecyclerItemClickListener;
import com.techuva.vehicletracking.model.HistoryDataPostParamter;
import com.techuva.vehicletracking.model.MonthsObject;
import com.techuva.vehicletracking.model.VehicleSummaryObject;
import com.techuva.vehicletracking.model.YearsObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MonthHistoryActivity extends AppCompatActivity implements GoToHistory {

    ImageView iv_back_btn, iv_back_month, iv_next_month;
    LinearLayout ll_data;
    Toolbar toolbar;
    CardView cv_year;
    TextView tv_year, tv_total_distance, tv_data_not_found;
    RecyclerView rcv_months, rcv_values;
    Context mContext;
    ListAdapter years_adapter;
    ArrayList<YearsObject> yearsList;
    ArrayList<MonthsObject> monthsList;
    static String fromDate;
    static String toDate;
    String selectedDate;
    private AnimationDrawable animationDrawable;
    public Dialog dialog;
    int userId;
    String authorityKey;
    String registrationNum;
    ArrayList<VehicleSummaryObject> summaryList;
    MonthDataRcvAdapter monthsAdapter;
    String day="";
    String month ="";
    int currentMonth;
    String year ="";
    String currentYear ="";
    String selectedYear="";
    Calendar startDate = Calendar.getInstance();
    Calendar endDate = Calendar.getInstance();
    String totalDistance;
    int monthValue;
    double totalDistanceTravelled;

    VehicleSummaryRcvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_history);
        init();

        years_adapter = new ListAdapter(mContext, R.layout.item_years, yearsList, year, this);
        showLoaderNew();
        serviceCall();
        cv_year.setOnClickListener(v -> {
            showCustomDialog(Calendar.getInstance().get(Calendar.YEAR));
        });

        rcv_months.setOnTouchListener((view, motionEvent) -> true);

        iv_back_btn.setOnClickListener(v -> {
            Intent intent = new Intent(MonthHistoryActivity.this, MainActivity.class);
            startActivity(intent);
        });

        rcv_values.addOnItemTouchListener(new RecyclerItemClickListener(mContext, rcv_values, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(mContext, History_View_Activity.class);
                intent.putExtra("date", summaryList.get(position).getCREATEDON());
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

        currentMonth = currentMonth+1;
        compareMonth();
        iv_back_month.setOnClickListener(v -> {
           // rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));summaryList.clear();
            int mo = Integer.parseInt(month);
            if(mo==0)
            {
                month = "11";
                rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));
                year = String.valueOf(Integer.parseInt(year)-1);
                selectedYear = year;
                tv_year.setText(year);
            }
            else if(mo<=11)
            {
                month = String.valueOf(Integer.parseInt(month)-1);
                rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));
            }
            summaryList.clear();
            compareMonth();
            showLoaderNew();
            serviceCall();
        });
        iv_next_month.setOnClickListener(v -> {
            int mo = Integer.parseInt(month);
            if(mo<=10)
            {
                month = String.valueOf(Integer.parseInt(month)+1);
                rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));
            }
            else if(mo==11)
            {
                month = "0";
                rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));
                if(!year.equals(currentYear))
                {
                    int selectedY = Integer.parseInt(year);

                    int y = Integer.parseInt(currentYear);
                    if(selectedY<y){
                        year = String.valueOf(selectedY+1);
                        selectedYear = year;
                        tv_year.setText(year);
                    }
                }
            }
            summaryList.clear();
            adapter.notifyDataSetChanged();
            compareMonth();
            showLoaderNew();
            serviceCall();
        });

    }

    private void compareMonth() {
        if(year.equals(currentYear))
            monthValue = Integer.parseInt(month)+1;
            if(monthValue<currentMonth) {
                iv_next_month.setEnabled(true);
                iv_next_month.setImageDrawable(mContext.getResources().getDrawable(R.drawable.forward));
            }
            else {
                iv_next_month.setEnabled(false);
                iv_next_month.setImageDrawable(mContext.getResources().getDrawable(R.drawable.forward_gray));
            }
}

    private void init() {
        mContext = MonthHistoryActivity.this;
        iv_back_btn = findViewById(R.id.iv_back_btn);
        iv_back_month = findViewById(R.id.iv_back_month);
        iv_next_month = findViewById(R.id.iv_next_month);
        toolbar = findViewById(R.id.toolbar);
        cv_year = findViewById(R.id.cv_year);
        tv_year = findViewById(R.id.tv_year);

        Typeface faceLight = Typeface.createFromAsset(mContext.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        TextView tv_title = toolbar.findViewById(R.id.tv_title);
        tv_title.setText(mContext.getResources().getString(R.string.tracking_summary));
        tv_title.setTypeface(faceLight);
        tv_total_distance = findViewById(R.id.tv_total_distance);
        tv_data_not_found = findViewById(R.id.tv_data_not_found);
        rcv_months = findViewById(R.id.rcv_months);
        rcv_values = findViewById(R.id.rcv_values);
        ll_data = findViewById(R.id.ll_data);
        startDate.add(Calendar.YEAR, -1 );
        endDate.add(Calendar.DAY_OF_MONTH, 0);
        monthsList = new ArrayList<>();
        yearsList = new ArrayList<>();
        Calendar defaultDate = Calendar.getInstance();
        summaryList = new ArrayList<>();
        userId = Integer.parseInt(MApplication.getString(mContext, Constants.UserIDSelected));
        authorityKey = "Bearer "+MApplication.getString(mContext, Constants.AccessToken);
        registrationNum = MApplication.getString(mContext, Constants.RegistrationNum);
        totalDistance = MApplication.getString(mContext, Constants.TotalDistance);
        defaultDate = Calendar.getInstance(TimeZone.getDefault());
        year = String.valueOf(defaultDate.get(Calendar.YEAR));
        currentYear = String.valueOf(defaultDate.get(Calendar.YEAR));
        month = String.valueOf(defaultDate.get(Calendar.MONTH));
        currentMonth = defaultDate.get(Calendar.MONTH);
        day = String.valueOf(defaultDate.get(Calendar.DAY_OF_MONTH));
        defaultDate.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day));
        tv_year.setText(year);
        selectedYear = year;
        addMonthsData();
        adapter = new VehicleSummaryRcvAdapter(mContext, summaryList, 0);
        LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        //manager.scrollToPosition(Integer.parseInt(month)-1);
        rcv_values.setLayoutManager(manager);
        rcv_values.setAdapter(adapter);
        if(!totalDistance.equals("null")){
            tv_total_distance.setText(mContext.getResources().getString(R.string.total_distance_txt)+" "+totalDistance+" Kms");
        }
        else {
            tv_total_distance.setText(mContext.getResources().getString(R.string.total_distance_txt)+" --");
        }
        tv_total_distance.setTypeface(faceLight);
        tv_year.setTypeface(faceLight);
        rcv_months.setNestedScrollingEnabled(false);
    }

    private void addMonthsData() {

        for (int i=1; i<13; i++)
        {
            MonthsObject object = new MonthsObject();
            if(i==1)
            {
                object.setMonthName("JANUARY");
                object.setMonthId("01");
            }
            else if(i==2)
            {
                object.setMonthName("FEBRUARY");
                object.setMonthId("02");
            }
            else if(i==3)
            {
                object.setMonthName("MARCH");
                object.setMonthId("03");
            }
            else if(i==4)
            {
                object.setMonthName("APRIL");
                object.setMonthId("04");
            }
            else if(i==5)
            {
                object.setMonthName("MAY");
                object.setMonthId("05");
            }
            else if(i==6)
            {
                object.setMonthName("JUNE");
                object.setMonthId("06");
            }
            else if(i==7)
            {
                object.setMonthName("JULY");
                object.setMonthId("07");
            }
            else if(i==8)
            {
                object.setMonthName("AUGUST");
                object.setMonthId("08");
            }
            else if(i==9)
            {
                object.setMonthName("SEPTEMBER");
                object.setMonthId("09");
            }
            else if(i==10)
            {
                object.setMonthName("OCTOBER");
                object.setMonthId("10");
            }
            else if(i==11)
            {
                object.setMonthName("NOVEMBER");
                object.setMonthId("11");
            }
            else {
                object.setMonthName("DECEMBER");
                object.setMonthId("12");
            }
            monthsList.add(object);
        }

        if(monthsList.size()>0)
        {
            monthsAdapter = new MonthDataRcvAdapter(mContext, monthsList, Integer.parseInt(month));
            LinearLayoutManager manager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
            manager.scrollToPosition(Integer.parseInt(month));
            rcv_months.setLayoutManager(manager);
            monthsAdapter.setDefaultSelection(Integer.parseInt(month));
            rcv_months.setAdapter(monthsAdapter);
        }
        rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));
        rcv_months.getLayoutManager().setItemPrefetchEnabled(true);
    }

    @Override
    public void update(String num) {

    }

    @Override
    public void updateYear(String year) {
        selectedYear = year;
    }

    private void showCustomDialog(int text) {
        //text = Integer.parseInt(year);
        yearsList = new ArrayList<>();
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(this).inflate(R.layout.year_popup, viewGroup, false);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(dialogView);
        ListView lv_years = dialogView.findViewById(R.id.lv_years);
        TextView button_ok = dialogView.findViewById(R.id.button_ok);
        YearsObject yearsObject = new YearsObject();
        //text = 2024;
        yearsObject.setYear(String.valueOf(text));
        yearsList.add(yearsObject);
        int tempYear = Integer.parseInt(year);

        for(int i=1; i<text; i++)
        {
            if(tempYear<text)
            {
                yearsObject = new YearsObject();
                yearsObject.setYear(String.valueOf(text-1));
                text = text-1;
                yearsList.add(yearsObject);
            }

        }

        //finally creating the alert dialog and displaying it
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        years_adapter = new ListAdapter(mContext, R.layout.item_years, yearsList, year, this);

        lv_years.setAdapter(years_adapter);
        lv_years.setSelector(R.color.colorPrimary);
        button_ok.setOnClickListener(v -> {
            if(!selectedYear.equals(""))
            {
                if(tv_year.getText().toString().equals(selectedYear))
                {
                    summaryList.clear();
                    //month = String.valueOf(Integer.parseInt(month)-1);
                    monthsAdapter.notifyDataSetChanged(); showLoaderNew();
                    serviceCall();
                }
                else {
                    tv_year.setText(selectedYear);
                    year = selectedYear;
                    month = String.valueOf(0);
                    rcv_months.getLayoutManager().scrollToPosition(Integer.parseInt(month));
                    selectedDate = year+"-"+month+"-"+day;
                    summaryList.clear();
                    //month = String.valueOf(Integer.parseInt(month)-1);
                    monthsAdapter.notifyDataSetChanged();
                    // Toast.makeText(context, ""+isTwoChannel, Toast.LENGTH_SHORT).show();
                    showLoaderNew();
                    serviceCall();
                }

            }
            else {
                tv_year.setText(year);
                year = year;

            }
            alertDialog.dismiss();
        });

        iv_back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

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
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing())
                {
                    dialog.dismiss();
                    dialog=null;
                }
                assert dialog != null;
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
                Log.e("", "");
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

    public void showLoaderNew() {
        runOnUiThread(new MonthHistoryActivity.Runloader(getResources().getString(R.string.loading)));
    }

    private void serviceCall(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        HistoryDataInterface service = retrofit.create(HistoryDataInterface.class);
        Call<JsonElement> call = service.getVehicleSummary(userId, authorityKey, new HistoryDataPostParamter(Integer.parseInt(month)+1, Integer.parseInt(year), registrationNum));
        call.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                hideloader();
                if (response.code()==401)
                {
                    MApplication.setBoolean(mContext, Constants.IsSessionExpired, true);
                    Intent intent = new Intent(mContext, TokenExpireActivity.class);
                    startActivity(intent);
                }

                else if(response.body()!=null){
                    JsonObject object = response.body().getAsJsonObject();
                    JsonObject info = object.get("info").getAsJsonObject();
                    if (info.get("errorCode").getAsInt()==0)
                    {
                        ll_data.setVisibility(View.VISIBLE);
                        tv_data_not_found.setVisibility(View.GONE);
                        JsonArray result = object.get("result").getAsJsonArray();
                        if(result.size()>0)
                        {
                            for (int i=0; i<result.size(); i++)
                            {
                                VehicleSummaryObject model = new VehicleSummaryObject();
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
                                if(jsonObject.has("CREATED_ON") && !jsonObject.get("CREATED_ON").isJsonNull())
                                {
                                    model.setCREATEDON(jsonObject.get("CREATED_ON").getAsString());
                                }
                                else {
                                    model.setCREATEDON("");
                                }
                                if(jsonObject.has("STATUS") && !jsonObject.get("STATUS").isJsonNull())
                                {
                                    model.setSTATUS(jsonObject.get("STATUS").getAsInt());
                                }
                                else {
                                    model.setSTATUS(0);
                                }
                                summaryList.add(model);
                            }
                        }
                        if(summaryList.size()>0)
                        {

                            for (int j = 0; j<summaryList.size(); j++)
                            {
                                if(j==0)
                                {
                                    totalDistanceTravelled = summaryList.get(j).getTOTALDISTANCE();
                                }
                                else {
                                    totalDistanceTravelled = totalDistanceTravelled + summaryList.get(j).getTOTALDISTANCE();
                                }
                            }
                            if(!String.valueOf(totalDistanceTravelled).equals("null")){
                               String distance = String.format("%.2f", totalDistanceTravelled);
                                tv_total_distance.setText(mContext.getResources().getString(R.string.total_distance_txt)+" "+distance+" Kms");
                            }
                            else {
                                tv_total_distance.setText(mContext.getResources().getString(R.string.total_distance_txt)+" --");
                            }
                         adapter.notifyDataSetChanged();

                        }
                    }
                    else {
                        ll_data.setVisibility(View.GONE);
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


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(MonthHistoryActivity.this, MainActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }
}
