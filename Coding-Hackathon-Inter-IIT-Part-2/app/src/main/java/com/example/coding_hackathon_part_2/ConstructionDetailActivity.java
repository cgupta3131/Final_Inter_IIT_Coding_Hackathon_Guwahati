package com.example.coding_hackathon_part_2;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

public class ConstructionDetailActivity extends AppCompatActivity {

    Toolbar mTopToolbar;
    int numTabs = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction_detail);

        mTopToolbar = findViewById(R.id.my_toolbar);
        mTopToolbar.setTitle("Roadseva");
        setSupportActionBar(mTopToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        ProjectId.projectId = getIntent().getStringExtra("projectId");
        Log.d("myProject", ProjectId.projectId);

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        for(int i = 0; i < numTabs; i++){
            tabLayout.addTab(tabLayout.newTab());
        }


        //Initializing viewPager
        //This is our viewPager
        ViewPager viewPager = findViewById(R.id.pager);

        //Creating our pager adapter
        ConstructionDetailPager adapter = new ConstructionDetailPager(getSupportFragmentManager(), tabLayout.getTabCount());

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);


        tabLayout.getTabAt(0).setIcon(R.drawable.ic_transfer_within_a_station_black_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_people_black_24dp);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) tab.setCustomView(R.layout.home_tab);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_assign_contractor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                this.finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
