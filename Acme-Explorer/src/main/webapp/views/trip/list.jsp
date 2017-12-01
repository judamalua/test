<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form action="trip/search.do">
	<label>
		<spring:message code="trip.search"/>
	</label>
	<input type="text" name="keyword" placeholder="Title, ticker or description">
	
	<security:authorize access = "isAuthenticated()">
	
	<label>
		<spring:message code="trip.startPrice"/>
	</label>
	<input type="number" name="startPrice" placeholder="200,00">
	
	<label>
		<spring:message code="trip.endPrice"/>
	</label>
	<input type="number" name="endPrice" placeholder="700,00">
	
	<label>
		<spring:message code="trip.startDate"/>
	</label>
	<input type="number" name="startDate" placeholder="dd/MM/yyyy">
	
	<label>
		<spring:message code="trip.endDate"/>
	</label>
	<input type="number" name="endDate" placeholder="dd/MM/yyyy">
	
	</security:authorize>
	
	<input type = "submit" name = "search" value = "<spring:message code = "trip.search"/>"/>
	
</form>

<display:table
	name="trips"
	id="trip"
	class="displaytag">
	
	<jstl:set value="&" var="connector"></jstl:set>
	<security:authorize access="isAnonymus()">
		<jstl:set value="?" var="connector"></jstl:set>
	</security:authorize>
	
	<ul>
	<jstl:forEach begin="1"  end="${pageNum}" var="index" >
		<li>
			<a href="${requestUri}${connector}page=${index}">
				<jstl:out value="${index}" />
			</a>
		</li>
	</jstl:forEach>
	</ul>
	
	<spring:message code="trip.title" var="title"/>
	<display:column property="title" title="${title}" sortable="true" />
	
	<spring:message code="trip.price" var="price"/>
	<display:column property="ticker" title="${price}" sortable="true" />
		
	<spring:message code="trip.startDate" var="startDate"/>
	<display:column property="ticker" title="${startDate}" sortable="true" />
		
	<spring:message code="trip.endDate" var="endDate"/>
	<display:column property="ticker" title="${endDate}" sortable="true" />
	
	<spring:message code="trip.moreDetails" var="moreDetails"/>
	<display:column>
	<a href="trip/detailed-list.do?tripId=${trip.id}">
		<button>
			<spring:message code="trip.moreDetails"/>
		</button>
	</a>
	</display:column>
	
<%-- 	<security:authorize access="hasRole('EXPLORER')"> --%>
		
<%-- 			<spring:message code="trip.join" var="join"/> --%>
<%-- 			<display:column> --%>
<%-- 				<jstl:if test="${explorerId!=null}"> --%>
<!-- 					<a href="application/explorer/create.do"> -->
<!-- 						<button> -->
<%-- 							<jstl:out value="${join}"/> --%>
<!-- 						</button> -->
<!-- 					</a> -->
<%-- 				</jstl:if> --%>
<%-- 			</display:column> --%>
		
<%-- 	</security:authorize> --%>
	
<%-- 	<security:authorize access="hasRole('MANAGER')"> --%>
	
<%-- 		<display:column> --%>
<%-- 			<jstl:if test="${managerId!=null}"> --%>
<!-- 				<a href="trip/manager/join.do"> -->
<!-- 					<button> -->
<%-- 						<spring:message code="trip.join"/> --%>
<!-- 					</button> -->
<!-- 				</a> -->
<%-- 			</jstl:if> --%>
<%-- 		</display:column> --%>
		
<%-- 		<jsp:useBean id="now" class="java.util.Date" /> --%>
<%-- 		<fmt:formatDate var="currentDate" value="${now}" pattern="dd/MM/yyyy HH:mm"/> --%>
<%-- 		<jstl:if test="${trip.publicationDate>=currentDate}"> --%>
<%-- 			<display:column> --%>
<%-- 				<jstl:if test="${managerId!=null}"> --%>
<!-- 					<a href="trip/manager/edit.do"> -->
<!-- 						<button> -->
<%-- 							<spring:message code="trip.edit"/> --%>
<!-- 						</button> -->
<!-- 					</a> -->
<%-- 				</jstl:if> --%>
<%-- 			</display:column> --%>
<%-- 		</jstl:if> --%>

<%-- </security:authorize> --%>
	
</display:table>

<security:authorize access="hasRole('MANAGER')">
	<a href="trip/manager/edit.do">
		<button>
			<spring:message code="trip.create"/>
		</button>
	</a>
</security:authorize>
