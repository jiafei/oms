<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
<title>咪咕视讯订单管理系统后台管理</title>
<%@ include file="/common/meta.jsp"%>
<sitemesh:write property="head" />
</head>
<body>
	<header class="Hui-header cl">
		<a class="Hui-logo l" title="咪咕视讯订单管理系统后台管理" href="${ctx}">咪咕视讯</a>
		<a class="Hui-logo-m l" href="/" title="咪咕视讯">cmvideo</a> <span
			class="Hui-subtitle l">订单管理系统后台管理</span>
		<%-- 快捷菜单 --%>
		<%--
		<nav class="mainnav cl" id="Hui-nav">
			<ul>
				<li class="dropDown dropDown_click"><a href="javascript:;"
					aria-expanded="true" aria-haspopup="true" data-toggle="dropdown"
					class="dropDown_A"><i class="Hui-iconfont">&#xe600;</i> 新增 <i
						class="Hui-iconfont">&#xe6d5;</i></a>
					<ul class="dropDown-menu radius box-shadow">
						<li><a href="javascript:;"
							onclick="member_add('添加用户','member-add.html','','510')"><i
								class="Hui-iconfont">&#xe60d;</i> 用户</a></li>
					</ul>
				</li>
			</ul>
		</nav>
		--%>
		<ul class="Hui-userbar">
			<li><%-- 可显示用户所属部门等必要信息 --%></li>
			<li class="dropDown dropDown_hover">
				<a href="#" class="dropDown_A">
					<shiro:principal/><i class="Hui-iconfont">&#xe6d5;</i>
				</a>
				<ul class="dropDown-menu radius box-shadow">
					<li><a href="#" onclick="layer_show('个人信息管理', '${ctx}/sys/user/preInfoEdit?username=<shiro:principal/>', '800', '500')">个人信息</a></li>
					<li><a href="${ctx}/logout">退出</a></li>
				</ul>
			</li>
			<%--
			<li id="Hui-msg"><a href="#" title="消息"><span
					class="badge badge-danger">1</span><i class="Hui-iconfont"
					style="font-size:18px">&#xe68a;</i></a></li>
			--%>
			<li id="Hui-skin" class="dropDown right dropDown_hover"><a
				href="javascript:;" title="换肤"><i class="Hui-iconfont"
					style="font-size:18px">&#xe62a;</i></a>
				<ul class="dropDown-menu radius box-shadow">
					<li><a href="javascript:;" data-val="default" title="默认（黑色）">默认（黑色）</a></li>
					<li><a href="javascript:;" data-val="blue" title="蓝色">蓝色</a></li>
					<li><a href="javascript:;" data-val="green" title="绿色">绿色</a></li>
					<li><a href="javascript:;" data-val="red" title="红色">红色</a></li>
					<li><a href="javascript:;" data-val="yellow" title="黄色">黄色</a></li>
					<li><a href="javascript:;" data-val="orange" title="绿色">橙色</a></li>
				</ul></li>
		</ul>
		<a href="javascript:;" class="Hui-nav-toggle Hui-iconfont"
			aria-hidden="false">&#xe667;</a>
	</header>
	<aside class="Hui-aside">
		<input runat="server" id="divScrollValue" type="hidden" value="" />
		<div class="menu_dropdown bk_2">
			<c:forEach items="${menus}" var="m">
				<dl id="menu-${m.identity}">
					<dt>
						<i class="Hui-iconfont">&#xe616;</i>
						${m.name}<i class="Hui-iconfont menu_dropdown-arrow">&#xe6d5;</i>
					</dt>
					<dd>
						<ul>
							<c:forEach items="${m.children}" var="mc">
								<li>
									<a _href="${ctx}${mc.url}" data-title="${mc.name}"
									href="javascript:void(0)">${mc.name}</a>
								</li>
							</c:forEach>
						</ul>
					</dd>
				</dl>
			</c:forEach>
		</div>
	</aside>
	<div class="dislpayArrow">
		<a class="pngfix" href="javascript:void(0);"
			onClick="displaynavbar(this)"></a>
	</div>
	<section class="Hui-article-box">
		<div id="Hui-tabNav" class="Hui-tabNav">
			<div class="Hui-tabNav-wp">
				<ul id="min_title_list" class="acrossTab cl">
					<li class="active"><span title="我的桌面" data-href="welcome.html">我的桌面</span><em></em></li>
				</ul>
			</div>
			<div class="Hui-tabNav-more btn-group">
				<a id="js-tabNav-prev" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d4;</i></a><a
					id="js-tabNav-next" class="btn radius btn-default size-S"
					href="javascript:;"><i class="Hui-iconfont">&#xe6d7;</i></a>
			</div>
		</div>
		<div id="iframe_box" class="Hui-article">
			<div class="show_iframe">
				<div style="display:none" class="loading"></div>
				<iframe scrolling="yes" frameborder="0" src="${ctx}/index"></iframe>
			</div>
		</div>
	</section>
	<script type="text/javascript">
		/*资讯-添加*/
		function article_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*图片-添加*/
		function picture_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*产品-添加*/
		function product_add(title, url) {
			var index = layer.open({
				type : 2,
				title : title,
				content : url
			});
			layer.full(index);
		}
		/*用户-添加*/
		function member_add(title, url, w, h) {
			layer_show(title, url, w, h);
		}
	</script>
</body>
</html>