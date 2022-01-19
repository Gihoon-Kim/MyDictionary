package com.hoonydictionary.mydictionary.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.NdefMessage;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.database.DBHelper;
import com.hoonydictionary.mydictionary.dialog.WordBottomSheetDialog;
import com.hoonydictionary.mydictionary.interfaces.OnItemClick;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.ItemViewHolder> {

    private final int m_DATABASE_VERSION = 1;
    private final static String TAG = "Main Adapter";

    // List for adapter
    private final ArrayList<WordsList> itemList;
    private final Context m_Context;
    private final FragmentManager fragmentManager;

    // interface Item Click Listener
    private OnItemClick m_Callback;

    public MainActivityRecyclerViewAdapter(ArrayList<WordsList> m_WordsArrayList, Context m_Context, FragmentManager fragmentManager, OnItemClick listener) {

        this.itemList = m_WordsArrayList;
        this.m_Context = m_Context;
        this.fragmentManager = fragmentManager;
        this.m_Callback = listener;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate rvmainwords_items.xml by using LayoutInflater
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rvmainwords_items,
                parent,
                false
        );
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        // The check status values (T/F) of the checkbox does not change only when it is declared as final
        final WordsList wordsList = itemList.get(position);

        // initialization listener of checkbox to null
        holder.cb_CheckBox.setOnCheckedChangeListener(null);
        // Get check value through getter of the model class, then set this value through setter into checkbox of an item
        holder.cb_CheckBox.setChecked(wordsList.getChecked());
        // Attach a listener to find out the status of the check box.
        holder.cb_CheckBox.setOnCheckedChangeListener((compoundButton, b) -> {

            wordsList.setChecked(b);
        });
        // Show item one by one
        holder.onBind(itemList.get(position));
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    // Delete Item Method
    public void removeWord(int position) {

        itemList.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_Word;
        private final CheckBox cb_CheckBox;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Word = itemView.findViewById(R.id.tvRecyclerViewItem);
            cb_CheckBox = itemView.findViewById(R.id.cb_CheckBox);

            // Click Event for Recycler View
            itemView.setOnClickListener(onAdapterItemClickListener());
            cb_CheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    cb_CheckBox.setChecked(cb_CheckBox.isChecked());
                }
            });
        }

        void onBind(WordsList wordsList) {

            tv_Word.setText(wordsList.get_m_Word());
        }

        private View.OnClickListener onAdapterItemClickListener() {
            return view -> {

                int pos = getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION) {

                    // DB and DB Helper
                    DBHelper dbHelper;
                    SQLiteDatabase database;

                    WordsList wordsList = itemList.get(pos);

                    // GET DATA THAT COLUMN "WORD" IS MATCHED FROM MEANS DATABASE
                    dbHelper = new DBHelper(
                            m_Context,
                            "WORDS.db",
                            null,
                            m_DATABASE_VERSION
                    );
                    database = dbHelper.getReadableDatabase();
                    // Get data from database through SELECT Query
                    String m_SelectQuery =
                            "SELECT pos, mean " +
                                "FROM MEANS " +
                                "WHERE WORD = '" + wordsList.get_m_Word() + "'";
                    @SuppressLint("Recycle")
                    Cursor m_Cursor = database.rawQuery(m_SelectQuery, null);
                    // ArrayList for String values of database
                    ArrayList<String> m_ArrayListPOS = new ArrayList<>();
                    ArrayList<String> m_ArrayListMean = new ArrayList<>();
                    while(m_Cursor.moveToNext()) {

                        Log.i(TAG, m_Cursor.getString(0));
                        Log.i(TAG, m_Cursor.getString(1));
                        m_ArrayListPOS.add(m_Cursor.getString(0));
                        m_ArrayListMean.add(m_Cursor.getString(1));
                    }

                    DisplayMetrics metrics = m_Context.getResources().getDisplayMetrics();

                    if (metrics.widthPixels < 1500) {
                        // Bottom Sheet Dialog
                        WordBottomSheetDialog wordBottomSheetDialog = new WordBottomSheetDialog(
                                m_Context,
                                wordsList,
                                m_ArrayListPOS,
                                m_ArrayListMean
                        );
                        wordBottomSheetDialog.show(fragmentManager, "Bottom Sheet");
                    } else {

                        m_Callback.onClickItem(
                                m_Context,
                                wordsList,
                                m_ArrayListPOS,
                                m_ArrayListMean
                        );
                    }
                }
            };
        }
    }
}
