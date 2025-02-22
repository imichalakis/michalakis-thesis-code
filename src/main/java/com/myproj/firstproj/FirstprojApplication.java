package com.myproj.firstproj;

// import org.camunda.bpm.engine.RuntimeService;
// import org.camunda.bpm.spring.boot.starter.event.PostDeployEvent;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class FirstprojApplication {

    public static void main(String... args) {
        SpringApplication.run(FirstprojApplication.class, args);
    }

    // @Autowired
    // private RuntimeService runtimeService;

    // @EventListener
    // public void processPostDeploy(PostDeployEvent event) {
    //     Map<String, Object> variables = new HashMap<>();
    //     variables.put("urgency", "urgent");          // Initialize the 'urgency' variable
    //     //variables.put("deployment", "on_azure");    // Initialize the 'deployment' variable
    //     runtimeService.startProcessInstanceByKey("autoprocess2", variables); // Start the process with the initialized variables
    // }

   
}
