package com.hoonydictionary.mydictionary.itemdata;

import android.graphics.Color;

public class WordsList {

    private String m_Word;
    private Boolean isChecked = false;
    private int m_Color = Color.BLACK;
    private int m_Text_Size = 20;

    public int get_m_Color() {
        return m_Color;
    }

    public void set_m_Color(int m_Color) {
        this.m_Color = m_Color;
    }

    public int get_m_Text_Size() {
        return m_Text_Size;
    }

    public void set_m_Text_Size(int m_Text_Size) {
        this.m_Text_Size = m_Text_Size;
    }

    public WordsList(String m_Word) {

        set_m_Word(m_Word);
    }

    public String get_m_Word() {
        return m_Word;
    }

    public void set_m_Word(String m_Word) {
        this.m_Word = m_Word;
    }

    public Boolean getChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
