package com.techuva.vehicletracking.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.ParseException;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.adapter.ChannelDataAdapter;
import com.techuva.vehicletracking.api_interface.HistoryDataInterface;
import com.techuva.vehicletracking.listeners.EndlessScrollListener;
import com.techuva.vehicletracking.listeners.GenerateMap;
import com.techuva.vehicletracking.model.HistoryDataInfoObject;
import com.techuva.vehicletracking.model.HistoryDataMainObject;
import com.techuva.vehicletracking.model.HistoryDataPostParamter;
import com.techuva.vehicletracking.model.HistoryDataResultObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class History_View_Activity extends AppCompatActivity implements GenerateMap {

    TextView tv_to_date;
    static String fromDate;
    static String fromDateV;
    static String toDate;
    public Dialog dialog;
    LinearLayout ll_day_view, ll_from__date, ll_tab_data_not_found, ll_headings, ll_back_btn, ll_to_date;
    LinearLayout ll_root_day_view;
    TextView tv_from_date;
    TextView tv_date_heading;
    TextView tv_time_heading;
    TextView tv_value_heading;
    TextView tv_meter_reading;
    TextView tv_data_not_found;
    ImageView iv_to_date, iv_from_date;
    Button btn_retry;
    RecyclerView rcv_day_data;
    HorizontalScrollView hv_list_channels;
    ListView channel_list;
    ListView lv_data;
    String deviceId, pagePerCount;
    int pageNumber = 1;
    Context context;
    int channelPosition = 0;
    int selectedTabValue = 1;
    Boolean channelNames = false;
    Boolean dateChanged = false;
    RecyclerView rcv_channel_names;
    ArrayList<HistoryDataResultObject> listforValues;
    ArrayList<HistoryDataResultObject> valuelist = new ArrayList<>();
    ArrayList<HistoryDataResultObject> valueObjectArrayList = new ArrayList<>();
    ChannelDataAdapter adapter;
    Toolbar toolbar;
    ImageView iv_back_btn;
    int listCount = 0;
    int day, year, month;
    Calendar calendar;
    List<HistoryDataMainObject> arrayInfo = new ArrayList<>();
    HistoryDataInfoObject info;
    Boolean isSecond = false;
    HistoryDataInterface service;
    Call<JsonElement> call1;
    int UserId;
    String authorityKey = "";
    String grantType = "";
    private boolean loading = true;
    private boolean tabChanged = false;
    private AnimationDrawable animationDrawable;
    private EndlessScrollListener scrollListener;
    String selectedDate = "";
    String registrationNum = "";
    LinearLayout ll_main, ll_no_internet;
    TextView tv_no_internet, tv_retry;
    int yy, mm, dd;
    private DatePickerDialog.OnDateSetListener myDateListener = (arg0, arg1, arg2, arg3) -> {
        // TODO Auto-generated method stub
        // arg1 = year
        // arg2 = month
        // arg3 = day
        showDate(arg1, arg2 + 1, arg3);
    };

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_view);
        Bundle extras = getIntent().getExtras();
        if (extras == null) {

            //displayValue = 1;
        } else {
            selectedDate = extras.getString("date");
            //Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
            selectedTabValue = extras.getInt("TabId");
        }
        Init_Views();

        setTypeface();
       // showLoaderNew();
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


        tv_retry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callService();
            }
        });

        adapter = new ChannelDataAdapter(R.layout.item_hr_channel_data, this, valuelist, History_View_Activity.this);
        lv_data.setAdapter(adapter);
        lv_data.setOnScrollListener(scrollListener);
        ll_from__date.setOnClickListener(v -> {
            if (month < 10) {
                showDate(year, month + 1, day);
            } else
                showDate(year, month, day);
        });
        ll_to_date.setOnClickListener(v -> {
            SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
            Calendar calendar = new GregorianCalendar();
            String date = sdf3.format(calendar.getTime());
            if(!date.equals(fromDate))
            {
                if (month < 10) {
                    showToDate(year, month + 1, day);
                } else
                    showToDate(year, month, day);
            }/*
            else {
                Toast.makeText(context, "Please select from date first", Toast.LENGTH_SHORT).show();
            }*/
        });


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        ll_back_btn.setOnClickListener(v -> {
            finish();
        });
    }

    private void loadNextDataFromApi(int page, int delay) {
        pageNumber = page;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                if(pageNumber==1)
                {
                    valuelist.clear();
                    adapter.notifyDataSetChanged();
                }
                serviceCall();
            }
        }, delay);
    }

    private void showToDate(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {

            monthOfYear = monthOfYear+1;
            String date2;
            String date = (year1 + "-" + (monthOfYear) + "-" + dayOfMonth);

            if(monthOfYear<10 && dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(monthOfYear<10)
            {
                date2  = (dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            else {
                date2 =   (dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            tv_to_date.setText(date2);
            toDate = date;
            fromDate = tv_from_date.getText().toString();
            DateFormat  outputFormat= new SimpleDateFormat("yyyy-MM-dd");
            DateFormat inputFormat  = new SimpleDateFormat("dd-MM-yyyy");
            DateFormat yearOnly  = new SimpleDateFormat("yyyy");
            DateFormat monthOnly  = new SimpleDateFormat("MM");
            DateFormat dayOnly  = new SimpleDateFormat("dd");
            String inputDateStr= fromDate;
            Date date1 = null;
            Date yearMax = null;
            Date monthMax = null;
            Date dayMax = null;
            try {
                date1 = inputFormat.parse(inputDateStr);
                yearMax = inputFormat.parse(inputDateStr);
                monthMax = inputFormat.parse(inputDateStr);
                dayMax = inputFormat.parse(inputDateStr);

            } catch (java.text.ParseException e) {
                e.printStackTrace();
            }
            String outputDateStr = outputFormat.format(date1);
             yy = Integer.parseInt(yearOnly.format(yearMax));
             mm = Integer.parseInt(monthOnly.format(monthMax));
             dd = Integer.parseInt(dayOnly.format(dayMax));

            /*try {
                date1 = sdf1.parse(fromDate);
                //date3 = sdf1.parse(toDate);
            } catch (ParseException | java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/
            fromDate = outputDateStr;
            dateChanged = true;
            lv_data.setSelectionAfterHeaderView();
            if (dateChanged) {
                listforValues.clear();
                valuelist.clear();
                adapter.notifyDataSetChanged();
                listforValues = new ArrayList<>();
                showLoaderNew();
                //serviceCall();
                pageNumber =1;
                loadNextDataFromApi(pageNumber, 0);
            }

        }, year, month, day);
        Calendar c = Calendar.getInstance();
        c.set(yy, mm-1, dd);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

        datePickerDialog.show();
    }

    private void showDate(int year, final int month, int day) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, (view, year1, monthOfYear, dayOfMonth) -> {
            monthOfYear = monthOfYear+1;
            String date2;
            String date = (year1 + "-" + (monthOfYear) + "-" + dayOfMonth);
            yy = year1;
            mm = monthOfYear;
            dd = dayOfMonth;
            if(monthOfYear<10 && dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(monthOfYear<10)
            {
                date2  = (dayOfMonth + "-" + "0"+monthOfYear + "-" + year1);
            }
            else if(dayOfMonth<10)
            {
                date2  = ("0"+dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            else {
                date2 =   (dayOfMonth + "-" +monthOfYear + "-" + year1);
            }
            tv_from_date.setText(date2);
            fromDate = date;
            fromDateV = date2;
            toDate = tv_to_date.getText().toString();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy");
                Date date1;
                Date date3;
                try {
                    date1 = sdf1.parse(toDate);
                    date3 = sdf1.parse(date);
                    toDate = sdf.format(date1);
                    //fromDate = sdf.format(date3);
                } catch (ParseException | java.text.ParseException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            dateChanged = true;
            lv_data.setSelectionAfterHeaderView();
            if (dateChanged) {
                SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
                Calendar calendar = new GregorianCalendar();
                String dateV = sdf3.format(calendar.getTime());
                //String  = sdf3.format(fromDate);
                if(fromDateV.equals(dateV))
                {
                    tv_to_date.setText(fromDateV);
                    listforValues.clear();
                    valuelist.clear();
                    adapter.notifyDataSetChanged();
                    listforValues = new ArrayList<>();
                    showLoaderNew();
                    //serviceCall();
                    pageNumber =1;
                    loadNextDataFromApi(pageNumber, 0);
                }
                else {
                    listforValues.clear();
                    valuelist.clear();
                    adapter.notifyDataSetChanged();
                    listforValues = new ArrayList<>();
                    showLoaderNew();
                    //serviceCall();
                    pageNumber =1;
                    loadNextDataFromApi(pageNumber, 0);
                }
            }
        }, year, month, day);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    private void Init_Views() {

        context = History_View_Activity.this;
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        registrationNum = MApplication.getString(context, Constants.RegistrationNum);

        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        TextView tv_title = toolbar.findViewById(R.id.tv_title);
        tv_title.setText(context.getResources().getString(R.string.history)+" - "+registrationNum);
        tv_title.setTypeface(faceLight);
        iv_back_btn = findViewById(R.id.iv_back_btn);
        info = new HistoryDataInfoObject();
        listforValues = new ArrayList<>();
        SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy");
        Calendar calendar = new GregorianCalendar();
        fromDate = sdf4.format(calendar.getTime());
        toDate = fromDate;
        ll_day_view = findViewById(R.id.ll_day_view);
        ll_to_date = findViewById(R.id.ll_to_date);
        ll_from__date = findViewById(R.id.ll_select_date);
        ll_tab_data_not_found = findViewById(R.id.ll_tab_data_not_found);
        ll_headings = findViewById(R.id.ll_headings);
        ll_root_day_view = findViewById(R.id.ll_root_day_view);
        ll_main = findViewById(R.id.ll_main);
        ll_no_internet = findViewById(R.id.ll_no_internet);
        tv_no_internet = findViewById(R.id.tv_no_internet);
        tv_retry = findViewById(R.id.tv_retry);
        tv_retry.setTypeface(faceLight);
        tv_no_internet.setTypeface(faceLight);
        tv_to_date = findViewById(R.id.tv_to_date);
        tv_from_date = findViewById(R.id.tv_from_date);
        iv_to_date = findViewById(R.id.iv_to_date);
        iv_from_date = findViewById(R.id.iv_from_date);
        rcv_day_data = findViewById(R.id.rcv_day_data);
        lv_data = findViewById(R.id.lv_data);
        ll_back_btn = findViewById(R.id.ll_back_btn);
        rcv_channel_names = findViewById(R.id.rcv_channel_names);
        tv_date_heading = findViewById(R.id.tv_date_heading);
        tv_time_heading = findViewById(R.id.tv_time_heading);
        tv_value_heading = findViewById(R.id.tv_value_heading);
        tv_meter_reading = findViewById(R.id.tv_meter_reading);
        tv_data_not_found = findViewById(R.id.tv_data_not_found);
        btn_retry = findViewById(R.id.btn_retry);
       // deviceId = MApplication.getString(context, Constants.DeviceID);
        deviceId = MApplication.getString(context, Constants.RegistrationNum);
        fromDate = tv_from_date.getText().toString();
        pagePerCount = "20";
        String date = sdf3.format(calendar.getTime());
        if(selectedDate.equals("")){
            tv_to_date.setText(date);
            tv_from_date.setText(date);
            fromDate = sdf4.format(calendar.getTime());
            toDate = sdf4.format(calendar.getTime());
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");
            Date date1 = null;
            try {
                date1 = sdf.parse(selectedDate);
            } catch (ParseException | java.text.ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            tv_to_date.setText(sdf3.format(date1));
            tv_from_date.setText(sdf3.format(date1));
            fromDate = sdf4.format(date1);
            toDate = sdf4.format(date1);
        }
        authorityKey = "Bearer " + MApplication.getString(context, Constants.AccessToken);
        UserId = Integer.parseInt(MApplication.getString(context, Constants.UserID));
        grantType = Constants.GrantType;

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private void serviceCall() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL_TOKEN)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(HistoryDataInterface.class);

        call1 = service.getStringScalarWithSession(UserId, authorityKey, new HistoryDataPostParamter(deviceId, fromDate, toDate, pagePerCount, String.valueOf(pageNumber)));
        call1.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                //response.body() have your LoginResult fields and methods  (example you have to access error then try like this response.body().getError() )
                if (response.body() != null) {
                    hideloader();
                    JsonObject jsonObject = response.body().getAsJsonObject();
                    JsonObject infoObject = jsonObject.get("info").getAsJsonObject();
                    if (infoObject.get("errorCode").getAsInt()==0) {
                        listCount = infoObject.get("listCount").getAsInt();
                        if (!jsonObject.get("result").isJsonNull()) {
                            ll_tab_data_not_found.setVisibility(View.GONE);
                            lv_data.setVisibility(View.VISIBLE);
                            JsonArray jsonArray = jsonObject.get("result").getAsJsonArray();
                            if(jsonArray.size()>0)
                            {
                                for (int i=0; i<jsonArray.size(); i++)
                                {
                                    HistoryDataResultObject object = new HistoryDataResultObject();
                                    JsonObject model = jsonArray.get(i).getAsJsonObject();
                                    object.setBATCHID(model.get("BATCH_ID").getAsInt());
                                    //object.setINVENTORYNAME(model.get("INVENTORY_NAME").getAsString());
                                    object.set1(model.get("1").getAsString());
                                    object.set2(model.get("2").getAsString());
                                    object.set3(model.get("3").getAsString());
                                    object.setUNITSCONSUMED(model.get("UNITS_CONSUMED").getAsString());
                                    object.setRECEIVEDTIME(model.get("RECEIVED_TIME").getAsString());
                                    valuelist.add(object);

                                }
                            }
                            dataResponseforValues(valuelist);

                        }
                    } else if (infoObject.get("errorCode").getAsInt()==1) {
                        hideloader();
                        lv_data.setVisibility(View.GONE);
                        ll_tab_data_not_found.setVisibility(View.VISIBLE);
                        tv_data_not_found.setText(infoObject.get("errorMessage").getAsString());
                    }

                } else {
                    hideloader();
                    lv_data.setVisibility(View.GONE);
                    ll_tab_data_not_found.setVisibility(View.VISIBLE);

                }

            }

            @Override
            public void onFailure(Call<JsonElement> call1, Throwable t) {
                hideloader();
                /*lv_data.setVisibility(View.GONE);
                ll_tab_data_not_found.setVisibility(View.VISIBLE);*/
            }

        });

    }

    private void dataResponseforValues(ArrayList<HistoryDataResultObject> result) {
        if (!((Activity) context).isFinishing()) {

            adapter.notifyDataSetChanged();
        }
    }

    private void setTypeface() {
        Typeface faceMedium = Typeface.createFromAsset(getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        tv_date_heading.setTypeface(faceMedium);
        tv_time_heading.setTypeface(faceMedium);
        tv_value_heading.setTypeface(faceMedium);
        tv_meter_reading.setTypeface(faceMedium);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showLoaderNew() {
        runOnUiThread(new History_View_Activity.Runloader(getResources().getString(R.string.loading)));
    }

    public void hideloader() {
        runOnUiThread(() -> {
            try {
                if (dialog != null && dialog.isShowing())
                    dialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    protected void onResume() {
        valuelist.clear();
        pageNumber = 1;
        adapter.notifyDataSetChanged();
        if(MApplication.isWorkingInternetPersent(context))
        {
            callService();
        }
        else {
            ll_main.setVisibility(View.GONE);
            ll_no_internet.setVisibility(View.VISIBLE);
        }
        super.onResume();

    }

    private void callService() {
        if( MApplication.isWorkingInternetPersent(context))
        {
            ll_main.setVisibility(View.VISIBLE);
            ll_no_internet.setVisibility(View.GONE);
            showLoaderNew();
            serviceCall();
            lv_data.setOnScrollListener(scrollListener);
        }
        else {
            ll_main.setVisibility(View.GONE);
            ll_no_internet.setVisibility(View.VISIBLE);
        }
    }


    private void goto_login_activity() {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void generateMap(String num, String latitude, String longitude) {
        if(num.equals("map"))
        {
            MApplication.setString(context, Constants.Latitude, latitude);
            MApplication.setString(context, Constants.Longitude, longitude);
            Intent intent = new Intent(History_View_Activity.this, MapActivity.class);
            intent.putExtra("From", Constants.HistoryData);
            startActivity(intent);
        }
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
                if (dialog == null) {
                    dialog = new Dialog(context, R.style.Theme_AppCompat_Light_DarkActionBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.getWindow().setBackgroundDrawable(
                            new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }
                dialog.setContentView(R.layout.loading);
                dialog.setCancelable(false);

                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog = null;
                }
                dialog.show();

                ImageView imgeView = (ImageView) dialog
                        .findViewById(R.id.imgeView);
                TextView tvLoading = (TextView) dialog
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
            } catch (Exception e) {

            }
        }
    }
}

