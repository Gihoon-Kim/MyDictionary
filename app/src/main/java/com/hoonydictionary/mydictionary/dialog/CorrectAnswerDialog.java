package com.hoonydictionary.mydictionary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.view.WindowManager;

import com.hoonydictionary.mydictionary.R;

public class CorrectAnswerDialog {

    private Context m_Context;

    public CorrectAnswerDialog(Context m_Context) {

        this.m_Context = m_Context;
    }

    public void CallDialog() {

        Dialog dialog = new Dialog(m_Context, R.style.Animation_DialogSlideUpDown);

        dialog.setContentView(R.layout.dialog_correct_answer);
        // Set Dialog's attribute
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        dialog.show();

        // Auto dismiss Dialog
        final Handler handler = new Handler();
        // Close dialog after 1000ms
        handler.postDelayed(dialog::dismiss, 1000);
    }
}
