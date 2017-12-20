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

<form:form id = "form" action = "curriculum/ranger/edit.do" modelAttribute = "curriculum">
	
	<form:hidden path = "ticker" />
	<form:hidden path = "ranger" />
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "educationRecords" />
	<form:hidden path = "endorserRecords" />
	<form:hidden path = "miscellaneousRecords" />
	<form:hidden path = "professionalRecords" />

	<form:label path = "personalRecord.nameOfCandidate">
		<spring:message code = "curriculum.personalRecord.nameOfCandidate" />:
	</form:label>
	<form:input path = "personalRecord.nameOfCandidate" />
	<form:errors cssClass = "error" path = "personalRecord.nameOfCandidate" />
	<br />
	
	<form:label path = "personalRecord.photo">
		<spring:message code = "curriculum.personalRecord.photo" />:
	</form:label>
	<form:input path = "personalRecord.photo" />
	<form:errors cssClass = "error" path = "personalRecord.photo" />
	<br />
	
	<form:label path = "personalRecord.email">
		<spring:message code = "curriculum.personalRecord.email" />:
	</form:label>
	<form:input path = "personalRecord.email" />
	<form:errors cssClass = "error" path = "personalRecord.email" />
	<br />
	
	<form:label path = "personalRecord.phoneNumber">
		<spring:message code = "curriculum.personalRecord.phoneNumber" />:
	</form:label>
	<form:input id = "phoneNumber" path = "personalRecord.phoneNumber" />
	<form:errors cssClass = "error" path = "personalRecord.phoneNumber" />
	<br />
	
	<form:label path = "personalRecord.linkedInProfileURL">
		<spring:message code = "curriculum.personalRecord.linkedInProfileURL" />:
	</form:label>
	<form:input path = "personalRecord.linkedInProfileURL" />
	<form:errors cssClass = "error" path = "personalRecord.linkedInProfileURL" />
	<br />
	
	
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="curriculum.save" />" 
		onclick = "return validate('<spring:message code = "curriculum.confirm.phone"/>')"/>


	<a href = "curriculum/ranger/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "curriculum.cancel" />" >
	</a>

</form:form>
