<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name = "survivalclass" id = "row" 
	requestURI = "survivalClass/explorer/list-joined.do" pagesize = "5" class = "displaytag">

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
	
		<display:column>
			<a href = "survivalClass/explorer/leave.do?survivalClassId=${row.id}">
				<spring:message code = "survivalclass.leave"/>
			</a>
		</display:column>
	


</display:table>
