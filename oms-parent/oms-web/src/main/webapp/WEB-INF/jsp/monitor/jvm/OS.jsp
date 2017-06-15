<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.lang.management.ManagementFactory"%>
<%@ page import="java.lang.management.OperatingSystemMXBean"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="os"
	value="<%=ManagementFactory.getOperatingSystemMXBean()%>" />
<table class="table table-border table-bordered table-bg mt-20">
	<thead>
		<tr>
			<th colspan="2" scope="col">JVM OS Info</th>
		</tr>
	</thead>
	<tbody>
		<tr>
			<td>OS Name</td>
			<td>${os.name}</td>
		</tr>
		<tr>
			<td>OS Versin</td>
			<td>${os.version}</td>
		</tr>
		<tr>
			<td>OS Available Processors</td>
			<td>${os.availableProcessors}</td>
		</tr>
		<tr>
			<td>OS Architecture</td>
			<td>${os.arch}</td>
		</tr>
	</tbody>
</table>