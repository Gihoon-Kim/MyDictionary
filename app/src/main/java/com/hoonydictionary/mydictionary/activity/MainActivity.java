package com.hoonydictionary.mydictionary.activity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
import com.hoonydictionary.mydictionary.database.DBHelper;
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

    private final static String TAG = "MainActivity";

    private final int m_DATABASE_VERSION = 1;
    // Views
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvMainWords)
    RecyclerView rvMainWords;

    Toolbar newToolBar;

    MainActivityRecyclerViewAdapter mainActivityRecyclerViewAdapter;

    // ArrayList for words
    ArrayList<WordsList> m_WordsArrayList = new ArrayList<>();

    // DB and DB Helper
    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Create Toolbar instead of Action Bar
        newToolBar = findViewById(R.id.newToolBar);
        setSupportActionBar(newToolBar);

        // Setting RecyclerView
        // Adapter for RecyclerView(rvMainWords)
        mainActivityRecyclerViewAdapter = new MainActivityRecyclerViewAdapter(m_WordsArrayList);
        rvMainWords.setLayoutManager(new LinearLayoutManager(this));
        rvMainWords.setAdapter(mainActivityRecyclerViewAdapter);

        // Setting database
        dbHelper = new DBHelper(this, "WORDS.db", null, m_DATABASE_VERSION);
        database = dbHelper.getReadableDatabase();
        dbHelper.onCreate(database);

        // Get data from database through SELECT Query
        String m_SelectQuery =
                "SELECT * " +
                "FROM WORDS;";
        @SuppressLint("Recycle") Cursor m_Cursor = database.rawQuery(m_SelectQuery, null);
        while(m_Cursor.moveToNext()) {

            Log.i(TAG, m_Cursor.getString(1));
            WordsList wordsList = new WordsList(m_Cursor.getString(1));
            m_WordsArrayList.add(wordsList);
            mainActivityRecyclerViewAdapter.notifyDataSetChanged();
        }

        m_Cursor.close();
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

                database = dbHelper.getWritableDatabase();
                // TODO : set click event for menu Item : Add Word
                AddNewWordDialog addNewWordDialog = new AddNewWordDialog(this);
                addNewWordDialog.CallDialog(database, m_WordsArrayList);
                mainActivityRecyclerViewAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}