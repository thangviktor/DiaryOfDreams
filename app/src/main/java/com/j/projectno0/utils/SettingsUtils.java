package com.j.projectno0.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatDelegate;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingsUtils {
    private static final String LANGUAGE = "language";
    private static final String MODE = "mode";
    private static final String RESET = "reset";

    @SuppressLint("ConstantLocale")
    private static final String defaultLang = Locale.getDefault().getLanguage();
    private static final SharedPreferences sharedPreferences =
            App.getContext().getSharedPreferences("DDPreferences", MODE_PRIVATE);

    public static void loadSettings(Resources resources) {
        if (!sharedPreferences.getBoolean(RESET, false)) {
            changeLanguage(resources, sharedPreferences.getString(LANGUAGE, defaultLang));
            changeMode(sharedPreferences.getInt(MODE, AppCompatDelegate.getDefaultNightMode()));
        }
    }

    public static void resetSettings(Resources resources) {
        Log.d("LanguageLog", "reset = " + sharedPreferences.getBoolean(RESET, false));
        changeLanguage(resources, defaultLang);
        changeMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        sharedPreferences.edit().putBoolean(RESET, true).apply();
    }

    public static void changeLanguage(Resources resources, String languageCode) {
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(languageCode));
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        sharedPreferences.edit().putString(LANGUAGE, languageCode).apply();
        sharedPreferences.edit().putBoolean(RESET, false).apply();
        Log.d("LanguageLog", "currentLang: " + languageCode);
    }

    public static void changeMode(int nightMode) {
        AppCompatDelegate.setDefaultNightMode(nightMode);
        sharedPreferences.edit().putInt(MODE, nightMode).apply();
    }
}