package com.techuva.vehicletracking.holders;

import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.techuva.vehicletracking.R;


public class AvailableDeviceRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

public TextView tv_serial_num;
public TextView tv_tag_name;
public RadioButton rb_checked;
public ImageView iv_device;
public LinearLayout ll_tag;
public int mSelectedItem;
public AvailableDeviceRCVHolder(View view) {
        super(view);
        // Find all views ids

        this.iv_device =  view.findViewById(R.id.iv_device);
        this.tv_tag_name = view.findViewById(R.id.tv_tag_name);
        this.tv_serial_num = view.findViewById(R.id.tv_serial_num);
        this.rb_checked = view.findViewById(R.id.rb_accountUsed);
        this.ll_tag = view.findViewById(R.id.ll_tag);
        Typeface faceLight = Typeface.createFromAsset(view.getResources().getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        this.tv_serial_num.setTypeface(faceLight);
        this.tv_tag_name.setTypeface(faceLight);
        }

        @Override
        public void onClick(View v) {

        mSelectedItem = getAdapterPosition();
        }

        private void setTypeface() {

        }
}