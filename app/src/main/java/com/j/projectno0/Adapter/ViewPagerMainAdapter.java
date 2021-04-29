package com.j.projectno0.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.j.projectno0.fragment.DayFragment;
import com.j.projectno0.fragment.NightFragment;

import java.util.ArrayList;

public class ViewPagerMainAdapter extends FragmentStateAdapter {

    public ViewPagerMainAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("SearchLog", "position = " + position);
        if (position == 0) {
            return DayFragment.newInstance();
        } else {
            return NightFragment.newInstance();
        }
    }



    @Override
    public int getItemCount() {
        return 2;
    }
}
