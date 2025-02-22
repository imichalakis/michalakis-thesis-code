package com.myproj.firstproj.controller;

import com.myproj.firstproj.model.ParsedResult;
import com.myproj.firstproj.model.WorkflowForm;
import com.myproj.firstproj.service.GorgiasService;
import com.myproj.firstproj.service.WorkflowService;

import io.swagger.client.model.GorgiasQueryResult;
import io.swagger.client.model.QueryResult;

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

import javax.servlet.http.HttpSession;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/gorgiascloud")
@SessionAttributes("form")
public class IntegrationController {
    @Autowired
    private GorgiasService gorgiasService;
// @PostMapping("/request-gorgias")
// public String requestGorgias(@ModelAttribute("form") WorkflowForm form, Model model) {
//      GorgiasQueryResult gorgiasResponseForInfrastructure = gorgiasService.executeGorgiasQueryForInfr(form);
//      // Έλεγχος αν υπάρχει σφάλμα στο αποτέλεσμα
//      if (gorgiasResponseForInfrastructure.isHasError()) {
//         System.out.println(gorgiasResponseForInfrastructure.getErrorMsg());
//     } else {
//         // Αν δεν υπάρχει σφάλμα, ελέγχουμε αν υπάρχουν αποτελέσματα
//         if (gorgiasResponseForInfrastructure.isHasResult()) {
//             List<QueryResult> results = gorgiasResponseForInfrastructure.getResult();
//             for (QueryResult queryResult : results) {
//                 // Εκτύπωση αποτελεσμάτων και επεξήγηση
                
//                 System.out.println("ExplanationStr: " + queryResult.getExplanationStr());
//                 System.out.println(queryResult.getHumanExplanation());
//                 model.addAttribute("explanation", queryResult.getHumanExplanation());
//                 QueryResult firstResult = results.get(0);
//                 model.addAttribute("explanation", firstResult.getHumanExplanation());
//                 model.addAttribute("explanationStr", firstResult.getExplanationStr());
                
//                 //pairno to apotelesma pou me endiaferei
//                 // Εκτύπωση του κειμένου εντός παρενθέσεων
//                 //System.out.println(form.getInfrastructureDecision());
//                 }

//              //   String decision = results.get(0).getExplanationStr();
                
//                 String decision = results.get(0).getVariables().get("X");
//                 String decisionMessage = getDecisionMessage(decision);

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
//    // boolean suggestScalabilityForm = form.isHighScalability() || form.isHighPerformance();
//     // Προσθήκη του αποτελέσματος στο μοντέλο για προβολή στη σελίδα
//    // model.addAttribute("suggestScalabilityForm", suggestScalabilityForm);
   
//     model.addAttribute("gorgiasResponseForInfrastructure", gorgiasResponseForInfrastructure);
//     model.addAttribute("infrastructureDecision", form.getInfrastructureDecision());


//     return "gorgiasReq/infrastructureReq-result";
// }


    private String getDecisionMessage(String decisionKey) {
    switch (decisionKey.toLowerCase()) {
        case "paas":
            return "PaaS infrastructure was chosen for its high scalability and support for custom integrations.";
        case "iaas":
            return "IaaS infrastructure was chosen for its full control over resources.";
        case "saas":
            return "SaaS infrastructure was chosen for its ease of use and low management overhead.";
        case "serverless":
            return "Serverless infrastructure was chosen for its cost efficiency and flexibility.";
        default:
            return "No specific infrastructure decision was determined.";
    }
    }

    // @PostMapping("/request-gorgias")
    // public String requestGorgias(@ModelAttribute("form") WorkflowForm form, Model model) {
    //     List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForInfr(form);

    // if (!parsedResults.isEmpty() && parsedResults.get(0).getSupportingFacts() != null) {
    //     List<String> supportingFacts = parsedResults.get(0).getSupportingFacts();
    //     List<String> naturalLanguageFacts = new ArrayList<>();

    //     for (String fact : supportingFacts) {
    //         String normalizedFact = fact.trim().replaceAll("[\"']", ""); // Normalize fact
    //         String convertedFact = gorgiasService.convertFactToNaturalLanguage(normalizedFact); // Convert to natural language
    //         naturalLanguageFacts.add(convertedFact);
    //     }

    //     String mainResult = parsedResults.get(0).getMainResult();
    //     String naturalLanguageMainResult = gorgiasService.mapMainResultToNaturalLanguage(mainResult);
    //     model.addAttribute("naturalLanguageMainResult", naturalLanguageMainResult);
    //     model.addAttribute("convertedFact", naturalLanguageFacts);
    //     form.setInfrastructureDecision(mainResult);
    // } else {
    //     model.addAttribute("error", "No results found or an error occurred.");
    // }

    // model.addAttribute("form", form);
    
    
    
    //     return "gorgiasReq/infrastructureReq-result";
    // }
    @PostMapping("/request-gorgias")
    public String requestGorgias(@ModelAttribute("form") WorkflowForm form, Model model, HttpSession session) {
        List<ParsedResult> parsedResults = gorgiasService.executeGorgiasQueryForInfr(form);
    
        if (!parsedResults.isEmpty()) {
            List<Map<String, Object>> resultsList = new ArrayList<>();
            List<String> infrastructureDecisions = new ArrayList<>();
    
            for (ParsedResult result : parsedResults) {
                Map<String, Object> resultMap = new HashMap<>();
    
                // Process supporting facts
                List<String> supportingFacts = result.getSupportingFacts() != null ? result.getSupportingFacts() : new ArrayList<>();
                List<String> naturalLanguageFacts = supportingFacts.stream()
                    .map(fact -> gorgiasService.convertFactToNaturalLanguage(fact.trim().replaceAll("[\"']", "")))
                    .collect(Collectors.toList());
    
                // Process main result and extract only X from propose_infrastructure(X)
                String rawResult = result.getMainResult();
                String extractedDecision = rawResult.contains("(") && rawResult.contains(")")
                    ? rawResult.substring(rawResult.indexOf('(') + 1, rawResult.indexOf(')'))
                    : rawResult;
    
                // Store processed data in the map
                resultMap.put("naturalLanguageMainResult", gorgiasService.mapMainResultToNaturalLanguage(extractedDecision, form));
                resultMap.put("convertedFact", naturalLanguageFacts);
                resultsList.add(resultMap);
    
                // Add only extracted decision (X)
                infrastructureDecisions.add(extractedDecision);
            }
    
            // Save all decisions as a comma-separated string
            form.setInfrastructureDecision(String.join(", ", infrastructureDecisions));
            session.setAttribute("infrastructureDecision", form.getInfrastructureDecision());
            session.setAttribute("infrastructureStatus", "✅ Completed");
            model.addAttribute("resultsList", resultsList);
        } else {
            model.addAttribute("error", "No results found or an error occurred.");
        }
        
        model.addAttribute("form", form);
        return "gorgiasReq/infrastructureReq-result";
    }
    
    


}