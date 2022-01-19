package com.hoonydictionary.mydictionary.interfaces;

import android.content.Context;

import com.hoonydictionary.mydictionary.itemdata.WordsList;

import java.util.ArrayList;

public interface OnItemClick {

    void onClickItem(Context m_Context, WordsList wordsList, ArrayList<String> m_ArrayListPOS, ArrayList<String> m_ArrayListMean);
}
