<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
		管理员管理 <span class="c-gray en">&gt;</span> 资源列表 <a
			class="btn btn-success radius r mr-20"
			style="line-height:1.6em;margin-top:3px"
			href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<div class="pd-20">
		<div class="cl pd-5 bg-1 bk-gray mt-20">
			<span class="l">
				<a href="javascript:;" onclick="opfn_add('添加子资源','${ctxPortal}/preAdd','800','500')" class="btn btn-primary radius">
					<i class="Hui-iconfont">&#xe600;</i>
					添加
				</a>
				<a id="btnEdit" href="javascript:;" onclick="opfn_edit('编辑资源','${ctxPortal}/preEdit','800','500')" class="btn radius">
					<i class="Hui-iconfont">&#xe6df;</i>
					查看/修改
				</a>
			</span>
		</div>
		<table class="table">
			<tr>
				<td width="200" class="va-t">
					<ul id="resourceTree" class="ztree"></ul>
				</td>
				<td class="va-t">
					<%--
					<IFRAME ID="testIframe" Name="testIframe" FRAMEBORDER=0 SCROLLING=AUTO width=100% height=390px SRC=""></IFRAME>
					--%>
				</td>
			</tr>
		</table>
	</div>
	<script type="text/javascript">
		$(function() {
			t = $.fn.zTree.init($("#resourceTree"), setting, getNodes());
		});

		var setting = {
			view: {
				showLine: false,
				selectedMulti: false
			},
			data: {
				simpleData: {
					enable:true,
					idKey: "id",
					pIdKey: "pId",
					rootPId: ""
				}
			}
		};

		function getNodes() {
			var zNodes =[];
			$.ajax({
				type	: "post",
				url		: "${ctxPortal}/listTreeData",
				data	: {},
				async	: false,
				success : function(data){  
					if(data && data.success) {
						$.each(data["data"],function(index, obj){
							zNodes.push({ id: obj.id, pId:obj.parentId, name:obj.name, open: true, editable: obj.editable});
						});
					}
				}
			});
			return zNodes;
		}

		/*
		 参数解释：
		 title	标题
		 url		请求的url
		 id		需要操作的数据id
		 w		弹出层宽度（缺省调默认值）
		 h		弹出层高度（缺省调默认值）
		 */
		/*增加*/
		function opfn_add(title, url, w, h) {
			var zTree = $.fn.zTree.getZTreeObj("resourceTree");
			var nodes = zTree.getSelectedNodes();
			if(!nodes.length) {
				layer.msg("请先选择需要添加子资源的父资源节点!", {
					icon : 2,
					time : 3000
				});
	            return;
	        }
			layer_show(title, url + "?parentId=" + nodes[0].id, w, h);
		}

		/*编辑*/
		function opfn_edit(title, url, w, h) {
			var resourceTree = $.fn.zTree.getZTreeObj("resourceTree");
			var nodes = resourceTree.getSelectedNodes();
			if(!nodes.length) {
				layer.msg("请先选择需要编辑的资源节点!", {
					icon : 2,
					time : 3000
				});
	            return;
	        }
			layer_show(title, url + "?id=" + nodes[0].id, w, h);
		}

		function opfn_refreshTree() {
			$.fn.zTree.init($("#resourceTree"), setting, getNodes());
		}
	</script>
</body>
</html>