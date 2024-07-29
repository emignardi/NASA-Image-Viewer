package com.example.nasaimageviewer;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * This class/fragment is used to display details related to a NasaImage.
 * Here, a user can navigate to a browser to view an HD image or delete that image from the database.
 * @author Eric Mignardi
 * @version 1.0
 */
public class DetailFragment extends Fragment {

    /**
     * The Bundle containing data passed from another activity
     */
    private Bundle data;

    /**
     * This is where the UI is inflated.
     * Similar in functionality to {@link android.widget.Adapter#getView}.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return The inflated view
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        data = getArguments();
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView dateTextView = view.findViewById(R.id.dateTextView2);
        dateTextView.setText(data.getString("date"));
        TextView hdurlTextView = view.findViewById(R.id.hdurlTextView2);
        hdurlTextView.setText(data.getString("hdurl"));
        TextView urlTextView = view.findViewById(R.id.urlTextView2);
        urlTextView.setText(data.getString("url"));
        Button viewButton = view.findViewById(R.id.view);
        viewButton.setOnClickListener((click) -> {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(data.getString("hdurl")));
            startActivity(intent);
        });
        Button delete = view.findViewById(R.id.delete);
        delete.setOnClickListener((click) -> {
            DatabaseHelper databaseHelper = new DatabaseHelper(getContext());
            SQLiteDatabase sqLiteDatabase = databaseHelper.getWritableDatabase();
            sqLiteDatabase.delete(DatabaseHelper.TABLE_NAME, "DATE=?", new String[]{data.getString("date")});
            sqLiteDatabase.close();
        });
        return view;
    }

    /**
     * This is called when a fragment has been added to the activity containing the FrameLayout
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

}