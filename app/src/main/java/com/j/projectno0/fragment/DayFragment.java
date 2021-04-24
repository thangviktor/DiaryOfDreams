package com.j.projectno0.fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.j.projectno0.R;

import java.util.Objects;

public class DayFragment extends BaseFragment {

    public DayFragment() {
    }

    public static DayFragment newInstance(int columnNum) {
        Bundle args = new Bundle();
        args.putInt("columnNum", columnNum);
        DayFragment f = new DayFragment();
        f.setArguments(args);
        return f;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_day, container, false);
        gridViewDiary = root.findViewById(R.id.gridViewDiary);
        addEmpty      = getActivity().findViewById(R.id.addEmpty);
        addNew        = getActivity().findViewById(R.id.addNew);
        searchView    = getActivity().findViewById(R.id.searchView);

        if (getArguments() != null) {
            columnNum = getArguments().getInt("columnNum");
        }
        return root;
    }

}
