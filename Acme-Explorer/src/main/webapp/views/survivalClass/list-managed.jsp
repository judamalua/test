<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="format.date" var="formatDate" />
<spring:message code="format.price" var="formatPrice" />
<display:table name="survivalClasses" id="row"
	requestURI="survivalClass/manager/list-managed.do"
	pagesize="${pagesize}" class="displaytag">

	<spring:message code="survivalclass.title" var="title" />
	<display:column property="title" title="${title}" sortable="true" />

	<spring:message code="survivalclass.description" var="description" />
	<display:column property="description" title="${description}"
		sortable="false" />

	<spring:message code="survivalclass.organisationMoment"
		var="organisationMoment" />
	<display:column property="organisationMoment"
		title="${organisationMoment}" sortable="true" format="${formatDate}" />

<%-- 	<spring:message code="survivalclass.location.name" var="locationName" /> --%>
<%-- 	<display:column property="location.name" title="${locationName}" --%>
<%-- 		sortable="true" /> --%>

	<spring:message code="survivalclass.location.gpsCoordinates"
		var="gpsCoordinates" />
	<display:column title = "${gpsCoordinates}" >
		<p><b>${row.location.name}</b></p>
		
		<iframe class="mapa"
				src="https://www.google.com/maps/embed/v1/search?q=${row.location.gpsCoordinates}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c"></iframe>
	</display:column>

	<%-- 	<spring:message code="survivalclass.trips" var="trips"/>
	<display:column title="${trips}" sortable="true" >
		<ul>
			<jstl:forEach var="trip" items="${survivalclass.trips}">
				<li>
					${trip.title}
				</li>
			</jstl:forEach>
		</ul>
	</display:column> --%>

	<spring:message code="table.actions" var="actions" />
	<display:column title="${actions}">
		<a href="survivalClass/manager/edit.do?survivalClassId=${row.id}">
			<button>
				<spring:message code="survivalclass.edit" />
			</button>
		</a>

		<%-- <a href = "survivalClass/manager/delete.do?survivalClassId=${row.id}">
				<spring:message code = "survivalclass.delete"/>
			</a> --%>
	</display:column>



</display:table>
<br>
<a href="survivalClass/manager/create.do">
	<button>
		<spring:message code="survivalclass.create" />
	</button>
</a>
