package com.techuva.vehicletracking.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.net.ParseException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.listeners.GenerateMap;
import com.techuva.vehicletracking.model.DeviceAllocationHistoryModel;
import com.techuva.vehicletracking.model.DriverAllocationHistoryModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class DriverAllocationHistoryAdapter extends BaseAdapter {
    private final int resource;
    private Context mContext;
    List<DriverAllocationHistoryModel> list;
    GenerateMap map;

    public DriverAllocationHistoryAdapter(int resource, Context mContext, List<DriverAllocationHistoryModel> list) {
        this.resource = resource;
        this.mContext = mContext;
        this.list = list;
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
            view = inflater.inflate(R.layout.item_allocation_history, parent, false);

            holder = new ViewHolder();

            holder.tv_device_serial = (TextView) view.findViewById(R.id.tv_device_serial);
            holder.tv_allocated_on = (TextView) view.findViewById(R.id.tv_allocated_on);
            holder.tv_allocated_by = (TextView) view.findViewById(R.id.tv_allocated_by);

            view.setTag(holder);

        } else {
            holder = (ViewHolder) view.getTag();
        }

        Typeface faceLight = Typeface.createFromAsset(mContext.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        holder.tv_device_serial.setTypeface(faceLight);
        holder.tv_allocated_on.setTypeface(faceLight);
        holder.tv_allocated_by.setTypeface(faceLight);

        final DriverAllocationHistoryModel model = list.get(position);

        String dateToParse = model.getASSIGNEDON();

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
        SimpleDateFormat timeFormatter = new SimpleDateFormat("dd-MMM-yyyy HH:mm");
        String sa = timeFormatter.format(date);
        holder.tv_allocated_on.setText(sa);
        holder.tv_device_serial.setText(model.getDRIVERNAME());
        holder.tv_allocated_by.setText(model.getASSIGNEDBYNAME());

        return view;
    }

    private class ViewHolder {
        public TextView tv_device_serial, tv_allocated_on, tv_allocated_by;
    }

}
