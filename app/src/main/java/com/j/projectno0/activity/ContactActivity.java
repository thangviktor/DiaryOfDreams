package com.j.projectno0.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;
import com.j.projectno0.R;
import com.j.projectno0.data.Database;

import org.jetbrains.annotations.NotNull;

public class ContactActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView tvDiariesNum = findViewById(R.id.tvDiariesNum);
        TextView tvAppInfor = findViewById(R.id.appInfor);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        Database database = new Database(this);
        navigationView.getMenu().findItem(R.id.menu_about).setChecked(true);
        tvDiariesNum.setText(getString(R.string.diaries_num, database.getDiaryCount()));
        tvAppInfor.setText(Html.fromHtml(getString(R.string.app_infor)));

        navigationView.setNavigationItemSelectedListener(onNavSelected());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        return super.onOptionsItemSelected(item);
    }

    /*************************************** Event Function ***************************************/
    private NavigationView.OnNavigationItemSelectedListener onNavSelected() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_diaries:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;
                    case R.id.menu_about:
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        finish();
                        break;
                }

                return false;
            }
        };
    }
}