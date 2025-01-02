package com.reza.places;

import com.google.android.libraries.places.api.model.Place;

import java.util.List;

import io.reactivex.Single;

/**
 * Manages interactions with the Google Places API.
 * <p>
 * This interface provides methods for retrieving place details using a place ID.
 */
public interface PlacesManager {

    /**
     * Retrieves details for a place using its place ID.
     *
     * @param placeId The ID of the place to retrieve details for.
     * @param placeFields A list of {@link Place.Field}s specifying the desired place data.
     * @return A {@link Single} that emits the {@link Place} object containing
     * the retrieved details, or an error if there was a problem.
     */
    Single<Place> getPlace(String placeId, List<Place.Field> placeFields);
}
