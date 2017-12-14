<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form
	action="category/admin/edit.do"
	modelAttribute ="category">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="categories"/>
	<%-- <form:hidden path="trips"/> --%>
	
	
	<form:label path="name">
		<spring:message code="category.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<form:label path="fatherCategory">
		<spring:message code="category.fatherCategory"/>
	</form:label>
	<form:select path="fatherCategory">
	
		<form:option value="${rootCategory.id}"> <!-- Variable pasada por el modelo -->
			<spring:message code = "category.root"/>
		</form:option>
		<jstl:forEach var="categoryIf" items="${categories}"> <!-- Variable del controlador -->
		<jstl:if test="${categoryIf.name != \"CATEGORY\"}">
			<form:option value="${categoryIf.id}">
				<jstl:out value="${categoryIf.name}"/> 	
			</form:option>
		</jstl:if>
		</jstl:forEach>
		
	</form:select>
	<form:errors cssClass="error" path="fatherCategory"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="category.save" />" />
		
	<jstl:if test="${category.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="category.delete" />"
			onclick="return confirm('<spring:message code='category.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="category.cancel" />"
		onclick="javascript: relativeRedir('category/list.do');" />

</form:form>
