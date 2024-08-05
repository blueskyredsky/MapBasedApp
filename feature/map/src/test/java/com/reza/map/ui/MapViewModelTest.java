package com.reza.map.ui;

import android.location.Location;

import com.google.common.truth.Truth;
import com.reza.location.LocationManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Single;
import io.reactivex.observers.TestObserver;

@RunWith(MockitoJUnitRunner.class)
public class MapViewModelTest {

    @Mock
    private LocationManager locationManager;

    private MapViewModel viewModel;

    private final Location location = new Location(""); //provider name is unnecessary

    @Before
    public void setUp() {
        viewModel = new MapViewModel(locationManager);
    }

    @After
    public void tearDown() {
        /* NO-OP */
    }

    @Test
    public void shouldReturnLocationWhenFetchingLocationIsSuccessful() {
        Mockito.when(locationManager.getLastLocation()).thenReturn(Single.just(location));

        TestObserver<Location> testObserver = viewModel.getLastLocation().test();

        testObserver.assertValue(location);
        testObserver.dispose();
    }

    @Test
    public void shouldReturnThrowableWhenFetchingLocationIsNotSuccessful() {
        Throwable throwable = new Throwable("Location is null");
        Mockito.when(locationManager.getLastLocation()).thenReturn(Single.error(throwable));

        TestObserver<Location> testObserver = viewModel.getLastLocation().test();

        testObserver.assertError(throwable);
        testObserver.dispose();
    }
}