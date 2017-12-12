

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
	
	
	<form:label path = "bannerUrl">
		<spring:message code = "note.bannerURL" />:
	</form:label>
	<form:input path = "bannerUrl" placeholder = "https://www.example.com" />
	<form:errors cssClass = "error" path = "bannerURL" />
	<br />
	
	<form:label path = "additionalInfo">
		<spring:message code = "note.additionalInfo" />:
	</form:label>
	<form:input path = "additionalInfo" placeholder = "https://www.example.com" />
	<form:errors cssClass = "error" path = "additionalInfo" />
	<br />
	
	<form:select path="trip">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="trip" items="${trips}">
			<form:option value="${trip.id}">
				<jstl:out value="${trip.title}"/> 
			</form:option>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass = "error" path = "trip" />
	<br />
	
	<h2><spring:message code="sponsorship.creditCard.info"/></h2>
	<jstl:if test="${sporsonship.id==0}">
		<form:label path = "creditcard.holdername">
			<spring:message code = "sponsorship.creditcard.holdername" />:
		</form:label>
		<form:input path = "creditcard.holdername" />
		<form:errors cssClass = "error" path = "creditcard.holdername" />
		<br />
		
		<form:label path = "creditcard.brandname">
			<spring:message code = "sponsorship.creditcard.brandname" />:
		</form:label>
		<form:input path = "creditcard.brandname" />
		<form:errors cssClass = "error" path = "creditcard.brandname" />
		<br />
		
		<form:label path = "creditcard.number">
			<spring:message code = "sponsorship.creditcard.number" />:
		</form:label>
		<form:input  type="number" path = "creditcard.number" placeholder = "xxxxxxxxxxxxxxxx" />
		<form:errors cssClass = "error" path = "creditcard.number" />
		<br />
		
		<form:label path = "creditcard.expirationMonth">
			<spring:message code = "sponsorship.creditcard.expirationMonth" />:
		</form:label>
		<form:input type="number" path = "creditcard.expirationMonth"  placeholder="xx" />
		<form:errors cssClass = "error" path = "creditcard.expirationMonth" />
		<br />
		
		<form:label path = "creditcard.expirationYear">
			<spring:message code = "sponsorship.creditcard.expirationYear" />:
		</form:label>
		<form:input  type="number" path = "creditcard.expirationYear" placeholder="xx" />
		<form:errors cssClass = "error" path = "creditcard.expirationYear" />
		<br />
		
		<form:label path = "creditcard.CVV">
			<spring:message code = "sponsorship.creditcard.CVV" />:
		</form:label>
		<form:input  type="number" path = "creditcard.CVV" placeholder="xxx"/>
		<form:errors cssClass = "error" path = "creditcard.CVV" />
		<br />
	</jstl:if>
	
	<input type = "submit" name = "save" value = "<spring:message code = "sponsorship.save"/>" />
	<jstl:if test="${sponsorship.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="sponsorship.delete" />"
			onclick="return confirm('<spring:message code='sponsorship.confirm.delete' />') " />
	</jstl:if>

	<a href = "/Acme-Explorer/sponsorship/list.do">
	<input type = "button" name = "cancel" value = "<spring:message code = "sponsorship.cancel" />" >
	</a>

</form:form>
