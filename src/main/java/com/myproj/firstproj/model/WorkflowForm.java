package com.myproj.firstproj.model;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Id;

public class WorkflowForm {
    private String urgency = "Normal";
    private String agencyCategory;
    private String startDate;
    private String dataType;
    private String requestType;
    private String contractWithContractor;
    private String connectivity = "false";
    private String deploymentLocation;
    private String buildNewOrMigrate;
    // Add other fields as necessary
    private int step = 1;

    //Fields based on the Azure decision flowchart

    private String controlLevel;
    private String workloadType;
    private String applicationType;
    private String isContainerized;
    private String fullControl;
    private String HPCWorkload;
    private String SBapps;
    private String eventDriven;
    private String cloudOptimised;
    private String containerized;
    private String cotsapp;
    private String serviceName;


    private boolean highScalability = false;
    private boolean devopsFriendly = false;
    private boolean highPerformance = false;
    private boolean highBudget = false; // true for high, false for low
    private boolean complexApp = false; // true for complex app, false for simple app
    private boolean dataSensitivity = false;

    private String budgetLimitation;
    private String latencyRequirement;
    private String scalabilityRequirement;
    private String complianceRequirement;
    private String disasterRecoveryNeeds;
    private String primaryGoal;
    private String controlRequirement;
    private boolean integrationRequirements = false;
    private String budget;

    private String expectedLoad;
    private String peakTimes;
    private String scalabilityNeeds;
    private String responseTime;
    private String costSensitivity;

    private String cpuRequirements;
    private String memoryRequirements;
    private String storageNeeds;
    private String scalingStrategy;

    private String urgencyDecision;
    private String locationDecision;
    private String infrastructureDecision;
    private String resourceDecision;
    private String scalabilityAndPerformanceDecision;
    private String scalingDecision;
    private String finalDecision;
    private String resourcePriority;      // Compute vs. Storage Optimization
private String performanceRequirement; // Low Latency vs. High Throughput // Auto-Scaling vs. Fixed Resources
private String processingOptimization; // Memory vs. CPU Priority
 // ✅ **New fields to store raw decision values separately**
 private String urgencyForFinal;
 private String infrastructureForFinal;
 private String scalabilityForFinal;
 private String resourceForFinal;
 private String processingOptimizationForFinal;
 private String yamlGenerationDecision;
 private static List<String> comparativeExplanations = new ArrayList<>();
 private static List<String> urgencyLevels = new ArrayList<>();

 public static void addUrgencyLevel(String level) {
     urgencyLevels.add(level);
 }
 
 public static List<String> getUrgencyLevels() {
     return urgencyLevels;
 }
 public static void clearComparativeExplanations() {
    comparativeExplanations.clear();
}
 public static void addComparativeExplanation(String explanation) {
     comparativeExplanations.add(explanation);
 }
 
 public static List<String> getComparativeExplanations() {
     return comparativeExplanations;
 }

 public String getYamlGenerationDecision() {
     return yamlGenerationDecision;
 }
 
 public void setYamlGenerationDecision(String yamlGenerationDecision) {
     this.yamlGenerationDecision = yamlGenerationDecision;
 }
 
 // **🔵 Getters and Setters for New Fields**
 
 // ✅ UrgencyForFinal
 public String getUrgencyForFinal() {
     return urgencyForFinal;
 }
 public void setUrgencyForFinal(String urgencyForFinal) {
     this.urgencyForFinal = urgencyForFinal;
 }

 // ✅ InfrastructureForFinal
 public String getInfrastructureForFinal() {
     return infrastructureForFinal;
 }
 public void setInfrastructureForFinal(String infrastructureForFinal) {
     this.infrastructureForFinal = infrastructureForFinal;
 }

 // ✅ ScalabilityForFinal
 public String getScalabilityForFinal() {
     return scalabilityForFinal;
 }
 public void setScalabilityForFinal(String scalabilityForFinal) {
     this.scalabilityForFinal = scalabilityForFinal;
 }

 // ✅ ResourceForFinal (Compute vs. Storage Priority)
 public String getResourceForFinal() {
     return resourceForFinal;
 }
 public void setResourceForFinal(String resourceForFinal) {
     this.resourceForFinal = resourceForFinal;
 }

