package com.example.ben.mygitapplication.NavigationFragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ben.mygitapplication.Data.User;
import com.example.ben.mygitapplication.R;

import org.w3c.dom.Text;

/**
 * Created by Ben on 08.11.2016.
 */

public class Fragment_MyProfile extends Fragment {



    View myView;
    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_my_profile,container,false);
        user = getArguments().getParcelable("userdata");

        return myView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        showUserData();
    }

    private void showUserData() {
        ImageView image = (ImageView)getView().findViewById(R.id.imgProfilePic);
        TextView tvUsername = (TextView)getView().findViewById(R.id.tvUsername);
        TextView tvEmail = (TextView)getView().findViewById(R.id.tvEmail);
        TextView tvSize = (TextView)getView().findViewById(R.id.tvSize);
        TextView tvWeight = (TextView)getView().findViewById(R.id.tvWeight);
        TextView tvGender = (TextView)getView().findViewById(R.id.tvGender);
        TextView tvExperience = (TextView)getView().findViewById(R.id.tvExperience);
        TextView tvAim = (TextView)getView().findViewById(R.id.tvAim);


        String encodedDataString =user.getBase64Picture().replace("data:image/jpeg;base64,","").replace(" ", "+");
        byte[] imageAsBytes = Base64.decode(encodedDataString.getBytes(), 0);
        image.setImageBitmap(BitmapFactory.decodeByteArray(
                imageAsBytes, 0, imageAsBytes.length));

        tvUsername.setText(user.getUsername());
        tvEmail.setText(user.getEmail());
        tvSize.setText(user.getSize() + " cm");
        tvWeight.setText(String.valueOf(user.getWeight()) + " Kg");
        tvGender.setText(user.getGender());
        tvExperience.setText(user.getExperience());
        tvAim.setText(user.getAim());







    }
}
