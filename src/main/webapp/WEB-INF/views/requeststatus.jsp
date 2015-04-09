<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>requeststatus forms | mini</title>
	<link href="<c:url value="/resources/form.css" />" rel="stylesheet"  type="text/css" />		
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
</c:if>
	<div id="formsContent3">
		<p>
			Enter the MO CR id to check request status and give comments.
		</p>
		<form:form id="form3" method="post" modelAttribute="miniequeststatus" cssClass="cleanform">
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
		  			<label>
					MO CR id
					</label>
			  		<input name="mocrid" value="${miniequeststatus.mocrid}">
			  		
					<label>
					Applicant email
					</label>
			  		<input name="userinfo.email" value="${miniequeststatus.userinfo.email}" readonly>
			  					  		
					<label>
					Status
					</label>
					<input name="status" value="${miniequeststatus.status}" readonly>
					
					<label>
					comments
					</label>
					<textarea form ="form3" name="comments" cols="45" wrap="soft">${miniequeststatus.comments}</textarea>		
			  		
			  		
		  	</fieldset>						  			
            <c:forEach items="${miniequeststatus.ojbclslisttype.objectclasslist}" var="objcls" varStatus="i" begin="0">
            	<fieldset>	
            			<legend>Object Class Info: ${i.index+1} </legend>
					<label>
						id
					</label>
			  		<input name="ojbclslisttype.objectclasslist[${i.index}].id" value="${objcls.id}">
			
			  		<label>
				  		intclass 
				 	</label>
			  		<input name="ojbclslisttype.objectclasslist[${i.index}].intclass" value="${objcls.intclass}" readonly>
			  		
			  		<label>
			  			abbrev 
			 		</label>
			  		<input name="ojbclslisttype.objectclasslist[${i.index}].abbrev" value="${objcls.abbrev}">
			  		 
			  		<label>
			  			adaID
			  		</label>
			  		<input name="ojbclslisttype.objectclasslist[${i.index}].adaID" value="${objcls.adaID}">
			
			  		<label>
			  			name 
			  		</label>
			  		<input name="ojbclslisttype.objectclasslist[${i.index}].name" value="${objcls.name}">
			  		
			  		<label>
			  			parents
			  		</label>
		  			<input name="ojbclslisttype.objectclasslist[${i.index}].parents" value="${objcls.parents}">
		  			
		  			<label>
			  			comment
			  		</label>
		  			<input name="ojbclslisttype.objectclasslist[${i.index}].comment" value="${objcls.comment}" readonly>
				</fieldset>
		      </c:forEach>
			<p><button type="submit">Submit</button></p>
		</form:form>
		
		<script type="text/javascript">
		
			$( document ).ready(function() {
				
				
				console.log($('input[name=mocrid]').val());
				console.log($('input[name="userinfo.email"]').val());
				
				if(!($('input[name="userinfo.email"]').val() == null || $('input[name="userinfo.email"]').val() == "")){
					
					$('input[name=mocrid]').attr('readonly', true);
				}
				
			    $("#form3").submit(function() {  
					$.post($(this).attr("action"), $(this).serialize(), function(html) {
						$("#formsContent3").replaceWith(html);
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