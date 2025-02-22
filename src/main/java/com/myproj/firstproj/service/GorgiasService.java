package com.myproj.firstproj.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.myproj.firstproj.model.ParsedResult;

import com.myproj.firstproj.model.ParsedResult;
import com.myproj.firstproj.model.WorkflowForm;
import java.util.Iterator;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

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

    // methods to interact with the Gorgias API 

        // methods to interact with the Gorgias API 
    
// 04-12-2023 modification
    // public GorgiasQueryResult executeGorgiasQuery(WorkflowForm form) {
    //     long startTime = System.currentTimeMillis();

    //     ExecuteGorgiasQueryControllerApi apiInstance = new ExecuteGorgiasQueryControllerApi();
    //     apiInstance.getApiClient().setUsername("imichalakis");
    //     apiInstance.getApiClient().setPassword("528528gm@@");
    //     apiInstance.getApiClient().setBasePath("http://aiasvm1.amcl.tuc.gr:8085");
    //     GorgiasQuery gorgiasQuery = new GorgiasQuery(); // GorgiasQuery | gorgiasQuery
    //     //example of using the prolog API to consult your file
    //     ArrayList<String> gorgiasFiles=new ArrayList<String>();
    //     gorgiasFiles.add("Theory1/urgencylev.pl");
    //     gorgiasQuery.setGorgiasFiles(gorgiasFiles);


    //     //example of using the prolog API to assert a fact
    //     ArrayList<String> facts=new ArrayList<String>();

    //     facts.add("startdate(10)");
    //     gorgiasQuery.setFacts(facts);

    //     //example of using the prolog API to prove a policy option
    //     //String myQuery = "challenge(Agent, Resource)";
    //     String myQuery = "urgency(X)";

    //     gorgiasQuery.setResultSize(1);
    //     gorgiasQuery.setQuery(myQuery);
    //     System.out.println("Query: " + gorgiasQuery.getQuery());
    //     try{
    //     GorgiasQueryResult result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
    //     long endTime = System.currentTimeMillis();
    //     long executionTime = endTime - startTime;
    //     System.out.println("Gorgias query executed in " + executionTime + " ms");
        

    //     System.out.println(result);
    //     System.out.println(form.getStartDate());
    //     return result;
    //     }
    //     catch (ApiException e) {
    //         System.out.println("to result einai null");

    //         e.printStackTrace();
    //         return null;
    //     }

    // public GorgiasQueryResult executeGorgiasQueryForUrgency(WorkflowForm form) {
    //     long startTime = System.currentTimeMillis();

    //     // ExecuteGorgiasQueryControllerApi apiInstance = new ExecuteGorgiasQueryControllerApi();
    //     // apiInstance.getApiClient().setUsername("imichalakis");
    //     // apiInstance.getApiClient().setPassword("528528gm@@");
    //     // apiInstance.getApiClient().setBasePath("http://aiasvm1.amcl.tuc.gr:8085");
    //     GorgiasQuery gorgiasQuery = new GorgiasQuery();
    //     ArrayList<String> gorgiasFiles = new ArrayList<String>();
    //    // gorgiasFiles.add("Theory1/urgencylev4.pl");
    //    gorgiasFiles.add("november2024/urgencylev3.pl");
    //     gorgiasQuery.setGorgiasFiles(gorgiasFiles);

    //     SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    //     ArrayList<String> facts = new ArrayList<>();
    //     try {
    //         String formStartDate = form.getStartDate();
    //         if (formStartDate != null && !formStartDate.trim().isEmpty()) {
    //             Date startDate = sdf.parse(formStartDate);
    //             Date currentDate = new Date(); // Gets the current date
    //             long diffInMillies = startDate.getTime() - currentDate.getTime();
    //             long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    //             if (diffInDays < 0) {
    //                 System.out.println("The start date must be a future date.");
    //                 return null;
    //             }
    //             facts.add("startdate(" + diffInDays + ")");
    //         }

    //         String agencyCategory = form.getAgencyCategory();
    //         if (agencyCategory != null && !agencyCategory.trim().isEmpty()) {
    //             facts.add("agency_category(" + agencyCategory + ")");
    //         }

    //         //determineUrgencyBasedOnStartDate(form);
    //         // String urgency = form.getUrgency();
    //         // if (urgency != null && !urgency.trim().isEmpty()) {
    //         //     urgency = urgency.toLowerCase(Locale.ROOT);
    //         //     facts.add("urgency(" + urgency + ")");
    //         // }

    //         // New Parameters: Request Type and Contract with Contractor
    //         String requestType = form.getRequestType();
    //         if (requestType != null && !requestType.trim().isEmpty()) {
    //             requestType = requestType.toLowerCase(Locale.ROOT);
    //             facts.add("request_type(" + requestType + ")");
    //         }

    //         String contractWithContractor = form.getContractWithContractor();
    //         if (contractWithContractor != null && !contractWithContractor.trim().isEmpty()) {
    //             contractWithContractor = contractWithContractor.toLowerCase(Locale.ROOT);
    //             facts.add("contract_with_contractor(" + contractWithContractor + ")");
    //         }

    //         if (facts.isEmpty()) {
    //             System.out.println("At least one of start date, agency category, urgency, request type, or contract with contractor must be provided.");
    //             return null;
    //         }
            
    //         gorgiasQuery.setFacts(facts);
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         System.out.println("Error with input data.");
    //         return null;
    //     }

    //     String myQuery = "urgency(X)";
    //     gorgiasQuery.setResultSize(1);
    //     gorgiasQuery.setQuery(myQuery);
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
    //     }

    //     // System.out.println("Start date as input: " + form.getStartDate());
    //     // System.out.println("Agency category as input: " + form.getAgencyCategory());
    //     // System.out.println("Urgency as input: " + form.getUrgency());
    //     return result;
    // }

    public List<ParsedResult> executeGorgiasQueryForUrgency(WorkflowForm form) {
        GorgiasQuery gorgiasQuery = new GorgiasQuery();
        ArrayList<String> gorgiasFiles = new ArrayList<>();
        gorgiasFiles.add("november2024/urgencylev3.pl"); // Reference to urgency Prolog file
        gorgiasQuery.setGorgiasFiles(gorgiasFiles);
    
        ArrayList<String> facts = new ArrayList<>();
        try {
            if (form.getStartDate() != null && !form.getStartDate().trim().isEmpty()) {
                facts.add("start_date(" + form.getStartDate() + ")");
            }
            if (form.getAgencyCategory() != null && !form.getAgencyCategory().trim().isEmpty()) {
                facts.add("agency_category(" + form.getAgencyCategory() + ")");
            }
            if (form.getRequestType() != null && !form.getRequestType().trim().isEmpty()) {
                facts.add("request_type(" + form.getRequestType() + ")");
            }
            if (form.getContractWithContractor() != null && !form.getContractWithContractor().trim().isEmpty()) {
                facts.add("contract_with_contractor(" + form.getContractWithContractor() + ")");
            }
    
            System.out.println("Urgency Facts: " + facts);
            gorgiasQuery.setFacts(facts);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error processing urgency data.");
            return Collections.emptyList();
        }
    
        gorgiasQuery.setResultSize(5); // Allow multiple results
        gorgiasQuery.setQuery("urgency(X)");
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
    
    public List<ParsedResult> executeGorgiasQueryForYamlGen(WorkflowForm form) {
        GorgiasQuery gorgiasQuery = new GorgiasQuery();
        ArrayList<String> gorgiasFiles = new ArrayList<>();
        gorgiasFiles.add("finaldecision/final.pl"); // Reference to final Prolog file
        gorgiasQuery.setGorgiasFiles(gorgiasFiles);
    
       // ✅ Generate facts inside this method
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

    return parseGorgiasQueryResult(result);
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
    
        // ✅ Latency Requirement
        if (form.getLatencyRequirement() != null) {
            if (form.getLatencyRequirement().equalsIgnoreCase("strict")) {
                facts.add("strict_latency");
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
    
        return facts;
    }
    
    
    

    public void determineUrgencyBasedOnStartDate(WorkflowForm form) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String formStartDate = form.getStartDate();
            if (formStartDate != null && !formStartDate.trim().isEmpty()) {
                Date startDate = sdf.parse(formStartDate);
                Date currentDate = new Date(); // Η σημερινή ημερομηνία
                long diffInMillies = startDate.getTime() - currentDate.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
    
                // Υπολογισμός της κατάστασης επείγοντος βάσει της διαφοράς σε ημέρες
                if (diffInDays < 3) {
                    form.setUrgency("urgent");
                } else if (diffInDays >= 3 && diffInDays < 10) {
                    form.setUrgency("high");
                } else {
                    form.setUrgency("normal");
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println("Error parsing start date.");
        }
    }
    
        // public GorgiasQueryResult executeGorgiasQueryForLocation(WorkflowForm form) {

        // String deploymentLocation = form.getDeploymentLocation();
        
        // GorgiasQuery gorgiasQuery = new GorgiasQuery();
        // ArrayList<String> gorgiasFiles = new ArrayList<String>();
        // gorgiasFiles.add("Theory1/urgencylev4.pl");
        // gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        //     GorgiasQueryResult result = null;
        //     return result;
        // }
        public List<ParsedResult> executeGorgiasQueryForLocation(WorkflowForm form) {
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
            gorgiasFiles.add("november2024/location.pl");  // Prolog file for location
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
        
        
        
        
        public GorgiasQueryResult executeGorgiasQueryForInfrastructure(WorkflowForm form) {
            long startTime = System.currentTimeMillis();
            
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
            gorgiasFiles.add("november2024/infrastructure4.pl"); // Αναφέρεται στο αρχείο υποδομής στο Gorgias
            gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
            ArrayList<String> facts = new ArrayList<>();
            try {
                // Προσθήκη των παραμέτρων υποδομής ως facts
                if (form.isHighScalability()) {
                    facts.add("high_scalability");
                }
                if (form.isHighPerformance()) {
                    facts.add("high_performance");
                }
                if (form.isDevopsFriendly()) {
                    facts.add("devops_friendly");
                }
                if (form.isHighBudget()) {
                    facts.add("high_budget");
                }
                if (!form.isHighBudget()) {
                    facts.add("low_budget"); // or "not_high_budget"
                }
                if (form.isComplexApp()) {
                    facts.add("complex_app");
                }
                if (form.isDataSensitivity()) {
                    facts.add("data_sensitivity");
                }

                
                
                gorgiasQuery.setFacts(facts);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error processing infrastructure data.");
                return null;
            }
        
            gorgiasQuery.setResultSize(0);
            gorgiasQuery.setQuery("propose_infrastructure(X)");
            System.out.println("Query: " + gorgiasQuery.getQuery());
            GorgiasQueryResult result = null;
        
            try {
                result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                System.out.println("Gorgias query executed in " + executionTime + " ms");
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
        
            return result;
        }
        public List<ParsedResult> executeGorgiasQueryForInfr(WorkflowForm form) {
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
            gorgiasFiles.add("2025/infrastructure4.pl"); // Αναφέρεται στο αρχείο υποδομής στο Gorgias
            gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
            ArrayList<String> facts = new ArrayList<>();
            try {
                // Προσθήκη των παραμέτρων υποδομής ως facts
                // if (form.isHighScalability()) {
                //     facts.add("high_scalability");
                // }
             
                
                // if (form.isHighBudget()) {
                //     facts.add("high_budget");
                // }
                // if (!form.isHighBudget()) {
                //     facts.add("low_budget"); // or "not_high_budget"
                // }
              
                // if (form.isDataSensitivity()) {
                //     facts.add("data_sensitivity");
                // }
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

        // public GorgiasQueryResult executeGorgiasQueryForInfr(WorkflowForm form) {
        //     GorgiasQuery gorgiasQuery = new GorgiasQuery();
        //     ArrayList<String> gorgiasFiles = new ArrayList<>();
        //     gorgiasFiles.add("2025/infrastructure3.pl"); // Αναφέρεται στο αρχείο υποδομής στο Gorgias
        //     gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
        //     ArrayList<String> facts = new ArrayList<>();
        //     try {
        //         // Προσθήκη των παραμέτρων υποδομής ως facts
        //         // if (form.isHighScalability()) {
        //         //     facts.add("high_scalability");
        //         // }
             
                
        //         // if (form.isHighBudget()) {
        //         //     facts.add("high_budget");
        //         // }
        //         // if (!form.isHighBudget()) {
        //         //     facts.add("low_budget"); // or "not_high_budget"
        //         // }
              
        //         // if (form.isDataSensitivity()) {
        //         //     facts.add("data_sensitivity");
        //         // }
        //         String primaryGoal = form.getPrimaryGoal();
        //         if (primaryGoal != null) {
        //             // Assuming the primaryGoal string exactly matches the Prolog facts
        //             facts.add(primaryGoal);
        //         }                
        //         if (form.isHighScalability()) {
        //             facts.add("high_scalability");
        //         }
        //         if (form.isDataSensitivity()) {
        //             facts.add("data_sensitivity");
        //         }
        //         String controlRequirement = form.getControlRequirement();
               
        //         if (controlRequirement != null) {
        //             facts.add(controlRequirement);
        //         }
        //         String budget = form.getBudget();
        //         if (budget != null) {
        //             facts.add(budget);
        //         }
                
        //         if (form.isIntegrationRequirements()) {
        //             facts.add("custom_integrations");
        //         }
        //         System.out.println(facts);
        //         gorgiasQuery.setFacts(facts);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //         System.out.println("Error processing infrastructure data.");
        //         return null;
        //     }
            
        //     gorgiasQuery.setResultSize(0);
        //     gorgiasQuery.setQuery("propose_infrastructure(X)");
        //     System.out.println("Query: " + gorgiasQuery.getQuery());
        //     GorgiasQueryResult result = null;
        //     try {
        //         result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
        //         System.out.println(result);
        //         // Εκτύπωση των αποτελεσμάτων
        // if (result != null && result.isHasResult()) {
        //     System.out.println("Results from Gorgias:");
        //     List<QueryResult> results = result.getResult();
        //     for (QueryResult queryResult : results) {
        //         System.out.println("Type: " + queryResult.getExplanationStr());
        //         System.out.println("Human Explanation: " + queryResult.getHumanExplanation());
        //     }
        // } else {
        //     System.out.println("No results found or an error occurred.");
        // }
        //     } catch (ApiException e) {
        //         System.out.println("Result is null due to an exception.");
        //         e.printStackTrace();
        //     }
        //     return result;
        // }

        public List<ParsedResult> parseGorgiasQueryResult(GorgiasQueryResult gorgiasQueryResult) {
            List<ParsedResult> parsedResults = new ArrayList<>();
        
            if (gorgiasQueryResult != null && gorgiasQueryResult.getResult() != null) {
                for (QueryResult queryResult : gorgiasQueryResult.getResult()) {
                    String humanExplanation = queryResult.getHumanExplanation();
                    if (humanExplanation != null && !humanExplanation.isEmpty()) {
                        // Extract the main result
                        String mainResult = extractMainResult(humanExplanation);
        
                        // Extract the supporting facts
                        List<String> supportingFacts = extractSupportingFacts(humanExplanation);
        
                        // Add parsed result
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
        private List<String> extractSupportingFacts(String humanExplanation) {
            List<String> facts = new ArrayList<>();
            if (humanExplanation != null && !humanExplanation.isEmpty()) {
                // Find the portion of the explanation starting with the list of facts
                String keyword = "- ";
                int start = humanExplanation.indexOf(keyword);
                if (start != -1) {
                    String factsSection = humanExplanation.substring(start + keyword.length());
        
                    // Split facts using " and " as the delimiter
                    String[] splitFacts = factsSection.split(" and ");
                    for (String fact : splitFacts) {
                        // Clean up the fact by removing extra spaces and quotes
                        fact = fact.trim();
                        if (fact.startsWith("\"") && fact.endsWith("\"")) {
                            fact = fact.substring(1, fact.length() - 1);
                        }
                        if (!fact.isEmpty()) {
                            facts.add(fact);
                        }
                    }
                }
            }
            return facts;
        }
        
    //     public String convertFactToNaturalLanguage(String fact) {
    //         if (fact == null || fact.trim().isEmpty()) {
    //             return "Unknown fact";
    //         }
        
    //         // Normalize the fact: trim whitespace and convert to lowercase
    //         fact = fact.trim().toLowerCase();
        
    //         // Mapping for Prolog facts to natural language
    //         Map<String, String> factMappings = new HashMap<>();
    //         factMappings.put("response_time_sla(high)", "Response Time SLA - High");
    //         factMappings.put("response_time_sla(medium)", "Response Time SLA - Medium");
    //         factMappings.put("response_time_sla(low)", "Response Time SLA - Low");
    //         factMappings.put("expected_load(high)", "Expected Load - High");
    //         factMappings.put("expected_load(medium)", "Expected Load - Medium");
    //         factMappings.put("expected_load(low)", "Expected Load - Low");
    //         factMappings.put("peak_times(rarely)", "Peak Times - Rarely");
    //         factMappings.put("peak_times(occasionally)", "Peak Times - Occasionally");
    //         factMappings.put("peak_times(frequently)", "Peak Times - Frequently");
    //         factMappings.put("peak_times(always)", "Peak Times - Always");
    //         factMappings.put("cost_sensitivity(low)", "Cost Sensitivity - Low");
    //         factMappings.put("cost_sensitivity(medium)", "Cost Sensitivity - Medium");
    //         factMappings.put("cost_sensitivity(high)", "Cost Sensitivity - High");
    //         factMappings.put("cost_sensitivity(high)", "Cost Sensitivity - High");
    //         factMappings.put("cpu_requirements(high)", "CPU Requirements - High");
    //         factMappings.put("memory_requirements(high)", "Memory Requirements - High");
    //         factMappings.put("storage_requirements(high)", "Storage Requirements - High");
    //         factMappings.put("storage_requirements(low)", "Storage Requirements - Low");
    //         factMappings.put("memory_requirements(low)", "Memory Requirements - Low");
    //         factMappings.put("memory_requirements(medium)", "Memory Requirements - Medium");
    //         factMappings.put("cpu_requirements(medium)", "CPU Requirements - Medium");
    //         factMappings.put("storage_requirements(medium)", "Storage Requirements - Medium");
    //         factMappings.put("cpu_requirements(low)", "CPU Requirements - Low");
    //         factMappings.put("agency_category(centralgovernment)", "Agency Category - Central Government");
    //         factMappings.put("agency_category(localgovernment)", "Agency Category - Local Government");
    //        factMappings.put("agency_category(independentauthority)", "Agency Category - Independent Authority");
    //         factMappings.put("request_type(ops)", "Ministry of Digital Governance");
    //         factMappings.put("request_type(other)", "Other Ministry");
    //         factMappings.put("contract_with_contractor(yes)", "There is contract");
    //         factMappings.put("contract_with_contractor(no)", "There is not any contract");

    //         // Add mappings for the new facts
    // factMappings.put("high_scalability", "High Scalability is required for the infrastructure.");
    // factMappings.put("data_sensitivity", "The infrastructure needs to handle sensitive data.");
    // factMappings.put("custom_integrations", "Custom integrations are required for the application.");
    // factMappings.put("fullControl", "Full control over the infrastructure is needed (e.g., VM, storage, and network configuration).");
    // factMappings.put("limitedControl", "Limited control over the infrastructure is sufficient (e.g., manage runtime without worrying about servers).");
    // factMappings.put("nocontrol", "No control over the infrastructure is required (e.g., ready-made tools).");
    // factMappings.put("high_budget", "The budget is high for this project.");
    // factMappings.put("low_budget", "Low budget is a supporting fact for this goal.");

    // // Add mappings for primaryGoal
    // factMappings.put("runCustomApps", "The primary goal is to run and customize applications (e.g., deployment of custom software).");
    // factMappings.put("consumeReadySoftware", "The primary goal is to consume ready-made software with minimal configuration (e.g., SaaS tools).");
    // factMappings.put("eventdrivenfunctions", "The primary goal is to automate tasks with event-driven architecture (e.g., serverless functions).");

    //         // Return the natural language equivalent or the original fact if no mapping exists
    //         return factMappings.getOrDefault(fact, fact);
    //     }
    public String convertFactToNaturalLanguage(String fact) {
        if (fact == null || fact.trim().isEmpty()) {
            return "Unknown fact";
        }
    
        // Normalize the fact: trim whitespace and convert to lowercase
        fact = fact.trim().toLowerCase();
    
        // Mapping for Prolog facts to natural language
        Map<String, String> factMappings = new HashMap<>();
        
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
        
        // Agency Category
        factMappings.put("agency_category(centralgovernment)", "This request comes from a Central Government agency.");
        factMappings.put("agency_category(localgovernment)", "This request originates from a Local Government body.");
        factMappings.put("agency_category(independentauthority)", "This request is from an Independent Authority.");
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

        // Request Type
        factMappings.put("request_type(ops)", "The request is related to the Ministry of Digital Governance.");
        factMappings.put("request_type(other)", "The request is from another ministry or organization.");
        
        // Contract Information
        factMappings.put("contract_with_contractor(yes)", "An active contract exists with an external contractor.");
        factMappings.put("contract_with_contractor(no)", "There is no current contract with any external contractor.");
        
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

        // Return the mapped natural language description or the original fact if no mapping exists
        return factMappings.getOrDefault(fact, fact);
    }
    
    public String mapMainResultToNaturalLanguage(String mainResult, WorkflowForm form) { 
        switch (mainResult) {
            // Urgency decisions
            case "urgent":
              
                return "Urgent: Critical response required immediately.";
            case "normal":
               
                return "Normal Urgency: Minimal immediate attention required.";
            case "high":
               
                return "High Urgency: Requires immediate action.";
    
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
    
            default:
                return "Unknown Strategy";
        }
    }
    
        
        
//         public List<ParsedResult> executeGorgiasQueryForScalability(WorkflowForm form) {
//     long startTime = System.currentTimeMillis();

//     GorgiasQuery gorgiasQuery = new GorgiasQuery();
//     ArrayList<String> gorgiasFiles = new ArrayList<>();
//     gorgiasFiles.add("november2024/scalability2025.pl");
//     gorgiasQuery.setGorgiasFiles(gorgiasFiles);

//     ArrayList<String> facts = new ArrayList<>();
//     try {
//         // Add expected_load fact
//         String expectedLoad = form.getExpectedLoad();
//         if (expectedLoad != null && !expectedLoad.isEmpty()) {
//             facts.add("expected_load(" + expectedLoad.toLowerCase() + ")");
//             System.out.println("Added facts: " + facts);
//         }

//         // Add peak_times fact
//         String peakTimes = form.getPeakTimes();
//         if (peakTimes != null && !peakTimes.isEmpty()) {
//             facts.add("peak_times(" + peakTimes.toLowerCase() + ")");
//             System.out.println("peakTimes: " + peakTimes);
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

//     gorgiasQuery.setResultSize(1);
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
public List<ParsedResult> executeGorgiasQueryForScalability(WorkflowForm form) {
    long startTime = System.currentTimeMillis();

    GorgiasQuery gorgiasQuery = new GorgiasQuery();
    ArrayList<String> gorgiasFiles = new ArrayList<>();
    gorgiasFiles.add("november2024/scalability2025.pl");
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

    // Parse the result into ParsedResult objects
    return parseGorgiasQueryResult(result);
}

        // public GorgiasQueryResult executeGorgiasQueryForScalability(WorkflowForm form) {
        //     long startTime = System.currentTimeMillis();
        
        //     GorgiasQuery gorgiasQuery = new GorgiasQuery();
        //     ArrayList<String> gorgiasFiles = new ArrayList<>();
        //     gorgiasFiles.add("november2024/scalability2025.pl");
        //     gorgiasQuery.setGorgiasFiles(gorgiasFiles);
        
        //     ArrayList<String> facts = new ArrayList<>();
        //     try {
        //         // Προσθήκη του expected_load
        //         String expectedLoad = form.getExpectedLoad();
        //         if (expectedLoad != null && !expectedLoad.isEmpty()) {
        //             facts.add("expected_load(" + expectedLoad.toLowerCase() + ")");
        //           //  System.out.println("expectedLoad: " + expectedLoad);
        //           System.out.println("Added facts: " + facts);  // Εκτύπωση του fact

        //         }
        
        //         String peakTimes = form.getPeakTimes();
        //         if (peakTimes != null && !peakTimes.isEmpty()) {
        //             facts.add("peak_times(" + peakTimes.toLowerCase() + ")");
        //             System.out.println("peakTimes: " + peakTimes);
        //         }
                
        
        //         // Προσθήκη του response_time_sla ως fact χωρίς συγκεκριμένη τιμή
        //         String responseTime = form.getResponseTime();
        //         if (responseTime != null && !responseTime.isEmpty()) {
        //             facts.add("response_time_sla(" + responseTime.toLowerCase().replace("-", "_") + ")");
        //             //System.out.println("scalabilityNeeds: " + scalabilityNeeds);
        //         }
                
        //         String costSensitivity = form.getCostSensitivity();
        //         if (costSensitivity != null && !costSensitivity.isEmpty()) {
        //             facts.add("cost_sensitivity(" + costSensitivity.toLowerCase().replace("-", "_") + ")");
        //             //System.out.println("scalabilityNeeds: " + scalabilityNeeds);
        //         }

        //         gorgiasQuery.setFacts(facts);
        //     } catch (Exception e) {
        //         e.printStackTrace();
        //         System.out.println("Error processing scalability data.");
        //         return null;
        //     }
        
        //     gorgiasQuery.setResultSize(1);
        //    // gorgiasQuery.setQuery("propose_scaling_strategy(X)");
        //    gorgiasQuery.setQuery("X");
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
        //     }
        
        //     return result;
        // }
        public List<ParsedResult> executeGorgiasQueryForResources(WorkflowForm form) {
            GorgiasQuery gorgiasQuery = new GorgiasQuery();
            ArrayList<String> gorgiasFiles = new ArrayList<>();
          //  gorgiasFiles.add("2025/resource2025.pl");  // Prolog file for resource decisions
          gorgiasFiles.add("2025/resource2025.pl"); 
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
        
        
        
        // public Map<String, GorgiasQueryResult> evaluateResourceRequirements(WorkflowForm form) {
        //     Map<String, GorgiasQueryResult> combinedResults = new HashMap<String, GorgiasQueryResult>();
        
        //     // Ερώτημα 1: propose_resource(X)
        //     GorgiasQuery gorgiasQuery1 = new GorgiasQuery();
        //     ArrayList<String> gorgiasFiles = new ArrayList<>();
        //     gorgiasFiles.add("november2024/resource3.pl"); // Προσάρμοσε το μονοπάτι του αρχείου Prolog
        //     gorgiasQuery1.setGorgiasFiles(gorgiasFiles);
        
        //     ArrayList<String> facts = new ArrayList<>();
        //     if (form.getCpuRequirements() != null) {
        //         facts.add("cpu_requirements(" + form.getCpuRequirements().toLowerCase() + ")");
        //     }
        //     if (form.getMemoryRequirements() != null) {
        //         facts.add("memory_requirements(" + form.getMemoryRequirements().toLowerCase() + ")");
        //     }
        //     if (form.getStorageNeeds() != null) {
        //         facts.add("storage_needs(" + form.getStorageNeeds().toLowerCase() + ")");
        //     }
        //     if (form.getScalingStrategy() != null) {
        //         facts.add("scaling_strategy(" + form.getScalingStrategy().toLowerCase() + ")");
        //     }
        
        //     // Έλεγχος αν υπάρχουν facts πριν εκτελέσεις το query
        //     if (!facts.isEmpty()) {
        //         gorgiasQuery1.setFacts(facts);
        //         gorgiasQuery1.setResultSize(1);
        //         gorgiasQuery1.setQuery("propose_resource(X)");
        
        //         try {
        //             GorgiasQueryResult result1 = apiInstance.executeQueryUsingPOST(gorgiasQuery1);
        //             // Έλεγχος αν το αποτέλεσμα δεν είναι null και περιέχει αποτέλεσμα
        //             if (result1 != null && result1.getResult() != null && !result1.getResult().isEmpty()) {
        //                 combinedResults.put("resource", result1);
        //             } else {
        //                 System.out.println("No valid resource result returned from Gorgias.");
        //             }
        //         } catch (ApiException e) {
        //             e.printStackTrace();
        //         }
        
        //         // Ερώτημα 2: propose_scaling(Y)
        //         GorgiasQuery gorgiasQuery2 = new GorgiasQuery();
        //         gorgiasQuery2.setGorgiasFiles(gorgiasFiles);
        //         gorgiasQuery2.setFacts(facts);
        //         gorgiasQuery2.setResultSize(1);
        //         gorgiasQuery2.setQuery("propose_scaling(Y)");
        
        //         try {
        //             GorgiasQueryResult result2 = apiInstance.executeQueryUsingPOST(gorgiasQuery2);
        //             // Έλεγχος αν το αποτέλεσμα δεν είναι null και περιέχει αποτέλεσμα
        //             if (result2 != null && result2.getResult() != null && !result2.getResult().isEmpty()) {
        //                 combinedResults.put("scaling", result2);
        //             } else {
        //                 System.out.println("No valid scaling result returned from Gorgias.");
        //             }
        //         } catch (ApiException e) {
        //             e.printStackTrace();
        //         }
        //     } else {
        //         System.out.println("No valid facts provided for resource requirements evaluation.");
        //     }
        
        //     return combinedResults;
        // }
        
        

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
    
//commented 7-12-2023 the below code

// public GorgiasQueryResult executeGorgiasQuery(WorkflowForm form) {
//         long startTime = System.currentTimeMillis();

//         ExecuteGorgiasQueryControllerApi apiInstance = new ExecuteGorgiasQueryControllerApi();
//         apiInstance.getApiClient().setUsername("imichalakis");
//         apiInstance.getApiClient().setPassword("528528gm@@");
//         apiInstance.getApiClient().setBasePath("http://aiasvm1.amcl.tuc.gr:8085");
//         GorgiasQuery gorgiasQuery = new GorgiasQuery(); // GorgiasQuery | gorgiasQuery
//         ArrayList<String> gorgiasFiles = new ArrayList<String>();
//         gorgiasFiles.add("Theory1/urgencylev4.pl");
//         gorgiasQuery.setGorgiasFiles(gorgiasFiles);

//         SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//         try {
//             Date startDate = sdf.parse(form.getStartDate());
//             Date currentDate = new Date(); // Gets the current date
            
//             // Calculate the difference in days between the current date and start date
//             long diffInMillies = startDate.getTime() - currentDate.getTime();
//             long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
            
//             // Guard against negative values if the start date is in the past
//             if (diffInDays < 0) {
//                 System.out.println("The start date must be a future date.");
//                 return null;
//             }

//             // Add the calculated startdate fact
//             ArrayList<String> facts = new ArrayList<String>();
//             facts.add("startdate(" + diffInDays + ")");

            
//             // Handle agency category fact, if available
//             String agencyCategory = form.getAgencyCategory();
//             if (agencyCategory != null && !agencyCategory.isEmpty()) {
//                 facts.add("agency_category(" + agencyCategory + ")");
//             }

//             // Handle urgency fact, convert to lowercase
//             String urgency = form.getUrgency();
//             if (urgency != null && !urgency.isEmpty()) {
//                 urgency = urgency.toLowerCase(Locale.ROOT); // Convert to lowercase
//                 facts.add("urgency(" + urgency + ")");
//             }

//             gorgiasQuery.setFacts(facts);
//         } catch (Exception e) {
//             e.printStackTrace();
//             System.out.println("Error parsing date from form");
//             return null;
//         }

//         String myQuery = "urgency(X)";
//         gorgiasQuery.setResultSize(1);
//         gorgiasQuery.setQuery(myQuery);
//         System.out.println("Query: " + gorgiasQuery.getQuery());
//         GorgiasQueryResult result = null;
//         try {
//             result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
//             long endTime = System.currentTimeMillis();
//             long executionTime = endTime - startTime;
//             System.out.println("Gorgias query executed in " + executionTime + " ms");
//             System.out.println(result);
//         } catch (ApiException e) {
//             System.out.println("Result is null due to an exception.");
//             e.printStackTrace();
//         }

//         System.out.println("Start date as input: " + form.getStartDate());
//         return result;
    


// }


        // Create a GorgiasQuery object from the form data
    //     GorgiasQuery gorgiasQuery = new GorgiasQuery();
    //     List<String> gorgiasFiles = Arrays.asList("your_gorgias_file.pl");
    //     // Set the Gorgias files
    //     gorgiasQuery.setGorgiasFiles(gorgiasFiles);        
    //     // Set facts from the form
    //     ArrayList<String> facts = new ArrayList<>();
    //     facts.add("urgency(" + form.getUrgency() + ")");
    //     facts.add("startDate(" + form.getStartDate() + ")");
    //     gorgiasQuery.setFacts(facts);

    //     // Set the query you want to execute
    //     gorgiasQuery.setQuery("your_query_here");
    //     gorgiasQuery.setResultSize(1); // Or any other result size you want

    //     try {
    //         // Execute the query and get the result
    //         return apiInstance.executeQueryUsingPOST(gorgiasQuery);
    //     } catch (ApiException e) {
    //         // Log the exception or handle it as needed
    //         e.printStackTrace();
          //  return null;
    //     }
      
     












     ///dokimastiko

    //  public CompletableFuture<GorgiasQueryResult> executeGorgiasQueryAsync(WorkflowForm form) {
    //     // Wrap the execution in CompletableFuture.supplyAsync
    //     return CompletableFuture.supplyAsync(() -> {
    //         long startTime = System.currentTimeMillis();

    //         ExecuteGorgiasQueryControllerApi apiInstance = new ExecuteGorgiasQueryControllerApi();
    //         apiInstance.getApiClient().setUsername("imichalakis");
    //         apiInstance.getApiClient().setPassword("528528gm@@");
    //         apiInstance.getApiClient().setBasePath("http://aiasvm1.amcl.tuc.gr:8085");
    //         GorgiasQuery gorgiasQuery = new GorgiasQuery();
    //         ArrayList<String> gorgiasFiles = new ArrayList<>();
    //         gorgiasFiles.add("urgencylev.pl");
    //         gorgiasQuery.setGorgiasFiles(gorgiasFiles);

    //         ArrayList<String> facts = new ArrayList<>();
    //         facts.add("request(localgov,startdate(5))");
    //         gorgiasQuery.setFacts(facts);

    //         String myQuery = "urgency(X)";
    //         gorgiasQuery.setResultSize(1);
    //         gorgiasQuery.setQuery(myQuery);
    //         System.out.println("Query: " + gorgiasQuery.getQuery());

    //         try {
    //             GorgiasQueryResult result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
    //             long endTime = System.currentTimeMillis();
    //             long executionTime = endTime - startTime;
    //             System.out.println("Gorgias query executed in " + executionTime + " ms");

    //             System.out.println(result);
    //             return result;
    //         } catch (ApiException e) {
    //             System.out.println("to result einai null");
    //             e.printStackTrace();
    //             return null;
    //         }
    //     });
    // }

}
