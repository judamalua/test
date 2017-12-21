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

<form:form action="trip/manager/edit.do" modelAttribute="trip">

	<form:hidden path="id" />
	<form:hidden path="version" />
	<form:hidden path="sponsorships" />
	<form:hidden path="stories" />
	<form:hidden path="stages" />
	<form:hidden path="survivalClasses" />
	<form:hidden path="cancelReason" />
	<form:hidden path="auditRecords" />
	<form:hidden path="tagValues" />
	<form:hidden path="notes" />
	<form:hidden path="applications" />
	<form:hidden path="managers" />
	<form:hidden path="price" value="0." />
	<form:hidden path="ticker" value="${trip.ticker}" />


	<form:label path="title">
		<spring:message code="trip.title" />
	</form:label>
	<form:input path="title" />
	<form:errors cssClass="error" path="title" />
	<br />

	<form:label path="description">
		<spring:message code="trip.description" />
	</form:label>
	<form:textarea path="description" />
	<form:errors cssClass="error" path="description" />
	<br />

	<form:label path="requirements">
		<spring:message code="trip.requirements" />
	</form:label>
	<form:textarea path="requirements" />
	<form:errors cssClass="error" path="requirements" />
	<br />

	<form:label path="startDate">
		<spring:message code="trip.startDate" />
	</form:label>
	<jsp:useBean id="now" class="java.util.Date" />
	<fmt:formatDate var="currentDate" value="${now}"
		pattern="dd/MM/yyyy HH:mm" />

	<!-- 	<div class="input-append date" id="datetimepicker" data-date="12/02/2017 hh:mm" data-date-format="dd/MM/yyyy hh:mm"> -->
	<form:input path="startDate" id="startDate"
		placeholder="dd/MM/yyyy hh:mm" />
	<!--     	<span class="add-on"><i class="icon-th"></i></span> -->
	<!-- 	</div> -->
	<form:errors cssClass="error" path="startDate" />
	<br />

	<form:label path="endDate">
		<spring:message code="trip.endDate" />
	</form:label>
	<!-- 	<div class="input-append date" id="datetimepicker" data-date="12/02/2017 hh:mm" data-date-format="dd/MM/yyyy hh:mm"> -->
	<form:input path="endDate" id="endDate" placeholder="dd/MM/yyyy hh:mm" />
	<!--     	<span class="add-on"><i class="icon-th"></i></span> -->
	<!-- 	</div> -->
	<form:errors cssClass="error" path="endDate" />
	<br />

	<form:label path="publicationDate">
		<spring:message code="trip.publicationDate" />
	</form:label>
	<!-- 	<div class="input-append date" id="datetimepicker" data-date="12/02/2017 hh:mm" data-date-format="dd/MM/yyyy hh:mm"> -->
	<form:input path="publicationDate" id="publicationDate"
		placeholder="dd/MM/yyyy hh:mm" />
	<!--     	<span class="add-on"><i class="icon-th"></i></span> -->
	<!-- 	</div> -->
	<form:errors cssClass="error" path="publicationDate" />
	<br />

	<form:label path="ranger">
		<spring:message code="trip.ranger" />
	</form:label>
	<form:select path="ranger">
		<form:option value="0">
				----------
			</form:option>
		<jstl:forEach var="ranger" items="${rangers}">
			<!-- Variable del controlador -->
			<form:option value="${ranger.id}">
				<jstl:out value="${ranger.name}" />
			</form:option>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="ranger" />
	<br />

	<form:label path="legalText">
		<spring:message code="trip.legalText" />
	</form:label>
	<form:select path="legalText">
		<form:option value="0">
			----------
		</form:option>
		<jstl:forEach var="legalText" items="${legalTexts}">
			<!-- Variable del controlador -->
			<form:option value="${legalText.id}">
				<jstl:out value="${legalText.title}" />
			</form:option>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="legalText" />
	<br />

<%-- 	<form:label path="tags"> --%>
<%-- 		<spring:message code="trip.tags" /> --%>
<%-- 	</form:label> --%>
<%-- 	<form:select multiple="true" path="tags"> --%>
<%-- 			<form:option value="0" selected="selected"> --%>
<!-- 				----------------- -->
<%-- 			</form:option> --%>
<%-- 		<jstl:forEach items="${tagsTrip}" var="tagTrip" > --%>
			<!-- Variable del controlador --> 
			
<%-- 			<form:option value="${tagTrip.id}"> --%>
<%-- 				<jstl:out value="${tagTrip.name}" /> --%>
<%-- 			</form:option> --%>
<%-- 		</jstl:forEach> --%>
<%-- 	</form:select> --%>
<%-- 	<form:errors cssClass="error" path="tags" /> --%>
<!-- <br /> -->



	<%-- 	<form:label path="survivalClasses"> --%>
	<%-- 		<spring:message code="trip.survivalClasses"/> --%>
	<%-- 	</form:label> --%>
	<%-- 	<form:select multiple="true"  path="survivalClasses"> --%>
	<%-- 	<jstl:forEach var="survivalClass" items="${trip.survivalClasses}"> <!-- Variable del controlador --> --%>
	<%-- 			<form:option selected="true" value="${survivalClass.id}"> --%>
	<%-- 				<jstl:out value="${survivalClass.title}"/>  --%>
	<%-- 			</form:option> --%>
	<%-- 		</jstl:forEach> --%>
	<%-- 		<jstl:forEach var="survivalClass" items="${survivalClasses}"> <!-- Variable del controlador --> --%>
	<%-- 			<form:option value="${survivalClass.id}"> --%>
	<%-- 				<jstl:out value="${survivalClass.title}"/>  --%>
	<%-- 			</form:option> --%>
	<%-- 		</jstl:forEach> --%>
	<%-- 	</form:select>	 --%>
	<%-- 	<form:errors cssClass = "error" path = "survivalClasses" /> --%>
	 	<!-- <br /> -->

	<form:label path="category">
		<spring:message code="trip.category" />
	</form:label>
	<form:select multiple="true" path="category">
		<jstl:forEach var="category" items="${categories}">
			<!-- Variable del controlador -->
			<jstl:if test="${category.name!=\"CATEGORY\"}">
				<form:option value="${category.id}">
					<jstl:out value="${category.name}" />
				</form:option>
			</jstl:if>
		</jstl:forEach>
	</form:select>
	<form:errors cssClass="error" path="category" />
	<br />

	<jstl:if test="${trip.id==0}">
		<h3>
			<spring:message code="trip.stages" />
		</h3>
	
		<spring:message var="inputError" code="stage.input.error"/>
		<label for="titleStage">
			 <spring:message code="stage.title" />
		</label>
		
		<input type="text" name="titleStage" required="required" 
		 oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}"/>

		<label for="priceStage"> <spring:message code="stage.price" />
		</label>
		<input type="number" name="priceStage" required="required" step="any"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}"/> &euro;

		<label for="descriptionStage"> 
		<spring:message code="stage.description" />
		</label>
		<textarea name="descriptionStage"
		required="required"
		oninvalid="setCustomValidity('${inputError}')"
    	onchange="try{setCustomValidity('')}catch(e){}"></textarea>
	</jstl:if>
	<br />
	<input type="submit" name="save"
		value="<spring:message code="trip.save" />">

	<jstl:if test="${trip.id!=0}">
		<input type="submit" name="delete"
			value="<spring:message code="trip.delete" />"
			onclick="return confirm('<spring:message code='stage.confirm.delete' />') " />
	</jstl:if>

	<input type="button" name="cancel"
		value="<spring:message code="trip.cancel" />"
		onclick="javascript: relativeRedir('trip/manager/list.do')">


</form:form>


