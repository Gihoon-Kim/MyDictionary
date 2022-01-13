package com.hoonydictionary.mydictionary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.hoonydictionary.mydictionary.R;

public class AddNewWordDialog {

    private final Context m_Context;

    public AddNewWordDialog(Context m_Context) {

        this.m_Context = m_Context;
    }

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

        // Attach Adapter to spinner
        spinner.setAdapter(spinnerAdapter);

        dialog.show();
    }
}
