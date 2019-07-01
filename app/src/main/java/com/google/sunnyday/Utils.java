package com.google.sunnyday;

import android.content.Context;


public class Utils {

    public static int resourceId;

    private Utils()
    {
    }

    public static int getResourceId(Context context, String resourceName, String resourceType) {
        return context.getResources().getIdentifier(resourceName, resourceType, context.getPackageName());
    }
}
