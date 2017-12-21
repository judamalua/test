<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="socialIdentities" id="socialIdentity" requestURI="socialIdentity/list.do" pagesize="${pagesize}" class="displayTag">
	
	<spring:message code="socialIdentity.nick" var="nick"/>
	<display:column property="nick" title="${nick}" sortable="true"/>
	
	<spring:message code="socialIdentity.name" var="name"/>
	<display:column property="name" title="${name}" sortable="true"/>
	
	<spring:message code="socialIdentity.profileLink" var="profileLink"/>
	<display:column>
	<a href="${socialIdentity.profileLink}">${socialIdentity.profileLink}</a>
	</display:column>
	<spring:message code="socialIdentity.photoUrl" var="photoUrl"/>
	<display:column>
	<img src="${row.photoURL}"/>
	</display:column>
	
	<display:column>
		<a href="socialIdentity/edit.do?socialIdentityId=${socialIdentity.id}">
			<spring:message code="socialIdentity.edit"/>
		</a>
	</display:column>
	
</display:table>

	<a href = "socialIdentity/create.do">
	<button>
		<spring:message code = "socialIdentity.create"/>
	</button>
	</a>

