package com.reza.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class DefaultLocationManager implements LocationManager {

    private Context context;
    private FusedLocationProviderClient fusedLocationClient;

    @Inject
    DefaultLocationManager(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }


    @Override
    public Single<Location> getLastLocation() {
        return Single.create(emitter -> {
//            fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
//                Location location = task.getResult();
//            });
        });
    }
}
