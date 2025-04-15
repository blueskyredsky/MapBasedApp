package com.reza.map.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.reza.common.util.intent.IntentConstants;
import com.reza.common.viewmodel.ViewModelFactory;
import com.reza.map.R;
import com.reza.map.data.model.BookmarkView;
import com.reza.map.data.model.PlaceInfo;
import com.reza.map.databinding.ActivityMapBinding;
import com.reza.map.di.MapComponent;
import com.reza.map.di.MapComponentProvider;
import com.reza.map.ui.adapter.BookmarkInfoWindowAdapter;
import com.reza.map.ui.adapter.BookmarkListAdapter;
import com.reza.map.ui.adapter.OnBookmarkClickListener;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, OnBookmarkClickListener {

    private static final String TAG = "MapActivityTag";

    private ActivityMapBinding binding;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    ViewModelFactory viewModelFactory;

    @IoScheduler
    @Inject
    Scheduler ioScheduler;

    @MainScheduler
    @Inject
    Scheduler mainScheduler;

    private MapViewModel viewModel;

    private GoogleMap map;

    private BookmarkListAdapter adapter;

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCurrentLocationOnMap();
                }
            });

    private void startBookmarkDetailsActivity(Long bookmarkId) {
        Intent intent = new Intent(IntentConstants.ACTION_OPEN_DETAILS);
        intent.putExtra(IntentConstants.EXTRA_BOOKMARK_ID, bookmarkId);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // providing mapComponent
        MapComponent mapComponent = ((MapComponentProvider) getApplicationContext()).provideMapComponent();
        mapComponent.inject(this);

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
        EdgeToEdge.enable(this);
        binding = ActivityMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.drawerLayout, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Apply top padding to the AppBarLayout
            binding.mainMapView.appbar.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            // Apply the insets to the DrawerLayout as well, if needed for other elements
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        setupNavigationDrawer();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.mainMapView.toolbar);
        new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.mainMapView.toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        ).syncState();
    }

    private void setupNavigationDrawer() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.drawerViewMaps.recyclerView.setLayoutManager(layoutManager);
        adapter = new BookmarkListAdapter(null, this);
        binding.drawerViewMaps.recyclerView.setAdapter(adapter);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        setupMapListeners();
        getCurrentLocation();
        createBookmarkMarkerObserver();
        viewModel.getBookmarks();
    }

    private void setupMapListeners() {
        map.setInfoWindowAdapter(new BookmarkInfoWindowAdapter(this));
        map.setOnPoiClickListener(this::displayPoi);
        map.setOnInfoWindowClickListener(this::handleInfoWindowClick);
        map.setOnMarkerClickListener(this::handleOnMarkerClickListener);
    }

    private boolean handleOnMarkerClickListener(Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof BookmarkView) {
            BookmarkView bookmark = (BookmarkView) tag;
            compositeDisposable.add(
                    viewModel.loadBookmarkImage(bookmark.getId(), bookmark)
                            .andThen(Completable.fromAction(marker::showInfoWindow))
                            .subscribe()
            );
            return true;
        } else {
            return false;
        }
    }

    private void displayPoi(PointOfInterest pointOfInterest) {
        displayPoiGetPlaceStep(pointOfInterest);
    }

    private void handleInfoWindowClick(Marker marker) {
        Object tag = marker.getTag();
        if (tag instanceof PlaceInfo) {
            PlaceInfo placeInfo = (PlaceInfo) tag;
            if (placeInfo.getPlace() != null) {
                compositeDisposable.add(viewModel.addBookmark(placeInfo.getPlace(), placeInfo.getPhoto())
                        .flatMapCompletable(bookmarkId -> viewModel.saveImageToFile(placeInfo.getPhoto(), bookmarkId))
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe());
            }
            marker.remove();
        } else if (tag instanceof BookmarkView) {
            BookmarkView bookmark = (BookmarkView) tag;
            marker.hideInfoWindow();
            startBookmarkDetailsActivity(bookmark.getId());
        } else {
            Log.e(TAG, "getInfoContents: unable to handle type");
        }
    }

    private void displayPoiGetPlaceStep(PointOfInterest pointOfInterest) {
        String placeId = pointOfInterest.placeId;
        List<Place.Field> placeFields = List.of(
                Place.Field.ID,
                Place.Field.DISPLAY_NAME,
                Place.Field.PHONE_NUMBER,
                Place.Field.PHOTO_METADATAS,
                Place.Field.FORMATTED_ADDRESS,
                Place.Field.LOCATION,
                Place.Field.TYPES
        );

        compositeDisposable.add(viewModel.getPlace(placeId, placeFields)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(this::displayPoiGetPhotoStep,
                        throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    private void displayPoiGetPhotoStep(Place place) {
        if (place.getPhotoMetadatas() != null) {
            PhotoMetadata photoMetadata = place.getPhotoMetadatas().get(0);
            compositeDisposable.add(viewModel.getPhoto(photoMetadata,
                            getResources().getDimensionPixelSize(com.reza.common.R.dimen.default_image_width),
                            getResources().getDimensionPixelSize(com.reza.common.R.dimen.default_image_height))
                    .subscribeOn(ioScheduler)
                    .observeOn(mainScheduler)
                    .subscribe(bitmap -> displayPoiDisplayStep(place, bitmap),
                            throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show()));
        } else {
            displayPoiDisplayStep(place, null);
        }
    }

    private void displayPoiDisplayStep(Place place, @Nullable Bitmap photo) {
        BitmapDescriptor iconPhoto;
        if (photo == null) {
            iconPhoto = BitmapDescriptorFactory.defaultMarker();
        } else {
            iconPhoto = BitmapDescriptorFactory.fromBitmap(photo);
        }

        if (place.getLocation() != null) {
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(place.getLocation())
                    .title(place.getDisplayName())
                    .snippet(place.getPhoneNumber())
            );

            if (marker != null && photo != null) {
                marker.setTag(new PlaceInfo(place, photo));
                marker.showInfoWindow();
            }
        }
    }

    @Nullable
    private Marker addPlaceMarker(@NonNull BookmarkView bookmarkView) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(bookmarkView.getLocation())
                .title(bookmarkView.getTitle())
                .snippet(bookmarkView.getPhoneNumber())
                .icon(BitmapDescriptorFactory.fromResource(bookmarkView.getCategoryResourceId()))
                .alpha(0.8f));
        if (marker != null) {
            marker.setTag(bookmarkView);
        }
        return marker;
    }

    private void displayAllBookmarks(@NonNull List<BookmarkView> bookmarkViews) {
        for (BookmarkView bookmarkView : bookmarkViews) {
            try {
                addPlaceMarker(bookmarkView);
            } catch (Exception e) {
                Log.e(TAG, "displayAllBookmarks: " + e.getMessage());
            }
        }
    }

    private void createBookmarkMarkerObserver() {
        viewModel.bookmarks.observe(this, bookmarkMarkers -> {
            map.clear();
            displayAllBookmarks(bookmarkMarkers);
            adapter.setBookmarkData(bookmarkMarkers);
        });
    }

    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
            showCurrentLocationOnMap();
