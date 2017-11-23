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

<form:form action = "sponsorship/sponsor/edit.do" modelAttribute = "sponsorship">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "sponsor" />
	
	
	<form:label path = "bannerURL">
		<spring:message code = "note.bannerURL" />:
	</form:label>
	<form:input path = "bannerURL" placeholder = "https://www.example.com" />
	<form:errors cssClass = "error" path = "bannerURL" />
	<br />
	
	<form:label path = "additionalInfo">
		<spring:message code = "note.additionalInfo" />:
	</form:label>
	<form:input path = "additionalInfo" />
	<form:errors cssClass = "error" path = "additionalInfo" />
	<br />
	
	<form:select path="trip">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="trip" items="${trips}">
			<form:option value="${trip.id}">
				<jstl:out value="${trip.title}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass = "error" path = "trip" />
	<br />
	
	
	<input type = "submit" name = "save" value = "<spring:message code = "sponsorship.save"/>" />
	<jstl:if test="${sponsorship.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="sponsorship.delete" />"
			onclick="return confirm('<spring:message code='sponsorship.confirm.delete' />') " />
	</jstl:if>

	<a href = "/Acme-Explorer/sponsorship/list.do">
	<input type = "button" name = "cancel" value = "<spring:message code = "sponsorship.cancel" />" >
	</a>

</form:form>
