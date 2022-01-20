package com.hoonydictionary.mydictionary.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

public class AddNewWordDialog {

    private final static String TAG = "Add New Word Dialog";
    private final Context m_Context;
    private ArrayList<WordsList> m_WordsArrayList;

    // to give specific IDs to new dynamically created spinner and EditText
    // Spinner id is odd, and EditTextId is even
    int m_NewViewIdNum = 1;

    public AddNewWordDialog(Context m_Context) {

        this.m_Context = m_Context;
    }

    @SuppressLint({"WrongConstant", "ResourceType"})
    public void CallDialog(SQLiteDatabase database, ArrayList<WordsList> m_WordsArrayList) {

        this.m_WordsArrayList = m_WordsArrayList;

        // Spinner option to attach on Spinner
        String[] m_SpinnerOptions = m_Context.getResources().getStringArray(R.array.parts_of_speech);

        // ArrayAdapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                m_Context,
                android.R.layout.simple_spinner_item,
                m_SpinnerOptions
        );

        final Dialog dialog = new Dialog(m_Context);

        // Hide Dialog's Title Bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_new_word);
        // Set Dialog's attribute
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        Spinner spinner = dialog.findViewById(R.id.spinner_part_of_speech);
        Button btnAddNewMeaning = dialog.findViewById(R.id.btnAddNewMeaning);

        // Attach Adapter to spinner
        spinner.setAdapter(spinnerAdapter);

        // create new TextView and Spinner dynamically
        btnAddNewMeaning.setOnClickListener(
                onClickAddNewMeaning(
                        spinnerAdapter,
                        dialog,
                        spinner
                )
        );

        Button btnAddNewWord = dialog.findViewById(R.id.btnAddNewWord);
        btnAddNewWord.setOnClickListener(
                onClickAddNewWord(
                        database,
                        dialog
                )
        );
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private View.OnClickListener onClickAddNewMeaning(ArrayAdapter<String> spinnerAdapter, Dialog dialog, Spinner spinner) {
        return view -> {

            LinearLayout linearLayout = dialog.findViewById(R.id.llParentNewViews);

            // New Views' parameter (Attributes)
            LinearLayout.LayoutParams newParam = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    1
            );
            newParam.setMargins(0, 0, 0, 10);

            LinearLayout newLinearLayout = new LinearLayout(m_Context);
            newLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            newLinearLayout.setLayoutParams(newParam);

            linearLayout.addView(newLinearLayout);

            // to make blank
            TextView newTextView = new TextView(m_Context);
            newTextView.setText("");
            newTextView.setLayoutParams(newParam);

            Spinner newSpinner = new Spinner(m_Context);
            newSpinner.setAdapter(spinnerAdapter);
            newSpinner.setBackground(spinner.getBackground());
            newSpinner.setId(m_NewViewIdNum++);
            newSpinner.setSelection(0);

            EditText newEditText = new EditText(m_Context);
            newEditText.setLayoutParams(newParam);
            newEditText.setId(m_NewViewIdNum++);

            newSpinner.setLayoutParams(newParam);
            newLinearLayout.addView(newTextView);
            newLinearLayout.addView(newSpinner);
            newLinearLayout.addView(newEditText);
        };
    }

    private View.OnClickListener onClickAddNewWord(SQLiteDatabase database, Dialog dialog) {

        return view -> {

            // Content for database Words
            ContentValues contentValuesForWord = new ContentValues();
            contentValuesForWord.put("word", ((EditText) dialog.findViewById(R.id.et_Word)).getText().toString());
            database.insert("WORDS", null, contentValuesForWord);
            WordsList newWord = new WordsList(((EditText) dialog.findViewById(R.id.et_Word)).getText().toString());

            // Set first option
            String m_SelectOptionQuery =
                    "SELECT * " +
                            "FROM OPTIONS;";
            @SuppressLint("Recycle") Cursor m_OptionCursor = database.rawQuery(
                    m_SelectOptionQuery,
                    null
            );
            while(m_OptionCursor.moveToNext()) {

                newWord.set_m_Text_Size(Integer.parseInt(m_OptionCursor.getString(2)));
                newWord.set_m_Color(Integer.parseInt(m_OptionCursor.getString(1)));
            }
            m_WordsArrayList.add(newWord);

            // Content for database Means
            ContentValues contentValuesForMean = new ContentValues();
            contentValuesForMean.put("WORD", ((EditText) dialog.findViewById(R.id.et_Word)).getText().toString());
            contentValuesForMean.put("POS", ((Spinner) dialog.findViewById(R.id.spinner_part_of_speech)).getSelectedItem().toString());
            contentValuesForMean.put("MEAN", ((EditText) dialog.findViewById(R.id.et_Meaning)).getText().toString());
            database.insert("MEANS", null, contentValuesForMean);

            // if more meaning and part of speeches are there, they also are added.
            if (m_NewViewIdNum > 2) {

                for (int i = 1; i < m_NewViewIdNum; i += 2) {

                    ContentValues contentValuesForMoreData = new ContentValues();

                    contentValuesForMoreData.put("WORD", ((EditText) dialog.findViewById(R.id.et_Word)).getText().toString());
                    contentValuesForMoreData.put("POS", ((Spinner) dialog.findViewById(i)).getSelectedItem().toString());
                    contentValuesForMoreData.put("MEAN", ((EditText) dialog.findViewById(i + 1)).getText().toString());

                    database.insert("MEANS", null, contentValuesForMoreData);
                }
            }

            dialog.dismiss();
        };
    }
}
