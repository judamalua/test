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


<spring:message code="format.date" var="formatDate"/>
<spring:message code="format.price" var="formatPrice"/>


<jstl:if test = "${curriculum != null}">
<display:table name="curriculum" id="row"
	requestURI="${requestUri}" pagesize="${pagesize}" class="displaytag">

	<spring:message code="curriculum.ticker" var="ticker" />
	<display:column property="ticker" title="${ticker}" sortable="false" />

</display:table>


<jstl:if test="${curriculum!=null}">
	

		<h2><spring:message code="curriculum.personalRecord" /></h2>
		
		<h4><spring:message code="curriculum.personalRecord.nameOfCandidate" /></h4>
		<p>${curriculum.personalRecord.nameOfCandidate}</p>

		<h4><spring:message code="curriculum.personalRecord.photo"/></h4>
		<img class = "curriculumPhoto" src="${curriculum.personalRecord.photo}"/>

		<h4><spring:message code="curriculum.personalRecord.email" /></h4>
		<p>${curriculum.personalRecord.email}</p>

		<h4><spring:message code="curriculum.personalRecord.phoneNumber"/></h4>
		<p>${curriculum.personalRecord.phoneNumber}</p>

		<h4><spring:message code="curriculum.personalRecord.linkedInProfileURL" /></h4>
		<a href="${curriculum.personalRecord.linkedInProfileURL}">${curriculum.personalRecord.linkedInProfileURL}</a>
		<br/>

		<jstl:if test="${curriculumRanger}">
		<security:authorize access="hasRole('RANGER')">


			<a href="personalRecord/ranger/edit.do?personalRecordId=${personalRecord.id}">
				<spring:message code="curriculum.edit" />
			</a>
		

		</security:authorize>
		</jstl:if>

	
</jstl:if>



<jstl:if test="${curriculum!=null}">
	<jstl:if test="${not empty curriculum.professionalRecords}">
			<h2><spring:message code="curriculum.professionalRecords" /></h2>
			
		
			<jstl:set value="${professionalRecords}" var="professionalRecords"></jstl:set>

			<display:table name="professionalRecords" id="professionalRecord"
				requestURI="${requestUri}"
				pagesize="${pagesize}" class="displaytag">

				<spring:message code="professionalRecord.companyName"
					var="companyNameHeader" />
				<display:column property="companyName" title="${companyNameHeader}"
					sortable="true" />

				<spring:message
					code="curriculum.professionalRecord.workingPeriodStart"
					var="workingPeriodStartHeader" />
				<display:column property="workingPeriodStart"
					title="${workingPeriodStartHeader}" sortable="true" format="${formatDate}"/>

				<spring:message
					code="curriculum.professionalRecord.workingPeriodEnd"
					var="workingPeriodEndHeader" />
				<display:column property="workingPeriodEnd"
					title="${workingPeriodEndHeader}" sortable="true" format="${formatDate}"/>

				<spring:message code="curriculum.professionalRecord.role"
					var="roleHeader" />
				<display:column property="role" title="${roleHeader}"
					sortable="true" />

				<spring:message code="curriculum.professionalRecord.attachment"
					var="attachmentHeader" />
				<display:column>
				<a href="${professionalRecord.attachment}">${professionalRecord.attachment}</a>
				</display:column>

				<spring:message code="curriculum.professionalRecord.commentaries"
					var="commentariesHeader" />
				<display:column property="commentaries"
					title="${commentariesHeader}" sortable="false" />


				<security:authorize access="hasRole('RANGER')">
				<jstl:if test="${curriculum!=null&&curriculumRanger}">
					<display:column>
						<a
							href="professionalRecord/ranger/edit.do?professionalRecordId=${professionalRecord.id}">
							<spring:message code="curriculum.edit" />
						</a>
					</display:column>
				</jstl:if>
				</security:authorize>

			</display:table>
		
	</jstl:if>
</jstl:if>
<jstl:if test="${curriculum!=null&&curriculumRanger}">
<security:authorize access="hasRole('RANGER')">

	<a href="professionalRecord/ranger/create.do">
		<button>
			<spring:message code="curriculum.professionalRecord.create" />
		</button>
	</a>

</security:authorize>
</jstl:if>


<jstl:if test="${curriculum!=null}">
	<jstl:if test="${not empty miscellaneousRecords}">
	<h2><spring:message code="curriculum.miscellaneousRecords" /></h2>
		
		

			<display:table name="miscellaneousRecords" id="miscellaneousRecord"
				requestURI="${requestUri}"
				pagesize="${pagesize}" class="displaytag">

			<spring:message code="curriculum.miscellaneousRecord.title"
					var="title1Header" />
				<display:column property="title" title="${title1Header}"
					sortable="true" /> 

				<spring:message code="curriculum.miscellaneousRecord.attachment"
					var="attachment1Header" />
				<display:column>
				<a href="${miscellaneousRecord.attachment}">${miscellaneousRecord.attachment}</a>
				</display:column>

				<spring:message code="curriculum.miscellaneousRecord.commentaries"
					var="commentaries1Header" />
				<display:column property="commentaries"
					title="${commentaries1Header}" sortable="false" />


				<security:authorize access="hasRole('RANGER')">
				<jstl:if test="${curriculum!=null&&curriculumRanger}">
					<display:column>
						<a
							href="miscellaneousRecord/ranger/edit.do?miscellaneousRecordId=${miscellaneousRecord.id}">
							<spring:message code="curriculum.edit" />
						</a>
					</display:column>
				</jstl:if>
				</security:authorize>

			</display:table>
		
	</jstl:if>
