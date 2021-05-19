package com.j.projectno0.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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

import com.j.projectno0.R;
import com.j.projectno0.activity.EditActivity;
import com.j.projectno0.adapter.DiaryAdapter;
import com.j.projectno0.data.Database;
import com.j.projectno0.data.Diary;
import com.j.projectno0.utils.TimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;


public class MainFragment extends Fragment {
    protected int columnNum = 0;

    protected Database database;
    protected final ArrayList<Diary> diaries = new ArrayList<>();
    protected final List<Diary> searchedList = new ArrayList<>();
    protected DiaryAdapter adapterDiary;

    protected GridView gridViewDiary;
    protected ImageButton addNew, addEmpty;
    protected SearchView searchView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DDPreferences", MODE_PRIVATE);
        columnNum = sharedPreferences.getInt("num_of_col", 1);

        displayAddButton();

        gridViewDiary.setNumColumns(columnNum);

        // Click on a diary in list
        gridViewDiary.setOnItemClickListener(onDiaryItemClick());
        gridViewDiary.setOnItemLongClickListener(onDiaryItemLongClick());


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
        return (parent, view, position, id) -> {

            final Diary diary = diaries.get(position);
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setMessage(getString(R.string.delete) + " \"" + diary.getTitle() + "\" ?")
                    .setPositiveButton(R.string.delete, (dialog, which) -> deleteDiary(diary.getId()))
                    .setNegativeButton(android.R.string.cancel, null)
                    .show();

            return true;
        };
    }

    private AdapterView.OnItemClickListener onDiaryItemClick() {
        return (parent, view, position, id) -> {
            Diary diary = diaries.get(position);
            Intent intent = new Intent(view.getContext(), EditActivity.class);
            intent.putExtra("diary", diary);
            startActivity(intent);
        };
    }

    /*************************************** Class Function ***************************************/
    @SuppressLint("NonConstantResourceId")
    private void showPopupFilter() {
        final View popupView = getActivity().findViewById(R.id.menu_filter);
        PopupMenu popupMenu = new PopupMenu(getContext(), popupView);
        popupMenu.inflate(R.menu.menu_filter);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.clearList:
                    database.deleteAllDiary();
                    diaries.clear();
                    adapterDiary.notifyDataSetChanged();
                    displayAddButton();
                    break;
                case R.id.sortDate:
                    sortByDate();
                    break;
            }
            return false;
        });
    }

    private void sortByDate() {
        List<Diary> rawList = database.getAllDiary();
        for (int i = 0; i < rawList.size()-1; i++) {
            long max = TimeUtils.getTimeMillis(rawList.get(i).getDate());
            for (int j = i+1; j < rawList.size(); j++)
                if (TimeUtils.getTimeMillis(rawList.get(j).getDate()) > max) {
                    max = TimeUtils.getTimeMillis(rawList.get(j).getDate());
                    Diary diary = rawList.get(j);
                    rawList.set(j, rawList.get(i));
                    rawList.set(i, diary);
                }
        }
        for (Diary diary : rawList)
            Log.d("SORTLOG", diary.getTitle() + ", date: " + diary.getDate());
        addToDiaryList(rawList);
        adapterDiary.notifyDataSetChanged();
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
