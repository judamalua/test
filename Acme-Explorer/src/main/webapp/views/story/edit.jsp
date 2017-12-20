<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form
	action="story/explorer/edit.do"
	modelAttribute ="story">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="hidden" name="trip" id="trip" value="${trip}"/>
	
	
	<form:label path="title">
		<spring:message code="story.title"/>
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="pieceOfText">
		<spring:message code="story.pieceOfText"/>
	</form:label>
	<form:textarea path="pieceOfText"/>
	<form:errors cssClass="error" path="pieceOfText"/>
	<br/>
	
	<form:label path="attachments">
		<spring:message code="story.attachments"/> <spring:message code="auditRecord.comma.message"/>
	</form:label>
	<form:textarea path="attachments" placeholder="https://www.google.com, http://www.facebook.com"/>
	<form:errors cssClass="error" path="attachments"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="story.save" />" />
		
	<jstl:if test="${story.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="story.delete" />"
			onclick="return confirm('<spring:message code='story.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="story.cancel" />"
		onclick="javascript: relativeRedir('/trip/detailed-trip.do?tripId=${trip}&anonymous=false');" />

</form:form>
