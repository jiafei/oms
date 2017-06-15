<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<nav class="breadcrumb">
		<i class="Hui-iconfont">&#xe67f;</i> 首页 <span class="c-gray en">&gt;</span>
		管理员管理 <span class="c-gray en">&gt;</span> 管理员列表 <a
			class="btn btn-success radius r mr-20"
			style="line-height:1.6em;margin-top:3px"
			href="javascript:location.replace(location.href);" title="刷新"><i
			class="Hui-iconfont">&#xe68f;</i></a>
	</nav>
	<div class="pd-20">
		<div class="text-c">
			<form class="Huiform" method="post" action="" target="_self">
				日期范围： <input type="text"
					onfocus="WdatePicker({maxDate:'#F{$dp.$D(\'datemax\')||\'%y-%M-%d\'}'})"
					id="datemin" name="Q_GTE_createDate_D" value="${param.Q_GTE_createDate_D}" class="input-text Wdate" style="width:120px;">
				- <input type="text"
					onfocus="WdatePicker({minDate:'#F{$dp.$D(\'datemin\')}',maxDate:'%y-%M-%d'})"
					id="datemax" name="Q_LTE_createDate_D" value="${param.Q_LTE_createDate_D}" class="input-text Wdate" style="width:120px;">
				<input type="text" class="input-text" style="width:250px"
					placeholder="输入管理员名称" id="" name="Q_LIKE_username" value="${param.Q_LIKE_username}">
				<button type="submit" class="btn btn-success" id="" name="">
					<i class="Hui-iconfont">&#xe665;</i> 搜索
				</button>
			</form>
		</div>
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
					<%--<th width="40">ID</th>--%>
					<th width="150">登录名</th>
					<th width="90">手机</th>
					<th width="150">邮箱</th>
					<%--<th>角色</th>--%>
					<th width="130">创建时间</th>
					<th width="100">是否已启用</th>
					<th width="100">操作</th>
				</tr>
			</thead>
		</table>
	</div>
	<script type="text/javascript">
		$(function() {
			$("#table-list").dataTable(dataTable_config);
		});

		function retrieveData(sSource, aoData, fnCallback, oSettings) {
	        $.ajax( {     
	            type: "POST",
	            url: sSource,   
	            dataType:"json",  
	            data: "jsonParam="+JSON.stringify(aoData)+"&Q_GTE_createDate_D=${param.Q_GTE_createDate_D}&Q_LTE_createDate_D=${param.Q_LTE_createDate_D}&Q_LIKE_username=${param.Q_LIKE_username}",  
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
				<%--{"data": "id"},--%>
				{"data": "username"},
				{"data": "mobilePhone"},
				{"data": "email"},
				{"data": "createDate", "className": "text-c", "render": function(data, type, full, meta) {
					return timeFormat(data);
				}},
				{"data": "status", "className": "td-status text-c", "render": function(data, type, full, meta) {
					if ("Normal"==data) {
						return "<span class=\"label label-success radius\">已启用</span>";
					} else {
						return "<span class=\"label radius\">已停用</span>";
					}
			    }},
				{"data": "", "orderable": false, "className": "td-manage text-c", "render":  function(data, type, full, meta) {
					var content="";
					if ("admin"!=full.username) {
						if ("Normal"==full.status) {
							content="<a style=\"text-decoration:none\" onClick=\"opfn_stop(this,'"+full.id+"')\" href=\"javascript:;\" title=\"停用\"><i class=\"Hui-iconfont\">&#xe631;</i></a>";
						} else {
							content="<a style=\"text-decoration:none\" onClick=\"opfn_start(this,'"+full.id+"')\" href=\"javascript:;\" title=\"启用\"><i class=\"Hui-iconfont\">&#xe615;</i></a>";
						}
					}
					content +="<a title=\"编辑\" href=\"javascript:;\" onclick=\"opfn_edit('编辑','${ctxPortal}/preAdd','"+full.id+"','800','500')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i></a>";
					content +="&nbsp;<a title=\"管理\" href=\"javascript:;\" onclick=\"opfn_add('管理用户角色','${ctxPortal}/preAddRole?userId="+full.id+"','800','500')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe61d;</i></a>";
					/*
					if ("admin"!=full.username) {
						content +="<a title=\"删除\" href=\"javascript:;\" onclick=\"opfn_del(this,'"+full.id+"')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6e2;</i></a>";
					}
					*/
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
		/*删除*/
		function opfn_del(obj, id) {
			layer.confirm("确认要删除吗？", function(index) {
				//此处请求后台程序，下方是成功后的前台处理……

				$(obj).parents("tr").remove();
				layer.msg("已删除!", {
					icon : 1,
					time : 1000
				});
			});
		}
		/*编辑*/
		function opfn_edit(title, url, id, w, h) {
			layer_show(title, url + "?id=" + id, w, h);
		}
		/*停用*/
		function opfn_stop(obj, id) {
			layer.confirm("确认要停用吗？", function(index) {
				$.post("${ctxPortal}/changeStatus/Blocked", {id:id}, function(data) {
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

		/*启用*/
		function opfn_start(obj, id) {
			layer.confirm("确认要启用吗？", function(index) {
				$.post("${ctxPortal}/changeStatus/Normal", {id:id}, function(data) {
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
	</script>
</body>
</html>