package com.reza.map.data.repository;

import android.location.Location;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

/**
 * A repository responsible for managing location data.
 * This interface provides methods for fetching the last known location,
 * starting and stopping location updates, and receiving a stream of
 * location updates.
 */
public interface LocationRepository {

    /**
     * Gets the last known location of the device.
     *
     * @return A {@link Single} that emits the last known location or an error.
     */
    Single<Location> getLastLocation();

    /**
     * Starts receiving location updates.
     *
     * @return A {@link Completable} that completes when location updates have
     * been successfully started, or emits an error if there was a problem.
     */
    Completable startLocationUpdates();

    /**
     * Stops receiving location updates.
     *
     * @return A {@link Completable} that completes when location updates have
     * been successfully stopped, or emits an error if there was a problem.
     */
    Completable stopLocationUpdates();

    /**
     * Gets a stream of location updates.
     *
     * @return A {@link Flowable} that emits location updates as they become
     * available. The Flowable handles backpressure to prevent the stream
     * from overwhelming the consumer.
     */
    Flowable<Location> getLocationUpdates();
}
