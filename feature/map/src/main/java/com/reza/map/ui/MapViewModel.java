package com.reza.map.ui;

import android.graphics.Bitmap;
import android.location.Location;

import androidx.lifecycle.ViewModel;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.reza.map.data.repository.location.LocationRepository;
import com.reza.map.data.repository.place.PlaceRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MapViewModel extends ViewModel {

    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;

    @Inject
    MapViewModel(LocationRepository locationRepository, PlaceRepository placeRepository) {
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;
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
        return placeRepository.getPlace(placeId, placeFields);
    }

    Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight) {
        return placeRepository.getPhoto(photoMetadata, maxWidth, maxHeight);
    }
}
