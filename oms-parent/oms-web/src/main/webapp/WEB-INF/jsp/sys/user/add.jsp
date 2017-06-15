<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<div class="pd-20">
		<form action="${ctxPortal}/add" method="post"
			class="form form-horizontal" id="form-add">
			<input type="hidden" name="id" value="${obj.id}">
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>管理员：</label>
				<div class="formControls col-5">
					<input type="text" id="username" name="username"
						value="${obj.username}" class="input-text" placeholder=""
						datatype="*2-16" nullmsg="用户名不能为空">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>初始密码：</label>
				<div class="formControls col-5">
					<input type="password" id="newpassword" name="newpassword"
						value="${obj.password}" placeholder="密码" autocomplete="off"
						class="input-text" datatype="*6-32" nullmsg="密码不能为空">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>确认密码：</label>
				<div class="formControls col-5">
					<input type="password" id="password" name="password"
						value="${obj.password}" placeholder="确认新密码" autocomplete="off"
						class="input-text" errormsg="您两次输入的新密码不一致！" datatype="*"
						nullmsg="请再输入一次新密码！" recheck="newpassword">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>手机：</label>
				<div class="formControls col-5">
					<input type="text" id="mobilePhone" name="mobilePhone"
						value="${obj.mobilePhone}" class="input-text" placeholder=""
						datatype="m" nullmsg="手机不能为空">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>邮箱：</label>
				<div class="formControls col-5">
					<input type="text" id="email" name="email" value="${obj.email}"
						class="input-text" placeholder="@" datatype="e" nullmsg="请输入邮箱！">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<div class="col-9 col-offset-3">
					<input class="btn btn-primary radius" type="submit"
						value="&nbsp;&nbsp;提交&nbsp;&nbsp;">
				</div>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		$(function() {
			$('.skin-minimal input').iCheck({
				checkboxClass : 'icheckbox-blue',
				radioClass : 'iradio-blue',
				increaseArea : '20%'
			});

			// 修改用户时不允许修改用户名
			<c:if test="${not empty obj.username}">
				$("#username").attr("readonly","readonly");
			</c:if>

			$("#form-add").Validform({
				tiptype : 2,
				callback : function(form) {
					$.post("${ctxPortal}/add", form.serialize(), function(data) {
						if(data && data.success) {
							parent.$("#table-list").DataTable().ajax.reload();
							var index = parent.layer.getFrameIndex(window.name);
							parent.layer.msg(data.message, {
								icon : 1,
								time : 1000
							});
							// 必须在提交表单后再关闭层，如果不用ajax方式而是form.submit后就layer.close，表单提交将会被中断。
							parent.layer.close(index);
						} else {
							parent.layer.msg(data.message, {
								icon : 2,
								time : 1000
							});
						}
					});
					// I have submit manually,so return false here,if return true, will trigger submit again!
					// 如果return true，将会提交表单，但我已经用异步方式做提交了，所以return false。
					return false;
				}
			});
		});
	</script>
</body>
</html>