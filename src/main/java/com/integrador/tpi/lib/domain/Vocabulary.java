package com.integrador.tpi.lib.domain;

public class Vocabulary {
    private String term;
    private Integer documentFrequency;
    private Integer maxFrequency;

    public Vocabulary(String term, Integer documentFrequency, Integer maxFrequency) {
        this.term = term;
        this.documentFrequency = documentFrequency;
        this.maxFrequency = maxFrequency;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public void setDocumentFrequency(Integer documentFrequency) {
        this.documentFrequency = documentFrequency;
    }

    public void setMaxFrequency(Integer maxFrequency) {
        this.maxFrequency = maxFrequency;
    }

    public Integer getDocumentFrequency() {
        return documentFrequency;
    }

    public Integer getMaxFrequency() {
        return maxFrequency;
    }

    public String toString() {
        return getTerm() + " | " + getDocumentFrequency() + " | " + getMaxFrequency();
    }

    public void incrementDocumentFrequency() {
        this.documentFrequency++;
    }
}
