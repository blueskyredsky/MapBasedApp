package com.reza.common.util.imagehelper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import androidx.exifinterface.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Completable;
import io.reactivex.Maybe;
import io.reactivex.Single;

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

    @Override
    public Single<File> createUniqueImageFile() {
        return Single.create(emitter -> {
            String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss", Locale.UK).format(new Date());
            String filename = "PlaceBook_" + timeStamp + "_";
            File filesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            try {
                emitter.onSuccess(File.createTempFile(filename, ".jpg", filesDir));
            } catch (IOException ioException) {
                emitter.onError(ioException);
            }
        });
    }

    @Override
    public Bitmap decodeFileToSize(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, width, height);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    @Override
    public Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {
        InputStream input = null;
        try {
            input = context.getContentResolver().openInputStream(selectedImage);
            String path = selectedImage.getPath();
            ExifInterface ei;
            if (input != null) {
                ei = new ExifInterface(input);
            } else if (path != null) {
                ei = new ExifInterface(path);
            } else {
                return img;
            }

            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    return rotateImage(img, 90.0f);
                case ExifInterface.ORIENTATION_ROTATE_180:
                    return rotateImage(img, 180.0f);
                case ExifInterface.ORIENTATION_ROTATE_270:
                    return rotateImage(img, 270.0f);
                default:
                    return img;
            }
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "rotateImageIfRequired: " + e.getMessage());
                }
            }
        }
    }

    @Override
    public Bitmap decodeUriStreamToSize(Uri uri, int width, int height) {
        InputStream inputStream = null;
        try {
            BitmapFactory.Options options;
            inputStream = context.getContentResolver().openInputStream(uri);
            if (inputStream != null) {
                options = new BitmapFactory.Options();
                options.inJustDecodeBounds = false;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
                inputStream = context.getContentResolver().openInputStream(uri);
                if (inputStream != null) {
                    options.inSampleSize = calculateInSampleSize(options.outWidth, options.outHeight, width, height);
                    options.inJustDecodeBounds = false;
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, options);
                    inputStream.close();
                    return bitmap;
                }
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "decodeUriStreamToSize: " + e.getMessage());
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                Log.e(TAG, "decodeUriStreamToSize: " + e.getMessage());
            }
        }
    }

    private int calculateInSampleSize(int width, int height, int reqWidth, int reqHeight) {
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            int halfHeight = height / 2;
            int halfWidth = width / 2;

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    private Bitmap rotateImage(Bitmap img, float degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
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