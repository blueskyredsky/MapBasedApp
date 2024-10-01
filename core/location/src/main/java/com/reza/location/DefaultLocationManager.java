package com.reza.location;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.processors.BehaviorProcessor;

@Singleton
public class DefaultLocationManager implements LocationManager {

    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private LocationRequest locationRequest;
    private final BehaviorProcessor<Location> locationUpdatesFlowable = BehaviorProcessor.create();

    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                locationUpdatesFlowable.onNext(location);
            }
        }
    };

    @Inject
    DefaultLocationManager(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public Single<Location> getLastLocation() {
        return Single.create(emitter -> {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                emitter.onError(new Exception(context.getString(R.string.permission_is_not_granted)));
            } else {
                fusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location != null) {
                        emitter.onSuccess(location);
                    } else {
                        emitter.onError(new Exception(context.getString(R.string.location_is_null)));
                    }
                });
            }
        });
    }

    @NonNull
    private LocationRequest createLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(1000)
                .build();
    }

    @Override
    public Flowable<Location> getLocationUpdates() {
        return locationUpdatesFlowable;
    }

    @Override
    public Completable startLocationUpdates() {
        return Completable.create(emitter -> {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        emitter.onError(new Exception(context.getString(R.string.permission_is_not_granted)));
                    } else {
                        if (locationRequest == null) {
                            locationRequest = createLocationRequest();
                        }
                        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
                        emitter.onComplete();
                    }
                }
        );
    }

    @Override
    public Completable stopLocationUpdates() {
        return Completable.create(emitter -> {
            try {
                fusedLocationClient.removeLocationUpdates(locationCallback);
                emitter.onComplete();
            } catch (Exception exception) {
                emitter.onError(exception);
            }
        });
    }
}