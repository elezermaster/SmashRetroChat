package io.emaster.smashretrochat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import io.emaster.smashretrochat.fragment.ShowDataAllUsersFragment;
import io.emaster.smashretrochat.fragment.ShowDataFAvoriteUsersFragment;
import io.emaster.smashretrochat.fragment.ShowDataNewUsersFragment;

/**
 * Created by elezermaster on 31/08/2017.
 */


public class TabsPagerAdapter extends FragmentPagerAdapter{
    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ShowDataAllUsersFragment showDataAllUsersFragment = new ShowDataAllUsersFragment();
                return showDataAllUsersFragment;
            case 1:
                ShowDataFAvoriteUsersFragment showDataFAvoriteUsersFragment = new ShowDataFAvoriteUsersFragment();
                return showDataFAvoriteUsersFragment;
            case 2:
                ShowDataNewUsersFragment showDataNewUsersFragment = new ShowDataNewUsersFragment();
                return showDataNewUsersFragment;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 3;
    }

    public CharSequence getPageTitle(int position){
        switch(position){
            case 0:
                return "All Users";
            case 1:
                return "Favorite";
            case 2:
                return "New";
            default:
                return "";
        }

    }
}
