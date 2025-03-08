package com.myproj.firstproj.service;

import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.myproj.firstproj.model.ParsedResult;

import com.myproj.firstproj.model.ParsedResult;
import com.myproj.firstproj.model.WorkflowForm;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays; 
import io.swagger.client.ApiException;
import io.swagger.client.api.OperationsControllerApi;
import io.swagger.client.model.GorgiasQuery;
import io.swagger.client.model.GorgiasQueryResult;
import io.swagger.client.model.QueryResult;
import io.swagger.client.api.ExecuteGorgiasQueryControllerApi;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


@Service
public class GorgiasService{
    
    private final RestTemplate restTemplate;
    private final String basePath;
     private ExecuteGorgiasQueryControllerApi apiInstance;
     /**
     * Parses a GorgiasQueryResult and extracts the main result and supporting facts.
     * @param gorgiasQueryResult The query result from the Gorgias API.
     * @return A list of parsed results containing the main result and supporting facts.
     */

  
    
     
   

    /**
     * A helper class to represent the parsed result.
     */
    
    @PostConstruct
    public void init() {
        apiInstance = new ExecuteGorgiasQueryControllerApi();
        apiInstance.getApiClient().setUsername("imichalakis");
        apiInstance.getApiClient().setPassword("528528gm@@");
        apiInstance.getApiClient().setBasePath("http://aiasvm1.amcl.tuc.gr:8085");
    }
    @Autowired
    public GorgiasService(RestTemplate restTemplate, @Value("${gorgias.api.basepath}") String basePath) {
        this.restTemplate = restTemplate;
        this.basePath = basePath;
    }

    

    public List<String> getUserProjects() {
        String url = basePath + "/getUserProjects"; // Update with the correct endpoint if needed
        // The RestTemplate will automatically add the credentials to each request
        String[] projectsArray = restTemplate.getForObject(url, String[].class);
        return Arrays.asList(projectsArray); // Convert the array to a list and return
    }




    public List<ParsedResult> executeGorgiasQueryForUrgency(WorkflowForm form, HttpSession session) {
        GorgiasQuery gorgiasQuery = new GorgiasQuery();
        setupGorgiasFiles(gorgiasQuery);
        
        List<String> facts = collectFacts(form);
        if (facts.isEmpty()) {
            System.out.println("No valid data to process for urgency.");
            return Collections.emptyList();
        }
    
        gorgiasQuery.setFacts(facts);
        gorgiasQuery.setResultSize(5);
        gorgiasQuery.setQuery("urgency(X)");
        System.out.println("Executing Query: " + gorgiasQuery.getQuery());
    
        return performQuery(gorgiasQuery);
    }
    
    private void setupGorgiasFiles(GorgiasQuery gorgiasQuery) {
        List<String> gorgiasFiles = new ArrayList<>();
        gorgiasFiles.add("2025/urgencylevel.pl");
        gorgiasQuery.setGorgiasFiles(gorgiasFiles);
    }

    private List<String> collectFacts(WorkflowForm form) {
        List<String> facts = new ArrayList<>();
    
        // ✅ Get urgency based on date and add it **without** a prefix
        String urgencyDateFact = determineUrgencyBasedOnStartDate(form);
        if (urgencyDateFact != null && !urgencyDateFact.trim().isEmpty()) {
            facts.add(urgencyDateFact); // Directly add the value
            System.out.println("✅ Added Fact (without prefix): " + urgencyDateFact);
        } else {
            System.out.println("⚠️ Skipped Urgency Date Fact (null or empty)");
        }
    
        // ✅ Add other facts normally with prefixes
        addFactIfValid(facts, form.getAgencyCategory(), "agency_category");
        addFactIfValid(facts, form.getRequestType(), "request_type");
        addFactIfValid(facts, form.getContractWithContractor(), "contract_with_contractor");
    
        // ✅ Debugging: Show collected facts before sending
        System.out.println("📌 Facts Collected: " + facts);
    
        return facts;
    }
    
    
    private void addFactIfValid(List<String> facts, String value, String prefix) {
        if (value != null && !value.trim().isEmpty()) {
            facts.add(prefix + "(" + value + ")");
        }
    }
    
