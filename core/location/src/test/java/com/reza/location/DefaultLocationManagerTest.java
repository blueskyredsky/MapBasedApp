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
import com.google.android.gms.location.SettingsClient;
import com.google.common.truth.Truth;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(shadows = {ShadowPreconditions.class})
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

    }
}