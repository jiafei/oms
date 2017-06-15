<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
		管理员管理 <span class="c-gray en">&gt;</span> 角色列表 <a
			class="btn btn-success radius r mr-20"
			style="line-height:1.6em;margin-top:3px"
			href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<div class="pd-20">
		<!-- <div class="text-c">
			<form class="Huiform" method="post" action="" target="_self">
				<button type="submit" class="btn btn-success" id="" name="">
					<i class="Hui-iconfont">&#xe665;</i> 搜索
				</button>
			</form>
		</div> -->
		<div class="cl pd-5 bg-1 bk-gray mt-20">
			<span class="l">
				<%--
				<a href="javascript:;" onclick="datadel()" class="btn btn-danger radius">
					<i class="Hui-iconfont">&#xe6e2;</i>批量删除
				</a>
				--%>
				<a href="javascript:;" onclick="opfn_add('添加','${ctxPortal}/preAdd','800','500')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i>
					添加
				</a>
			</span>
		</div>
		<table id="table-list"
			class="table table-border table-bordered table-bg">
			<thead>
				<tr>
					<th scope="col" colspan="9">列表</th>
				</tr>
				<tr class="text-c">
					<th width="25"><input type="checkbox" name="" value=""></th>
					<!-- <th width="40">ID</th> -->
					<th width="150">角色名</th>
					<th width="90">角色标识</th>
					<th width="90">角色状态</th>
					<th width="90">角色描述</th>
					<th width="100">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<script type="text/javascript">
		$(function() {
			/* $("#table-list").dataTable({
				"aaSorting": [[ 1, "desc" ]],//默认第几个排序
				"bStateSave": true,//状态保存
				"aoColumnDefs": [
				  //{"bVisible": false, "aTargets": [ 3 ]} //控制列的隐藏显示
				  {"orderable":false,"aTargets":[0,8,9]}// 制定列不参与排序
				]
			}); */
			$("#table-list").dataTable(dataTable_config);
		});

		function retrieveData(sSource, aoData, fnCallback, oSettings) {
	        $.ajax( {
	            type: "POST",
	            url: sSource,
	            dataType:"json",
	            data: "jsonParam="+JSON.stringify(aoData),
	            success: function(data) {
	                fnCallback(data); //服务器端返回的对象的returnObject部分是要求的格式
	            }
	        });
	    }

		var dataTable_config = {
			//"stateSave": true, // 是否允许浏览器缓存Datatables，以便下次恢复之前的状态
	        //"lengthChange":false, // 是否允许改变每页显示的数据条数
	        "processing": true,	// 是否显示加载信息
	        "autoWidth": false, // 是否让Datatables自动计算宽度
	        "serverSide": true, // 是否开启服务器模式
	        "sAjaxSource": "${ctxPortal}/listData", // 从 Ajax 源加载数据的表的内容
	        "fnServerData": retrieveData,
	        "paging": true,  //是否允许表格分页
	        "searching": false,	// 是否开启搜索功能
	        //"pagingType": "full_numbers", // 分页按钮种类显示选项(numbers/simple/simple_numbers/full/full_numbers)
	        //"pageLength": 2, // 每页的行数
	        "columns" : [
				{"data": "id", "orderable": false, "className": "text-c", "render": function(data, type, full, meta) {
					return "<input type=\"checkbox\" value=\""+data+"\" name=\"ids\">";
			    }},
				/* {"data": "id"}, */
				{"data": "name"},
				{"data": "identity"},
				{"data": "status", "render":  function(data, type, full, meta) {
					if ("Enabled"==data) {
						return "<span class=\"label label-success radius\">已启用</span>";
					} else {
						return "<span class=\"label radius\">已停用</span>";
					}
				}},
				{"data": "description"},
				{"data": "", "orderable": false, "className": "td-manage text-c", "render":  function(data, type, full, meta) {
					var content="";
					if ("sysadmin"!=full.identity) {
						if ("Enabled"==full.status) {
							content="<a style=\"text-decoration:none\" onClick=\"opfn_stop(this,'"+full.id+"')\" href=\"javascript:;\" title=\"停用\"><i class=\"Hui-iconfont\">&#xe631;</i></a>";
						} else {
							content="<a style=\"text-decoration:none\" onClick=\"opfn_start(this,'"+full.id+"')\" href=\"javascript:;\" title=\"启用\"><i class=\"Hui-iconfont\">&#xe615;</i></a>";
						}
					}
					content +="<a title=\"编辑\" href=\"javascript:;\" onclick=\"opfn_edit('编辑','${ctxPortal}/preAdd','"+full.id+"','800','500')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i></a>";
					content +="&nbsp;<a title=\"管理资源\" href=\"javascript:;\" onclick=\"opfn_manage('管理"+full.name+"的资源权限','${ctxPortal}/manage','"+full.id+"','800','500')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe61d;</i></a>";
					return content;
				}}
			]
		};

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
			layer_show(title, url, w, h);
		}
		/*编辑*/
		function opfn_edit(title, url, id, w, h) {
			layer_show(title, url + "?id=" + id, w, h);
		}
		/* function opfn_manage(title, url, id, w, h) {
			layer_show(title, url + "?id=" + id, w, h);
		} */
		/**
		管理角色的资源
		*/
		function opfn_manage(title, url, id, w, h) {
			var index = layer.open({
				type: 2,
				title: title,
				content: url + "?id=" + id
			});
			layer.full(index);
		}
		/*启用*/
		function opfn_start(obj, id) {
			layer.confirm("确认要启用吗？", function(index) {
				$.post("${ctxPortal}/changeStatus/Enabled", {id:id}, function(data) {
					if(data && data.success) {
						$("#table-list").DataTable().ajax.reload();
						layer.msg("已启用!", {
							icon : 1,
							time : 1000
						});
					}
				});
			});
		}
		/*停用*/
		function opfn_stop(obj, id) {
			layer.confirm("确认要停用吗？", function(index) {
				$.post("${ctxPortal}/changeStatus/Disabled", {id:id}, function(data) {
					if(data && data.success) {
						$("#table-list").DataTable().ajax.reload();
						layer.msg("已停用!", {
							icon : 1,
							time : 1000
						});
					}
				});
			});
		}
	</script>
</body>
</html>