package com.reza.map.data.repository.place;

import android.graphics.Bitmap;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import io.reactivex.Single;

/**
 * A repository interface for retrieving place-related data.
 */
public interface PlaceRepository {

    /**
     * Retrieves details for a place using its place ID.
     */
    Single<Place> getPlace(String placeId, List<Place.Field> placeFields);

    /**
     * Retrieves a photo for a given photo metadata.
     */
    Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight);
}
