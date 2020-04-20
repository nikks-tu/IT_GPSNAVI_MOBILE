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
import android.widget.TextView;

import com.techuva.vehicletracking.R;

import org.w3c.dom.Text;

public class AboutApp extends Fragment {

    Context context;
    TextView tv_gps, tv_navi, tv_tech, tv_uva;
    TextView tv_from, tv_about_app_text, tv_app_version_txt, tv_app_version;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View contentView = inflater.inflate(R.layout.fragment_about_app, null, false);
        init(contentView);

        return contentView;

    }

    private void init(View v) {
        context = getActivity();
        Toolbar toolbar =  getActivity().findViewById(R.id.toolbar);
        TextView tv_title = toolbar.findViewById(R.id.tv_title);
        Typeface faceLight = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Light.otf");
        Typeface faceMedium = Typeface.createFromAsset(context.getAssets(),
                "fonts/AvenirLTStd-Medium.otf");
        Typeface faceDenmark = Typeface.createFromAsset(context.getAssets(),
                "fonts/films.DENMARK.ttf");
        tv_title.setText(context.getResources().getString(R.string.about_app));
        tv_title.setTypeface(faceLight);
        tv_gps = v.findViewById(R.id.tv_gps);
        tv_gps.setTypeface(faceDenmark);
        tv_navi = v.findViewById(R.id.tv_navi);
        tv_navi.setTypeface(faceDenmark);
        tv_tech = v.findViewById(R.id.tv_tech);
        tv_tech.setTypeface(faceDenmark);
        tv_uva = v.findViewById(R.id.tv_uva);
        tv_uva.setTypeface(faceDenmark);
        tv_from = v.findViewById(R.id.tv_from);
        tv_from.setTypeface(faceLight);
        tv_about_app_text = v.findViewById(R.id.tv_about_app_text);
        tv_about_app_text.setTypeface(faceLight);
        tv_app_version_txt = v.findViewById(R.id.tv_app_version_txt);
        tv_app_version_txt.setTypeface(faceLight);
        tv_app_version = v.findViewById(R.id.tv_app_version);
        tv_app_version.setTypeface(faceLight);
    }

    public static AboutApp newInstance() {
        Bundle args = new Bundle();

        AboutApp fragment = new AboutApp();
        fragment.setArguments(args);
        return fragment;
    }

/*    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/


}
