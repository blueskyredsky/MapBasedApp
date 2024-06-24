package com.reza.location;

import android.content.Context;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;

public class DefaultLocationManager implements LocationManager {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    @Inject
    DefaultLocationManager(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }


}