 // ✅ Processing Optimization (CPU vs. Memory Priority)
 public String getProcessingOptimizationForFinal() {
     return processingOptimizationForFinal;
 }
 public void setProcessingOptimizationForFinal(String processingOptimizationForFinal) {
     this.processingOptimizationForFinal = processingOptimizationForFinal;
 }
// Getters και Setters
// Getters and Setters for new fields

public void setPrimaryGoal(String primaryGoal){
    this.primaryGoal = primaryGoal;
}

public String getPrimaryGoal(){
    return primaryGoal;
}

 // Getter and Setter for Control Requirement
 public String getControlRequirement() {
    return controlRequirement;
}

public void setControlRequirement(String controlRequirement) {
    this.controlRequirement = controlRequirement;
}
 // Getter and Setter for Integration Requirements
 public boolean isIntegrationRequirements() {
    return integrationRequirements;
}

public void setIntegrationRequirements(boolean integrationRequirements) {
    this.integrationRequirements = integrationRequirements;
}

// Getter and Setter for Budget
public String getBudget() {
    return budget;
}

public void setBudget(String budget) {
    this.budget = budget;
}
public String getBudgetLimitation() {
    return budgetLimitation;
}

public void setBudgetLimitation(String budgetLimitation) {
    this.budgetLimitation = budgetLimitation;
}

public String getLatencyRequirement() {
    return latencyRequirement;
}

public void setLatencyRequirement(String latencyRequirement) {
    this.latencyRequirement = latencyRequirement;
}

public String getScalabilityRequirement() {
    return scalabilityRequirement;
}

public void setScalabilityRequirement(String scalabilityRequirement) {
    this.scalabilityRequirement = scalabilityRequirement;
}

public String getComplianceRequirement() {
    return complianceRequirement;
}

public void setComplianceRequirement(String complianceRequirement) {
    this.complianceRequirement = complianceRequirement;
}

public String getDisasterRecoveryNeeds() {
    return disasterRecoveryNeeds;
}

public void setDisasterRecoveryNeeds(String disasterRecoveryNeeds) {
    this.disasterRecoveryNeeds = disasterRecoveryNeeds;
}
public String getRequestType() {
    return requestType;
}

public void setRequestType(String requestType) {
    this.requestType = requestType;
}

public String getContractWithContractor() {
    return contractWithContractor;
}

public void setContractWithContractor(String contractWithContractor) {
    this.contractWithContractor = contractWithContractor;
}
public String getCpuRequirements() { return cpuRequirements; }
public void setCpuRequirements(String cpuRequirements) { this.cpuRequirements = cpuRequirements; }

public String getMemoryRequirements() { return memoryRequirements; }
public void setMemoryRequirements(String memoryRequirements) { this.memoryRequirements = memoryRequirements; }

public String getStorageNeeds() { return storageNeeds; }
public void setStorageNeeds(String storageNeeds) { this.storageNeeds = storageNeeds; }

public String getScalingStrategy() { return scalingStrategy; }
public void setScalingStrategy(String scalingStrategy) { this.scalingStrategy = scalingStrategy; }

public String getFullControl() {
    return fullControl;
}

public void setFullControl(String fullControl) {
    this.fullControl = fullControl;
}
public boolean isHighScalability() {
    return highScalability;
}

public void setHighScalability(boolean highScalability) {
    this.highScalability = highScalability;
}

public boolean isDevopsFriendly() {
    return devopsFriendly;
}

public void setDevopsFriendly(boolean devopsFriendly) {
    this.devopsFriendly = devopsFriendly;
}

public boolean isHighPerformance() {
    return highPerformance;
}

public void setHighPerformance(boolean highPerformance) {
    this.highPerformance = highPerformance;
}

public boolean isHighBudget() {
    return highBudget;
}

public void setHighBudget(boolean highBudget) {
    this.highBudget = highBudget;
}

public boolean isComplexApp() {
    return complexApp;
}

public void setComplexApp(boolean complexApp) {
    this.complexApp = complexApp;
}

public boolean isDataSensitivity() {
    return dataSensitivity;
}

public void setDataSensitivity(boolean dataSensitivity) {
    this.dataSensitivity = dataSensitivity;
}

// Getters και Setters για την expectedLoad
public String getExpectedLoad() {
    return expectedLoad;
}

public void setExpectedLoad(String expectedLoad) {
    this.expectedLoad = expectedLoad;
}

public String getPeakTimes() {
    return peakTimes;
}

public void setPeakTimes(String peakTimes) {
    this.peakTimes = peakTimes;
}


public String getResponseTime() {
    return responseTime;
}

public void setResponseTime(String responseTime) {
    this.responseTime = responseTime;
}

public String getCostSensitivity() {
    return costSensitivity;
}

public void setCostSensitivity(String costSensitivity) {
    this.costSensitivity = costSensitivity;
}
// Getters και Setters για την scalabilityNeeds
public String getScalabilityNeeds() {
    return scalabilityNeeds;
}

public void setScalabilityNeeds(String scalabilityNeeds) {
    this.scalabilityNeeds = scalabilityNeeds;
}

