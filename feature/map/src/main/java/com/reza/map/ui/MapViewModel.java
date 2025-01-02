package com.reza.map.ui;

import android.location.Location;

import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.Place;
import com.reza.map.data.repository.LocationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MapViewModel extends ViewModel {

    final LocationRepository locationRepository;

    @Inject
    public MapViewModel(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Single<Location> getLastLocation() {
        return locationRepository.getLastLocation();
    }

    public Flowable<Location> getLocationUpdates() {
        return locationRepository.getLocationUpdates();
    }

    public Completable stopLocationUpdates() {
        return locationRepository.stopLocationUpdates();
    }

    public Completable startLocationUpdates() {
        return locationRepository.startLocationUpdates();
    }

    public Single<Place> getPlace(String placeId, List<Place.Field> placeFields) {
        return locationRepository.getPlace(placeId, placeFields);
    }
}
