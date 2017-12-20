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
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action = "personalRecord/ranger/edit.do" modelAttribute = "personalRecord">
	

	<form:hidden path = "id" />
	<form:hidden path = "version" />
	
	<form:label path = "nameOfCandidate">
		<spring:message code = "personalRecord.nameOfCandidate" />:
	</form:label>
	<form:input path = "nameOfCandidate"  />
	<form:errors cssClass = "error" path = "nameOfCandidate" />
	<br />
	
	<form:label path = "email">
		<spring:message code = "personalRecord.email" />:
	</form:label>
	<form:input path = "email"   />
	<form:errors cssClass = "error" path = "email" />
	<br />
	
	<form:label path = "photo">
		<spring:message code = "personalRecord.photo" />:
	</form:label>
	<form:input path = "photo"  />
	<form:errors cssClass = "error" path = "photo" />
	<br />
	
	
	<form:label path = "phoneNumber">
		<spring:message code = "personalRecord.phoneNumber" />:
	</form:label>
	<form:input path = "phoneNumber" />
	<form:errors cssClass = "error" path = "phoneNumber" />
	<br />
	
	
	<form:label path = "linkedInProfileURL">
		<spring:message code = "personalRecord.linkedInProfileURL" />:
	</form:label>
	<form:input path = "linkedInProfileURL" />
	<form:errors cssClass = "error" path = "linkedInProfileURL" />
	<br />

	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="professionalRecord.save" />" 
		/>
		

	<a href = "curriculum/ranger/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "personalRecord.cancel" />" >
	</a>

</form:form>
