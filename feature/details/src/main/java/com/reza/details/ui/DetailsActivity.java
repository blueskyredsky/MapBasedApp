package com.reza.details.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.reza.common.util.intent.IntentConstants;
import com.reza.common.viewmodel.ViewModelFactory;
import com.reza.details.R;
import com.reza.details.data.model.BookmarkDetailsView;
import com.reza.details.databinding.ActivityDetailsBinding;
import com.reza.details.di.DetailsComponent;
import com.reza.details.di.DetailsComponentProvider;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class DetailsActivity extends AppCompatActivity implements PhotoOptionDialogFragment.PhotoOptionDialogListener {

    private static final String TAG = "DetailsActivity";

    private ActivityDetailsBinding binding;

    @Inject
    CompositeDisposable compositeDisposable;

    @Inject
    ViewModelFactory viewModelFactory;

    @IoScheduler
    @Inject
    Scheduler ioScheduler;

    @MainScheduler
    @Inject
    Scheduler mainScheduler;

    private DetailsViewModel viewModel;

    private Long bookmarkId;

    private ActivityResultLauncher<Uri> takePictureLauncher;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private Uri currentPhotoUri;
    private File currentPhotoFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // providing mapComponent
        DetailsComponent detailsComponent = ((DetailsComponentProvider) getApplicationContext()).provideDetailsComponent();
        detailsComponent.inject(this);

        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, viewModelFactory).get(DetailsViewModel.class);
        EdgeToEdge.enable(this);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initPictureLauncher();
        initPickerLauncher();
        setupToolbar();
        getIntentData();
        observeBookmark();
        observeIsSavingDone();
        setupListeners();
    }

    private void initPickerLauncher() {
        imagePickerLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null && data.getData() != null) {
                            Uri selectedImageUri = data.getData();
                            Bitmap bitmap = viewModel.getImageWithUri(
                                    selectedImageUri,
                                    getResources().getDimensionPixelSize(com.reza.common.R.dimen.default_image_width),
                                    getResources().getDimensionPixelSize(com.reza.common.R.dimen.default_image_height)
                            );
                            try {
                                Bitmap modifiedImage = viewModel.rotateImageIfRequired(bitmap, selectedImageUri);
                                updateImage(modifiedImage);
                            } catch (IOException e) {
                                Log.e(TAG, "Error rotating image" + e.getMessage());
                            }
                        }
                    } else {
                        Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void initPictureLauncher() {
        takePictureLauncher = registerForActivityResult(
                new ActivityResultContracts.TakePicture(),
                success -> {
                    if (success) {
                        Bitmap bitmap = viewModel.getImageWithPath(
                                currentPhotoFile.getAbsolutePath(),
                                getResources().getDimensionPixelSize(com.reza.common.R.dimen.default_image_width),
                                getResources().getDimensionPixelSize(com.reza.common.R.dimen.default_image_height)
                        );
                        try {
                            Bitmap modifiedImage = viewModel.rotateImageIfRequired(bitmap, currentPhotoUri);
                            updateImage(modifiedImage);
                        } catch (IOException e) {
                            Log.e(TAG, "Error rotating image" + e.getMessage());
                        }
                    } else {
                        // Handle the case where taking the picture was cancelled or failed
                        if (currentPhotoFile != null) {
                            currentPhotoFile.delete();
                        }
                    }
                });
    }

    private void updateImage(Bitmap image) {
        binding.imageViewPlace.setImageBitmap(image);
        compositeDisposable.add(viewModel.saveImageToFile(image, bookmarkId)
                .subscribeOn(ioScheduler)
                .observeOn(mainScheduler)
                .subscribe(() -> Toast.makeText(this, R.string.image_saved, Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show()
                )
        );
    }

    private void setupListeners() {
        binding.imageViewPlace.setOnClickListener(v -> replaceImage());
    }

    private void observeIsSavingDone() {
        viewModel.isSavingDone.observe(this, isSavingDone -> {
            Toast.makeText(this, R.string.bookmark_saved, Toast.LENGTH_SHORT).show();
        });
    }

    private void observeBookmark() {
        viewModel.bookmarks.observe(this, bookmark -> {
            if (bookmark != null) {
                binding.imageViewPlace.setImageBitmap(bookmark.getImage());
                binding.editTextName.setText(bookmark.getName());
                binding.editTextNotes.setText(bookmark.getNotes());
                binding.editTextPhone.setText(bookmark.getPhoneNumber());
                binding.editTextAddress.setText(bookmark.getAddress());
            }
        });
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getIntentData() {
        bookmarkId = getIntent().getLongExtra(IntentConstants.EXTRA_BOOKMARK_ID, 0);
        viewModel.loadBookmark(bookmarkId);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.action_save) {
            String address = binding.editTextAddress.getText() != null ? binding.editTextAddress.getText().toString() : "";
            String notes = binding.editTextNotes.getText() != null ? binding.editTextNotes.getText().toString() : "";
            String name = binding.editTextName.getText() != null ? binding.editTextName.getText().toString() : "";
            String phone = binding.editTextPhone.getText() != null ? binding.editTextPhone.getText().toString() : "";

            viewModel.updateBookmark(new BookmarkDetailsView(
                            address,
                            notes,
                            name,
                            phone,
                            null),
                    bookmarkId);
            return true;
        } else if (itemId == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark_details, menu);
        return true;
    }

    @Override
    public void onCaptureClick() {
        compositeDisposable.add(
                viewModel.createUniqueImageFile()
                        .subscribeOn(ioScheduler)
                        .observeOn(mainScheduler)
                        .subscribe(file -> {
                            currentPhotoFile = file;
                            currentPhotoUri = FileProvider.getUriForFile(this,
                                    getPackageName() + ".fileprovider",
                                    file);
                            takePictureLauncher.launch(currentPhotoUri);
                        }, throwable -> {
                            Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        })
        );
    }

    @Override
    public void onPickClick() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        imagePickerLauncher.launch(intent);
    }

    void replaceImage() {
        PhotoOptionDialogFragment photoOptionDialogFragment = PhotoOptionDialogFragment.newInstance(this);
        if (photoOptionDialogFragment != null) {
            photoOptionDialogFragment.show(getSupportFragmentManager(), TAG);
        }
    }
}