package com.hoonydictionary.mydictionary.dialog;

import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.adapter.MainActivityRecyclerViewAdapter;
import com.hoonydictionary.mydictionary.database.DBHelper;

/*
This dialog is to set text size, text color, and background color
 */
public class SettingDialog {

    private final static int m_DATABASE_VERSION = 1;

    private final Context m_Context;
    private final MainActivityRecyclerViewAdapter m_MainActivityRecyclerViewAdapter;
    private final ConstraintLayout m_Layout;

    // Views
    private Spinner m_Spinner_Text_Color;
    private Spinner m_Spinner_Text_Size;
    private Spinner m_Spinner_Background_Color;

    Dialog dialog;
    DBHelper dbHelper;
    SQLiteDatabase database;

    public SettingDialog(Context m_Context, MainActivityRecyclerViewAdapter mainActivityRecyclerViewAdapter, ConstraintLayout parentLayout) {

        this.m_Context = m_Context;
        this.m_MainActivityRecyclerViewAdapter = mainActivityRecyclerViewAdapter;
        this.m_Layout = parentLayout;
    }

    public void CallDialog() {

        dialog = new Dialog(m_Context);

        dbHelper = new DBHelper(m_Context, "WORDS.db", null, m_DATABASE_VERSION);
        database = dbHelper.getWritableDatabase();
        dbHelper.onCreate(database);

        // Hide Dialog's Title Bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_setting);
        // Set Dialog's attribute
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        // Spinners Setting
        m_Spinner_Text_Size = dialog.findViewById(R.id.setting_spinner_text_size);
        m_Spinner_Text_Color = dialog.findViewById(R.id.setting_spinner_text_color);
        m_Spinner_Background_Color = dialog.findViewById(R.id.setting_spinner_background_color);

        // Spinner option to attach on Spinner
        String[] m_Spinner_Options_Text_Size = m_Context.getResources().getStringArray(R.array.text_size);
        String[] m_Spinner_Options_Text_Color = m_Context.getResources().getStringArray(R.array.text_Color);
        String[] m_Spinner_Options_Background_Color = m_Context.getResources().getStringArray(R.array.background_Color);

        // ArrayAdapter for Spinners
        ArrayAdapter<String> m_Adapter_Spinner_Text_Size = new ArrayAdapter<>(
                m_Context,
                android.R.layout.simple_spinner_item,
                m_Spinner_Options_Text_Size
        );
        ArrayAdapter<String> m_Adapter_Spinner_Text_Color = new ArrayAdapter<>(
                m_Context,
                android.R.layout.simple_spinner_item,
                m_Spinner_Options_Text_Color
        );
        ArrayAdapter<String> m_Adapter_Spinner_Background_Color = new ArrayAdapter<>(
                m_Context,
                android.R.layout.simple_spinner_item,
                m_Spinner_Options_Background_Color
        );

        m_Spinner_Text_Size.setAdapter(m_Adapter_Spinner_Text_Size);
        m_Spinner_Text_Color.setAdapter(m_Adapter_Spinner_Text_Color);
        m_Spinner_Background_Color.setAdapter(m_Adapter_Spinner_Background_Color);

        Button m_BtnApplySetting = dialog.findViewById(R.id.btnApply);
        m_BtnApplySetting.setOnClickListener(OnBtnApplyListener());
        Button m_BtnCancel = dialog.findViewById(R.id.btnCancel);
        m_BtnCancel.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
    }

    private View.OnClickListener OnBtnApplyListener() {
        return view -> {

            // Get Color and apply
            int m_Color;
            String selectedItem = m_Spinner_Text_Color.getSelectedItem().toString();
            switch (selectedItem) {
                case "Red":
                    m_Color = Color.RED;
                    break;
                case "Blue":
                    m_Color = Color.BLUE;
                    break;
                case "Green":
                    m_Color = Color.GREEN;
                    break;
                case "White":
                    m_Color = Color.WHITE;
                    break;
                default:
                    m_Color = Color.BLACK;
                    break;
            }
            m_MainActivityRecyclerViewAdapter.setTextColor(m_Color);

            // Get Size and apply
            int m_Size;
            selectedItem = m_Spinner_Text_Size.getSelectedItem().toString();
            switch (selectedItem) {
                case "Small":
                    m_Size = 20;
                    break;
                case "Medium":
                    m_Size = 30;
                    break;
                default:
                    m_Size = 40;
                    break;
            }
            m_MainActivityRecyclerViewAdapter.setTextSize(m_Size);
            m_MainActivityRecyclerViewAdapter.notifyDataSetChanged();

            // Background Color and Apply
            int m_Background_Color;
            selectedItem = m_Spinner_Background_Color.getSelectedItem().toString();
            switch (selectedItem) {
                case "Red":
                    m_Background_Color = Color.RED;
                    break;
                case "Blue":
                    m_Background_Color = Color.BLUE;
                    break;
                case "Green":
                    m_Background_Color = Color.GREEN;
                    break;
                case "White":
                    m_Background_Color = Color.WHITE;
                    break;
                default:
                    m_Background_Color = Color.BLACK;
                    break;
            }
            m_Layout.setBackgroundColor(m_Background_Color);

            // TODO : SAVE ON DATABASE SETTING OPTIONS TO KEEP OPTIONS WHEN THE APPLICATION RESTART
            ContentValues contentValues = new ContentValues();
            contentValues.put("TextColor", m_Color);
            contentValues.put("TextSize", m_Size);
            contentValues.put("BackgroundColor", m_Background_Color);

            String insertOrReplaceQuery = "INSERT OR REPLACE INTO OPTIONS (_id, TextColor, TextSize, BackgroundColor) "
                    + "VALUES ("
                    + 1 + ", "
                    + m_Color + ", "
                    + m_Size + ", "
                    + m_Background_Color + ");";
            database.execSQL(insertOrReplaceQuery);
            dialog.dismiss();
        };
    }
}