package com.reza.common.util.imagehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class DefaultImageHelper implements ImageHelper {

    private final Context context;

    @Inject
    public DefaultImageHelper(Context context) {
        this.context = context;
    }

    private static final String TAG = "DefaultImageHelperTAG";

    @Override
    public void saveBitmapToFile(Bitmap bitmap, String filename) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] bytes = stream.toByteArray();
        saveBytesToFile(bytes, filename);
    }

    private void saveBytesToFile(byte[] bytes, String filename) {
        FileOutputStream outputStream = null;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "saveBytesToFile: " + e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Log.e(TAG, "saveBytesToFile: " + e.getMessage());
                }
            }
        }
    }
}
