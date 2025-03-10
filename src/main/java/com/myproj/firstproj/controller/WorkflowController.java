package com.myproj.firstproj.controller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.MalformedURLException;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;
import java.nio.file.Files;

import com.myproj.firstproj.model.ParsedResult;
import com.myproj.firstproj.model.WorkflowForm;
import com.myproj.firstproj.service.GorgiasService;
import com.myproj.firstproj.service.WorkflowService;
import java.util.Set;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Collections;
import io.swagger.client.model.GorgiasQueryResult;
import io.swagger.client.model.QueryResult;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import javax.servlet.http.HttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;


@Controller
@RequestMapping("/workflow")
@SessionAttributes("form")
public class WorkflowController {
    private static final Logger logger = LoggerFactory.getLogger(WorkflowController.class);

    private final WorkflowService workflowService;
    @Autowired
    private GorgiasService gorgiasService;
 

    private static final Logger log = LoggerFactory.getLogger(WorkflowController.class);

    @Autowired
    public WorkflowController(WorkflowService workflowService) {
        this.workflowService = workflowService;
    }
  
//     @GetMapping("/infrtype")
// public String showInfrastructureTypePage(Model model) {
//     if (!model.containsAttribute("form")) {
//         model.addAttribute("form", new WorkflowForm());
//     }
//     return "workflow/infrtype"; // Επιστρέφει το infrtype.html στον κατάλογο templates/workflow
// }

@PostMapping("/reset-status")
public String resetStatus(HttpServletRequest request, HttpSession session, 
                          RedirectAttributes redirectAttributes) {
    System.out.println("Performing complete workflow reset...");

    try {
        // Get the current form from session
        WorkflowForm currentForm = (WorkflowForm) session.getAttribute("form");
        
        // If form exists, reset all its fields
        if (currentForm != null) {
            currentForm.resetAllFields();
            System.out.println("Form object has been reset to default values");
        } else {
            // Create a new form object if none exists
            currentForm = new WorkflowForm();
            System.out.println("Created new form object");
        }
        
        // Update the form in the session
        session.setAttribute("form", currentForm);
        
        // Reset all status attributes to pending
        session.setAttribute("urgencyStatus", "⏳ Pending");
        session.setAttribute("infrastructureStatus", "⏳ Pending");
        session.setAttribute("locationStatus", "⏳ Pending");
        session.setAttribute("resourceStatus", "⏳ Pending");
        session.setAttribute("scalabilityStatus", "⏳ Pending");
        
        // Reset all decision attributes
        session.setAttribute("urgencyDecision", null);
        session.setAttribute("infrastructureDecision", null);
        session.setAttribute("locationDecision", null);
        session.setAttribute("resourceDecision", null);
        session.setAttribute("scalabilityAndPerformanceDecision", null);
        
        // Clear any reasoning data
        session.setAttribute("resourceReasoningData", null);
        session.setAttribute("scalabilityReasoningData", null);
        
        // Add the status attributes to flash attributes to ensure they're available after redirect
        redirectAttributes.addFlashAttribute("urgencyStatus", "⏳ Pending");
        redirectAttributes.addFlashAttribute("infrastructureStatus", "⏳ Pending");
        redirectAttributes.addFlashAttribute("locationStatus", "⏳ Pending");
        redirectAttributes.addFlashAttribute("resourceStatus", "⏳ Pending");
        redirectAttributes.addFlashAttribute("scalabilityStatus", "⏳ Pending");
        
        // Signal to client that reset was performed
        redirectAttributes.addFlashAttribute("resetPerformed", true);
        redirectAttributes.addFlashAttribute("message", "Workflow has been completely reset. All selections cleared.");
        
        System.out.println("Reset completed successfully");
        
        // Add cache-busting parameter
        return "redirect:/?reset=" + System.currentTimeMillis();
        
    } catch (Exception e) {
        System.err.println("Error during reset: " + e.getMessage());
        e.printStackTrace();
        
        // Add error message
        redirectAttributes.addFlashAttribute("error", "An error occurred during reset. Please try again.");
        return "redirect:/";
    }
}


@GetMapping("/index")
public String showIndexPage(Model model, SessionStatus sessionStatus){
    // Ensure the 'form' attribute is present, create a new one if not.
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new WorkflowForm());
    }
    // If the form is supposed to be reset, you can reset the session here.
    // sessionStatus.setComplete(); // Uncomment if you need to reset the session

    return "workflow/index"; // Make sure the 'index.html' exists under 'src/main/resources/templates/workflow'
}

@GetMapping("/location")
public String showLocationPage(Model model) {
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new WorkflowForm());
    }
    return "workflow/location"; // Επιστρέφει τη σελίδα location.html
}


@GetMapping("/yaml-generated")
public String showYamlGen(Model model, HttpSession session) {
    System.out.println("🚀 DEBUG: showYamlGen() triggered. Checking session data...");

    // Retrieve decision and results list from session
    String yamlDecision = (String) session.getAttribute("finalYamlDecision");
    List<Map<String, Object>> resultsList = (List<Map<String, Object>>) session.getAttribute("resultsList");

    if (yamlDecision == null) {
        yamlDecision = "No decision available";
    }

    if (resultsList == null || resultsList.isEmpty()) {
        resultsList = new ArrayList<>();
        System.out.println("❌ No results found in session.");
    } else {
        System.out.println("✅ Results List Loaded Successfully: " + resultsList);
    }

    // Pass data to Thymeleaf
    model.addAttribute("finalYamlDecision", yamlDecision);
    model.addAttribute("resultsList", resultsList);

    return "workflow/yamlgen";
}



@GetMapping("/resource-requirements")
public String showResourcesPage(Model model) {
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new WorkflowForm());
    }
    return "workflow/resource-requirements"; // Επιστρέφει το infrtype.html στον κατάλογο templates/workflow
}


@GetMapping("/scalability-performance")
public String showScalabilityAndPerformance(Model model) {
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new WorkflowForm());
    }
    return "workflow/scalability-performance"; // Επιστρέφει στον κατάλογο templates/workflow
}
@GetMapping("/infrtype")
public String showInfrastructureTypePage(Model model) {
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new WorkflowForm());
    }
    return "workflow/infrtype"; // Επιστρέφει το infrtype.html στον κατάλογο templates/workflow
}

@GetMapping("/infrbroadcat")
public String showInfrBroadCatPage(Model model) {
    if (!model.containsAttribute("form")) {
        model.addAttribute("form", new WorkflowForm());
    }
    return "workflow/infrbroadcat"; // Επιστρέφει το infrbroadcat στον κατάλογο templates/workflow
}



//     @GetMapping("/combined-decision-result")
//     public String showCombinedDecisionResult(@ModelAttribute("form") WorkflowForm form, Model model) {
//        // model.addAttribute("form", new WorkflowForm());
//            // Εκτέλεση Gorgias Query για την τελική απόφαση
    
//     GorgiasQueryResult finalDecisionResult = gorgiasService.executeGorgiasQueryForFinalDecision(form);

//     // Ελέγχουμε αν υπάρχει σφάλμα
//     if (finalDecisionResult.isHasError()) {
//         System.out.println("Error in Final Decision: " + finalDecisionResult.getErrorMsg());
//         model.addAttribute("error", "Error in processing final decision.");
//     } else if (finalDecisionResult.isHasResult()) {
//         // Αποθήκευση της τελικής απόφασης και εξήγησης στη φόρμα και το μοντέλο
//         List<QueryResult> results = finalDecisionResult.getResult();
//         String finalDecision = results.get(0).getExplanationStr();

//         // Εξαγωγή απόφασης χωρίς παρενθέσεις
//         int start = finalDecision.indexOf('(') + 1;
//         int end = finalDecision.indexOf(')');
//         if (start != 0 && end != -1) {
//             form.setFinalDecision(finalDecision.substring(start, end));
//         }

//         model.addAttribute("combinedDecision", form.getFinalDecision());
//         model.addAttribute("combinedExplanation", results.get(0).getHumanExplanation());
//     }
//     String urgencyValue = form.getUrgencyDecision().replace("urgency(", "").replace(")", "");
//    // String infrastructureValue = form.getInfrastructureDecision().replace("propose_infrastructure(", "").replace(")", "");
 

//     // Προσθήκη των intermediate αποφάσεων στο μοντέλο
//    // System.out.println("sadasdsadsad"+infrastructureValue);
//     model.addAttribute("infrastructureDecision", form.getInfrastructureDecision());
//     model.addAttribute("scalabilityAndPerformanceDecision", form.getScalabilityAndPerformanceDecision());
//     model.addAttribute("urgencyDecision", urgencyValue);
//     model.addAttribute("scalingDecision", form.getScalingDecision());
//     model.addAttribute("resourceDecision", form.getResourceDecision());
//     model.addAttribute("locationDecision", form.getLocationDecision());

//         model.addAttribute("infrastructureDecision", form.getInfrastructureDecision());
//         model.addAttribute("scalabilityAndPerformanceDecision", form.getScalabilityAndPerformanceDecision());

//         return "workflow/combined-decision-result";
//     }
boolean isPending(String status) {
    return status == null || "⏳ Pending".equals(status);
}
    
