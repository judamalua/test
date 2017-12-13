
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name = "actors" id = "row" 
	requestURI = "${requestUri}" pagesize = "5" class = "displaytag">

	<spring:message code = "actor.name" var = "name"/>
	<display:column property = "name" title = "${name}" sortable = "true"/>
	
	<spring:message code = "actor.surname" var = "surname"/>
	<display:column property = "surname" title = "${surname}" sortable = "true"/>
	
	<spring:message code = "actor.email" var = "email"/>
	<display:column property = "email" title = "${email}" sortable = "true"/>

	<display:column>
	<jstl:if test="${!row.isBanned}">
		<a href = "actor/admin/ban.do?actorId=${row.id}">
			<spring:message code = "actor.ban"/>
		</a>
	</jstl:if>
	</display:column>
	
</display:table>
