package com.reza.map.data.repository;

import android.location.Location;

import com.reza.location.LocationManager;
import com.reza.places.PlacesManager;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DefaultLocationRepository implements LocationRepository {

    final LocationManager locationManager;
    final PlacesManager placesManager; // todo add functionality for this manger into this repository

    @Inject
    public DefaultLocationRepository(LocationManager locationManager, PlacesManager placesManager) {
        this.locationManager = locationManager;
        this.placesManager = placesManager;
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
