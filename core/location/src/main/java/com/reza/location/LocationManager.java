package com.reza.location;

import io.reactivex.Flowable;
import io.reactivex.Single;

import android.location.Location;

public interface LocationManager {
    Single<Location> getLastLocation();
    Flowable<Location> getLocationUpdates();
}
