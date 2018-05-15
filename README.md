# Elections

This repository contains the source of web application which is a Maven project.

Tools used:
* Java 8
* Spring MVC with Java configuration
* JPA/Hibernate
* MySQL relational database 
* Thymeleaf
* Bootstrap
* HTML
* CSS

To start this application is needed:
* Java IDE
* server/web application container (e.g. Tomcat)
* MySQL database which should be configured according to settings contained in the application.properties file
in the resources directory
* the appropriate setting to static resources (modification is needed in the addResourceHandlers method 
in AppConfiguration class in configuration directory),

The application enables the simulation of election of candidates from different constituencies, 
included next rounds.

Hints:

* Holding the elections requires consecutively proper configuration of constituencies, candidates and amount of voters.

* After the elections, when you want to hold it once more for the same data, another configuration of voters is required.

 * The button named „KASOWANIE DANYCH” is used to delete all data in the database in order that the new constituencies, candidates and voters can be set up.


The project was created by Marta Gładysz.
