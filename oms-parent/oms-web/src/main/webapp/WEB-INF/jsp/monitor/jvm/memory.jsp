<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.lang.management.ManagementFactory"%>
<%@ include file="/common/taglib.jsp"%>
<% ManagementFactory.getMemoryMXBean().gc(); %>
<table class="table table-border table-bordered table-bg mt-20">
	<thead>
		<tr>
			<th colspan="2" scope="col">JVM Memory Info</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>Init Memory</td>
			<td><%=ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getInit()/1000000%>M</td>
		</tr>
		<tr>
			<td>MAX Memory</td>
			<td><%=ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getMax()/1000000%>M</td>
		</tr>
		<tr>
			<td>Used Memory</td>
			<td><%=ManagementFactory.getMemoryMXBean().getHeapMemoryUsage().getUsed()/1000000%>M</td>
		</tr>
	</tbody>
</table>