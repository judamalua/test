
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action = "survivalClass/manager/edit.do" modelAttribute = "survivalClass">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "explorers"/>

	
	
	<form:label path = "title">
		<spring:message code = "survivalclass.title" />:
	</form:label>
	<form:input path = "title" />
	<form:errors cssClass = "error" path = "title" />
	<br />
	
	<form:label path = "description">
		<spring:message code = "survivalclass.description" />:
	</form:label>
	<form:textarea path = "description" />
	<form:errors cssClass = "error" path = "description" />
	<br />
	
	<form:label path = "organisationMoment">
	<spring:message code = "survivalclass.organisationMoment" />:
	</form:label>
	<form:input path="organisationMoment" id="organisationMoment" placeholder="dd/MM/yyyy hh:mm" />
	<form:errors cssClass = "error" path = "organisationMoment" />
	<br />
	
	<form:label path = "location.name">
		<spring:message code = "survivalclass.location.name" />:
	</form:label>
	<form:input path = "location.name" />
	<form:errors cssClass = "error" path = "location.name" />
	<br />
	
	<form:label path = "location.gpsCoordinates">
		<spring:message code = "survivalclass.location.gpsCoordinates" />:
	</form:label>
	<form:input path = "location.gpsCoordinates" placeholder=" [-]xxx.xx,[-]xxx.xx" />
	<form:errors cssClass = "error" path = "location.gpsCoordinates" />
	<br />
	
	
	<input type = "submit" name = "save" value = "<spring:message code = "survivalclass.save"/>" />
	<jstl:if test="${survivalClass.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="survivalclass.delete" />"
			onclick="return confirm('<spring:message code='survivalclass.confirm.delete' />') " />
	</jstl:if>

	<a href = "survivalClass/manager/list-managed.do">
	<input type = "button" name = "cancel" value = "<spring:message code = "survivalclass.cancel" />" >
	</a>

</form:form>
