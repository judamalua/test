<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name = "survivalclass" id = "row" 
	requestURI = "survivalclass/list.do" pagesize = "5" class = "displaytag">

	<spring:message code = "survivalclass.title" var = "title"/>
	<display:column property = "title" title = "${title}" sortable = "true"/>

	<spring:message code = "survivalclass.description" var = "description"/>
	<display:column property = "description" title = "${description}" sortable = "false"/>
	
	<spring:message code = "survivalclass.organisationMoment" var = "organisationMoment"/>
	<display:column property = "organisationMoment" title = "${organisationMoment}" sortable = "true"/>
	
	<spring:message code = "survivalclass.location.name" var = "locationName"/>
	<display:column property = "locationName" title = "${locationName}" sortable = "true"/>
	
	<spring:message code = "survivalclass.location.gpsCoordinates" var = "gpsCoordinates"/>
	<display:column property = "gpsCoordinates" title = "${gpsCoordinates}" sortable = "true"/>
	
	<spring:message code="survivalclass.trips" var="trips"/>
	<display:column title="${trips}" sortable="true" >
		<ul>
			<jstl:forEach var="trip" items="${survivalclass.trips}">
				<li>
					${trip.title}
				</li>
			</jstl:forEach>
		</ul>
	</display:column>


	
	<security:authorize access = "hasRole('MANAGER')">
		<display:column>
			<a href = "survivalclass/manager/edit.do?survivalClassId=${row.id}">
				<spring:message code = "survivalclass.edit"/>
			</a>
		</display:column>
	</security:authorize>
	
	<security:authorize access = "hasRole('EXPLORER')">
		<display:column>
			<a href = "survivalclass/auditor/join.do?survivalClassId=${row.id}">
				<spring:message code = "survivalclass.join"/>
			</a>
		</display:column>
	</security:authorize>

</display:table>
