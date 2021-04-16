package com.j.projectno0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    private ActionBarDrawerToggle drawerToggle;

    final int EDIT_FROM_LIST = 0;
    int columnNum;
    SharedPreferences sharedPreferences;

    GridView gridViewDiary;
    ImageButton addNew, addEmpty;
    SearchView searchView;
    LinearLayout about;

    Database database;
    ArrayList<Diary> diaries = new ArrayList<>();
    List<Diary> searchedList = new ArrayList<>();
    DiaryAdapter adapterDiary;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        gridViewDiary   = findViewById(R.id.gridViewDiary);
        addNew          = findViewById(R.id.addNew);
        addEmpty        = findViewById(R.id.addEmpty);
        searchView      = findViewById(R.id.searchView);
        about           = findViewById(R.id.about);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        // Display diaries
        database = new Database(this);
        addToDiaryList(database.getAllDiary());
        adapterDiary = new DiaryAdapter(this, R.layout.item_diary, diaries);
        gridViewDiary.setAdapter(adapterDiary);

        sharedPreferences = getSharedPreferences("view_type", MODE_PRIVATE);
        columnNum = sharedPreferences.getInt("num_of_col", 1);

        displayAddButton();

        // Click on a diary in list
        gridViewDiary.setOnItemClickListener(onDiaryItemClick());
        gridViewDiary.setOnItemLongClickListener(onDiaryItemLongClick());
        // Add a new diary
        addEmpty.setOnClickListener(onAddEmptyClick());
        addNew.setOnClickListener(onAddNewClick());
        // Search
        searchView.setOnQueryTextListener(onSearchQueryText());
        searchView.setOnCloseListener(onSearchClose());
        // Open About
        about.setOnClickListener(onAboutClick());
    }

    @Override
    protected void onStart() {
        gridViewDiary.setNumColumns(columnNum);
        super.onStart();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        switch (item.getItemId()) {
            case R.id.filter:
                showPopupFilter();
                break;
            case R.id.bar_list:
                columnNum = 1;
                onStart();
                break;
            case R.id.bar_grid:
                columnNum = 2;
                onStart();
                break;
        }
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("num_of_col", columnNum);
        editor.apply();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == EDIT_FROM_LIST && resultCode == RESULT_OK) {
                addEmpty.setVisibility(View.INVISIBLE);
                addNew.setVisibility(View.VISIBLE);
                diaries.clear();
                addToDiaryList(database.getAllDiary());
                adapterDiary.notifyDataSetChanged();
            }
    }

    /*************************************** Event Click ******************************************/
    private View.OnClickListener onAboutClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ContactActivity.class);
                intent.putExtra("NumOfDiary", database.getDiaryCount());
                startActivity(intent);
            }
        };
    }

    private SearchView.OnQueryTextListener onSearchQueryText() {
        return new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchedText) {
                diaries.clear();
                addToDiaryList(database.getAllDiary());
                if (diaries.size() > 0) {
                    searchedList.clear();
                    for (Diary diary : diaries) {
                        if (diary.getTitle().contains(searchedText)
                                || diary.getContent().contains(searchedText)
                                || diary.getDate().contains(searchedText)) {
                            searchedList.add(diary);
                        }
                    }
                    diaries.clear();
                    addToDiaryList(searchedList);
                    adapterDiary.loadList(searchedText);
                }
                return false;
            }
        };
    }

    private SearchView.OnCloseListener onSearchClose() {
        return new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapterDiary.loadAnimation();
                return false;
            }
        };
    }

    private View.OnClickListener onAddNewClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivityForResult(intent, EDIT_FROM_LIST);
            }
        };
    }

    private View.OnClickListener onAddEmptyClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditActivity.class);
                startActivityForResult(intent, EDIT_FROM_LIST);
            }
        };
    }

    private AdapterView.OnItemLongClickListener onDiaryItemLongClick() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Diary diary = diaries.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(getString(R.string.delete) + "\"" + diary.getTitle() + "\" ?");
                builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteDiary(diary.getId());
                    }
                });
                builder.setNegativeButton(R.string.cancel, null);
                builder.show();

                return true;
            }
        };
    }

    private AdapterView.OnItemClickListener onDiaryItemClick()  {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diary diary = diaries.get(position);
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("diary", diary);
                startActivityForResult(intent, EDIT_FROM_LIST);
            }
        };
    }

    /*************************************** Class Function ***************************************/
    private void showPopupFilter() {
        final View popupView = findViewById(R.id.filter);
        resetFocus(popupView);
        PopupMenu popupMenu = new PopupMenu(this, popupView);
        popupMenu.inflate(R.menu.menu_filter);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.clearAll) {
                    database.deleteAllDiary();
                    diaries.clear();
                    adapterDiary.notifyDataSetChanged();
                    displayAddButton();
                }
                return false;
            }
        });
    }

    private void displayAddButton() {
        if (diaries.isEmpty()) {
            addEmpty.setVisibility(View.VISIBLE);
            addNew.setVisibility(View.INVISIBLE);
        } else {
            addEmpty.setVisibility(View.INVISIBLE);
            addNew.setVisibility(View.VISIBLE);
        }
    }

    private void deleteDiary(int id) {
        database.deleteDiary(id);
        diaries.clear();
        addToDiaryList(database.getAllDiary());
        adapterDiary.notifyDataSetChanged();
        displayAddButton();
    }

    public void addToDiaryList(List<Diary> list) {
        for (Diary diary : list)
        diaries.add(0, diary);
    }

    public void resetFocus(View view) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}