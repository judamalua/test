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

<jstl:set value="auditor" var="role"/>
<security:authorize access="hasRole('MANAGER')">
	<jstl:set value="manager" var="role"/>
</security:authorize>

<spring:message code="format.date" var="formatDate"/>
<spring:message code="format.price" var="formatPrice"/>
<display:table name = "notes" id = "row" 
	requestURI = "note/${role}/list.do" pagesize = "${pagesize}" class = "displaytag">

	<spring:message code = "note.moment" var = "momentHeader"/>
	<display:column property = "moment" title = "${momentHeader}" sortable = "true" format="${formatDate}"/>

	<spring:message code = "note.remark" var = "remarkHeader"/>
	<display:column property = "remark" title = "${remarkHeader}"/>
	
	<jstl:if test = "${not empty row.reply}">
		<spring:message code = "note.reply" var = "replyHeader"/>
		<display:column property = "reply" title = "${replyHeader}" />
	</jstl:if>
	
	<jstl:if test = "${not empty row.momentOfReply}">
		<spring:message code = "note.momentOfReply" var = "momentOfReplyHeader"/>
		<display:column property = "momentOfReply" title = "${momentOfReplyHeader}" format="${formatDate}"/>
	</jstl:if>
	
	<security:authorize access = "hasRole('MANAGER')">
	
		<display:column>
		<jstl:if test="${row.reply != null}">
			<a href = "note/manager/edit.do?noteId=${row.id}">
				<spring:message code = "note.reply"/>
			</a>
		</jstl:if>
		</display:column>
	</security:authorize>

</display:table>
