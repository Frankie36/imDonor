package com.mwikali.imdonor.utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.mwikali.imdonor.R;
import com.mwikali.imdonor.activity.WebViewActivity;
import com.mwikali.imdonor.shared.CustomTabActivityHelper;


public class ArticleUtils {
    /**
     * Handles opening the url in a custom chrome tab
     *
     * @param uri
     */
    public static void openCustomChromeTab(Activity activity, Uri uri) {
        CustomTabsIntent.Builder intentBuilder = new CustomTabsIntent.Builder();

        // set toolbar colors
        intentBuilder.setToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimary));
        intentBuilder.setSecondaryToolbarColor(ContextCompat.getColor(activity, R.color.colorPrimaryDark));

        // set start and exit animations
        intentBuilder.setStartAnimations(activity, R.anim.slide_in_right, R.anim.slide_out_left);
        intentBuilder.setExitAnimations(activity, android.R.anim.slide_in_left,
                android.R.anim.slide_out_right);

        // build custom tabs intent
        CustomTabsIntent customTabsIntent = intentBuilder.build();

        // call helper to open custom tab
        CustomTabActivityHelper.openCustomTab(activity, customTabsIntent, uri, new CustomTabActivityHelper.CustomTabFallback() {
            @Override
            public void openUri(Activity activity, Uri uri) {
                // fall back, call open open webview
                openWebView(activity, uri);
            }
        });
    }

    /**
     * Handles opening the url in a webview
     *
     * @param uri
     */
    public static void openWebView(Activity activity, Uri uri) {
        Intent webViewIntent = new Intent(activity, WebViewActivity.class);
        webViewIntent.putExtra(WebViewActivity.EXTRA_URL, uri.toString());
        activity.startActivity(webViewIntent);
    }

}