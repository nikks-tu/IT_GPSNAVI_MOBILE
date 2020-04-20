package com.techuva.vehicletracking.holders;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.techuva.vehicletracking.R;


public class VehicleSummaryRCVHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public LinearLayout ll_date, ll_main_bubble,ll_data ;
        public TextView tv_month, tv_date, tv_total_distance_value, tv_total_distance_txt;

public VehicleSummaryRCVHolder(View view, Context context) {
        super(view);
        // Find all views ids

        this.tv_month =  view.findViewById(R.id.tv_month);
        this.tv_date =  view.findViewById(R.id.tv_date);
        this.tv_total_distance_value =  view.findViewById(R.id.tv_total_distance_value);
        this.tv_total_distance_txt =  view.findViewById(R.id.tv_total_distance_txt);
        this.ll_date =  view.findViewById(R.id.ll_date);
        this.ll_main_bubble =  view.findViewById(R.id.ll_main_bubble);
        this.ll_data =  view.findViewById(R.id.ll_data);

        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        tv_month.setTypeface(faceLight);
        tv_date.setTypeface(faceLight);
        tv_total_distance_value.setTypeface(faceLight);
        tv_total_distance_txt.setTypeface(faceLight);
        }

        @Override
        public void onClick(View v) {
                int position = getAdapterPosition();
        }
}