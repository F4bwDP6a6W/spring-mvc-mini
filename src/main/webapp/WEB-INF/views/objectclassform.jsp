<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>objectclasslist forms | mini</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
</c:if>
	<div id="formsContent2">
		<p id="">
			Enter the Object Class information which to reserve.
		</p>
		<p>
			More than 1, please select "Add More" button.
		</p>
		<p id="waitmessage"></p>
		<form:form id="form2" method="post" modelAttribute="ojbclslisttype" cssClass="cleanform">
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
		  								  			
            <c:forEach items="${ojbclslisttype.objectclasslist}" var="objcls" varStatus="i" begin="0">
            	<fieldset>	
            			<legend>Object Class Info: ${i.index+1} </legend>
					<label>
						id
					</label>
			  		<input name="objectclasslist[${i.index}].id" value="${objcls.id}">
			
			  		<label>
			  			intclass 
			 		</label>
			  		<input name="objectclasslist[${i.index}].intclass" value="${objcls.intclass}" readonly>
			  		
			  		<label>
			  			abbrev 
			 		</label>
			  		<input name="objectclasslist[${i.index}].abbrev" value="${objcls.abbrev}">
			  		 
			  		<label>
			  			adaID
			  		</label>
			  		<input name="objectclasslist[${i.index}].adaID" value="${objcls.adaID}">
			
			  		<label>
			  			name 
			  		</label>
			  		<input name="objectclasslist[${i.index}].name" value="${objcls.name}">
			  		
			  		<label>
			  			parents
			  		</label>
		  			<input name="objectclasslist[${i.index}].parents" value="${objcls.parents}">
		  			
		  			<label>
			  			comment
			  		</label>
		  			<input name="objectclasslist[${i.index}].comment" value="${objcls.comment}" readonly>
				</fieldset>
		      </c:forEach>
		  	
		  	<p><button id="addmore" type="button">Add More</button></p>
			<p><button id="submitbutton" type="submit">Submit</button></p>
		</form:form>
		
		<script type="text/javascript">
			$(document).ready(function() {
				$("#form2").submit(function() {  
					$("#submitbutton").hide();
					$("#waitmessage").replaceWith("<div id=\"message\" class=\"success\">Please be patience, the server is handling.</div>");
					$.post($(this).attr("action"), $(this).serialize(), function(html) {
						$("#formsContent2").replaceWith(html);
						$('html, body').animate({ scrollTop: $("#message").offset().top }, 500);
					});
					return false;  
				});		
				$("#addmore").click(function() {
					 $.post("/spring-mvc-mini/objectclassform" ,
							$("#form2").serialize()+"&objclscount=${fn:length(ojbclslisttype.objectclasslist)}",
							 function(html){$("#formsContent2").replaceWith(html);}
							 );
				});	
			});
		</script>
	</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>