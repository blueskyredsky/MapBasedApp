package com.reza.common.util.imagehelper;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * Interface for helping with image-related operations.
 */
public interface ImageHelper {

    /**
     * Saves a Bitmap image to a file.
     */
    void saveBitmapToFile(Bitmap image, String filename);
}
