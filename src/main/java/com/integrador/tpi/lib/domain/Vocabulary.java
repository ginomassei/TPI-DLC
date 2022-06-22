package com.integrador.tpi.lib.domain;

public class Vocabulary {
    private final String term;
    private int documentFrequency;
    private int maxFrequency;

    public Vocabulary(String term) {
        this.documentFrequency = 1;
        this.term = term;
    }

    public Vocabulary(String term, int documentFrequency, int maxFrequency) {
        this.term = term;
        this.documentFrequency = documentFrequency;
        this.maxFrequency = maxFrequency;
    }

    public String getTerm() {
        return term;
    }

    public void setMaxFrequency(int maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public int getDocumentFrequency() {
        return documentFrequency;
    }

    public int getMaxFrequency() {
        return maxFrequency;
    }

    public String toString() {
        return getTerm() + " | " + getDocumentFrequency() + " | " + getMaxFrequency();
    }

    public void incrementDocumentFrequency() {
        this.documentFrequency++;
    }
}
