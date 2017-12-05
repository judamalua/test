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


Errores: Añadir formato a fechas Añadir formato a numeros Arreglar
iframe de maps


<br />
<a href="${sponsorship.additionalInfoLink}"><img
	src="${sponsorship.bannerUrl}" alt="trip.sponsorship" /></a>
<h1>
	<spring:message code="trip.title" />
	:
	<jstl:out value="${trip.title}" />
</h1>

<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${hasManager}">
		<a href="trip/manager/edit.do?tripId=${trip.id}">
			<button>
				<spring:message code="trip.edit" />
			</button>
		</a>
	</jstl:if>
</security:authorize>

<p>
	<spring:message code="trip.ticker" />
	:
	<jstl:out value="${trip.ticker}" />
</p>
<p>
	<spring:message code="trip.category" />
	:
	<jstl:out value="${trip.category.name}" />
</p>
<p>
	<spring:message code="trip.description" />
	:
	<jstl:out value="${trip.description}" />
</p>
<jstl:if test="${trip.cancelReason != null}">
	<p>
		<spring:message code="trip.cancelReason" />
		:
		<jstl:out value="${trip.cancelReason}" />
	</p>
</jstl:if>
<p>
	<spring:message code="trip.price" />
	:
	<jstl:out value="${trip.price}" />
	EUR
</p>
<p>
	<spring:message code="trip.requirements" />
	:
	<jstl:out value="${trip.requirements}" />
</p>
<p>
	<spring:message code="trip.startDate" />
	:
	<jstl:out value="${trip.startDate}" />
</p>
<p>
	<spring:message code="trip.endDate" />
	:
	<jstl:out value="${trip.endDate}" />
</p>

<jstl:if test="${not empty trip.tags}">
	<p>
		<spring:message code="trip.tags" />
		:
		<jstl:forEach var="tag" items="${trip.tags}" varStatus="index">
			<jstl:out value="${tag.name}" />

			<%-- 		<jstl:if test="${trip.tags.length != index}">, </jstl:if> --%>
		</jstl:forEach>
	</p>
</jstl:if>

<p>
	<spring:message code="trip.ranger" />
	:
	<jstl:out value="${trip.ranger.name}" />
	<jstl:out value="${trip.ranger.surname}" />
</p>

<security:authorize access="hasRole('EXPLORER')">
	<jstl:if test="${!hasExplorer}">
		<jsp:useBean id="now" class="java.util.Date" />
		<fmt:formatDate var="currentDate" value="${now}"
			pattern="dd/MM/yyyy HH:mm" />
		<jstl:if test="${trip.publicationDate>=currentDate}">

			<a href="application/explorer/create.do?tripId=${trip.id}">
				<button>
					<spring:message code="trip.apply" />
				</button>
			</a>
		</jstl:if>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('MANAGER')">
	<jstl:if test="${!hasManager}">
		<a href="trip/manager/join.do?tripId=${trip.id}">
			<button>
				<spring:message code="trip.manage" />
			</button>
		</a>
	</jstl:if>
</security:authorize>

<jstl:if test="${not empty trip.stages}">
	<display:table name="${trip.stages}" id="row1"
		requestURI="stage/list.do?tripId=${trip.id}" pagesize="10"
		class="displaytag">

		<spring:message code="detailedTrip.stage.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="true" />

		<spring:message code="detailedTrip.stage.description"
			var="descriptionHeader" />
		<display:column property="description" title="${descriptionHeader}"
			sortable="false" />

		<spring:message code="detailedTrip.stage.price" var="priceHeader" />
		<display:column property="price" title="${priceHeader}"
			sortable="true" />
	</display:table>
</jstl:if>

<jstl:if test="${not empty trip.survivalClasses}">
	<display:table name="${trip.survivalClasses}" id="row3"
		requestURI="survivalClass/list.do?tripId=${trip.id}" pagesize="10"
		class="displaytag">

		<spring:message code="detailedTrip.survivalClass.title"
			var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="true" />

		<spring:message code="detailedTrip.survivalClass.description"
			var="descriptionHeader" />
		<display:column property="description" title="${descriptionHeader}"
			sortable="false" />

		<spring:message code="detailedTrip.survivalClass.organisationMoment"
			var="organisationMomentHeader" />
		<display:column property="organisationMoment"
			title="${organisationMomentHeader}" sortable="true" />

		<spring:message code="detailedTrip.survivalClass.location"
			var="locationHeader" />
		<display:column title="${locationHeader}" sortable="false">
			<p>${row.location.name}</p>
			<iframe class="mapa"
				src="https://www.google.com/maps/embed/v1/search?q=${row3.location.gpsCoordinates}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c"></iframe>
		</display:column>


		<security:authorize access="hasRole('MANAGER')">
			<jstl:if test="${hasManager}">
				<display:column>

					<a href="survivalClass/manager/edit.do?survivalClassId=${row3.id}">
						<spring:message code="detailed.trip.edit" />
					</a>

				</display:column>
			</jstl:if>
		</security:authorize>

		<security:authorize access="hasRole('EXPLORER')">
			<jstl:if test="${hasExplorer}">
				<display:column>

					<a href="survivalClass/explorer/join.do?survivalClassId=${row3.id}">
						<spring:message code="detailed.trip.join" />
					</a>

				</display:column>
			</jstl:if>

			<%-- 		<display:column> --%>
			<%-- 			<a href = "survivalClass/auditor/leave.do?survivalClassId=${row.id}"> --%>
			<%-- 				<spring:message code = "survivalclass.leave"/> --%>
			<!-- 			</a> -->
			<%-- 		</display:column> --%>

		</security:authorize>

	</display:table>
