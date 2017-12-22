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


<spring:message code="format.price" var="formatPrice" />
<spring:message code="format.date" var="formatDate" />
<spring:message code="format.date.out" var="formatDateOut" />
<spring:message code="language" var="language" />

<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<br />

<fmt:formatDate var="publicationDate" value="${trip.publicationDate}"
	pattern="yyyy-MM-dd HH:mm" />
<br />

<jstl:if test="${sponsorship != null}">
	<a href="${sponsorship.additionalInfoLink}"> <img
		class="sponsorshipBannerUrl" src="${sponsorship.bannerUrl}"
		alt="trip.sponsorship" />
	</a>
</jstl:if>

<h1>
	<spring:message code="trip.title" />
	:
	<jstl:out value="${trip.title}" />
</h1>

<security:authorize access="hasRole('MANAGER')">
	<jstl:if
		test="${hasManager and publicationDate > currentDate and (trip.cancelReason==null or trip.cancelReason==\"\")}">
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
<jstl:if test="${trip.cancelReason != null && trip.cancelReason != ''}">
	<p>
		<spring:message code="trip.cancelReason" />
		:
		<jstl:out value="${trip.cancelReason}" />
	</p>
</jstl:if>
<p>
	<spring:message code="trip.price" />
	:
	<jstl:set value="${trip.price}" var="price" />

	<jstl:if test="${language==\"en\"}">
		&#8364; <jstl:out value="${trip.price}" />
	</jstl:if>
	<jstl:if test="${language==\"es\"}">
		<jstl:out value="${trip.price} " />&#8364; 
	</jstl:if>
</p>
<p>
	<spring:message code="trip.requirements" />
	:
	<jstl:out value="${trip.requirements}" />
</p>
<p>
	<spring:message code="trip.startDate" />
	:
	<jstl:set value="${trip.startDate}" var="startDate" />
	<fmt:formatDate value="${startDate}" pattern="${formatDateOut}" />
</p>
<p>
	<spring:message code="trip.endDate" />
	:
	<jstl:set value="${trip.endDate}" var="endDate" />
	<fmt:formatDate value="${endDate}" pattern="${formatDateOut}" />
</p>

<%-- <jstl:if test="${not empty trip.tags}">
	<p>
		<spring:message code="trip.tags" />
		:
		<jstl:forEach var="tag" items="${trip.tags}" varStatus="index">
			<jstl:out value="${tag.name}" />

			<jstl:if test="${trip.tags.length != index}">, </jstl:if>
		</jstl:forEach>
	</p>
</jstl:if>  --%>

<jstl:if test="${not empty trip.tagValues}">
	<display:table name="trip.tagValues" id="tagValue">

		<spring:message code="trip.tagValue.value" var="tagValueTitle" />
		<display:column property="value" title="${tagValueTitle}"
			sortable="true" />

		<spring:message code="trip.tagValue.tag" var="tagTitle" />
		<display:column property="tag.name" title="${tagTitle}"
			sortable="true" />
			
		<display:column>
			<a href="tagValue/manager/edit.do?tagValueId=${tagValue.id}&tripId=${trip.id}">
				<button>
					<spring:message code="tagValue.edit" />
				</button>
			</a>
		</display:column>

	</display:table>
</jstl:if>

<p>
	<spring:message code="trip.ranger" />
	:
	<jstl:out value="${trip.ranger.name}" />
	<jstl:out value="${trip.ranger.surname}" />
	<jstl:set value="false" var="isRanger" />
	<security:authorize access="hasRole('RANGER')">
		<jstl:set value="true" var="isRanger" />
	</security:authorize>
	<a
		href="curriculum/show.do?rangerId=${trip.ranger.id}&isRanger=${isRanger}">
		<button>
			<spring:message code="trip.ranger.curriculum" />
		</button>
	</a>
</p>

