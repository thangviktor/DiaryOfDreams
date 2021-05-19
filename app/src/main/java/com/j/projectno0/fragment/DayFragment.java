package com.j.projectno0.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.j.projectno0.adapter.DiaryAdapter;
import com.j.projectno0.R;
import com.j.projectno0.activity.MainActivity;
import com.j.projectno0.data.Database;
import com.j.projectno0.data.Diary;

public class DayFragment extends MainFragment {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);
        gridViewDiary = root.findViewById(R.id.gridViewDiary);
        addEmpty      = getActivity().findViewById(R.id.addEmpty);
        addNew        = getActivity().findViewById(R.id.addNew);
        searchView    = getActivity().findViewById(R.id.searchView);

        // Display diaries
        database = new Database(getContext());
        adapterDiary = new DiaryAdapter(getContext(), R.layout.item_diary, diaries);
        gridViewDiary.setAdapter(adapterDiary);
        addToDiaryList(database.getAllDiary());
        adapterDiary.notifyDataSetChanged();

        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    //*************************************** Event Click ******************************************
    public void loadSearchedList(String searchedText) {
        addToDiaryList(database.getAllDiary());
        if (diaries.size() > 0) {
            Log.d("SearchLog", "tab 1: searchedText = " + searchedText);
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
    }
}
