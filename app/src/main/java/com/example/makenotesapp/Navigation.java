package com.example.makenotesapp;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class Navigation {
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public int getBackStackEntryCount() {
        return fragmentManager.getBackStackEntryCount();
    }

    public void popBackStack() {
        fragmentManager.popBackStack();
    }

    public void addFragmentToFirstFrame(Fragment fragment, boolean useBackStack, String tag, boolean isReplacing) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (isReplacing)
            fragmentTransaction = fragmentTransaction.replace(R.id.first_frame_layout, fragment, tag);
        else
            fragmentTransaction = fragmentTransaction.add(R.id.first_frame_layout, fragment, tag);
        if (useBackStack) {
            fragmentTransaction = fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.commit();
        int entryCount = fragmentManager.getBackStackEntryCount();
        Log.d("Navigation", "entryCount = " + entryCount);
    }
}
