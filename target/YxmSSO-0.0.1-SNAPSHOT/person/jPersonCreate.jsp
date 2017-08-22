<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'jPersonList.jsp' starting page</title>
    
    <script type="text/javascript">
    	function formSubmit(){
    		document.forms[0].action = "${pageContext.request.contextPath}/person/insert.action";
    		document.forms[0].submit();
    	}
    </script>
  </head>
  
  <body>
<form method="post"> 
  
    新增人员信息<br>

<a href="#" onclick="formSubmit();">保存</a>

<table border="1">
<tr>
	<td>姓名</td>
	<td><input type="text" name="name"/></td>
</tr>
<tr>
	<td>年龄</td>
	<td><input type="text" name="age"/></td>
</tr>
<tr>
	<td>备注</td>
	<td><input type="text" name="remark"/></td>
</tr>
</table>    
    
</form> 
  </body>
</html>
