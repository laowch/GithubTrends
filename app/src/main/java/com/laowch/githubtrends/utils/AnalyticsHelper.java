package com.laowch.githubtrends.utils;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.HitBuilders.ScreenViewBuilder;
import com.google.android.gms.analytics.Tracker;
import com.laowch.githubtrends.R;

import java.io.PrintWriter;
import java.io.StringWriter;


public class AnalyticsHelper {


    private static Tracker sTracker;


    public static void initialize(final Context pContext) {
        AnalyticsHelper.sTracker = GoogleAnalytics.getInstance(pContext).newTracker(R.xml.analytics);
    }

    public static void sendView(final String pScreenName) {
        AnalyticsHelper.sTracker.setScreenName(pScreenName);
        AnalyticsHelper.sTracker.send(new ScreenViewBuilder().build());
        AnalyticsHelper.sTracker.setScreenName(null);
    }

    public static void sendEvent(final String pCategory, final String pAction, final String pLabel, final Long pValue) {

        AnalyticsHelper.sTracker.send(new HitBuilders.EventBuilder().setCategory(pCategory).setAction(pAction).setLabel(pLabel).setValue(pValue).build());
    }


    public static void sendException(Exception ex, boolean isFatal) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);


        AnalyticsHelper.sTracker.send(new HitBuilders.ExceptionBuilder()
                .setDescription(ex.getMessage() + "\n" + sw.toString())
                .setFatal(isFatal)
                .build());
    }

    public static void sendTiming(String category, long timeMills, String action, String label) {
        AnalyticsHelper.sTracker.send(new HitBuilders.TimingBuilder().setCategory(category).setVariable(action).setLabel(label).setValue(timeMills).build());
    }

}