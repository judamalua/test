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
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action = "auditRecord/auditor/edit.do" modelAttribute = "auditRecord">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "auditor" />
	<form:hidden path = "trip" />
	
	<form:label path = "momentWhenCarriedOut">
		<spring:message code = "auditRecord.moment" />:
	</form:label>
	<form:input path = "momentWhenCarriedOut" placeholder = "yyyy/MM/dd" />
	<form:errors cssClass = "error" path = "momentWhenCarriedOut" />
	<br />
	
	<form:label path = "title">
		<spring:message code = "auditRecord.title" />:
	</form:label>
	<form:input path = "title" />
	<form:errors cssClass = "error" path = "title" />
	<br />
	
	<form:label path = "description">
		<spring:message code = "auditRecord.description" />:
	</form:label>
	<form:input path = "description" />
	<form:errors cssClass = "error" path = "description" />
	<br />
	
	<form:label path = "isFinalMode">
		<spring:message code = "auditRecord.isFinalMode" />:
	</form:label>
	<form:checkbox path = "isFinalMode" value = "<spring:message code =  auditRecord.yes/>" />
	<form:errors cssClass = "error" path = "isFinalMode" />
	<br />
	
	<display:table name = "auditRecord" id = "row" 
	requestURI = "auditRecord/edit.do" pagesize = "10" class = "displaytag">
	
	<spring:message code = "auditRecord.attachments" var = "attachmentHeader"/>
	<display:column property = "attachments" title = "${attachmentsHeader}" sortable = "false">
				<ul>
				<jstl:forEach var = "attachment" items = "${row.attachments}" varStatus = "loop">
				
					<li>
					${auditRecord.attachment}
					</li>
					
				</jstl:forEach>
				</ul>
		<display:column>
			<a href = "auditRecord/auditor/deleteAttachment.do?auditRecordId=${row.id}&index=${loop.index}">
				<spring:message code = "auditRecord.delete"/>
			</a>
		</display:column>
	</display:column>
	
	</display:table>
	
	<a href = "auditRecord/auditor/addAttachment.do?auditRecordId=${auditRecord.id}">
		<spring:message code = "auditRecord.add"/>
	</a>
	
	<jstl:if test="${auditRecord.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="auditRecord.delete" />"
			onclick="return confirm('<spring:message code='auditRecord.confirm.delete' />') " />
	</jstl:if>

	<a href = "/Acme-Explorer/auditRecord/list.do">
	<input type = "button" name = "cancel" value = "<spring:message code = "auditRecord.cancel" />" >
	</a>

</form:form>
