package com.reza.details.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.reza.details.R;

import java.util.ArrayList;

public class PhotoOptionDialogFragment extends DialogFragment {

    interface PhotoOptionDialogListener {
        void onCaptureClick();

        void onPickClick();
    }

    private PhotoOptionDialogListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        listener = (PhotoOptionDialogListener) getActivity();
        int captureSelectIdx = -1;
        int pickSelectIdx = -1;

        ArrayList<String> options = new ArrayList<>();
        Context context = getActivity();

        if (context != null && canCapture(context)) {
            options.add("Camera");
            captureSelectIdx = 0;
        }

        if (context != null && canPick(context)) {
            options.add("Gallery");
            pickSelectIdx = (captureSelectIdx == 0) ? 1 : 0;
        }

        int finalCaptureSelectIdx = captureSelectIdx;
        int finalPickSelectIdx = pickSelectIdx;

        return new AlertDialog.Builder(context)
                .setTitle(R.string.photo_option)
                .setItems(options.toArray(new CharSequence[0]), (dialog, which) -> {
                    if (which == finalCaptureSelectIdx) {
                        listener.onCaptureClick();
                    } else if (which == finalPickSelectIdx) {
                        listener.onPickClick();
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
    }

    public static boolean canPick(Context context) {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        return (pickIntent.resolveActivity(context.getPackageManager()) != null);
    }

    public static boolean canCapture(Context context) {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        return (captureIntent.resolveActivity(context.getPackageManager()) != null);
    }

    public static PhotoOptionDialogFragment newInstance(Context context) {
        if (canPick(context) || canCapture(context)) {
            return new PhotoOptionDialogFragment();
        } else {
            return null;
        }
    }
}