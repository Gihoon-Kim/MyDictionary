package com.hoonydictionary.mydictionary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hoonydictionary.mydictionary.R;

import java.util.ArrayList;

public class FragmentRecyclerViewAdapter extends RecyclerView.Adapter<FragmentRecyclerViewAdapter.ItemViewHolder>  {

    private final ArrayList<String> m_MeanList;
    private final ArrayList<String> m_POSList;

    public FragmentRecyclerViewAdapter(ArrayList<String> m_ArrayListPOS, ArrayList<String> m_ArrayListMean) {

        this.m_MeanList = m_ArrayListMean;
        this.m_POSList = m_ArrayListPOS;
    }

    @NonNull
    @Override
    public FragmentRecyclerViewAdapter.ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.rv_fragment_pos_mean,
                parent,
                false
        );
        return new FragmentRecyclerViewAdapter.ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FragmentRecyclerViewAdapter.ItemViewHolder holder, int position) {

        holder.onBind(m_MeanList.get(position), m_POSList.get(position));
    }

    @Override
    public int getItemCount() {

        return m_MeanList.size();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView tv_BottomSheet_POS;
        private final TextView tv_BottomSheet_Mean;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_BottomSheet_POS = itemView.findViewById(R.id.tv_BottomSheet_POS);
            tv_BottomSheet_Mean = itemView.findViewById(R.id.tv_BottomSheet_Mean);
        }

        void onBind(String mean, String pos) {

            tv_BottomSheet_POS.setText(pos);
            tv_BottomSheet_Mean.setText(mean);
        }
    }
}
