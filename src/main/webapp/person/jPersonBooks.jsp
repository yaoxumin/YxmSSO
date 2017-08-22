<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'jPersonList.jsp' starting page</title>
  </head>
  
  <body>
   ${person.name}书籍列表<br>


<table border="1">
<tr>
	<td>序号</td>
	<td>名称</td>
	<td>金额</td>
</tr>

<c:forEach items="${person.books}" var="b" varStatus="status">
<tr>
	<td>${status.index+1}</td>
	<td>${b.name}</td>
	<td>${b.money}</td>
</tr>
</c:forEach>

</table>    
    
  </body>
</html>
