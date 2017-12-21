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

<form:form action = "miscellaneousRecord/ranger/edit.do" modelAttribute = "miscellaneousRecord">
	
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	
	<form:label path = "title">
		<spring:message code = "miscellaneousRecord.title" />:
	</form:label>
	<form:input path = "title"  />
	<form:errors cssClass = "error" path = "title" />
	<br />
	
	<form:label path = "attachment">
		<spring:message code = "miscellaneousRecord.attachment" />:
	</form:label>
	<form:input path = "attachment" placeholder = "http://www.test.com" />
	<form:errors cssClass = "error" path = "attachment" />
	<br />
	
	<form:label path = "commentaries">
		<spring:message code = "miscellaneousRecord.commentaries" />:
	</form:label>
	<form:textarea path = "commentaries"  />
	<form:errors cssClass = "error" path = "commentaries" />
	<br />

	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="miscellaneousRecord.save" />" />
		

	<jstl:if test="${miscellaneousRecord.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="miscellaneousRecord.delete" />"
			onclick="return confirm('<spring:message code='miscellaneousRecord.confirm.delete' />')"/>
	</jstl:if>

	<a href = "curriculum/ranger/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "miscellaneousRecord.cancel" />" >
	</a>

</form:form>
