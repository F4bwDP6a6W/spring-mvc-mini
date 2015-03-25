This application is a project using Spring MVC.

To run the application:
-------------------	
From the command line with Maven:

    $ cd companyname-projectname-mocr
    $ mvn tomcat7:run [-Dmaven.tomcat.port=<port no.>] (In case 8080 is busy] 

or

In your preferred IDE such as Eclipse:

    $ cd companyname-projectname-mocr
    $ mvn eclipse:eclipse
	
* Import companyname-projectname-mocr as a Maven Project

Access the deployed web application at: http://localhost:8080/companyname-projectname-mocr

Note:
-------------------

If you want to deploy this project to Linux server, you might need to edit conf files:
companyname-oss-mocr\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml:<context:property-placeholder location="file:/opt/web/companyname-projectname-mocr/resources/application.properties"/>
companyname-oss-mocr\src\main\resources\logback.xml
companyname-oss-mocr\resources\application.properties


/user/local/tomcat7/apache-tomcat-7.0.57/webapps
/opt/web/companyname-projectname-mocr

