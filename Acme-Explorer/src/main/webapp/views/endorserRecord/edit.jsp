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

<form:form id = "form" action = "endorserRecord/ranger/edit.do" modelAttribute = "endorserRecord">
	
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	
	<form:label path = "fullName">
		<spring:message code = "endorserRecord.fullName" />:
	</form:label>
	<form:input path = "fullName"  />
	<form:errors cssClass = "error" path = "fullName" />
	<br />
	
	<form:label path = "email">
		<spring:message code = "endorserRecord.email" />:
	</form:label>
	<form:input path = "email"  />
	<form:errors cssClass = "error" path = "email" />
	<br />
	
	<form:label path = "phoneNumber">
		<spring:message code = "endorserRecord.phoneNumber" />:
	</form:label>
	<form:input id = "phoneNumber" path = "phoneNumber"  />
	<form:errors cssClass = "error" path = "phoneNumber" />
	<br />
	
	<form:label path = "linkedInProfileURL">
		<spring:message code = "endorserRecord.linkedInProfileURL" />:
	</form:label>
	<form:input path = "linkedInProfileURL"  />
	<form:errors cssClass = "error" path = "linkedInProfileURL" />
	<br />
	
	<form:label path = "commentaries">
		<spring:message code = "endorserRecord.commentaries" />:
	</form:label>
	<form:textarea path = "commentaries"  />
	<form:errors cssClass = "error" path = "commentaries" />
	<br />
	
	
	

	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="endorserRecord.save" />" 
		onclick = "return validate('<spring:message code = "endorserRecord.confirm.phone"/>')"/>
		

	<jstl:if test="${endorserRecord.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="endorserRecord.delete" />"
			onclick="return confirm('<spring:message code='endorserRecord.confirm.delete' />')" />
	</jstl:if>

	<a href = "curriculum/ranger/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "endorserRecord.cancel" />" >
	</a>

</form:form>
