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

<form:form
	action="application/explorer/edit.do"
	modelAttribute="application">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="status"/>
	<form:hidden path="creditCard"/>
	<form:hidden path="rejection"/>
	
	<form:label path="date">
		<spring:message code="application.date"/>
	</form:label>
	<form:input path="date"/>
	<form:errors cssClass="error" path="date"/>
	<br/>
	
	<form:label path="commentaries">
		<spring:message code="application.commentaries"/>
	</form:label>
	<form:input path="commentaries"/>
	<form:errors cssClass="error" path="commentaries"/>
	<br/>
	
	<form:label path="trip">
		<spring:message code="application.trip"/>
	</form:label>
	<form:select path = "trip" >
		<form:option label="-------" value="0"/>
		<form:options itemLabel="title" itemValue="id" items="${trips}"/> <!-- Añadir variable al controlador -->
	</form:select>
	<form:errors cssClass="error" path="trip"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="application.save" />">
	
	<jstl:if test="${application.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="application.delete" />"
			onclick="return confirm('<spring:message code='application.confirm.delete' />') " />
	</jstl:if>
	
	<jstl:if test="${application.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="application.delete" />"
			onclick="return confirm('<spring:message code='application.confirm.delete' />') " />
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="application.cancel" />"
		onclick="javascript: window.location.replace('application/list.do')">
	
	
</form:form>
