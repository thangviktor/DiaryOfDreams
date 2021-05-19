package com.j.projectno0.adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.j.projectno0.fragment.DayFragment;
import com.j.projectno0.fragment.NightFragment;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerMainAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = new ArrayList<>();



    public ViewPagerMainAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Log.d("SearchLog", "position = " + position);
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }

    public void addFragment(Fragment fragment) {
        fragments.add(fragment);
    }
}
