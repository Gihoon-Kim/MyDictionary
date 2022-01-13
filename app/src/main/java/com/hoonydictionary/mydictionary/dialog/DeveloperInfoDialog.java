package com.hoonydictionary.mydictionary.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

import com.hoonydictionary.mydictionary.R;

public class DeveloperInfoDialog {

    private Context m_Context;

    public DeveloperInfoDialog(Context m_Context) {

        this.m_Context = m_Context;
    }

    public void CallDialog() {

        final Dialog dialog = new Dialog(m_Context);

        // Hide Dialog's Title Bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_developer_info);

        dialog.show();
    }
}
