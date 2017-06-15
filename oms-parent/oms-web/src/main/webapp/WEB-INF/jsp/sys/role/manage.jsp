<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE HTML>
<html>
<head>
</head>
<body>
	<div class="pd-20">
		<div class="cl pd-5 bg-1 bk-gray mt-20">
			<span class="l">
				<a href="javascript:;" onclick="opfn_add('添加','${ctxPortal}/preAddResource?roleId=${param.id}','800','500')" class="btn btn-primary radius"><i class="Hui-iconfont">&#xe600;</i>
					添加资源
				</a>
			</span>
		</div>
		<table id="table-list"
			class="table table-border table-bordered table-bg">
			<thead>
				<tr>
					<th scope="col" colspan="9">资源权限列表</th>
				</tr>
				<tr class="text-c">
					<th width="25"><input type="checkbox" name="" value=""></th>
					<th width="40">ID</th>
					<th width="150">资源</th>
					<th width="90">权限</th>
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
        $.ajax({
            type: "POST",
            url: sSource,
            dataType:"json",
            data: "jsonParam="+JSON.stringify(aoData)+"&Q_EQ_role.id=${param.id}",
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
        "sAjaxSource": "${ctxPortal}/manageData", // 从 Ajax 源加载数据的表的内容
        "fnServerData": retrieveData,
        "paging": true,  //是否允许表格分页
        "searching": false,	// 是否开启搜索功能
        //"pagingType": "full_numbers", // 分页按钮种类显示选项(numbers/simple/simple_numbers/full/full_numbers)
        //"pageLength": 2, // 每页的行数
        "columns" : [
			{"data": "id", "orderable": false, "className": "text-c", "render": function(data, type, full, meta) {
				return "<input type=\"checkbox\" value=\""+data+"\" name=\"ids\">";
		    }},
			{"data": "id"},
			{"data": "resource.name"},
			{"data": "permissionIds"},
			{"data": "", "orderable": false, "className": "td-manage text-c", "render":  function(data, type, full, meta) {
				var content="";
				/*
				content +="<a title=\"编辑\" href=\"javascript:;\" onclick=\"opfn_edit('编辑','${ctxPortal}/preAdd','"+full.id+"','800','500')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe6df;</i></a>";
				*/
				// TODO Needs to finish.
				content +="&nbsp;<a title=\"管理\" href=\"javascript:;\" onclick=\"opfn_add('管理','${ctxPortal}/preAddResource?roleId=${param.id}&resourceId="+full.resource.id+"','800','500')\" class=\"ml-5\" style=\"text-decoration:none\"><i class=\"Hui-iconfont\">&#xe61d;</i></a>";
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
	function opfn_manage(title, url, id, w, h) {
		layer_show(title, url + "?Q_EQ_resource.id=" + id + "&Q_EQ_role.id=${param.id}", w, h);
	}
	</script>
</body>
</html>