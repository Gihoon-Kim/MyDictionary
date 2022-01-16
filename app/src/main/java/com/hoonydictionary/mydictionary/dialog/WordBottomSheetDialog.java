package com.hoonydictionary.mydictionary.dialog;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.adapter.BottomSheetDialogAdapter;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

public class WordBottomSheetDialog extends BottomSheetDialogFragment {

    private final static String TAG = "Bottom Sheet Dialog";

    private Context m_Context;
    private WordsList m_WordsList;
    private ArrayList<String> m_ArrayListPOS;
    private ArrayList<String> m_ArrayListMean;

    // Dialog Adapter for Recycler View of the Dialog

    public WordBottomSheetDialog(Context context, WordsList wordsList, ArrayList<String> m_ArrayListPOS, ArrayList<String> m_ArrayListMean) {

        this.m_Context = context;
        this.m_WordsList = wordsList;
        this.m_ArrayListPOS = m_ArrayListPOS;
        this.m_ArrayListMean = m_ArrayListMean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        BottomSheetDialogAdapter adapter = new BottomSheetDialogAdapter(m_Context, m_ArrayListPOS, m_ArrayListMean);

        View view = inflater.inflate(R.layout.dialog_bottom_sheet_item_click, container, false);

        // Text View that is word
        TextView tv_Word = view.findViewById(R.id.tv_Word);
        tv_Word.setText(m_WordsList.get_m_Word());

        RecyclerView recyclerView = view.findViewById(R.id.rv_Bottom_Sheet_List);
        recyclerView.setLayoutManager(new LinearLayoutManager(m_Context));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
