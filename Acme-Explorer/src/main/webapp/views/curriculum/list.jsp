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



<display:table name="curriculum" id="row"
	requestURI="curriculum/ranger/list.do" pagesize="10" class="displaytag">

	<spring:message code="curriculum.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" sortable="false" />

</display:table>


<jstl:if test="${curriculum!=null}">
	<security:authorize access="hasRole('RANGER')">


		<spring:message code="curriculum.personalRecord.nameOfCandidate"
			var="nameOfCandidateHeader" />
		<p>${curriculum.personalRecord.nameOfCandidate}</p>

		<spring:message code="curriculum.personalRecord.photo"
			var="photoHeader" />
		<p>${curriculum.personalRecord.photo}</p>

		<spring:message code="curriculum.personalRecord.email"
			var="emailHeader" />
		<p>${curriculum.personalRecord.email}</p>

		<spring:message code="curriculum.personalRecord.phoneNumber"
			var="phoneNumberHeader" />
		<p>${curriculum.personalRecord.phoneNumber}</p>

		<spring:message code="curriculum.personalRecord.linkedInProfileURL"
			var="linkedInProfileURLHeader" />
		<p>${curriculum.personalRecord.linkedInProfileURL}</p>


		<security:authorize access="hasRole('RANGER')">


			<a href="auditRecord/ranger/edit.do?auditorRecordId=${row1.id}">
				<spring:message code="curriculum.edit" />
			</a>


		</security:authorize>


	</security:authorize>
</jstl:if>



<jstl:if test="${curriculum!=null}">
	<jstl:if test="${not empty curriculum.professionalRecords}">
		<security:authorize access="hasRole('RANGER')">
			<jstl:set value="${professionalRecords}" var="professionalRecords"></jstl:set>

			<display:table name="professionalRecords" id="professionalRecord"
				requestURI="professionalRecord/list.do?curriculumId=${curriculum.id}"
				pagesize="10" class="displaytag">

				<spring:message code="professionalRecord.companyName"
					var="companyNameHeader" />
				<display:column property="companyName" title="${companyNameHeader}"
					sortable="false" />

				<spring:message
					code="curriculum.professionalRecord.workingPeriodStart"
					var="workingPeriodStartHeader" />
				<display:column property="workingPeriodStart"
					title="${workingPeriodStartHeader}" sortable="false" />

				<spring:message
					code="curriculum.professionalRecord.workingPeriodEnd"
					var="workingPeriodEndHeader" />
				<display:column property="workingPeriodEnd"
					title="${workingPeriodEndHeader}" sortable="false" />

				<spring:message code="curriculum.professionalRecord.role"
					var="roleHeader" />
				<display:column property="role" title="${roleHeader}"
					sortable="false" />

				<spring:message code="curriculum.professionalRecord.attachment"
					var="attachmentHeader" />
				<display:column property="attachment" title="${attachmentHeader}"
					sortable="false" />

				<spring:message code="curriculum.professionalRecord.commentaries"
					var="commentariesHeader" />
				<display:column property="commentaries"
					title="${commentariesHeader}" sortable="false" />


				<security:authorize access="hasRole('RANGER')">

					<display:column>
						<a
							href="professionalRecord/ranger/edit.do?professionalRecordId=${professionalRecord.id}">
							<spring:message code="curriculum.edit" />
						</a>
					</display:column>

				</security:authorize>

			</display:table>
		</security:authorize>
	</jstl:if>
</jstl:if>

<security:authorize access="hasRole('RANGER')">

	<a href="professionalRecord/ranger/create.do">
		<button>
			<spring:message code="curriculum.professionalRecord.create" />
		</button>
	</a>

</security:authorize>

<jstl:out value="${miscellaneousRecords}"></jstl:out>

<jstl:if test="${curriculum!=null}">
	<jstl:if test="${not empty miscellaneousRecords}">
		<security:authorize access="hasRole('RANGER')">
		<jstl:set value="${miscellaneousRecords}" var="miscellaneousRecords"></jstl:set>

			<display:table name="miscellaneousRecords" id="miscellaneousRecord"
				requestURI="miscellaneousRecord/list.do?curriculumId=${curriculum.id}"
				pagesize="10" class="displaytag">

			<spring:message code="curriculum.miscellaneousRecord.title"
					var="title1Header" />
				<display:column property="title" title="${title1Header}"
					sortable="false" /> 

				<spring:message code="curriculum.miscellaneousRecord.attachment"
					var="attachment1Header" />
				<display:column property="attachment" title="${attachment1Header}"
					sortable="false" />

				<spring:message code="curriculum.miscellaneousRecord.commentaries"
					var="commentaries1Header" />
				<display:column property="commentaries"
					title="${commentaries1Header}" sortable="false" />


				<security:authorize access="hasRole('RANGER')">

					<display:column>
						<a
							href="miscellaneousRecord/ranger/edit.do?miscellaneousRecordId=${miscellaneousRecord.id}">
							<spring:message code="curriculum.edit" />
						</a>
					</display:column>

				</security:authorize>

			</display:table>
		</security:authorize>
	</jstl:if>
</jstl:if>


<security:authorize access="hasRole('RANGER')">

	<a href="miscellaneousRecord/ranger/create.do">
		<button>
			<spring:message code="curriculum.miscellaneousRecord.create" />
		</button>
	</a>

</security:authorize> 


