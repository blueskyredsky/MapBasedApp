package com.reza.details.ui;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.reza.details.databinding.ActivityDetailsBinding;
import com.reza.details.di.DetailsComponent;
import com.reza.details.di.DetailsComponentProvider;

public class DetailsActivity extends AppCompatActivity {

    private ActivityDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // providing mapComponent
        DetailsComponent detailsComponent = ((DetailsComponentProvider) getApplicationContext()).provideDetailsComponent();
        detailsComponent.inject(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ViewCompat.setOnApplyWindowInsetsListener(binding.main, (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupToolbar();
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
    }
}