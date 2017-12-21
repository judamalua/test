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
	<display:column property="tagValue.value"/>
	
	<spring:message code="tagValue.tag" />
	<display:column property="tagValue.tag.name"/>
		
</display:table>

	<a href = "tag/admin/create.do">
	<button>
		<spring:message code = "tag.create"/>
	</button>
	</a>
