package com.example.ben.mygitapplication.NavigationFragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ben.mygitapplication.R;

/**
 * Created by Ben on 08.11.2016.
 */

public class Fragment_Gyms extends Fragment {

    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_gyms,container,false);
        return myView;
    }
}
