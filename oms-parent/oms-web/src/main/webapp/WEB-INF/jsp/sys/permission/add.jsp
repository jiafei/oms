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
				<label class="form-label col-3"><span class="c-red">*</span>权限名：</label>
				<div class="formControls col-5">
					<input type="text" id="name" name="name"
						value="${obj.name}" class="input-text" placeholder=""
						datatype="*" nullmsg="权限名不能为空">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>权限标识：</label>
				<div class="formControls col-5">
					<input type="text" id="identity" name="identity"
						value="${obj.identity}" class="input-text" placeholder=""
						datatype="*" nullmsg="权限标识不能为空">
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

			<c:if test="${not empty obj.identity}">
				$("#identity").attr("readonly","readonly");
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