    // Getters and Setters
    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getAgencyCategory() {
        return agencyCategory;
    }

    public void setAgencyCategory(String agencyCategory) {
        this.agencyCategory = agencyCategory;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getConnectivity() {
        return connectivity;
    }

    public void setConnectivity(String connectivity) {
        this.connectivity = connectivity;
    }

    public String getDeploymentLocation() {
        return deploymentLocation;
    }

    public void setDeploymentLocation(String deploymentLocation) {
        this.deploymentLocation = deploymentLocation;
    }


    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }
     // getters and setters  based on the Azure decision flowchart
     public String getBuildNewOrMigrate() {
        return buildNewOrMigrate;
    }

    public void setBuildNewOrMigrate(String buildNewOrMigrate) {
        this.buildNewOrMigrate = buildNewOrMigrate;
    }

    public String getControlLevel() {
        return controlLevel;
    }

    public void setControlLevel(String controlLevel) {
        this.controlLevel = controlLevel;
    }

    public String getWorkloadType() {
        return workloadType;
    }

    public void setHPCWorkload(String HPCWorkload) {
        this.HPCWorkload = HPCWorkload;
    }
    public String getHPCWorkload() {
        return HPCWorkload;
    }

    public void setWorkloadType(String workloadType) {
        this.workloadType = workloadType;
    }

    public String getApplicationType() {
        return applicationType;
    }

    public void setApplicationType(String applicationType) {
        this.applicationType = applicationType;
    }

    public String getIsContainerized() {
        return isContainerized;
    }

    public void setIsContainerized(String isContainerized) {
        this.isContainerized = isContainerized;
    }
     public String getSBapps() {
        return SBapps;
    }

    public void setSBapps(String SBapps) {
        this.SBapps = SBapps;
    }

    public String getEventDriven(){
        return eventDriven;
    }
    
    public void setEventDriven(String eventDriven)
    {
        this.eventDriven = eventDriven;
    }

    public String getCloudOptimised(){
        return cloudOptimised;
    }

    public void setCloudOptimised(String cloudOptimised){
        this.cloudOptimised = cloudOptimised;
    }
    public String getContainerized(){
        return containerized;
    }

    public void setContainerized(String containerized){
        this.containerized = containerized;
    }
    public String getCotsapp(){
        return cotsapp;
    }

    public void setCotsapp(String cotsapp){
        this.cotsapp = cotsapp;
    }

    public void setServiceName(String serviceName){
        this.serviceName = serviceName;
    }
    public String getServiceName(){
        return serviceName;
    }

    public String getLocationDecision() {
        return locationDecision;
    }

    public void setLocationDecision(String locationDecision) {
        this.locationDecision = locationDecision;
    }

    public String getInfrastructureDecision() {
        return infrastructureDecision;
    }

    public void setInfrastructureDecision(String infrastructureDecision) {
        this.infrastructureDecision = infrastructureDecision;
    }

    public String getResourceDecision() {
        return resourceDecision;
    }

