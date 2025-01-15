package com.reza.map.data.repository.place;

import android.graphics.Bitmap;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.reza.places.PlacesManager;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Single;

public class DefaultPlaceRepository implements PlaceRepository {

    private final PlacesManager placesManager;

    @Inject
    public DefaultPlaceRepository(PlacesManager placesManager) {
        this.placesManager = placesManager;
    }

    @Override
    public Single<Place> getPlace(String placeId, List<Place.Field> placeFields) {
        return placesManager.getPlace(placeId, placeFields);
    }

    @Override
    public Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight) {
        return placesManager.getPhoto(photoMetadata, maxWidth, maxHeight);
    }
}
