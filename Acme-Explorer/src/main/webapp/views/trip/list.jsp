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

<form action="trip/search.do" method="post">

	<security:authorize access="isAnonymous()">
		<input type="hidden" name="isAnonymous" value="1" />
	</security:authorize>

	<jstl:if test="${requestUri==\"trip/list.do\"}">
		<label> <spring:message code="trip.search" />
		</label>
		<input type="text" name="keyword" id="keyword"
			placeholder="<spring:message code="search.keyword.placeholder"/>">

	</jstl:if>
	<security:authorize access="hasRole('EXPLORER')">


		<label> <spring:message code="trip.startPrice" />
		</label>
		<input type="number" name="startPrice" placeholder="200,00">

		<label> <spring:message code="trip.endPrice" />
		</label>
		<input type="number" name="endPrice" placeholder="700,00">

		<label> <spring:message code="trip.startDate" />
		</label>
		<input type="text" name="startDate" placeholder="dd/MM/yyyy">

		<label> <spring:message code="trip.endDate" />
		</label>
		<input type="text" name="endDate" placeholder="dd/MM/yyyy">

	</security:authorize>

	<jstl:if test="${requestUri==\"trip/list.do\"}">
		<input type="submit" name="search" id="search"
			value="<spring:message code = "trip.search"/>" />
	</jstl:if>

</form>


<jstl:set value="&" var="connector" />
<jstl:if test="${requestUri==\"trip/list.do\"}">
	<jstl:set value="?" var="connector" />
</jstl:if>
<ul>
	<jstl:forEach begin="1" end="${pageNum}" var="index">
		<li><a href="${requestUri}${connector}page=${index-1}"> <jstl:out
					value="${index}" />
		</a></li>
	</jstl:forEach>
</ul>

<display:table name="trips" id="trip" requestURI="${requestUri}"
	class="displaytag" pagesize = "${pagesize}">

	<spring:message code="trip.title" var="title" />
	<display:column property="title" title="${title}" sortable="true" />

	<spring:message code="trip.price" var="price" />
	<spring:message code="format.price" var="formatPrice" />
	<display:column property="price" title="${price}" sortable="true"
		format="${formatPrice}" />

	<spring:message code="format.date" var="formatDate" />
	
	
	<spring:message code="trip.startDate" var="startDate" />
	<display:column property="startDate" title="${startDate}"
		sortable="true" format="${formatDate}" />

	<spring:message code="trip.endDate" var="endDate" />

	<display:column property="endDate" title="${endDate}" sortable="true"
		format="${formatDate}" />

	<jstl:set value="false" var="anonymous" />
	<security:authorize access="isAnonymous()">
		<jstl:set value="true" var="anonymous" />
	</security:authorize>
	<spring:message code="trip.moreDetails" var="moreDetails" />
	<display:column>
		<a
			href="trip/detailed-trip.do?tripId=${trip.id}&anonymous=${anonymous}">
			<spring:message code="trip.moreDetails" />
		</a>
	</display:column>


	<security:authorize access="hasRole('MANAGER')">
		<%-- 	<jsp:useBean id="currDate" class="java.util.Date" /> --%>
		<%
			Date date = new Date(System.currentTimeMillis() + 60000);
					request.setAttribute("currDate", date);
		%>
		<jstl:if test="${requestUri eq \"trip/manager/list.do\"}">
			<fmt:formatDate value="${currDate}" var="currentDate"
				pattern="yyyy-MM-dd HH:mm" />
			<display:column>
				<jstl:if
					test="${trip.publicationDate > currentDate and (trip.cancelReason==null || trip.cancelReason==\"\")}">
					<a href="stage/manager/create.do?tripId=${trip.id}">
						<button>
							<spring:message code="stage.create" />
						</button>
					</a>
				</jstl:if>
			</display:column>


			<display:column>
				<jstl:if
					test="${trip.startDate > currentDate and (trip.cancelReason==null || trip.cancelReason==\"\")}">
					<a href="tagValue/manager/list.do?tripId=${trip.id}">
						<button>
							<spring:message code="trip.tagValue.list" />
						</button>
					</a>
				</jstl:if>
			</display:column>
		</jstl:if>
	</security:authorize>

</display:table>

<security:authorize access="hasRole('MANAGER')">
	<a href="trip/manager/create.do">
		<button>
			<spring:message code="trip.create" />
		</button>
	</a>
</security:authorize>
