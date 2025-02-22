package com.myproj.firstproj.model;

import java.util.List;

public class ParsedResult {
    private final String mainResult;
    private final List<String> supportingFacts;

    public ParsedResult(String mainResult, List<String> supportingFacts) {
        this.mainResult = mainResult;
        this.supportingFacts = supportingFacts;
    }

    public String getMainResult() {
        return mainResult;
    }

    public List<String> getSupportingFacts() {
        return supportingFacts;
    }

    @Override
    public String toString() {
        return "Main Result: " + mainResult + "\nSupporting Facts: " + supportingFacts;
    }
}
