package com.example.coding_hackathon_part_2;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class Pager extends FragmentStatePagerAdapter {

    private Ongoing_Projects_List_Fragment tab0=null;
    private ProjectsFragment tab1 = null;
    private ComplaintsFragment tab2 = null;

    //integer to count number of tabs
    int tabCount;
//    private String[] tabTitles = new String[]{"Tab1", "Tab2", "Tab3"};

    //Constructor to the class
    public Pager(FragmentManager fm, int tabCount) {
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
                    tab0 = new Ongoing_Projects_List_Fragment();
                }
                return tab0;

            case 1:
                if(tab1 == null){
                    tab1 = new ProjectsFragment();
                }
                return tab1;

            case 2:
                if(tab2 == null){
                    tab2 = new ComplaintsFragment();
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
