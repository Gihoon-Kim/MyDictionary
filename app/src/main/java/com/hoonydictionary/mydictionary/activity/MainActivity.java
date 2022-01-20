package com.hoonydictionary.mydictionary.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.adapter.MainActivityRecyclerViewAdapter;
import com.hoonydictionary.mydictionary.database.DBHelper;
import com.hoonydictionary.mydictionary.dialog.AddNewWordDialog;
import com.hoonydictionary.mydictionary.dialog.DeveloperInfoDialog;
import com.hoonydictionary.mydictionary.dialog.SettingDialog;
import com.hoonydictionary.mydictionary.fragment.MainFragment;
import com.hoonydictionary.mydictionary.interfaces.OnItemClick;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
This activity is for showing words and basic widgets such as menu and recycler view
 */
public class MainActivity extends AppCompatActivity implements OnItemClick {

    private final static String TAG = "MainActivity";

    private final static int m_DATABASE_VERSION = 1;
    // Views
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.rvMainWords)
    RecyclerView rvMainWords;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.parentLayout)
    ConstraintLayout parentLayout;

    Toolbar newToolBar;

    MainActivityRecyclerViewAdapter mainActivityRecyclerViewAdapter;

    // ArrayList for words
    ArrayList<WordsList> m_WordsArrayList = new ArrayList<>();

    // DB and DB Helper
    DBHelper dbHelper;
    SQLiteDatabase database;

    // For bottom sheet Dialog
    FragmentManager fragmentManager;

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
        fragmentManager = getSupportFragmentManager();
        mainActivityRecyclerViewAdapter = new MainActivityRecyclerViewAdapter(m_WordsArrayList, getApplicationContext(), fragmentManager, this);
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
        @SuppressLint("Recycle") Cursor m_Cursor = database.rawQuery(
                m_SelectQuery,
                null
        );
        while(m_Cursor.moveToNext()) {

            Log.i(TAG, m_Cursor.getString(1));
            WordsList wordsList = new WordsList(m_Cursor.getString(1));
            m_WordsArrayList.add(wordsList);
            mainActivityRecyclerViewAdapter.notifyDataSetChanged();
        }
        // Get options from database through SELECT Query
        String m_SelectOptionQuery =
                "SELECT * " +
                    "FROM OPTIONS;";
        @SuppressLint("Recycle") Cursor m_OptionCursor = database.rawQuery(
                m_SelectOptionQuery,
                null
        );
        while(m_OptionCursor.moveToNext()) {

            for (int i = 0; i < m_WordsArrayList.size(); i++) {

                m_WordsArrayList.get(i).set_m_Color(Integer.parseInt(m_OptionCursor.getString(1)));
                m_WordsArrayList.get(i).set_m_Text_Size(Integer.parseInt(m_OptionCursor.getString(2)));
            }
            parentLayout.setBackgroundColor(Integer.parseInt(m_OptionCursor.getString(3)));
        }

        // TODO : SET DEFAULT VALUE OF TEXT SIZE, TEXT COLOR, AND BACKGROUND COLOR
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
                AddNewWordDialog addNewWordDialog = new AddNewWordDialog(this);
                addNewWordDialog.CallDialog(database, m_WordsArrayList);
                mainActivityRecyclerViewAdapter.notifyDataSetChanged();
                return true;

            case R.id.menuItemDeleteWord:

                // to get positions of checked items
                ArrayList<Integer> m_Position = new ArrayList<>();
                int m_NumCheckedBox = 0;
                for (int i = 0; i < m_WordsArrayList.size(); i++) {

                    if (m_WordsArrayList.get(i).getChecked()) {

                        m_Position.add(i);
                        m_NumCheckedBox++;
                    }
                }

                if (m_NumCheckedBox == 0) {

                    Toast.makeText(this, "Words are not selected", Toast.LENGTH_SHORT).show();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("Delete Words").setMessage("Do you really want to delete " + m_NumCheckedBox + "Words?");
                    int final_m_NumCheckedBox = m_NumCheckedBox;
                    builder.setPositiveButton("Yes", (dialogInterface, i) -> {

                        for (int itemCount = final_m_NumCheckedBox; itemCount > 0; itemCount--) {

                            // Delete Item from Database
                            database.execSQL(
                                    "DELETE " +
                                        "FROM WORDS " +
                                        "WHERE word = '" + m_WordsArrayList.get(m_Position.get(itemCount - 1)).get_m_Word() + "'");
                            database.execSQL(
                                    "DELETE " +
                                            "FROM MEANS " +
                                            "WHERE word = '" + m_WordsArrayList.get(m_Position.get(itemCount - 1)).get_m_Word() + "'");
                            // Delete Item from Adapter
                            mainActivityRecyclerViewAdapter.removeWord(m_Position.get(itemCount - 1));
                        }

                        mainActivityRecyclerViewAdapter.notifyDataSetChanged();
                    }).setNegativeButton("Cancel", null);

                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                return true;
            case R.id.menuItemSetting:

                SettingDialog settingDialog = new SettingDialog(this, mainActivityRecyclerViewAdapter, parentLayout);
                settingDialog.CallDialog();
                return true;
            default:

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

    // If the screen is rotated, keep the checkbox status
    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onClickItem(Context m_Context, WordsList wordsList, ArrayList<String> m_ArrayListPOS, ArrayList<String> m_ArrayListMean) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction;

        // Fragment for device size are big (tab)
        MainFragment mainFragment = new MainFragment(
                m_Context,
                wordsList,
                m_ArrayListPOS,
                m_ArrayListMean
        );

        transaction = fragmentManager.beginTransaction();
        transaction.setCustomAnimations(R.anim.enter_from_right, 0);
        transaction.addToBackStack(null);
        transaction.replace(R.id.frameLayout, mainFragment);
        transaction.commit();
    }
}