package com.reza.places;

import android.graphics.Bitmap;

import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoResponse;

import java.util.List;

import io.reactivex.Single;

/**
 * Manages interactions with the Google Places API.
 */
public interface PlacesManager {

    /**
     * Retrieves details for a place using its place ID.
     */
    Single<Place> getPlace(String placeId, List<Place.Field> placeFields);

    /**
     * Retrieves a photo for a given photo metadata.
     */
    Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight);
}
