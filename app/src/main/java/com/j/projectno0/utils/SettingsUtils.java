package com.j.projectno0.utils;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.j.projectno0.R;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingsUtils {
    private static final String LANGUAGE = "language";
    private static final String THEME = "theme";
    private static final String RESET = "reset";


    private static final int NATURE = 0;
    private static final int OCEAN = 1;
    private static final int NIGHT = 2;

    @SuppressLint("ConstantLocale")
    private static final String defaultLang = Locale.getDefault().getLanguage();
    private static final SharedPreferences sharedPreferences =
            App.getContext().getSharedPreferences("DDPreferences", MODE_PRIVATE);

    public static void loadSettings(Resources resources) {
        if (!sharedPreferences.getBoolean(RESET, false)) {
            changeLanguage(resources, sharedPreferences.getString(LANGUAGE, defaultLang));
        }
    }

    public static void resetSettings(Resources resources) {
        Log.d("LanguageLog", "reset = " + sharedPreferences.getBoolean(RESET, false));
        changeLanguage(resources, defaultLang);
        changeTheme(NATURE);
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

    public static void changeTheme(int theme) {
        switch (theme) {
            case NATURE:
                sharedPreferences.edit().putInt(THEME, 0).apply();
                break;
            case OCEAN:
                sharedPreferences.edit().putInt(THEME, 1).apply();
                break;
            case NIGHT:
                sharedPreferences.edit().putInt(THEME, 2).apply();
                break;
        }
    }

    public static int getTheme() {
        switch (sharedPreferences.getInt(THEME, NATURE)) {
            case NATURE:
                return R.style.AppThemeNature;
            case OCEAN:
                return R.style.AppThemeSky;
            case NIGHT:
                return R.style.AppThemeNight;
        }
        return 0;
    }

    public static int getThemeIndex() {
        return sharedPreferences.getInt(THEME, NATURE);
    }

}