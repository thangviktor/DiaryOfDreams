package com.j.projectno0.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.j.projectno0.R;
import com.j.projectno0.utils.LanguageUtils;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
public class SettingActivity extends AppCompatActivity {

    TextView tvLang, tvTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getString(R.string.setting));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvLang = findViewById(R.id.tvLanguage);
        tvTheme = findViewById(R.id.tvTheme);

        tvLang.setOnClickListener(onLangClick());
    }

    private View.OnClickListener onLangClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] languages = getResources().getStringArray(R.array.languages);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getString(R.string.language));
                int currentLang = LanguageUtils.getCurrentLanguage(getBaseContext());
                builder.setSingleChoiceItems(languages, currentLang, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (i == 1) {
                            LanguageUtils.changeLanguage(getBaseContext(), "en");
                        }
                        else {
                            LanguageUtils.changeLanguage(getBaseContext(), "vi");
                        }
                        refreshLayout();
                    }
                });
                builder.show();
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(R.anim.anim_exit, R.anim.anim_enter);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void refreshLayout(){
        Intent intent = getIntent();
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
}