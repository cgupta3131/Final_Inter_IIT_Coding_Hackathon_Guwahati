package com.example.coding_hackaton_guwahati;

import com.google.firebase.firestore.auth.User;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class UserPager extends FragmentStatePagerAdapter {

    private maintenance_request tab0 = null;
    private UserSurveyList tab1 = null;
    private user_profile tab2 = null;

    //integer to count number of tabs
    int tabCount;
//    private String[] tabTitles = new String[]{"Tab1", "Tab2", "Tab3"};

    //Constructor to the class
    public UserPager(FragmentManager fm, int tabCount) {
        super(fm);
        this.tabCount = tabCount;
    }

    //Overriding method getItem
    @Override
    public Fragment getItem(int position) {
        //Returning the current tabs
        switch (position) {
            case 0:
                if(tab0 == null){
                    tab0 = new maintenance_request();
                }
                return tab0;
            case 1:
                if(tab1 == null){
                    tab1 = new UserSurveyList();
                }
                return tab1;
            case 2:
                if(tab2 == null){
                    tab2 = new user_profile();
                }
                return tab2;

            default:
                return null;
        }
    }

    //Overriden method getCount to get the number of tabs
    @Override
    public int getCount() {
        return tabCount;
    }

}
