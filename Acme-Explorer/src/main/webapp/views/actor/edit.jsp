<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form id="form" action="${requestUri}" modelAttribute ="actor">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isBanned"/>
	<form:hidden path="suspicious"/>
	<form:hidden path="userAccount"/>
	<form:hidden path="messageFolders"/>
	<form:hidden path="socialIdentities"/>
	
	<security:authorize access="hasRole('RANGER')">
		<form:hidden path="curriculum"/>
		<form:hidden path="trips"/>
	</security:authorize>
	
	<security:authorize access="hasRole('SPONSOR')">
		<form:hidden path="sponsorships"/>
	</security:authorize>
	
	<security:authorize access="hasRole('AUDITOR')">
		<form:hidden path="auditRecords"/>
		<form:hidden path="notes"/>
	</security:authorize>
	
	<security:authorize access="hasRole('MANAGER')">
		<form:hidden path="repliedNotes"/>
		<form:hidden path="trips"/>
		<form:hidden path="survivalClasses"/>
		<form:hidden path="rejections"/>
	</security:authorize>
	
	<security:authorize access="hasRole('EXPLORER')">
		<form:hidden path="applications"/>
		<form:hidden path="stories"/>
		<form:hidden path="survivalClasses"/>
		<form:hidden path="contacts"/>
		<form:hidden path="searches"/>
	</security:authorize>
	
	<form:label path="name">
		<spring:message code="actor.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<form:label path="surname">
		<spring:message code="actor.surname"/>
	</form:label>
	<form:input path="surname"/>
	<form:errors cssClass="error" path="surname"/>
	<br/>
	
	<spring:message code="actor.email.placeholder" var="emailPlaceholder"/>
	<form:label path="email">
		<spring:message code="actor.email"/>
	</form:label>
	<form:input path="email" placeholder = "${emailPlaceholder}"/>
	<form:errors cssClass="error" path="email"/>
	<br/>
	
	<spring:message code="actor.address.placeholder" var="addressPlaceholder"/>
	<form:label path="address">
		<spring:message code="actor.address"/>
	</form:label>
	<form:input path="address" placeholder = "${addressPlaceholder}"/>
	<form:errors cssClass="error" path="address"/>
	<br/>
	
	<form:label path="phoneNumber">
		<spring:message code="actor.phoneNumber"/>
	</form:label>
	<form:input id = "phoneNumber" name="phoneNumber" path="phoneNumber" placeholder = "xxxxxxxxx"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="actor.save.edit" />"
		onclick = "return validate('<spring:message code = "actor.confirm.phone"/>')">
	
</form:form>

<button
		name="cancel"
		onclick="javascript: relativeRedir('/welcome/index.do')">
		<spring:message code="actor.cancel" /></button>
