package com.hoonydictionary.mydictionary.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.adapter.MainActivityRecyclerViewAdapter;
import com.hoonydictionary.mydictionary.dialog.AddNewWordDialog;
import com.hoonydictionary.mydictionary.dialog.DeveloperInfoDialog;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
This activity is for showing words and basic widgets such as menu and recycler view
 */
public class MainActivity extends AppCompatActivity {

    // Views
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvMainWords)
    RecyclerView rvMainWords;

    Toolbar newToolBar;

    // ArrayList for words
    ArrayList<WordsList> wordsLists = new ArrayList<>();

    // Adapter for RecyclerView(rvMainWords)
    private MainActivityRecyclerViewAdapter mainActivityRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Create Toolbar instead of Action Bar
        newToolBar = (Toolbar) findViewById(R.id.newToolBar);
        setSupportActionBar(newToolBar);

        // Setting RecyclerView
        mainActivityRecyclerViewAdapter = new MainActivityRecyclerViewAdapter();
        rvMainWords.setLayoutManager(new LinearLayoutManager(this));
        rvMainWords.setAdapter(mainActivityRecyclerViewAdapter);

    }

    // Add Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    // Menu Item Click Event
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {

            case R.id.menuItemAddWord:

                // TODO : set click event for menu Item : Add Word
                AddNewWordDialog addNewWordDialog = new AddNewWordDialog(this);
                addNewWordDialog.CallDialog();
                return true;
            case R.id.menuItemSetting:

                // TODO : set click event for menu Item : Setting
                return true;
            default:

                // TODO : set click event for menu Item : Developer Info
                DeveloperInfoDialog developerInfoDialog = new DeveloperInfoDialog(this);
                developerInfoDialog.CallDialog();
                return true;
        }
    }
}