<security:authorize access="hasRole('EXPLORER')">

	<jstl:if test="${!hasExplorer}">
		<jstl:if
			test="${trip.publicationDate<currentDate and trip.startDate > currentDate}">
			<a href="application/explorer/create.do?tripId=${trip.id}">
				<button>
					<spring:message code="trip.apply" />
				</button>
			</a>
		</jstl:if>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('MANAGER')">
	<jstl:if
		test="${!hasManager and (trip.cancelReason==null or trip.cancelReason==\"\")}">
		<a href="trip/manager/join.do?tripId=${trip.id}">
			<button>
				<spring:message code="trip.manage" />
			</button>
		</a>
	</jstl:if>
</security:authorize>

<jstl:if test="${not empty trip.stages}">
	<h2>
		<spring:message code="detailed.trip.stages" />
	</h2>
	<display:table name="${trip.stages}" id="row1"
		requestURI="trip/detailed-trip.do?tripId=${trip.id}"
		pagesize="${pagesize}" class="displaytag">

		<spring:message code="detailedTrip.stage.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="true" />

		<spring:message code="detailedTrip.stage.description"
			var="descriptionHeader" />
		<display:column property="description" title="${descriptionHeader}"
			sortable="false" />

		<spring:message code="detailedTrip.stage.price" var="priceHeader" />
		<display:column property="price" title="${priceHeader}"
			sortable="true" format="${formatPrice}" />
		<display:column>
			<jstl:if
				test="${trip.publicationDate > currentDate and hasManager and (trip.cancelReason==null or trip.cancelReason==\"\")}">
				<a href="stage/manager/edit.do?stageId=${row1.id}">
					<button>
						<spring:message code="stage.edit" />
					</button>
				</a>
			</jstl:if>
		</display:column>
	</display:table>

	<jstl:if
		test="${trip.publicationDate > currentDate and hasManager and (trip.cancelReason==null or trip.cancelReason==\"\")}">
		<a href="stage/manager/create.do?tripId=${trip.id}">
			<button>
				<spring:message code="stage.create" />
			</button>
		</a>
	</jstl:if>
</jstl:if>
<br />
<jstl:if test="${not empty survivalClasses}">
	<h2>
		<spring:message code="detailed.trip.survivalClasses" />
	</h2>
	<display:table name="${survivalClasses}" id="row3"
		requestURI="trip/detailed-trip.do?tripId=${trip.id}"
		pagesize="${pagesize}" class="displaytag">

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
			title="${organisationMomentHeader}" sortable="true"
			format="${formatDate}" />

		<spring:message code="detailedTrip.survivalClass.location"
			var="locationHeader" />
		<display:column title="${locationHeader}" sortable="false">
			<p><b>${row3.location.name}</b></p>
			<iframe class="mapa"
				src="https://www.google.com/maps/embed/v1/search?q=${row3.location.gpsCoordinates}&key=AIzaSyBe0wmulZvK1IM3-3jIUgbxt2Ax_QOVW6c"></iframe>
		</display:column>


		<%-- 		<security:authorize access="hasRole('MANAGER')"> --%>
		<%-- 			<jstl:if test="${hasManager}"> --%>
		<%-- 				<display:column> --%>
		<%-- 				<jstl:if test="${row3.organisationMoment>currentDate}"> --%>
		<%-- 					<a href="survivalClass/manager/edit.do?survivalClassId=${row3.id}"> --%>
		<%-- 						<spring:message code="detailed.trip.edit" /> --%>
		<!-- 					</a> -->
		<%-- 				</jstl:if> --%>
		<%-- 					<jstl:if --%>
		<%-- 						test="${trip.publicationDate>=currentDate and hasManager and (trip.cancelReason==null or trip.cancelReason==\"\")}}"> --%>
		<!-- 						<a -->
		<%-- 							href="trip/manager/removeSurvivalClass.do?survivalClassId=${row3.id}&tripId=${trip.id}"> --%>
		<%-- 							<spring:message code="detailed.trip.remove" /> --%>
		<!-- 						</a> -->
		<%-- 					</jstl:if> --%>
		<%-- 				</display:column> --%>
		<%-- 			</jstl:if> --%>
		<%-- 		</security:authorize> --%>

		<security:authorize access="hasRole('EXPLORER')">
			<jstl:if test="${hasExplorer}">
				<jstl:if test="${!survivalClassesJoinedIndexed[row3_rowNum -1]}">
					<jstl:if
						test="${row3.organisationMoment>=currentDate and (trip.cancelReason==null or trip.cancelReason==\"\")}">
						<display:column>

							<a
								href="survivalClass/explorer/join.do?survivalClassId=${row3.id}">
								<spring:message code="survivalclass.join" />
							</a>

						</display:column>
					</jstl:if>
				</jstl:if>
				<jstl:if
					test="${survivalClassesJoinedIndexed[row3_rowNum -1] and (trip.cancelReason==null or trip.cancelReason==\"\")}">
					<display:column>

						<a
							href="survivalClass/explorer/leave.do?survivalClassId=${row3.id}">
							<spring:message code="survivalclass.leave" />
						</a>
					</display:column>
				</jstl:if>
			</jstl:if>



		</security:authorize>

	</display:table>
