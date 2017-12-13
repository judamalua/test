<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form action = "sponsorship/sponsor/addCreditCard.do" method="post">

		<spring:bind path="bannerUrl">
   			<input type="hidden" name="bannerUrl" value="${bannerUrl}"><br />
        </spring:bind>
 		<spring:bind path="additionalInfoLink">
   			<input type="hidden" name="additionalInfoLink" value="${additionalInfoLink}"><br />
        </spring:bind>
        <spring:bind path="tripId">
   			<input type="hidden" name="tripId" value="${tripId}"><br />
        </spring:bind>
        

<form:label path = "creditcard.holderName">
			<spring:message code = "sponsorship.creditcard.holdername" />:
		</form:label>
		<spring:bind path="creditcard.holderName">
   			<input type="text" name="${status.expression}" value="${status.value}"><br />
        </spring:bind>
		<form:errors cssClass = "error" path = "holderName" />
		<br />
		
		<form:label path = "creditcard.brandName">
			<spring:message code = "sponsorship.creditcard.brandname" />:
		</form:label>
		<spring:bind path="creditcard.brandName">
   			<input type="text" name="${status.expression}" value="${status.value}"><br />
        </spring:bind>		
        <form:errors cssClass = "error" path = "creditcard.brandName" />
		<br />
		
		<form:label path = "creditcard.number">
			<spring:message code = "sponsorship.creditcard.number" />:
		</form:label>
		<spring:bind path="creditcard.number">
   			<input type="text" name="${status.expression}" value="${status.value}" placeholder = "xxxxxxxxxxxxxxxx"><br />
        </spring:bind>
		<form:errors cssClass = "error" path = "creditcard.number" />
		<br />
		
		<form:label path = "creditcard.expirationMonth">
			<spring:message code = "sponsorship.creditcard.expirationMonth" />:
		</form:label>
		<spring:bind path="creditcard.expirationMonth">
   			<input type="text" name="${status.expression}" value="${status.value}" placeholder = "xx"><br />
        </spring:bind>
        <form:errors cssClass = "error" path = "creditcard.expirationMonth" />
		<br />
		
		<form:label path = "creditcard.expirationYear">
			<spring:message code = "sponsorship.creditcard.expirationYear" />:
		</form:label>
		<spring:bind path="creditcard.expirationYear">
   			<input type="text" name="${status.expression}" value="${status.value}" placeholder = "xx"><br />
        </spring:bind>		
        <form:errors cssClass = "error" path = "creditcard.expirationYear" />
		<br />
		
		<form:label path = "creditcard.cvv">
			<spring:message code = "sponsorship.creditcard.CVV" />:
		</form:label>
		<spring:bind path="creditcard.cvv">
   			<input type="text" name="${status.expression}" value="${status.value}" placeholder = "xxx"><br />
        </spring:bind>
		<form:errors cssClass = "error" path = "creditcard.cvv" />
		<br />
		<input type = "submit" name = "save" value = "<spring:message code = "sponsorship.save"/>" />
</form>