package com.example.coding_hackathon_part_2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class ConstructionDetailPager extends FragmentStatePagerAdapter {
    private ContractorConstructionFragment tab0 = null;
    private UserConstructionFragment tab1 = null;

    //integer to count number of tabs
    int tabCount;
//    private String[] tabTitles = new String[]{"Tab1", "Tab2", "Tab3"};

    //Constructor to the class
    public ConstructionDetailPager(FragmentManager fm, int tabCount) {
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
                    tab0 = new ContractorConstructionFragment();
                }
                return tab0;

            case 1:
                if(tab1 == null){
                    tab1 = new UserConstructionFragment();
                }
                return tab1;

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
