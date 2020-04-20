package com.techuva.vehicletracking.adapter;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.holders.ChannelDataRCVHolder;
import com.techuva.vehicletracking.holders.VehicleSummaryRCVHolder;
import com.techuva.vehicletracking.model.MonthsObject;
import com.techuva.vehicletracking.model.VehicleSummaryObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class VehicleSummaryRcvAdapter extends RecyclerView.Adapter<VehicleSummaryRCVHolder>
{// Recyclerview will extend to
    // recyclerview adapter
    private ArrayList<VehicleSummaryObject> list;
    private Context context;
    private VehicleSummaryRCVHolder listHolder;
    int selectedPosition=0;
    int defaultMonth=0;

    public VehicleSummaryRcvAdapter(Context context, ArrayList<VehicleSummaryObject> list, int position) {
        this.context = context;
        this.list = list;
        this.defaultMonth = position;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return list.size();

    }

    @Override
    public void onBindViewHolder(VehicleSummaryRCVHolder holder, int position) {
        VehicleSummaryObject model = list.get(position);
        String dateToParse = model.getCREATEDON();
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        Date date = null;
        try {
            date = sdf.parse(dateToParse);
        } catch (ParseException | java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        SimpleDateFormat format = new SimpleDateFormat("MMM");
        String month = format.format(date);
        holder.tv_month.setText(month);
        SimpleDateFormat timeFormatter = new SimpleDateFormat("dd");
        String day = timeFormatter.format(date);
        holder.tv_date.setText(day);
        if(model.getTOTALDISTANCE()!=null){
            holder.tv_total_distance_value.setText(model.getTOTALDISTANCE()+" Kms");
        }
        else {
            holder.tv_total_distance_value.setText("--");
        }


       /* if(selectedPosition==position){
            holder.ll_main_months.setBackgroundColor(context.getResources().getColor(R.color.text_color_dark));
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        }
        else {
            holder.ll_main_months.setBackgroundColor(context.getResources().getColor(R.color.white));
            holder.tv_month.setTextColor(context.getResources().getColor(R.color.text_color_dark));
        }*/

    }

    @Override
    public VehicleSummaryRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_vehicle_summary, viewGroup, false);

        listHolder = new VehicleSummaryRCVHolder(mainGroup, context);
        return listHolder;

    }

   public void setDefaultSelection(int position)
    {
        selectedPosition = position;
    }




}