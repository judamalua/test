<%--
 * action-2.jsp
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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<jstl:set value="auditor" var="role"/>
<jstl:set value="false" var="enabled"/>
<security:authorize access="hasRole('MANAGER')">
	<jstl:set value="manager" var="role"/>
	<jstl:set value="true" var="enabled"/>
</security:authorize>

<form:form action = "note/${role}/edit.do" modelAttribute = "note">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "auditor" />
	<form:hidden path = "trip" />
	<form:hidden path = "moment" />
	
	<security:authorize access="hasRole('MANAGER')">
		<fmt:formatDate var="moment" value="${momentOfReply}" pattern="dd/MM/yyyy hh:mm"/>
		
		<form:hidden path = "momentOfReply" value="${moment}"/>
		<form:hidden path = "replierManager" value="${manager.id}"/>
		<form:hidden path = "remark" />
	</security:authorize>

	<security:authorize access="hasRole('AUDITOR')">
		<form:label path = "remark">
			<spring:message code = "note.remark" />:
		</form:label>
		<form:input disabled="${enabled}"  path = "remark" />
		<form:errors cssClass = "error" path = "remark" />
		<br />
	</security:authorize>
	
	<security:authorize access="hasRole('MANAGER')">
	
		<form:label path = "reply">
			<spring:message code = "note.reply" />:
		</form:label>
		<form:input path = "reply" />
		<form:errors cssClass = "error" path = "reply" />
		<br />
		
	</security:authorize>
	
	<input type = "submit" name = "save" value = "<spring:message code = "note.save"/>" />
	<%-- <jstl:if test="${note.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="note.delete" />"
			onclick="return confirm('<spring:message code='note.confirm.delete' />') " /> --%>
	<%-- </jstl:if> --%>
	
	<a href="trip/detailed-trip.do?tripId=${note.trip.id}&anonymous=false">
		<input type = "button" name = "cancel" value = "<spring:message code = "note.cancel" />"/>
	</a>


</form:form>
