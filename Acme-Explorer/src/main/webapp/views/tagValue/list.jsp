<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="tagValues" id="tagValue" requestURI="tagValue/manager/list.do" pagesize="${pagesize}" class="displayTag">

	<spring:message code="tagValue.value" />
	<display:column property="value"/>
	
	<spring:message code="tagValue.tag" />
	<display:column property="tag.name"/>
	
	<display:column>
		<a href = "tagValue/manager/edit.do?tagValueId=${tagValue.id}&tripId=${tripId}">
			<button>
				<spring:message code = "tagValue.edit"/>
			</button>
		</a>
	</display:column>
		
</display:table>

<%-- 	<a href = "tagValue/manager/create.do?tripId=${tripId}"> --%>
<%-- 		<jstl:if test="${trip.startDate > currentDate and (trip.cancelReason==null || trip.cancelReason==\"\")}"> --%>
<!-- 		<button> -->
<%-- 			<spring:message code = "tagValue.create"/> --%>
<!-- 		</button> -->
<!-- 	</a> -->
