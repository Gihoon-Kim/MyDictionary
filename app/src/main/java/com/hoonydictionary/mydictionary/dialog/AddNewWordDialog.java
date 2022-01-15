package com.hoonydictionary.mydictionary.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
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

public class AddNewWordDialog {

    private final Context m_Context;

    // to give specific IDs to new dynamically created spinner and EditText
    int spinnerIdNum = 0;
    int editTextIdNum = 0;

    public AddNewWordDialog(Context m_Context) {

        this.m_Context = m_Context;
    }

    @SuppressLint({"WrongConstant", "ResourceType"})
    public void CallDialog() {

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
        btnAddNewWord.setOnClickListener(onClickAddNewWord());
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
            newSpinner.setId(spinnerIdNum);
            spinnerIdNum++;

            EditText newEditText = new EditText(m_Context);
            newEditText.setLayoutParams(newParam);
            newEditText.setId(editTextIdNum);
            editTextIdNum++;

            newSpinner.setLayoutParams(newParam);
            newLinearLayout.addView(newTextView);
            newLinearLayout.addView(newSpinner);
            newLinearLayout.addView(newEditText);
        };
    }

    private View.OnClickListener onClickAddNewWord() {

        return view -> {
            // TODO : put data into database and ArrayList of the Main Activity
        };
    }
}
