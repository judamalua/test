

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action = "sponsorship/sponsor/edit.do" modelAttribute = "sponsorship">
	
	<form:hidden path = "id" />
	<form:hidden path = "version" />
	<form:hidden path = "sponsor" />
	<form:hidden path = "trip" />
	
	
	<form:label path = "bannerUrl">
		<spring:message code = "sponsorship.bannerUrl" />:
	</form:label>
	<form:input path = "bannerUrl" placeholder = "https://www.example.com" />
	<form:errors cssClass = "error" path = "bannerUrl" />
	<br />
	
	<form:label path = "additionalInfoLink">
		<spring:message code = "sponsorship.additionalInfoLink" />:
	</form:label>
	<form:input path = "additionalInfoLink" placeholder = "https://www.example.com" />
	<form:errors cssClass = "error" path = "additionalInfoLink" />
	<br />
	
	<input type = "submit" name = "save" value = "<spring:message code = "sponsorship.save"/>" />
	<jstl:if test="${sponsorship.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="sponsorship.delete" />"
			onclick="return confirm('<spring:message code='sponsorship.confirm.delete' />') " />
	</jstl:if>

	<a href = "sponsorship/sponsor/list.do">
	<input type = "button" name = "cancel" value = "<spring:message code = "sponsorship.cancel" />" >
	</a>

</form:form>