@GetMapping("/combined-decision-result")
public String showCombinedDecisionResult(HttpSession session, Model model) {
    try {
        // Retrieve form from session or create a new one if missing
        WorkflowForm form = (WorkflowForm) session.getAttribute("form");
        if (form == null) {
            form = new WorkflowForm(); // Initialize a new form if missing
            session.setAttribute("form", form);
        }

        // Retrieve statuses from session
        String urgencyStatus = (String) session.getAttribute("urgencyStatus");
        String infrastructureStatus = (String) session.getAttribute("infrastructureStatus");
        String locationStatus = (String) session.getAttribute("locationStatus");
        String resourceStatus = (String) session.getAttribute("resourceStatus");
        String scalabilityStatus = (String) session.getAttribute("scalabilityStatus");

 // Print the collected parameters along with their decisions
//  System.out.println("Collected Parameters and Decisions:");
//  System.out.println("----------------------------------------------------");
//  System.out.println("Primary Goal: " + form.getPrimaryGoal());
//  System.out.println("Control Requirement: " + form.getControlRequirement());
//  System.out.println("Data Sensitivity: " + form.isDataSensitivity());
//  System.out.println("Integration Requirements: " + form.isIntegrationRequirements());
//  System.out.println("Budget: " + form.getBudget());
//  System.out.println("Disaster Recovery Needs: " + form.getDisasterRecoveryNeeds());
//  System.out.println("Latency Requirement: " + form.getLatencyRequirement());
//  System.out.println("Resource Priority: " + form.getResourcePriority());
//  System.out.println("Performance Requirement: " + form.getPerformanceRequirement());
//  System.out.println("Processing Optimization: " + form.getProcessingOptimization());
//  System.out.println("----------------------------------------------------");
//  System.out.println("Decisions:");
//  System.out.println("Urgency Decision: " + form.getUrgencyDecision());
//  System.out.println("Infrastructure Decision: " + form.getInfrastructureDecision());
//  System.out.println("Scalability and Performance Decision: " + form.getScalabilityAndPerformanceDecision());

//  System.out.println("Resource Decision: " + form.getResourceDecision());
//  System.out.println("Location Decision: " + form.getLocationDecision());
//  System.out.println("----------------------------------------------------");    
        // Ensure safe retrieval of form attributes, setting "Not Provided" if status is pending
        model.addAttribute("urgencyDecision", 
        (!isPending(urgencyStatus) && form.getUrgencyDecision() != null) 
        ? form.getUrgencyDecision().replace("urgency(", "").replace(")", "") 
        : null);
    
    model.addAttribute("infrastructureDecision", 
        (!isPending(infrastructureStatus) && form.getInfrastructureDecision() != null) 
        ? form.getInfrastructureDecision() 
        : null);
    
    model.addAttribute("scalabilityAndPerformanceDecision", 
        (!isPending(scalabilityStatus) && form.getScalabilityAndPerformanceDecision() != null) 
        ? form.getScalabilityAndPerformanceDecision() 
        : null);
    
    model.addAttribute("scalingDecision", 
        (!isPending(scalabilityStatus) && form.getScalingDecision() != null) 
        ? form.getScalingDecision() 
        : null);
    
    model.addAttribute("resourceDecision", 
        (!isPending(resourceStatus) && form.getResourceDecision() != null) 
        ? form.getResourceDecision() 
        : null);
    
    model.addAttribute("locationDecision", 
        (!isPending(locationStatus) && form.getLocationDecision() != null) 
        ? form.getLocationDecision() 
        : null);
    
        
        return "workflow/combined-decision-result";

    } catch (Exception e) {
        // Log error and return the page safely
        System.err.println("Unexpected error: " + e.getMessage());
        model.addAttribute("error", "An unexpected error occurred while processing the decision.");
        return "workflow/combined-decision-result";
    }
}





    @GetMapping("/step2")
    public String showSecondForm(Model model) {
        if (!model.containsAttribute("form")) {
            return "redirect:/workflow/index"; // Redirect to start if 'form' is not in the model
        }
  
        return "workflow/step2";
    }

    @GetMapping("/step3")
    public String showThirdForm(@ModelAttribute("form") WorkflowForm form, Model model) {
        if (form == null) {
            // If 'form' is not present or missing important data, redirect to the index
            return "redirect:/workflow/index";
        }
    
        // Add the form to the model if it's not already present
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", form);
        }
    
        return "workflow/step3";
    }


    @GetMapping("/determineAzureServiceWorkflow/buildNew")
public String showNewBuild(Model model) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/newBuild"; // the view name should match the template location
}

   @GetMapping("/determineAzureServiceWorkflow/migrate")
public String showMigrate(Model model) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/migrate/migrate"; // the view name should match the template location
}
@GetMapping("/yaml-result")
public String showYamlResult(Model model) {
    // Add attributes and logic needed for the newBuild page
    return "workflow/yaml-result"; // the view name should match the template location
}

   @GetMapping("/determineAzureServiceWorkflow/migrate/liftAndShift")
public String showContainerizedOrNot(Model model) {
    // Add attributes and logic needed for the newBuild page

    return "determineAzureServiceWorkflow/migrate/liftAndShift/containerizedOrNot/containerizedOrNot"; // the view name should match the template location
}
   @GetMapping("/determineAzureServiceWorkflow/migrate/liftAndShift/containerized")
public String showContainerized(Model model) {
    // Add attributes and logic needed for the newBuild page

    return "determineAzureServiceWorkflow/migrate/liftAndShift/containerizedOrNot/containerized/containerized"; // the view name should match the template location
}
   @GetMapping("/determineAzureServiceWorkflow/migrate/liftAndShift/notContainerized")
public String showNotContainerized(Model model) {
    // Add attributes and logic needed for the newBuild page

    return "determineAzureServiceWorkflow/migrate/liftAndShift/containerizedOrNot/notContainerized/notContainerized"; // the view name should match the template location
}

   @GetMapping("/determineAzureServiceWorkflow/migrate/liftAndShift/notContainerized/cotsapp")
public String showCotsapp(Model model) {
    // Add attributes and logic needed for the newBuild page

    return "determineAzureServiceWorkflow/migrate/liftAndShift/containerizedOrNot/notContainerized/cotsappOrNot/cotsapp"; // the view name should match the template location
}

   @GetMapping("/determineAzureServiceWorkflow/migrate/liftAndShift/notContainerized/notCotsapp")
public String showNotCotsapp(Model model) {
    // Add attributes and logic needed for the newBuild page

    return "determineAzureServiceWorkflow/migrate/liftAndShift/containerizedOrNot/notContainerized/cotsappOrNot/notCotsapp"; // the view name should match the template location
}


    @GetMapping("/determineAzureServiceWorkflow/buildNew/fullControl")
public String showfullControl(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    form.setServiceName("Virtual Machine");
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/fullControl"; // the view name should match the template location
}
 @GetMapping("/determineAzureServiceWorkflow/buildNew/notControl")
public String showNotControl(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/notControl"; // the view name should match the template location
}
@GetMapping("/determineAzureServiceWorkflow/buildNew/notControl/HPCWorkload")
public String showHPCWokrload(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
            // Ρύθμιση του serviceName στο WorkflowForm
    form.setServiceName("Azure Batch"); // Θέτουμε το serviceName στο "Azure Batch" για αυτήν την περίπτωση

    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/HPCOrNotWorkload/HPCWorkload"; // the view name should match the template location
}




@GetMapping("/determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload")
public String showNotHPCWokrload(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/HPCOrNotWorkload/notHPCWorkload"; // the view name should match the template location
}

@GetMapping("/determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/SBapps")
public String showSBapps(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    form.setServiceName("Spring Boot Apps");
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/HPCOrNotWorkload/SBOrNotApps/SBapps"; // the view name should match the template location
}


@GetMapping("/determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/notSBapps")
public String shownotSBapps(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/HPCOrNotWorkload/SBOrNotApps/notSBapps"; // the view name should match the template location
}

@GetMapping("/determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/notSBapps/notEventDriven")
public String showNotEventDriven(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/HPCOrNotWorkload/SBOrNotApps/eventOrNotDrivenWorkload/notEventDrivenWorkload"; // the view name should match the template location
}

@GetMapping("/determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/notSBapps/eventDriven")
public String showEventDriven(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    // Add attributes and logic needed for the newBuild page
    return "determineAzureServiceWorkflow/buildNew/fullOrNotControl/HPCOrNotWorkload/SBOrNotApps/eventOrNotDrivenWorkload/eventDrivenWorkload"; // the view name should match the template location
}

@PostMapping("/buildNewOrMigration")
public String buildNewOrMigration(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("newBuild".equals(form.getBuildNewOrMigrate())) {
        return "redirect:determineAzureServiceWorkflow/buildNew";
    } else {
        // Handle other cases
        return "redirect:determineAzureServiceWorkflow/migrate";
    }
}

@PostMapping("/fullOrNotControl")
public String fullOrNotControl(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getFullControl())) {
        return "redirect:determineAzureServiceWorkflow/buildNew/fullControl";
    } else {
        // Handle other cases
        return"redirect:determineAzureServiceWorkflow/buildNew/notControl";
    }
}
    @PostMapping("/HPCOrNotWorkload")
    public String HPCOrNotWorkload(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getHPCWorkload())) {
        return "redirect:determineAzureServiceWorkflow/buildNew/notControl/HPCWorkload";
    } else {
        // Handle other cases
        return"redirect:determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload";
    }
}

 @PostMapping("/cloudOrNotOptimised")
    public String cloudOrNotOptimised(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getCloudOptimised())) {
        return "redirect:determineAzureServiceWorkflow/buildNew/notControl";
    } else {
        // Handle other cases
        return"redirect:determineAzureServiceWorkflow/migrate/liftAndShift";
    }
}

 @PostMapping("/SBOrNotApps")
    public String SBOrNotApps(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getSBapps())) {
        return "redirect:determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/SBapps";
    } else {
        // Handle other cases
        return"redirect:determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/notSBapps";
    }
}

