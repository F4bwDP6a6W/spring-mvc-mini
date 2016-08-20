Spring-mvc-mini是一个完整的，轻量、简单的Java项目，基于Spring MVC.
里面有基本的增删改查的功能。而且无需修改任何内容，就可以直接跑起来。

另外我有一个Spring MVC的RESTful项目，数据库是Mysql，请参看：https://github.com/hot13399/spring-mvc-REST

-------------------
在这个项目里主要实现了以下技术：
* spring-webmvc
* svnkit
* jgit
* javax.mail
* jasypt
* dom4j
* spring scheduler

如何运行：
-------------------

在Windows的CMD：

    $ cd spring-mvc-mini
    $ mvn tomcat7:run [-Dmaven.tomcat.port=<port no.>] (In case 8080 is busy] 

通过浏览器打开：http://localhost:8080/spring-mvc-mini

如果你想要学习或贡献和这个项目：

就通过maven把它build成一个IDE项目，执行以下命令，打开CMD：

    $ cd spring-mvc-mini
    $ mvn eclipse:eclipse or mvn idea:idea

通过Eclipse或IDEA导入即可。

Note:
-------------------

 如果你要在Linux环境运行，以下的文件需要修改。

    $ spring-oss-mini\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml:<context:property-placeholder 
	$ location="file:/opt/web/spring-mvc-mini/resources/application.properties"/>
	$ spring-oss-mini\src\main\resources\logback.xml
	$ spring-oss-mini\resources\application.properties

 最后通过Maven build一个war包部署即可。



--------------------
Spring-mvc-mini is a mini project using Spring MVC.

In this project, you can see the code of:
* spring-webmvc
* svnkit
* javax.mail
* jasypt
* dom4j
* spring scheduler

To run the application:
From the command line with Maven:
    $ cd spring-mvc-mini
    $ mvn tomcat7:run [-Dmaven.tomcat.port=<port no.>] (In case 8080 is busy]

Access the deployed web application at: http://localhost:8080/spring-mvc-mini

To contribute to this project:
In your preferred IDE such as Eclipse:
    $ cd spring-mvc-mini
    $ mvn eclipse:eclipse

Import spring-mvc-mini as a Maven Project

If you want to deploy this project to Linux server, you might need to edit conf files:

    $ spring-oss-mini\src\main\webapp\WEB-INF\spring\appServlet\servlet-context.xml:<context:property-placeholder
	$ location="file:/opt/web/spring-mvc-mini/resources/application.properties"/>
	$ spring-oss-mini\src\main\resources\logback.xml
	$ spring-oss-mini\resources\application.properties


