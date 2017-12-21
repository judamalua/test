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
	
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	
	<form:label path = "companyName">
		<spring:message code = "professionalRecord.companyName" />:
	</form:label>
	<form:input path = "companyName"  />
	<form:errors cssClass = "error" path = "companyName" />
	<br />
	
	<form:label path = "workingPeriodStart">
		<spring:message code = "professionalRecord.workingPeriodStart" />:
	</form:label>
	<form:input path = "workingPeriodStart" placeholder = "dd/MM/yyyy HH:mm" />
	<form:errors cssClass = "error" path = "workingPeriodStart" />
	<br />
	
	<form:label path = "workingPeriodEnd">
		<spring:message code = "professionalRecord.workingPeriodEnd" />:
	</form:label>
	<form:input path = "workingPeriodEnd" placeholder = "dd/MM/yyyy HH:mm" />
	<form:errors cssClass = "error" path = "workingPeriodEnd" />
	<br />
	
	
	<form:label path = "role">
		<spring:message code = "professionalRecord.role" />:
	</form:label>
	<form:input path = "role" />
	<form:errors cssClass = "error" path = "role" />
	<br />
	
	<form:label path = "attachment">
		<spring:message code = "professionalRecord.attachment" />:
	</form:label>
	<form:input path = "attachment" placeholder = "http://www.test.com" />
	<form:errors cssClass = "error" path = "attachment" />
	<br />
	
	
	<form:label path = "commentaries">
		<spring:message code = "professionalRecord.commentaries" />:
	</form:label>
	<form:textarea path = "commentaries" />
	<form:errors cssClass = "error" path = "commentaries" />
	<br />

	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="professionalRecord.save" />" />
		

	<jstl:if test="${professionalRecord.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="professionalRecord.delete" />"
			onclick="return confirm('<spring:message code='professionalRecord.confirm.delete' />')"/>
	</jstl:if>

	<a href = "curriculum/ranger/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "professionalRecord.cancel" />" >
	</a>

</form:form>
