<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form
	action="stage/manager/edit.do"
	modelAttribute ="stage">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<input type="hidden" name="trip" id="trip" value="${trip}"/>
	
	
	<form:label path="title">
		<spring:message code="stage.title"/>
	</form:label>
	<form:input path="title"/>
	<form:errors cssClass="error" path="title"/>
	<br/>
	
	<form:label path="description">
		<spring:message code="stage.description"/>
	</form:label>
	<form:textarea path="description"/>
	<form:errors cssClass="error" path="description"/>
	<br/>
	
	<form:label path="price">
		<spring:message code="stage.price"/>
	</form:label>
<!-- 	<div class="wrapper"> -->
<%--   <form class="form-inline"> --%>
<!--     <label class="sr-only" for="inlineFormInputGroup">Amount</label> -->
<!--     <div class="input-group mb-2 mr-sm-2 mb-sm-0"> -->
<!--       <div class="input-group-addon currency-symbol">$</div> -->
      <form:input path="price" type="text" placeholder="0.00" /> <!--  class="form-control currency-amount" id="inlineFormInputGroup"--> 
<!--       <div class="input-group-addon currency-addon"> -->

<!--         <select class="currency-selector"> -->
<!--           <option data-symbol="$" data-placeholder="0.00" selected>USD</option> -->
<!--           <option data-symbol="€" data-placeholder="0.00">EUR</option> -->
<!--           <option data-symbol="£" data-placeholder="0.00">GBP</option> -->
<!--           <option data-symbol="¥" data-placeholder="0">JPY</option> -->
<!--           <option data-symbol="$" data-placeholder="0.00">CAD</option> -->
<!--           <option data-symbol="$" data-placeholder="0.00">AUD</option> -->
<!--         </select> -->

<!--       </div> -->
<!--     </div> -->
<%--   </form> --%>
<!-- </div> -->
	<form:errors cssClass="error" path="price"/> &euro;
	<br/>
	
	
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="stage.save" />" />
		
	<jstl:if test="${stage.id!=0}">
		<input 
			type="submit"
			name="delete"
			value="<spring:message code="stage.delete" />"
			onclick="return confirm('<spring:message code='stage.confirm.delete' />') "/>
	</jstl:if>
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="stage.cancel" />"
		onclick="javascript: relativeRedir('/trip/manager/list.do');" />

</form:form>
