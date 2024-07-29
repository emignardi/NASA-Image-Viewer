package com.example.nasaimageviewer;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.time.LocalDate;

import javax.net.ssl.HttpsURLConnection;

/**
 * This class/activity is used to search for an image from NASA's API using an interactive calendar.
 * The details of the image will be displayed alongside the image itself.
 * A user can choose to save the image to the database stored on the device for later viewing.
 * @author Eric Mignardi
 * @version 1.0
 */
public class SearchActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

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
     * The ProgressBar used to display progress of AsyncTask
     */
    private ProgressBar progressBar;
    /**
     * The DatePicker used to select a date for image retrieval
     */
    private DatePicker datePicker;
    /**
     * The TextView used to display the NasaImage date
     */
    private TextView dateTextView;
    /**
     * The TextView used to display the NasaImage hdurl
     */
    private TextView hdUrlTextView;
    /**
     * The TextView used to display the NasaImage url
     */
    private TextView urlTextView;
    /**
     * The Button used to insert data into a database
     */
    private Button button;
    /**
     * The ImageView used to display the fetched NasaImage
     */
    private ImageView imageView;
    /**
     * The DrawerLayout used to hold the main page content as well as the NavigationView
     */
    private DrawerLayout drawerLayout;
    /**
     * The temporary URL of the NASA API without the ending date string
     */
    private String apiTempUrl = "https://api.nasa.gov/planetary/apod?api_key=JFd8aorFfdUkKXmQCfcA17agxH4zjluVrnDRunlT&date=";
    /**
     * The URL used with the parameters of the AsyncTask
     */
    private URL url;
    /**
     * The HttpURLConnection used to open a connection and to retrieve an InputStream
     */
    private HttpsURLConnection connection;
    /**
     * The InputStream containing the data from the NASA API
     */
    private InputStream inputStream;
    /**
     * The BufferedReader to read the InputStream data
     */
    private BufferedReader bufferedReader;
    /**
     * The StringBuilder used to append and build a String from the BufferedReader data
     */
    private StringBuilder stringBuilder;
    /**
     * The object used to store a result from the API
     */
    private JSONObject jsonObject;
    /**
     * The NasaImage object used for mapping JSONObject values
     */
    private NasaImage nasaImage;
    /**
     * The URL used to retrieve the image
     */
    private URL url1;
    /**
     * The InputStream used with the Bitmap.Factory to obtain an image
     */
    private InputStream inputStream1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.searchLayout), (v, insets) -> {
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
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        datePicker = findViewById(R.id.datePicker);
        LocalDate localDate = LocalDate.now();
        datePicker.setOnDateChangedListener((view, year, month, day) -> {
            progressBar.setVisibility(View.VISIBLE);
            String apiYear = String.valueOf(datePicker.getYear());
            String apiMonth = String.valueOf(datePicker.getMonth() + 1);
            String apiDay = String.valueOf(datePicker.getDayOfMonth());
            int apiYearInt = Integer.parseInt(apiYear);
            int apiMonthInt = Integer.parseInt(apiMonth);
            int apiDayInt = Integer.parseInt(apiDay);
            if (apiYearInt <= localDate.getYear() && apiMonthInt <= localDate.getMonth().getValue() && apiDayInt <= localDate.getDayOfMonth()){
                new Task().execute(apiTempUrl + apiYear + "-" + apiMonth + "-"+ apiDay);
            }
            else {
                Toast.makeText(SearchActivity.this, "Invalid date", Toast.LENGTH_LONG).show();
            }
        });
        dateTextView = findViewById(R.id.dateTextView);
        hdUrlTextView = findViewById(R.id.hdurlTextView);
        urlTextView = findViewById(R.id.urlTextView);
        button = findViewById(R.id.button);
        button.setOnClickListener((click) -> {
            if (nasaImage.getDate() != null && nasaImage.getHdurl() != null && nasaImage.getUrl() != null){
                DatabaseHelper databaseHelper = new DatabaseHelper(SearchActivity.this);
                SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
                ContentValues values = new ContentValues();
                values.put("date", nasaImage.getDate());
                values.put("hdurl", nasaImage.getHdurl());
                values.put("url", nasaImage.getUrl());
                long count = sqLiteDatabase.insert(DatabaseHelper.TABLE_NAME, null, values);
                if (count > 0){
                    Snackbar.make(findViewById(R.id.searchLayout), R.string.insertSuccess, Snackbar.LENGTH_SHORT).show();
                }
                else {
                    Snackbar.make(findViewById(R.id.searchLayout), R.string.insertFailure, Snackbar.LENGTH_SHORT).show();
                }
                sqLiteDatabase.close();
            }
        });
        imageView = findViewById(R.id.imageView);
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
            builder.setTitle(R.string.searchHelpTitle).setMessage(R.string.searchHelpMessage)
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
            builder.setTitle(R.string.searchHelpTitle).setMessage(R.string.searchHelpMessage)
                    .setCancelable(true).create().show();
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * This private inner-class is used for working on a long-running task in order prevent the UI thread from hanging.
     * @author Eric Mignardi
     * @version 1.0
     */
    private class Task extends AsyncTask <String, Void, Bitmap> {

        private Bitmap bitmap;

        /**
         * This method performs its operations on a separate thread from the main one.
         * A connection is established with NASA's server and data is read through a BufferedReader and InputStream.
         * From this, a JSONObject and NasaImage object are created.
         * The URL from the JSONObject is used to with a BitmapFactory to create a Bitmap image.
         * Upon completion of the doInBackground method, the ImageView will be set to display that image if the Bitmap passed to the onPostExecute method is not null.
         * @param voids The parameters of the task.
         * @return The Bitmap NASA image
         */
        @Override
        protected Bitmap doInBackground(String... voids) {
            try {
                Log.i("DO_IN_BACKGROUND", Thread.currentThread().getName());
                url = new URL(voids[0]);
                connection = (HttpsURLConnection) url.openConnection();
                connection.connect();
                inputStream = connection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null){
                    stringBuilder.append(line);
                }
                bufferedReader.close();
                connection.disconnect();
                String result = stringBuilder.toString();
                jsonObject = new JSONObject(result);
                String date = jsonObject.getString("date");
                String hdurl = jsonObject.getString("hdurl");
                String url = jsonObject.getString("url");
                nasaImage = new NasaImage(date, hdurl, url);
                url1 = new URL(url);
                inputStream1 = url1.openStream();
                bitmap = BitmapFactory.decodeStream(inputStream1);
            } catch (IOException | JSONException e) {
                throw new RuntimeException(e);
            }
            return bitmap;
        }

        /**
         * This method is called in the main thread when {@link #doInBackground} calls {@link #publishProgress}.
         * @param values The values indicating progress.
         */
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        /**
         * This method is called when {@link #doInBackground} is complete.
         * @param bitmap The result of the operation computed by {@link #doInBackground}.
         */
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null){
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setImageBitmap(bitmap);
                dateTextView.setText("Date: " + nasaImage.getDate());
                hdUrlTextView.setText("HDURL: " + nasaImage.getHdurl());
                urlTextView.setText("URL: " + nasaImage.getUrl());
            }
        }

    }

}