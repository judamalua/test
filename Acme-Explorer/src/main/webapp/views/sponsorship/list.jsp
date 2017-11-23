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

<display:table name = "sponsorship" id = "row" 
	requestURI = "sponsorship/list.do" pagesize = "5" class = "displaytag">

	<spring:message code = "sponsorship.bannerURL" var = "bannerURL"/>
	<display:column property = "bannerURL" title = "${bannerURLHeader}" sortable = "true"/>

	<spring:message code = "sponsorship.additionalInfo" var = "additionalInfo"/>
	<display:column property = "additionalInfo" title = "${additionalInfo}" sortable = "false"/>
	
	<spring:message code = "sponsorship.trip.title" var = "trip"/>
	<display:column property = "trip.title" title = "${trip}" sortable = "false"/>
	
	<display:column>
		<a href = "sponsorship/edit.do?sponsorshipId=${row.id}">
			<spring:message code = "sponsorship.reply"/>
		</a>
	</display:column>
	
	

</display:table>
