package com.reza.map.ui;

import android.graphics.Bitmap;
import android.location.Location;

import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.reza.map.data.repository.LocationRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

class MapViewModel extends ViewModel {

    final LocationRepository locationRepository;

    @Inject
    MapViewModel(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    Single<Location> getLastLocation() {
        return locationRepository.getLastLocation();
    }

    Flowable<Location> getLocationUpdates() {
        return locationRepository.getLocationUpdates();
    }

    Completable stopLocationUpdates() {
        return locationRepository.stopLocationUpdates();
    }

    Completable startLocationUpdates() {
        return locationRepository.startLocationUpdates();
    }

    Single<Place> getPlace(String placeId, List<Place.Field> placeFields) {
        return locationRepository.getPlace(placeId, placeFields);
    }

    Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight) {
        return locationRepository.getPhoto(photoMetadata, maxWidth, maxHeight);
    }
}
