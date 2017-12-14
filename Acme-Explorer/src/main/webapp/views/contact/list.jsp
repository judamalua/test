<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security"	uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<display:table name="contacts" id="contact" requestURI="contact/explorer/list.do" pagesize="${pagesize}" class="displayTag">
	
	<spring:message code="contact.name" var="name"/>
	<display:column property="name" title="${name}" sortable="true"/>

	<spring:message code="contact.email" var="email"/>
	<display:column property="email" title="${email}" sortable="false"/>
	
	<spring:message code="contact.phoneNumber" var="phoneNumber"/>
	<display:column property="phoneNumber" title="${phoneNumber}" sortable="false"/>
	
	<display:column>
		<a href="contact/explorer/edit.do?contactId=${contact.id}">
			<spring:message code="contact.edit"/>
		</a>
	</display:column>
	
</display:table>

	<a href = "contact/explorer/create.do">
	<button>
		<spring:message code = "contact.create"/>
	</button>
	</a>

