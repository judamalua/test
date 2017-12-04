<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table 
	name="applications"
	id="row"
	requestURI="${requestUri}"
	pagesize="10"
	class="displayTag">
	
	<spring:message code="application.date" var="date"/>
	<display:column property="date" title="${date}" sortable="true" />
	
	<spring:message code="application.commentaries" var="commentaries"/>
	<display:column property="commentaries" title="${commentaries}" sortable="true" />
	
	<spring:message code="application.status" var="status"/>
	<display:column property="status" title="${status}" sortable="true" />
	
	<spring:message code="application.rejection" var="rejection"/>
	<display:column property="rejection.reason" title="${rejection}" sortable="true"/>
	
	<spring:message code="application.trip" var="trip"/>
	<display:column property="trip.title" title="${row.trip.title}" sortable="true"/>
	
	<security:authorize access="hasRole('MANAGER')">
	
	<display:column>
		<a href="application/manager/edit.do">
			<spring:message code="application.edit"/>
		</a>
	</display:column>
	</security:authorize>
	
</display:table>