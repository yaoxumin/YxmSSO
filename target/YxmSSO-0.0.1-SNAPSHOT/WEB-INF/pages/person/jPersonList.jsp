<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'jPersonList.jsp' starting page</title>
  </head>
  
  <body>
    人员列表<br>

<a href="${pageContext.request.contextPath}/person/addRecord.action">新增</a>
<a href="${pageContext.request.contextPath}/person/saveOrUpdate.action">saveOrUpdate</a>
<table border="0">
<tr>
	<td>序号</td>
	<td>姓名</td>
	<td>年龄</td>
	<td>备注</td>
	<td>操作</td>
</tr>

<c:forEach items="${personList}" var="p" varStatus="status">
<tr>
	<td>${status.index+1}</td>
	<td>${p.username}</td>
	<td>${p.age}</td>
	<td>${p.gender}</td>
	<td><a href="${pageContext.request.contextPath}/person/tobook.action?id=${p.id}">查看书籍</a></td>
	<td><a href="${pageContext.request.contextPath}/person/getDetail.action?id=${p.id}">详情</a></td>
	<td><a href="${pageContext.request.contextPath}/person/deleteRecord.action?id=${p.id}">删除人员</a></td>
</tr>
</c:forEach>

</table>    
    
  </body>
</html>
