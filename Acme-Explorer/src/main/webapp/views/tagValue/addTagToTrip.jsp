<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="tag/manager/addTagToTrip.do" modelAttribute ="tags">
	
	<label for="name">
		<spring:message code="tag.name"/>
	</label>
	<select name="name">
		<jstl:forEach var="tag" items="${tags}">
			<form:option value="${tag.id}" title="${tag.name}"/>
		</jstl:forEach>
	</select>
	
	<label for="tagValue">
		<spring:message code="tag.tagValue"/>
	</label>
	<input type="text" required="required"/>
		
	<input 
		type="submit"
		name="save"
		value="<spring:message code="tag.save" />" />
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="tag.cancel" />"
		onclick="javascript: relativeRedir('tag/manager/list.do');" />

</form:form>
