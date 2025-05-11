package com.example.turfly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx. appcompat. widget. Toolbar;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import com.google.android.material.navigation.NavigationView;
import com.example.turfly.R;


public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView fitness, aiChat, turfBooking, help;
    private long backPressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        drawerLayout = findViewById(R.id.main);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        fitness = findViewById(R.id.fitnessImg);
        aiChat = findViewById(R.id.aiChatImg);
        turfBooking = findViewById(R.id.turfbookingImg);
        help = findViewById(R.id.helpImg);

        fitness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, FitnessActivity.class);
                startActivity(intent);
            }
        });

        aiChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, AiChatActivity.class);
                startActivity(intent);
            }
        });

        turfBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, TurfBookingActivity.class);
                startActivity(intent);
            }
        });

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, HelpActivity.class);
                startActivity(intent);
            }
        });

        setSupportActionBar(toolbar);
        Menu menu = navigationView.getMenu();
//        menu.findItem(R.id.nav_logout).setVisible(false);
//        menu.findItem(R.id.nav_profile).setVisible(false);
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_home);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            if (backPressedTime + 2000 > System.currentTimeMillis()) {
                super.onBackPressed(); // Exit the app
            } else {
                Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
                backPressedTime = System.currentTimeMillis(); // Update back press time
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        int itemId = menuItem.getItemId();

        if (itemId == R.id.nav_home){

        } else if (itemId == R.id.nav_turf) {
            Intent intent = new Intent(HomeActivity.this, TurfBookingActivity.class);
            startActivity(intent);
        } else if (itemId == R.id.nav_fitness) {
            Intent intent1 = new Intent(HomeActivity.this, FitnessActivity.class);
            startActivity(intent1);
        } else if (itemId == R.id.nav_aichat) {
            Intent intent2 = new Intent(HomeActivity.this, AiChatActivity.class);
            startActivity(intent2);
        } else if (itemId == R.id.nav_feedback) {
            Intent intent3 = new Intent(HomeActivity.this, FeedbackActivity.class);
            startActivity(intent3);
        } else if (itemId == R.id.nav_share) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String sharebody = "Try out, the best Turf booking and fitness app!!\n Free download now!\n" + "https://play.google.com/store/apps/details?id=com.example.turfly&hl=en";
            String sharehub = "Turfly";
            myIntent.putExtra(Intent.EXTRA_SUBJECT,sharehub);
            myIntent.putExtra(Intent.EXTRA_TEXT,sharebody);
            startActivity(Intent.createChooser(myIntent, "share using"));
            return true;
        } else if (itemId == R.id.nav_rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.example.turfly&hl=en"));
            startActivity(intent);
            return true;
        } else if (itemId == R.id.nav_logout) {
            Intent intent4 = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent4);
            Toast.makeText(HomeActivity.this, "Logged Out Successfully", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.nav_profile) {
            Intent intent5 = new Intent(HomeActivity.this, AdminLoginActivity.class);
            startActivity(intent5);
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}