</jstl:if>
<security:authorize access="hasRole('MANAGER')">
	<jstl:if
		test="${trip.publicationDate>currentDate and hasManager and (trip.cancelReason==null or trip.cancelReason==\"\")}">

		<a href="trip/manager/manageSurvivalClasses.do?tripId=${trip.id}">
			<button>
				<spring:message code="detailed.trip.manageSurvivalClasses" />
			</button>
		</a>
	</jstl:if>
</security:authorize>

<jstl:if test="${not empty trip.auditRecords}">
	<h2>
		<spring:message code="detailed.trip.auditRecords" />
	</h2>
	<display:table name="${trip.auditRecords}" id="row4"
		requestURI="trip/detailed-trip.do?tripId=${trip.id}"
		pagesize="${pagesize}" class="displaytag">

		<spring:message code="detailedTrip.auditRecord.moment"
			var="momentHeader" />
		<display:column property="momentWhenCarriedOut"
			title="${momentHeader}" sortable="true" format="${formatDate}" />

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
		<display:column>
			<ul>
				<jstl:forEach var="attachment" items="${row4.attachments}">
					<li><a href="${attachment}"><jstl:out
								value="${attachment}" /></a></li>
				</jstl:forEach>
			</ul>
		</display:column>
		<spring:message code="detailedTrip.auditRecord.auditor"
			var="auditorHeader" />
		<display:column property="auditor.name" title="${auditorHeader}"
			sortable="false" />
	</display:table>
</jstl:if>

<jstl:if test="${trip.legalText!=null and trip.legalText.title!=null}">

	<%-- 	<spring:message code="trip.legalText" /> --%>
	<!-- 		: -->
	<h2>
		<jstl:out value="${trip.legalText.title}" />
	</h2>
	<br />
	<jstl:out value="${trip.legalText.body}" />
	<br />
	<jstl:set value="${trip.legalText.registrationDate}"
		var="registrationDate" />
	<fmt:formatDate value="${registrationDate}" pattern="${formatDateOut}" />

</jstl:if>

