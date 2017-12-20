<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>


<jstl:if test="${fatherCategory != null && fatherCategory.name != \"CATEGORY\"}">
	<h1><a href="category/list.do?categoryId=${fatherCategory.fatherCategory.id}" ><jstl:out value="${fatherCategory.name}"/></a></h1>
</jstl:if>

<jstl:set var = "fatherCategoryName" value = "${fatherCategory.name}"/>

<display:table 
	name="categories"
	id="category"
	requestURI="category/list.do"
	pagesize="${pagesize}"
	class="displayTag">
	
	<spring:message code="category.name" var="name"/>
	<display:column property="name" title="${name}" sortable="true"/>

	<spring:message code="category.fatherCategory" var="fatherCategory"/>
	<jstl:if test="${fatherCategoryName != \"CATEGORY\"}">
	<display:column property="fatherCategory.name" title="${fatherCategory}" sortable="false"/>
	</jstl:if>

	<spring:message code="category.childrenCategories" var="childrenCategories"/>
	<display:column title="${childrenCategories}">
		<a href="category/list.do?categoryId=${category.id}">
			<spring:message code="category.childrenCategories.link"/>
		</a>
	</display:column>

	<spring:message code="category.trips" var="trips"/>
	<display:column title="${trips}">
		<a href="trip/list.do?categoryId=${category.id}">
			<spring:message code="category.trips.link"/>
		</a>
	</display:column>
	
	<security:authorize access="hasRole('ADMIN')">
	
	<display:column>
		<a href="category/admin/edit.do?categoryId=${category.id}">
			<spring:message code="category.edit"/>
		</a>
	</display:column>
	</security:authorize>
	
</display:table>

<security:authorize access = "hasRole('ADMIN')">

	<a href = "category/admin/create.do">
		<spring:message code = "category.create"/>
	</a>

</security:authorize>