<%--
 * header.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page import="org.springframework.beans.factory.annotation.Autowired"%>
<%@page import="services.ConfigurationService"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>

<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="security"
	uri="http://www.springframework.org/security/tags"%>

<div>
	<img src="${banner}" alt="Acme Co., Inc." />
</div>

<div>
	<ul id="jMenu">
		<!-- Do not forget the "fNiv" class for the first level links !! -->

		<security:authorize access="hasRole('EXPLORER')">
			<li><a class="fNiv"><spring:message
						code="master.page.explorer" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="application/explorer/list.do"><spring:message
								code="master.page.applicationList" /></a></li>
					<%-- 					<li><a href="trip/list.do"><spring:message --%>
					<%-- 								code="master.page.tripList" /></a></li> --%>
					<li><a href="contact/explorer/list.do"><spring:message code="master.page.contacts" /></a></li>
					<li><a href="survivalClass/explorer/list-joined.do"><spring:message
								code="master.page.survivalClassList" /></a></li>
					<li><a href="actor/explorer/edit.do"><spring:message
								code="master.page.actorEdit" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('RANGER')">
			<li><a class="fNiv"><spring:message
						code="master.page.ranger" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="trip/list.do"><spring:message
								code="master.page.tripList" /></a></li>
					<%-- 					<li><a href="category/list.do"><spring:message --%>
					<%-- 								code="master.page.categoryList" /></a></li> --%>
					<li><a href="curriculum/ranger/list.do"><spring:message
								code="master.page.curriculumList" /></a></li>
					<li><a href="actor/ranger/edit.do"><spring:message
								code="master.page.actorEdit" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('SPONSOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.sponsor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="trip/list.do"><spring:message
								code="master.page.tripList" /></a></li>
					<%-- 					<li><a href="category/list.do"><spring:message --%>
					<%-- 								code="master.page.categoryList" /></a></li> --%>
					<li><a href="sponsorship/sponsor/list.do"><spring:message
								code="master.page.sponsorshipList" /></a></li>
					<li><a href="actor/sponsor/edit.do"><spring:message
								code="master.page.actorEdit" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('MANAGER')">
			<li><a class="fNiv"><spring:message
						code="master.page.manager" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="trip/manager/list.do"><spring:message
								code="master.page.tripList" /></a></li>
					<li><a href="application/manager/list.do"><spring:message
								code="master.page.applicationList" /></a></li>
					<li><a href="auditRecord/manager/list.do"><spring:message
								code="master.page.auditRecordList" /></a></li>
					<li><a href="survivalClass/manager/list-managed.do"><spring:message
								code="master.page.survivalClassListManaged" /></a></li>
					<%-- 					<li><a href="category/list.do"><spring:message --%>
					<%-- 								code="master.page.categoryList" /></a></li> --%>
					<li><a href="actor/manager/edit.do"><spring:message
								code="master.page.actorEdit" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="hasRole('AUDITOR')">
			<li><a class="fNiv"><spring:message
						code="master.page.auditor" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="trip/list.do"><spring:message
								code="master.page.tripList" /></a></li>
					<li><a href="category/list.do"><spring:message
								code="master.page.categoryList" /></a></li>
					<li><a href="auditRecord/auditor/list.do"><spring:message
								code="master.page.auditRecordList" /></a></li>
					<li><a href="note/auditor/list.do"><spring:message
								code="master.page.noteList" /></a></li>
					<li><a href="actor/auditor/edit.do"><spring:message
								code="master.page.actorEdit" /></a></li>
				</ul></li>
		</security:authorize>
		<security:authorize access="hasRole('ADMIN')">
			<li><a class="fNiv"><spring:message code="master.page.admin" /></a>
				<ul>
					<li class="arrow"></li>
					<li><a href="actor/admin/list-suspicious.do"><spring:message
								code="master.page.list.suspicious" /></a></li>
					<li><a href="trip/list.do"><spring:message
								code="master.page.tripList" /></a></li>
					<li><a href="category/list.do"><spring:message
								code="master.page.categoryList" /></a></li>
					<li><a href="search/admin/list.do"><spring:message
								code="master.page.searchs" /></a></li>
					<li><a href="configuration/admin/list.do"><spring:message
								code="master.page.configurationList" /></a></li>
				</ul></li>
		</security:authorize>

		<security:authorize access="isAnonymous()">
			<li><a class="fNiv" href="security/login.do"><spring:message
						code="master.page.login" /></a></li>
			<li><a class="fNiv" href="actor/register-explorer.do"><spring:message
						code="master.page.register.explorer" /></a></li>
			<li><a class="fNiv" href="actor/register-ranger.do"><spring:message
						code="master.page.register.ranger" /></a></li>
			<li><a class="fNiv" href="trip/list.do"><spring:message
						code="master.page.tripList" /></a></li>
			<li><a href="category/list.do"><spring:message
						code="master.page.categoryList" /></a></li>
		</security:authorize>

		<security:authorize access="isAuthenticated()">
			<li><a class="fNiv"> <spring:message
						code="master.page.profile" /> (<security:authentication
						property="principal.username" />)
			</a>
				<ul>
					<li class="arrow"></li>
					<li><a href="j_spring_security_logout"><spring:message
								code="master.page.logout" /> </a></li>
				</ul></li>

			<li><a href="messageFolder/list.do"><spring:message
						code="master.page.actorMessageFolder" /></a></li>
			<li><a href="category/list.do"><spring:message
						code="master.page.categoryList" /></a></li>
			<li><a class="fNiv" href="trip/list.do"><spring:message
						code="master.page.tripList" /></a></li>

		</security:authorize>

	</ul>
</div>

<div>
	<a href="?language=en">en</a> | <a href="?language=es">es</a>
</div>

