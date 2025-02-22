package com.myproj.firstproj.service;

import com.myproj.firstproj.model.WorkflowForm;

import org.springframework.stereotype.Service;

@Service
public class WorkflowService {
   

   
    public void processFormData(WorkflowForm form) {
        if (form.getStep() == 2 && (form.getConnectivity() == null || !form.getConnectivity().equals("true"))) {
            form.setConnectivity("false");
        }
        // Process the form data here
        // For example, save the form data to the database or prepare it for an API call
    }

    public String determineNextStep(WorkflowForm form) {
        // Decide the next step based on the form data
        if (form.getStep() == 1) {
            return "step2";
        } else if (form.getStep() == 2) {
          //  return form.getDeploymentLocation().equals("azure") ? "step3" : "complete";
              return "complete";
        } else if (form.getStep() == 3) {
              return "step3";
            // if ("newBuild".equals(form.getBuildNewOrMigrate()))
            // {
            //     return "redirect:/determineAzureService/newBuild";
            // }

            // else{
            //     return "redirect:/determineAzureService/newBuild";
            // }
        } else {
            return "index";
        }
    }
    public String determineAzureService(WorkflowForm form) {
        // Example logic based on the flowchart for Azure service selection
         if ("newBuild".equals(form.getBuildNewOrMigrate())) {
            
              System.out.println("einai == newBuild");   
             if ("yes".equals(form.getFullControl())) {
                 return "Virtual Machine";
    //         } else if ("hpcWorkload".equals(form.getWorkloadType())) {
    //             return "Azure Batch";
    //         } else if ("springBootApp".equals(form.getApplicationType())) {
    //             return "Azure Spring Apps";
    //         } else if ("eventDriven".equals(form.getWorkloadType())) {
    //             return "Azure Functions";
             } else {
                 return "Azure App Service"; // Default to Azure App Service for other types
            }
         //} else if ("migrate".equals(form.getBuildNewOrMigrate())) {
           } else  {
    //         if ("containerized".equals(form.getIsContainerized())) {
    //             return "Azure Kubernetes Service (AKS)";
    //         } else {
    //             return "Azure Virtual Machine"; // Default to Azure VM for non-containerized apps
    //         }
    //     }
         return "Undetermined"; // If none of the conditions match
     
     }
     //return "newBuild";   

}
}
