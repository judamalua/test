<%--
 * 403.jsp
 *
 * Copyright (C) 2017 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the 
 * TDG Licence, a copy of which you may download from 
 * http://www.tdg-seville.info/License.html
 --%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@taglib prefix="jstl"	uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@taglib prefix="security" uri="http://www.springframework.org/security/tags"%>
<%@taglib prefix="display" uri="http://displaytag.sf.net"%>

<img src="https://media.giphy.com/media/5Zesu5VPNGJlm/200.gif"/>
<p>Oops! It appears that you don't have access to this resource.</p>
<p>If this is an error, our team of highly trained monkeys are trying to repair this problem right now.</p>
<p>Thank you for using this service.</p>

<p><a href="<spring:url value='/' />">Return to the welcome page</a><p>
