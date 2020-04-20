package com.techuva.vehicletracking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;
import com.techuva.vehicletracking.listeners.GenerateMap;
import com.techuva.vehicletracking.listeners.GoToHistory;
import com.techuva.vehicletracking.model.AllVehiclesDataModel;
import java.util.ArrayList;

public class AllVehicleDataAdapter extends BaseAdapter{
    private final int resource;
    private Context mContext;
    ArrayList<AllVehiclesDataModel> list;
    String vehicle_type="";
    String vehicle_status;
    String vehicle_condition;
    GoToHistory goToHistory;
    GenerateMap map;

    public AllVehicleDataAdapter(int resource, Context mContext, ArrayList<AllVehiclesDataModel> list, GoToHistory go, GenerateMap map) {
        this.resource = resource;
        this.mContext = mContext;
        this.list = list;
        this.goToHistory = go;
        this.map = map;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent){
        ViewHolder holder = null;

        // Inflater for custom layout
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.item_all_vehicle_data, parent, false);

            holder = new ViewHolder();
            holder.ll_root =  view.findViewById(R.id.ll_root);
            holder.ll_top_view =  view.findViewById(R.id.ll_top_view);
            holder.ll_bottom =  view.findViewById(R.id.ll_bottom);
            holder.iv_vehicle_type =  view.findViewById(R.id.iv_vehicle_type);
            holder.tv_vehicle_reg_num =  view.findViewById(R.id.tv_vehicle_reg_num);
            holder.tv_driver_name =  view.findViewById(R.id.tv_driver_name);
            holder.tv_total_distance =  view.findViewById(R.id.tv_total_distance);
            holder.tv_status_symbol =  view.findViewById(R.id.tv_status_symbol);
            holder.tv_status =  view.findViewById(R.id.tv_status);
            holder.tv_tag_text =  view.findViewById(R.id.tv_tag_text);
            holder.tv_tag =  view.findViewById(R.id.tv_tag);
            holder.tv_current_location =  view.findViewById(R.id.tv_current_location);
            holder.tv_travel_summary =  view.findViewById(R.id.tv_travel_summary);
            holder.tv_vehicle_details =  view.findViewById(R.id.tv_vehicle_details);
            holder.ll_current_location= view.findViewById(R.id.ll_current_location);
            holder.ll_travel_summary = view.findViewById(R.id.ll_travel_summary);
            holder.ll_vehicle_details = view.findViewById(R.id.ll_vehicle_details);
            holder.ll_vehicle_history = view.findViewById(R.id.ll_vehicle_history);

            Typeface faceLight = Typeface.createFromAsset(mContext.getAssets(), "fonts/AvenirLTStd-Light.otf");
            holder.tv_vehicle_reg_num.setTypeface(faceLight);
            holder.tv_driver_name.setTypeface(faceLight);
            holder.tv_total_distance.setTypeface(faceLight);
            holder.tv_status_symbol.setTypeface(faceLight);
            holder.tv_status.setTypeface(faceLight);
            holder.tv_tag_text.setTypeface(faceLight);
            holder.tv_tag.setTypeface(faceLight);
            holder.tv_current_location.setTypeface(faceLight);
            holder.tv_travel_summary.setTypeface(faceLight);
            holder.tv_vehicle_details.setTypeface(faceLight);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AllVehiclesDataModel model = list.get(position);

/*
        holder.ll_vehicle_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHistory.update(String.valueOf(model.getINVENTORYID()));
            }
        });
*/

        holder.ll_travel_summary.setOnClickListener(v -> {
            MApplication.setString(mContext, Constants.RegistrationNum, model.getRegistrationNumber());
            MApplication.setString(mContext, Constants.ReceivedTime, model.getLastSyncOn());
            MApplication.setString(mContext, Constants.TotalDistance, String.valueOf(model.geteNTIRE_DISTANCE()));
            MApplication.setString(mContext, Constants.DeviceID, String.valueOf(model.getINVENTORYID()));
            goToHistory.update("sum");
        });

        holder.ll_current_location.setOnClickListener(v -> {

            if(!(model.getLANG() ==null) && !(model.getLAT() ==null))
            {
                MApplication.setString(mContext, Constants.RegistrationNum, model.getRegistrationNumber());
                MApplication.setString(mContext, Constants.ReceivedTime, model.getLastSyncOn());
                map.generateMap("map", String.valueOf(model.getLAT()), String.valueOf(model.getLANG()));
            }
            else {
                Toast.makeText(mContext, "Location not found", Toast.LENGTH_SHORT).show();
            }

       });

        holder.ll_vehicle_details.setOnClickListener(v -> {
            MApplication.setString(mContext, Constants.RegistrationNum, model.getRegistrationNumber());
            goToHistory.update("details");
        });

       vehicle_type = model.getVehicleType();
       if(!vehicle_type.equals(""))
       {
           switch (vehicle_type)
           {
               case "CAR":
                   holder.iv_vehicle_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.car));
                   break;
               case "VAN":
                   holder.iv_vehicle_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.van));
                   break;
               case "TRUCK":
                   holder.iv_vehicle_type.setImageDrawable(mContext.getResources().getDrawable(R.drawable.truck));
                   break;
           }
       }
       vehicle_status = model.getStatus();
        vehicle_condition = model.getCondition();
       if(!vehicle_condition.equals("") && vehicle_status.equals("Allocated"))
       {
           holder.tv_status.setText(vehicle_condition);
           switch (vehicle_condition)
           {
               case "Running":
                   holder.tv_status_symbol.setBackground(mContext.getResources().getDrawable(R.drawable.circle_background_running));
                   break;
               case "Idle":
                   holder.tv_status_symbol.setBackground(mContext.getResources().getDrawable(R.drawable.circle_background_idle));
                   break;
               case "Offline":
                   holder.tv_status_symbol.setBackground(mContext.getResources().getDrawable(R.drawable.circle_background_offline));
                   break;
               case "Unallocated":
                   holder.tv_status_symbol.setBackground(mContext.getResources().getDrawable(R.drawable.circle_background));
                   break;
           }
       }
       else{
           holder.tv_status.setText("--");
           holder.tv_status_symbol.setBackground(mContext.getResources().getDrawable(R.drawable.circle_background));
       }

        holder.tv_vehicle_reg_num.setText(model.getRegistrationNumber());
        holder.tv_driver_name.setText(mContext.getResources().getString(R.string.driver_name)+" "+model.getDRIVERNAME());
        if(!String.valueOf(model.gettOTALDISTANCE()).equals("null"))
        {
            holder.tv_total_distance.setText(mContext.getResources().getString(R.string.today_travelled)+" "+model.gettOTALDISTANCE()+" Kms");
         }
        else
            holder.tv_total_distance.setText(mContext.getResources().getString(R.string.today_travelled)+" --");
        holder.tv_tag.setText(vehicle_status);

        return view;
    }

    private class ViewHolder {
        LinearLayout ll_root, ll_top_view, ll_bottom, ll_current_location, ll_travel_summary, ll_vehicle_details, ll_vehicle_history;
        ImageView iv_vehicle_type;
        TextView tv_vehicle_reg_num, tv_driver_name, tv_total_distance, tv_status_symbol, tv_status;
        TextView tv_tag_text, tv_tag, tv_current_location, tv_travel_summary, tv_vehicle_details;
    }
}
