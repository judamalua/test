<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<script type="text/javascript">
	
</script>

<form
	action="trip/manager/addSurvivalClasses.do" method="POST">
	<input type="hidden" name="tripId" value="${trip}">
	
	<select multiple name="selectedSurvivalClasses">
	<jstl:forEach items="${survivalClasses}" var="survivalClass">
	<option  value="${survivalClass.id}">
	<jstl:out value="${survivalClass.title}"/> 
	</option>
	</jstl:forEach>
	</select>
	
	<input 
		type="submit"
		name="save"
		value="<spring:message code="trip.save" />">
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="trip.cancel" />"
		onclick="javascript: relativeRedir('trip/manager/list.do')">
	
	
</form>
