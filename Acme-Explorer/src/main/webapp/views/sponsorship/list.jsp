

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="sponsorships" id="row" requestURI="${requestUri}"
	pagesize="${pagesize}" class="displaytag">

	<spring:message code="sponsorship.bannerUrl" var="bannerUrl" />
	<display:column title="${bannerUrl}">
		<a href="${row.bannerUrl}">${row.bannerUrl}</a>
	</display:column>
	<spring:message code="sponsorship.additionalInfoLink"
		var="additionalInfoLink" />
	<display:column title="${additionalInfoLink}">
		<a href="${row.additionalInfoLink}">${row.additionalInfoLink}</a>
	</display:column>
	<spring:message code="sponsorship.trip.title" var="trip" />
	<display:column property="trip.title" title="${trip}" sortable="false" />

	<security:authorize access="hasRole('SPONSOR')">
		<display:column>
			<a href="sponsorship/sponsor/edit.do?sponsorshipId=${row.id}"> <spring:message
					code="sponsorship.edit" />
			</a>
		</display:column>
	</security:authorize>

</display:table>

