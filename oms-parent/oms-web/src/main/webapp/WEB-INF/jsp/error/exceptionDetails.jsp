<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<%@ include file="/common/meta.jsp"%>
<div>
    <a class="btn btn-link btn-detail">
        点击显示详细错误信息
    </a>
    <%--<a href="#" class="btn btn-link">&gt;&gt;报告给系统管理员</a>--%>
    <div class="prettyprint detail" style="display: none;">
        ${stackTrace}
    </div>
</div>
<script type="text/javascript">
    $(".btn-detail").click(function() {
        var a = $(this);
        $(".detail").toggle();
    });
</script>