    public void setResourceDecision(String resourceDecision) {
        this.resourceDecision = resourceDecision;
    }
    public String getScalabilityAndPerformanceDecision() {
        return scalabilityAndPerformanceDecision;
    }

    // Setter for scalabilityAndPerformanceDecision
    public void setScalabilityAndPerformanceDecision(String scalabilityAndPerformanceDecision) {
        this.scalabilityAndPerformanceDecision = scalabilityAndPerformanceDecision;
    }

    public void setScalingDecision(String scalingDecision)
    {
        this.scalingDecision = scalingDecision;
    }
    public String getScalingDecision()
    {
        return scalingDecision;
    }


    public void setUrgencyDecision(String urgencyDecision)
    {
        this.urgencyDecision = urgencyDecision;
    }
    public String getUrgencyDecision()
    {
        return urgencyDecision;
    }

    public void setFinalDecision(String finalDecision)
    {
        this.finalDecision = finalDecision;
    }
    public String getFinalDecision()
    {
        return finalDecision;
    }

     // **🆕 Getters and Setters for New Resource Parameters**
     public String getResourcePriority() {
        return resourcePriority;
    }

    public void setResourcePriority(String resourcePriority) {
        this.resourcePriority = resourcePriority;
    }

    public String getPerformanceRequirement() {
        return performanceRequirement;
    }

    public void setPerformanceRequirement(String performanceRequirement) {
        this.performanceRequirement = performanceRequirement;
    }

  

    public String getProcessingOptimization() {
        return processingOptimization;
    }

    public void setProcessingOptimization(String processingOptimization) {
        this.processingOptimization = processingOptimization;
    }

    /**
 * Resets all form fields to their default values.
 * This method is used when a complete workflow reset is needed.
 */
public void resetAllFields() {
    // Reset primitive string fields to defaults or empty strings
    this.urgency = "Normal";
    this.agencyCategory = null;
    this.startDate = null;
    this.dataType = null;
    this.requestType = null;
    this.contractWithContractor = null;
    this.connectivity = "false";
    this.deploymentLocation = null;
    this.buildNewOrMigrate = null;
    this.step = 1;
    
    // Reset Azure decision flowchart fields
    this.controlLevel = null;
    this.workloadType = null;
    this.applicationType = null;
    this.isContainerized = null;
    this.fullControl = null;
    this.HPCWorkload = null;
    this.SBapps = null;
    this.eventDriven = null;
    this.cloudOptimised = null;
    this.containerized = null;
    this.cotsapp = null;
    this.serviceName = null;
    
    // Reset boolean fields to defaults
    this.highScalability = false;
    this.devopsFriendly = false;
    this.highPerformance = false;
    this.highBudget = false;
    this.complexApp = false;
    this.dataSensitivity = false;
    this.integrationRequirements = false;
    
    // Reset requirement fields
    this.budgetLimitation = null;
    this.latencyRequirement = null;
    this.scalabilityRequirement = null;
    this.complianceRequirement = null;
    this.disasterRecoveryNeeds = null;
    this.primaryGoal = null;
    this.controlRequirement = null;
    this.budget = null;
    
    // Reset load and performance fields
    this.expectedLoad = null;
    this.peakTimes = null;
    this.scalabilityNeeds = null;
    this.responseTime = null;
    this.costSensitivity = null;
    
    // Reset resource requirement fields
    this.cpuRequirements = null;
    this.memoryRequirements = null;
    this.storageNeeds = null;
    this.scalingStrategy = null;
    
    // Reset all decision fields
    this.urgencyDecision = null;
    this.locationDecision = null;
    this.infrastructureDecision = null;
    this.resourceDecision = null;
    this.scalabilityAndPerformanceDecision = null;
    this.scalingDecision = null;
    this.finalDecision = null;
    
    // Reset resource priority fields
    this.resourcePriority = null;
    this.performanceRequirement = null;
    this.processingOptimization = null;
    
    // Reset final decision fields
    this.urgencyForFinal = null;
    this.infrastructureForFinal = null;
    this.scalabilityForFinal = null;
    this.resourceForFinal = null;
    this.processingOptimizationForFinal = null;
    this.yamlGenerationDecision = null;
}
}
