package com.j.projectno0.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.j.projectno0.R;

public class SettingActivity extends AppCompatActivity {
    TextView tvLang, tvTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

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
                builder.setItems(languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.show();
            }
        };
    }
}