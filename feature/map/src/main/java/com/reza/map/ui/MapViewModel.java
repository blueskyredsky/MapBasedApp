package com.reza.map.ui;

import android.graphics.Bitmap;
import android.location.Location;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.reza.common.util.imagehelper.ImageHelper;
import com.reza.data.repository.BookmarkRepository;
import com.reza.data.repository.DefaultBookmarkRepository;
import com.reza.database.BookmarkEntity;
import com.reza.map.data.model.BookmarkMapView;
import com.reza.map.data.repository.location.LocationRepository;
import com.reza.map.data.repository.place.PlaceRepository;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;

public class MapViewModel extends ViewModel {

    private static final String TAG = "MapViewModelTag";
    private final LocationRepository locationRepository;
    private final PlaceRepository placeRepository;
    private final BookmarkRepository bookmarkRepository;
    private final CompositeDisposable compositeDisposable;
    private final ImageHelper imageHelper;
    private final Scheduler ioScheduler;
    private final Scheduler mainScheduler;

    private final MutableLiveData<List<BookmarkMapView>> _bookmarks = new MutableLiveData<>();
    LiveData<List<BookmarkMapView>> bookmarks = _bookmarks;

    @Inject
    MapViewModel(LocationRepository locationRepository,
                 PlaceRepository placeRepository,
                 BookmarkRepository bookmarkRepository,
                 CompositeDisposable compositeDisposable,
                 ImageHelper imageHelper,
                 @IoScheduler Scheduler ioScheduler,
                 @MainScheduler Scheduler mainScheduler
    ) {
        this.locationRepository = locationRepository;
        this.placeRepository = placeRepository;
        this.bookmarkRepository = bookmarkRepository;
        this.compositeDisposable = compositeDisposable;
        this.imageHelper = imageHelper;
        this.ioScheduler = ioScheduler;
        this.mainScheduler = mainScheduler;
    }

    Single<Long> addBookmark(Place place, @Nullable Bitmap photo) {
        Optional<LatLng> LatLngOptional = Optional.ofNullable(place.getLocation());
        double latitude = LatLngOptional.map(latLng -> latLng.latitude).orElse(0.0);
        double longitude = LatLngOptional.map(latLng -> latLng.longitude).orElse(0.0);

        BookmarkEntity bookmark = new BookmarkEntity(
                null,
                place.getId(),
                place.getDisplayName(),
                place.getFormattedAddress(),
                latitude,
                longitude,
                place.getInternationalPhoneNumber(),
                "",
                ""); // fixme added temporarily

        return bookmarkRepository.addBookmark(bookmark);
    }

    private String getPlaceCategory(Place place) {
        String category = DefaultBookmarkRepository.OTHER;
        List<String> types = place.getPlaceTypes();

        if (types != null) {
            if (!types.isEmpty()) {
                String placeType = types.get(0);
                category = bookmarkRepository.placeTypeToCategory(placeType);
            }
        }
        return category;
    }

    Completable saveImageToFile(@Nullable Bitmap photo, Long bookmarkId) {
        if (photo != null) {
            String filename = imageHelper.generateImageFilename(bookmarkId);
            return imageHelper.saveBitmapToFile(photo, filename);
        } else {
            return Completable.complete(); // Complete without doing anything
        }
    }

    void getBookmarks() {
        compositeDisposable.add(
                bookmarkRepository.getAllBookmarks()
                        .map(bookmarkEntities -> bookmarkEntities
                                .stream()
                                .map(this::bookmarkEntityToBookmarkMarker)
                                .collect(Collectors.toList())
                        )
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .onErrorReturn(throwable -> {
                            Log.e(TAG, "getBookmarks: " + throwable.getMessage());
                            return Collections.emptyList();
                        })
                        .subscribe(_bookmarks::setValue,
                                throwable -> Log.e(TAG, "getBookmarks: " + throwable.getMessage()))
        );

        // a better approach is to call this function [bookmarkMarker.setImage(bitmap);] on demand
        /*compositeDisposable.add(
                bookmarkRepository.getAllBookmarks()
                        .flatMap(bookmarkEntities -> Flowable.fromIterable(bookmarkEntities)
                                .flatMap(bookmarkEntity -> {
                                    try {
                                        BookmarkMarker bookmarkMarker = bookmarkEntityToBookmarkMarker(bookmarkEntity);
                                        if (bookmarkMarker.getId() != null) {
                                            return imageHelper.loadBitmapFromFile(imageHelper.generateImageFilename(bookmarkMarker.getId()))
                                                    .subscribeOn(ioScheduler)
                                                    .map(bitmap -> {
                                                        bookmarkMarker.setImage(bitmap);
                                                        return bookmarkMarker;
                                                    })
                                                    .onErrorReturnItem(bookmarkMarker)
                                                    .toFlowable();
                                        } else {
                                            return Flowable.just(bookmarkMarker);
                                        }
                                    } catch (Exception e) {
                                        Log.e(TAG, "Error in loadBitmapFromFile: " + e.getMessage());
                                        return Flowable.empty();
                                    }
                                })
                        )
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .scan(new ArrayList<BookmarkMarker>(), (accumulator, bookmarkMarker) -> {
                            accumulator.add(bookmarkMarker);
                            return accumulator;
                        })
                        .subscribe(_bookmarks::setValue, throwable -> {
                                    Log.e(TAG, "getBookmarks ERROR: " + throwable.getMessage(), throwable);
                                    _bookmarks.setValue(new ArrayList<>());
                                }
                        )
        );*/
    }

    Completable loadBookmarkImage(Long bookmarkId, BookmarkMapView bookmarkMapView) {
        return imageHelper.loadBitmapFromFile(imageHelper.generateImageFilename(bookmarkId))
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .flatMapCompletable(bitmap ->
                        Completable.fromAction(() -> bookmarkMapView.setImage(bitmap))
                );
    }

    private BookmarkMapView bookmarkEntityToBookmarkMarker(BookmarkEntity bookmarkEntity) {
        return new BookmarkMapView(bookmarkEntity.getId(),
                new LatLng(bookmarkEntity.getLatitude(), bookmarkEntity.getLongitude()),
                bookmarkEntity.getName(),
                bookmarkEntity.getPhone(),
                null);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.clear();
    }
}
