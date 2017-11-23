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

<form:form action = "note/auditor/edit.do" modelAttribute = "note">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "auditor" />
	<form:hidden path = "trip" />
	<form:hidden path = "manager" />
	
	<form:label path = "moment">
		<spring:message code = "note.moment" />:
	</form:label>
	<form:input path = "moment" placeholder = "yyyy/MM/dd" />
	<form:errors cssClass = "error" path = "moment" />
	<br />
	
	<form:label path = "remark">
		<spring:message code = "note.remark" />:
	</form:label>
	<form:input path = "remark" />
	<form:errors cssClass = "error" path = "remark" />
	<br />
	
	<input type = "submit" name = "save" value = "<spring:message code = "note.save"/>" />
	<jstl:if test="${note.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="note.delete" />"
			onclick="return confirm('<spring:message code='note.confirm.delete' />') " />
	</jstl:if>
	
	<a href = "note/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "note.cancel" />" />
	</a>

</form:form>
