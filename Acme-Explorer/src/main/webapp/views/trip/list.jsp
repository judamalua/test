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

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table
	name="trips"
	id="trip"
	requestURI="trip/list.do"
	pagesize="10"
	class="displaytag">
	
	
	<spring:message code="trip.title" var="title"/>
	<display:column property="title" title="${title}" sortable="true" />
	
	<spring:message code="trip.price" var="price"/>
	<display:column property="ticker" title="${price}" sortable="true" />
		
	<spring:message code="trip.startDate" var="startDate"/>
	<display:column property="ticker" title="${startDate}" sortable="true" />
		
	<spring:message code="trip.endDate" var="endDate"/>
	<display:column property="ticker" title="${endDate}" sortable="true" />
	
	<spring:message code="trip.moreDetails" var="moreDetails"/>
	<display:column title="${moreDetails}" sortable="true" >
		<a href="trip/detail-list.do?tripId=${trip.id}">
			<spring:message code="trip.moreDetails"/>
		</a>
	</display:column>
	
</display:table>

<security:authorize access="hasRole('MANAGER')">
	<a href="trip/manager/edit.do">
			<spring:message code="trip.create"/>
	</a>
</security:authorize>
