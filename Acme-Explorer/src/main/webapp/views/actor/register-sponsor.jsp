<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form id = "form" action="actor/admin/register-sponsor.do" modelAttribute="actor">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isBanned"/>
	<form:hidden path="suspicious"/>
	<form:hidden path="userAccount.authorities" value="${authority}" />
	<form:hidden path="messageFolders"/>
	<form:hidden path="socialIdentities"/>
	
	<form:hidden path="sponsorships" />
	
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
	
	<form:label path="email">
		<spring:message code="actor.email"/>
	</form:label>
	<form:input path="email"/>
	<form:errors cssClass="error" path="email"/>
	<br/>
	
	<form:label path="address">
		<spring:message code="actor.address"/>
	</form:label>
	<form:input path="address"/>
	<form:errors cssClass="error" path="address"/>
	<br/>
	
	<form:label path="phoneNumber">
		<spring:message code="actor.phoneNumber"/>
	</form:label>
	<form:input id = "phoneNumber" path="phoneNumber"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	<br/>
	
	<form:label path="userAccount.username">
		<spring:message code="actor.username"/>
	</form:label>
	<form:input path="userAccount.username"/>
	<form:errors cssClass="error" path="userAccount.username"/>
	<br/>
	
	<form:label path="userAccount.password">
		<spring:message code="actor.password"/>
	</form:label>
	<form:password path="userAccount.password"/>
	<form:errors cssClass="error" path="userAccount.password"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="actor.save.register" />"
		onclick = "return validate('<spring:message code = "actor.confirm.phone"/>')">
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="actor.cancel" />"
		onclick ="javascript: relativeRedir('welcome/index.do');">
	
</form:form>
