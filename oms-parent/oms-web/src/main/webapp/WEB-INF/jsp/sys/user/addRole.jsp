<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<div class="pd-20">
		<form action="${ctxPortal}/addRole" method="post"
			class="form form-horizontal" id="form-add">
			<input type="hidden" id="id" name="id" value="${obj.id}">
			<input type="hidden" name="user.id" value="${user.id}">
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>用户：</label>
				<div class="formControls col-5">
					<label>${user.username}</label>
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>角色：</label>
				<div class="formControls col-8">
					<c:forEach var="role" items="${roleList}">
						&nbsp;
						<label>
							<input type="checkbox" value="${role.id}" name="roleIds" id="roles_${role.id}">
							${role.name}
						</label>
					</c:forEach>
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

			<c:if test="${not empty obj}">
				<c:forEach var="pid" items="${obj.roleIds}">
					$("#roles_${pid}").prop("checked","checked");
				</c:forEach>
			</c:if>

			$("#form-add").Validform({
				tiptype : 2,
				callback : function(form) {
					// 获取选中的权限数量
					var checkedCount = $("input[name='roleIds']:checked").length;
					if(!checkedCount || checkedCount==0) {
						layer.msg("需要至少选择一个权限！", {
							icon : 2,
							time : 5000
						});
						return false;
					}

					// 准备判断资源是否已关联到角色
					var checkExist = false;
					$.ajax({
						type	: "post",
						url		: "${ctxPortal}/checkExist",
						data	: {roleId:$("#role\\.id").val(),resourceId:$("#resource\\.id").val()},
						async	: false,
						success : function(data){  
							if(!data || !data.success) {
								checkExist = true;
							}
							layer.msg(data.message, {
								icon : 2,
								time : 5000
							});
						}
					});
					if(checkExist) {
						return false;
					}
					$.post("${ctxPortal}/addRole", form.serialize(), function(data) {
						if(data && data.success) {
							parent.$("#table-list").DataTable().ajax.reload();
							var index = parent.layer.getFrameIndex(window.name);
							parent.layer.msg("为用户分配角色成功！", {
								icon : 1,
								time : 1000
							});
							// 必须在提交表单后再关闭层，如果不用ajax方式而是form.submit后就layer.close，表单提交将会被中断。
							parent.layer.close(index);
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