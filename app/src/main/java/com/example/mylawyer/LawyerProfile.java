package com.example.mylawyer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class LawyerProfile extends AppCompatActivity {
    PagerViewAdapter pagerViewAdapter;
    ViewPager viewPager;
    private TextView clients,staff,profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lawyer_profile);

        clients = (TextView)findViewById(R.id.clients_text);
        staff = (TextView)findViewById(R.id.staff_text);
        profile = (TextView)findViewById(R.id.profile_text);

        clients.setTextSize(23);

        viewPager = (ViewPager)findViewById(R.id.fragment_container);
        pagerViewAdapter = new PagerViewAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerViewAdapter);

        clients.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0);
            }
        });

        staff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1);
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(2);
            }
        });



        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

                onChangeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void onChangeTab(int position) {

        if(position == 0 )
        {
            clients.setTextSize(23);
            staff.setTextSize(13);
            profile.setTextSize(13);
        }

        if(position == 1 )
        {
            clients.setTextSize(13);
            staff.setTextSize(23);
            profile.setTextSize(13);

        }

        if (position==2)
        {
            clients.setTextSize(13);
            staff.setTextSize(13);
            profile.setTextSize(23);
        }


    }

}
