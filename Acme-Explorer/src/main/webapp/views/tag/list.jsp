<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="tags" id="tag" requestURI="tag/admin/list.do" pagesize="${pagesize}" class="displayTag">

<jstl:set var="containedInTrips" value="0" />

<jstl:forEach var="tagTrip" items="${tagsOnTrips}">
  <jstl:if test="${tagTrip.id == tag.id}">
    <jstl:set var="containedInTrips" value="1" />
  </jstl:if>
</jstl:forEach>
	
	<spring:message code="tag.name" var="name"/>
	<display:column property="name" title="${name}" sortable="true"/>
	
	<display:column>
	
		<jstl:if test="${containedInTrips == 0}">
			<a href="tag/admin/edit.do?tagId=${tag.id}">
				<spring:message code="tag.edit"/>
			</a>
		</jstl:if>
		<jstl:if test="${containedInTrips > 0}">
			<a href="tag/admin/delete.do?tagId=${tag.id}">
				<spring:message code="tag.delete"/>
			</a>
		</jstl:if>
	</display:column>
	
	
</display:table>

	<a href = "tag/admin/create.do">
	<button>
		<spring:message code = "tag.create"/>
	</button>
	</a>
