<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<%
		long start = System.currentTimeMillis();
	%>
	<div class="pd-20" style="padding-top:20px;">
		<p class="f-20 text-success"></p>
		<!-- <p>登录次数：18</p> -->
		<!-- <p>上次登录IP：xxx.xxx.xxx.xxx 上次登录时间：2016-1-1 12:12:12</p> -->
		<%@include file="runtime.jsp"%>
		<%@include file="OS.jsp"%>
		<%@include file="memory.jsp"%>
		<p>
			Execute Cost Time:
			<%=System.currentTimeMillis() - start%>
			ms.
		</p>
	</div>
</body>