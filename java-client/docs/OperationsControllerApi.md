# OperationsControllerApi

All URIs are relative to *https://aiasvm1.amcl.tuc.gr:8085*

Method | HTTP request | Description
------------- | ------------- | -------------
[**addFileUsingPOST**](OperationsControllerApi.md#addFileUsingPOST) | **POST** /addFile | addFile
[**addFileUsingPOST1**](OperationsControllerApi.md#addFileUsingPOST1) | **POST** /setFileType | addFile
[**createProjectUsingPOST**](OperationsControllerApi.md#createProjectUsingPOST) | **POST** /createProject | createProject
[**deleteFileUsingPOST**](OperationsControllerApi.md#deleteFileUsingPOST) | **POST** /deleteFile | deleteFile
[**deleteProjectUsingPOST**](OperationsControllerApi.md#deleteProjectUsingPOST) | **POST** /deleteProject | deleteProject
[**getFileContentUsingPOST**](OperationsControllerApi.md#getFileContentUsingPOST) | **POST** /getFileContent | getFileContent
[**getProjectFilesUsingPOST**](OperationsControllerApi.md#getProjectFilesUsingPOST) | **POST** /getProjectFiles | getProjectFiles
[**getUserProjectsUsingGET**](OperationsControllerApi.md#getUserProjectsUsingGET) | **GET** /getUserProjects | getUserProjects
[**updateFileUsingPOST**](OperationsControllerApi.md#updateFileUsingPOST) | **POST** /updateFile | updateFile


<a name="addFileUsingPOST"></a>
# **addFileUsingPOST**
> List&lt;String&gt; addFileUsingPOST(file, project, type)

addFile

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
byte[] file = BINARY_DATA_HERE; // byte[] | file
String project = "project_example"; // String | project
String type = "type_example"; // String | type
try {
    List<String> result = apiInstance.addFileUsingPOST(file, project, type);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#addFileUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **byte[]**| file |
 **project** | **String**| project |
 **type** | **String**| type |

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: */*

<a name="addFileUsingPOST1"></a>
# **addFileUsingPOST1**
> String addFileUsingPOST1(filename, project, type)

addFile

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
String filename = "filename_example"; // String | filename
String project = "project_example"; // String | project
String type = "type_example"; // String | type
try {
    String result = apiInstance.addFileUsingPOST1(filename, project, type);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#addFileUsingPOST1");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **filename** | **String**| filename |
 **project** | **String**| project |
 **type** | **String**| type |

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="createProjectUsingPOST"></a>
# **createProjectUsingPOST**
> String createProjectUsingPOST(projectName, name)

createProject

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
String projectName = "projectName_example"; // String | project_name
String name = "name_example"; // String | 
try {
    String result = apiInstance.createProjectUsingPOST(projectName, name);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#createProjectUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **projectName** | **String**| project_name |
 **name** | **String**|  | [optional]

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="deleteFileUsingPOST"></a>
# **deleteFileUsingPOST**
> String deleteFileUsingPOST(filename, project)

deleteFile

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
String filename = "filename_example"; // String | filename
String project = "project_example"; // String | project
try {
    String result = apiInstance.deleteFileUsingPOST(filename, project);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#deleteFileUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **filename** | **String**| filename |
 **project** | **String**| project |

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="deleteProjectUsingPOST"></a>
# **deleteProjectUsingPOST**
> String deleteProjectUsingPOST(project, name)

deleteProject

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
String project = "project_example"; // String | project
String name = "name_example"; // String | 
try {
    String result = apiInstance.deleteProjectUsingPOST(project, name);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#deleteProjectUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **project** | **String**| project |
 **name** | **String**|  | [optional]

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getFileContentUsingPOST"></a>
# **getFileContentUsingPOST**
> String getFileContentUsingPOST(filename, project)

getFileContent

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
String filename = "filename_example"; // String | filename
String project = "project_example"; // String | project
try {
    String result = apiInstance.getFileContentUsingPOST(filename, project);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#getFileContentUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **filename** | **String**| filename |
 **project** | **String**| project |

### Return type

**String**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: application/json

<a name="getProjectFilesUsingPOST"></a>
# **getProjectFilesUsingPOST**
> List&lt;File&gt; getProjectFilesUsingPOST(project)

getProjectFiles

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
String project = "project_example"; // String | project
try {
    List<File> result = apiInstance.getProjectFilesUsingPOST(project);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#getProjectFilesUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **project** | **String**| project |

### Return type

[**List&lt;File&gt;**](File.md)

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

<a name="getUserProjectsUsingGET"></a>
# **getUserProjectsUsingGET**
> List&lt;String&gt; getUserProjectsUsingGET()

getUserProjects

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
try {
    List<String> result = apiInstance.getUserProjectsUsingGET();
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#getUserProjectsUsingGET");
    e.printStackTrace();
}
```

### Parameters
This endpoint does not need any parameter.

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: Not defined
 - **Accept**: */*

<a name="updateFileUsingPOST"></a>
# **updateFileUsingPOST**
> List&lt;String&gt; updateFileUsingPOST(file, project, type)

updateFile

### Example
```java
// Import classes:
//import io.swagger.client.ApiClient;
//import io.swagger.client.ApiException;
//import io.swagger.client.Configuration;
//import io.swagger.client.auth.*;
//import io.swagger.client.api.OperationsControllerApi;

ApiClient defaultClient = Configuration.getDefaultApiClient();

// Configure HTTP basic authorization: basicAuth
HttpBasicAuth basicAuth = (HttpBasicAuth) defaultClient.getAuthentication("basicAuth");
basicAuth.setUsername("YOUR USERNAME");
basicAuth.setPassword("YOUR PASSWORD");

OperationsControllerApi apiInstance = new OperationsControllerApi();
byte[] file = BINARY_DATA_HERE; // byte[] | file
String project = "project_example"; // String | project
String type = "type_example"; // String | type
try {
    List<String> result = apiInstance.updateFileUsingPOST(file, project, type);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling OperationsControllerApi#updateFileUsingPOST");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **file** | **byte[]**| file |
 **project** | **String**| project |
 **type** | **String**| type |

### Return type

**List&lt;String&gt;**

### Authorization

[basicAuth](../README.md#basicAuth)

### HTTP request headers

 - **Content-Type**: multipart/form-data
 - **Accept**: */*

