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

<jstl:if test="${requestUri==\"trip/search.do\"}">
	<script type="text/javascript">
		function searchAjax() {
			console.log("Test");
			delay(function() {

				var searchText = $('#keyword').val();
				var searchURL = "trip/search-ajax.do?keyword=" + searchText;

				$.post(searchURL, null, function(result) {
					$("#tableTrips").html(result);
				});
			}, 800);
		};

		var delay = (function() {
			var timer = 0;
			return function(callback, ms) {
				clearTimeout(timer);
				timer = setTimeout(callback, ms);
			};
		})();
	</script>
	<form action="trip/search.do" method="GET">

		<label> <spring:message code="trip.search" />
		</label> <input type="text" name="keyword" id="keyword" onkeyup="searchAjax()"
			placeholder="<spring:message code="search.keyword.placeholder"/>">
		<input type="submit" name="search" id="search"
			value="<spring:message code = "trip.search"/>" />
	</form>

</jstl:if>

<jstl:if test="${requestUri==\"trip/listExplorer.do\"}">

	<form:form action="trip/searchExplorer.do" modelAttribute="search"
		method="POST">
		<form:hidden path="id" />
		<form:hidden path="version" />
		<form:hidden path="searchMoment" />
		<form:hidden path="trips" />


		<label> <spring:message code="trip.search" />
		</label>
		<form:input path="keyWord" />
<%-- 		<form:errors cssClass="error" path="keyWord" /> --%>

		<label> <spring:message code="trip.startPrice" />
		</label>
		<form:input path="priceRangeStart" placeholder="200.00" />
		<form:errors cssClass="error" path="priceRangeStart" />

		<label> <spring:message code="trip.endPrice" />
		</label>
		<form:input path="priceRangeEnd" placeholder="700.00" />
		<form:errors cssClass="error" path="priceRangeEnd" />

		<label> <spring:message code="trip.startDate" />
		</label>
		<form:input path="dateRangeStart" placeholder="dd/MM/yyyy hh:mm" />
		<form:errors cssClass="error" path="dateRangeStart" />

		<label> <spring:message code="trip.endDate" />
		</label>
		<form:input path="dateRangeEnd" placeholder="dd/MM/yyyy hh:mm" />
		<form:errors cssClass="error" path="dateRangeEnd" />


		<input type="submit" name="save" id="save"
			value="<spring:message code = "trip.search"/>" />

	</form:form>
</jstl:if>


<div id="tableTrips">
	<jstl:set value="&" var="connector" />
	<jstl:if test="${requestUri==\"trip/search.do\"}">
		<jstl:set value="?keyword=${keyword}&" var="connector" />
	</jstl:if>
	<jstl:if test="${requestUri==\"trip/manager/list.do\"}">
		<jstl:set value="?" var="connector" />
	</jstl:if>
	<jstl:if test="${requestUri==\"trip/listExplorer.do\"}">
		<jstl:set
			value="?"
			var="connector" />
	</jstl:if>
	<span class="pagebanner"> <jstl:forEach begin="1"
			end="${pageNum}" var="index">
			<a href="${requestUri}${connector}page=${index-1}"> <jstl:out
					value="${index}" />
			</a>
			<jstl:if test="${index!=pageNum}">,</jstl:if>
		</jstl:forEach>
	</span>
	<display:table name="trips" id="trip" requestURI="${requestUri}"
		class="displayTag" pagesize="${pagesize}">

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
						<a href = "tagValue/manager/create.do?tripId=${trip.id}">
							<button>
								<spring:message code = "tagValue.create"/>
							</button>
						</a>
					</jstl:if>
				</display:column>
			</jstl:if>
		</security:authorize>

	</display:table>

</div>

<security:authorize access="hasRole('MANAGER')">
	<a href="trip/manager/create.do">
		<button>
			<spring:message code="trip.create" />
		</button>
	</a>
</security:authorize>
