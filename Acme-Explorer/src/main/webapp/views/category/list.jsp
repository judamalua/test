<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table 
	name="categories"
	id="category"
	requestURI="category/list.do"
	pagesize="10"
	class="displayTag">
	
	<jstl:if test="${categoryFather!=null}">
		<h1><a href="category/list.do?categoryId=${categoryFather.categoryFather.id}" ><jstl:out value="${categoryFather.name}"/></a></h1>
	</jstl:if>
	
	<spring:message code="category.name" var="name"/>
	<display:column property="name" title="${name}" sortable="true"/>

	<spring:message code="category.fatherCategory" var="fatherCategory"/>
	<display:column property="fatherCategory" title="${category.fatherCategory}" sortable="true"/>

	<spring:message code="category.childrenCategories" var="childrenCategories"/>
	<display:column title="${childrenCategories}">
		<a href="category/list.do?categoryId?=${category.id}">
			<spring:message code="category.childrenCategories.link"/>
		</a>
	</display:column>

	<spring:message code="category.trips" var="trips"/>
	<display:column title="${trips}">
		<a href="trip/list.do?categoryId?=${category.id}">
			<spring:message code="category.trips.link"/>
		</a>
	</display:column>
	
</display:table>