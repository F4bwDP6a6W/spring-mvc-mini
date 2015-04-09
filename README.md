Spring-mvc-mini is a mini project using Spring MVC.
-------------------	
In this project, you can see the code of:
*spring-webmvc
*svnkit
*javax.mail
*jasypt
*dom4j
*spring scheduler

To run the application:
-------------------	
From the command line with Maven:

    $ cd spring-mvc-mini
    $ mvn tomcat7:run [-Dmaven.tomcat.port=<port no.>] (In case 8080 is busy] 

or

In your preferred IDE such as Eclipse:

    $ cd spring-mvc-mini
    $ mvn eclipse:eclipse
	
* Import spring-mvc-mini as a Maven Project

Access the deployed web application at: http://localhost:8080/spring-mvc-mini

Note:
-------------------

If you want to deploy this project to Linux server, you might need to edit conf files:
spring-oss-mini\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml:<context:property-placeholder location="file:/opt/web/spring-mvc-mini/resources/application.properties"/>
spring-oss-mini\src\main\resources\logback.xml
spring-oss-mini\resources\application.properties


