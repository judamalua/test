<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="tag/admin/edit.do" modelAttribute ="tag">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	
	
	<form:label path="name">
		<spring:message code="tag.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="tag.save" />" />
		
	<jstl:if test="${tag.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="tag.delete" />"
			onclick="return confirm('<spring:message code='tag.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="tag.cancel" />"
		onclick="javascript: relativeRedir('tag/admin/list.do');" />

</form:form>
