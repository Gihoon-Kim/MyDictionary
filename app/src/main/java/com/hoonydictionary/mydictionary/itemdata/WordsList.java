package com.hoonydictionary.mydictionary.itemdata;

public class WordsList {

    private String m_Word;

    public WordsList(String m_Word) {

        set_m_Word(m_Word);
    }

    public String get_m_Word() {
        return m_Word;
    }

    public void set_m_Word(String m_Word) {
        this.m_Word = m_Word;
    }
}
