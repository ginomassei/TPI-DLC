package com.integrador.tpi.lib.domain;

public class Post implements Comparable<Post> {
    private Integer termFrequency;
    private double relevanceIdx;
    private final Integer documentId;
    private String documentName;

    public Post(Integer documentId, Integer termFrequency) {
        this.documentId = documentId;
        this.termFrequency = termFrequency;
        this.relevanceIdx = 0;
    }

    public Post(Integer documentId, Integer termFrequency, String documentName) {
        this.documentId = documentId;
        this.termFrequency = termFrequency;
        this.documentName = documentName;
        this.relevanceIdx = 0;
    }

    public Integer getTermFrequency() {
        return termFrequency;
    }

    public void setTermFrequency(Integer termFrequency) {
        this.termFrequency = termFrequency;
    }

    public Integer getDocumentId() {
        return documentId;
    }

    public double getRelevanceIdx() {
        return relevanceIdx;
    }

    public void setRelevanceIdx(double relevanceIdx) {
        this.relevanceIdx = relevanceIdx;
    }

    public void incrementTermFrequency() {
        this.termFrequency++;
    }

    @Override
    public int compareTo(Post o) {
        return o.termFrequency - this.termFrequency;
    }

    @Override
    public boolean equals(Object o) {
        return this.documentId.equals(((Post) o).documentId);
    }

    @Override
    public String toString() {
        return this.documentName + " | " + this.termFrequency + " | " + Math.round(this.relevanceIdx);
    }
}