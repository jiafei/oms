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
			<input type="hidden" name="parentId" value="${obj.id}">
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>父资源名：</label>
				<div class="formControls col-5">
					<input type="text" id="parentName" name="parentName"
						value="${obj.name}" class="input-text" placeholder="" readonly="readonly">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>资源名：</label>
				<div class="formControls col-5">
					<input type="text" id="name" name="name"
						value="" class="input-text" placeholder=""
						datatype="*" nullmsg="资源名不能为空">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>资源标识：</label>
				<div class="formControls col-5">
					<input type="text" id="identity" name="identity"
						value="" class="input-text" placeholder=""
						datatype="*" nullmsg="资源标识不能为空">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>是否菜单资源：</label>
				<div class="formControls col-5 skin-minimal">
					<div class="radio-box">
						<input type="radio" id="isMenu-1" name="isMenu" value="1" datatype="*" nullmsg="资源标识不能为空">
						<label for="isMenu-1">是</label>
					</div>
					<div class="radio-box">
						<input type="radio" id="isMenu-0" name="isMenu" value="0">
						<label for="isMenu-0">否</label>
					</div>
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3"><span class="c-red">*</span>排序权重：</label>
				<div class="formControls col-5">
					<input type="text" id="weight" name="weight"
						value="" class="input-text" placeholder=""
						datatype="n">
				</div>
				<div class="col-4"></div>
			</div>
			<div class="row cl">
				<label class="form-label col-3">URL地址：</label>
				<div class="formControls col-5">
					<input type="text" id="url" name="url"
						value="" class="input-text" placeholder="">
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

			$("#form-add").Validform({
				tiptype : 2,
				callback : function(form) {
					$.post("${ctxPortal}/add", form.serialize(), function(data) {
						if(data && data.success) {
							// 刷新父窗体数据
							parent.opfn_refreshTree();
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