<security:authorize access="hasRole('MANAGER')">

	<jstl:if test="${not empty trip.notes}">
		<h2>
			<spring:message code="detailed.trip.notes" />
		</h2>

		<display:table name="${trip.notes}" id="row4"
			requestURI="trip/detailed-trip.do?tripId=${trip.id}"
			pagesize="${pagesize}" class="displaytag">

			<spring:message code="detailedTrip.notes.moment" var="noteHeader" />
			<display:column property="moment" title="${noteHeader}"
				sortable="true" format="${formatDate}" />

			<spring:message code="detailedTrip.notes.remark" var="remarkHeader" />
			<display:column property="remark" title="${remarkHeader}" />

			<jstl:if test="${row.reply!=null}">
				<spring:message code="detailedTrip.notes.reply" var="replyHeader" />
				<display:column property="reply" title="${replyHeader}" />

				<spring:message code="detailedTrip.notes.momentReply"
					var="momentReplyHeader" />
				<display:column property="momentOfReply"
					title="${momentReplyHeader}" format="${formatDate}" />
			</jstl:if>

			<security:authorize access="hasRole('MANAGER')">
				<jstl:if test="${hasManager}">
					<display:column>
						<jstl:if test="${row4.reply == null or row4.reply == \"\"}">
							<a href="note/manager/edit.do?noteId=${row4.id}"> <spring:message
									code="detailedTrip.notes.manager.reply" />
							</a>
						</jstl:if>
					</display:column>
				</jstl:if>
			</security:authorize>
		</display:table>
	</jstl:if>
</security:authorize>


<security:authorize access="hasRole('AUDITOR')">
	<jstl:if
		test="${trip.publicationDate<currentDate and (trip.cancelReason==null or trip.cancelReason==\"\")}">
		<a href="note/auditor/create.do?tripId=${trip.id}">
			<button>
				<spring:message code="detailedTrip.notes.create" />
			</button>
		</a>
	</jstl:if>
</security:authorize>

<security:authorize access="hasRole('AUDITOR')">
	<jstl:if
		test="${trip.publicationDate<currentDate and (trip.cancelReason==null or trip.cancelReason==\"\")}">
		<a href="auditRecord/auditor/create.do?tripId=${trip.id}">
			<button>
				<spring:message code="auditRecord.create" />
			</button>
		</a>
	</jstl:if>

</security:authorize>

<security:authorize access="hasRole('SPONSOR')">
	<jstl:if
		test="${trip.publicationDate<currentDate and (trip.cancelReason==null or trip.cancelReason==\"\")}">
		<a href="sponsorship/sponsor/create.do?tripId=${trip.id}">
			<button>
				<spring:message code="sponsorship.create" />
			</button>
		</a>
	</jstl:if>

</security:authorize>
<jstl:if test="${not empty trip.stories}">
	<h2>
		<spring:message code="detailed.trip.stories" />
	</h2>
	<display:table name="${trip.stories}" id="row2"
		requestURI="trip/detailed-trip.do?tripId=${trip.id}"
		pagesize="${pagesize}" class="displaytag">

		<spring:message code="detailedTrip.story.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="true" />

		<spring:message code="detailedTrip.story.text" var="textHeader" />
		<display:column property="pieceOfText" title="${textHeader}"
			sortable="false" />

		<spring:message code="detailedTrip.story.attachments"
			var="attachmentsHeader" />
		<display:column>
			<ul>
				<jstl:forEach var="attachment" items="${row2.attachments}">
					<li><a href="${attachment}"><jstl:out
								value="${attachment}" /></a></li>
				</jstl:forEach>
			</ul>
		</display:column>


	</display:table>
</jstl:if>

<security:authorize access="hasRole('EXPLORER')">

	<jstl:if test="${hasExplorer and trip.endDate<currentDate }">

		<a href="story/explorer/create.do?tripId=${trip.id}">
			<button>
				<spring:message code="detailedTrip.story.create" />
			</button>
		</a>
	</jstl:if>
</security:authorize>
<br />
<br />

<security:authorize access="hasRole('MANAGER')">
	<jstl:if
		test="${trip.publicationDate<currentDate and trip.startDate > currentDate and hasManager and (trip.cancelReason==null or trip.cancelReason==\"\")}">
		<a href="trip/manager/cancel.do?tripId=${trip.id}">
			<button>
				<spring:message code="detailedTrip.cancel" />
			</button>
		</a>
	</jstl:if>


</security:authorize>