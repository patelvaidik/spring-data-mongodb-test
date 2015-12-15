## Spring Data for MongoDB & Spring Boot Test

This simple project demonstrate the way to utilize Spring Data for MongoDb within a Spring Boot application.
It also demonstrate how to configure and deploy the application to pivotal cloud foundry using a manifest file.


The purpose of this application is only to test and self educate in the technologies and it is not intended for 
production purposes. 


### Description	
The application is a simple gradle spring-boot application that utilizes the following technologies:
	* Spring Boot
	* Spring Data MongoDb
	* Spring Data Rest
	* Spring Web
	
It also uses the following auxiliary libraries:
	* Spring Boot Test
	* Json Path
	* Embedded MongoDb
	
In order to build this application it is necessary to run the following gradle command:

`gradle clean build`

The result should be the executable spring-data-mongodb-test-0.0.1-SNAPSHOT.jar inside the folder build/libs.
To run this jar is necessary to execute the following command from the base path of the project:

`java -jar build/libs/spring-data-mongodb-test-0.0.1-SNAPSHOT.jar`

This will run the server that should be available in the url http://localhost:8080. Check the operations
section to see what resources are available in the server. *Note: To run the application is is necessary to have a local mongodb 
database running. Check mongodb section to review the configuration of the database* 

This project can also be imported into eclipse (or any ide that support gradle extension) and the unit tests can be run within the
ide to review the functionality.

The project can also be deployed to pivotal cloud foundry PaaS in order to work on the cloud instantly. Just run the command
`cf push` from the base directory (where the file manifest.yml is located) and it should work. *Your cf setup should be complete
in order for this command to run. If not ready check the following url to complete installation 
[docs.run.pivotal.io](URL http://docs.run.pivotal.io/starting/index.html)*


### Explanation
In the following sub sections I will try to explain how the application works and how it is configured to work across environments. Each 
sub-section will try to explain one technology and how you can change specific behavior of it.

##### Gradle
We start with the **build.gradle** file that will configure the default behavior of gradle builds. This is a regular build file created
by spring starter that defines a few extra dependencies *spring-boot-starter-data-mongodb* for mongo connections, 
*de.flapdoodle.embed:de.flapdoodle.embed.mongo:1.50.1* for unit testing with mongodb, *com.jayway.jsonpath:json-path:2.1.0* for unit test
of the rest results. Also *org.springframework.cloud:spring-cloud-spring-service-connector* and 
*org.springframework.cloud:spring-cloud-cloudfoundry-connector* are added to make the application cloud ready for pivotal cloudfoundry.

##### MongoDb

The main objective of this application is to demonstrate mongodb database using simple Spring Data Repositories so we will start with 
configuring the connection when we are using development environment (or running our application locally). The file **application.yml**
inside the folder **src/main/resources** declare the default active profile for spring when the application is running as dev. It also
declares the required properties to describe the details to connect to a local mongodb database. These properties are injected to the
class **com.albiworks.test.spdatamongo.DbConfigDev** which is annotated with @Configuration in order to create a MongoDbFactory object
that will serve as the base of the connection. You can see that this class is annotated with @Profile(value="dev") which means the
configurations inside this class will only be loaded when the dev profile is active (which is the default as marked by the application.yml file)


When running unit tests (either during a gradle build or inside an ide) the methods in the class 
**com.albiworks.test.spdatamongo.SpringDataMongodbTestApplicationTests** are executed. This class is located in the source directory
*src/main/test/*. This application is annotated with @ActiveProfiles which will override the default profile defined in the application.yml
file for a profile called "unit-test". Since this class is also annotated with **@RunWith(SpringJUnit4ClassRunner.class)** which will prepare
spring-test framework for our unit tests. Inside the same test package we have a class called **DBConfigUnitTest** which will be loaded
when the unit tests are ran since the @Profile annotation in this class matches the active profile in our unit tests. This class is in charge
to override the MongoDbFactory object (normally created by DbConfigDev class) for one object that will use an embedded mongo database during
our unit tests. This will ensure that we have a clean database for each one of our tests.


When running on cloud foundry (when deployed using cf command line tool and manifest.yml file) an environment variable in the cloud machine
will be declared as SPRING_PROFILES_ACTIVE=cloud. This will override the active profile for the cloud machine and will make the class
**com.abliworks.test.spdatamongo.DbConfigCloud** override the MongoDbFactory object for the one retrieved from the cloud services
using the auxiliary class **AbstractCloudConfig**. This class is part of the spring-cloud-connectors project (you can check more in this 
[link](http://cloud.spring.io/spring-cloud-connectors/))

##### Spring Data MongoDb

By now you might have realized that we are using spring data for mongodb by enabling the annotation **@EnableMongoRepositories** in our
configuration classes. This enables our application to declare spring repository typed interfaces and let spring framework to leverage
all our querying to the database. The repository for this application is located in the package *com.albiworks.test.spdatamongo.repository*
which enable our application to save the domain class com.albiworks.test.spdatamongo.domain.Message in our mongo database. Mongo Repositories
require a MongoDbFactory object to be enabled, this is why it is important to declare the correct factory depending the environment what we 
are located at. The rest will be taken care of by the respository instance automatically allowing to save the object in the correct location.
To know more about spring-data-mongobd you can visit this [link](http://projects.spring.io/spring-data-mongodb/).

##### Spring Rest Mvc

The rest of the application is a simple Rest MVC application that will declare simple rest controllers in order to serve a Rest Interface to 
the user. The controller is located in the *com.albiworks.test.spdatamongo.resources* package and very well explained by the spring-rest project
in this [link](https://spring.io/guides/gs/rest-service/)



### How to use in the cloud with pivotal Cloud Foundry
As mentioned before the application is ready to be deployed in pivotal cloud foundry PaaS. This is done using the **manifest.yml** file which is
located in the base directory of the project. This file declares the needed information to deploy the app in the platform based on the explanation
described by pivotal in this [article](https://docs.cloudfoundry.org/devguide/deploy-apps/manifest.html#find-manifest). Most of the properties in
this file are self explanatory but there is one dependency required by the platform (as you might guess by now its the mongodb service required in
the cloud profile). 

In order to create the mongo db service in the platform so it is available for the application the following command can be run:
`cf create-service mongolab sandbox mongodb`
This will create the mongodb service declared as dependency for the application in the manifest.yml file.

Once that this is complete it is just needed to run `cf push` command any time that the application wants to be deployed or updated.


