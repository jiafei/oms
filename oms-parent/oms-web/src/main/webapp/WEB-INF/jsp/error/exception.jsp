<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>

<div class="panel">
	<br />
	<c:set var="stackTrace" value="${error.stackTrace}" />
	<%@include file="exceptionDetails.jsp"%>
</div>