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

<form:form action = "professionalRecord/ranger/edit.do" modelAttribute = "professionalRecord">
	
	<form:hidden path = "curriculum" />
	
	<form:label path = "companyName">
		<spring:message code = "professionalRecord.companyName" />:
	</form:label>
	<form:input path = "companyName"  />
	<form:errors cssClass = "error" path = "companyName" />
	<br />
	
	<form:label path = "workingPerdiodStart">
		<spring:message code = "professionalRecord.workingPerdiodStart" />:
	</form:label>
	<form:input path = "workingPerdiodStart" placeholder = "dd/MM/yyyy hh:mm" />
	<form:errors cssClass = "error" path = "workingPerdiodStart" />
	<br />
	
	<form:label path = "workingPerdiodEnd">
		<spring:message code = "professionalRecord.workingPerdiodEnd" />:
	</form:label>
	<form:input path = "workingPerdiodEnd" placeholder = "dd/MM/yyyy hh:mm" />
	<form:errors cssClass = "error" path = "workingPerdiodEnd" />
	<br />
	
	
	<form:label path = "role">
		<spring:message code = "professionalRecord.role" />:
	</form:label>
	<form:input path = "role" />
	<form:errors cssClass = "error" path = "role" />
	<br />
	
	<form:label path = "phoneNumber">
		<spring:message code = "professionalRecord.phoneNumber" />:
	</form:label>
	<form:input path = "phoneNumber" />
	<form:errors cssClass = "error" path = "phoneNumber" />
	<br />
	
	
	<form:label path = "linkedInProfileURL">
		<spring:message code = "professionalRecord.linkedInProfileURL" />:
	</form:label>
	<form:input path = "linkedInProfileURL" />
	<form:errors cssClass = "error" path = "linkedInProfileURL" />
	<br />

	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="professionalRecord.save" />" />
		

	<jstl:if test="${auditRecord.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="professionalRecord.delete" />"
			/>
	</jstl:if>

	<a href = "curriculum/ranger/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "professionalRecord.cancel" />" >
	</a>

</form:form>
