<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form id = "form" action="contact/explorer/edit.do" modelAttribute ="contact">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	
	<form:label path="name">
		<spring:message code="contact.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<form:label path="email">
		<spring:message code="contact.email"/>
	</form:label>
	<form:input id = "email" path="email"/>
	<form:errors cssClass="error" path="email"/>
	<br/>
	
	<form:label path="phoneNumber">
		<spring:message code="contact.phoneNumber"/>
	</form:label>
	<form:input id = "phoneNumber" path="phoneNumber"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="contact.save" />" 
		onclick = "return validate('<spring:message code = "contact.confirm.phone"/>')"/>
		
	<jstl:if test="${contact.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="contact.delete" />"
			onclick="return confirm('<spring:message code='contact.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="contact.cancel" />"
		onclick="javascript: relativeRedir('contact/explorer/list.do');" />

</form:form>

<jstl:if test="${error}">
<div class = "message">
	<spring:message code = "contact.email.phone.alert"/>
</div>
</jstl:if>
