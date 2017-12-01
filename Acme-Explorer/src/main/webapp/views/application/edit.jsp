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


	<jstl:set value="explorer" var="role"/>
	<security:authorize access="hasRole('MANAGER')">
		<jstl:set value="manager" var="role"/>
	</security:authorize>

<form:form
	action="application/${role}/edit.do"
	modelAttribute="application">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<jstl:if test="${application.id!=0}">
		<form:hidden path="creditCard"/>
	</jstl:if>
	
	
	<security:authorize access="hasRole('EXPLORER')">
		<form:hidden path="status"/>
		<form:hidden path="rejection"/>
	</security:authorize>
	
	<jstl:set value="false" var="disabled"/>
	<security:authorize access="hasRole('MANAGER')">
		<jstl:set value="true" var="disabled"/>
	</security:authorize>
	
	
	<form:label path="date">
		<spring:message code="application.date"/>
	</form:label>
	<form:input disabled="${disabled}" path="date" placeholder = "dd/MM/yyyy hh:mm" />
	<form:errors cssClass="error" path="date"/>
	<br/>
	
	<form:label path="commentaries">
		<spring:message code="application.commentaries"/>
	</form:label>
	<form:input disabled="${disabled}"  path="commentaries"/>
	<form:errors cssClass="error" path="commentaries"/>
	<br/>
	
	<form:label path="trip">
		<spring:message code="application.trip"/>
	</form:label>
	<form:input disabled="true" value="${trip.title}" path = "trip"/><!-- Variable del modelo -->
	<form:errors cssClass="error" path="trip"/>
	<br/>
	
	<security:authorize access = "hasRole('EXPLORER')">
		<h2><spring:message code="application.creditCard.info"/></h2>
	
		<form:label path="holderName">
			<spring:message code="application.holderName"/>
		</form:label>
		<form:input path="holderName"/>
		<form:errors cssClass="error" path="holderName"/>
		<br/>
	
	
		<form:label path="brandName">
			<spring:message code="application.brandName"/>
		</form:label>
		<form:input path="brandName"/>
		<form:errors cssClass="error" path="brandName"/>
		<br/>
		
		<form:label path="number">
			<spring:message code="application.number"/>
		</form:label>
		<form:input path="number" placeholder = "xxxxxxxxxxxxxxxx"/>
		<form:errors cssClass="error" path="number" />
		<br/>
		
		<form:label path="expirationMonth">
			<spring:message code="application.expirationMonth" />
		</form:label>
		<form:input path="expirationMonth"/>
		<form:errors cssClass="error" path="expirationMonth" placeholder = "xx"/>
		<br/>
		
		<form:label path="expirationYear">
			<spring:message code="application.expirationYear"/>
		</form:label>
		<form:input path="expirationYear" placeholder = "xx"/>
		<form:errors cssClass="error" path="expirationYear" />
		<br/>
		
		<form:label path="cvv">
			<spring:message code="application.cvv"/>
		</form:label>
		<form:input path="cvv" placeholder = "xxx"/>
		<form:errors cssClass="error" path="cvv"/>
		<br/>
		
		</security:authorize>
	
	<security:authorize access="hasRole('MANAGER')">
		<form:label path="status">
			<spring:message code="application.status"/>
		</form:label>
		<form:select path = "status" >
			<form:option label="-------" value=""/>
			<form:option label="<spring:messagge code="application.status.accepted"/>"  value="PENDING"/>
			<form:option label="<spring:messagge code="application.status.cancelled"/>"  value="REJECTED"/>
			<form:option label="<spring:messagge code="application.status.due"/>"  		value="DUE"/>
			<form:option label="<spring:messagge code="application.status.rejected"/>"  value="ACCEPTED"/>
			<form:option label="<spring:messagge code="application.status.pending"/>"  value="CANCELLED"/>
		</form:select>
		<form:errors cssClass="error" path="status"/>
		<br/>
		
		
		<form:label path="rejection.reason">
			<spring:message code="application.rejection.reason"/>
		</form:label>
		<form:input path="rejection.reason"/>
		<form:errors cssClass="error" path="rejection"/>
		<br/>
		
	</security:authorize>
	
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
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="application.cancel" />"
		onclick="javascript: window.location.replace('application/list.do')">
	
	
</form:form>
