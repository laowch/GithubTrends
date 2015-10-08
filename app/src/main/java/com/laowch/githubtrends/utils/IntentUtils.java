package com.laowch.githubtrends.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.laowch.githubtrends.Constants;
import com.laowch.githubtrends.ui.AboutActivity;
import com.laowch.githubtrends.ui.AddLanguagesActivity;
import com.laowch.githubtrends.ui.CustomLanguagesActivity;
import com.laowch.githubtrends.ui.FavoritesActivity;

/**
 * Created by lao on 15/9/24.
 */
public class IntentUtils {

    public static void openAbout(Context context) {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
    }

    public static void openFavos(final Context context) {
        Intent intent = new Intent(context, FavoritesActivity.class);
        context.startActivity(intent);
    }

    public static void openCustomLanguage(Context context) {
        Intent intent = new Intent(context, CustomLanguagesActivity.class);
        context.startActivity(intent);
    }

    public static void openRemoveLanguage(Context context) {
        Intent intent = new Intent(context, CustomLanguagesActivity.class);
        intent.putExtra("mode", Constants.MODE_REMOVE);
        context.startActivity(intent);
    }

    public static void openAddLanguage(Context context) {
        Intent intent = new Intent(context, AddLanguagesActivity.class);
        context.startActivity(intent);
    }

    public static void openExternalUrl(final Context context, String url) {

        // process url like intent://scan/#Intent;scheme=zxing;package=com.google.zxing.client.android;end"
        // ref: https://developer.chrome.com/multidevice/android/intents
        try {
            if (url.startsWith("intent")) {

                int schemeStart = url.indexOf("scheme=") + "scheme=".length();
                int schemeEnd = url.indexOf(';', schemeStart);
                String scheme = url.substring(schemeStart, schemeEnd);
                url = scheme + url.substring("intent".length());
            }
        } catch (Exception ex) {
        }


        try {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            context.startActivity(intent);
        } catch (Exception ex) {
        }
    }


}
