<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<meta charset="utf-8">
<meta name="renderer" content="webkit|ie-comp|ie-stand">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width,initial-scale=1,minimum-scale=1.0,maximum-scale=1.0,user-scalable=no" />
<meta http-equiv="Cache-Control" content="no-siteapp" />
<!--[if lt IE 9]>
<script type="text/javascript" src="${ctxStatic}/lib/html5.js"></script>
<script type="text/javascript" src="${ctxStatic}/lib/respond.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/lib/PIE_IE678.js"></script>
<![endif]-->
<link href="${ctxStatic}/css/H-ui.min.css" rel="stylesheet"
	type="text/css" />
<link href="${ctxStatic}/css/H-ui.login.css" rel="stylesheet"
	type="text/css" />
<link href="${ctxStatic}/css/style.css" rel="stylesheet" type="text/css" />
<link href="${ctxStatic}/lib/Hui-iconfont/1.0.6/iconfont.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var ctxStatic = "${ctxStatic}";
</script>
<!--[if IE 6]>
<script type="text/javascript" src="http://lib.h-ui.net/DD_belatedPNG_0.0.8a-min.js" ></script>
<script>DD_belatedPNG.fix('*');</script>
<![endif]-->
<script type="text/javascript"
	src="${ctxStatic}/lib/jquery/1.9.1/jquery.min.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/H-ui.js"></script>

<script type="text/javascript" src="${ctxStatic}/lib/layer/2.1/layer.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/H-ui.js"></script>
<script type="text/javascript" src="${ctxStatic}/js/H-ui.admin.js"></script>
<script type="text/javascript">
	$(function() {
		if (top != window) {
			window.top.location.href = "${ctx}";
		}

		if (!$("#username").val()) {
			$("#username").focus();
		} else {
			$("#password").focus();
		}

		$("#refreshImg").click(function() {
			var img = $("#kaptchaImg");
			var imageSrc = img.attr("src");
			if (imageSrc.indexOf("?") > 0) {
				imageSrc = imageSrc.substr(0, imageSrc.indexOf("?"));
			}
			imageSrc = imageSrc + "?" + new Date().getTime();
			img.attr("src", imageSrc);
		});
	});
</script>
<title>咪咕视讯后台管理</title>
</head>
<body>
	<input type="hidden" id="TenantId" name="TenantId" value="" />
	<div class="header"></div>
	<div class="loginWraper">
		<div id="loginform" class="loginBox">
			<form class="form form-horizontal" method="post">
				<div class="row cl">
					<label class="form-label col-3"><i class="Hui-iconfont">&#xe60d;</i></label>
					<div class="formControls col-8">
						<input id="username" name="username" value="<shiro:principal/>"
							type="text" placeholder="账户" class="input-text size-L">
					</div>
				</div>
				<div class="row cl">
					<label class="form-label col-3"><i class="Hui-iconfont">&#xe60e;</i></label>
					<div class="formControls col-8">
						<input id="password" name="password" type="password"
							placeholder="密码" class="input-text size-L">
					</div>
				</div>
				<c:if test="${kaptchaEnabled}">
					<div class="row cl">
						<div class="formControls col-8 col-offset-3">
							<input name="kaptchaCode" class="input-text size-L" type="text"
								placeholder="验证码"
								onclick="if(this.value=='验证码:'){this.value='';}"
								style="width:150px;"> <img id="kaptchaImg"
								src="${ctx}/kaptcha.jpg"> <a id="refreshImg"
								href="javascript:;">看不清，换一张</a>
						</div>
					</div>
				</c:if>
				<div class="row">
					<div class="formControls col-8 col-offset-3">
						<label for="rememberMe"> <input type="checkbox"
							name="rememberMe" id="rememberMe" value="true" checked="checked">
							使我保持登录状态
						</label>
					</div>
				</div>
				<div class="row">
					<div class="formControls col-8 col-offset-3">
						<input name="" type="submit" class="btn btn-success radius size-L"
							value="&nbsp;登&nbsp;&nbsp;&nbsp;&nbsp;录&nbsp;"> <input
							name="" type="reset" class="btn btn-default radius size-L"
							value="&nbsp;取&nbsp;&nbsp;&nbsp;&nbsp;消&nbsp;">
					</div>
				</div>
			</form>
		</div>
	</div>
	<div class="footer">
		<img style="width: 30px;" src="${ctxStatic}/images/cmvideo.png">&nbsp;&nbsp;&nbsp;&nbsp;Copyright
		&copy;2015~2016 咪咕视讯科技有限公司 All Rights Reserved.
	</div>
</body>
</html>