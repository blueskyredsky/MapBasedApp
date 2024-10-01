package com.reza.map.ui;

import android.location.Location;

import com.reza.location.LocationManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;
import io.reactivex.observers.TestObserver;
import io.reactivex.subscribers.TestSubscriber;

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
    public void shouldReturnLastLocationWhenCallingLastKnownLocation() {
        Mockito.when(locationManager.getLastLocation()).thenReturn(Single.just(location));

        TestObserver<Location> testObserver = viewModel.getLastLocation().test();

        testObserver.assertValue(location);
        testObserver.dispose();
    }

    @Test
    public void shouldReturnThrowableWhenCallingLastKnownLocation() {
        Throwable throwable = new Throwable("Location is null");
        Mockito.when(locationManager.getLastLocation()).thenReturn(Single.error(throwable));

        TestObserver<Location> testObserver = viewModel.getLastLocation().test();

        testObserver.assertError(throwable);
        testObserver.dispose();
    }

    @Test
    public void shouldStartLocationUpdatesWhenCallingStartLocationUpdates() {
        Mockito.when(locationManager.startLocationUpdates()).thenReturn(Completable.complete());

        TestObserver<Void> testObserver = viewModel.startLocationUpdates().test();

        testObserver.assertComplete();
        testObserver.dispose();
    }

    @Test
    public void shouldStopLocationUpdatesWhenCallingStopLocationUpdates() {
        Mockito.when(locationManager.stopLocationUpdates()).thenReturn(Completable.complete());

        TestObserver<Void> testObserver = viewModel.stopLocationUpdates().test();

        testObserver.assertComplete();
        testObserver.dispose();
    }

    @Test
    public void shouldReturnLocationUpdatesWhenCallingLocationUpdates() {
        Mockito.when(locationManager.getLocationUpdates()).thenReturn(Flowable.just(location));

        TestSubscriber<Location> testObserver = viewModel.getLocationUpdates().test();

        testObserver.assertValue(location);
        testObserver.dispose();
    }
}