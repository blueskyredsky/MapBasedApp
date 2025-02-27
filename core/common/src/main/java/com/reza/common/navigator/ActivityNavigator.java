package com.reza.common.navigator;

import android.content.Context;
import android.content.Intent;

/**
 * interface defines a contract for creating intent to navigate to specific activities
 */
public interface ActivityNavigator {
    Intent createTargetIntent(Context context, String data);
}
