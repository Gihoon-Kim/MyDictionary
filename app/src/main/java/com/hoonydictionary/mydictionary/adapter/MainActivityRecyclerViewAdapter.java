package com.hoonydictionary.mydictionary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoonydictionary.mydictionary.R;
import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

public class MainActivityRecyclerViewAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewAdapter.ItemViewHolder> {

    // List for adapter
    private ArrayList<WordsList> itemList = new ArrayList<>();

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        // Inflate rvmainwords_items.xml by using LayoutInflater
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rvmainwords_items, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {

        // Show item one by one
        holder.onBind(itemList.get(position));
    }

    @Override
    public int getItemCount() {

        return itemList.size();
    }

    void addWord(WordsList wordsList) {

        itemList.add(wordsList);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Word;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Word = itemView.findViewById(R.id.tvRecyclerViewItem);
        }

        void onBind(WordsList wordsList) {

            tv_Word.setText(wordsList.get_m_Word());
        }
    }
}
