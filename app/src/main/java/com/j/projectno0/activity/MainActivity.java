package com.j.projectno0.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.j.projectno0.Adapter.ViewPagerMainAdapter;
import com.j.projectno0.R;
import com.j.projectno0.TextSearching;
import com.j.projectno0.utils.SettingsUtils;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MainActivity extends BaseActivity {
    private TextSearching.Day textSearchingDay;
    private TextSearching.Night textSearchingNight;
    private ViewPagerMainAdapter vpAdapter;
    private ViewPager2 vpMain;
    String searchedText = "";

    private DrawerLayout drawerLayout;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SettingsUtils.loadSettings(getResources());

        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar   = findViewById(R.id.toolbar);
        vpMain = findViewById(R.id.vpMain);
        ImageButton addNew = findViewById(R.id.addNew);
        ImageButton addEmpty = findViewById(R.id.addEmpty);
        final SearchView searchView = findViewById(R.id.searchView);

        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu);
        getSupportActionBar().setTitle(getString(R.string.app_name));

        navigationView.getMenu().findItem(R.id.menu_diaries).setChecked(true);

        vpAdapter = new ViewPagerMainAdapter(getSupportFragmentManager(), getLifecycle());
        vpMain.setAdapter(vpAdapter);

        TabLayout tabLayout = findViewById(R.id.tabMain);
        new TabLayoutMediator(tabLayout, vpMain, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                if (position == 0)
                    tab.setText(getString(R.string.day).toUpperCase());
                else tab.setText(getString(R.string.night).toUpperCase());
            }
        }).attach();

        addEmpty.setOnClickListener(onAddClick());
        addNew.setOnClickListener(onAddClick());
        searchView.setOnQueryTextListener(onQueryTextListener());
        vpMain.registerOnPageChangeCallback(onPageChange());

        navigationView.setNavigationItemSelectedListener(onNavSelected());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
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
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_about:
                        startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case R.id.menu_setting:
                        startActivity(new Intent(getApplicationContext(), SettingActivity.class));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
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

    private SearchView.OnQueryTextListener onQueryTextListener() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String text) {
                searchedText = text;
                if (vpMain.getCurrentItem() == 0) {
                    Log.d("SearchLog", "" + vpMain.getCurrentItem());
                    if (textSearchingDay != null) {
                        textSearchingDay.loadSearchedList(searchedText);
                    }
                }
                else if (textSearchingNight != null) {
                    textSearchingNight.loadSearchedList(searchedText);
                }
                return false;
            }
        };
    }

    private ViewPager2.OnPageChangeCallback onPageChange() {
        return new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    textSearchingDay.loadSearchedList(searchedText);
                else
                    textSearchingNight.loadSearchedList(searchedText);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        };
    }

    //************************************** Class Function ****************************************
    public void setTextSearchingDay(TextSearching.Day textSearchingDay) {
        this.textSearchingDay = textSearchingDay;
    }

    public void setTextSearchingNight(TextSearching.Night textSearchingNight) {
        this.textSearchingNight = textSearchingNight;
    }

}