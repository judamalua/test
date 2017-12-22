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


<title><tiles:insertAttribute name="title" ignore="true" /></title>


<jstl:set value="&" var="connector" />
<jstl:if test="${requestUri==\"trip/search.do\"}">
	
	<jstl:set value="?keyword=${keyword}&" var="connector" />
</jstl:if>
<ul>
	<jstl:forEach begin="1" end="${pageNum}" var="index">
		<li><a href="${requestUri}${connector}page=${index-1}"> <jstl:out
					value="${index}" />
		</a></li>
	</jstl:forEach>
</ul>

<display:table pagesize="${pagesize}" requestURI="" name="trips" id="trip" class="displaytag">

	<spring:message code="trip.title" var="title" />
	<display:column property="title" title="${title}" sortable="true" />

	<spring:message code="trip.price" var="price" />
	<spring:message code="format.price" var="formatPrice"/>
	<display:column property="price" title="${price}" sortable="true" format="${formatPrice}" />
	
	<spring:message code="format.date" var="formatDate"/>
	<spring:message code="trip.startDate" var="startDate" />
	<display:column property="startDate" title="${startDate}"
		sortable="true" format="${formatDate}" />

	<spring:message code="trip.endDate" var="endDate" />

	<display:column property="endDate" title="${endDate}" sortable="true" format="${formatDate}"/>

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
	<jsp:useBean id="currDate" class="java.util.Date" />
	<jstl:if test="${requestUri==\"trip/manager/list.do\"}"></jstl:if>
	<fmt:formatDate value="${currDate}" var="currentDate" pattern="yyyy-MM-dd hh:mm:ss"/>
		<display:column>
		<jstl:if test="${trip.publicationDate > currentDate}">
			<a href="stage/manager/create.do?tripId=${trip.id}">
				<button>
					<spring:message code="stage.create" />
				</button>
			</a>
		</jstl:if>
		</display:column>
	</security:authorize>
	

</display:table>