</jstl:if>

<jstl:if test="${not empty trip.auditRecords}">
	<security:authorize access="hasRole('MANAGER')">
		<jstl:if test="${hasManager}">
			<display:table name="${trip.auditRecords}" id="row4"
				requestURI="auditRecord/list.do?tripId=${trip.id}" pagesize="10"
				class="displaytag">

				<spring:message code="detailedTrip.auditRecord.moment"
					var="momentHeader" />
				<display:column property="momentWhenCarriedOut" title="${momentHeader}"
					sortable="true" />

				<spring:message code="detailedTrip.auditRecord.title"
					var="titleHeader" />
				<display:column property="title" title="${titleHeader}"
					sortable="true" />

				<spring:message code="detailedTrip.auditRecord.description"
					var="descriptionHeader" />
				<display:column property="description" title="${descriptionHeader}"
					sortable="false" />
				<spring:message code="detailedTrip.auditRecord.attachments"
					var="attachmentsHeader" />
				<display:column property="attachments" title="${attachmentsHeader}"
					sortable="false">
					<ul>
						<jstl:forEach var="attachment" items="${row4.attachments}">

							<li>${attachment}</li>

						</jstl:forEach>
					</ul>
				</display:column>
				<spring:message code="detailedTrip.auditRecord.auditor"
					var="auditorHeader" />
				<display:column property="auditor" title="${auditorHeader}"
					sortable="false" />
			</display:table>
		</jstl:if>
	</security:authorize>
</jstl:if>

<jstl:if test="${trip.legalText!=null}">

	<spring:message code="trip.legalText" />
		:
		<h2>
		<jstl:out value="${trip.legalText.title}" />
	</h2>
	<br />
	<jstl:out value="${trip.legalText.body}" />
	<br />
	<jstl:out value="${trip.legalText.registrationDate}" />

</jstl:if>

<jstl:if test="${not empty trip.notes}">
	<display:table name="${trip.notes}" id="row4"
		requestURI="notes/list.do?tripId=${trip.id}" pagesize="10"
		class="displaytag">

		<spring:message code="detailedTrip.notes.moment" var="noteHeader" />
		<display:column property="moment" title="${noteHeader}"
			sortable="true" />

		<spring:message code="detailedTrip.notes.remark" var="remarkHeader" />
		<display:column property="remark" title="${remarkHeader}" />

		<jstl:if test="${row.reply!=null}">
			<spring:message code="detailedTrip.notes.reply" var="replyHeader" />
			<display:column property="reply" title="${replyHeader}" />

			<spring:message code="detailedTrip.notes.momentReply"
				var="momentReplyHeader" />
			<display:column property="momentOfReply" title="${momentReplyHeader}" />
		</jstl:if>

		<security:authorize access="hasRole('MANAGER')">
			<jstl:if test="${hasManager}">
				<display:column>
					<a href="note/manager/edit.do?noteId=${row.id}"> <spring:message
							code="detailedTrip.notes.manager.reply" />
					</a>
				</display:column>
			</jstl:if>
		</security:authorize>
	</display:table>

	<security:authorize access="hasRole('AUDITOR')">
		<a href="note/auditor/edit.do">
			<button>
				<spring:message code="detailedTrip.notes.create" />
			</button>
		</a>
	</security:authorize>
</jstl:if>

<jstl:if test="${not empty trip.stories}">
	<display:table name="${trip.stories}" id="row2"
		requestURI="story/list.do?tripId=${trip.id}" pagesize="10"
		class="displaytag">

		<spring:message code="detailedTrip.story.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="true" />

		<spring:message code="detailedTrip.story.text" var="textHeader" />
		<display:column property="pieceOfText" title="${textHeader}"
			sortable="false" />

		<spring:message code="detailedTrip.story.attachments"
			var="attachmentsHeader" />
		<display:column property="attachments" title="${attachmentsHeader}"
			sortable="false">
			<ul>
				<jstl:forEach var="attachment" items="${row2.attachments}">

					<li>${attachment}</li>

				</jstl:forEach>
			</ul>
		</display:column>
	</display:table>
</jstl:if>