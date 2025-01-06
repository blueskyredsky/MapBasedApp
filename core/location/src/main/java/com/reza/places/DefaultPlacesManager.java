package com.reza.places;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.reza.location.BuildConfig;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Single;

@Singleton
public class DefaultPlacesManager implements PlacesManager {

    private static final String TAG = "DefaultPlacesManagerTAG";
    private PlacesClient placesClient;

    @Inject
    public DefaultPlacesManager(Context context) {
        if (!Places.isInitialized()) {
            Places.initialize(context, BuildConfig.GOOGLE_MAP_API_KEY);
            try {
                placesClient = Places.createClient(context); //Verify initialization
            } catch (Exception e) {
                Log.e(TAG, "Places initialization failed: " + e.getMessage());
            }
        }
        placesClient = Places.createClient(context);
    }

    @Override
    public Single<Place> getPlace(String placeId, List<Place.Field> placeFields) {
        return Single.create(emitter -> {
            FetchPlaceRequest request = FetchPlaceRequest
                    .builder(placeId, placeFields)
                    .build();

            placesClient.fetchPlace(request)
                    .addOnSuccessListener(response -> {
                        emitter.onSuccess(response.getPlace());
                    }).addOnFailureListener(exception -> {
                        if (exception instanceof ApiException) {
                            int statusCode = ((ApiException) exception).getStatusCode();
                            Log.e(TAG, "Place not found: " + exception.getMessage() + ", " + "statusCode: " + statusCode);
                        }
                        emitter.onError(exception);
                    });
        });
    }

    @Override
    public Single<Bitmap> getPhoto(PhotoMetadata photoMetadata, int maxWidth, int maxHeight) {
        return Single.create(emitter -> {
            FetchPhotoRequest request = FetchPhotoRequest
                    .builder(photoMetadata)
                    .setMaxWidth(maxWidth)
                    .setMaxHeight(maxHeight)
                    .build();

            placesClient.fetchPhoto(request).addOnSuccessListener(response -> {
                Bitmap bitmap = response.getBitmap();
                emitter.onSuccess(bitmap);
            }).addOnFailureListener(exception -> {
                if (exception instanceof ApiException) {
                    int statusCode = ((ApiException) exception).getStatusCode();
                    Log.e(TAG, "Photo not found: " + exception.getMessage() + ", " + "statusCode: " + statusCode);
                }
                emitter.onError(exception);
            });
        });
    }
}