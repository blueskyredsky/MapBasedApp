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
import com.reza.database.BookmarkEntity;
import com.reza.map.data.model.BookmarkMarker;
import com.reza.map.data.repository.bookmark.BookmarkRepository;
import com.reza.map.data.repository.location.LocationRepository;
import com.reza.map.data.repository.place.PlaceRepository;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import java.util.ArrayList;
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

    private final MutableLiveData<List<BookmarkMarker>> _bookmarks = new MutableLiveData<>();
    LiveData<List<BookmarkMarker>> bookmarks = _bookmarks;

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

        BookmarkEntity bookmark = new BookmarkEntity(place.getId(),
                place.getDisplayName(),
                place.getFormattedAddress(),
                latitude,
                longitude,
                place.getInternationalPhoneNumber());

        return bookmarkRepository.addBookmark(bookmark);
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
        /*compositeDisposable.add(
                bookmarkRepository.getAllBookmarks()
                        .map(bookmarkEntities -> bookmarkEntities
                                .stream()
                                .map(this::bookmarkEntityToBookmarkMarker)
                                .collect(Collectors.toList())
                        )
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(_bookmarks::setValue,
                                throwable -> Log.e(TAG, "getBookmarks: " + throwable.getMessage()))
        );*/

        compositeDisposable.add(
                bookmarkRepository.getAllBookmarks()
                        .flatMap(bookmarkEntities -> Flowable.fromIterable(bookmarkEntities)
                                .flatMap(bookmarkEntity -> {
                                    BookmarkMarker bookmarkMarker = bookmarkEntityToBookmarkMarker(bookmarkEntity);
                                    if (bookmarkMarker.getId() != null) {
                                        return imageHelper.loadBitmapFromFile(imageHelper.generateImageFilename(bookmarkMarker.getId())) // Assuming getImagePath exists
                                                .map(bitmap -> {
                                                    bookmarkMarker.setImage(bitmap); // Set the bitmap to the BookmarkMarker
                                                    return bookmarkMarker;
                                                })
                                                .subscribeOn(ioScheduler)
                                                .defaultIfEmpty(bookmarkMarker) // Important: Handle cases where bitmap loading fails
                                                .toFlowable(); // Convert Maybe back to Flowable
                                    } else {
                                        return Flowable.just(bookmarkMarker);
                                    }
                                }) // Limit concurrency to 1 to avoid excessive bitmap loading*/
                        )
                        .map(bookmarkMarker -> {
                            return bookmarkMarker;
                        })
                        // .toList()
                        .map(bookmarkMarkers -> {
                            return bookmarkMarkers;
                        })
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(value -> {
                                    ArrayList<BookmarkMarker> bookmarkMarkers = new ArrayList<>();
                                    bookmarkMarkers.add(value);
                                    _bookmarks.setValue(bookmarkMarkers);
                                },
                                throwable -> {
                                    Log.e(TAG, "getBookmarks: " + throwable.getMessage());
                                    _bookmarks.setValue(new ArrayList<>());
                                }
                        )
        );
    }

    private BookmarkMarker bookmarkEntityToBookmarkMarker(BookmarkEntity bookmarkEntity) {
        return new BookmarkMarker(bookmarkEntity.getId(),
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
