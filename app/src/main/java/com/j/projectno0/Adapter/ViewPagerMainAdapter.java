package com.j.projectno0.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.j.projectno0.fragment.DayFragment;
import com.j.projectno0.fragment.NightFragment;

import java.util.ArrayList;

public class ViewPagerMainAdapter extends FragmentStateAdapter {
    private static final ArrayList<Fragment> fragments = new ArrayList<>();

    static {
        fragments.add(DayFragment.newInstance());
        fragments.add(NightFragment.newInstance());
    }

    public ViewPagerMainAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
