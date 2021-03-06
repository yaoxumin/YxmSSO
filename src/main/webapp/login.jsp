<%@ page language="java" pageEncoding="utf-8"%>
<%  
    response.setHeader("Pragma", "No-cache");
    response.setHeader("Cache-Control", "no-cache");
    response.setDateHeader("Expires", 0);   
%>
<!DOCTYPE html>
<html>
	<head>
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
		<meta charset="utf-8" />
		<title>YxmSSO登录页面</title>
		
		<meta name="description" content="User login page" />
		<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />
		
		<script type="text/javascript">
			var url = window.location.href;
			if(url.indexOf('${loginUrl}') == -1){
				window.location.href = '${_path}${loginUrl}';
			}
		</script>
	</head>

	<body class="login-layout">
		<div class="main-container">
			<div class="main-content">
				<div class="row">
					<div class="col-sm-10 col-sm-offset-1">
						<div class="login-container">
							<div class="center">
								<h1>
									<i class="ace-icon fa fa-leaf green"></i>
									<!-- <span class="red">XX</span> -->
									<span class="white" id="id-text2"></span>
								</h1>
								<h4 class="blue" id="id-company-text"> </h4>
							</div>

							<div class="space-6"></div>

							<div class="position-relative">
								<div id="login-box" class="login-box visible widget-box no-border">
									<div class="widget-body">
										<div class="widget-main">
											<h4 class="header blue lighter bigger">
												<i class="ace-icon fa fa-coffee green"></i>
												请填写登录信息
											</h4>

											<div class="space-6"></div>

											<form id="_loginForm" action="/user/login.action" method="post"
												validate="true" vmessage="false">
												<input name="url" value="${result.backUrl }" />
												<fieldset>
													<label class="block clearfix form-group">
														<span class="block input-icon input-icon-right help-validate">
															<input id="_account" name="username" type="text" class="form-control form-data" placeholder="登录名"
																required="true" minlength = '2'/>
															<i class="ace-icon fa fa-user"></i>
														</span>
													</label>

													
													<div class="space"></div>

													<div class="clearfix">
														<label class="inline">
															<input id="_rememberMe" type="checkbox" class="ace" checked=""/>
															<span class="lbl"> 记住我</span>
														</label>

														<button id="_loginButton" type="submit" class="width-35 pull-right btn btn-sm btn-primary">
															<i class="ace-icon fa fa-key"></i>
															<span class="bigger-110">登录</span>
														</button>
													</div>

													<div class="space-4"></div>
												</fieldset>
											</form>

											
										</div><!-- /.widget-main -->

										
									</div><!-- /.widget-body -->
								</div><!-- /.login-box -->

							</div><!-- /.position-relative -->
						</div>
					</div><!-- /.col -->
				</div><!-- /.row -->
			</div><!-- /.main-content -->
		</div><!-- /.main-container -->
		<script type="text/javascript">
			alert("${result.msg}" );    
		</script>
	</body>
</html>
