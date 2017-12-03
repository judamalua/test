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

<jstl:if test="${father!=null}">
	<jstl:if test="${ father.messageFolderFather==null}">
	<h2><a href="messageFolder/list.do">
		<jstl:out value="${father.name}" />
	</a></h2>
	</jstl:if>
	
	<jstl:if test="${ father.messageFolderFather!=null}">
	<h2><a href="messageFolder/list.do?messageFolderId=${father.messageFolderFather.id}">
		<jstl:out value="${father.name}" />
	</a></h2>
	</jstl:if>
</jstl:if>

<display:table name="messageFolders" id="messageFolder"
	requestURI="messageFolder/list.do" pagesize="10" class="displayTag">

	<spring:message code="messageFolder.name" var="name" />
	<display:column title="${name}" property="name" sortable="true" />

	<spring:message code="messageFolder.messageFolderChildren"
		var="messageFolderChildren" />
	<display:column title="${messageFolderChildren}">
		<a href="messageFolder/list.do?messageFolderId=${messageFolder.id}">
			<button>
				<spring:message code="messageFolder.messageFolderChildrenLink" />
			</button>
		</a>
	</display:column>

	<spring:message code="messageFolder.messages" var="messages" />
	<display:column title="${messages}">
		<a href="message/list.do?messageFolderId=${messageFolder.id}">
			<button>
				<spring:message code="messageFolder.messagesLink" />
			</button>
		</a>
	</display:column>
	<display:column>
		<jstl:if test="${!messageFolder.isDefault}">
			<a href="messageFolder/edit.do?messageFolderId=${messageFolder.id}">
				<button>
					<spring:message code="messageFolder.edit" />
				</button>
			</a>
		</jstl:if>
	</display:column>


</display:table>

<a href="messageFolder/create.do">
	<button>
		<spring:message code="messageFolder.create" />
	</button>
</a>