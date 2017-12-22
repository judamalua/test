<%--
 * action-1.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<spring:message code="format.date" var="formatDate" />
<jstl:if test="${not empty auditRecords}">
	<display:table name="auditRecords" id="row"
		requestURI="auditRecord/auditor/list.do" pagesize="${pagesize}"
		class="displaytag">

		<spring:message code="auditRecord.title" var="titleHeader" />
		<display:column property="title" title="${titleHeader}"
			sortable="true" />

		<spring:message code="auditRecord.description" var="descriptionHeader" />
		<display:column property="description" title="${descriptionHeader}"
			sortable="false" />

		<spring:message code="auditRecord.moment" var="momentHeader" />
		<display:column property="momentWhenCarriedOut"
			title="${momentHeader}" sortable="true" format="${formatDate}" />

		<spring:message code="auditRecord.attachments" var="attachmentsHeader" />
		<display:column title="${attachmentsHeader}">
			<ul>
				<jstl:forEach var="attachment" items="${row.attachments}">
					<li><a href="${attachment}"><jstl:out value="${attachment}" /></a></li>
				</jstl:forEach>
			</ul>
		</display:column>
		<security:authorize access="hasRole('AUDITOR')">

			<display:column>
				<jstl:if test="${!row.isFinalMode}">

					<a href="auditRecord/auditor/edit.do?auditRecordId=${row.id}">
						<button>
							<spring:message code="auditRecord.edit" />
						</button>
					</a>
				</jstl:if>
			</display:column>
		</security:authorize>
	</display:table>
</jstl:if>

