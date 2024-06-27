package com.reza.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class DefaultLocationManager implements LocationManager {

    private final FusedLocationProviderClient fusedLocationClient;

    @Inject
    DefaultLocationManager(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }


    @SuppressLint("MissingPermission")
    @Override
    public Single<Location> getLastLocation() {
        return Single.create(emitter ->
                fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null) {
                        emitter.onSuccess(location);
                    } else {
                        emitter.onError(new Exception("Location is null"));
                    }
                }));
    }
}
