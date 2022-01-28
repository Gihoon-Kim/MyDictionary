package com.hoonydictionary.mydictionary.itemdata;

public class MeaningList {

    private String m_POS;
    private String m_Mean;
    private String m_Word;

    public MeaningList(String m_POS, String m_Mean, String m_Word) {

        this.m_Mean = m_Mean;
        this.m_POS = m_POS;
        this.m_Word = m_Word;
    }

    public String get_m_POS() {
        return m_POS;
    }

    public void set_m_POS(String m_POS) {
        this.m_POS = m_POS;
    }

    public String get_m_Mean() {
        return m_Mean;
    }

    public void set_m_Mean(String m_Mean) {
        this.m_Mean = m_Mean;
    }

    public String get_m_Word() {
        return m_Word;
    }

    public void set_m_Word(String m_Word) {
        this.m_Word = m_Word;
    }
}
