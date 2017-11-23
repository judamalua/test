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

<display:table name = "notes" id = "row" 
	requestURI = "note/list.do" pagesize = "5" class = "displaytag">

	<spring:message code = "note.moment" var = "momentHeader"/>
	<display:column property = "moment" title = "${momentHeader}" sortable = "true"/>

	<spring:message code = "note.remark" var = "remarkHeader"/>
	<display:column property = "remark" title = "${remarkHeader}" sortable = "false"/>
	
	<jstl:if test = "${not empty row.reply}">
		<spring:message code = "note.reply" var = "replyHeader"/>
		<display:column property = "reply" title = "${replyHeader}" sortable = "false"/>
	</jstl:if>
	
	<jstl:if test = "${not empty row.momentOfReply}">
		<spring:message code = "note.momentOfReply" var = "momentOfReplyHeader"/>
		<display:column property = "momentOfReply" title = "${momentOfReplyHeader}" sortable = "false" />
	</jstl:if>
	
	<security:authorize access = "hasRole('MANAGER')">
		<display:column>
			<a href = "note/manager/reply.do?noteId=${row.id}">
				<spring:message code = "note.reply"/>
			</a>
		</display:column>
	</security:authorize>

</display:table>
