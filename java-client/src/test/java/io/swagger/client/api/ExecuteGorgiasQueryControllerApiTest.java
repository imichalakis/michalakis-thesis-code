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

import io.swagger.client.model.GorgiasQuery;
import io.swagger.client.model.GorgiasQueryResult;
import org.junit.Test;
import org.junit.Ignore;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for ExecuteGorgiasQueryControllerApi
 */
@Ignore
public class ExecuteGorgiasQueryControllerApiTest {

    private final ExecuteGorgiasQueryControllerApi api = new ExecuteGorgiasQueryControllerApi();

    
    /**
     * executeQuery
     *
     * 
     *
     * @throws Exception
     *          if the Api call fails
     */
    @Test
    public void executeQueryUsingPOSTTest() throws Exception {
        GorgiasQuery gorgiasQuery = null;
        GorgiasQueryResult response = api.executeQueryUsingPOST(gorgiasQuery);

        // TODO: test validations
    }
    
}
