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

<display:table name="legalTexts" id="legalText"
	requestURI="legalText/admin/list.do" pagesize="${pagesize}"
	class="displayTag">

	<spring:message code="legalText.title" var="title" />
	<display:column property="title" title="${title}" sortable="true" />

	<spring:message code="legalText.body" var="body" />
	<display:column property="body" title="${body}" sortable="false" />

	<spring:message code="legalText.registrationDate"
		var="registrationDate" />
	<display:column property="registrationDate" title="${registrationDate}"
		sortable="true" />

	<spring:message code="legalText.applicableLaws" var="applicableLaws" />
	<display:column title="${applicableLaws}">
		<ul>
			<jstl:forEach var="applicableLaw" items="${legalText.applicableLaws}">
				<li>
					<jstl:out value="${applicableLaw}" />
				</li>
			</jstl:forEach>
		</ul>
	</display:column>

	<display:column>
		<jstl:if test="${!legalText.finalMode}">
			<a href="legalText/admin/edit.do?legalTextId=${legalText.id}">
				<button>
					<spring:message code="legalText.edit" />
				</button>
			</a>
		</jstl:if>
	</display:column>


</display:table>

<a href="legalText/admin/create.do">
	<button>
		<spring:message code="legalText.create" />
	</button>
</a>

