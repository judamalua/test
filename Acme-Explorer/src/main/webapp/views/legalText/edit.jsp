<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="legalText/admin/edit.do" modelAttribute ="legalText">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="registrationDate"/>
	
	
	<form:label path="title">
		<spring:message code="legalText.title"/>
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="body">
		<spring:message code="legalText.body"/>
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body"/>
	<br/>
	
	<spring:message code="legalText.applicableLaws.placeholder" var="applicableLawsPlaceholder"/>
	<form:label path="applicableLaws">
		<spring:message code="legalText.applicableLaws"/> <spring:message code="legalText.applicableLaws.message" />
	</form:label>
	<form:textarea path="applicableLaws" placeholder="${applicableLawsPlaceholder}"/>
	<form:errors cssClass="error" path="applicableLaws"/>
	<br/>
	
	<input 
		type="submit"
		name="saveDraft"
		value="<spring:message code="legalText.save.draft" />" />
		
	<input 
		type="submit"
		name="saveFinal"
		value="<spring:message code="legalText.save.final" />" />
		
	<jstl:if test="${legalText.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="legalText.delete" />"
			onclick="return confirm('<spring:message code='legalText.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="legalText.cancel" />"
		onclick="javascript: relativeRedir('legalText/admin/list.do');" />

</form:form>
