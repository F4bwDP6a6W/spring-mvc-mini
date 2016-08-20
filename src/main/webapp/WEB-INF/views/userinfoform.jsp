<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="true" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>UserInfo forms | mini</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
</c:if>
	<div id="formsContent1">
		<p>
			Enter your nsn-intra account information:
		</p>
		<p id="waitmessage"></p>
		<form:form id="form1" method="post" modelAttribute="userinfo" cssClass="cleanform">
			<div class="header">
		  		<c:if test="${not empty message}">
					<div id="message" class="success">${message}</div>	
		  		</c:if>
		  		<s:bind path="*">
		  			<c:if test="${status.error}">
				  		<div id="message" class="error">Form has errors</div>
		  			</c:if>
		  		</s:bind>
			</div>
		  	<fieldset>
		  		<legend>User Info</legend>
		  		<form:label path="username">
		  		   username
		 		</form:label>
		  		<form:input path="username" />
	
		  		<form:label type="email" path="email">
		  			email
		 		</form:label>
		  		<form:input path="email" />

		  		<form:label type="password" path="password">
		  			password
		 		</form:label>
		  		<form:input type="password" path="password" />
		  				  		 
		  	</fieldset>
	
			<p><button id="submitbutton" type="submit">Submit</button></p>
		</form:form>
		<script type="text/javascript">
			$(document).ready(function() {	
						
				$("#form1").submit(function() {  
					$("#waitmessage").replaceWith("<div id=\"message\" class=\"success\">Please be patience, the server is handling.</div>");
					$("#submitbutton").hide();
					$.post($(this).attr("action"), $(this).serialize(), function(html) {
						$("#formsContent1").replaceWith(html);
						$('html, body').animate({ scrollTop: $("#message").offset().top }, 500);
					});
					return false;  
				});	
			});
		</script>
	</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>