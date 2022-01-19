package com.hoonydictionary.mydictionary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.adapter.FragmentRecyclerViewAdapter;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private final Context m_Context;
    private final WordsList m_WordsList;
    private final ArrayList<String> m_ArrayListPOS;
    private final ArrayList<String> m_ArrayListMean;

    public MainFragment(Context context, WordsList wordsList, ArrayList<String> m_ArrayListPOS, ArrayList<String> m_ArrayListMean) {

        this.m_Context = context;
        this.m_WordsList = wordsList;
        this.m_ArrayListPOS = m_ArrayListPOS;
        this.m_ArrayListMean = m_ArrayListMean;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FragmentRecyclerViewAdapter adapter = new FragmentRecyclerViewAdapter( m_ArrayListPOS, m_ArrayListMean);

        View view = inflater.inflate(R.layout.activity_main_fragment, container, false);

        // Text View that is word
        TextView tv_Word = view.findViewById(R.id.tv_Word);
        tv_Word.setText(m_WordsList.get_m_Word());

        RecyclerView recyclerView = view.findViewById(R.id.rv_Fragment_List);
        recyclerView.setLayoutManager(new LinearLayoutManager(m_Context));
        recyclerView.setAdapter(adapter);

        return view;
    }
}