</jstl:if>

<jstl:if test="${curriculum!=null&&curriculumRanger}">
<security:authorize access="hasRole('RANGER')">

	<a href="miscellaneousRecord/ranger/create.do">
		<button>
			<spring:message code="curriculum.miscellaneousRecord.create" />
		</button>
	</a>

</security:authorize> 
</jstl:if>

<jstl:if test="${curriculum!=null}">
	<jstl:if test="${not empty curriculum.educationRecords}">
		<security:authorize access="hasRole('RANGER')">
		<h2><spring:message code="curriculum.educationRecords" /></h2>

			<display:table name="educationRecords" id="educationRecord"
				requestURI="${requestUri}"
				pagesize="${pagesize}" class="displaytag">

				<spring:message code="curriculum.educationRecord.diplomaTitle"
					var="diplomaTitleHeader" />
				<display:column property="diplomaTitle"
					title="${diplomaTitleHeader}" sortable="true" />

				<spring:message
					code="curriculum.educationRecord.studyingPeriodStart"
					var="studyingPeriodStartHeader" />
				<display:column property="studyingPeriodStart"
					title="${studyingPeriodStartHeader}" sortable="true" />

				<spring:message code="curriculum.educationRecord.studyingPeriodEnd"
					var="studyingPeriodEndHeader" />
				<display:column property="studyingPeriodEnd"
					title="${studyingPeriodEndHeader}" sortable="true" />

				<spring:message code="curriculum.educationRecord.institution"
					var="institutionHeader" />
				<display:column property="institution"
					title="${institutionHeader}" sortable="true" />

				<spring:message code="curriculum.educationRecord.attachment"
					var="attachment2Header" />
				<display:column>
				<a href="${educationRecord.attachment}">${educationRecord.attachment}</a>
				</display:column>

				<spring:message code="curriculum.educationRecord.commentaries"
					var="commentaries2Header" />
				<display:column property="commentaries"
					title="${commentaries2Header}" sortable="false" />


				<security:authorize access="hasRole('RANGER')">
				<jstl:if test="${curriculum!=null&&curriculumRanger}">
					<display:column>
						<a
							href="educationRecord/ranger/edit.do?educationRecordId=${educationRecord.id}">
							<spring:message code="curriculum.edit" />
						</a>
					</display:column>
				</jstl:if>
				</security:authorize>

			</display:table>
		</security:authorize>
	</jstl:if>
</jstl:if>
<jstl:if test="${curriculum!=null&&curriculumRanger}">
<security:authorize access="hasRole('RANGER')">

	<a href="educationRecord/ranger/create.do">
		<button>
			<spring:message code="curriculum.educationRecord.create" />
		</button>
	</a>

</security:authorize>
</jstl:if>


<jstl:if test="${curriculum!=null}">
	<jstl:if test="${not empty curriculum.endorserRecords}">
	<h2><spring:message code="curriculum.endorserRecords" /></h2>

		
	
			<display:table name="endorserRecords" id="endorserRecord"
				requestURI="${requestUri}"
				pagesize="${pagesize}" class="displaytag">

				<spring:message code="curriculum.endorserRecord.fullName"
					var="fullNameHeader" />
				<display:column property="fullName" title="${fullNameHeader}"
					sortable="true" />

				<spring:message code="curriculum.endorserRecord.email"
					var="email1Header" />
				<display:column property="email" title="${email1Header}"
					sortable="true" />

				<spring:message code="curriculum.endorserRecord.phoneNumber"
					var="phoneNumber1Header" />
				<display:column property="phoneNumber" title="${phoneNumber1Header}"
					sortable="false" />


				<spring:message code="curriculum.endorserRecord.commentaries"
					var="commentaries3Header" />
				<display:column property="commentaries"
					title="${commentaries3Header}" sortable="false" />


				<security:authorize access="hasRole('RANGER')">
				<jstl:if test="${curriculum!=null&&curriculumRanger}">

					<display:column>
						<a
							href="endorserRecord/ranger/edit.do?endorserRecordId=${endorserRecord.id}">
							<spring:message code="curriculum.edit" />
						</a>
					</display:column>
				</jstl:if>
				</security:authorize>

			</display:table>
		
	</jstl:if>
</jstl:if>
<jstl:if test="${curriculum!=null&&curriculumRanger}">
<security:authorize access="hasRole('RANGER')">

	<a href="endorserRecord/ranger/create.do">
		<button>
			<spring:message code="curriculum.endorserRecord.create" />
		</button>
	</a>

</security:authorize>
</jstl:if>



<security:authorize access="hasRole('RANGER')">
<jstl:if test="${curriculum==null&&curriculumRanger}">

	<a href="curriculum/ranger/create.do">
		<button>
			<spring:message code="curriculum.create" />
		</button>
	</a>
</jstl:if>
</security:authorize>

<security:authorize access="hasRole('RANGER')">
<jstl:if test="${curriculum!=null&&curriculumRanger}">
	<a href="curriculum/ranger/delete.do?curriculumId=${curriculum.id}">
		<button onclick = "return validate('<spring:message code = "curriculum.confirm.phone"/>')">
			<spring:message code="curriculum.delete" />
		</button>
	</a>
			

</jstl:if>
</security:authorize>

</jstl:if>