//            getLocationUpdates();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(
                this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected, and what
            // features are disabled if it's declined. In this UI,include a
            // "cancel" or "no thanks" button that lets the user continue
            // using your app without granting the permission.
            showEducationalDialogForLocationPermission();
        } else {
            // You can directly ask for the permission.
            // The registered ActivityResultCallback gets the result of this request.
            requestPermissionLauncher.launch(
                    Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    private void getLocationUpdates() {
        compositeDisposable.add(viewModel.getLocationUpdates()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(location -> {
                    // todo handle location updates here
                }, throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show())
        );
    }

    @SuppressLint("MissingPermission")
    private void showCurrentLocationOnMap() {
        compositeDisposable.add(viewModel.getLastLocation()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(location -> {
                    // the permission is already granted
                    map.setMyLocationEnabled(true);
                    LatLng userCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    addMarkerToMap(userCurrentLatLng);
                    moveCameraToLocation(userCurrentLatLng);
                }, throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    private void addMarkerToMap(LatLng latLng) {
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.you_are_here)));
    }

    private void moveCameraToLocation(LatLng latLng) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
        map.moveCamera(update);
    }

    private void showEducationalDialogForLocationPermission() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.title_educational_dialog)
                .setMessage(R.string.description_educational_dialog)
                .setPositiveButton(R.string.ok, (dialog, id) -> requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION))
                .setNegativeButton(R.string.cancel, null) // A null listener allows the button to dismiss the dialog and take no further action.
                .create()
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compositeDisposable.add(
                viewModel.startLocationUpdates()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(() -> {
                            // todo adding a timber.d
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }

    @Override
    protected void onPause() {
        super.onPause();
        compositeDisposable.add(
                viewModel.stopLocationUpdates()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(() -> {
                            // todo adding a timber.d
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        compositeDisposable.dispose();
    }

    @Override
    public void onBookmarkClicked(BookmarkView bookmark) {

    }
}