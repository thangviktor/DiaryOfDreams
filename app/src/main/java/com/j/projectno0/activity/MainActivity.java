package com.j.projectno0.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.j.projectno0.Adapter.ViewPagerMainAdapter;
import com.j.projectno0.R;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    ImageButton addNew, addEmpty;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar   = findViewById(R.id.toolbar);
        ViewPager2 vpMain = findViewById(R.id.vpMain);
        addNew      = findViewById(R.id.addNew);
        addEmpty    = findViewById(R.id.addEmpty);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);

        navigationView.getMenu().findItem(R.id.menu_diaries).setChecked(true);
        ViewPagerMainAdapter vpAdapter = new ViewPagerMainAdapter(getSupportFragmentManager(), getLifecycle());
        vpMain.setAdapter(vpAdapter);

        TabLayout tabLayout = findViewById(R.id.tabMain);
        new TabLayoutMediator(tabLayout, vpMain, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 1)
                    tab.setText("NIGHT");
                else tab.setText("DAY");
            }
        }).attach();

        // Add a new diary
        addEmpty.setOnClickListener(onAddClick());
        addNew.setOnClickListener(onAddClick());

        // Open Drawer Layout
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
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.menu_about) {
                    startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                    finish();
                }

                return false;
            }
        };
    }

    private View.OnClickListener onAddClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivity(intent);
            }
        };
    }

}