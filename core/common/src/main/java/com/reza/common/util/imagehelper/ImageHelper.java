package com.reza.common.util.imagehelper;

import android.graphics.Bitmap;

import io.reactivex.Completable;
import io.reactivex.Maybe;

/**
 * Interface for helping with image-related operations.
 */
public interface ImageHelper {

    /**
     * Saves a Bitmap image to a file.
     */
    Completable saveBitmapToFile(Bitmap image, String filename);

    /**
     * Generates a unique filename for an image.
     */
    String generateImageFilename(Long bookmarkId);

    /**
     * Loads a Bitmap image from a file.
     */
    Maybe<Bitmap> loadBitmapFromFile(String filename);
}
