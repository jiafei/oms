<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<%@ include file="/common/meta.jsp"%>
<title>我的桌面</title>
</head>
<body>
	<sitemesh:write property="body" />
	<footer class="footer">
		<p>
			Copyright &copy;2016  咪咕视讯科技有限公司 All Rights Reserved.
			<%--
				<br> 本后台系统由<a
				href="http://www.h-ui.net/" target="_blank" title="H-ui前端框架">H-ui前端框架</a>提供前端技术支持
			--%>
		</p>
	</footer>
</body>
</html>