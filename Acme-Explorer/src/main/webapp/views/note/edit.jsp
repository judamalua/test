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
	<form:hidden path = "replierManager" />
	<security:authorize access="hasRole('AUDITOR')">
		<form:hidden path = "reply" />
		<form:hidden path = "momentOfReply" />
	</security:authorize>
	
	
	<form:label path = "moment">
		<spring:message code = "note.moment" />:
	</form:label>
	<form:input  disabled="${enabled}" path = "moment" placeholder = "yyyy/MM/dd" />
	<form:errors cssClass = "error" path = "moment" />
	<br />
	
	<form:label path = "remark">
		<spring:message code = "note.remark" />:
	</form:label>
	<form:input disabled="${enabled}"  path = "remark" />
	<form:errors cssClass = "error" path = "remark" />
	<br />
	
	<security:authorize access="hasRole('MANAGER')">
	
		<form:label path = "reply">
			<spring:message code = "note.reply" />:
		</form:label>
		<form:input path = "reply" />
		<form:errors cssClass = "error" path = "reply" />
		<br />
		
		<form:label path = "momentOfReply">
			<spring:message code = "note.momentOfReply" />:
		</form:label>
		<form:input path = "momentOfReply" />
		<form:errors cssClass = "error" path = "momentOfReply" />
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
	
	<a href = "note/${role}/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "note.cancel" />" />
	</a>

</form:form>
