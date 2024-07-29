package com.example.nasaimageviewer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
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

import com.example.nasaimageviewer.model.NasaImage;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

/**
 * This class/activity is used for displaying all of the persisted NasaImage records from a SQLiteDatabase.
 * @author Eric Mignardi
 * @version 1.0
 */
public class ImagesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
     * The ArrayList used to hold NasaImage objects
     */
    private ArrayList<NasaImage> nasaImages = new ArrayList<>();

    /**
     * This method is called when an activity is first launched and is responsible for creating.
     * A ListView containing all of the records in the database is displayed.
     * The data is populated from a SQLiteDatabase and using an adapter class {@link CustomAdapter} and method {@link CustomAdapter#getData()}.
     * A Bundle and Intent are paired together to pass data off to another activity when a ListView item is clicked.
     * The data passed is the data, hdurl, and url of the clicked item.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_images);
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
        ListView listView = findViewById(R.id.listView);
        CustomAdapter adapter = new CustomAdapter();
        adapter.getData();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener((list, item, position, id) -> {
            Bundle data = new Bundle();
            data.putString("date", nasaImages.get(position).getDate());
            data.putString("hdurl", nasaImages.get(position).getHdurl());
            data.putString("url", nasaImages.get(position).getUrl());
            Intent nextActivity = new Intent(ImagesActivity.this, EmptyActivity.class);
            nextActivity.putExtras(data);
            startActivity(nextActivity);
        });
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
            builder.setTitle(R.string.imagesHelpTitle).setMessage(R.string.imagesHelpMessage)
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
            builder.setTitle(R.string.imagesHelpTitle).setMessage(R.string.imagesHelpMessage)
                    .setCancelable(true).create().show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This private inner-class is used as a custom adapter for a ListView and extends the {@link BaseAdapter} abstract class.
     * @author Eric Mignardi
     * @version 1.0
     */
    private class CustomAdapter extends BaseAdapter {

        /**
         * This method is used to populate a ListView and utilizes a custom {@link DatabaseHelper} class.
         * A writeable SQLiteDatabase object is obtained to perform queries.
         * A Cursor stores the query results (similar to {@link java.sql.ResultSet}) and allows for iteration through them.
         * Each record is mapped to a NasaImage object and added to the list.
         */
        public void getData(){
            DatabaseHelper databaseHelper = new DatabaseHelper(ImagesActivity.this);
            SQLiteDatabase db = databaseHelper.getWritableDatabase();
            String[] columns = {DatabaseHelper.COL_NAME_2, DatabaseHelper.COL_NAME_3, DatabaseHelper.COL_NAME_4};
            Cursor cursor = db.query(DatabaseHelper.TABLE_NAME, columns, null, null, null, null, null);
            while ((cursor.moveToNext())){
                NasaImage nasaImage = new NasaImage(cursor.getString(0), cursor.getString(1), cursor.getString(2));
                nasaImages.add(nasaImage);
            }
        }

        /**
         * This method returns the size of the nasaImages ArrayList.
         * @return The size of the ArrayList
         */
        @Override
        public int getCount() {
            return nasaImages.size();
        }

        /**
         * This method is used in the getView method to retrieve a single item from a list.
         * @param position Position of the item whose data we want within the adapter's
         * data set.
         * @return The object in the array at the specified position
         */
        @Override
        public Object getItem(int position) {
            return nasaImages.get(position);
        }

        /**
         * This method is used to retrieve the ID of an item.
         * @param position The position of the item within the adapter's data set whose row id we want.
         * @return The ID of the item and a specified position
         */
        @Override
        public long getItemId(int position) {
            return nasaImages.get(position).getId();
        }

        /**
         * This method specifies how each row in the list should look.
         * When a row is scrolled off the screen, memory can be reused for a new row coming onto the screen.
         * @param position The position of the item within the adapter's data set of the item whose view
         *        we want.
         * @param convertView The old view to reuse, if possible. Note: You should check that this view
         *        is non-null and of an appropriate type before using. If it is not possible to convert
         *        this view to display the correct data, this method can create a new view.
         *        Heterogeneous lists can specify their number of view types, so that this View is
         *        always of the right type (see {@link #getViewTypeCount()} and
         *        {@link #getItemViewType(int)}).
         * @param parent The parent that this view will eventually be attached to
         * @return The newly inflated view for a list item
         */
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View newView = convertView;
            LayoutInflater inflater = getLayoutInflater();
            if (newView == null){
                newView = inflater.inflate(R.layout.row_layout, parent, false);
            }
            TextView dateTextView = newView.findViewById(R.id.dateTextView2);
            dateTextView.setText(getItem(position).toString());
            return newView;
        }
    }

}