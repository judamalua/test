<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="configuration/admin/edit.do"
	modelAttribute="configuration">

	<form:hidden path="id" />
	<form:hidden path="version" />

	<form:label path="vat">
		<spring:message code="configuration.vat"/>
	</form:label>
	<form:input path="vat"/>
	<form:errors cssClass="error" path="vat"/>
	
	<form:label path="searchTimeout">
		<spring:message code="configuration.searchTimeout"/>
	</form:label>
	<form:input path="searchTimeout"/> <spring:message code="configuration.hour"/>
	<form:errors cssClass="error" path="searchTimeout"/> 
	
	<spring:message code="configuration.spamWords.placeholder" var="spamWordsPlaceholder"/>
	<form:label path="spamWords">
		<spring:message code="configuration.spamWords"/>
	</form:label>
	<form:textarea path="spamWords" placeholder="${spamWordsPlaceholder}" />
	<form:errors cssClass="error" path="spamWords"/>
	
	<form:label path="bannerUrl">
		<spring:message code="configuration.bannerUrl"/>
	</form:label>
	<form:textarea path="bannerUrl"/>
	<form:errors cssClass="error" path="bannerUrl"/>
	
	<form:label path="welcomeMessageEng">
		<spring:message code="configuration.welcomeMessageEng"/>
	</form:label>
	<form:textarea path="welcomeMessageEng"/>
	<form:errors cssClass="error" path="welcomeMessageEng"/>
	
	<form:label path="welcomeMessageEsp">
		<spring:message code="configuration.welcomeMessageEsp"/>
	</form:label>
	<form:textarea path="welcomeMessageEsp"/>
	<form:errors cssClass="error" path="welcomeMessageEsp"/>
	
	<form:label path="defaultPhoneCountryCode">
		<spring:message code="configuration.defaultPhoneCountryCode"/>
	</form:label>
	<form:input path="defaultPhoneCountryCode"/>
	<form:errors cssClass="error" path="defaultPhoneCountryCode"/>
	
	<spring:message code="configuration.notEmpty" var="inputError"/>
	<form:label path="maxResults">
		<spring:message code="configuration.maxResults"/>
	</form:label>
	<form:input path="maxResults" required="required"
	oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}"/>
	<form:errors cssClass="error" path="maxResults"/>
	<br/>
	<br/>
	
	<input type="submit" name="save"
		value="<spring:message code="configuration.save" />">

	<input type="button" name="cancel"
		value="<spring:message code="configuration.cancel" />"
		onclick="javascript: relativeRedir('configuration/admin/list.do')">


</form:form>
