package com.reza.map.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.reza.common.viewmodel.ViewModelFactory;
import com.reza.map.R;
import com.reza.map.data.di.MapComponent;
import com.reza.map.data.di.MapComponentProvider;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

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

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher, as an instance variable.
    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    showCurrentLocationOnMap();
                } else {
                    showEducationalDialogForLocationPermission();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // providing mapComponent
        MapComponent mapComponent = ((MapComponentProvider) getApplicationContext()).provideMapComponent();
        mapComponent.inject(this);

        super.onCreate(savedInstanceState);

        viewModel = new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.map), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        getCurrentLocation();
    }

    /**
     * Checks and requests location permission if necessary.
     * <p>
     * This method verifies if the app has permission to access fine location.
     * If permission is granted, it proceeds to display the current location on the map.
     * If permission is not granted and the user should be shown a rationale, an educational dialog is displayed.
     * Otherwise, the method directly requests the location permission.
     */
    private void getCurrentLocation() {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            // You can use the API that requires the permission.
//            showCurrentLocationOnMap();
//            getLocationUpdates();
            map.setMyLocationEnabled(true);
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

    /**
     * Subscribes to location updates and handles them.
     * <p>
     * This method subscribes to the location updates Flowable from the ViewModel and handles
     * each location update. It displays a toast message if an error occurs.
     */
    private void getLocationUpdates() {
        compositeDisposable.add(viewModel.getLocationUpdates()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(location -> {
                    // todo handle location updates here
                }, throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show())
        );
    }

    /**
     * Displays the current location on the map.
     * <p>
     * This method subscribes to the location updates Flowable from the ViewModel and displays
     * the current location on the map by adding a marker and moving the camera.
     * It displays a toast message if an error occurs.
     */
    private void showCurrentLocationOnMap() {
        compositeDisposable.add(viewModel.getLocationUpdates()
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(location -> {
                    LatLng userCurrentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                    addMarkerToMap(userCurrentLatLng);
                    moveCameraToLocation(userCurrentLatLng);
                }, throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show()));
    }

    /**
     * Adds a marker to the map at the specified location.
     *
     * @param latLng The LatLng of the marker.
     */
    private void addMarkerToMap(LatLng latLng) {
        map.clear();
        map.addMarker(new MarkerOptions().position(latLng).title(getString(R.string.you_are_here)));
    }

    /**
     * Moves the camera to the specified location.
     *
     * @param latLng The LatLng to move the camera to.
     */
    private void moveCameraToLocation(LatLng latLng) {
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(latLng, 16.0f);
        map.moveCamera(update);
    }

    /**
     * Displays an educational dialog to the user explaining the need for location permission.
     * <p>
     * The dialog provides "Ok" and "Cancel" buttons for user interaction.
     * If the user clicks "Ok", the permission request is launched.
     */
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
}