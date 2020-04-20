package com.techuva.vehicletracking.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.vehicletracking.R;
import com.techuva.vehicletracking.holders.AvailableDeviceRCVHolder;
import com.techuva.vehicletracking.model.AvailableDevicesModel;
import com.techuva.vehicletracking.model.AvailableDriversListModel;
import com.techuva.vehicletracking.utils.Constants;
import com.techuva.vehicletracking.utils.MApplication;

import java.util.ArrayList;


public class DriverListRcvAdapter extends RecyclerView.Adapter<AvailableDeviceRCVHolder>
{
    private ArrayList<AvailableDriversListModel> arrayList;
    private Context context;
    private AvailableDeviceRCVHolder listHolder;
    private CompoundButton lastCheckedRB = null;
    private String UserName="";
    private int selectedPosition = -1;
    private OnItemClicked listener;

    public DriverListRcvAdapter(Context context, ArrayList<AvailableDriversListModel> arrayList, OnItemClicked listener) {
        this.context = context;
        this.arrayList = arrayList;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
       // return (null != arrayList ? arrayList.size() : 0);
        return arrayList.size();

    }

    @Override
    public void onBindViewHolder(AvailableDeviceRCVHolder holder, int position) {
        AvailableDriversListModel model = arrayList.get(position);

        holder.tv_tag_name.setText(model.getNAME());
        holder.tv_serial_num.setText(String.valueOf(model.getMOBILE_NO()));
        holder.iv_device.setImageDrawable(context.getResources().getDrawable(R.drawable.person));



        holder.ll_tag.setOnClickListener(v -> {
            MApplication.setString(context, Constants.VEHICLE_DRIVER_ID, String.valueOf(model.getVEHICLE_DRIVER_ID()));
            holder.rb_checked.setChecked(true);
            holder.rb_checked.setTag(position);
            holder.rb_checked.setChecked(position == selectedPosition);
        });
         holder.rb_checked.setOnCheckedChangeListener(ls);
         holder.rb_checked.setTag(position);
         holder.rb_checked.setChecked(position == selectedPosition);

    }

    private CompoundButton.OnCheckedChangeListener ls = ((buttonView, isChecked) -> {
        int tag = (int) buttonView.getTag();
        if (lastCheckedRB == null) {
            lastCheckedRB = buttonView;
            itemCheckChanged(buttonView);

        } else if (tag != (int) lastCheckedRB.getTag()) {
            lastCheckedRB.setChecked(false);
            lastCheckedRB = buttonView;
            itemCheckChanged(buttonView);
        }

    });

    @Override
    public AvailableDeviceRCVHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater mInflater = LayoutInflater.from(viewGroup.getContext());

        ViewGroup mainGroup = (ViewGroup) mInflater.inflate(R.layout.item_device_list, viewGroup, false);
        listHolder = new AvailableDeviceRCVHolder(mainGroup);
        return listHolder;

    }


    private void itemCheckChanged(View v) {
        selectedPosition = (Integer) v.getTag();
        //Toast.makeText(context, "Changed" +selectedPosition, Toast.LENGTH_SHORT).show();

        if(!getSelectedItem().equals(""))
        {
            /*int i =  arrayList.get(Integer.parseInt(getSelectedItem())).getCompanyId();
            String userId =  arrayList.get(Integer.parseInt(getSelectedItem())).getUserId();
            MApplication.setString(context, Constants.CompanyID, String.valueOf(i));
            MApplication.setBoolean(context, Constants.IsDefaultDeviceSaved, false);
           // Toast.makeText(context, "Changed" + userId, Toast.LENGTH_SHORT).show();
            if(arrayList.size()==1)
            {
                MApplication.setString(context, Constants.UserID, String.valueOf(userId));
            }
            else {
                MApplication.setString(context, Constants.UserIDSelected, String.valueOf(userId));
            }
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);*/
        }
        else
            Toast.makeText(context, "Please select account to proceed", Toast.LENGTH_SHORT).show();
        /*
        Intent intent = new Intent(context, Dashboard.class);
        context.startActivity(intent);*/
        notifyDataSetChanged();
    }

    public interface OnItemClicked {
        void onItemClick(View view, int position);
    }

    public String getSelectedItem() {
        if ( selectedPosition!= -1) {
           // Toast.makeText(context, "Selected Item : " + arrayList.get(selectedPosition).getCompanyName(), Toast.LENGTH_SHORT).show();
            return String.valueOf(selectedPosition);
        }
        return "";
    }

}