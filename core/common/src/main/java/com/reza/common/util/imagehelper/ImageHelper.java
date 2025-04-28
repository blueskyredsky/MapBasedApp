package com.reza.common.util.imagehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import java.io.File;
import java.io.IOException;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

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

    /**
     * Creates a unique file for an image.
     */
    Single<File> createUniqueImageFile();

    /**
     * Decodes a file into a Bitmap.
     */
    Bitmap decodeFileToSize(String filePath, int width, int height);

    /**
     * Rotates an image if required.
     */
    Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException;
}
