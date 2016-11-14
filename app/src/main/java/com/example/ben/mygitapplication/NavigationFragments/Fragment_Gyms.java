package com.example.ben.mygitapplication.NavigationFragments;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;

import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ben.mygitapplication.BackgroundWorker.NearbyPlacesTask;
import com.example.ben.mygitapplication.Data.User;
import com.example.ben.mygitapplication.LocationHelper;
import com.example.ben.mygitapplication.R;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.content.Context.LOCATION_SERVICE;


/**
 * Created by Ben on 08.11.2016.
 */

public class Fragment_Gyms extends Fragment implements OnMapReadyCallback {

    private static final String TAG = "Debug";
    private static final int REQUEST_PLACE_PICKER = 1;
    View myView;
    User user;
    //Context context;
    GoogleMap gmap;
    Location currerntLocation;
    private static final int INITIAL_REQUEST = 1337;
    private static final int LOCATION_REQUEST = INITIAL_REQUEST + 3;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private static final String[] INITIAL_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.layout_gyms, container, false);
        user = getArguments().getParcelable("userdata");
        return myView;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        MapFragment map = getMapFragment();
        map.getMapAsync(this);
        //PlacePicker.IntentBuilder intentBuilder = new PlacePicker.IntentBuilder();
        //https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522,151.1957362&radius=500&types=food&name=cruise&key=YOUR_API_KEY


    }

    private MapFragment getMapFragment() {
        FragmentManager fm = null;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            fm = getFragmentManager();
        } else {
            fm = getChildFragmentManager();
        }
        return (MapFragment) fm.findFragmentById(R.id.mygoogleMap);
    }

    @Override
    public void onMapReady(final GoogleMap map) {

        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            requestLocation();
        }
        gmap = map;

    }


    public void requestLocation() {
        LocationHelper locationHelper = new LocationHelper();
        LocationHelper.LocationResult locationResult = new LocationHelper.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                setMap(location);
            }
        };
        locationHelper.getLocation(getActivity(), locationResult);

    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    requestLocation();
                } else {
                    //permission denied
                }
                return;
            }

        }
    }

    public void setMap(Location location) {
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        gmap.setMyLocationEnabled(true);
        gmap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
        gmap.animateCamera(CameraUpdateFactory.zoomTo(14));
        addNearPlaces(location);


    }


    private void addNearPlaces(Location location) {
        NearbyPlacesTask nearby = new NearbyPlacesTask(String.valueOf(location.getLongitude()), String.valueOf(location.getLatitude()),
                new NearbyPlacesTask.TaskListener() {
                    @Override
                    public void onFinished(JSONObject json) {
                        try {
                            JSONArray leaders = json.getJSONArray("results");
                            Log.d("leaders", leaders.toString());
                            for (int i = 0; i < leaders.length(); i++) {
                                JSONObject jsonResult = leaders.getJSONObject(i);

                                JSONObject location = jsonResult.getJSONObject("geometry").getJSONObject("location");

                                double lat = Double.parseDouble(location.getString("lat"));
                                double lng = Double.parseDouble(location.getString("lng"));

                                LatLng latLng = new LatLng(lat, lng);
                                String name = jsonResult.getString("name");
                                String vicinity = jsonResult.getString("vicinity");

                                MarkerOptions markerOptions = new MarkerOptions();
                                markerOptions.position(latLng);
                                markerOptions.title(name + " : " + vicinity);
                                gmap.addMarker(markerOptions);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                    }
                });
        nearby.execute((Void) null);

    }
}


