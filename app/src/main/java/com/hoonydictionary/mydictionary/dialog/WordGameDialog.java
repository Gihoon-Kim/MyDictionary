package com.hoonydictionary.mydictionary.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.database.DBHelper;
import com.hoonydictionary.mydictionary.itemdata.MeaningList;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
This dialog is to play game.
Get list of words from database and randomly pick 10 words.
 */
public class WordGameDialog {

    private final static int m_DATABASE_VERSION = 1;
    private static final String TAG = "WordGameDialog";

    private final Context m_Context;

    // Views
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_CountDown)
    TextView tv_CountDown;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_QuestionHead)
    TextView tv_QuestionHead;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_QuestionPOS)
    TextView tv_QuestionPOS;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.tv_QuestionMean)
    TextView tv_QuestionMean;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.btn_Enter)
    Button btn_Enter;
    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.et_Answer)
    EditText et_Answer;

    DBHelper dbHelper;
    SQLiteDatabase database;

    Dialog dialog;

    public WordGameDialog(Context m_Context) {

        this.m_Context = m_Context;
    }

    public void CallDialog() {

        dialog = new Dialog(m_Context);

        // Database Setting
        dbHelper = new DBHelper(m_Context, "WORDS.db", null, m_DATABASE_VERSION);
        database = dbHelper.getReadableDatabase();
        dbHelper.onCreate(database);

        // Hide Dialog's Title Bar
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_game);
        // Set Dialog's attribute
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(params);

        ButterKnife.bind(this, dialog);
        // Start CountDown
        countDown(4000);
        // Play game
        playGame();
        dialog.show();

    }

    public void countDown(int time) {

        // First element : Time
        // Second element : Interval
        new CountDownTimer(time, 1000) {

            // Change View Every seconds
            @SuppressLint("SetTextI18n")
            @Override
            public void onTick(long l) {

                tv_CountDown.setText("Game will be started in " + l / 1000 + "Seconds");
            }

            @Override
            public void onFinish() {

                tv_CountDown.setVisibility(View.GONE);
                tv_QuestionHead.setVisibility(View.VISIBLE);
                tv_QuestionMean.setVisibility(View.VISIBLE);
                tv_QuestionPOS.setVisibility(View.VISIBLE);
                et_Answer.setVisibility(View.VISIBLE);
                btn_Enter.setVisibility(View.VISIBLE);
            }
        }.start();
    }

    @SuppressLint("SetTextI18n")
    public void playGame() {

        AtomicInteger m_Round = new AtomicInteger(1);

        // Get Meanings from Database
        String m_SelectQuery =
                "SELECT * " +
                        "FROM MEANS;";
        @SuppressLint("Recycle") Cursor m_Cursor = database.rawQuery(
                m_SelectQuery,
                null
        );

        // String list to save Part of Speeches, Meanings, and Words
        ArrayList<MeaningList> m_ListMean = new ArrayList<>();

        while (m_Cursor.moveToNext()) {

            MeaningList meaningList = new MeaningList(
                    m_Cursor.getString(1),
                    m_Cursor.getString(2),
                    m_Cursor.getString(3)
            );
            m_ListMean.add(meaningList);
        }
        m_Cursor.close();

        // To use the randomly generated index later, the function return the value.
        int index = generateNewQuestion(m_ListMean);
        AtomicInteger m_Score = new AtomicInteger();
        btn_Enter.setOnClickListener(view -> {

            generateNewQuestion(m_ListMean);
            tv_QuestionHead.setText("Question " + m_Round.incrementAndGet() + " : ");
            // if correct
            if (et_Answer.getText().toString().toLowerCase().trim().equals(m_ListMean.get(index).get_m_Word().toLowerCase())) {

                // New Question should be shown if the answer is correct
                m_Score.getAndIncrement();
                CorrectAnswerDialog correctAnswerDialog = new CorrectAnswerDialog(m_Context);
                correctAnswerDialog.CallDialog();
            } else {

                // if not correct
                IncorrectAnswerDialog incorrectAnswerDialog = new IncorrectAnswerDialog(m_Context);
                incorrectAnswerDialog.CallDialog();
            }

            if (m_Round.get() == 11) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(m_Context);

                alertDialogBuilder.setMessage("You got " + m_Score.get() * 10 + "Points");
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                final Handler handler = new Handler();
                // Close dialog after 3000ms
                handler.postDelayed(alertDialog::dismiss, 3000);

                dialog.dismiss();
            }
        });
    }

    public int generateNewQuestion(ArrayList<MeaningList> m_ListMean) {

        int randomIndex = new Random().nextInt(m_ListMean.size());
        tv_QuestionPOS.setText(m_ListMean.get(randomIndex).get_m_POS());
        tv_QuestionMean.setText(m_ListMean.get(randomIndex).get_m_Mean());
        return randomIndex;
    }
}