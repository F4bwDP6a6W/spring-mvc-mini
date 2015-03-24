Edit conf files:
companyname-oss-mocr\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml:<context:property-placeholder location="file:/opt/web/companyname-projectname-mocr/resources/application.properties"/>

companyname-oss-mocr\src\main\resources\logback.xml

companyname-oss-mocr\resources\application.properties

/user/local/tomcat7/apache-tomcat-7.0.57/webapps

/opt/web/companyname-projectname-mocr

To run the application:
-------------------	
From the command line with Maven:

    $ cd companyname-projectname-mocr
    $ mvn tomcat7:run [-Dmaven.tomcat.port=<port no.>] (In case 8080 is busy] 

or

In your preferred IDE such as SpringSource Tool Suite (STS) or IDEA:

* Import companyname-projectname-mocr as a Maven Project
* Drag-n-drop the project onto the "SpringSource tc Server Developer Edition" or another Servlet 2.5 or > Server to run, such as Tomcat.

Access the deployed web application at: http://localhost:8080/companyname-projectname-mocr

Note:
-------------------


