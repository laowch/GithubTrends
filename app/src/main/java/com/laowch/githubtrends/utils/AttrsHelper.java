package com.laowch.githubtrends.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by lao on 9/30/15.
 */
public class AttrsHelper {

    public static int getColor(Context context, int attr) {
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = context.getTheme();
        theme.resolveAttribute(attr, typedValue, true);
        int color = typedValue.data;

        return color;
    }
}
