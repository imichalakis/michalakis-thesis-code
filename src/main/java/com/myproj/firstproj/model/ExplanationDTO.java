package com.myproj.firstproj.model;

public class ExplanationDTO {
    private String readableFact;
    private String metaJustification;

    public ExplanationDTO(String readableFact, String metaJustification) {
        this.readableFact = readableFact;
        this.metaJustification = metaJustification;
    }

    public String getReadableFact() {
        return readableFact;
    }

    public String getMetaJustification() {
        return metaJustification;
    }
}
