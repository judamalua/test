<%@page import="java.util.Date"%>
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
	window.onload = function() {
		var elem = document.getElementsByTagName("tr");
		var results = [];
		for ( var x = 0; x < elem.length; x++) {
			results.push(elem[x].value);
		}
	};
</script>
<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<spring:message code="format.date" var="formatDate" />

<%
	Date month = new Date();
	month.setMonth(now.getMonth()+1);
	request.setAttribute("month", month);
%>

<fmt:formatDate var="monthDate" value="${month}" pattern="yyyy-MM-dd HH:mm" />

<display:table name="applications" id="row" requestURI="${requestUri}"
	pagesize="${pagesize}" class="displayTag">

	<jstl:if test="${row.status==\"PENDING\"}">
		<jstl:set var="classTd" value="pending" />
	</jstl:if>
	<jstl:if test="${row.status==\"DUE\"}">
		<jstl:set var="classTd" value="due" />
	</jstl:if>
	<jstl:if
		test="${row.trip.startDate >= currentDate and row.trip.startDate <= monthDate}">
		<jstl:set var="classTd" value="tripGoingStart" />
	</jstl:if>
	<jstl:if test="${row.status==\"REJECTED\"}">
		<jstl:set var="classTd" value="rejected" />
	</jstl:if>
	<jstl:if test="${row.status==\"ACCEPTED\"}">
		<jstl:set var="classTd" value="accepted" />
	</jstl:if>
	<jstl:if test="${row.status==\"CANCELLED\"}">
		<jstl:set var="classTd" value="cancelled" />
	</jstl:if>


	<spring:message code="application.date" var="date" />

	<display:column property="date" title="${date}" sortable="true"
		format="${formatDate}" class="${classTd}" />

	<spring:message code="application.commentaries" var="commentariesApp" />
	<display:column property="commentaries" title="${commentariesApp}"
		sortable="true" class="${classTd}" />

	<spring:message code="application.status" var="status" />
	<display:column property="status" title="${status}" sortable="true"
		class="${classTd}" />

	<spring:message code="application.rejection" var="rejection" />
	<display:column property="rejection.reason" title="${rejection}"
		sortable="true" class="${classTd}" />

	<spring:message code="application.trip" var="trip" />
	<display:column property="trip.title" title="${trip}" sortable="true"
		class="${classTd}" />

	<jstl:set value="${row.status}" var="status" />
	<jstl:set value="PENDING" var="pending" />
	<jstl:set value="ACCEPTED" var="accepted" />
	<jstl:set value="DUE" var="due" />
	<jstl:set value="REJECTED" var="rejected" />
	<security:authorize access="hasRole('MANAGER')">
		<display:column class="${classTd}">
			<jstl:if test="${status != rejected}">
				<a href="application/manager/edit.do?applicationId=${row.id}">
					<button>
						<spring:message code="application.reject" />
					</button>
				</a>
			</jstl:if>
		</display:column>

		<display:column class="${classTd}">
			<jstl:if test="${status == pending}">
				<a
					href="application/manager/change-status.do?applicationId=${row.id}">
					<button>
						<spring:message code="application.change" />
					</button>
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>

	<security:authorize access="hasRole('EXPLORER')">

		<display:column class="${classTd}" >
			<jstl:if test="${status == due}">
				<a href="application/explorer/edit.do?applicationId=${row.id}">
					<button>
						<spring:message code="application.edit.creditCard" />
					</button>
				</a>
			</jstl:if>
		</display:column>

		<jsp:useBean id="currDate" class="java.util.Date" />
		<fmt:formatDate value="${currDate}" var="currentDate"
			pattern="yyyy-MM-dd hh:mm:ss" />
		<display:column class="${classTd}" >
			<jstl:if
				test="${status == accepted and row.trip.endDate > currentDate}">
				<a href="application/explorer/cancel.do?applicationId=${row.id}">
					<button>
						<spring:message code="application.status.cancel" />
					</button>
				</a>
			</jstl:if>
		</display:column>
	</security:authorize>


</display:table>