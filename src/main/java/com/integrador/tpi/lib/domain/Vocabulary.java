package com.integrador.tpi.lib.domain;

public class Vocabulary {
    private enum status {
        NEW,
        UPDATED,
        ALREADY_EXISTS
    }

    private final String term;
    private int documentFrequency;
    private int maxFrequency;
    private status status;

    public Vocabulary(String term) {
        this.documentFrequency = 1;
        this.term = term;
        this.status = status.NEW;
    }

    public Vocabulary(String term, int documentFrequency, int maxFrequency) {
        this.term = term;
        this.documentFrequency = documentFrequency;
        this.maxFrequency = maxFrequency;
        this.status = status.ALREADY_EXISTS;
    }

    public boolean needsUpdate() {
        return status == status.UPDATED;
    }

    public boolean isNew() {
        return status == status.NEW;
    }

    public String getTerm() {
        return term;
    }

    public void setMaxFrequency(int maxFrequency) {
        this.maxFrequency = maxFrequency;
        this.status = (this.status == status.ALREADY_EXISTS) ? status.UPDATED : status.NEW;
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
        this.status = (this.status == status.ALREADY_EXISTS) ? status.UPDATED : status.NEW;
    }
}
