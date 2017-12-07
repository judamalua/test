<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript">


$(document).ready(){
	$("save").click(function(){

		if (!($("phoneNumber").match("/^\+\d{1,3} \(\d{1,3}\) \d{4}\d*$/") && !($("phoneNumber").match("/^\+\d{1,3} \d{4}\d*$/") && !($("phoneNumber").match("/^\d{4}\d*$/"))))){
			confirm(<spring:message code="actor.confirm.phone"/>);
		}

	});

};	
</script>

<form:form
	id = "form"
	action="actor/edit.do"
	modelAttribute ="actor">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="isBanned"/>
	<form:hidden path="suspicious"/>
	<form:hidden path="userAccount"/>
	<form:hidden path="messageFolders"/>
	<form:hidden path="socialIdentities"/>
	
	<form:label path="name">
		<spring:message code="actor.name"/>
	</form:label>
	<form:input path="name"/>
	<form:errors cssClass="error" path="name"/>
	<br/>
	
	<form:label path="surname">
		<spring:message code="actor.surname"/>
	</form:label>
	<form:input path="surname"/>
	<form:errors cssClass="error" path="surname"/>
	<br/>
	
	<form:label path="email">
		<spring:message code="actor.email"/>
	</form:label>
	<form:input path="email" placeholder = "example@domail.com"/>
	<form:errors cssClass="error" path="email"/>
	<br/>
	
	<form:label path="address">
		<spring:message code="actor.address"/>
	</form:label>
	<form:input path="address" placeholder = "False Street 123"/>
	<form:errors cssClass="error" path="address"/>
	<br/>
	
	<form:label path="phoneNumber">
		<spring:message code="actor.phoneNumber"/>
	</form:label>
	<form:input name="phoneNumber" path="phoneNumber" placeholder = "xxxxxxxxx"/>
	<form:errors cssClass="error" path="phoneNumber"/>
	<br/>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="actor.save" />"
		onclick = "return validate('<spring:message code = "actor.confirm.phone"/>')">
	
	<input 
		type="submit"
		name="cancel"
		value="<spring:message code="actor.cancel" />"
		onclick="javascript: window.location.replace('welcome/index.jsp')">

</form:form>
