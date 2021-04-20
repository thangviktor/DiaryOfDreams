package com.j.projectno0.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.j.projectno0.R;

public class DayFragment extends BaseFragment{

    public DayFragment() {
    }

    public static DayFragment newInstance() {
        return new DayFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);
        gridViewDiary = root.findViewById(R.id.gridViewDiary);
        addNew        = getActivity().findViewById(R.id.addNew);
        addEmpty      = getActivity().findViewById(R.id.addEmpty);
        searchView    = getActivity().findViewById(R.id.searchView);

        return root;
    }

}
