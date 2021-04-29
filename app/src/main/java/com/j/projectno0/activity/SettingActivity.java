package com.j.projectno0.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.j.projectno0.R;
import com.j.projectno0.utils.SettingsUtils;

import java.util.Objects;

@SuppressLint("CutPasteId")
@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingActivity extends BaseActivity {
    private int curLangIndex;
    private int curThemeIndex;
    private Boolean changed;

    private TextView tvLanguage;
    private TextView tvTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getSupportActionBar()).setTitle(getString(R.string.setting));
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        ViewGroup llLanguage = findViewById(R.id.llLang);
        ViewGroup llTheme = findViewById(R.id.llTheme);
        ViewGroup llReset = findViewById(R.id.llReset);
        tvLanguage = findViewById(R.id.tvLanguage);
        tvTheme = findViewById(R.id.tvTheme);

        changed = getIntent().getBooleanExtra("changed", false);
        setLanguage();
        curThemeIndex = SettingsUtils.getThemeIndex();
        tvTheme.setText(getResources().getStringArray(R.array.themes)[curThemeIndex]);

        llLanguage.setOnClickListener(onLanguageClick());
        llTheme.setOnClickListener(onThemeClick());
        llReset.setOnClickListener(onResetClick());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (changed) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.anim_exit, R.anim.anim_enter);
        }
        finish();
    }

    /********************************** Event Function ********************************************/
    private View.OnClickListener onLanguageClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getString(R.string.language));
                builder.setSingleChoiceItems(getResources().getStringArray(R.array.languages),
                        curLangIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i != curLangIndex) {
                                    String changedLang = getResources().getStringArray(R.array.languages_code)[i];
                                    SettingsUtils.changeLanguage(getResources(), changedLang);
                                    changed = i != curLangIndex;
                                    refreshLayout();
                                }
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
            }
        };
    }

    private View.OnClickListener onThemeClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getString(R.string.theme));
                builder.setSingleChoiceItems(getResources().getStringArray(R.array.themes),
                        curThemeIndex, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (i != curThemeIndex) {
                                    SettingsUtils.changeTheme(i);
                                    changed = i != curThemeIndex;
                                    refreshLayout();
                                }
                                dialogInterface.cancel();
                            }
                        });
                builder.show();
            }
        };
    }

    private View.OnClickListener onResetClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(getString(R.string.reset) + " ?")
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                SettingsUtils.resetSettings(getResources());
                                refreshLayout();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        };
    }

    /********************************** Class Function ********************************************/
    private void refreshLayout() {
        Intent intent = getIntent();
        intent.putExtra("changed", changed);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }

    private void setLanguage() {
        String currentLang = getResources().getConfiguration().locale.getLanguage();
        int length = getResources().getStringArray(R.array.languages_code).length;
        for (int i = 0; i < length; i++) {
            if (currentLang.equals(getResources().getStringArray(R.array.languages_code)[i]))
                curLangIndex = i;
        }
        tvLanguage.setText(getResources().getStringArray(R.array.languages)[curLangIndex]);
    }
}