package com.example.nasaimageviewer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

/**
 * This class/activity is used for representing the About page.
 * @author Eric Mignardi
 * @version 1.0
 */
public class AboutActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    /**
     * The Toolbar used to display menu items
     */
    private Toolbar toolbar;
    /**
     * The ActionBarDrawerToggle used to enable opening of the NavigationView through click
     */
    private ActionBarDrawerToggle toggle;
    /**
     * The NavigationView used to display menu items and allow seamless transition between activities
     */
    private NavigationView navigationView;
    /**
     * The DrawerLayout used to hold the main page content as well as the NavigationView
     */
    private DrawerLayout drawerLayout;

    /**
     * This method is called when an activity is first launched and is responsible for creating.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawerLayout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        TextView textView = findViewById(R.id.aboutTextView);
    }

    /**
     * This method inflates the menu from a XML layout file.
     * @param menu The options menu in which you place your items.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_body, menu);
        return true;
    }

    /**
     * This method is called when a Toolbar item is clicked and contains logic for navigation across the applications' activities.
     * An unique AlertDialog providing a description of the current activity will be displayed if the Help button is selected.
     * @param item The menu item that was selected.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.home){
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.images){
            intent = new Intent(getApplicationContext(), ImagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.aboutHelpTitle).setMessage(R.string.aboutHelpMessage)
                    .setCancelable(true).create().show();
        }
        return true;
    }

    /**
     * This method is called when a NavigationView item is clicked and contains logic for navigation across the applications' activities.
     * An unique AlertDialog providing a description of the current activity will be displayed if the Help button is selected.
     * @param item The selected item
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        if (id == R.id.home){
            intent = new Intent(getApplicationContext(), HomeActivity.class);
            startActivity(intent);
        } else if (id == R.id.search) {
            intent = new Intent(getApplicationContext(), SearchActivity.class);
            startActivity(intent);
        } else if (id == R.id.images){
            intent = new Intent(getApplicationContext(), ImagesActivity.class);
            startActivity(intent);
        } else if (id == R.id.about) {
            intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent);
        } else if (id == R.id.help) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.aboutHelpTitle).setMessage(R.string.aboutHelpMessage)
                    .setCancelable(true).create().show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

}