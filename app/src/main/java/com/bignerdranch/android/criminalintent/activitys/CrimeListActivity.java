package com.bignerdranch.android.criminalintent.activitys;

import android.support.v4.app.Fragment;

import com.bignerdranch.android.criminalintent.Fragments.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }
}
