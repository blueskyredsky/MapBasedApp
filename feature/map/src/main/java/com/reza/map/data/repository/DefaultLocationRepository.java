package com.reza.map.data.repository;

import android.location.Location;

import com.google.android.libraries.places.api.model.Place;
import com.reza.location.LocationManager;
import com.reza.places.PlacesManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class DefaultLocationRepository implements LocationRepository {

    final LocationManager locationManager;
    final PlacesManager placesManager;

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

    @Override
    public Single<Place> getPlace(String placeId, List<Place.Field> placeFields) {
        return placesManager.getPlace(placeId, placeFields);
    }
}
