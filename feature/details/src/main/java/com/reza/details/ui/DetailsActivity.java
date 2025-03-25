package com.reza.details.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.reza.common.util.intent.IntentConstants;
import com.reza.common.viewmodel.ViewModelFactory;
import com.reza.details.R;
import com.reza.details.databinding.ActivityDetailsBinding;
import com.reza.details.di.DetailsComponent;
import com.reza.details.di.DetailsComponentProvider;
import com.reza.threading.schedulers.IoScheduler;
import com.reza.threading.schedulers.MainScheduler;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.disposables.CompositeDisposable;

public class DetailsActivity extends AppCompatActivity {

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

        setupToolbar();
        getIntentData();
        observeBookmark();
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
        Long bookmarkId = getIntent().getLongExtra(IntentConstants.EXTRA_BOOKMARK_ID, 0);
        viewModel.loadBookmarks(bookmarkId);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_bookmark_details, menu);
        return true;
    }
}