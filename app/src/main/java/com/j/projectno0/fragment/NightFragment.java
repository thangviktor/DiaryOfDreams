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

public class NightFragment extends MainFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_night, container, false);
        gridViewDiary = root.findViewById(R.id.gridViewDiary);
        addNew        = getActivity().findViewById(R.id.addNew);
        addEmpty      = getActivity().findViewById(R.id.addEmpty);
        searchView    = getActivity().findViewById(R.id.searchView);

        Log.d("LifecycleLog", "onCreateView");

        // Display diaries
        database = new Database(getContext());
        adapterDiary = new DiaryAdapter(getContext(), R.layout.item_diary, diaries);
        gridViewDiary.setAdapter(adapterDiary);
        addToDiaryList(database.getAllDiary());
        adapterDiary.notifyDataSetChanged();

        return root;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    //*************************************** Event Click ******************************************
    public void loadSearchedList(String searchedText) {
        addToDiaryList(database.getAllDiary());
        if (diaries.size() > 0) {
            Log.d("SearchLog", "tab 2: searchedText = " + searchedText);
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