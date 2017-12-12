<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form
	action="messageFolder/edit.do"
	modelAttribute ="messageFolder">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isDefault"/>
	<form:hidden path="messageFolderChildren"/>
	<form:hidden path="messages"/>
	
	
	<form:label path="name">
		<spring:message code="messageFolder.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<form:label path="messageFolderFather">
		<spring:message code="messageFolder.messageFolderFather"/>
	</form:label>
	<form:select path="messageFolderFather">
	
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="messageFolder" items="${messageFolders}"> <!-- Variable del controlador -->
			<form:option value="${messageFolder.id}">
				<jstl:out value="${messageFolder.name}"/> 
			</form:option>
		</jstl:forEach>
		
	</form:select>
	<form:errors cssClass="error" path="messageFolderFather"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="messageFolder.save" />" />
		
	<jstl:if test="${messageFolder.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="messageFolder.delete" />"
			onclick="return confirm('<spring:message code='messageFolder.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="messageFolder.cancel" />"
		onclick="javascript: relativeRedir('messageFolder/list.do');" />

</form:form>
