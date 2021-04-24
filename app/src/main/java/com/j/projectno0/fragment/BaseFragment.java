package com.j.projectno0.fragment;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.j.projectno0.Adapter.DiaryAdapter;
import com.j.projectno0.R;
import com.j.projectno0.activity.EditActivity;
import com.j.projectno0.data.Database;
import com.j.projectno0.data.Diary;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class BaseFragment extends Fragment {

    protected int columnNum = 1;

    protected Database database;
    protected final ArrayList<Diary> diaries = new ArrayList<>();
    protected final List<Diary> searchedList = new ArrayList<>();
    protected DiaryAdapter adapterDiary;

    protected GridView gridViewDiary;
    protected ImageButton addNew, addEmpty;
    protected SearchView searchView;

    @Override
    public void onStart() {
        super.onStart();

        // Display diaries
        database = new Database(getContext());
        addToDiaryList(database.getAllDiary());
        adapterDiary = new DiaryAdapter(getContext(), R.layout.item_diary, diaries);
        gridViewDiary.setAdapter(adapterDiary);

        displayAddButton();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        gridViewDiary.setNumColumns(columnNum);

        // Click on a diary in list
        gridViewDiary.setOnItemClickListener(onDiaryItemClick());
        gridViewDiary.setOnItemLongClickListener(onDiaryItemLongClick());

        // Search
        searchView.setOnQueryTextListener(onSearchQueryText());
        searchView.setOnCloseListener(onSearchClose());
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_filter:
                showPopupFilter();
                break;
            case R.id.menu_bar_list:
                columnNum = 1;
                break;
            case R.id.menu_bar_grid:
                columnNum = 2;
                break;
        }
        gridViewDiary.setNumColumns(columnNum);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Objects.requireNonNull(getActivity())
                    .getSharedPreferences("DDPreferences", MODE_PRIVATE)
                    .edit()
                    .putInt("num_of_col", columnNum)
                    .apply();
        }

        return super.onOptionsItemSelected(item);
    }

    /*************************************** Event Click ******************************************/
    private AdapterView.OnItemLongClickListener onDiaryItemLongClick() {
        return new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                final Diary diary = diaries.get(position);
                AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setMessage(getString(R.string.delete) + " \"" + diary.getTitle() + "\" ?")
                        .setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteDiary(diary.getId());
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();

                return true;
            }
        };
    }

    private AdapterView.OnItemClickListener onDiaryItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Diary diary = diaries.get(position);
                Intent intent = new Intent(view.getContext(), EditActivity.class);
                intent.putExtra("diary", diary);
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

    /*************************************** Class Function ***************************************/
    private void showPopupFilter() {
        final View popupView = getActivity().findViewById(R.id.menu_filter);
        PopupMenu popupMenu = new PopupMenu(getContext(), popupView);
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

    protected void addToDiaryList(List<Diary> list) {
        diaries.clear();
        for (Diary diary : list)
            diaries.add(0, diary);
    }


    protected void deleteDiary(int id) {
        database.deleteDiary(id);
        addToDiaryList(database.getAllDiary());
        adapterDiary.notifyDataSetChanged();
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
}
