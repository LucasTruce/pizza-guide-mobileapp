package com.app.pizza;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.app.pizza.fragments.HomeFragment;
import com.app.pizza.fragments.ChatFragment;
import com.google.android.material.navigation.NavigationView;

public class MenuActivity extends AppCompatActivity {

    Toolbar toolbar;
    private DrawerLayout drawerLayout;
    NavigationView navigationView;
    TextView navUsernameLabel;
    TextView navEmailLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //initialization
        drawerLayout = findViewById(R.id.drawer);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.main_toolbar);

        SharedPreferences sharedPref = getSharedPreferences("pref", 0);

        //setting toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pizza Guide");

        //setting user email and username on navigation
        View headerView = navigationView.getHeaderView(0);
        navEmailLabel = headerView.findViewById(R.id.nav_email_label);
        navUsernameLabel = headerView.findViewById(R.id.nav_username_label);
        navEmailLabel.setText(sharedPref.getString("email", ""));
        navUsernameLabel.setText(sharedPref.getString("username", ""));

        //create actionBarDrawerToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.openNavDrawer,
                R.string.closeNavDrawer
        );

        //add drawer and load HomeFragment as home page
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        loadFragment(new HomeFragment());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id=menuItem.getItemId();
                Fragment fragment;
                switch (id)
                {
                    case R.id.home:
                        fragment=new HomeFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.chat:
                        fragment=new ChatFragment();
                        loadFragment(fragment);
                        break;
                    case R.id.logout:
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });
    }
    private void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment).commit();
        drawerLayout.closeDrawer(GravityCompat.START);
        fragmentTransaction.addToBackStack(null);
    }

}
