<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.lang.management.ManagementFactory"%>
<%@ page import="java.lang.management.RuntimeMXBean"%>
<%@ include file="/common/taglib.jsp"%>
<c:set var="rt" value="<%=ManagementFactory.getRuntimeMXBean()%>" />
<table class="table table-border table-bordered table-bg mt-20">
	<thead>
		<tr>
			<th colspan="2" scope="col">Java Runtime Info</th>
		</tr>
	</thead>
	<tbody>
		<!-- <tr>
				<th width="200">服务器计算机名</th>
				<td><span id="lbServerName">http://127.0.0.1/</span></td>
			</tr> -->
		<tr>
			<td>Vm Name</td>
			<td>${rt.vmName}</td>
		</tr>
		<tr>
			<td>Vm Version</td>
			<td>${rt.vmVersion}</td>
		</tr>
		<tr>
			<td>Vm Vendor</td>
			<td>${rt.vmVendor}</td>
		</tr>
		<tr>
			<td>Up Time</td>
			<td>${(rt.uptime)/(1000*60*60)}hours</td>
		</tr>
		<tr>
			<td>Input Arguments</td>
			<td>${rt.inputArguments}</td>
		</tr>
		<tr>
			<td>Library Path</td>
			<td>${rt.libraryPath}</td>
		</tr>
		<tr>
			<td>Class Path</td>
			<td>${rt.classPath}</td>
		</tr>
	</tbody>
</table>