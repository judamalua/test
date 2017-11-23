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
	action="trip/manager/edit.do"
	modelAttribute="trip">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="sponsorships"/>
	<form:hidden path="stories"/>
	<form:hidden path="survivalClasses"/>
	<form:hidden path="cancelReason"/>
	<form:hidden path="auditRecors"/>
	<form:hidden path="notes"/>
	<form:hidden path="applications"/>
	
	<form:label path="title">
		<spring:message code="trip.title"/>
	</form:label>
	<form:input path="title"/>
	
	<form:label path="description">
		<spring:message code="trip.description"/>
	</form:label>
	<form:input path="description"/>
	
	<form:label path="price">
		<spring:message code="trip.price"/>
	</form:label>
	<form:input path="price"/>
	
	<form:label path="requirements">
		<spring:message code="trip.requirements"/>
	</form:label>
	<form:input path="requirements"/>
	
	<form:label path="startDate">
		<spring:message code="trip.startDate"/>
	</form:label>
	<form:input path="startDate"/>
	
	<form:label path="endDate">
		<spring:message code="trip.endDate"/>
	</form:label>
	<form:input path="endDate"/>
	
	<form:label path="publicationDate">
		<spring:message code="trip.publicationDate"/>
	</form:label>
	<form:input path="publicationDate"/>
	
	<form:label path="ranger">
		<spring:message code="trip.ranger"/>
	</form:label>
	<form:select path="ranger">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="ranger" items="${rangers}"> <!-- Variable del controlador -->
			<form:option value="${ranger.id}">
				<jstl:out value="${ranger.name}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>
	
	<form:label path="legalText">
		<spring:message code="trip.legalText"/>
	</form:label>
	<form:select path="legalText">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="legalText" items="${legalTexts}"> <!-- Variable del controlador -->
			<form:option value="${legalText.id}">
				<jstl:out value="${legalText.title}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>
	
	<form:label path="tags">
		<spring:message code="trip.tags"/>
	</form:label>
	<form:select multiple="true" path="tags">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="tag" items="${tags}"> <!-- Variable del controlador -->
			<form:option value="${tag.id}">
				<jstl:out value="${tag.name}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>

	<form:label path="category">
		<spring:message code="trip.category"/>
	</form:label>
	<form:select multiple="true" path="category">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="category" items="${categories}"> <!-- Variable del controlador -->
			<form:option value="${category.id}">
				<jstl:out value="${category.name}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>
	
	<form:label path="managers">
		<spring:message code="trip.managers"/>
	</form:label>
	<form:select multiple="true" path="managers">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="manager" items="${managers}"> <!-- Variable del controlador -->
			<form:option value="${manager.id}">
				<jstl:out value="${manager.name}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="trip.save" />">
	
	<jstl:if test="${trip.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="trip.delete" />"
			onclick="return confirm('<spring:message code='trip.confirm.delete' />') " />
	</jstl:if>
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="trip.cancel" />"
		onclick="javascript: window.location.replace('trip/list.do')">
	
	
</form:form>
