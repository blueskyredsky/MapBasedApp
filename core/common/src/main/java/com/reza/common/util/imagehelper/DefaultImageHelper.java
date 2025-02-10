package com.reza.common.util.imagehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;

@Singleton
public class DefaultImageHelper implements ImageHelper {

    private static final String TAG = "DefaultImageHelperTAG";
    private final Context context;

    @Inject
    public DefaultImageHelper(Context context) {
        this.context = context;
    }

    @Override
    public Completable saveBitmapToFile(Bitmap image, String filename) {
        return Completable.create(emitter -> {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] bytes = stream.toByteArray();
                saveBytesToFile(bytes, filename);
                emitter.onComplete();
            } catch (Exception e) {
                emitter.onError(e);
            }
        });
    }

    @Override
    public String generateImageFilename(Long bookmarkId) {
        return "bookmark" + bookmarkId + ".png";
    }

    @Override
    public Maybe<Bitmap> loadBitmapFromFile(String filename) {
        return Maybe.create(emitter -> {
            try {
                String filePath = new File(context.getFilesDir(), filename).getAbsolutePath();
                Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                if (bitmap != null) {
                    emitter.onSuccess(bitmap);
                } else {
                    emitter.onError(new Exception("Failed to load bitmap from file: " + filename));
                }
            } catch (Exception exception) {
                emitter.onError(exception);
            }
        });
    }

    private void saveBytesToFile(byte[] bytes, String filename) {
        FileOutputStream outputStream;
        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(bytes);
            outputStream.close();
        } catch (Exception e) {
            Log.e(TAG, "saveBytesToFile: " + e.getMessage());
        }
    }
}