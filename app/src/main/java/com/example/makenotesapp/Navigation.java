package com.example.makenotesapp;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void addFragmentToFirstFrame(Fragment fragment, boolean useBackStack, String tag) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.first_frame_layout, fragment, tag);
            if (useBackStack) {
                fragmentTransaction.addToBackStack(null);
            }
            fragmentTransaction.commit();
    }
}
