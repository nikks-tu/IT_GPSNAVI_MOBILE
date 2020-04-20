package com.techuva.vehicletracking.fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.techuva.vehicletracking.MainActivity;
import com.techuva.vehicletracking.R;

public class TermsAndConditions extends Fragment {

    Context context;
    TextView tv_terms, tv_from, tv_tech, tv_uva;
    ScrollView sv_main;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_terms_and_conditions, container, false);
        init(contentView);

        return contentView;
    }

    private void init(View contentView) {
        context = getActivity();
        Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        TextView tv_title = toolbar.findViewById(R.id.tv_title);
        tv_title.setText(context.getResources().getString(R.string.terms_and_conditions));
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceMedium = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_title.setTypeface(faceLight);
        sv_main = contentView.findViewById(R.id.sv_main);
        tv_terms = contentView.findViewById(R.id.tv_terms);
        tv_terms.setTypeface(faceLight);
        tv_from = contentView.findViewById(R.id.tv_from);
        tv_from.setTypeface(faceLight);
        tv_tech = contentView.findViewById(R.id.tv_tech);
        tv_tech.setTypeface(faceDenmark);
        tv_uva = contentView.findViewById(R.id.tv_uva);
        tv_uva.setTypeface(faceDenmark);
    }

    public static TermsAndConditions newInstance() {
        Bundle args = new Bundle();

        TermsAndConditions fragment = new TermsAndConditions();
        fragment.setArguments(args);
        return fragment;
    }

}
