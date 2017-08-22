<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">

<title>My JSP 'index.jsp' starting page</title>
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="expires" content="0">
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<meta http-equiv="description" content="This is my page">
<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
</head>

<body>
	This is my JSP page.
	<br>

	<script src="static/lib/jquery-2.1.4.min.js"></script>
	<script type="text/javascript"></script>
	<script>
		var token = location.search.substring(1);
		console.log("token", token);
		
		if (token != null && token.length > 0) {
			console.log("测试进来了");
			$.ajax({
				type : 'GET',
				url : 'http://127.0.0.1:8080/YxmSSO/user/getInfo.action?token='+token,
				success : function(res) {
					console.log(res);
				}
			});
		}
	</script>
</body>
</html>
