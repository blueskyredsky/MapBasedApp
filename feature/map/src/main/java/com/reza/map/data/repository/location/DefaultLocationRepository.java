package com.reza.map.data.repository.location;

import android.location.Location;

import com.reza.location.LocationManager;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DefaultLocationRepository implements LocationRepository {

    final LocationManager locationManager;

    @Inject
    public DefaultLocationRepository(LocationManager locationManager) {
        this.locationManager = locationManager;
    }

    @Override
    public Single<Location> getLastLocation() {
        return locationManager.getLastLocation();
    }

    @Override
    public Completable startLocationUpdates() {
        return locationManager.startLocationUpdates();
    }

    @Override
    public Completable stopLocationUpdates() {
        return locationManager.stopLocationUpdates();
    }

    @Override
    public Flowable<Location> getLocationUpdates() {
        return locationManager.getLocationUpdates();
    }
}