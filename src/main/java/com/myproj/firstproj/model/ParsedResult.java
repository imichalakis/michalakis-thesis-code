package com.myproj.firstproj.model;

import java.util.List;

public class ParsedResult {
    private final String mainResult;
    private final List<String> supportingFacts;
    private String fullExplanation;
    private String comparativeReason;
    public ParsedResult(String mainResult, List<String> supportingFacts, String fullExplanation) {
        this.mainResult = mainResult;
        this.supportingFacts = supportingFacts;
        this.fullExplanation = fullExplanation;
    }
 

public String getComparativeReason() {
    return comparativeReason;
}

public void setComparativeReason(String comparativeReason) {
    this.comparativeReason = comparativeReason;
}

    public String getMainResult() {
        return mainResult;
    }
    public String getFullExplanation() {
        return fullExplanation;
    }
    
    public List<String> getSupportingFacts() {
        return supportingFacts;
    }

    @Override
    public String toString() {
        return "Main Result: " + mainResult + "\nSupporting Facts: " + supportingFacts;
    }
}
