/*
 * Api Documentation
 * Api Documentation
 *
 * OpenAPI spec version: 1.0
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


package io.swagger.client.api;

import java.io.File;
import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for OperationsControllerApi
 */
@Ignore
public class OperationsControllerApiTest {

    private final OperationsControllerApi api = new OperationsControllerApi();

    
    /**
     * addFile
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void addFileUsingPOSTTest() throws Exception {
        byte[] file = null;
        String project = null;
        String type = null;
        List<String> response = api.addFileUsingPOST(file, project, type);

        // TODO: test validations
    }
    
    /**
     * addFile
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void addFileUsingPOST1Test() throws Exception {
        String filename = null;
        String project = null;
        String type = null;
        String response = api.addFileUsingPOST1(filename, project, type);

        // TODO: test validations
    }
    
    /**
     * createProject
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void createProjectUsingPOSTTest() throws Exception {
        String projectName = null;
        String name = null;
        String response = api.createProjectUsingPOST(projectName, name);

        // TODO: test validations
    }
    
    /**
     * deleteFile
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void deleteFileUsingPOSTTest() throws Exception {
        String filename = null;
        String project = null;
        String response = api.deleteFileUsingPOST(filename, project);

        // TODO: test validations
    }
    
    /**
     * deleteProject
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void deleteProjectUsingPOSTTest() throws Exception {
        String project = null;
        String name = null;
        String response = api.deleteProjectUsingPOST(project, name);

        // TODO: test validations
    }
    
    /**
     * getFileContent
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getFileContentUsingPOSTTest() throws Exception {
        String filename = null;
        String project = null;
        String response = api.getFileContentUsingPOST(filename, project);

        // TODO: test validations
    }
    
    /**
     * getProjectFiles
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getProjectFilesUsingPOSTTest() throws Exception {
        String project = null;
        List<File> response = api.getProjectFilesUsingPOST(project);

        // TODO: test validations
    }
    
    /**
     * getUserProjects
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void getUserProjectsUsingGETTest() throws Exception {
        List<String> response = api.getUserProjectsUsingGET();

        // TODO: test validations
    }
    
    /**
     * updateFile
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void updateFileUsingPOSTTest() throws Exception {
        byte[] file = null;
        String project = null;
        String type = null;
        List<String> response = api.updateFileUsingPOST(file, project, type);

        // TODO: test validations
    }
    
}
