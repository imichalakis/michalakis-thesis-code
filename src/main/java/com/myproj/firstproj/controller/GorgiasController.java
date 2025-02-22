package com.myproj.firstproj.controller;

import com.myproj.firstproj.service.GorgiasService;
import com.myproj.firstproj.session.UserSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import java.util.Enumeration;
import java.util.List;
import org.springframework.web.client.RestClientException;

@Controller
public class GorgiasController {

    @Autowired
    private UserSession userSession;

    @Autowired
    private GorgiasService gorgiasService;

    @GetMapping("/")
    public String showHomePage(Model model, HttpSession session) {
        // Initialize session attributes
     // Convert Enumeration<String> to a List and print attributes
     System.out.println("Session Attributes Before Rendering Home Page:");
     Enumeration<String> attributeNames = session.getAttributeNames();
     while (attributeNames.hasMoreElements()) {
         String name = attributeNames.nextElement();
         System.out.println(name + " = " + session.getAttribute(name));
     }

        userSession.setConnectionStatus(false);
        if (session.getAttribute("urgencyStatus") == null) {
            session.setAttribute("urgencyStatus", "⏳ Pending");  // Default to pending
        }
        if (session.getAttribute("infrastructureStatus") == null) {
            session.setAttribute("infrastructureStatus", "⏳ Pending");
        }
        if (session.getAttribute("locationStatus") == null) {
            session.setAttribute("locationStatus", "⏳ Pending");
        }
        if (session.getAttribute("resourceStatus") == null) {
            session.setAttribute("resourceStatus", "⏳ Pending");
        }
        if (session.getAttribute("scalabilityStatus") == null) {
            session.setAttribute("scalabilityStatus", "⏳ Pending");
        }
        try {
            List<String> projects = gorgiasService.getUserProjects();
            userSession.setProjects(projects);
            userSession.setConnectionStatus(true);
            // Simulate completion of steps
          
        } catch (RestClientException e) {
            e.printStackTrace();
            model.addAttribute("error", "Σφάλμα επικοινωνίας με το Gorgias.");
        }

        // Debug: Print session attributes
        System.out.println("Session attributes: " + session.getAttributeNames());

        // Get urgency status from session with default fallback
        String urgencyStatus = (String) session.getAttribute("urgencyStatus");
        String statusClass = "text-warning"; // Default class
        
        if (urgencyStatus != null) {
            System.out.println("Found urgency status: " + urgencyStatus);
            model.addAttribute("urgencyStatus", urgencyStatus);

            // Set appropriate CSS class based on status
            if (urgencyStatus.contains("✅")) {
                statusClass = "text-success";
            } else if (urgencyStatus.contains("❌")) {
                statusClass = "text-danger";
            }

            // If completed, also show the decision summary
            if (urgencyStatus.contains("✅")) {
                String urgencyDecision = (String) session.getAttribute("urgencyDecision");
                if (urgencyDecision != null) {
                    model.addAttribute("urgencyDecisionSummary", urgencyDecision);
                }
            }
        } else {
            System.out.println("No urgency status found, defaulting to Pending");
            model.addAttribute("urgencyStatus", "⏳ Pending");
        }

        model.addAttribute("urgencyStatusClass", statusClass);
        model.addAttribute("urgencyDecision", session.getAttribute("urgencyDecision"));
        // Load statuses from session
        model.addAttribute("urgencyStatus", session.getAttribute("urgencyStatus"));
        model.addAttribute("infrastructureStatus", session.getAttribute("infrastructureStatus"));
        model.addAttribute("locationStatus", session.getAttribute("locationStatus"));
        model.addAttribute("resourceStatus", session.getAttribute("resourceStatus"));
        model.addAttribute("scalabilityStatus", session.getAttribute("scalabilityStatus"));
        // Pass userSession to the view
        model.addAttribute("userSession", userSession);

        return "welcome"; // Make sure this matches your HTML template filename
    }
}
