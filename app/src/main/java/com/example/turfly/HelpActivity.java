package com.example.turfly;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends AppCompatActivity {

    private ViewPager2 viewPager;
    private HelpPagerAdapter adapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        // Initialize ViewPager and TabLayout
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        // List of Help Pages
        List<HelpPage> helpPages = new ArrayList<>();
        helpPages.add(new HelpPage("Home", "Explore available turfs & book your slot.", R.drawable.home_icon));
        helpPages.add(new HelpPage("Booking", "Fill details & estimate costs before payment.", R.drawable.icon_booking));
        helpPages.add(new HelpPage("Payments", "Secure payments using Razorpay.", R.drawable.ic_payment));
        helpPages.add(new HelpPage("Admin", "Admins can add and remove turfs.", R.drawable.ic_admin));
        helpPages.add(new HelpPage("Support", "Contact us via in-app support.", R.drawable.icon_help));

        // Set Adapter
        adapter = new HelpPagerAdapter(helpPages);
        viewPager.setAdapter(adapter);

        // Attach ViewPager to TabLayout
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
            tab.setText(helpPages.get(position).getTitle());
            tab.setContentDescription(helpPages.get(position).getTitle() + " Tab"); // Accessibility Fix ✅
        }).attach();
    }
}