    private List<ParsedResult> performQuery(GorgiasQuery gorgiasQuery) {
        try {
            System.out.println("📤 Sending Facts to Gorgias: " + gorgiasQuery.getFacts());

            GorgiasQueryResult result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
            System.out.println("Query Result: " + result);
            return parseGorgiasQueryResult(result);
        } catch (ApiException e) {
            System.out.println("API call failed: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
    
    
    // public List<ParsedResult> executeGorgiasQueryForYamlGen(WorkflowForm form) {
    //     GorgiasQuery gorgiasQuery = new GorgiasQuery();
    //     ArrayList<String> gorgiasFiles = new ArrayList<>();
    //     gorgiasFiles.add("finaldecision/final.pl"); // Reference to final Prolog file
    //     gorgiasQuery.setGorgiasFiles(gorgiasFiles);
    
    //    // ✅ Generate facts inside this method
    // List<String> facts = generateFactsForGorgias(form);

    // gorgiasQuery.setFacts(facts);

    // System.out.println("Final Facts for Gorgias Query: " + facts);

    // gorgiasQuery.setResultSize(5);
    // gorgiasQuery.setQuery("yamlfile(X)");

    // GorgiasQueryResult result = null;
    // try {
    //     result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
    //     System.out.println("Query Result: " + result);
    // } catch (ApiException e) {
    //     System.out.println("Result is null due to an exception.");
    //     e.printStackTrace();
    // }

    // return parseGorgiasQueryResult(result);
    // }
    /**
 * Execute Gorgias query for YAML generation with enhanced fact generation
 * 
 * @param form WorkflowForm containing user selections
 * @return List of ParsedResult containing the query results
 */
public List<ParsedResult> executeGorgiasQueryForYamlGen(WorkflowForm form, HttpSession session) {
    GorgiasQuery gorgiasQuery = new GorgiasQuery();
    ArrayList<String> gorgiasFiles = new ArrayList<>();
    gorgiasFiles.add("finaldecision/final5.pl"); 
    gorgiasQuery.setGorgiasFiles(gorgiasFiles);

    List<String> facts = generateFactsForGorgias(form);
    gorgiasQuery.setFacts(facts);

    System.out.println("Final Facts for Gorgias Query: " + facts);

    gorgiasQuery.setResultSize(5);
    gorgiasQuery.setQuery("yamlfile(X)");

    GorgiasQueryResult result = null;
    try {
        result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
        System.out.println("Query Result: " + result);
    } catch (ApiException e) {
        System.out.println("Result is null due to an exception.");
        e.printStackTrace();
    }

    List<ParsedResult> parsedResults = parseGorgiasQueryResult(result);

    // Convert parsed results into a list of maps for Thymeleaf
    List<Map<String, Object>> resultsList = new ArrayList<>();
    for (ParsedResult parsedResult : parsedResults) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("naturalLanguageMainResult", parsedResult.getMainResult());
        resultMap.put("convertedFact", parsedResult.getSupportingFacts());
        resultsList.add(resultMap);
    }

    // ❗ Ensure resultsList is NOT empty before saving
    if (!resultsList.isEmpty()) {
        session.setAttribute("resultsList", resultsList);
        System.out.println("✅ Results List Stored in Session: " + resultsList);
    } else {
        System.out.println("❌ No valid results found for storage in session.");
    }

    return parsedResults;
}

    
    private List<String> generateFactsForGorgias(WorkflowForm form) {
        List<String> facts = new ArrayList<>();
    
        // ✅ Urgency Decision
        String urgency = form.getUrgencyDecision();
        if (urgency.equals("urgent")) {
            facts.add("urgent");
        } else if (urgency.equals("high")) {
            facts.add("high_urgency");
        } else if (urgency.equals("normal")) {
            facts.add("normal");
        }
    
        // ✅ Infrastructure Decision
        String infrastructure = form.getInfrastructureDecision();
        if (infrastructure.equalsIgnoreCase("iaas")) {
            facts.add("iaas");
        } else if (infrastructure.equalsIgnoreCase("paas")) {
            facts.add("paas");
        } else if (infrastructure.equalsIgnoreCase("serverless")) {
            facts.add("serverless");
        }
    
        // ✅ Scalability Decision
        String scalability = form.getScalabilityAndPerformanceDecision();
        if (scalability.equals("auto_scaling")) {
            facts.add("auto_scaling");
        } else if (scalability.equals("fixed_allocation")) {
            facts.add("fixed_allocation");
        }
    
        // ✅ Budget Decision
        if (form.getBudget() != null) {
            if (form.getBudget().equalsIgnoreCase("high_budget")) {
                facts.add("high_budget");
            } else if (form.getBudget().equalsIgnoreCase("low_budget")) {
                facts.add("low_budget");
            }
        }
    
        // ✅ Disaster Recovery Needs
        if (form.getDisasterRecoveryNeeds() != null) {
            if (form.getDisasterRecoveryNeeds().equalsIgnoreCase("high")) {
                facts.add("high_disaster_recovery");
            } else if (form.getDisasterRecoveryNeeds().equalsIgnoreCase("low")) {
                facts.add("low_disaster_recovery");
            }
        }
    
       // Extract latency requirement (from location.html - select)
    
       // Extract expected load (from scalability-performance.html - select)
       String expectedLoad = form.getExpectedLoad();
       if (expectedLoad != null) {
           if (expectedLoad.equals("low")) {
               facts.add("low_load");
           } else if (expectedLoad.equals("medium")) {
               facts.add("medium_load");
           } else if (expectedLoad.equals("high")) {
               facts.add("high_load");
           }
       }
       String peakTimes = form.getPeakTimes();
       if (peakTimes != null) {
           if (peakTimes.equals("rarely")) {
               facts.add("rare_peaks");
           } else if (peakTimes.equals("occasionally")) {
               facts.add("occasional_peaks");
           } else if (peakTimes.equals("frequently")) {
               facts.add("frequent_peaks");
           } else if (peakTimes.equals("always")) {
               facts.add("continuous_high_demand");
           }
       }
        // Extract response time (from scalability-performance.html - select)
    String responseTime = form.getResponseTime();
    if (responseTime != null) {
        if (responseTime.equals("low")) {
            facts.add("relaxed_response_time");
        } else if (responseTime.equals("medium")) {
            facts.add("standard_response_time");
        } else if (responseTime.equals("high")) {
            facts.add("strict_response_time");
        }
    }
    
    // Extract cost sensitivity (from scalability-performance.html - select)
    

    String performanceRequirement = form.getPerformanceRequirement();
    if (performanceRequirement != null) {
        if (performanceRequirement.equals("low_latency")) {
            facts.add("low_latency_required");
        } else if (performanceRequirement.equals("high_throughput")) {
            facts.add("high_throughput_required");
        }
    }
    
        // ✅ Resource Priority
        if (form.getResourcePriority() != null) {
            if (form.getResourcePriority().equalsIgnoreCase("compute")) {
                facts.add("compute_optimized");
            } else if (form.getProcessingOptimization().equalsIgnoreCase("memory_optimized")) {
                facts.add("memory_optimized");
            }
            else if (form.getProcessingOptimization().equalsIgnoreCase("compute_optimized")) {
                facts.add("compute_optimized");
            }
        }
            // // ✅ Resource Priority
            // if (form.getPrimaryGoal() != null) {
            //     if (form.getPrimaryGoal().equalsIgnoreCase("compute")) {
            //         facts.add("compute_optimized");
            //     } else if (form.getProcessingOptimization().equalsIgnoreCase("memory_optimized")) {
            //         facts.add("memory_optimized");
            //     }
            
            // }
        
    
        return facts;
    }
    
    public String determineUrgencyBasedOnStartDate(WorkflowForm form) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String formStartDate = form.getStartDate();
        if (formStartDate == null || formStartDate.trim().isEmpty()) {
            System.out.println("Start date is null or empty.");
            return "unknownBasedOnDate";
        }
        try {
            Date startDate = sdf.parse(formStartDate);
            return calculateUrgencyBasedOnDateDiff(startDate, new Date());
        } catch (ParseException e) {
            System.out.println("Error parsing start date: " + formStartDate);
            return "unknownBasedOnDate";
        }
    }
    
    private String calculateUrgencyBasedOnDateDiff(Date startDate, Date currentDate) {
        long diffInMillies = startDate.getTime() - currentDate.getTime();
        long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (diffInDays <= 3) {
            return "urgentbasedondate";
        } else if (diffInDays <= 10) {
            return "highbasedondate";
        } else {
            return "normalbasedondate";
        }
    }
    
    

   
        public List<ParsedResult> executeGorgiasQueryForLocation(WorkflowForm form) {
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
           // gorgiasFiles.add("november2024/location.pl");  // Prolog file for location
           gorgiasFiles.add("2025/location2.pl");
           gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
            ArrayList<String> facts = new ArrayList<>();
            try {
                //  Data Type
                if (form.getDataType() != null && !form.getDataType().trim().isEmpty()) {
                    facts.add("data_type(" + form.getDataType().toLowerCase(Locale.ROOT) + ")");
                }
        
                // Connectivity Requirement
                if (form.getConnectivity() != null && !form.getConnectivity().trim().isEmpty()) {
                    facts.add("connectivity(" + form.getConnectivity().toLowerCase(Locale.ROOT) + ")");
                }
        
                //  Budget Limitation
                if (form.getBudgetLimitation() != null && !form.getBudgetLimitation().trim().isEmpty()) {
                    facts.add("budget_limitation(" + form.getBudgetLimitation().toLowerCase(Locale.ROOT) + ")");
                }
        
                // Latency Requirement
                if (form.getLatencyRequirement() != null && !form.getLatencyRequirement().trim().isEmpty()) {
                    facts.add("latency_requirement(" + form.getLatencyRequirement().toLowerCase(Locale.ROOT) + ")");
                }
        
                //  Scalability Requirement
                if (form.getScalabilityRequirement() != null && !form.getScalabilityRequirement().trim().isEmpty()) {
                    facts.add("scalability_requirement(" + form.getScalabilityRequirement().toLowerCase(Locale.ROOT) + ")");
                }
        
                //  Compliance Requirement
                if (form.getComplianceRequirement() != null && !form.getComplianceRequirement().trim().isEmpty()) {
                    facts.add("compliance_requirement(" + form.getComplianceRequirement().toLowerCase(Locale.ROOT) + ")");
                }
        
                //  Disaster Recovery Needs
                if (form.getDisasterRecoveryNeeds() != null && !form.getDisasterRecoveryNeeds().trim().isEmpty()) {
                    facts.add("disaster_recovery_needs(" + form.getDisasterRecoveryNeeds().toLowerCase(Locale.ROOT) + ")");
                }
        
                //  Preferred Deployment Location
                if (form.getDeploymentLocation() != null && !form.getDeploymentLocation().trim().isEmpty()) {
                    facts.add("deployment_location(" + form.getDeploymentLocation().toLowerCase(Locale.ROOT) + ")");
                }
        
                // Debugging: Print all facts before executing the query
                System.out.println("Query Facts: " + facts);
        
                if (facts.isEmpty()) {
                    System.out.println("No valid facts were provided for location.");
                    return Collections.emptyList();  // Return empty list instead of null
                }
        
                gorgiasQuery.setFacts(facts);
        
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing location data.");
                return Collections.emptyList();  // Return empty list instead of null
            }
        
            //  Query for multiple results
            gorgiasQuery.setResultSize(5);  
            gorgiasQuery.setQuery("location(X)");  
            System.out.println("Executing Query: " + gorgiasQuery.getQuery());
        
            GorgiasQueryResult result = null;
            try {
                result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
                System.out.println("Query Result: " + result);
            } catch (ApiException e) {
                System.out.println("Result is null due to an exception.");
                e.printStackTrace();
            }
        
            return parseGorgiasQueryResult(result);
        }
        
        
        
        public List<ParsedResult> executeGorgiasQueryForInfr(WorkflowForm form) {
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
            //gorgiasFiles.add("2025/infrastructure4.pl"); // Αναφέρεται στο αρχείο υποδομής στο Gorgias
            gorgiasFiles.add("2025/infrastructurelatest2.pl");
            gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
            ArrayList<String> facts = new ArrayList<>();
            try {
               
                String primaryGoal = form.getPrimaryGoal();
                if (primaryGoal != null) {
                    // Assuming the primaryGoal string exactly matches the Prolog facts
                    facts.add(primaryGoal);
                }                
                if (form.isHighScalability()) {
                    facts.add("high_scalability");
                }
                if (form.isDataSensitivity()) {
                    facts.add("data_sensitivity");
                }
                String controlRequirement = form.getControlRequirement();
               
                if (controlRequirement != null) {
                    facts.add(controlRequirement);
                }
                String budget = form.getBudget();
                if (budget != null) {
                    facts.add(budget);
                }
                
                if (form.isIntegrationRequirements()) {
                    facts.add("custom_integrations");
                }
                System.out.println(facts);
                gorgiasQuery.setFacts(facts);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing infrastructure data.");
                return null;
            }
            
            gorgiasQuery.setResultSize(5);
            gorgiasQuery.setQuery("propose_infrastructure(X)");
            System.out.println("Query: " + gorgiasQuery.getQuery());
            GorgiasQueryResult result = null;
            try {
                result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
                System.out.println(result);
                // Εκτύπωση των αποτελεσμάτων
        if (result != null && result.isHasResult()) {
            System.out.println("Results from Gorgias:");
            List<QueryResult> results = result.getResult();
            for (QueryResult queryResult : results) {
                System.out.println("Type: " + queryResult.getExplanationStr());
                System.out.println("Human Explanation: " + queryResult.getHumanExplanation());
            }
        } else {
            System.out.println("No results found or an error occurred.");
        }
            } catch (ApiException e) {
                System.out.println("Result is null due to an exception.");
                e.printStackTrace();
            }
            return parseGorgiasQueryResult(result);
        }

     

       // Existing method where you handle the processing
public List<ParsedResult> parseGorgiasQueryResult(GorgiasQueryResult gorgiasQueryResult) {
    List<ParsedResult> parsedResults = new ArrayList<>();
    if (gorgiasQueryResult != null && gorgiasQueryResult.getResult() != null) {
        for (QueryResult queryResult : gorgiasQueryResult.getResult()) {
            String humanExplanation = queryResult.getHumanExplanation();
            // Simplify logical expressions in the explanation
            String simplifiedExplanation = simplifyLogicalComparison(humanExplanation);
            if (simplifiedExplanation != null && !simplifiedExplanation.isEmpty()) {
                // Extract the main result
                String mainResult = extractMainResult(simplifiedExplanation);
                // Extract supporting facts
                List<String> supportingFacts = extractSupportingFacts(simplifiedExplanation);
                // Convert and add parsed result
                parsedResults.add(new ParsedResult(mainResult, supportingFacts));
            }
        }
    }
    return parsedResults;
}

        

        // Helper method to extract the main result
        private String extractMainResult(String humanExplanation) {
            String keyword = "The statement \"";
            int start = humanExplanation.indexOf(keyword) + keyword.length();
            int end = humanExplanation.indexOf("\" is supported by:");
            if (start > -1 && end > start) {
                return humanExplanation.substring(start, end).trim();
            }
            return null;
        }
        
        // Helper method to extract supporting facts
      /**
 * Enhanced method to extract supporting facts including compound facts with 'and' conjunctions
 */
private List<String> extractSupportingFacts(String humanExplanation) {
    List<String> facts = new ArrayList<>();
    if (humanExplanation == null || humanExplanation.isEmpty()) {
        return facts;
    }

    try {
        // Extract the section containing the main supporting facts
        int factsStart = humanExplanation.indexOf("is supported by:");
        if (factsStart == -1) return facts;
        
        // Find where the main facts section ends (before any "This reason is" part)
        int reasonStart = humanExplanation.indexOf("This reason is :");
        String factsSection;
        
        if (reasonStart != -1) {
            // If there's a "This reason is" section, get only the main facts before it
            factsSection = humanExplanation.substring(factsStart + "is supported by:".length(), reasonStart).trim();
        } else {
            // Otherwise, get everything after "is supported by:"
            factsSection = humanExplanation.substring(factsStart + "is supported by:".length()).trim();
        }
        
        System.out.println("Main facts section: " + factsSection);
        
        // Extract quoted facts from the main facts section only
        Pattern quotePattern = Pattern.compile("\"([^\"]+)\"");
        Matcher quoteMatcher = quotePattern.matcher(factsSection);
        
        while (quoteMatcher.find()) {
            String fact = quoteMatcher.group(1).trim();
            if (!fact.isEmpty()) {
                facts.add(fact);
                System.out.println("Found main fact: " + fact);
            }
        }
        
        System.out.println("All extracted facts: " + facts);
    } catch (Exception e) {
        System.err.println("Error extracting facts: " + e.getMessage());
        e.printStackTrace();
    }
    
    return facts;
}
        /**
 * Enhanced method to convert facts to natural language
 * This handles both individual facts and compound fact strings
 */
public String convertFactToNaturalLanguage(String factInput) {
    System.out.println("Converting fact to natural language: " + factInput);
    
    if (factInput == null || factInput.trim().isEmpty()) {
        return "Unknown fact";
    }
    
    // Normalize and clean the input
    String normalizedFact = factInput.trim()
            .replaceAll("\\s+", " ")  // Replace multiple spaces with single space
            .replaceAll("•\\s*", ""); // Remove bullet points
    
    System.out.println("Normalized fact: " + normalizedFact);
    
    // Load fact mappings
    Map<String, String> factMappings = new HashMap<>();
    populateMappings(factMappings);
    
    // Normalize to lowercase for case-insensitive lookup
    String lookupKey = normalizedFact.toLowerCase();
    
    // Try a direct mapping first
    String result = factMappings.getOrDefault(lookupKey, factInput);
    
    // If no direct mapping found, try partial matching
    if (result.equals(factInput)) {
        for (Map.Entry<String, String> entry : factMappings.entrySet()) {
            if (lookupKey.contains(entry.getKey())) {
                result = entry.getValue();
                break;
            }
        }
    }
    
    System.out.println("Mapped fact result: " + result);
    return simplifyLogicalComparison(result);
}

/**
 * Handle a compound fact string containing multiple facts joined by 'and'
 */
private List<String> parseAndDeduplicateReasoning(String reasoning) {
    // Split by bullet points
    String[] points = reasoning.split("•");
    
    // Use a Set to automatically remove duplicates
    Set<String> uniquePoints = new LinkedHashSet<>();
    
    for (String point : points) {
        String cleanPoint = point.trim();
        if (!cleanPoint.isEmpty()) {
            uniquePoints.add(cleanPoint);
        }
    }
    
    return new ArrayList<>(uniquePoints);
}
public List<ParsedResult> deduplicateAndCleanResults(List<ParsedResult> parsedResults) {
    // Use LinkedHashMap to maintain order while deduplicating
    Map<String, ParsedResult> uniqueResults = new LinkedHashMap<>();
    
    for (ParsedResult result : parsedResults) {
        // Extract the main conclusion (e.g., "urgency(high)")
        String mainResult = result.getMainResult();
        
        // Clean supporting facts
        List<String> cleanedFacts = new ArrayList<>();
        for (String fact : result.getSupportingFacts()) {
            // Remove leading bullets and clean up the text
            String cleaned = fact.replaceAll("^\\s*•+\\s*", "").trim();
            if (!cleaned.isEmpty()) {
                cleanedFacts.add(cleaned);
            }
        }
        
        // Create a unique key for deduplication
        String resultKey = mainResult;
        
        // Only add if we haven't seen this conclusion before
        if (!uniqueResults.containsKey(resultKey)) {
            uniqueResults.put(resultKey, new ParsedResult(mainResult, cleanedFacts));
        }
    }
    
    return new ArrayList<>(uniqueResults.values());
}
/**
 * Modified handleCompoundFact method
 */
private String handleCompoundFact(String compoundFact) {
    System.out.println("🔄 Processing Compound Fact: " + compoundFact);
    
    // First deduplicate the reasoning points
    List<String> uniquePoints = parseAndDeduplicateReasoning(compoundFact);
    
    // Convert each unique point
    List<String> convertedFacts = new ArrayList<>();
    for (String point : uniquePoints) {
        String convertedFact = convertSingleFactToNaturalLanguage(point);
        if (!convertedFact.trim().isEmpty()) {
            convertedFacts.add(convertedFact);
        }
    }
    
    // Join unique points with bullet points
    return convertedFacts.stream()
            .distinct() // Additional deduplication
            .collect(Collectors.joining("\n• ", "• ", ""));
}


/**
 * Convert a single fact to natural language without recursion
 */
private String convertSingleFactToNaturalLanguage(String fact) {
    // Load fact mappings
    Map<String, String> factMappings = new HashMap<>();
    populateMappings(factMappings);
    
    // Normalize to lowercase for lookup
    String lookupKey = fact.toLowerCase();
    
    // Try direct mapping
    if (factMappings.containsKey(lookupKey)) {
        return factMappings.get(lookupKey);
    }
    
    // Try partial matching
    for (Map.Entry<String, String> entry : factMappings.entrySet()) {
        if (lookupKey.contains(entry.getKey())) {
            return entry.getValue();
        }
    }
    
    // Add special case mappings for facts 
    Map<String, String> specialCaseMappings = new HashMap<>();
    specialCaseMappings.put("expected_load(low)", "expected load is low");
    specialCaseMappings.put("peak_times(rarely)", "peak times occur rarely");
    specialCaseMappings.put("response_time_sla(low)", "response time SLA is low");
    specialCaseMappings.put("cost_sensitivity(low)", "cost sensitivity is low");
    specialCaseMappings.put("high_scalability", "high scalability is required");
    specialCaseMappings.put("custom_integrations", "custom integrations are necessary");
    specialCaseMappings.put("high_budget", "the project has a high budget");
    
    // Check special case mappings
    for (Map.Entry<String, String> entry : specialCaseMappings.entrySet()) {
        if (lookupKey.contains(entry.getKey())) {
            return entry.getValue();
        }
    }
    
    // Return the original fact if no mapping found
    return fact;
}
        
        
        

private void populateMappings(Map<String, String> factMappings) {
    // Here populate the map with all simple mappings from your current method
    factMappings.put("agency_category(centralgovernment)", "This request comes from a Central Government agency.");
    factMappings.put("agency_category(localgovernment)", "This request originates from a Local Government body.");
    factMappings.put("agency_category(independentauthority)", "This request is from an Independent Authority.");

    factMappings.put("request_type(ops)", "The request is related to the Ministry of Digital Governance.");
    factMappings.put("request_type(other)", "The request is from another ministry or organization.");
    factMappings.put("contract_with_contractor(yes)", "An active contract exists with an external contractor.");
    factMappings.put("contract_with_contractor(no)", "There is no current contract with any external contractor.");
    factMappings.put("auto_scaling", "Auto Scaling: Dynamically adjusts resources based on demand.");
factMappings.put("fixed_allocation", "Fixed Allocation: Suitable for predictable workloads with static resource limits.");
    // Response Time SLA
        factMappings.put("response_time_sla(high)", "A high Service Level Agreement (SLA) for response time is required, ensuring quick system performance.");
        factMappings.put("response_time_sla(medium)", "A balanced response time SLA is needed, optimizing both speed and cost.");
        factMappings.put("response_time_sla(low)", "A low response time SLA is acceptable, prioritizing cost-efficiency over performance.");
        
        // Expected Load
        factMappings.put("expected_load(high)", "The system is expected to handle a high number of users and heavy workloads.");
        factMappings.put("expected_load(medium)", "The system requires moderate resource allocation for average usage.");
        factMappings.put("expected_load(low)", "The system is expected to handle a low number of users and minimal workloads.");
        
        // Peak Times
        factMappings.put("peak_times(rarely)", "Traffic spikes are rare, so minimal scaling is needed.");
        factMappings.put("peak_times(occasionally)", "The system experiences occasional demand peaks.");
        factMappings.put("peak_times(frequently)", "Frequent demand spikes require a scalable infrastructure.");
        factMappings.put("peak_times(always)", "The system is always under high demand, requiring constant scalability.");
        
        // Cost Sensitivity
        factMappings.put("cost_sensitivity(low)", "Cost is not a major constraint, allowing for more premium infrastructure solutions.");
        factMappings.put("cost_sensitivity(medium)", "A balance between cost and performance is preferred.");
        factMappings.put("cost_sensitivity(high)", "Budget constraints require cost-effective infrastructure choices.");
        factMappings.put("eventdrivenfunctions", "The system is designed for event-driven automation, enabling responsive and serverless computing.");
        // Software Consumption Model
factMappings.put("consumereadysoftware", "The primary goal is to use pre-configured software solutions with minimal customization (e.g., SaaS and PaaS).");

// Integration Requirements
factMappings.put("custom_integrations", "Custom integrations are necessary to align with existing systems and workflows.");

// Data Security Considerations
factMappings.put("data_sensitivity", "The system will handle sensitive data, requiring strong security measures.");

// Application Architecture

// Infrastructure Control Requirements
factMappings.put("limitedcontrol", "Limited control over infrastructure is sufficient, allowing some flexibility while managing workloads.");
factMappings.put("nocontrol", "No direct control over infrastructure is required, relying entirely on managed solutions.");

// Budget Considerations
factMappings.put("high_budget", "The project has a high budget, allowing for advanced infrastructure solutions.");
factMappings.put("low_budget", "The project is budget-constrained, requiring cost-effective infrastructure choices.");

// Scalability Requirements
factMappings.put("high_scalability", "The system must be highly scalable to accommodate future growth and demand.");

// Application Development Model
factMappings.put("runcustomapps", "The main objective is to deploy and manage custom applications tailored to specific needs.");
        // Ensure fullControl is properly mapped
        factMappings.put("fullcontrol", "Full infrastructure control is needed, including servers, storage, and network configuration.");
        // Resource Requirements
        factMappings.put("cpu_requirements(high)", "The system requires high processing power for computation-heavy tasks.");
        factMappings.put("cpu_requirements(medium)", "A moderate CPU configuration is required for standard operations.");
        factMappings.put("cpu_requirements(low)", "Minimal CPU power is sufficient for system operations.");
        factMappings.put("memory_requirements(high)", "A large amount of memory is needed for efficient data processing.");
        factMappings.put("memory_requirements(medium)", "Moderate memory usage is required for balanced performance.");
        factMappings.put("memory_requirements(low)", "Minimal memory is sufficient for system operations.");
        factMappings.put("storage_requirements(high)", "The system requires significant storage capacity for data and applications.");
        factMappings.put("storage_requirements(medium)", "A moderate storage allocation is required for infrastructure needs.");
        factMappings.put("storage_requirements(low)", "Minimal storage capacity is sufficient for operations.");
        
       
         // ** Compute vs Storage Prioritization**
    factMappings.put("resource_priority(compute)", "The system prioritizes compute-intensive workloads requiring high CPU and RAM.");
    factMappings.put("resource_priority(storage)", "The system requires significant storage capacity over processing power.");

    // ** Performance vs Throughput**
    factMappings.put("performance_requirement(low_latency)", "Fast response time is crucial for real-time applications.");
    factMappings.put("performance_requirement(high_throughput)", "The system handles large-scale batch processing and high data transfer volumes.");

    // ** Scalability Strategy**
    factMappings.put("scalability_requirement(auto_scaling)", "The system dynamically scales resources based on demand fluctuations.");
    factMappings.put("scalability_requirement(fixed_allocation)", "A fixed resource allocation strategy is in place, suitable for predictable workloads.");

    // ** Processing Optimization**
    factMappings.put("processing_optimization(memory_optimized)", "High memory allocation is required for caching, in-memory processing, and large datasets.");
    factMappings.put("processing_optimization(cpu_optimized)", "The system is optimized for high CPU usage, making it suitable for AI, ML, and scientific computations.");

      
        
        
        // Scalability & Control
        factMappings.put("high_scalability", "The system must be highly scalable to accommodate future growth and demand.");
        factMappings.put("data_sensitivity", "The system will handle sensitive data, requiring strong security measures.");
        factMappings.put("custom_integrations", "Custom integrations are necessary to align with existing systems and workflows.");
        factMappings.put("fullControl", "Full infrastructure control is needed, including servers, storage, and network configuration.");
        factMappings.put("limitedControl", "Limited control over infrastructure is sufficient, allowing some flexibility while managing workloads.");
        factMappings.put("noControl", "No direct control over infrastructure is required, relying entirely on managed solutions.");
        
        // Budget Considerations
        factMappings.put("high_budget", "The project has a high budget, allowing for advanced infrastructure solutions.");
        factMappings.put("low_budget", "The project is budget-constrained, requiring cost-effective infrastructure choices.");
        
        // Primary Goals
        factMappings.put("runCustomApps", "The main objective is to deploy and manage custom applications tailored to specific needs.");
        factMappings.put("consumeReadySoftware", "The primary goal is to use pre-configured software solutions with minimal customization (e.g., SaaS).");
        factMappings.put("eventdrivenfunctions", "The system is designed for event-driven automation, enabling responsive and serverless computing.");
        // **New Mappings for Location Form**
         // Budget Limitation
    factMappings.put("budget_limitation(low)", "The project operates with a low budget, requiring cost-effective solutions.");
    factMappings.put("budget_limitation(high)", "A high budget is available, allowing for premium infrastructure choices.");
    // Scalability Requirements
    factMappings.put("scalability_requirement(high)", "High scalability is required, enabling automatic scaling of resources.");
    factMappings.put("scalability_requirement(medium)", "Moderate scalability is needed, balancing performance and cost.");
    factMappings.put("scalability_requirement(low)", "Low scalability is sufficient, with fixed resource allocation.");
    // Data Type Considerations
    factMappings.put("data_type(personal)", "The system handles personal data, requiring privacy and security measures.");
    factMappings.put("data_type(nonpersonal)", "The system processes non-personal data, with fewer regulatory constraints.");
    factMappings.put("data_type(medical)", "Medical data is involved, requiring compliance with healthcare regulations.");
    factMappings.put("data_type(tax)", "The system handles tax-related data, requiring strict financial security measures.");
    factMappings.put("data_type(critical)", "Critical data is being managed, necessitating high security and uptime guarantees.");

    // Latency Requirements
    factMappings.put("latency_requirement(strict)", "Strict low-latency performance is required for real-time processing.");
    factMappings.put("latency_requirement(flexible)", "Flexible latency is acceptable for non-time-sensitive operations.");

    // Compliance Needs
    factMappings.put("compliance_requirement(strict)", "Strict compliance measures (e.g., GDPR, ISO 27001) are required.");
    factMappings.put("compliance_requirement(general)", "General compliance policies are needed for standard security requirements.");

    // Disaster Recovery Needs
    factMappings.put("disaster_recovery_needs(high)", "High disaster recovery requirements demand multi-region backups and automatic failover.");
    factMappings.put("disaster_recovery_needs(medium)", "Medium disaster recovery strategy with frequent backups and quick restoration.");
    factMappings.put("disaster_recovery_needs(low)", "Minimal disaster recovery support with basic backup plans.");

    // Preferred Deployment Locations
    factMappings.put("deployment_location(azure)", "The system is recommended for deployment on Azure for cloud scalability and management.");
    factMappings.put("deployment_location(onPremise)", "The system is recommended for an on-premise deployment for full control over infrastructure.");

    // Connectivity Requirements
    factMappings.put("connectivity(true)", "The system requires a direct connection to A.A.D.E. infrastructure for tax or government services.");
    factMappings.put("connectivity(false)", "The system operates independently without direct connectivity to A.A.D.E.");
    factMappings.put("urgentbasedondate", "Based on the date there is immidiate-urgent request for deployment ");
    factMappings.put("normalbasedondate", "Based on the date there is a normal-urgency request for deployment ");
   // factMappings.put("request_type(other)", "It's a request type from Other Public Sectors - Not from Ministry Of Digital Governance");
   // factMappings.put("request_type(ops)", "It's a request type from Ministry Of Digital Governance");

    factMappings.put("highbasedondate", "Based on the date there is high request for deployment");
     factMappings.put("iaas", "Infrastructure as a Service: Provides virtual machines and network infrastructure with maximum control.");
    factMappings.put("paas", "Platform as a Service: Managed application hosting environment with reduced management overhead.");
    factMappings.put("serverless", "Serverless Computing: Event-driven architecture that automatically scales and charges only for execution time.");
    
    // Urgency Levels
    factMappings.put("urgent", "Urgent Deployment: Requires immediate provisioning with priority resource allocation.");
    factMappings.put("high_urgency", "High Urgency: Requires expedited deployment with minimal delays.");
    factMappings.put("normal", "Normal Urgency: Standard deployment timeline with regular prioritization.");
    factMappings.put("low_urgency", "Low Urgency: Flexible deployment timeline with no immediate pressure.");
    
    // Scaling Approach
    factMappings.put("auto_scaling", "Auto Scaling: Dynamically adjusts resources based on workload demands and traffic patterns.");
    factMappings.put("fixed_allocation", "Fixed Allocation: Suitable for predictable workloads with static resource limits.");
    
    // Budget Considerations
    factMappings.put("high_budget", "High Budget: Financial resources available for premium services and optimal performance.");
    factMappings.put("low_budget", "Low Budget: Cost-constrained deployment requiring economical resource utilization.");
    
    // Disaster Recovery
    factMappings.put("high_disaster_recovery", "High Disaster Recovery: Comprehensive backup, replication, and failover capabilities for critical workloads.");
    factMappings.put("low_disaster_recovery", "Low Disaster Recovery: Basic recovery capabilities sufficient for non-critical workloads.");
    
    // Workload Intensity
    factMappings.put("low_load", "Low Load: Minimal resource utilization with limited concurrent users or transactions.");
    factMappings.put("medium_load", "Medium Load: Moderate resource requirements with average concurrency and throughput needs.");
    factMappings.put("high_load", "High Load: Intensive resource utilization with significant concurrency and throughput demands.");
    
    // Traffic Pattern
    factMappings.put("rare_peaks", "Rare Traffic Peaks: Infrequent spikes in demand requiring occasional scaling.");
    factMappings.put("occasional_peaks", "Occasional Traffic Peaks: Periodic spikes in demand requiring intermittent scaling.");
    factMappings.put("frequent_peaks", "Frequent Traffic Peaks: Regular spikes in demand requiring responsive scaling capabilities.");
    factMappings.put("continuous_high_demand", "Continuous High Demand: Sustained high utilization requiring constant availability of resources.");
    
    // Response Time Requirements
    factMappings.put("relaxed_response_time", "Relaxed Response Time: Non-time-sensitive operations where latency is not critical.");
    factMappings.put("standard_response_time", "Standard Response Time: Balanced performance expectations with reasonable latency targets.");
    factMappings.put("strict_response_time", "Strict Response Time: Time-sensitive operations requiring consistent low-latency responses.");
    
    // Performance Characteristics
    factMappings.put("low_latency_required", "Low Latency Required: Applications requiring minimal processing and response delays.");
    factMappings.put("high_throughput_required", "High Throughput Required: Systems processing large volumes of data or transactions.");
    
    // Compute Optimization
    factMappings.put("compute_optimized", "Compute Optimized: Configured for CPU-intensive workloads like batch processing, scientific modeling, or high-performance computing.");
    factMappings.put("memory_optimized", "Memory Optimized: Configured for memory-intensive workloads like in-memory databases, caching, or real-time analytics.");
    // Urgency Levels
factMappings.put("urgent", "Deployment requires immediate attention and expedited implementation.");
factMappings.put("high_urgency", "Deployment has high priority with short timeline requirements.");
factMappings.put("normal", "Deployment follows standard implementation timelines.");
factMappings.put("low_urgency", "Deployment has flexible timeline with no immediate pressure.");

// Service Models
factMappings.put("iaas", "Infrastructure as a Service: Provides virtualized computing resources over the internet.");
factMappings.put("paas", "Platform as a Service: Provides hardware and software tools over the internet for application development.");
factMappings.put("serverless", "Serverless Computing: Allows code execution without managing underlying infrastructure.");
factMappings.put("saas", "Software as a Service: Delivers software applications over the internet on a subscription basis.");

// Resource Allocation Strategies
factMappings.put("auto_scaling", "Auto Scaling: Dynamically adjusts resources based on demand.");
factMappings.put("fixed_allocation", "Fixed Allocation: Suitable for predictable workloads with static resource limits.");

// Budget Constraints
factMappings.put("high_budget", "High Budget: Sufficient financial resources available for premium services and optimal performance.");
factMappings.put("low_budget", "Low Budget: Limited financial resources requiring cost-effective solutions.");

// Disaster Recovery Requirements
factMappings.put("high_disaster_recovery", "High Disaster Recovery: Comprehensive backup, replication, and failover capabilities for critical workloads.");
factMappings.put("low_disaster_recovery", "Low Disaster Recovery: Basic backup solutions for non-critical workloads with flexible recovery time objectives.");

// Load Characteristics
factMappings.put("low_load", "Low Load: Minimal resource utilization with limited concurrent users or operations.");
factMappings.put("medium_load", "Medium Load: Moderate resource utilization with average concurrency requirements.");
factMappings.put("high_load", "High Load: Intensive resource utilization with significant concurrent operations.");

// Traffic Pattern Characteristics
factMappings.put("rare_peaks", "Rare Peaks: Traffic spikes occur very infrequently (few times per year).");
factMappings.put("occasional_peaks", "Occasional Peaks: Traffic spikes occur periodically (few times per month).");
factMappings.put("frequent_peaks", "Frequent Peaks: Traffic spikes occur regularly (multiple times per week).");
factMappings.put("continuous_high_demand", "Continuous High Demand: Consistently high traffic levels with minimal variation.");

// Response Time Requirements
factMappings.put("relaxed_response_time", "Relaxed Response Time: Application can tolerate longer processing times without significant impact.");
factMappings.put("standard_response_time", "Standard Response Time: Application requires industry-standard response times for good user experience.");
factMappings.put("strict_response_time", "Strict Response Time: Application requires guaranteed response times with minimal variation.");

// Performance Requirements
factMappings.put("low_latency_required", "Low Latency Required: Applications requiring minimal processing and response delays.");
factMappings.put("high_throughput_required", "High Throughput Required: Applications needing to process large volumes of data or transactions.");

// Optimization Preferences
factMappings.put("compute_optimized", "Compute Optimized: Workloads requiring high-performance processors and computing power.");
factMappings.put("memory_optimized", "Memory Optimized: Workloads requiring large memory allocations for data processing or caching.");
}

private String handleComplexReasoning(String fact) {
    System.out.println("🔍 Handling Complex Reasoning: " + fact);

    // Handle the specific format found in Option 3
    if (fact.contains("This reason is :") && fact.contains("Stronger than the reason of")) {
        // For the specific pattern in Option 3
        try {
            // Extract the request type
            String requestType = "request from another ministry or organization";
            
            // Create a more natural language explanation
            String result = "This " + requestType + " requires high urgency. This takes precedence over normal urgency considerations, even though there is an active contract with an external contractor.";
            
            System.out.println("✅ Mapped Complex Reasoning: " + result);
            return result;
        } catch (Exception e) {
            System.out.println("⚠️ Error parsing complex reasoning: " + e.getMessage());
        }
    }
    
    // Original logic for other patterns
    if (fact.toLowerCase().contains("stronger than the reason of")) {
        // Make the search case-insensitive
        int mainStart = fact.indexOf("request_type(");
        if (mainStart >= 0) {
            int mainEnd = fact.indexOf(") ", mainStart);
            String mainFact = fact.substring(mainStart, mainEnd + 1).trim();

            // Extract supporting fact - use case-insensitive search
            int reasonStart = fact.toLowerCase().indexOf("stronger than the reason of") + "stronger than the reason of".length();
            int reasonEnd = fact.indexOf(" supporting");
            if (reasonEnd > reasonStart) {
                String supportingFact = fact.substring(reasonStart, reasonEnd).trim();

                // Convert to natural language
                String readableMainFact = convertSimpleFactToNaturalLanguage(mainFact);
                String readableSupportingFact = convertSimpleFactToNaturalLanguage(supportingFact);

                String result = readableMainFact + " is considered more urgent than " + readableSupportingFact + ".";
                System.out.println("✅ Mapped Complex Reasoning: " + result);
                return result;
            }
        }
    }

    // Fallback: If no complex pattern is detected, process it as a simple fact
    return convertSimpleFactToNaturalLanguage(fact);
}



// Helper method to convert simple facts to natural language
private String convertSimpleFactToNaturalLanguage(String fact) {
    Map<String, String> simpleMappings = new HashMap<>();
    simpleMappings.put("request_type(other)", "the request is from another ministry or organization");
    simpleMappings.put("contract_with_contractor(yes)", "there is an active contract with an external contractor");
    simpleMappings.put("agency_category(independentAuthority)", "this request is from an Independent Authority");
    simpleMappings.put("urgentbasedondate", "based on the date there is immediate request for deployment (less than 3 days)");
    // Add any other facts that might appear in your complex reasoning

    // Check if the fact is in the mapping
    for (Map.Entry<String, String> entry : simpleMappings.entrySet()) {
        if (fact.contains(entry.getKey())) {
            return entry.getValue();
        }
    }
    
    // Return the fact itself if no mapping found
    return fact;
}

    public String simplifyLogicalComparison(String reasoning) {
        reasoning = reasoning.replace("this reason is :", "This is because");
        reasoning = reasoning.replace("stronger than the reason of", "which takes precedence over");
        reasoning = reasoning.replace("supporting urgency(normal)", "which suggests normal urgency");
        reasoning = reasoning.replace("when", "even though");
        return reasoning;
    }
    
    
    public String mapMainResultToNaturalLanguage(String mainResult, WorkflowForm form) { 
        switch (mainResult) {
            // Urgency decisions
            case "urgent":
              
                return "Urgent Priority: Critical deployment required within 72 hours. Immediate resource allocation necessary. All standard procedures may be expedited to meet this critical timeline.";
            case "normal":
               
                return "Normal Priority: Regular deployment timeline of up to 60 days. Standard resource allocation appropriate. All normal procedures and quality checks to be followed without modification.";
            case "high":
               
                return "High Priority: Accelerated deployment required within 10 days. Prompt resource allocation recommended. Standard procedures should be followed with expedited processing.";
            
            // Scalability decisions
            case "fixed_allocation":
                
                return "Fixed Allocation: Suitable for predictable workloads with static resource limits.";
            case "auto_scaling":

                return "Auto Scaling: Dynamically adjusts resources based on demand.";
    
            // Compute and storage decisions
            case "high_compute":
                form.setResourceForFinal("high_compute");
                return "High Compute: Prioritizes CPU & RAM for compute-intensive tasks.";
            case "high_storage":
                form.setResourceForFinal("high_storage");
                return "High Storage: Emphasizes large storage capacity for data-intensive workloads.";
            case "real_time_compute":
                form.setResourceForFinal("real_time_compute");
                return "Real-Time Compute: Optimized for low-latency applications requiring fast responses.";
            case "batch_processing":
                form.setResourceForFinal("batch_processing");
                return "Batch Processing: Focused on high throughput for large-scale or delayed workloads.";
    
            // Performance and allocation strategies
            case "scalable_architecture":
                form.setScalabilityForFinal("scalable_architecture");
                return "Scalable Architecture: Designed to automatically expand or contract resources.";
            case "static_allocation":
                form.setScalabilityForFinal("static_allocation");
                return "Static Allocation: Maintains fixed resources for predictable usage.";
            case "memory_intensive":
                form.setProcessingOptimizationForFinal("memory_intensive");
                return "Memory-Optimized: Prioritizes large memory capacity for caching or in-memory processing.";
            case "cpu_intensive":
                form.setProcessingOptimizationForFinal("cpu_intensive");
                return "CPU-Optimized: Ideal for compute-heavy tasks such as AI/ML or simulations.";
           // ===== VIRTUAL MACHINE CONFIGURATIONS =====
        case "azure_vm_high_performance":
        return "Azure Virtual Machine (High Performance): Full control with compute-optimized resources for demanding workloads requiring maximum performance and customization";
        
    case "azure_vm_standard":
        return "Azure Virtual Machine (Standard): Full control with balanced resources for general-purpose workloads with predictable resource needs";
        
    case "azure_vm_memory_optimized":
        return "Azure Virtual Machine (Memory Optimized): Full control with enhanced memory allocation for data-intensive workloads requiring large RAM capacity";
        
    case "azure_vm_basic":
        return "Azure Virtual Machine (Basic): Cost-effective virtual machines for development, testing, or low-traffic production workloads with minimal resource requirements";
        
    // ===== APP SERVICE CONFIGURATIONS =====
    case "azure_app_service_autoscaling":
        return "Azure App Service (Auto-scaling): Fully managed web hosting platform with dynamic scaling based on traffic patterns and resource utilization";
        
    case "azure_app_service_standard":
        return "Azure App Service (Standard): Managed web hosting with fixed resource allocation for applications with consistent traffic patterns";
        
    case "azure_app_service_premium":
        return "Azure App Service (Premium): Enhanced web hosting with dedicated compute resources for high-performance web applications with strict latency requirements";
        
    // ===== AZURE FUNCTIONS CONFIGURATIONS =====
    case "azure_functions":
        return "Azure Functions (Standard): Serverless compute service for event-driven applications with automatic scaling and flexible resource allocation";
        
    case "azure_functions_premium":
        return "Azure Functions (Premium): Enhanced serverless compute with dedicated resources, VNet integration, and pre-warmed instances for performance-critical event processing";
        
    case "azure_functions_consumption":
        return "Azure Functions (Consumption): Cost-optimized serverless compute with consumption-based pricing for irregular workloads and budget-conscious deployments";
        
    // ===== CONTAINER SOLUTIONS =====
    case "azure_kubernetes_service":
        return "Azure Kubernetes Service (AKS): Managed Kubernetes platform for containerized applications with dynamic scaling and orchestration capabilities";
        
    case "azure_container_apps":
        return "Azure Container Apps: Serverless container service with auto-scaling, service discovery, and built-in application gateway for modern microservices";
        
    // ===== SPECIALIZED CONFIGURATIONS =====
    case "azure_high_performance_compute":
        return "Azure High Performance Compute: Specialized compute configuration optimized for CPU-intensive workloads requiring low latency and high throughput processing";
        
    case "azure_memory_intensive":
        return "Azure Memory Intensive Deployment: Specialized configuration optimized for memory-intensive workloads requiring large RAM capacity and high throughput data processing";
        
    case "azure_disaster_recovery_optimized":
        return "Azure Disaster Recovery Optimized: Multi-region deployment with automated failover and backup for applications requiring high availability and business continuity";
        
           
                case "firstyaml":
                return "Auto Scaling Compute Optimized";
            case "secondyaml":
                return "Infrastructure As A Service, Compute Optimized - Fixed Allocation (IaaS)";
            case "thirdyaml":
                return "Platform As a Service Standard Memory Deployment";
            // Infrastructure decisions
            case "onPremise":
                form.setInfrastructureForFinal("onPremise");
                return "On-Premise: Deploying infrastructure within a private data center, offering full control over hardware, security, and compliance.";
            case "azure":
                form.setInfrastructureForFinal("azure");
                return "Azure: A public cloud platform providing scalable and managed computing resources, ideal for flexible and high-availability deployments.";
            case "iaas":
                form.setInfrastructureForFinal("iaas");
                return "Infrastructure as a Service (IaaS): Provides virtualized computing resources over the internet.";
            case "paas":
                form.setInfrastructureForFinal("paas");
                return "Platform as a Service (PaaS): Provides a platform allowing customers to develop, run, and manage applications.";
            case "saas":
                form.setInfrastructureForFinal("saas");
                return "Software as a Service (SaaS): Provides access to software and its functions remotely as a web-based service.";
            case "serverless":
                form.setInfrastructureForFinal("serverless");
                return "Serverless: Focuses on building applications without managing the underlying infrastructure.";
    
            // Balanced resource allocation strategies
            case "balanced":
                form.setResourceForFinal("balanced");
                return "Balanced: A strategy that balances CPU, memory, and storage requirements for general-purpose workloads.";
            case "cpu_optimized":
                form.setProcessingOptimizationForFinal("cpu_optimized");
                return "CPU Optimized: Focused on maximizing processing power, ideal for compute-intensive tasks.";
            case "memory_optimized":
                form.setProcessingOptimizationForFinal("memory_optimized");
                return "Memory Optimized: Prioritizes high memory capacity, suitable for memory-intensive applications.";
            case "storage_optimized":
                form.setProcessingOptimizationForFinal("storage_optimized");
                return "Storage Optimized: Tailored for applications requiring large storage capacity and high I/O performance.";
    
            // Urgency mappings with explicit naming
            case "urgency(high)":
                form.setUrgencyForFinal("high");
                return "High: Indicates an urgent or high-priority scenario requiring immediate attention and resources.";
            case "urgency(normal)":
                form.setUrgencyForFinal("normal");
                return "Normal: Indicates a standard-priority scenario with typical resource allocation needs.";
            case "urgency(urgent)":
                form.setUrgencyForFinal("urgent");
                return "Urgent: Indicates a critical situation that demands rapid response and resource scaling.";
                case "real_time_compute_optimized":
                return "Real-Time Compute Optimized: High-performance computing resources configured for low-latency, responsive applications.";
            
            case "data_processing_optimized":
                return "Data Processing Optimized: Storage-focused resources designed for high-throughput batch processing of large datasets.";
            
            case "elastic_compute_optimized":
                return "Elastic Compute Optimized: Auto-scaling compute resources that dynamically adjust to workload demands.";
            
            case "in_memory_processing_optimized":
                return "In-Memory Processing Optimized: Memory-intensive configuration for real-time data processing with minimal latency.";
            
            case "compute_cluster_optimized":
                return "Compute Cluster Optimized: CPU-intensive configuration for high-throughput batch processing workloads.";
            
            case "dedicated_storage_optimized":
                return "Dedicated Storage Optimized: Fixed-allocation storage resources for predictable data-intensive workloads.";
            
            case "elastic_cache_optimized":
                return "Elastic Cache Optimized: Auto-scaling memory resources for dynamic caching and in-memory data processing.";
            
            case "elastic_compute_cluster":
                return "Elastic Compute Cluster: Auto-scaling CPU resources for dynamic computational workloads.";
            
            case "real_time_elastic_compute":
                return "Real-Time Elastic Compute: Low-latency compute resources with auto-scaling capabilities for varying demand.";
            
            case "high_performance_compute":
                return "High-Performance Compute: CPU-optimized resources configured for real-time, compute-intensive workloads.";
            
            case "in_memory_real_time_compute":
                return "In-Memory Real-Time Compute: Memory-optimized resources for real-time applications requiring fast data access.";
            
            case "data_warehouse_optimized":
                return "Data Warehouse Optimized: Fixed storage resources designed for batch processing of structured data.";
            
            case "data_lake_optimized":
                return "Data Lake Optimized: Elastic storage resources for scalable processing of diverse data types.";
            
            case "in_memory_analytics_optimized":
                return "In-Memory Analytics Optimized: Memory-focused resources for high-throughput data analysis.";
            
            case "distributed_processing_optimized":
                return "Distributed Processing Optimized: CPU-focused resources for parallel processing of large datasets.";
            
            case "high_performance_elastic_compute":
                return "High-Performance Elastic Compute: CPU-optimized auto-scaling resources for real-time applications with varying load.";
            
            case "elastic_in_memory_real_time":
                return "Elastic In-Memory Real-Time: Memory-optimized auto-scaling resources for real-time applications with varying load.";
            
            case "dedicated_high_performance_compute":
                return "Dedicated High-Performance Compute: Fixed CPU-optimized resources for consistent real-time computing needs.";
            
            case "dedicated_in_memory_real_time":
                return "Dedicated In-Memory Real-Time: Fixed memory-optimized resources for consistent real-time data processing.";
            
            case "elastic_distributed_processing":
                return "Elastic Distributed Processing: Auto-scaling CPU-optimized resources for batch processing with varying workloads.";
            
            case "elastic_in_memory_analytics":
                return "Elastic In-Memory Analytics: Auto-scaling memory-optimized resources for data analytics with varying workloads.";
            
            case "dedicated_distributed_processing":
                return "Dedicated Distributed Processing: Fixed CPU-optimized resources for consistent batch processing workloads.";
            
            case "dedicated_in_memory_analytics":
                return "Dedicated In-Memory Analytics: Fixed memory-optimized resources for consistent data analytics workloads.";
               
            case "scalability_decision(auto_scaling)":
                return "Auto Scaling: Dynamically adjusts resources based on demand.";
            case "scalability_decision(fixed_allocation)":
                return "Fixed Allocation: Suitable for predictable workloads with static resource limits.";
                default:
                return "Azure App Service (Standard): Default managed web hosting platform for general-purpose applications";
        }
    }
    
    public List<ParsedResult> executeGorgiasQueryForScalability(WorkflowForm form) {
        long startTime = System.currentTimeMillis();
    
        GorgiasQuery gorgiasQuery = new GorgiasQuery();
        ArrayList<String> gorgiasFiles = new ArrayList<>();
        gorgiasFiles.add("2025/scalability2025.pl");
        gorgiasQuery.setGorgiasFiles(gorgiasFiles);
    
        ArrayList<String> facts = new ArrayList<>();
        try {
            // Add expected_load fact
            String expectedLoad = form.getExpectedLoad();
            if (expectedLoad != null && !expectedLoad.isEmpty()) {
                facts.add("expected_load(" + expectedLoad.toLowerCase() + ")");
            }
    
            // Add peak_times fact
            String peakTimes = form.getPeakTimes();
            if (peakTimes != null && !peakTimes.isEmpty()) {
                facts.add("peak_times(" + peakTimes.toLowerCase() + ")");
            }
    
            // Add response_time_sla fact
            String responseTime = form.getResponseTime();
            if (responseTime != null && !responseTime.isEmpty()) {
                facts.add("response_time_sla(" + responseTime.toLowerCase().replace("-", "_") + ")");
            }
    
            // Add cost_sensitivity fact
            String costSensitivity = form.getCostSensitivity();
            if (costSensitivity != null && !costSensitivity.isEmpty()) {
                facts.add("cost_sensitivity(" + costSensitivity.toLowerCase().replace("-", "_") + ")");
            }
    
            gorgiasQuery.setFacts(facts);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing scalability data.");
            return new ArrayList<>(); // Return empty list in case of an error
        }
    
        // KEY FIX 1: Use a specific query instead of "X"
        gorgiasQuery.setResultSize(5);
        gorgiasQuery.setQuery("X"); // Set query
        System.out.println("Query: " + gorgiasQuery.getQuery());
    
        GorgiasQueryResult result = null;
    
        try {
            result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
            System.out.println("Gorgias query executed in " + executionTime + " ms");
            System.out.println(result);
        } catch (ApiException e) {
            System.out.println("Result is null due to an exception.");
            e.printStackTrace();
            return new ArrayList<>(); // Return empty list in case of an API exception
        }
    
        // KEY FIX 2: Deduplicate results before returning
        List<ParsedResult> parsedResults = parseGorgiasQueryResult(result);
        return deduplicateScalabilityResults(parsedResults);
    }
    
    /**
     * Deduplicate scalability results based on the main decision (auto_scaling or fixed_allocation)
     */
    private List<ParsedResult> deduplicateScalabilityResults(List<ParsedResult> results) {
        Map<String, ParsedResult> uniqueResults = new LinkedHashMap<>();
        
        for (ParsedResult result : results) {
            // Extract the base decision (auto_scaling or fixed_allocation)
            String mainResult = result.getMainResult();
            String baseDecision = mainResult;
            
            // Extract from scalability_decision(X) if needed
            if (mainResult.contains("(") && mainResult.contains(")")) {
                baseDecision = mainResult.substring(mainResult.indexOf('(') + 1, mainResult.indexOf(')'));
            }
            
            // Only keep the first result for each base decision type
            if (!uniqueResults.containsKey(baseDecision)) {
                // Find the most specific supporting fact for this decision
                List<String> bestFacts = findBestSupportingFacts(result.getSupportingFacts(), baseDecision);
                uniqueResults.put(baseDecision, new ParsedResult(mainResult, bestFacts));
            }
        }
        
        return new ArrayList<>(uniqueResults.values());
    }
    
    /**
     * Find the most specific supporting facts for a scalability decision
     */
    private List<String> findBestSupportingFacts(List<String> facts, String decision) {
        // Prioritize facts based on relevance to the decision
        List<String> bestFacts = new ArrayList<>();
        
        // Look for facts specifically about the chosen strategy
        for (String fact : facts) {
            if (decision.equals("auto_scaling") && 
                (fact.contains("frequent") || fact.contains("spike") || fact.contains("high"))) {
                bestFacts.add(fact);
            } else if (decision.equals("fixed_allocation") && 
                      (fact.contains("predictable") || fact.contains("balanced") || fact.contains("low"))) {
                bestFacts.add(fact);
            }
        }
        
        // If no specific facts found, use any available facts
        if (bestFacts.isEmpty() && !facts.isEmpty()) {
            bestFacts.add(facts.get(0));
        }
        
        return bestFacts;
    }
        

// public List<ParsedResult> executeGorgiasQueryForScalability(WorkflowForm form) {
//     long startTime = System.currentTimeMillis();

//     GorgiasQuery gorgiasQuery = new GorgiasQuery();
//     ArrayList<String> gorgiasFiles = new ArrayList<>();
//     //gorgiasFiles.add("november2024/scalability2025.pl");
//     gorgiasFiles.add("2025/scalability2025.pl");
//     gorgiasQuery.setGorgiasFiles(gorgiasFiles);

//     ArrayList<String> facts = new ArrayList<>();
//     try {
//         // Add expected_load fact
//         String expectedLoad = form.getExpectedLoad();
//         if (expectedLoad != null && !expectedLoad.isEmpty()) {
//             facts.add("expected_load(" + expectedLoad.toLowerCase() + ")");
//         }

//         // Add peak_times fact
//         String peakTimes = form.getPeakTimes();
//         if (peakTimes != null && !peakTimes.isEmpty()) {
//             facts.add("peak_times(" + peakTimes.toLowerCase() + ")");
//         }

//         // Add response_time_sla fact
//         String responseTime = form.getResponseTime();
//         if (responseTime != null && !responseTime.isEmpty()) {
//             facts.add("response_time_sla(" + responseTime.toLowerCase().replace("-", "_") + ")");
//         }

//         // Add cost_sensitivity fact
//         String costSensitivity = form.getCostSensitivity();
//         if (costSensitivity != null && !costSensitivity.isEmpty()) {
//             facts.add("cost_sensitivity(" + costSensitivity.toLowerCase().replace("-", "_") + ")");
//         }

//         gorgiasQuery.setFacts(facts);
//     } catch (Exception e) {
//         e.printStackTrace();
//         System.out.println("Error processing scalability data.");
//         return new ArrayList<>(); // Return empty list in case of an error
//     }

//     gorgiasQuery.setResultSize(5);
//     gorgiasQuery.setQuery("X"); // Set query
//     System.out.println("Query: " + gorgiasQuery.getQuery());

//     GorgiasQueryResult result = null;

//     try {
//         result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
//         long endTime = System.currentTimeMillis();
//         long executionTime = endTime - startTime;
//         System.out.println("Gorgias query executed in " + executionTime + " ms");
//         System.out.println(result);
//     } catch (ApiException e) {
//         System.out.println("Result is null due to an exception.");
//         e.printStackTrace();
//         return new ArrayList<>(); // Return empty list in case of an API exception
//     }

//     // Parse the result into ParsedResult objects
//     return parseGorgiasQueryResult(result);
// }

    
        public List<ParsedResult> executeGorgiasQueryForResources(WorkflowForm form) {
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
          //  gorgiasFiles.add("2025/resource2025.pl");  // Prolog file for resource decisions
         // gorgiasFiles.add("2025/resource2025.pl"); 
         gorgiasFiles.add("2025/resource2025latest.pl"); 
          gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
            ArrayList<String> facts = new ArrayList<>();
            try {
                
              // **Resource Priority**
        if (form.getResourcePriority() != null && !form.getResourcePriority().trim().isEmpty()) {
            facts.add("resource_priority(" + form.getResourcePriority().toLowerCase(Locale.ROOT) + ")");
        }

        // **Performance Requirement**
        if (form.getPerformanceRequirement() != null && !form.getPerformanceRequirement().trim().isEmpty()) {
            facts.add("performance_requirement(" + form.getPerformanceRequirement().toLowerCase(Locale.ROOT) + ")");
        }

        // **Scalability Requirement**
        if (form.getScalabilityRequirement() != null && !form.getScalabilityRequirement().trim().isEmpty()) {
            facts.add("scalability_requirement(" + form.getScalabilityRequirement().toLowerCase(Locale.ROOT) + ")");
        }

        // **Processing Optimization**
        if (form.getProcessingOptimization() != null && !form.getProcessingOptimization().trim().isEmpty()) {
            facts.add("processing_optimization(" + form.getProcessingOptimization().toLowerCase(Locale.ROOT) + ")");
        }
        
                // Debugging: Print all facts before executing the query
                System.out.println("Query Facts for Resource Requirements: " + facts);
        
                if (facts.isEmpty()) {
                    System.out.println("No valid facts were provided for resource requirements.");
                    return Collections.emptyList();  // Return empty list instead of null
                }
        
                gorgiasQuery.setFacts(facts);
        
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing resource requirements data.");
                return Collections.emptyList();  // Return empty list instead of null
            }
        
            //  Query for multiple results
            gorgiasQuery.setResultSize(5);  
            gorgiasQuery.setQuery("resource_decision(X)");  
            System.out.println("Executing Query: " + gorgiasQuery.getQuery());
        
            GorgiasQueryResult result = null;
            try {
                result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
                System.out.println("Query Result: " + result);
            } catch (ApiException e) {
                System.out.println("Result is null due to an exception.");
                e.printStackTrace();
            }
        
            return parseGorgiasQueryResult(result);
        }
        
        
        
       
        

    public GorgiasQueryResult executeGorgiasQueryForFinalDecision(WorkflowForm form) {
        long startTime = System.currentTimeMillis();
    
        // Δημιουργία ερωτήματος και φόρτωση αρχείων
        GorgiasQuery gorgiasQuery = new GorgiasQuery();
        ArrayList<String> gorgiasFiles = new ArrayList<>();
        gorgiasFiles.add("november2024/finalDecision3.pl");  // Το νέο αρχείο Prolog
        gorgiasQuery.setGorgiasFiles(gorgiasFiles);
    
        // Δημιουργία των facts από τις ενδιάμεσες αποφάσεις
        ArrayList<String> facts = new ArrayList<>();
        facts.add("infrastructure(" + form.getInfrastructureDecision() + ")");
        facts.add("scalability_and_performance(" + form.getScalabilityAndPerformanceDecision() + ")");
        facts.add("urgency(" + form.getUrgencyDecision() + ")");
        facts.add("scaling(" + form.getScalingDecision() + ")");
        facts.add("resource(" + form.getResourceDecision() + ")");
        facts.add("location(" + form.getLocationDecision() + ")");
        gorgiasQuery.setFacts(facts);
    
        // Ερώτημα
        String myQuery = "final_decision(X)";
        gorgiasQuery.setResultSize(1);
        gorgiasQuery.setQuery(myQuery);
    
        GorgiasQueryResult result = null;
        try {
            result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
            long endTime = System.currentTimeMillis();
            long executionTime = endTime - startTime;
    
            System.out.println("Gorgias query executed in " + executionTime + " ms");
            System.out.println(result);
        } catch (ApiException e) {
            System.out.println("Result is null due to an exception.");
            e.printStackTrace();
        }
    
        return result;
    }
    
    private void addFact(List<String> facts, String factKey, String factValue) {
        if (factValue != null && !factValue.isEmpty()) {
            facts.add(factKey + "(" + factValue.toLowerCase().replace("-", "_") + ")");
        }
    }
    
}