@PostMapping("/eventDrivenOrNot")
    public String eventDrivenOrNot(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getEventDriven())) {
        return "redirect:determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/notSBapps/eventDriven";
    } else {
        // Handle other cases
        return"redirect:determineAzureServiceWorkflow/buildNew/notControl/notHPCWorkload/notSBapps/notEventDriven";
    }
}


    @PostMapping("/submit-to-gorgias")
    public String handleFormSubmission(
            @ModelAttribute("form") WorkflowForm form,
            RedirectAttributes redirectAttributes) {
           // System.out.println("Form received with step: " + form.getStep()); // Add this line for debugging
           // Log the form fields
           log.info("Form attributes: {}", form);

        workflowService.processFormData(form);
        // Handle the new form data
        
        
       // String chosenService = workflowService.determineAzureService(form);
        
        
        String nextStep = workflowService.determineNextStep(form);

        redirectAttributes.addFlashAttribute("form", form);
        if ("complete".equals(nextStep)) {
            return "redirect:/workflow/complete"; // Redirect to the completion page
        } else {
            return "redirect:/workflow/" + nextStep; // Redirect to the next step
        }
        // if(nextStep.equals("step3"))
        // {
        //     String chosenService = workflowService.determineAzureService(form);
            

        //     return "redirect:/workflow/determineAzureServiceWorkflow/determineAzureService";
        // }
        // else {
           
        //     return "redirect:/workflow/" + nextStep;
        // }

    }

    @GetMapping("/complete")
    public String showCompletion(@ModelAttribute("form") WorkflowForm form, Model model) {
        if (form == null) {
            return "redirect:/workflow/index"; // Redirect to start if 'form' is not in the model
        }
        return "workflow/complete";
    }
    @PostMapping("/create-yaml")
    public String createYamlForService(@ModelAttribute("form") WorkflowForm form, RedirectAttributes redirectAttributes) {
        try {
            // Κλήση της μεθόδου δημιουργίας YAML με βάση το επιλεγμένο service
            createAzureYamlFileForService(form.getServiceName());
    
            // Προσθήκη μηνύματος επιβεβαίωσης στο RedirectAttributes
            redirectAttributes.addFlashAttribute("message", "Το YAML αρχείο δημιουργήθηκε επιτυχώς για το service: " + form.getServiceName());
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("error", "Σφάλμα κατά τη δημιουργία του YAML αρχείου: " + e.getMessage());
            e.printStackTrace();
        }
        return "redirect:/workflow/yaml-result";
    }
    
    private void createAzureYamlFileForService(String serviceName) throws IOException {
        Map<String, Object> yamlData = new HashMap<>();
        List<Map<String, Object>> resources = new ArrayList<>();
    
        if ("Azure Batch".equalsIgnoreCase(serviceName)) {
            Map<String, Object> batchService = new HashMap<>();
            batchService.put("type", "Microsoft.Batch/batchAccounts");
            batchService.put("name", "myAzureBatch");
            batchService.put("location", "eastus");
            resources.add(batchService);
        } else if ("Azure Spring Apps".equalsIgnoreCase(serviceName)) {
            Map<String, Object> springApp = new HashMap<>();
            springApp.put("type", "Microsoft.AppPlatform/Spring");
            springApp.put("name", "mySpringApp");
            springApp.put("location", "eastus");
            resources.add(springApp);
        } else if ("Virtual Machine".equalsIgnoreCase(serviceName)) {
            Map<String, Object> virtualMachine = new HashMap<>();
            virtualMachine.put("type", "Microsoft.Compute/virtualMachines");
            virtualMachine.put("name", "myAzureVM");
            virtualMachine.put("location", "eastus");
    
            Map<String, Object> hardwareProfile = new HashMap<>();
            hardwareProfile.put("vmSize", "Standard_D2s_v3");  // Μέγεθος της VM
    
            Map<String, Object> osDisk = new HashMap<>();
            osDisk.put("createOption", "FromImage");
    
            Map<String, Object> storageProfile = new HashMap<>();
            storageProfile.put("osDisk", osDisk);
    
            Map<String, Object> networkInterface = new HashMap<>();
            networkInterface.put("id", "[resourceId('Microsoft.Network/networkInterfaces', 'myNic')]");
    
            List<Map<String, Object>> networkInterfacesList = new ArrayList<>();
            networkInterfacesList.add(networkInterface);
    
            Map<String, Object> networkProfile = new HashMap<>();
            networkProfile.put("networkInterfaces", networkInterfacesList);
    
            Map<String, Object> vmProperties = new HashMap<>();
            vmProperties.put("hardwareProfile", hardwareProfile);
            vmProperties.put("storageProfile", storageProfile);
            vmProperties.put("networkProfile", networkProfile);
    
            virtualMachine.put("properties", vmProperties);
            resources.add(virtualMachine);
        }
        else if ("Spring Boot Apps".equalsIgnoreCase(serviceName)) {
            Map<String, Object> springBootApp = new HashMap<>();
            springBootApp.put("type", "Microsoft.AppPlatform/Spring/bootApps");
            springBootApp.put("name", "mySpringBootApp");
            springBootApp.put("location", "eastus");
    
            Map<String, Object> sku = new HashMap<>();
            sku.put("name", "B0");
            sku.put("tier", "Basic");
    
            springBootApp.put("sku", sku);
    
            Map<String, Object> properties = new HashMap<>();
            properties.put("public", true);
    
            Map<String, Object> configServer = new HashMap<>();
            configServer.put("uri", "https://config-server.example.com");
            properties.put("configServer", configServer);
    
            springBootApp.put("properties", properties);
            resources.add(springBootApp);
        }
    
        // Άλλες επιλογές services μπορούν να προστεθούν εδώ
    
        yamlData.put("resources", resources);
    
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
    
        try (FileWriter writer = new FileWriter("azure_infrastructure.yaml")) {
            yaml.dump(yamlData, writer);
            // Αντιγραφή του αρχείου στο φάκελο static για λήψη
            Files.copy(Paths.get("azure_infrastructure.yaml"), Paths.get("src/main/resources/static/azure_infrastructure.yaml"), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    @PostMapping("/process-urgency")
    public String processUrgency(@ModelAttribute("form") WorkflowForm form, HttpSession session, Model model) {
        System.out.println("🚀 Executing Gorgias Query for Urgency...");
    
        // Store input values in model
        prepareModelWithFormData(model, form);
    
        // Execute Gorgias query
        List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForUrgency(form, session);
        
        if (parsedResults != null && !parsedResults.isEmpty()) {
            // Deduplicate results before processing
            List<ParsedResult> deduplicatedResults = deduplicateResults(parsedResults);
            processAndStoreResults(deduplicatedResults, session, model, form);
        } else {
            handleNoResultsFound(session, model);
        }
    
        return "workflow/urgency-result";
    }
    /**
 * Deduplicate parsed results based on both main result and supporting facts
 */
public List<ParsedResult> deduplicateResults(List<ParsedResult> parsedResults) {
    Map<String, ParsedResult> uniqueResults = new LinkedHashMap<>();
    
    for (ParsedResult result : parsedResults) {
        // Create a unique key combining the main result and supporting facts
        String mainResult = result.getMainResult();
        
        // Get a string representation of the supporting facts
        String factString = result.getSupportingFacts().stream()
                .map(fact -> fact.replaceAll("^\\s*•+\\s*", "").trim())
                .collect(Collectors.joining("|"));
        
        // Create a composite key for deduplication
        String compositeKey = mainResult + ":" + factString;
        
        // Only add if we haven't seen this exact combination before
        if (!uniqueResults.containsKey(compositeKey)) {
            // Clean supporting facts
            List<String> cleanedFacts = result.getSupportingFacts().stream()
                    .map(fact -> fact.replaceAll("^\\s*•+\\s*", "").trim())
                    .filter(fact -> !fact.isEmpty())
                    .collect(Collectors.toList());
            
            uniqueResults.put(compositeKey, new ParsedResult(mainResult, cleanedFacts));
        }
    }
    
    return new ArrayList<>(uniqueResults.values());
}

private void prepareModelWithFormData(Model model, WorkflowForm form) {
    model.addAttribute("startDate", form.getStartDate());
    model.addAttribute("agencyCategory", form.getAgencyCategory());
    model.addAttribute("requestType", form.getRequestType());
    model.addAttribute("contractWithContractor", form.getContractWithContractor());
}

private void processAndStoreResults(List<ParsedResult> parsedResults, HttpSession session, Model model, WorkflowForm form) {
    List<Map<String, Object>> resultsList = new ArrayList<>();
    Set<String> uniqueUrgencyDecisions = new LinkedHashSet<>();

    for (ParsedResult result : parsedResults) {
        Map<String, Object> resultMap = new HashMap<>();
        processParsedResult(result, resultMap, form);  // Passing form here
        resultsList.add(resultMap);
        uniqueUrgencyDecisions.add(resultMap.get("naturalLanguageMainResult").toString());
    }

    String finalUrgencyDecision = String.join("; ", uniqueUrgencyDecisions);
    updateSessionAndModelWithResults(session, model, finalUrgencyDecision, resultsList, form);
}

private void processParsedResult(ParsedResult result, Map<String, Object> resultMap, WorkflowForm form) {
    String rawResult = result.getMainResult();
    System.out.println("Raw Urgency Result: " + rawResult);

    String extractedUrgency = extractUrgencyFromResult(rawResult);
    String humanReadableUrgency = gorgiasService.mapMainResultToNaturalLanguage(extractedUrgency, form).trim();

    resultMap.put("naturalLanguageMainResult", humanReadableUrgency);
    resultMap.put("convertedFact", convertFactsToNaturalLanguage(result.getSupportingFacts()));
}


private String extractUrgencyFromResult(String rawResult) {
    if (rawResult.contains("(") && rawResult.contains(")")) {
        return rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'));
    }
    return rawResult;
}

private List<String> convertFactsToNaturalLanguage(List<String> supportingFacts) {
    return supportingFacts.stream()
        .map(fact -> gorgiasService.convertFactToNaturalLanguage(fact.trim().replaceAll("[\"']", "")))
        .collect(Collectors.toList());
}

private void updateSessionAndModelWithResults(HttpSession session, Model model, String urgencyDecision, List<Map<String, Object>> resultsList, WorkflowForm form) {
    form.setUrgencyDecision(urgencyDecision);
    model.addAttribute("urgencyDecision", urgencyDecision);
    session.setAttribute("urgencyDecision", urgencyDecision);
    session.setAttribute("urgencyStatus", "✅ Completed");
    model.addAttribute("resultsList", resultsList);
}

private void handleNoResultsFound(HttpSession session, Model model) {
    session.setAttribute("urgencyStatus", "❌ No Decision");
    model.addAttribute("error", "No results found or an error occurred.");
}







    // @PostMapping("/process-urgency")
    // public String processUrgency(@ModelAttribute("form") WorkflowForm form, HttpSession session, Model model) {
    //     System.out.println("Executing Gorgias Query for Urgency...");
    
    //     // Store input values in model
    //     model.addAttribute("startDate", form.getStartDate());
    //     model.addAttribute("agencyCategory", form.getAgencyCategory());
    //     model.addAttribute("requestType", form.getRequestType());
    //     model.addAttribute("contractWithContractor", form.getContractWithContractor());
    
    //     // Execute Gorgias query
    //     List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForUrgency(form, session);
    
    //     if (parsedResults != null && !parsedResults.isEmpty()) {
    //         List<Map<String, Object>> resultsList = new ArrayList<>();
    //         Set<String> uniqueUrgencyDecisions = new LinkedHashSet<>(); // To remove duplicates
    
    //         for (ParsedResult result : parsedResults) {
    //             Map<String, Object> resultMap = new HashMap<>();
    
    //             // Extract raw result
    //             String rawResult = result.getMainResult();
    //             System.out.println("Raw Urgency Result: " + rawResult);
    
    //             String extractedUrgency = rawResult.contains("(") && rawResult.contains(")")
    //                     ? rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'))
    //                     : rawResult;
    
    //             System.out.println("Extracted Urgency Decision: " + extractedUrgency);
    
    //             // Convert to human-readable text
    //             String humanReadableUrgency = gorgiasService.mapMainResultToNaturalLanguage(extractedUrgency, form).trim();
    //             if (!humanReadableUrgency.isEmpty()) {
    //                 uniqueUrgencyDecisions.add(humanReadableUrgency);
    //             }
    
    //             // Convert supporting facts
    //             List<String> supportingFacts = result.getSupportingFacts();
    //             List<String> naturalLanguageFacts = new ArrayList<>();
    //             for (String fact : supportingFacts) {
    //                 String convertedFact = gorgiasService.convertFactToNaturalLanguage(fact.trim().replaceAll("[\"']", ""));
    //                 naturalLanguageFacts.add(convertedFact);
    //             }
    
    //             // Store data
    //             resultMap.put("naturalLanguageMainResult", humanReadableUrgency);
    //             resultMap.put("convertedFact", naturalLanguageFacts);
    //             resultsList.add(resultMap);
    //         }
    
    //         // Store final decision
    //         String finalUrgencyDecision = String.join("; ", uniqueUrgencyDecisions);
    //         form.setUrgencyDecision(finalUrgencyDecision);
    //         System.out.println("DEBUG: Urgency Decision = " + finalUrgencyDecision);
    
    //         // ✅ Store in session & update Welcome Page
    //         model.addAttribute("urgencyDecision", finalUrgencyDecision);
    //         session.setAttribute("urgencyDecision", finalUrgencyDecision);
    
    //         session.setAttribute("urgencyStatus", "✅ Completed"); // ✅ Mark as Completed
    //         System.out.println("Session after processing: urgencyStatus=" + 
    //             session.getAttribute("urgencyStatus") + 
    //             ", urgencyDecision=" + session.getAttribute("urgencyDecision"));
    
    //         // ✅ Add to model
    //            System.out.println("Session Attributes Before Rendering Home Page:");
    //  Enumeration<String> attributeNames = session.getAttributeNames();
    //  while (attributeNames.hasMoreElements()) {
    //      String name = attributeNames.nextElement();
    //      System.out.println(name + " = " + session.getAttribute(name));
    //  }
    //         model.addAttribute("resultsList", resultsList);
    //         model.addAttribute("urgencyDecision","✅ Completed");
    //         model.addAttribute("form", form);
    //         System.out.println("Session Attributes Before Rendering Home Page:");
    //          attributeNames = session.getAttributeNames();
    //         while (attributeNames.hasMoreElements()) {
    //             String name = attributeNames.nextElement();
    //             System.out.println(name + " = " + session.getAttribute(name));
    //         }
    //     } else {
    //         // If no results were found, set status as "No Decision"
    //         session.setAttribute("urgencyStatus", "❌ No Decision");
    //         model.addAttribute("error", "No results found or an error occurred.");
    //     }
    
    //     return "workflow/urgency-result";
    // }
    
    



//     @PostMapping("/process-urgency")
// public String processUrgency(@ModelAttribute("form") WorkflowForm form, Model model) {
//     // Εκτέλεση του Gorgias Query για το Urgency
//     GorgiasQueryResult gorgiasResponseForUrgency = gorgiasService.executeGorgiasQueryForUrgency(form);
    
//     if (gorgiasResponseForUrgency != null && gorgiasResponseForUrgency.isHasError()) {
//         System.out.println(gorgiasResponseForUrgency.getErrorMsg());
//     } else if (gorgiasResponseForUrgency != null && gorgiasResponseForUrgency.isHasResult()) {
//         // Ανάλυση και καταγραφή αποτελεσμάτων
//         List<QueryResult> results = gorgiasResponseForUrgency.getResult();
//         if (!results.isEmpty()) {
//             QueryResult result = results.get(0);  // Παίρνουμε το πρώτο αποτέλεσμα για παρουσίαση
//             String explanation = result.getExplanationStr();
//             String humanExplanation = result.getHumanExplanation();
            
//             // Αποθήκευση αποτελεσμάτων στη φόρμα και στο μοντέλο
//             explanation = explanation.replaceAll("[\\[\\]()]", "");
//             form.setUrgencyDecision(result.getVariables().get("X"));
            
//            // form.setUrgencyDecision(explanation);
//             //System.out.println(form.getUrgencyDecision());
//             model.addAttribute("urgencyExplanation", explanation);
//             model.addAttribute("urgencyDecision", form.getUrgencyDecision());

            
//             model.addAttribute("urgencyHumanExplanation", humanExplanation);
//         }
//     } else {
//         System.out.println("No results for urgency query.");
//         model.addAttribute("urgencyExplanation", "No results available.");
//         model.addAttribute("urgencyHumanExplanation", "The system could not determine an urgency level based on the input provided.");
//     }
    
//     // Προσθήκη των δεδομένων του query στο μοντέλο για προβολή στην σελίδα `urgency-result`
//     model.addAttribute("urgencyDecision", form.getUrgencyDecision());
//     model.addAttribute("gorgiasResponseForUrgency", gorgiasResponseForUrgency);
    
//     return "workflow/urgency-result"; // Σελίδα εμφάνισης αποτελεσμάτων
// }


@PostMapping("/process-location")
public String processLocation(@ModelAttribute("form") WorkflowForm form, Model model,HttpSession session) {
    System.out.println("Executing Gorgias Query for Location...");
    
    // Execute the query
    List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForLocation(form);
    
    if (parsedResults == null || parsedResults.isEmpty()) {
        System.out.println("Gorgias Query returned no results.");
        model.addAttribute("error", "No results found or an error occurred.");
        return "workflow/location-result";
    }

    System.out.println("Results found: " + parsedResults.size());

    List<Map<String, Object>> resultsList = new ArrayList<>();
    Set<String> locationDecisions = new LinkedHashSet<>(); // Unique decisions

    for (ParsedResult result : parsedResults) {
        Map<String, Object> resultMap = new HashMap<>();
        
        // Extract and normalize location decision
        String rawResult = result.getMainResult();
        System.out.println("Raw Result: " + rawResult);
        
        String extractedLocation = rawResult.contains("(") && rawResult.contains(")")
                ? rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'))
                : rawResult;
        
        System.out.println("Extracted Location: " + extractedLocation);

        // Convert to human-readable format
        String humanReadableLocation = gorgiasService.mapMainResultToNaturalLanguage(extractedLocation, form).trim();
        if (!humanReadableLocation.isEmpty()) {
            locationDecisions.add(humanReadableLocation);
        }

        // Process supporting facts (convert to natural language)
        List<String> supportingFacts = result.getSupportingFacts();
        List<String> naturalLanguageFacts = new ArrayList<>();
        for (String fact : supportingFacts) {
            String convertedFact = gorgiasService.convertFactToNaturalLanguage(fact.trim().replaceAll("[\"']", ""));
            naturalLanguageFacts.add(convertedFact);
        }

        // Store processed data
        resultMap.put("naturalLanguageMainResult", humanReadableLocation);
        resultMap.put("convertedFact", naturalLanguageFacts);
        resultsList.add(resultMap);
    }

    // Store unique location decisions as a **List**
    form.setLocationDecision(String.join(";", locationDecisions)); 
    session.setAttribute("locationDecision", form.getLocationDecision());
    
            session.setAttribute("locationStatus", "✅ Completed");
    model.addAttribute("resultsList", resultsList);
    model.addAttribute("form", form);

    return "workflow/location-result";
}






// @PostMapping("/process-infrastructure")
// public String processInfrastructure(@ModelAttribute("form") WorkflowForm form, Model model) {
//     // Εκτέλεση του Gorgias Query για το Infrastructure
//     GorgiasQueryResult gorgiasResponseForInfrastructure = gorgiasService.executeGorgiasQueryForInfrastructure(form);
    
//     // Έλεγχος αν υπάρχει σφάλμα στο αποτέλεσμα
//     if (gorgiasResponseForInfrastructure.isHasError()) {
//         System.out.println(gorgiasResponseForInfrastructure.getErrorMsg());
//     } else {
//         // Αν δεν υπάρχει σφάλμα, ελέγχουμε αν υπάρχουν αποτελέσματα
//         if (gorgiasResponseForInfrastructure.isHasResult()) {
//             List<QueryResult> results = gorgiasResponseForInfrastructure.getResult();
//             for (QueryResult queryResult : results) {
//                 // Εκτύπωση αποτελεσμάτων και επεξήγηση
//                 System.out.println("ExplanationStr: " + queryResult.getExplanationStr());
//                 System.out.println(queryResult.getHumanExplanation());
//                 //pairno to apotelesma pou me endiaferei
//                 // Εκτύπωση του κειμένου εντός παρενθέσεων
//                 //System.out.println(form.getInfrastructureDecision());
//                 }

//              //   String decision = results.get(0).getExplanationStr();
                
//                 String decision = results.get(0).getVariables().get("X");
//                 form.setInfrastructureDecision(decision);
//                 System.out.println(decision);
//             //     int start = decision.indexOf('(') + 1; // Βρίσκουμε τη θέση μετά το ανοιχτό παρένθεση
//             //     int end = decision.indexOf(')'); // Βρίσκουμε τη θέση του κλειστού παρένθεση
        
//             //     if (start != 0 && end != -1) { // Έλεγχος αν υπάρχουν παρενθέσεις
//             //          form.setInfrastructureDecision(decision.substring(start, end));
//             //         //System.out.println(form.getInfrastructureDecision());
//             // }
//         }
//     }
//     boolean suggestScalabilityForm = form.isHighScalability() || form.isHighPerformance();
//     // Προσθήκη του αποτελέσματος στο μοντέλο για προβολή στη σελίδα
//     model.addAttribute("suggestScalabilityForm", suggestScalabilityForm);

//     model.addAttribute("gorgiasResponseForInfrastructure", gorgiasResponseForInfrastructure);
//     model.addAttribute("infrastructureDecision", form.getInfrastructureDecision());
//     // Κατεύθυνση στη σελίδα αποτελεσμάτων
//     return "workflow/infrastructure-result-page";
// }


@PostMapping("/process-infrastructure2")
public String processInfrastructure2(@ModelAttribute("form") WorkflowForm form, Model model) {
    // Εκτέλεση του Gorgias Query για το Infrastructure
    // GorgiasQueryResult gorgiasResponseForInfrastructure = gorgiasService.executeGorgiasQueryForInfrastructure(form);
    
    // // Έλεγχος αν υπάρχει σφάλμα στο αποτέλεσμα
    // if (gorgiasResponseForInfrastructure.isHasError()) {
    //     System.out.println(gorgiasResponseForInfrastructure.getErrorMsg());
    // } else {
    //     // Αν δεν υπάρχει σφάλμα, ελέγχουμε αν υπάρχουν αποτελέσματα
    //     if (gorgiasResponseForInfrastructure.isHasResult()) {
    //         List<QueryResult> results = gorgiasResponseForInfrastructure.getResult();
    //         for (QueryResult queryResult : results) {
    //             // Εκτύπωση αποτελεσμάτων και επεξήγηση
    //             System.out.println("ExplanationStr: " + queryResult.getExplanationStr());
    //             System.out.println(queryResult.getHumanExplanation());
    //             //pairno to apotelesma pou me endiaferei
    //             // Εκτύπωση του κειμένου εντός παρενθέσεων
    //             //System.out.println(form.getInfrastructureDecision());
    //             }

    //          //   String decision = results.get(0).getExplanationStr();
                
    //             String decision = results.get(0).getVariables().get("X");
    //             form.setInfrastructureDecision(decision);
    //             System.out.println(decision);
    //         //     int start = decision.indexOf('(') + 1; // Βρίσκουμε τη θέση μετά το ανοιχτό παρένθεση
    //         //     int end = decision.indexOf(')'); // Βρίσκουμε τη θέση του κλειστού παρένθεση
        
    //         //     if (start != 0 && end != -1) { // Έλεγχος αν υπάρχουν παρενθέσεις
    //         //          form.setInfrastructureDecision(decision.substring(start, end));
    //         //         //System.out.println(form.getInfrastructureDecision());
    //         // }
    //     }
    // }
    // boolean suggestScalabilityForm = form.isHighScalability() || form.isHighPerformance();
    // // Προσθήκη του αποτελέσματος στο μοντέλο για προβολή στη σελίδα
    // model.addAttribute("suggestScalabilityForm", suggestScalabilityForm);

    // model.addAttribute("gorgiasResponseForInfrastructure", gorgiasResponseForInfrastructure);
    // model.addAttribute("infrastructureDecision", form.getInfrastructureDecision());
    // // Κατεύθυνση στη σελίδα αποτελεσμάτων
    return "workflow/infrastructure2-result-page";
}



@PostMapping("/process-scalability")
public String processScalability(@ModelAttribute("form") WorkflowForm form, Model model, HttpSession session) {
    System.out.println("Executing Gorgias Query for Scalability & Performance...");

    // Store form attributes in the model
    model.addAttribute("expectedLoad", form.getExpectedLoad());
    model.addAttribute("peakTimes", form.getPeakTimes());
    model.addAttribute("responseTime", form.getResponseTime());
    model.addAttribute("costSensitivity", form.getCostSensitivity());

    // Execute Gorgias query
    List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForScalability(form);

    if (parsedResults == null || parsedResults.isEmpty()) {
        System.out.println("Gorgias Query returned no results.");
        model.addAttribute("error", "No results found or an error occurred.");
        return "workflow/scalability-result";
    }

    System.out.println("Results found: " + parsedResults.size());

    List<Map<String, Object>> resultsList = new ArrayList<>();
    List<String> scalabilityDecisions = new ArrayList<>();

    for (ParsedResult result : parsedResults) {
        Map<String, Object> resultMap = new HashMap<>();
        
        // Extract main result safely
        String rawResult = result.getMainResult();
        if (rawResult == null) {
            System.out.println("Error: Main result is null.");
            continue;  // Skip processing if null
        }

        System.out.println("Raw Result: " + rawResult);

        // Extract only X from scalability_decision(X)
        String extractedDecision = rawResult.contains("(") && rawResult.contains(")")
                ? rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'))
                : rawResult;

        System.out.println("Extracted Scalability Decision: " + extractedDecision);

        // Convert main result into human-readable format
        String humanReadableDecision = gorgiasService.mapMainResultToNaturalLanguage(extractedDecision, form);

        // Process supporting facts (convert to natural language)
        List<String> supportingFacts = result.getSupportingFacts();
        List<String> naturalLanguageFacts = new ArrayList<>();
        if (supportingFacts != null) {
            for (String fact : supportingFacts) {
                if (fact != null) {
                    String convertedFact = gorgiasService.convertFactToNaturalLanguage(fact.trim().replaceAll("[\"']", ""));
                    naturalLanguageFacts.add(convertedFact);
                }
            }
        }

        // Store processed data
        resultMap.put("naturalLanguageMainResult", humanReadableDecision);
        resultMap.put("convertedFact", naturalLanguageFacts);
        resultsList.add(resultMap);

        System.out.println("Converted Facts: " + naturalLanguageFacts);

        // Add extracted scalability decision
        scalabilityDecisions.add(humanReadableDecision);
    }

    if (scalabilityDecisions.isEmpty()) {
        model.addAttribute("error", "No valid decisions found.");
    } else {
        form.setScalabilityAndPerformanceDecision(String.join(", ", scalabilityDecisions));
        form.setScalabilityAndPerformanceDecision(removeDuplicates(form.getScalabilityAndPerformanceDecision()));
        session.setAttribute("scalabilityAndPerformanceDecision",form.getScalabilityAndPerformanceDecision() );
    
            session.setAttribute("scalabilityStatus", "✅ Completed"); 
        model.addAttribute("resultsList", resultsList);
    }

    model.addAttribute("form", form);
    return "workflow/scalability-result";
}


@PostMapping("/process-resource-requirements")
public String processResourceRequirements(@ModelAttribute("form") WorkflowForm form, Model model, HttpSession session) {
    System.out.println("Executing Gorgias Query for Resource Requirements...");
    
    // Execute the query
    List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForResources(form);
    
    if (parsedResults == null || parsedResults.isEmpty()) {
        System.out.println("Gorgias Query returned no results.");
        model.addAttribute("error", "No results found or an error occurred.");
        return "workflow/resource-requirements-result";
    }

    System.out.println("Results found: " + parsedResults.size());

    List<Map<String, Object>> resultsList = new ArrayList<>();
    List<String> resourceDecisions = new ArrayList<>();
    
    // Map to track complete resource descriptions to avoid splitting issues
    Map<String, Boolean> completeResourceMap = new HashMap<>();

    // First pass: Process all results and identify complete resource descriptions
    for (ParsedResult result : parsedResults) {
        String rawResult = result.getMainResult();
        System.out.println("Raw Result: " + rawResult);
        
        String extractedResource = rawResult.contains("(") && rawResult.contains(")")
                ? rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'))
                : rawResult;
        
        System.out.println("Extracted Resource Decision: " + extractedResource);

        // Convert main result into a human-readable format
        String humanReadableResource = gorgiasService.mapMainResultToNaturalLanguage(extractedResource, form);
        
        // Special handling for known resource descriptions that should be kept together
        if (humanReadableResource.contains("Real-Time Compute Optimized:")) {
            completeResourceMap.put("Real-Time Compute Optimized: High-performance computing resources configured for low-latency, responsive applications", true);
        } 
        else if (humanReadableResource.contains("Elastic Compute Optimized:")) {
            completeResourceMap.put("Elastic Compute Optimized: Auto-scaling compute resources that dynamically adjust to workload demands", true);
        }
        else {
            completeResourceMap.put(humanReadableResource, true);
        }

        // Process supporting facts
        List<String> supportingFacts = result.getSupportingFacts();
        List<String> naturalLanguageFacts = new ArrayList<>();
        for (String fact : supportingFacts) {
            String convertedFact = gorgiasService.convertFactToNaturalLanguage(fact.trim().replaceAll("[\"']", ""));
            naturalLanguageFacts.add(convertedFact);
        }

        // Store processed data
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("naturalLanguageMainResult", humanReadableResource);
        resultMap.put("convertedFact", naturalLanguageFacts);
        resultsList.add(resultMap);
    }

    // Convert the complete resource map keys to a list
    List<String> completeResourceList = new ArrayList<>(completeResourceMap.keySet());
    
    // KEY FIX: Use a delimiter that won't appear in the descriptions
    // Use pipe character (|) as a delimiter instead of comma
    form.setResourceDecision(String.join("|", completeResourceList));
    session.setAttribute("resourceDecision", form.getResourceDecision());
    session.setAttribute("resourceStatus", "✅ Completed"); 
    
    model.addAttribute("resultsList", resultsList);
    model.addAttribute("form", form);

    return "workflow/resource-requirements-result";
}


// @PostMapping("/process-resource-requirements")
// public String processResourceRequirements(@ModelAttribute("form") WorkflowForm form, Model model) {
//     // Εκτέλεση του ερωτήματος
//     Map<String, GorgiasQueryResult> results = gorgiasService.evaluateResourceRequirements(form);
//    // form.setResourceDecision(results.get("resource").getResult().get(0).getExplanationStr());
//     //form.setScalingDecision(results.get("scaling").getResult().get(0).getExplanationStr());
//     String resourceDecision = results.get("resource").getResult().get(0).getExplanationStr();
//     String scalingDecision = results.get("scaling").getResult().get(0).getExplanationStr();
//     int start = resourceDecision.indexOf('(') + 1; // Βρίσκουμε τη θέση μετά το ανοιχτό παρένθεση
//     int end = resourceDecision.indexOf(')'); // Βρίσκουμε τη θέση του κλειστού παρένθεση
//     int start1 = scalingDecision.indexOf('(') + 1;
//     int end1 = scalingDecision.indexOf(')'); 
//     if (start != 0 && end != -1) { // Έλεγχος αν υπάρχουν παρενθέσεις
//          form.setResourceDecision(resourceDecision.substring(start, end));   
//     }
//     if (start1 != 0 && end1 != -1) { // Έλεγχος αν υπάρχουν παρενθέσεις
//         form.setScalingDecision(scalingDecision.substring(start1, end1));   
//    }

//     System.out.println(form.getResourceDecision());
//     System.out.println(form.getScalingDecision());
//     // Προσθήκη των αποτελεσμάτων στο μοντέλο για εμφάνιση στην προβολή
//     model.addAttribute("resourceDecision", form.getResourceDecision());
//     model.addAttribute("scalingDecision", form.getScalingDecision());


//     model.addAttribute("gorgiasResponseForResource", results.get("resource"));
//     model.addAttribute("gorgiasResponseForScaling", results.get("scaling"));
//    // System.out.println(results.get("resource").getResult().get(0).getExplanationStr());

//     return "workflow/resource-requirements-result";
// }
    // @PostMapping("/process-decision")
    // public String processDecision(@ModelAttribute("form") WorkflowForm form, Model model)
    // {
        

       
        //GorgiasResponse gorgiasResponse = gorgiasService.processDecision(form);
//          GorgiasQueryResult gorgiasResponseForUrgency = gorgiasService.executeGorgiasQueryForUrgency(form);
//          if(gorgiasResponseForUrgency.isHasError()) {
//                 System.out.println(gorgiasResponseForUrgency.getErrorMsg());
//             }else {
//                 if(gorgiasResponseForUrgency.isHasResult()) {
//                     List<QueryResult>results=gorgiasResponseForUrgency.getResult();
//                     for(QueryResult queryResult:results) {
                       
//                         System.out.println("ExplanationStr:"+queryResult.getExplanationStr());
//                         System.out.println(queryResult.getHumanExplanation());
                        


//                      }
//                  }


//              }
//          GorgiasQueryResult gorgiasResponseForLocation = gorgiasService.executeGorgiasQueryForLocation(form);
//          boolean showContinueWithAzure = false;
//          if(gorgiasResponseForLocation.isHasError()) {
//             System.out.println(gorgiasResponseForLocation.getErrorMsg());
//         } else {
//             if(gorgiasResponseForLocation.isHasResult()) {
//                 List<QueryResult> results = gorgiasResponseForLocation.getResult();
//                 if (!results.isEmpty()) {
//                     QueryResult firstResult = results.get(0);
//                     if (firstResult.getExplanationStr().toLowerCase().contains("azure")) {
//                         showContinueWithAzure = true;
//                     }
//                     // The loop can still be used for other logic if needed
//                     for(QueryResult queryResult: results) {
//                         System.out.println("ExplanationStr:" + queryResult.getExplanationStr());
//                         System.out.println(queryResult.getHumanExplanation());
//                     }
//                 }
//             }
//         }
//        // Add the Urgency response from Gorgias Cloud to the model
//         model.addAttribute("gorgiasResponseForUrgency", gorgiasResponseForUrgency);
//        // Add the Location response from Gorgias Cloud to the model
//         model.addAttribute("gorgiasResponseForLocation", gorgiasResponseForLocation);
//        // model.addAttribute("gorgiasResponse", gorgiasResponse);
//       // model.addAttribute("message", "The processDecision method was executed.");
//         model.addAttribute("showContinueWithAzure", showContinueWithAzure);


//          // Δημιουργία αρχείου YAML με τις παραμέτρους της φόρμας
//    // Δημιουργία Virtual Machine Resource
// //    if (showContinueWithAzure==true){
// //    String location = form.getDeploymentLocation();
// //     Map<String, Object> virtualMachine = new HashMap<>();
// //     virtualMachine.put("type", "Microsoft.Compute/virtualMachines");
// //     virtualMachine.put("name", "myAzureVM");
// //     virtualMachine.put("location", location);

// //     Map<String, Object> hardwareProfile = new HashMap<>();
    
// //     Map<String, Object> osDisk = new HashMap<>();
// //     osDisk.put("createOption", "FromImage");
// //     Map<String, Object> storageProfile = new HashMap<>();
// //     storageProfile.put("osDisk", osDisk);

// //     Map<String, Object> networkInterface = new HashMap<>();
// //     networkInterface.put("id", "[resourceId('Microsoft.Network/networkInterfaces', 'myNic')]");
// //     List<Map<String, Object>> networkInterfacesList = new ArrayList<>();
// //     networkInterfacesList.add(networkInterface);

// //     Map<String, Object> networkProfile = new HashMap<>();
// //     networkProfile.put("networkInterfaces", networkInterfacesList);

// //     Map<String, Object> vmProperties = new HashMap<>();
// //     vmProperties.put("hardwareProfile", hardwareProfile);
// //     vmProperties.put("storageProfile", storageProfile);
// //     vmProperties.put("networkProfile", networkProfile);
// //     virtualMachine.put("properties", vmProperties);

// //     // Δημιουργία Virtual Network Resource
// //     Map<String, Object> virtualNetwork = new HashMap<>();
// //     virtualNetwork.put("type", "Microsoft.Network/virtualNetworks");
// //     virtualNetwork.put("name", "myVNet");
// //     virtualNetwork.put("location", location);

// //     Map<String, Object> addressSpace = new HashMap<>();
// //     List<String> addressPrefixes = new ArrayList<>();
// //     addressPrefixes.add("10.0.0.0/16");
// //     addressSpace.put("addressPrefixes", addressPrefixes);

// //     Map<String, Object> subnetProperties = new HashMap<>();
// //     subnetProperties.put("addressPrefix", "10.0.0.0/24");

// //     Map<String, Object> subnet = new HashMap<>();
// //     subnet.put("name", "default");
// //     subnet.put("properties", subnetProperties);

// //     List<Map<String, Object>> subnetsList = new ArrayList<>();
// //     subnetsList.add(subnet);

// //     Map<String, Object> vnetProperties = new HashMap<>();
// //     vnetProperties.put("addressSpace", addressSpace);
// //     vnetProperties.put("subnets", subnetsList);
// //     virtualNetwork.put("properties", vnetProperties);

// //     // Δημιουργία Storage Account Resource
// //     Map<String, Object> storageAccount = new HashMap<>();
// //     storageAccount.put("type", "Microsoft.Storage/storageAccounts");
// //     storageAccount.put("name", "mystorageaccount");
// //     storageAccount.put("location", location);
// //     storageAccount.put("kind", "StorageV2");

// //     Map<String, Object> sku = new HashMap<>();
// //     sku.put("name", "Standard_LRS");
// //     storageAccount.put("sku", sku);

// //     // Συνδυασμός όλων των πόρων σε ένα YAML αρχείο
// //     List<Map<String, Object>> resources = new ArrayList<>();
// //     resources.add(virtualMachine);
// //     resources.add(virtualNetwork);
// //     resources.add(storageAccount);
    
// //     Map<String, Object> yamlData = new HashMap<>();
// //     yamlData.put("resources", resources);



// //     // Ρυθμίσεις YAML και αποθήκευση σε αρχείο
// //     DumperOptions options = new DumperOptions();
// //     options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
// //     Yaml yaml = new Yaml(options);

// //     try (FileWriter writer = new FileWriter("myYAMLGENERATION.yaml")) {
// //         yaml.dump(yamlData, writer);
// //     } catch (IOException e) {
// //         e.printStackTrace();
// //     }
// // }
//     // Επιστροφή στη σελίδα αποτελεσμάτων
//     return "workflow/result-page";
//     }

@PostMapping("/save-selections")
@ResponseBody
public ResponseEntity<?> saveSelections(@RequestBody Map<String, List<String>> selections, HttpSession session) {
    try {
        // Retrieve existing WorkflowForm or create a new one
        WorkflowForm form = (WorkflowForm) session.getAttribute("form");
        if (form == null) {
            form = new WorkflowForm();
            session.setAttribute("form", form);
        }

        // Save user selections
        form.setInfrastructureDecision(selections.get("infrastructure") != null ? selections.get("infrastructure").get(0) : null);
        form.setScalabilityAndPerformanceDecision(selections.get("scalability") != null ? String.join(",", selections.get("scalability")) : null);
        form.setUrgencyDecision(selections.get("urgency") != null ? selections.get("urgency").get(0) : null);
        form.setResourceDecision(selections.get("resources") != null ? String.join(",", selections.get("resources")) : null);
        form.setLocationDecision(selections.get("location") != null ? selections.get("location").get(0) : null);
     // Normalize and extract meaningful selections
     form.setInfrastructureDecision(extractKeyword(selections.get("infrastructure") != null ? selections.get("infrastructure").get(0) : null));
     form.setScalabilityAndPerformanceDecision(extractKeyword(selections.get("scalability") != null ? String.join(",", selections.get("scalability")) : null));
     form.setUrgencyDecision(extractKeyword(selections.get("urgency") != null ? selections.get("urgency").get(0) : null));
     form.setResourceDecision(extractKeyword(selections.get("resources") != null ? String.join(",", selections.get("resources")) : null));
     form.setLocationDecision(extractKeyword(selections.get("location") != null ? selections.get("location").get(0) : null));

        // Store updated form in session
        session.setAttribute("form", form);
        System.out.println("----------------------------------------------------");
        System.out.println("User Selections Received (Normalized):");
        System.out.println("Infrastructure: " + form.getInfrastructureDecision());
        System.out.println("Scalability & Performance: " + form.getScalabilityAndPerformanceDecision());
        System.out.println("Urgency: " + form.getUrgencyDecision());
        System.out.println("Computing Resources: " + form.getResourceDecision());
        System.out.println("Location: " + form.getLocationDecision());
        System.out.println("----------------------------------------------------");

        return ResponseEntity.ok(Collections.singletonMap("message", "Selections saved successfully"));
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "An error occurred while saving selections"));
    }
}

private String extractKeyword(String input) {
    if (input == null || input.isEmpty()) {
        return "unknown";
    }
    
    // Extract text before colon (:) if present
    String[] parts = input.split(":");
    String keyword = parts[0].trim().toLowerCase().replace(" ", "_"); 

    return keyword;
}

    
    @PostMapping("/generate-yaml")
    public String generateYamlFileBasedOnDecisions(@ModelAttribute("form") WorkflowForm form, RedirectAttributes redirectAttributes, HttpSession session) {
      
            
            // Κλήση της μεθόδου δημιουργίας YAML με βάση τις αποφάσεις από το form
        //    createYamlFileBasedOnDecisions(form);
            // Call mapping method to ensure ForFinal variables are set
            

            System.out.println("=== Collected Parameters and Decisions ===");
          
            System.out.println("Decisions:");
            System.out.println("Urgency Decision: " + getUrgencyShort(form.getUrgencyDecision()));  // ✅ Raw value
            System.out.println("Infrastructure Decision: " + getFirstValue(form.getInfrastructureDecision()));  // ✅ Raw value
            System.out.println("Scalability and Performance Decision: " +getScalabilityShort(form.getScalabilityAndPerformanceDecision()) );  // ✅ Raw value
             System.out.println("Resource Priority: " + form.getResourcePriority());
            System.out.println("Disaster Recovery: " +form.getDisasterRecoveryNeeds());
            System.out.println("Budget: " +form.getBudget());
            System.out.println("latency: "+form.getLatencyRequirement());
            System.out.println(" primary goal " +form.getPrimaryGoal());
            System.out.println(" control requirement " + form.getControlRequirement());
            System.out.println(" compliance requirement " + form.getComplianceRequirement());

            

        //     // Προσθήκη μηνύματος επιβεβαίωσης στο RedirectAttributes
            form.setUrgencyDecision(getUrgencyShort(form.getUrgencyDecision()));
            form.setInfrastructureDecision(getFirstValue(form.getInfrastructureDecision()));
            form.setScalabilityAndPerformanceDecision(getScalabilityShort(form.getScalabilityAndPerformanceDecision()));
            
        //      redirectAttributes.addFlashAttribute("yamlMessage", "Το YAML αρχείο δημιουργήθηκε επιτυχώς με βάση τις αποφάσεις.");
        //  } catch (IOException e) {
        //      redirectAttributes.addFlashAttribute("yamlError", "Σφάλμα κατά τη δημιουργία του YAML αρχείου: " + e.getMessage());
        //      e.printStackTrace();
        //  }
        //createYamlFileBasedOnDecisions(form);


        //Here goes all the process like process-urgency and other similar
        //to execute gorgias query
            //function yamlgen(form)
         //   System.out.println("Executing Gorgias Query for Urgency...");
    
            // Store input values in model
           
      
           // ✅ Call Gorgias Service to execute query (Facts are set inside the method)
    List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForYamlGen(form, session);

    if (parsedResults != null && !parsedResults.isEmpty()) {
        processGorgiasResults(parsedResults, form, session);
        System.out.println("✅ YAML Generation Decision Completed!");
    } else {
        System.out.println("❌ No valid results from Gorgias. Marking decision as 'No Decision'.");
    }

  

            



        return "redirect:/workflow/yaml-generated";
    }

    // private void processGorgiasResults(List<ParsedResult> parsedResults, WorkflowForm form, HttpSession session) {
    //     List<String> candidateDecisions = new ArrayList<>();
    //     Map<String, List<String>> supportingFactsMap = new HashMap<>();
        
    //     for (ParsedResult result : parsedResults) {
    //         String rawResult = result.getMainResult();
    //         System.out.println("Raw Gorgias Result: " + rawResult);
            
    //         if (rawResult == null) continue;
            
    //         // Extract meaningful decision
    //         String extractedDecision = rawResult.contains("(") && rawResult.contains(")")
    //                 ? rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'))
    //                 : rawResult;
            
    //         try {
    //             // Call the service method to convert to human-readable format
    //             String humanReadableDecision = gorgiasService.mapMainResultToNaturalLanguage(extractedDecision, form);
                
    //             if (humanReadableDecision != null && !humanReadableDecision.trim().isEmpty()) {
    //                 candidateDecisions.add(humanReadableDecision);
                    
    //                 // Process supporting facts
    //                 List<String> convertedFacts = new ArrayList<>();
    //                 for (String fact : result.getSupportingFacts()) {
    //                     String convertedFact = gorgiasService.convertFactToNaturalLanguage(fact.trim());
    //                     convertedFacts.add(convertedFact);
    //                 }
                    
    //                 supportingFactsMap.put(humanReadableDecision, convertedFacts);
    //             }
    //         } catch (Exception e) {
    //             System.err.println("Error mapping decision: " + e.getMessage());
    //             e.printStackTrace();
    //         }
    //     }
        
    //     // Select a single optimal decision
    //     String finalDecision = selectOptimalDecision(candidateDecisions, form);
        
    //     // Store final decision in form and session
    //     form.setYamlGenerationDecision(finalDecision);
    //     session.setAttribute("finalYamlDecision", finalDecision);
        
    //     // Store supporting facts for the selected decision
    //     if (supportingFactsMap.containsKey(finalDecision)) {
    //         session.setAttribute("decisionRationale", supportingFactsMap.get(finalDecision));
    //     }
        
    //     System.out.println("Final YAML Decision: " + finalDecision);
    // }

/** 

/**
 /**
 * Processes the results from the Gorgias rule engine, extracting the main decision and supporting facts.
 * Stores the processed results in the session for use by the view layer.
 * 
 * @param parsedResults The list of parsed results from the Gorgias engine
 * @param form The workflow form containing user selections
 * @param session The HTTP session for storing results
 */
private void processGorgiasResults(List<ParsedResult> parsedResults, WorkflowForm form, HttpSession session) {
    // Add logging
    logger.debug("Processing Gorgias results: {} results received", 
                 parsedResults != null ? parsedResults.size() : 0);
    
    // Check for null or empty results
    if (parsedResults == null || parsedResults.isEmpty()) {
        logger.warn("No parsed results available from Gorgias engine");
        session.setAttribute("finalYamlDecision", "No suitable deployment configuration found");
        // Add empty supporting facts to avoid NPE in view
        session.setAttribute("decisionRationale", Collections.emptyList());
        return;
    }
    
    try {
        // Get the first result (highest priority from Prolog rules)
        ParsedResult firstResult = parsedResults.get(0);
        
        if (firstResult == null) {
            logger.warn("First result is null despite non-empty results list");
            handleDefaultDecision(form, session);
            return;
        }
        
        String mainResult = firstResult.getMainResult();
        
        // Check for null mainResult
        if (mainResult == null || mainResult.trim().isEmpty()) {
            logger.warn("Main result is null or empty");
            handleDefaultDecision(form, session);
            return;
        }
        
        // Extract the decision from the result with defensive string manipulation
        String extractedDecision;
        if (mainResult.contains("(") && mainResult.contains(")") && 
            mainResult.indexOf('(') < mainResult.indexOf(')')) {
            // Safe substring extraction with bounds checking
            int openParenIndex = mainResult.indexOf('(');
            int closeParenIndex = mainResult.indexOf(')');
            extractedDecision = mainResult.substring(openParenIndex + 1, closeParenIndex);
        } else {
            // If no parentheses or invalid format, use the whole string
            logger.info("Main result doesn't contain expected parentheses format, using as-is: {}", mainResult);
            extractedDecision = mainResult;
        }
        
        // Convert to human-readable description
        String humanReadableDecision = gorgiasService.mapMainResultToNaturalLanguage(extractedDecision, form);
        
        // Check if we got a valid human-readable decision
        if (humanReadableDecision == null || humanReadableDecision.trim().isEmpty()) {
            logger.warn("Failed to map result to natural language: {}", extractedDecision);
            humanReadableDecision = "Azure deployment configuration: " + extractedDecision;
        }
        
        // Process supporting facts safely
        List<String> supportingFacts = new ArrayList<>();
        if (firstResult.getSupportingFacts() != null) {
            for (String fact : firstResult.getSupportingFacts()) {
                if (fact != null && !fact.trim().isEmpty()) {
                    try {
                        String convertedFact = gorgiasService.convertFactToNaturalLanguage(fact.trim());
                        if (convertedFact != null && !convertedFact.trim().isEmpty()) {
                            supportingFacts.add(convertedFact);
                        }
                    } catch (Exception e) {
                        logger.warn("Error converting fact '{}' to natural language: {}", fact, e.getMessage());
                        // Add the raw fact as fallback
                        supportingFacts.add(fact.trim());
                    }
                }
            }
        }
        
        // If no supporting facts were successfully processed, add a default explanation
        if (supportingFacts.isEmpty()) {
            supportingFacts.add("This recommendation is based on your requirements");
        }
        
        // Store the decision and supporting facts
        form.setYamlGenerationDecision(humanReadableDecision);
        session.setAttribute("finalYamlDecision", humanReadableDecision);
        session.setAttribute("decisionRationale", supportingFacts);
        
        logger.info("Selected decision: {}", extractedDecision);
        logger.info("Human-readable decision: {}", humanReadableDecision);
        logger.debug("Supporting facts: {}", supportingFacts);
        
    } catch (Exception e) {
        logger.error("Unexpected error processing Gorgias results", e);
        handleDefaultDecision(form, session);
    }
}

/**
 * Handles the case when no valid decision could be determined by setting default values.
 * 
 * @param form The workflow form
 * @param session The HTTP session
 */
private void handleDefaultDecision(WorkflowForm form, HttpSession session) {
    String defaultDecision = "Azure App Service (Standard): Managed web hosting with fixed resource allocation";
    
    form.setYamlGenerationDecision(defaultDecision);
    session.setAttribute("finalYamlDecision", defaultDecision);
    
    List<String> defaultRationale = new ArrayList<>();
    defaultRationale.add("This is a default recommendation based on general best practices");
    defaultRationale.add("For more specific recommendations, please refine your requirements");
    
    session.setAttribute("decisionRationale", defaultRationale);
    
    logger.info("Applied default decision: {}", defaultDecision);
}
private String selectOptimalDecision(List<String> decisions, WorkflowForm form) {
    if (decisions == null || decisions.isEmpty()) {
        return "No suitable deployment configuration found";
    }
    
    // If only one decision, return it
    if (decisions.size() == 1) {
        return decisions.get(0);
    }
    
    // Create a map to store decision scores
    Map<String, Integer> decisionScores = new HashMap<>();
    
    for (String decision : decisions) {
        int score = 0;
        String lowerDecision = decision.toLowerCase();
        
        // Infrastructure alignment - higher priority
        String infrastructure = form.getInfrastructureDecision();
        if (infrastructure != null) {
            if (infrastructure.equalsIgnoreCase("serverless") && lowerDecision.contains("function")) {
                score += 50;
            } else if (infrastructure.equalsIgnoreCase("iaas") && lowerDecision.contains("machine")) {
                score += 50;
            } else if (infrastructure.equalsIgnoreCase("paas") && lowerDecision.contains("app service")) {
                score += 50;
            }
        }
        
        // Control requirements
        String controlReq = form.getControlRequirement();
        if (controlReq != null) {
            if (controlReq.contains("full") && lowerDecision.contains("full control")) {
                score += 40;
            } else if (!controlReq.contains("full") && !lowerDecision.contains("full control")) {
                score += 30;
            }
        }
        
        // Performance optimization
        String procOpt = form.getProcessingOptimization();
        if (procOpt != null) {
            if (procOpt.contains("memory") && lowerDecision.contains("memory")) {
                score += 35;
            } else if (procOpt.contains("compute") && 
                    (lowerDecision.contains("compute") || lowerDecision.contains("performance"))) {
                score += 35;
            }
        }
        
        // Scalability preferences
        String scalability = form.getScalabilityAndPerformanceDecision();
        if (scalability != null) {
            if (scalability.contains("auto") && lowerDecision.contains("auto-scaling")) {
                score += 30;
            } else if (scalability.contains("fixed") && lowerDecision.contains("fixed")) {
                score += 30;
            }
        }
        
        // Urgency alignment
        String urgency = form.getUrgencyDecision();
        if (urgency != null && urgency.equalsIgnoreCase("urgent") && 
            (lowerDecision.contains("premium") || lowerDecision.contains("high performance"))) {
            score += 25;
        }
        
        // Latency requirements
        String latency = form.getLatencyRequirement();
        if (latency != null && latency.equalsIgnoreCase("strict") && 
            (lowerDecision.contains("premium") || lowerDecision.contains("performance"))) {
            score += 20;
        }
        
        // Budget alignment
        String budget = form.getBudget();
        if (budget != null) {
            if (budget.contains("high") && 
                (lowerDecision.contains("premium") || lowerDecision.contains("high"))) {
                score += 15;
            } else if (budget.contains("low") && lowerDecision.contains("basic")) {
                score += 15;
            }
        }
        
        // Penalize duplicate recommendations
        if (lowerDecision.contains("standard") && score < 70) {
            score -= 10;
        }
        
        decisionScores.put(decision, score);
    }
    
    // Find the highest scoring decision
    return decisionScores.entrySet().stream()
            .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
            .map(Map.Entry::getKey)
            .findFirst()
            .orElse(decisions.get(0)); // Fallback to first decision
}
   /**
 * Downloads the YAML configuration file based on the selected deployment option
 * 
 * @param session The HTTP session containing the deployment decision
 * @return The YAML file as a downloadable resource
 */
@GetMapping("/download-yaml")
public ResponseEntity<Resource> downloadYaml(HttpSession session) {
    try {
        // Retrieve the resultsList from session
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> resultsList = (List<Map<String, Object>>) session.getAttribute("resultsList");
        
        if (resultsList == null || resultsList.isEmpty()) {
            System.out.println("❌ ERROR: No results found in session.");
            return ResponseEntity.badRequest().body(null);
        }
        
        // Get the first result's mainResult (which contains our decision)
        String mainResult = (String) resultsList.get(0).get("naturalLanguageMainResult");
        System.out.println("📝 Main result from session: " + mainResult);
        
        if (mainResult == null || mainResult.isEmpty()) {
            System.out.println("❌ ERROR: No main result found in session.");
            return ResponseEntity.badRequest().body(null);
        }
        
        // Extract the deployment type from yamlfile(azure_vm_high_performance) format
        String deploymentType = null;
        if (mainResult.startsWith("yamlfile(") && mainResult.endsWith(")")) {
            deploymentType = mainResult.substring(9, mainResult.length() - 1);
            System.out.println("🔑 Extracted deployment type: " + deploymentType);
        } else {
            System.out.println("⚠️ WARNING: Main result is not in expected format: " + mainResult);
            deploymentType = mainResult; // Use as-is and let the mapping function handle it
        }
        
        // Map decision to YAML file name
        String yamlFileName = mapYamlDecisionToFile(deploymentType);
        System.out.println("🧐 Attempting to load file: " + yamlFileName);
        
        // Load file from classpath (Works inside JAR and Docker)
        ClassPathResource resource = new ClassPathResource("yaml/" + yamlFileName);
        
        if (!resource.exists()) {
            System.out.println("❌ ERROR: YAML file not found -> " + yamlFileName);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        
        // Read as InputStream (Required for JAR & Docker)
        InputStreamResource inputStreamResource = new InputStreamResource(resource.getInputStream());
        
        System.out.println("✅ YAML File Found: " + yamlFileName);
        
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + yamlFileName + "\"")
                .body(inputStreamResource);
        
    } catch (IOException e) {
        System.out.println("❌ ERROR: Could not read file: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    } catch (Exception e) {
        System.out.println("❌ ERROR: Unexpected exception: " + e.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }
}

    
    
    
    // private String mapYamlDecisionToFile(String decision) {
    //     switch (decision.toLowerCase()) {
    //         case "firstyaml":
    //         case "auto scaling compute optimized":  // ✅ Ensure it matches the human-readable output
    //             return "firstyaml.yaml";
    //         case "secondyaml":
    //         case "infrastructure as a service, compute optimized - fixed allocation (iaas)":
    //             return "secondyaml.yaml";
    //         case "thirdyaml":
    //         case "platform as a service standard memory deployment": // ✅ Add this case
    //             return "thirdyaml.yaml";
    //         default:
    //             System.out.println("❌ ERROR: No mapping found for decision '" + decision + "'");
    //             return "default.yaml";  // Provide a default file
    //     }
    // }
    /**
 * Maps YAML file names to actual template files for download
 * 
 * @param decision The YAML file decision from Prolog
 * @return Filename of the YAML template
 */
private String mapYamlDecisionToFile(String decision) {
    switch (decision.toLowerCase()) {
        // Virtual Machine configurations
        case "azure_vm_high_performance":
            return "azure_vm_high_performance.yaml";
        case "azure_vm_standard":
            return "azure_vm_standard.yaml";
            
        // Managed compute services
        case "azure_batch":
            return "azure_batch.yaml";
        case "azure_spring_apps":
            return "azure_spring_apps.yaml";
        case "azure_spring_apps_autoscaling":
            return "azure_spring_apps_autoscaling.yaml";
            
        // Serverless options
        case "azure_functions":
            return "azure_functions.yaml";
        case "azure_functions_premium":
            return "azure_functions_premium.yaml";
            
        // Web hosting
        case "azure_app_service":
            return "azure_app_service.yaml";
        case "azure_app_service_autoscaling":
            return "azure_app_service_autoscaling.yaml";
            
        // Container solutions
        case "azure_container_instances":
            return "azure_container_instances.yaml";
        case "azure_kubernetes_service":
            return "azure_kubernetes_service.yaml";
        case "azure_red_hat_openshift":
            return "azure_red_hat_openshift.yaml";
        case "azure_service_fabric":
            return "azure_service_fabric.yaml";
        case "azure_container_apps":
            return "azure_container_apps.yaml";
            
        // Legacy mappings for backward compatibility
        case "firstyaml":
            return "firstyaml.yaml";
        case "secondyaml":
            return "secondyaml.yaml";
        case "thirdyaml":
            return "thirdyaml.yaml";
            
        default:
            System.out.println("⚠️ WARNING: No mapping found for decision '" + decision + "'");
            return "default.yaml";  // Provide a default file
    }
}


    


    private static String getUrgencyShort(String urgency) {
        if (urgency.toLowerCase().contains("urgent")) {
            return "urgent";
        } else if (urgency.toLowerCase().contains("high")) {
            return "high";
        } else if (urgency.toLowerCase().contains("normal")) {
            return "normal";
        }
        return urgency; // Return as is if no match is found
    }
    private static String getFirstValue(String value) {
        if (value.contains(",")) {
            return value.split(",")[0].trim(); // Take first value before comma and trim spaces
        }
        return value; // Return as is if no comma is found
    }
    
    private static String getScalabilityShort(String scalability) {
        if (scalability.contains(",")) {
            scalability = scalability.split(",")[0]; // Take first value before comma
        }
        
        if (scalability.toLowerCase().contains("auto scaling")) {
            return "auto_scaling";
        } else if (scalability.toLowerCase().contains("fixed allocation")) {
            return "fixed_allocation";
        }
        return scalability; // Return as is if no match is found
    }
    
    public static void createYamlFileBasedOnDecisions(WorkflowForm form) throws IOException {
        Map<String, Object> yamlData = new HashMap<>();
        List<Map<String, Object>> resources = new ArrayList<>();

        // Extracting first choices from multi-option inputs
        String location = extractFirstChoice(form.getLocationDecision());
        String infrastructure = extractFirstChoice(form.getInfrastructureDecision());
        String resourceDecision = extractFirstChoice(form.getResourceDecision());
        String scalability = extractFirstChoice(form.getScalabilityAndPerformanceDecision());
        String urgency = extractFirstChoice(form.getUrgencyDecision());

        // Assigning Location Decision
        yamlData.put("location", location.equalsIgnoreCase("azure") ? "eastus" : "local");

        // Adding Infrastructure
        resources.add(getInfrastructureResource(infrastructure, location));

        // Adding Resource Allocation
        resources.add(getResourceConfig(resourceDecision, location));

        // Adding Scalability Decision
        resources.add(getScalabilityConfig(scalability, location));

        // Adding Urgency
        resources.add(getUrgencyConfig(urgency, location));

        // Finalizing YAML
        yamlData.put("resources", resources);

        // YAML Formatting & Writing
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);

        try (FileWriter writer = new FileWriter("deployment_config.yaml")) {
            yaml.dump(yamlData, writer);
            Files.copy(Paths.get("deployment_config.yaml"),
                       Paths.get("src/main/resources/static/deployment_config.yaml"),
                       StandardCopyOption.REPLACE_EXISTING);
        }
    }
    public static Map<String, Object> getInfrastructureResource(String decision, String location) {
        Map<String, Object> resource = new HashMap<>();
        switch (decision.toLowerCase()) {
            case "paas":
                resource.put("type", "Microsoft.AppPlatform/Spring");
                resource.put("name", "SpringAppDeployment");
                break;
            case "iaas":
                resource.put("type", "Microsoft.Compute/virtualMachines");
                resource.put("name", "VMDeployment");
                break;
            case "serverless":
                resource.put("type", "Microsoft.Web/sites");
                resource.put("name", "FunctionAppDeployment");
                break;
            default:
                resource.put("type", "Generic/Deployment");
                resource.put("name", "DefaultDeployment");
        }
        resource.put("location", location);
        return resource;
    }
    private static Map<String, Object> getResourceConfig(String decision, String location) {
        Map<String, Object> resource = new HashMap<>();
        switch (decision.toLowerCase()) {
            case "high compute":
                resource.put("type", "Compute/High");
                resource.put("name", "HighPerformanceCompute");
                break;
            case "memory-optimized":
                resource.put("type", "Memory/Optimized");
                resource.put("name", "MemoryOptimizedDeployment");
                break;
            case "real-time compute":
                resource.put("type", "Compute/RealTime");
                resource.put("name", "LowLatencyDeployment");
                break;
            default:
                resource.put("type", "Compute/General");
                resource.put("name", "DefaultCompute");
        }
        resource.put("location", location);
        return resource;
    }
    private static Map<String, Object> getScalabilityConfig(String decision, String location) {
        Map<String, Object> resource = new HashMap<>();
        switch (decision.toLowerCase()) {
            case "auto scaling":
                resource.put("type", "Scaling/Auto");
                resource.put("name", "AutoScalingEnabled");
              
                break;
            case "fixed allocation":
                resource.put("type", "Scaling/Fixed");
                resource.put("name", "FixedScaling");
                break;
            default:
                resource.put("type", "Scaling/Default");
                resource.put("name", "DefaultScaling");
        }
        resource.put("location", location);
        return resource;
    }

    private static Map<String, Object> getUrgencyConfig(String decision, String location) {
        Map<String, Object> resource = new HashMap<>();
        switch (decision.toLowerCase()) {
            case "high urgency":
                resource.put("type", "Urgency/High");
                resource.put("name", "CriticalDeployment");
                break;
            case "urgent":
                resource.put("type", "Urgency/Urgent");
                resource.put("name", "ImmediateDeployment");
                break;
            default:
                resource.put("type", "Urgency/Normal");
                resource.put("name", "DefaultDeployment");
        }
        resource.put("location", location);
        return resource;
    }
    private static String extractFirstChoice(String rawInput) {
        if (rawInput != null && rawInput.contains(";")) {
            return rawInput.split(";")[0].trim().split(":")[0].trim(); // Take first part before ":"
        }
        return rawInput != null ? rawInput.trim().split(":")[0].trim() : "default";
    }
    
    
    // Utility function to remove duplicates before passing to the model
private String removeDuplicates(String input) {
    if (input == null || input.isEmpty()) return null; // Handle empty case
    Set<String> uniqueValues = Arrays.stream(input.split(","))
            .map(String::trim) // Remove leading/trailing spaces
            .filter(value -> !value.isEmpty()) // Remove empty values
            .collect(Collectors.toSet()); // Store unique values
    return String.join(", ", uniqueValues); // Convert back to a string
}

    
    
    

@PostMapping("/containerizedOrNot")
public String containerizedOrNot(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {

         
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getContainerized())) {
        return "redirect:determineAzureServiceWorkflow/migrate/liftAndShift/containerized";
    } else {
        // Handle other cases
        return "redirect:determineAzureServiceWorkflow/migrate/liftAndShift/notContainerized";
    }
}

@PostMapping("/cotsappOrNot")
public String cotsappOrNot(@ModelAttribute("form") WorkflowForm form,
        RedirectAttributes redirectAttributes) {

         
    if (form == null) {
        return "redirect:/workflow/index";
    }
    
    redirectAttributes.addFlashAttribute("form", form);
    
    if ("yes".equals(form.getCotsapp())) {
         return "redirect:determineAzureServiceWorkflow/migrate/liftAndShift/notContainerized/cotsapp";
       
    } else {
        // Handle other cases
         return "redirect:determineAzureServiceWorkflow/migrate/liftAndShift/notContainerized/notCotsapp";
        
    }

    
}

    //dokimastiko
    // ...

// @PostMapping("/process-decision")
// public String processDecision(@ModelAttribute("form") WorkflowForm form, Model model) {
//     // Assuming you have a method in GorgiasService to process the form asynchronously
//     CompletableFuture<GorgiasQueryResult> gorgiasResponseFuture = gorgiasService.executeGorgiasQueryAsync(form);

//     // Handle the completion of the CompletableFuture
//     gorgiasResponseFuture.whenComplete((gorgiasResponse, exception) -> {
//         if (exception != null) {
//             // Handle the exception
//             exception.printStackTrace();
//         } else {
//             // Handle the response from Gorgias Cloud
//             if (gorgiasResponse != null && gorgiasResponse.isHasError()) {
//                 System.out.println(gorgiasResponse.getErrorMsg());
//             } else {
//                 if (gorgiasResponse != null && gorgiasResponse.isHasResult()) {
//                     List<QueryResult> results = gorgiasResponse.getResult();
//                     for (QueryResult queryResult : results) {
//                         System.out.println("ExplanationStr:" + queryResult.getExplanationStr());
//                         System.out.println(queryResult.getHumanExplanation());
//                     }
//                 }
//             }
//         }
//     });

//     // Continue with other processing or redirect logic
//     // ...

//     // Redirect to a result page or return the same page with the result
//     return "workflow/result-page"; // Replace with your actual result page
// }

}
