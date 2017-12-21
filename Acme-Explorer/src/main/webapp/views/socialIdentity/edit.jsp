<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="socialIdentity/edit.do" modelAttribute ="socialIdentity">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	
	<form:label path="nick">
		<spring:message code="socialIdentity.nick"/>
	</form:label>
	<form:input path="nick"/>
	<form:errors cssClass="error" path="nick"/>
	<br/>
	
	<form:label path="name">
		<spring:message code="socialIdentity.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<form:label path="profileLink">
		<spring:message code="socialIdentity.profileLink"/>
	</form:label>
	<form:input path="profileLink" placeholder="https://www.facebook.com"/>
	<form:errors cssClass="error" path="profileLink"/>
	<br/>
	
	<form:label path="photoUrl">
		<spring:message code="socialIdentity.photoUrl"/>
	</form:label>
	<form:input path="photoUrl" placeholder="https://www.tumblr.com"/>
	<form:errors cssClass="error" path="photoUrl"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="socialIdentity.save" />"/>
		
	<jstl:if test="${socialIdentity.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="socialIdentity.delete" />"
			onclick="return confirm('<spring:message code='socialIdentity.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="socialIdentity.cancel" />"
		onclick="javascript: relativeRedir('socialIdentity/list.do');" />

</form:form>
