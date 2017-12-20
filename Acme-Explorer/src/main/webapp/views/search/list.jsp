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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="format.date" var="formatDate"/>

<h2><spring:message code = "search.show"></spring:message></h2>
<spring:message code="format.date" var="formatDate"/>

<display:table name = "searchs" id = "search" 
	requestURI = "search/admin/list.do" pagesize = "${pagesize}" class = "displaytag">

	<spring:message code = "search.keyWord" var = "keyWordHeader"/>
	<display:column property = "keyWord" title = "${keyWordHeader}" sortable = "true"/>

	<spring:message code = "search.priceRangeStart" var = "priceRangeStartHeader"/>
	<display:column property = "priceRangeStart" title = "${priceRangeStartHeader}"  format="${formatDate}" sortable = "false"/>
	
	<spring:message code = "search.priceRangeEnd" var = "priceRangeEndHeader"/>
	<display:column property = "priceRangeEnd" title = "${priceRangeEndHeader}" sortable = "true" format="${formatDate}"/>

	<spring:message code = "search.dateRangeStart" var = "dateRangeStartHeader"/>
	<display:column property = "dateRangeStart" title = "${dateRangeStartHeader}"  format="${formatDate}" sortable = "false"/>
		
	<spring:message code = "search.dateRangeEnd" var = "dateRangeEndHeader"/>
	<display:column property = "dateRangeEnd" title = "${dateRangeEndHeader}"   format="${formatDate}" sortable = "false"/>	
	
	<spring:message code = "search.searchMoment" var = "searchMomentHeader"/>
	<display:column property = "searchMoment" title = "${searchMomentHeader}"   format="${formatDate}" sortable = "false"/>	
		
		
		
</display:table>


