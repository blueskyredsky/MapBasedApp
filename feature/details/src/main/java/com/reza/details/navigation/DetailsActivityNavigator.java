package com.reza.details.navigation;

import android.content.Context;
import android.content.Intent;

import com.reza.common.navigator.ActivityNavigator;
import com.reza.details.ui.DetailsActivity;

public class DetailsActivityNavigator implements ActivityNavigator {
    @Override
    public Intent createTargetIntent(Context context, String data) {
        Intent intent = new Intent(context, DetailsActivity.class);
        intent.putExtra("data", data);
        return intent;
    }
}
