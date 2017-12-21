

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
	<jstl:if test="${!(sponsorship.id == 0)}">
	<form:hidden path = "creditCard" />
	</jstl:if>
	
	<spring:message var="inputError" code="sponsorship.input.error"/>
	
	<form:label path = "bannerUrl">
		<spring:message code = "sponsorship.bannerUrl" />:
	</form:label>
	<form:input path = "bannerUrl" placeholder = "https://www.example.com" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
	<form:errors cssClass = "error" path = "bannerUrl" />
	<br />
	
	<form:label path = "additionalInfoLink">
		<spring:message code = "sponsorship.additionalInfoLink" />:
	</form:label>
	<form:input path = "additionalInfoLink" placeholder = "https://www.example.com" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
	<form:errors cssClass = "error" path = "additionalInfoLink" />
	<br />
	
	<jstl:if test="${sponsorship.id == 0}">
	<form:label path = "creditCard.holderName">
			<spring:message code = "sponsorship.creditcard.holdername" />:
		</form:label>
			<form:input path = "creditCard.holderName" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
		<form:errors cssClass = "error" path = "creditCard.holderName" />
		<br />
		
		<form:label path = "creditCard.brandName">
			<spring:message code = "sponsorship.creditcard.brandname" />:
		</form:label>
			<form:input path = "creditCard.brandName" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
        <form:errors cssClass = "error" path = "creditCard.brandName" />
		<br />
		
		<form:label path = "creditCard.number">
			<spring:message code = "sponsorship.creditcard.number" />:
		</form:label>
			<form:input path = "creditCard.number" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
		<form:errors cssClass = "error" path = "creditCard.number" placeholder = "XXXXXXXXXXXXXXXXX"/>
		<br />
		
		<form:label path = "creditCard.expirationMonth">
			<spring:message code = "sponsorship.creditcard.expirationMonth" />:
		</form:label>
			<form:input path = "creditCard.expirationMonth" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
        <form:errors cssClass = "error" path = "creditCard.expirationMonth" placeholder = "XX"/>
		<br />
		
		<form:label path = "creditCard.expirationYear">
			<spring:message code = "sponsorship.creditcard.expirationYear" />:
		</form:label>
			<form:input path = "creditCard.expirationYear" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
        <form:errors cssClass = "error" path = "creditCard.expirationYear" placeholder = "XX"/>
		<br />
		
		<form:label path = "creditCard.cvv">
			<spring:message code = "sponsorship.creditcard.CVV" />:
		</form:label>
			<form:input path = "creditCard.cvv" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
		<form:errors cssClass = "error" path = "creditCard.cvv" placeholder = "XXX"/>
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

	<a href = "sponsorship/sponsor/list.do">
	<input type = "button" name = "cancel" value = "<spring:message code = "sponsorship.cancel" />" >
	</a>

</form:form>
