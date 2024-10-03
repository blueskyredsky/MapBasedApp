package com.reza.location;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.Priority;
import com.google.android.gms.tasks.Task;
import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class DefaultLocationManagerTest {
    Context context;

    @Before
    public void setup() {
        context = ApplicationProvider.getApplicationContext();
    }

    @After
    public void cleanup() {

    }

    @Test
    public void testShouldCreateLocationClient() {
        FusedLocationProviderClient client = LocationServices.getFusedLocationProviderClient(context);
        Truth.assertThat(client).isNotNull();
    }

    @Test
    public void testShouldCreateLocationRequest() {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build();
        Truth.assertThat(request).isNotNull();
    }

    @Test
    public void testShouldCreateLocationCallback() {
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

            }
        };
        Truth.assertThat(callback).isNotNull();
    }

    @Test
    public void testShouldRequestLastKnownLocation() {
        Task<Location> locationTask = LocationServices.getFusedLocationProviderClient(context)
                .getLastLocation();
        Truth.assertThat(locationTask).isNotNull();
    }

    @Test
    public void testShouldRequestLocationUpdates() {
        LocationRequest request = new LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 0).build();
        LocationCallback callback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {

            }
        };
        Task<Void> locationupdateTask = LocationServices.getFusedLocationProviderClient(context)
                .requestLocationUpdates(request, callback, null);
        Truth.assertThat(locationupdateTask).isNotNull();
    }
}