# P07_DAJAVA_SpringBoot_RestApi

## First Run

Build project with Maven file _pom.xml_ to import all dependencies.
    
    mvn package
    mvn clean install

Install a MySQL/Maria DBB like [Xampp](https://www.apachefriends.org/fr/index.html). 

### BDD Configuration

Configure the access to the database in *application.properties* file. 
Execute the application once: on the first run, all tables will be build. 
Open a Browser and create the first user with this URL: http://localhost:8080/user/add

    
## Resources
    
The project need _Java JDK 11_ or newer.
Open JDK is recommended: https://adoptopenjdk.net

The project use _Spring Boot 2.4_ https://start.spring.io 

### Dependencies 
     
    Spring Web (Tomcat),
    Spring Security,
    Spring Validation,
    Spring Data JPA,
    Mysql-connector-java
    Thymeleaf,
    Lombok,
    Mapstruct,
    Passay,    	
    Springfox-swagger-ui

## Application End-Points

All application End-Points can be find on one public html page (swagger):

    http://localhost:8080/swagger-ui.html

### List of All Public End-Points (Loging is not necessary)
    
    http://localhost:8080/login
    http://localhost:8080/user/list
    http://localhost:8080/user/add
    http://localhost:8080/home
    http://localhost:8080
    http://localhost:8080/swagger-ui.html



	