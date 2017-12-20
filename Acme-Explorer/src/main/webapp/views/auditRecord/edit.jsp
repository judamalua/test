<%--
 * action-2.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action = "auditRecord/auditor/edit.do" modelAttribute = "auditRecord">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "auditor" />
	<form:hidden path = "trip" />
	<form:hidden path = "momentWhenCarriedOut" />
	
<%-- 	<form:label path = "momentWhenCarriedOut">
		<spring:message code = "auditRecord.moment" />:
	</form:label>
	<form:input path = "momentWhenCarriedOut" disabled = "true" placeholder = "dd/MM/yyyy hh:mm" />
	<form:errors cssClass = "error" path = "momentWhenCarriedOut" />
	<br /> --%>
	
	<form:label path = "title">
		<spring:message code = "auditRecord.title" />:
	</form:label>
	<form:input path = "title" />
	<form:errors cssClass = "error" path = "title" />
	<br />
	
	<form:label path = "description">
		<spring:message code = "auditRecord.description" />:
	</form:label>
	<form:textarea path = "description" />
	<form:errors cssClass = "error" path = "description" />
	<br />

	<form:label path = "attachments">
		<spring:message code="auditRecord.attachments"/> <spring:message code="auditRecord.comma.message"/>
	</form:label>
		<form:textarea path = "attachments" />
	<form:errors cssClass = "error" path = "attachments" />
	<br />

	<%-- <table class="displayTag">
		<tr><spring:message code="auditRecord.attachments"/></tr>
		
		<jstl:forEach var="attachment" varStatus="loop" items="${auditRecord.attachments}">
			<tr><jstl:out value="${attachment}"/></tr>
			<tr>
				<a href = "auditRecord/auditor/deleteAttachment.do?auditRecordId=${auditRecord.id}&index=${loop.index}" >
					<spring:message code = "auditRecord.delete"/>
				</a>
			</tr>
		</jstl:forEach>
	
	</table>
	
	<a href = "auditRecord/auditor/addAttachment.do?auditRecordId=${auditRecord.id}">
		<button>
			<spring:message code = "auditRecord.add"/>
		</button>
	</a>
	<form:input path="attachment" />
	<form:errors cssClass = "error" path = "attachment" />
	<br /> --%>
	
	
	<input 
		type="submit"
		name="saveDraft"
		value="<spring:message code="auditRecord.save.draft" />" />
		
	<input 
		type="submit"
		name="saveFinal"
		value="<spring:message code="auditRecord.save.final" />" />

	<jstl:if test="${auditRecord.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="auditRecord.delete" />"
			onclick="return confirm('<spring:message code='auditRecord.confirm.delete' />') " />
	</jstl:if>

	<a href = "auditRecord/auditor/list.do">
		<input type = "button" name = "cancel" value = "<spring:message code = "auditRecord.cancel" />" >
	</a>

</form:form>
