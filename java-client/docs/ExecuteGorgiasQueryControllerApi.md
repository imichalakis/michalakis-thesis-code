# ExecuteGorgiasQueryControllerApi

All URIs are relative to *https://aiasvm1.amcl.tuc.gr:8085*

Method | HTTP request | Description
------------- | ------------- | -------------
[**executeQueryUsingPOST**](ExecuteGorgiasQueryControllerApi.md#executeQueryUsingPOST) | **POST** /GorgiasQuery | executeQuery


<a name="executeQueryUsingPOST"></a>
# **executeQueryUsingPOST**
> GorgiasQueryResult executeQueryUsingPOST(gorgiasQuery)

executeQuery

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.ExecuteGorgiasQueryControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

ExecuteGorgiasQueryControllerApi apiInstance = new ExecuteGorgiasQueryControllerApi();
GorgiasQuery gorgiasQuery = new GorgiasQuery(); // GorgiasQuery | gorgiasQuery
try {
    GorgiasQueryResult result = apiInstance.executeQueryUsingPOST(gorgiasQuery);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling ExecuteGorgiasQueryControllerApi#executeQueryUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **gorgiasQuery** | [**GorgiasQuery**](GorgiasQuery.md)| gorgiasQuery |

### Return type

[**GorgiasQueryResult**](GorgiasQueryResult.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

