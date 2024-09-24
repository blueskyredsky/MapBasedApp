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

/**
 * Default implementation of the {@link LocationManager} interface using the Fused Location Provider.
 * <p>
 * This class provides methods for retrieving the last known location, starting and stopping
 * location updates, and receiving a stream of location updates. It uses the Fused Location
 * Provider for location services and handles permission checks.
 */
@Singleton
public class DefaultLocationManager implements LocationManager {

    private final FusedLocationProviderClient fusedLocationClient;
    private final Context context;
    private LocationRequest locationRequest;

    /**
     * A {@link BehaviorProcessor} used to store and emit location updates.
     */
    private final BehaviorProcessor<Location> locationUpdatesFlowable = BehaviorProcessor.create();

    /**
     * A {@link LocationCallback} that handles location updates from the Fused Location Provider.
     * It emits received locations to the {@link #locationUpdatesFlowable}.
     */
    private final LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {
                locationUpdatesFlowable.onNext(location);
            }
        }
    };

    /**
     * Constructor for {@link DefaultLocationManager}.
     *
     * @param context The application context.
     */
    @Inject
    DefaultLocationManager(Context context) {
        this.context = context;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * Retrieves the last known location of the device.
     * <p>
     * This method checks for location permission and, if granted, requests the last known location
     * from the Fused Location Provider. It emits the location if successful or an error if
     * permission is denied or the location is unavailable.
     *
     * @return A {@link Single} that emits the last known location or an error.
     */
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

    /**
     * Creates a {@link LocationRequest} with high accuracy and specific update intervals.
     * <p>
     * The request is configured for high accuracy, an interval of 5 seconds, and a minimum
     * update interval of 1 second.
     *
     * @return The created {@link LocationRequest} object.
     */
    @NonNull
    private LocationRequest createLocationRequest() {
        return new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 5000)
                .setMinUpdateIntervalMillis(1000)
                .build();
    }

    /**
     * Provides a stream of location updates.
     * <p>
     * This method returns a {@link Flowable} that emits location updates as they become available.
     * Subscribe to this Flowable to receive continuous location updates.
     *
     * @return A {@link Flowable} that emits location updates.
     */
    @Override
    public Flowable<Location> getLocationUpdates() {
        return locationUpdatesFlowable;
    }

    /**
     * Starts location updates.
     * <p>
     * This method initiates the process of receiving location updates from the Fused Location Provider.
     * It checks for location permission and creates a location request if one hasn't been created yet.
     * The returned {@link Completable} completes when the updates have been successfully started.
     *
     * @return A {@link Completable} that completes when location updates are started.
     */
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

    /**
     * Stops location updates.
     * <p>
     * This method stops the delivery of location updates from the Fused Location Provider.
     * The returned {@link Completable} completes when the updates have been successfully stopped.
     *
     * @return A {@link Completable} that completes when location updates are stopped.
     */
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