package com.j.projectno0.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.j.projectno0.R;

public class NightFragment extends BaseFragment {

    public NightFragment() {
    }

    public static NightFragment newInstance() {
        return new NightFragment();
    }

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

        return root;
    }
}