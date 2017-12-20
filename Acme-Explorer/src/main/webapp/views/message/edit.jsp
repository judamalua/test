<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<form:form action="message/edit.do"
	modelAttribute="modelMessage">
	
	<form:hidden path="id"/>
	<form:hidden path="version"/>
	<form:hidden path="receptionDate"/>
	<form:hidden path="sender"/>
	<form:hidden path="messageFolder"/>
	
	<spring:message code="message.priority.low" var="low"/>
	<spring:message code="message.priority.neutral" var="neutral"/>
	<spring:message code="message.priority.high" var="high"/>
	
	<form:label path="priority">
		<spring:message code="message.priority"/>
	</form:label>
	<form:select path="priority">
		<form:option title="${low}" value="LOW"/>
		<form:option title="${neutral}" value="NEUTRAL"/>
		<form:option title="${high}" value="HIGH"/>
	</form:select>
	<form:errors cssClass="error" path="priority"/>
	<br/>
	<br/>
	<br/>
	
	<form:label path="subject">
		<spring:message code="message.subject"/>
	</form:label>
	<form:input path="subject"/>
	<form:errors cssClass="error" path="subject"/>
	<br/>
	<br/>
	<br/>
	
	<form:label path="body">
		<spring:message code="message.body"/>
	</form:label>
	<form:textarea path="body"/>
	<form:errors cssClass="error" path="body"/>
	<br/>
	<br/>
	<br/>
	
	
	<form:label path="receiver">
		<spring:message code="message.receiver"/>
	</form:label>
	<form:select path="receiver">
		<jstl:forEach items="${actors}" var="actor">
			<jstl:if test="${actor.name!=\"SYSTEM\"}">
				<form:option label="${actor.email}" value="${actor.id}"/>
			</jstl:if>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="priority"/>
	
	<security:authorize access="hasRole('ADMIN')">
		<label>
			<spring:message code="message.broadcast" />
		</label>
		<input type="checkbox" id="broadcast" name="broadcast" />
		
		<br/>
		<br/>
		
	</security:authorize>
	
		<input 
		type="submit"
		name="save"
		value="<spring:message code="message.save" />">
	
	<input 
		type="button"
		name="cancel"
		value="<spring:message code="message.cancel" />"
		onclick="javascript: relativeRedir('/messageFolder/list.do')">
	
</form:form>
    