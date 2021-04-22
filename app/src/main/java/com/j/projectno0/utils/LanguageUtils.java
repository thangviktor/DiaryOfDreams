package com.j.projectno0.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class LanguageUtils {
    private static final String LANGUAGE = "language";

    @SuppressLint("ConstantLocale")
    private static final String defaultLang = Locale.getDefault().getLanguage();
    private static final SharedPreferences sharedPreferences =
            App.getContext().getSharedPreferences("DDPreferences", MODE_PRIVATE);

    public static void loadLocale(Context context) {
        Log.d("LanguageLog", "defaultLang: " + defaultLang);
        changeLanguage(context, sharedPreferences.getString(LANGUAGE, defaultLang));
    }

    public static void changeLanguage(Context context, String languageCode) {

        Resources resources = context.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(new Locale(languageCode));
        Log.d("LanguageLog", configuration.locale.getLanguage());
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());

        sharedPreferences.edit().putString(LANGUAGE, languageCode).apply();
        Log.d("LanguageLog", "currentLang: " + languageCode);

    }

    public static int getCurrentLanguage(Context context) {
        String currentLang = context.getResources().getConfiguration().locale.getLanguage();
        if (currentLang.equals("en"))
            return 1;
        else
            return 0;
    }
}