<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page session="false" %>
<c:if test="${!ajaxRequest}">
<html>
<head>
	<title>ObjectClass List | mini</title>
	<link href="<c:url value="/resources/table.css" />" rel="stylesheet"  type="text/css" />	
	<script type="text/javascript" src="<c:url value="/resources/jquery/1.6/jquery.js" />"></script>
</head>
<body>
</c:if>
	<h2><a href="<c:url value="/" />" title="Home">Back To Home</a></h2>
	<h5><input id="searchcritical" value="id\intclass\comment\etc" > <a id="searchbutton" href="">Search</a></h5>
	<div id="formsContent">
		<div class="CSSTableGenerator" >
		<table>
                    <tr>
                        <td>id</td>
                        <td>intclass</td>
                        <td>abbrev</td>
                        <td>adaID</td>
                        <td>name</td>
                        <td>parents</td>
                        <td>comment</td>
                    </tr>
                    <c:forEach items="${productList.pageList}" var="ObjectClass" varStatus="i" begin="0">
						<tr>
							<td>${ObjectClass.id}</td>
	                        <td>${ObjectClass.intclass}</td>
	                        <td>${ObjectClass.abbreviation}</td>
	                        <td>${ObjectClass.packageName}</td>
	                        <td>${ObjectClass.name}</td>
	                        <td>${ObjectClass.parents}</td>
	                        <td>${ObjectClass.comment}</td>
			            </tr>
                    </c:forEach>
            </table>
          				<c:if test="${!productList.firstPage}">
						    <a href="?jumppage=previous"><h5>&lt;&lt; Prev &lt;&lt; </h5></a>
						</c:if>
						<c:if test="${!productList.lastPage}">
	    					<a href="?jumppage=next"><h5> &gt;&gt; Next &gt;&gt; </h5></a>
						</c:if>
						<input id="numberjumpvalue" value="1" >
						<a id="numberjump" href=""><h5> &lt;&lt; Jump To &gt;&gt; </h5></a>
            </div>
            
            <script type="text/javascript">
				$(document).ready(function() {
					$("#numberjump").click(function() {
						$("#numberjump").attr('href', "?jumppage="+$("#numberjumpvalue").val());
						$(location).attr('href', "?jumppage="+$("#numberjumpvalue").val());
					});	
					$("#searchbutton").click(function() {
						$("#searchbutton").attr('href', "?searchcritical="+$("#searchcritical").val());
						$(location).attr('href', "?searchcritical="+$("#searchcritical").val());
					});	
				});
			</script>
	</div>
<c:if test="${!ajaxRequest}">
</body>
</html>
</c:if>