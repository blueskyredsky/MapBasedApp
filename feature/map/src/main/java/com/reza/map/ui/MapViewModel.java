package com.reza.map.ui;

import android.graphics.Bitmap;
import android.location.Location;

import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.reza.database.BookmarkEntity;
import com.reza.map.data.repository.bookmark.BookmarkRepository;
import com.reza.map.data.repository.location.LocationRepository;
import com.reza.map.data.repository.place.PlaceRepository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class MapViewModel extends ViewModel {

    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;

    @Inject
    MapViewModel(LocationRepository locationRepository, PlaceRepository placeRepository, BookmarkRepository bookmarkRepository) {
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;
        this.bookmarkRepository = bookmarkRepository;
    }

    Completable addBookmark(Place place, @Nullable Bitmap photo) {
        Optional<LatLng> LatLngOptional = Optional.ofNullable(place.getLocation());
        double latitude = LatLngOptional.map(latLng -> latLng.latitude).orElse(0.0);
        double longitude = LatLngOptional.map(latLng -> latLng.longitude).orElse(0.0);

        BookmarkEntity bookmark = new BookmarkEntity(place.getId(),
                place.getDisplayName(),
                place.getFormattedAddress(),
                latitude,
                longitude,
                place.getInternationalPhoneNumber());

        return bookmarkRepository.addBookmark(bookmark);
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
