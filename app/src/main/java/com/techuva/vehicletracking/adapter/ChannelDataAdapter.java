package com.techuva.vehicletracking.adapter;

import android.content.Context;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.listeners.GenerateMap;
import com.techuva.vehicletracking.model.HistoryDataResultObject;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ChannelDataAdapter extends BaseAdapter {
    private final int resource;
    private Context mContext;
    List<HistoryDataResultObject> list;
    GenerateMap map;

    public ChannelDataAdapter(int resource, Context mContext, List<HistoryDataResultObject> list, GenerateMap map) {
        this.resource = resource;
        this.mContext = mContext;
        this.list = list;
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
            view = inflater.inflate(R.layout.item_hr_channel_values, parent, false);

            holder = new ViewHolder();

            holder.tv_time_value = (TextView) view.findViewById(R.id.tv_time_value);
            holder.tv_history_channel_values = (ImageView) view.findViewById(R.id.tv_history_channel_values);
            holder.tv_date_value = (TextView) view.findViewById(R.id.tv_date_value);
            holder.tv_meter_reading = (TextView) view.findViewById(R.id.tv_meter_reading);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        final HistoryDataResultObject model = list.get(position);


        // bitmap

        // setting data over views
       // holder.tv_time_value.setText(model.getTime());

        String dateToParse = model.getRECEIVEDTIME();
        //"Feb 25, 2020 12:36:10 PM"
        //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy hh:mm:ss a");

        Date date = null;
        try {
            date = sdf.parse(dateToParse);
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
        SimpleDateFormat timeFormatter = new SimpleDateFormat("dd-MMM, hh:mm a");
        String sa = timeFormatter.format(date);

       //  holder.tv_time_value.setText(model.getTime());
        double value = Double.parseDouble(model.get3());
        double rounded2 = Math.round(value * 10) / 10.0;
        holder.tv_date_value.setText(sa);
        holder.tv_time_value.setText(model.getUNITSCONSUMED());
        holder.tv_meter_reading.setText(String.valueOf(rounded2));
       // holder.tv_history_channel_values.setText(model.get1()+", "+model.get2());

        holder.tv_history_channel_values.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MApplication.setString(mContext, Constants.ReceivedTime, model.getRECEIVEDTIME());
                map.generateMap("map", String.valueOf(model.get1()), String.valueOf(model.get2()));
            }
        });

        return view;
    }

    // View Holder
    private class ViewHolder {
        public TextView tv_time_value, tv_date_value, tv_meter_reading;
        ImageView tv_history_channel_values;
    }
   /* @Override
    public int getViewTypeCount() {
        if(list.size()==0){
            return 1;
        }
        return list.size();
    }*/




}
