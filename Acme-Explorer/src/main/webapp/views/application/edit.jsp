<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript">
window.onload = function(){
	$(".creditCard").val('');
};
</script>

<jstl:set value="explorer" var="role" />
<security:authorize access="hasRole('MANAGER')">
	<jstl:set value="manager" var="role" />
</security:authorize>

<spring:message var="inputError" code="application.input.error"/>

<form:form action="application/${role}/edit.do"
	modelAttribute="application">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="trip" />

	<security:authorize access="hasRole('EXPLORER')">
		<form:hidden path="status" />
		<jstl:if test="${application.id==0}">
			<form:hidden path="creditCard" />
			<form:hidden path="date"/>
		</jstl:if>
	</security:authorize>

	<jstl:set value="false" var="disabled" />
	<security:authorize access="hasRole('MANAGER')">
		<jstl:set value="true" var="disabled" />
		<form:hidden path="date" />
		<form:hidden path="trip" />
		<form:hidden path="creditCard" />
		<form:hidden path="status"/>
	</security:authorize>
	<jstl:if test="${application.id!=0}">
		<jstl:set value="true" var="disabled" />
		<form:hidden path="date" />
		<form:hidden path="commentaries" />
	</jstl:if>

	<security:authorize access="hasRole('EXPLORER')">

		<jstl:if test="${application.id==0}">
<%-- 			<form:label path="date"> --%>
<%-- 				<spring:message code="application.date" /> --%>
<%-- 			</form:label> --%>
<%-- 			<form:input disabled="true" path="date" --%>
<%-- 				placeholder="dd/MM/yyyy hh:mm" value=""/> --%>
<%-- 			<form:errors cssClass="error" path="date" /> --%>
<!-- 			<br /> -->

			<form:label path="commentaries">
				<spring:message code="application.commentaries" />
			</form:label>
			<form:textarea disabled="${disabled}" path="commentaries" />
			<form:errors cssClass="error" path="commentaries" />
			<br />
		</jstl:if>

		<jstl:if test="${application.id!=0}">
			<h2>
				<spring:message code="application.creditCard.info" />
			</h2>

			<form:label path="creditCard.holderName">
				<spring:message code="application.holderName" />
			</form:label>
			<form:input path="creditCard.holderName" class="creditCard" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
			<form:errors cssClass="error" path="creditCard.holderName" />
			<br />


			<form:label path="creditCard.brandName">
				<spring:message code="application.brandName" />
			</form:label>
			<form:input path="creditCard.brandName" class="creditCard" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
			<form:errors cssClass="error" path="creditCard.brandName" />
			<br />

			<form:label path="creditCard.number">
				<spring:message code="application.number" />
			</form:label>
			<form:input path="creditCard.number" placeholder="xxxxxxxxxxxxxxxx"
				class="creditCard" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
			<form:errors cssClass="error" path="creditCard.number" />
			<br />

			<form:label path="creditCard.expirationMonth">
				<spring:message code="application.expirationMonth" />
			</form:label>
			<form:input path="creditCard.expirationMonth" class="creditCard" placeholder="mm" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
			<form:errors cssClass="error" path="creditCard.expirationMonth" />
			<br />

			<form:label path="creditCard.expirationYear">
				<spring:message code="application.expirationYear" />
			</form:label>
			<form:input path="creditCard.expirationYear" placeholder="yy"
				class="creditCard" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
			<form:errors cssClass="error" path="creditCard.expirationYear" />
			<br />

			<form:label path="creditCard.cvv">
				<spring:message code="application.cvv" />
			</form:label>
			<form:input path="creditCard.cvv" placeholder="xxx" class="creditCard" required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}" />
			<form:errors cssClass="error" path="creditCard.cvv" />
			<br />
		</jstl:if>
	</security:authorize>

	<spring:message code="application.status.due" var="due" />
	<spring:message code="application.status.rejected" var="rejected" />

	<security:authorize access="hasRole('MANAGER')">


		<form:label path="rejection.reason">
			<spring:message code="application.rejection" />
		</form:label>
		<form:input path="rejection.reason" />
		<form:errors cssClass="error" path="rejection.reason" />
		<br />


	</security:authorize>

	<input type="submit" name="save"
		value="<spring:message code="application.save" />">

	<input type="button" name="cancel"
		value="<spring:message code="application.cancel" />"
		onclick="javascript: relativeRedir('application/${role}/list.do')">


</form:form>
