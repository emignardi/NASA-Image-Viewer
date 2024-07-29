package com.example.nasaimageviewer;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * This class/activity is used as the container for {@link DetailFragment}.
 * The details include a date, hdurl, and url.
 * Users from this activity can view the hd image in the browser or delete the image from the NASA_IMAGES database.
 * @author Eric Mignardi
 * @version 1.0
 */
public class EmptyActivity extends AppCompatActivity {

    /**
     * This method is called when an activity is first launched and is responsible for creating.
     * A Bundle of data is retrieved from the activity that triggered the transition through an Intent.
     * The Bundle is then passed to an instantiated DetailFragment.
     * The Fragment stores that data using setArguments and a FragmentManager begins a transaction to replace the fragment in the specified location.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_empty);
        Bundle data = getIntent().getExtras();
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(data);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentLocation, fragment).commit();
    }

}