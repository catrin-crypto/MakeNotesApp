package com.example.makenotesapp;

import android.provider.Contacts;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.makenotesapp.ui.ListFragment;

public class Navigation {
    private final FragmentManager fragmentManager;

    public Navigation(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public int getBackStackEntryCount() {
        return fragmentManager.getBackStackEntryCount();
    }

    public com.example.makenotesapp.ui.ListFragment getListFragment(){ return (ListFragment) fragmentManager.findFragmentByTag(ListFragment.TAG);}

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
