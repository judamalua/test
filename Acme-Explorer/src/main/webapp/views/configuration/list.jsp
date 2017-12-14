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


<jsp:useBean id="now" class="java.util.Date" />
<fmt:formatDate var="currentDate" value="${now}"
	pattern="yyyy-MM-dd HH:mm" />
<spring:message code="format.date" var="formatDate" />


<p>
	<b><spring:message code="configuration.vat" /></b> :
	<jstl:out value="${configuration.vat}" />
</p>

<p>
	<b><spring:message code="configuration.searchTimeout" /></b> :
	<jstl:out value="${configuration.searchTimeout}" />
</p>


<p>
	<b><spring:message code="configuration.spamWords" /></b> :
<ul>
	<jstl:forEach var="spamWord" items="${configuration.spamWords}">
		<li><jstl:out value="${spamWord}" /></li>
	</jstl:forEach>
</ul>


<p>
	<b><spring:message code="configuration.bannerUrl" /></b> :
	<jstl:out value="${configuration.bannerUrl}" />
</p>


<p>
	<b><spring:message code="configuration.welcomeMessageEng" /></b> :
	<jstl:out value="${configuration.welcomeMessageEng}" />
</p>


<p>
	<b><spring:message code="configuration.welcomeMessageEsp" /></b> :
	<jstl:out value="${configuration.welcomeMessageEsp}" />
</p>


<p>
	<b><spring:message code="configuration.defaultPhoneCountryCode" /></b>
	:
	<jstl:out value="${configuration.defaultPhoneCountryCode}" />
</p>


<p>
	<b><spring:message code="configuration.maxResults" /></b> :
	<jstl:out value="${configuration.maxResults}" />
</p>

<a href="configuration/admin/edit.do">
	<button>
		<spring:message code="configuration.edit" />
	</button>
</a>
