# messi
Sample mock server to mock APIs without writing any mock code manually. Built with vertx web to showcase the usage of [vertx-boot]
(https://github.com/greyseal/vertx-boot) library. 
```diff
- This library is mandatory. You must download vertx-boot before running messi.
- Your directory structure should be like: 
/workspace
  /messi
    ...
  /vertx-boot
    ...
```

## Getting Started

Git clone the project on your local machine and import it to your favorite ide.

### Prerequisites

For runnning this, you will need
- Java 1.8
- Gradle support - In Eclipse editor, goto help -> eclipse marketplace -> search for buildship (buildship gradle integration) and install it.
- [Vertx-Boot](https://github.com/greyseal/vertx-boot) library. 

## Brief
This sample application make use of [Vertx-Boot](https://github.com/greyseal/vertx-boot) library to expose a rest API **/runner/api/ping**
- **HttpServerVerticle**       -> Default verticle from the vertx-boot library. Can be extened for the functionality.
- **DatabaseVerticle**         -> Database verticle to store the mocks.
- **MessiMockHandler**         -> Sample handler to create mocks and then use to get the desired results.
- **PingHandler**              -> Default handler from the vertx-boot library to send a "OK" Json response.

## Running the app

For running the app, (IDE used here is **IntelliJ**)
- Open **appConfig.json** file and set the "http_server_port" as per your choice. Set the **mongo** details as well.
- Once, changes are done in **appConfig.json**, add/edit Run/Debug Configurations for the project("messi") and set:
  * **Main class**: com.greyseal.vertx.boot.AppLauncher
  * **VM options**: -Dlogback.configurationFile=file:../messi/src/main/resources/logback.xml
  * **Program arguments**: run com.greyseal.vertx.boot.verticle.MainVerticle -conf ../messi/src/main/resources/appConfig.json 
  * **Use classpath of module**: messi_main
  * **JRE**: 1.8
  * **Environment variables**: ENV=dev (default is dev)
 <br /><br />
 ![alt text](https://github.com/greyseal/messi/blob/master/src/main/resources/messi.png) <br />
 
In terminal/command prompt, do `gradle clean build` or `./gradlew clean build`  <br />
Now Run/Debug the project. If app starts successfully, then try <br /><br />
```
curl -X GET \
  http://localhost:8080/messi/ping \
  -H 'cache-control: no-cache' \
  -H 'content-type: application/json' \
  -H 'trace-id: c1d887063c3e492b9951b0479fadddda'
```
<br />
or in PostMan, try <br />

**Type:**  *GET http://localhost:8080/messi/ping* <br />
**Headers:**  *Content-Type: application/json*;  *Trace-Id: c1d887063c3e492b9951b0479fadddda* <br />

Response would be: <br />
```
{
    "status": "OK"
}
```
That's it.

## Messi usage
Currently added support to add a mock and then fetch it to consume. Run/debug the application...<br  /><br  />
* To create a Mock **MockCreate** <br /><br />
**Type:** *POST http://localhost:8080/messi/mock/create* <br />
**Headers:** *Content-Type: application/json*;  *Trace-Id: c1d887063c3e492b9951b0479faddddu* <br />
**Request body:**
 ```
 {
  "response": {
    "firstName": "First Name",
    "lastName": "Last Name"
  },
  "headers": {
    "Content-Type": "application/json",
    "X-Id": "Id"
  },
  "statusCode": 200,
  "url": "/corona/user/get/123",
  "httpMethod": "GET"
}
 ```
 where, <br />
 **response** is the response of the API to mock. <br />
 **headers** are the response headers of the API to mock. <br />
 **statusCode** is the statusCode for which Mock should return the above response. <br />
 **url** is the URL used to fetch the Mock API result. <br />
 **httpMethod** is the http method for which Mock should return the above response. <br /> <br />
 **Response:**
 ```
{
  "createdBy": "saurabh",
  "headers": {
    "Content-Type": "application/json",
    "X-Id": "Id"
  },
  "httpMethod": "GET",
  "isActive": true,
  "response": {
    "firstName": "First Name",
    "lastName": "Last Name"
  },
  "statusCode": 200,
  "updatedBy": "saurabh",
  "url": "/corona/user/get/123",
  "_id": "5bc62bbcbda0fc88432db0dc"
}
 ```
<br /> <br />
* To fetch the mock result for an API **FetchMock** <br /><br />
**Type:** *{HTTP_METHOD} {BASE_URL}/{MOCK_API_URL}* <br />
where, <br />
  1. **HTTP_METHOD** = Http method [Get, Post, Put, Delete etc]. <br />
  1. **BASE_URL** = http://localhost:8080/messi/mock/server This is constant.<br />
  2. **MOCK_API_URL**=corona/user/get/123?statusCode={**status_code**} This is the dynamic URL or URL for which mock result is required. This is the URL which we have passed while creating the Mock #**MockCreate**  <br />
  3. **status_code** = Http Status code for which Mock result has to be prepared. Default is 200.<br />
  
  **Headers:** *Content-Type: application/json*;  *Trace-Id: c1d887063c3e492b9951b0479faddddu* <br />
 **Response:**
 ```
{
  "firstName": "First Name",
  "lastName": "Last Name"
}
 ```
 
## To run as server
In terminal/command prompt, go to messi directory (cd messi) and then type  
1. `gradle clean build shadowJar` or `./gradlew clean build shadowJar`
2. java -jar build/libs/messi-0.0.1-fat.jar -conf ../messi/src/main/resources/appConfig.json -Dlogback.configurationFile=file:../messi/src/main/resources/logback.xml
 
## Built With

* [Vertx](http://vertx.io/) - The web framework used
* [Gradle](https://gradle.org/) - Dependency Management
