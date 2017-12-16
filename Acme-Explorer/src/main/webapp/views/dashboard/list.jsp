<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<h4><spring:message code="dashboard.application.number"/></h4>
<p><spring:message code="dashboard.average"/> : <jstl:out value="${applicationAverage}"></jstl:out></p>

<p><spring:message code="dashboard.minimum"/> : <jstl:out value="${applicationMinimum}"></jstl:out></p>

<p><spring:message code="dashboard.maximum"/> : <jstl:out value="${applicationMaximum}"></jstl:out></p>

<p><spring:message code="dashboard.standardDeviation"/> : <jstl:out value="${applicationStandardDeviation}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.manager"/></h4>
<p><spring:message code="dashboard.average"/> : <jstl:out value="${tripManagerAverage}"></jstl:out></p>

<p><spring:message code="dashboard.minimum"/> : <jstl:out value="${tripManagerMinimum}"></jstl:out></p>

<p><spring:message code="dashboard.maximum"/> : <jstl:out value="${tripManagerMaximum}"></jstl:out></p>

<p><spring:message code="dashboard.standardDeviation"/> : <jstl:out value="${tripManagerStandardDeviation}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.price"/></h4>
<p><spring:message code="dashboard.average"/> : <jstl:out value="${tripPriceAverage}"></jstl:out></p>

<p><spring:message code="dashboard.minimum"/> : <jstl:out value="${tripPriceMinimum}"></jstl:out></p>

<p><spring:message code="dashboard.maximum"/> : <jstl:out value="${tripPriceMaximum}"></jstl:out></p>

<p><spring:message code="dashboard.standardDeviation"/> : <jstl:out value="${tripPriceStandardDeviation}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.ranger"/></h4>
<p><spring:message code="dashboard.average"/> : <jstl:out value="${tripRangerAverage}"></jstl:out></p>

<p><spring:message code="dashboard.minimum"/> : <jstl:out value="${tripRangerMinimum}"></jstl:out></p>

<p><spring:message code="dashboard.maximum"/> : <jstl:out value="${tripRangerMaximum}"></jstl:out></p>

<p><spring:message code="dashboard.standardDeviation"/> : <jstl:out value="${tripRangerStandardDeviation}"></jstl:out></p>

<h4><spring:message code="dashboard.application.ratio.status"/></h4>

<p><spring:message code="dashboard.application.ratio.status.pending"/> : <jstl:out value="${ratioApplicationPending}"></jstl:out></p>

<p><spring:message code="dashboard.application.ratio.status.due"/> : <jstl:out value="${ratioApplicationDue}"></jstl:out></p>

<p><spring:message code="dashboard.application.ratio.status.accepted"/> : <jstl:out value="${ratioApplicationAccepted}"></jstl:out></p>

<p><spring:message code="dashboard.application.ratio.status.cancelled"/> : <jstl:out value="${ratioApplicationCancelled}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.ratio.cancelled"/></h4>

<p> <jstl:out value="${ratioCancelled}"></jstl:out></p>

<p><spring:message code="dashboard.trip.total"/> : <jstl:out value="${numTrips}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.more.applications"/></h4>

<display:table name="tripsMoreApplications">
	<spring:message var="titleApplication" code="trip.title"/>
	<display:column title="${titleApplication}" property="title" />
</display:table>

<h4><spring:message code="dashboard.legalText"/></h4>
<table>
		<tr>
			<th><spring:message code="dashboard.legalText"/></th>
			<th><spring:message code="dashboard.numReferences"/></th>
		</tr>
	<jstl:forEach items="${numberReferencesLegalText}" var="row">
		<tr>
			<td>${row[0]}</td> <td> ${row[1]} </td>
		</tr>
	</jstl:forEach>

</table>

<h4><spring:message code="dashboard.trip.note"/></h4>

<p><spring:message code="dashboard.average"/> : <jstl:out value="${tripNoteAverage}"></jstl:out></p>

<p><spring:message code="dashboard.minimum"/> : <jstl:out value="${tripNoteMinimum}"></jstl:out></p>

<p><spring:message code="dashboard.maximum"/> : <jstl:out value="${tripRangerMaximum}"></jstl:out></p>

<p><spring:message code="dashboard.standardDeviation"/> : <jstl:out value="${tripRangerStandardDeviation}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.auditRecord"/></h4>

<p><spring:message code="dashboard.average"/> : <jstl:out value="${tripAuditRecordAverage}"></jstl:out></p>

<p><spring:message code="dashboard.minimum"/> : <jstl:out value="${tripAuditRecordMinimum}"></jstl:out></p>

<p><spring:message code="dashboard.maximum"/> : <jstl:out value="${tripAuditRecordMaximum}"></jstl:out></p>

<p><spring:message code="dashboard.standardDeviation"/> : <jstl:out value="${tripAuditRecordStandardDeviation}"></jstl:out></p>

<h4><spring:message code="dashboard.trip.ratio.auditRecord"/></h4>

<p> <jstl:out value="${tripAuditRecordRatio}"></jstl:out></p>

<h4><spring:message code="dashboard.ratio.curricula"/></h4>

<p><jstl:out value="${CurriculaRatio}"></jstl:out></p>

<h4><spring:message code="dashboard.ratio.curricula.endorsed"/></h4>

<p><jstl:out value="${EndorsedCurriculaRatio}" /></p>

<h4><spring:message code="dashboard.ratio.suspicious.manager"/></h4>

<p> <jstl:out value="${ratioSuspiciousManager}"></jstl:out></p>

<h4><spring:message code="dashboard.ratio.suspicious.ranger"/></h4>

<p> <jstl:out value="${ratioSuspiciousRanger}"></jstl:out></p>

