package com.example.concertscollector;

import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.view.View;
import android.widget.ImageView;

public class Feed extends AppCompatActivity {

    ImageView btn_messages,btn_search;
    TabLayout tabLayout;
    int[] tabIcons={R.drawable.profile,R.drawable.home,R.drawable.plus,R.drawable.heart,R.drawable.search};
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        //Relaciones------------------------
        btn_messages = findViewById(R.id.btn_messages);
        btn_search = findViewById(R.id.btn_search);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        tabLayout = (TabLayout) findViewById(R.id.ly_tabs);

        //Filtro de color a botones
        /*
        btn_messages.setColorFilter(Color.parseColor("#E0E0E0"), PorterDuff.Mode.SRC_IN);
        btn_search.setColorFilter(Color.parseColor("#E0E0E0"), PorterDuff.Mode.SRC_IN);*/

        //Carga de ViewPager
        loadViewPager(tabLayout);

        //Carga de íconos
        tabIcons(tabLayout);
        initializeIconColors(tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               iconColor(tab,"#df78ef");
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                iconColor(tab,"#E0E0E0");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        btn_messages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a mensajes", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Ir a búsqueda", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void loadViewPager(TabLayout tablayout){
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Fragment_profile(),"");
        adapter.addFragment(new Fragment_home(),"");
        adapter.addFragment(new Fragment_add(),"");
        adapter.addFragment(new Fragment_activity(),"");
        adapter.addFragment(new Fragment_schedule(),"");
        viewPager.setAdapter(adapter);
        tablayout.setupWithViewPager(viewPager);
    }

    private void tabIcons(TabLayout tablayout){
        for(int i=0;i<5;i++){
            tablayout.getTabAt(i).setIcon(tabIcons[i]);
            iconColor(tablayout.getTabAt(i),"#E0E0E0");
        }
    }

    private void iconColor(TabLayout.Tab tab, String color){
        tab.getIcon().setColorFilter(Color.parseColor(color), PorterDuff.Mode.SRC_IN);
    }

    private void initializeIconColors(TabLayout tablayout){
        iconColor(tablayout.getTabAt(tabLayout.getSelectedTabPosition()),"#df78ef");
    }
}
