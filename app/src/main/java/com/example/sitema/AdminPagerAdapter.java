package com.example.sitema;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AdminPagerAdapter extends FragmentStateAdapter {

    public AdminPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new AdminAlumnosFragment();
            case 1:
                return new AdminDocentesFragment();
            case 2:
                return new AdminMateriasFragment();
            default:
                return new AdminAlumnosFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
