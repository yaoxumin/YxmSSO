layui.use('jquery', function(){
	var socket;//webSocket属性
	var bodyData;
	var command;
	protobuf.load("/tio/chat.proto", function(err, root) {
		if (err) {
			throw err;
		}
		loadClass(root);
	});

	var ByteBuffer = window.dcodeIO.ByteBuffer;
	
	function loadClass(root) {
		AuthReqBody = root
				.lookup("com.yxm.lim.tio.packets.AuthReqBody");
		AuthRespBody = root
				.lookup("com.yxm.lim.tio.packets.AuthRespBody");

		JoinReqBody = root
				.lookup("com.yxm.lim.tio.packets.JoinReqBody");
		JoinGroupResult = root
				.lookup("com.yxm.lim.tio.packets.JoinGroupResult");
		JoinRespBody = root
				.lookup("com.yxm.lim.tio.packets.JoinRespBody");

		ChatReqBody = root
				.lookup("com.yxm.lim.tio.packets.ChatReqBody");
		ChatRespBody = root
				.lookup("com.yxm.lim.tio.packets.ChatRespBody");

		ChatType = root.lookup("com.yxm.lim.tio.packets.ChatType");
		DeviceType = root
				.lookup("com.yxm.lim.tio.packets.DeviceType");
		Command = root.lookup("com.yxm.lim.tio.packets.Command");
		console.log("Command", Command);
	}

	/**
	 * 发送packet
	 *
	 * @param {} ws
	 * @param {} command
	 * @param {} BodyClass
	 * @param {} bodyData
	 */
	function sendPacket(ws, command, BodyClass, bodyData) {
		var bodyObj = null;
		if (bodyData) {
			bodyObj = BodyClass.create(bodyData);
		}

		var bodyBuffer = null;
		if (bodyObj) {
			bodyBuffer = BodyClass.encode(bodyObj).finish();
		}
		sendBuffer(ws, command, bodyBuffer);
	}

	/**
	 * webSocket发送buffer
	 * @param {} ws
	 * @param {} command
	 * @param {} bodyBuffer
	 */
	function sendBuffer(ws, command, bodyBuffer) {
		var bodyLength = 0;
		if (bodyBuffer) {
			bodyLength = bodyBuffer.length;
		}

		var allBuffer = ByteBuffer.allocate(1 + bodyLength);
		allBuffer.writeByte(command);

		if (bodyBuffer) {
			allBuffer.append(bodyBuffer);
		}
		ws.send(allBuffer.buffer);
	}
	
	var aesKey= "abcdefgabcdefg12";//秘钥。16位
	//加密
	function aesEncrypt(content) {
		var key = CryptoJS.enc.Utf8.parse(aesKey);
		var srcs = CryptoJS.enc.Utf8.parse(content);
		var encrypted = CryptoJS.AES.encrypt(srcs, key, {
			mode : CryptoJS.mode.ECB,
			padding : CryptoJS.pad.Pkcs7
		});
		return encrypted.toString();
	}
	//解密
	function aesDecrypt(content) {
		content =content.replace(/\s/g,'+');//由于地址栏接收到的加密串存在+号回被自动转为空格。需要替换回来。
		var key = CryptoJS.enc.Utf8.parse(aesKey);
		var decrypt = CryptoJS.AES.decrypt(content, key, {
			mode : CryptoJS.mode.ECB,
			padding : CryptoJS.pad.Pkcs7
		});
		return CryptoJS.enc.Utf8.stringify(decrypt).toString();
	}
	var $ = layui.jquery;
	var scripts = document.getElementsByTagName('script');
	var sum = scripts.length;
	var url = "";
	var regex = /\S*lim.js\?\S*$/;
	for (var i = 0; i < sum; i++) {
		if (regex.exec(scripts[i].src)) {
			url = scripts[i].src;
		}
	}
	var regex = /^(\w+):\/\/([^\/:]*)(?::(\d+))?\/([^?]*)(?:\?(.*))?$/gi;
	regex.exec(url);

	
	var protocol = RegExp.$1;
	var domain = RegExp.$2;
	var port = RegExp.$3;
	var uri = RegExp.$4;
	var qstring = RegExp.$5;

	var host = protocol + '://' + domain + ':' + port;
	var wshost = 'ws://' + domain + ':' + port;
	var tiowshost = 'ws://127.0.0.1:9321';
	console.log("host",host);
	console.log("wshost",wshost);
	$.ajax({
		type : 'POST',
		url : host + '/httpClient/ajaxKey.action?' + qstring + '&' + window.location.hostname,
		async : false,
		xhrFields: {withCredentials: true},
	    crossDomain: true,
		success : function(res) {
			res =  JSON.parse(res);
			console.log("res",res.code);
			if (res.code == 200) {
				console.log("请求",host);
				var userconfig;
				var user = res.data;
				document.cookie = "JSESSIONID=" + user.sessionId+";path=/";
				$.ajax({
					type : 'GET',
					url : host+"/socket/lim.config.json?type="+user.type+"&avatar="+user.avatar+"&id="+user.id+"&host="+host+"&JS="+user.sessionId+"&wshost="+tiowshost,
					async : false,
					dataType : 'json',
					success : function(res) {
						userconfig = res.data;
						console.log("userconfig",userconfig);
					}
				});

				layui.use('layim', function(layim) {
					// 基础配置
					layim.config(userconfig);

					// 向后端的webSocket传递参数格式为:companyId_type_userId
					//var target = wshost + '/webSocketServer.action?key=' + user.id;
					socket = new WebSocket(tiowshost);
					socket.binaryType = 'arraybuffer';
					// 连接成功时触发
					var msgboxCount = 0;
					socket.onopen = function() {
						command = Command.values.COMMAND_AUTH_REQ;
						bodyData = {
							deviceId : "deviceId--888888888888",
							seq : 1,
							deviceType : DeviceType.DEVICE_TYPE_PC,
							deviceInfo : "chrome",
							token : user.id
						};
						sendPacket(socket, command, AuthReqBody,
								bodyData);
						
						$.get(host + '/chatlog/countChatlog.action?id='+user.id,function(data, status) {
							setTimeout(function() {
								if (Number(data) > 0) {
									msgboxCount = data;
									layim.msgbox(msgboxCount);
								}
							}, 1500);
						});
					};
					
					// 每次窗口打开窗口查询离线消息
					layim.on('chatChange', function(res) {
						var type = res.data.type;
						var fromid = res.data.id;
						var avatar = res.data.avatar;
						var username = res.data.username;
						if (type == 'friend' && msgboxCount != 0) {
							$.getJSON( 
									host + '/chatlog/readFriendChatlog.action',
									{fromid : fromid, id : user.id}
									,function(json) {
										if (json != null && json != '') {
											$.each(json, function(n, value) {
												layim.getMessage({
													username : value.username,
													avatar : value.avatar,
													id : value.id,
													type : value.type,
													content : value.content,
													timestamp : value.timestamp,
													fromid : value.fromid,
													mine : value.mine,
													system : false
												});
											});
											msgboxCount = msgboxCount - json.length;
											if (msgboxCount > 0) {
												layim.msgbox(msgboxCount);
											} else {
												$('.layim-tool-msgbox span').css('display','none');
											}
										}
								});
						}
					});

					// 监听收到的消息
					socket.onmessage = function(res) {
						var arrayBuffer = res.data;
						var byteBuffer = ByteBuffer.wrap(arrayBuffer);
						command = Command.valuesById[byteBuffer
								.readByte()];
						arrayBuffer = byteBuffer.toArrayBuffer();
						var uint8Array = new Uint8Array(arrayBuffer);
						var respBody = ChatRespBody.decode(uint8Array);
						if (obj != null) {
							var type = respBody.type;
							var data = respBody.data;
							/* 各种消息类型 */
							if (type != null && (type == 'chatMessage'  || type == 'chatKefuMessage' || type == 'rebroadMessage' || type == 'crossMessage')) {
								layim.getMessage(data);
							} else if (type != null && (type == 'offline' || type == 'online')) {
								var id = data.id;
								layim.setFriendStatus(id, type);
							} else if (type != null && type == 'kefuStatusMessage') {
								console.log("提示客服:"+data.content);
								if (data.content.length > 0) {
									$('#tipContent').html(data.content);
								}
								if (data.receving > 0) {
									$('#recevingCount').html(data.receving);
								}
								
							}else if (type != null && (type == 'systemTip')) {
								layer.open({
									  title: '系统提醒'
									  ,content: data.systemTip
									  ,offset: 'rb'
									  ,shade: 0
									  ,time :10000
								});  
							}
						} else {
							console.log('socket.onmessage接受到的消息为空');
						}
					};
					

					// 监听发送消息
					layim.on('sendMessage', function(res) {
						if (res.to.list != null ) {
							res.to.list = null;
						}
						command = Command.values.COMMAND_CHAT_REQ;
						bodyData = {
							type : 'chatMessage',
							data : res
						};
						console.log("bodyData",bodyData);
						sendPacket(socket, command, ChatReqBody, bodyData);
					});
					// 状态切换
					layim.on('online', function(status) {
						command = Command.values.COMMAND_CHAT_REQ;
						if (status == 'online') { // 除非选在在线状态，其他状态全部是离线
							bodyData = {
								type : 'memberStatusMessage', // 随便定义，用于在服务端区分消息类型
								data : {
									id : user.id,
									type : 'friend',
									content : 'online'
								}
							};
						} else { // 如果是隐身或者离线，同一提示为离线
							bodyData = {
								type : 'memberStatusMessage', // 随便定义，用于在服务端区分消息类型
								data : {
									id : user.id,
									system : true,
									type : 'friend',
									content : 'offline'
								}
							};
						}
						sendPacket(socket, command, ChatReqBody, bodyData);
					});
					
					//修改个性签名
					layim.on('sign', function(value){
						$.ajax({
							type : 'POST',
							url : host+"/httpClient/ajaxUpateSign.action",
							data : {"id" : user.id, "sign" : value},
							dataType : "text",
							success : function(res) {
								layer.msg(res);
							},
							error : function() {
								console.log("出错了");
							}
						});
						//此时，你就可以通过Ajax将新的签名同步到数据库中了。
					});     

					// 专为程序猿设计,发送代码
					layim.on('tool(code)', function(insert, send, obj) { // 事件中的tool为固定字符，而code则为过滤器，对应的是工具别名（alias）
						layer.prompt({
							title : '插入代码',
							formType : 2,
							shade : 0
						}, function(text, index) {
							layer.close(index);
							insert('[pre class=layui-code]' + text + '[/pre]'); // 将内容插入到编辑器，主要由insert完成
							// send(); //自动发送
						});
					});
					/* 将客户呼叫转移给其他客服 */
					layim.on('tool(rebroad)', function(insert, send, obj) { // 事件中的tool为固定字符，而code则为过滤器，对应的是工具别名（alias）
						var temp = obj.data;
						if ($('#recevingCount').html() > 0) {
							$('#recevingCount').html($('#recevingCount').html()-1);
						}
						//设置命令格式，如果是通过webSocket的一般都是Command.values.COMMAND_CHAT_REQ
						command = Command.values.COMMAND_CHAT_REQ;
						bodyData = {
							type : 'rebroadMessage', // 随便定义，用于在服务端区分消息类型
							data : {
								to : {
									id : temp.id,// 用户在layim面板中的id
									username : temp.username,// 用户名称
									avatar : temp.avatar,// 用户头像
									type : temp.type,// 消息类型
								},
								mine : {
									id : user.id,// 客服的id
									username : user.username, // 我的昵称
									content : user.id,
								}
							}
						};
						sendPacket(socket, command, ChatReqBody, bodyData);
					});
					/* 完成客户咨询 */
					layim.on('tool(finish)', function(insert, send, obj) {
						var temp = obj.data;
						var tempType = temp.id.split("_")[3];
						if(tempType!="tourist"){//不是游客或者已经提交过
							layer.msg("无法操作");
							return;
						}else{
							layer.msg("完成咨询");
							if ($('#recevingCount').html() > 0) {
								$('#recevingCount').html($('#recevingCount').html()-1);
							}
							//设置命令类型
							command = Command.values.COMMAND_CHAT_REQ;
							bodyData = {
								type : 'finishChatKefu',
								data : {
									to : {
										id : temp.id,
										username : temp.username,
										avatar : temp.avatar,
										type : temp.type,
									},
									mine : {
										id : user.id,
										username : user.username
									}
								}
							};
							sendPacket(socket, command, ChatReqBody, bodyData);
						}
					});
					
					//评分
					var markingFlag =true;//标识符防止重复提交
					layim.on('tool(marking)', function(insert, send, obj) { 
						var classname= document.querySelector(".layim-tool-marking");
						var temp = obj.data;
						if(markingFlag && temp.id!="type_system_system_0"){
						    layer.open({
							   type:2,
							   title: false,
							   content: host+"/socket/score.html?touristId="+aesEncrypt(user.id)+"&touritName="+aesEncrypt(user.username) +"&touristAvatar="+aesEncrypt(user.avatar) +"&type="+aesEncrypt(temp.type)  +"&kefuId="+aesEncrypt(temp.id)  +"&wshost="+aesEncrypt(tiowshost)  +"&kefuName="+ aesEncrypt( temp.username) +"&kefuAvatar="+aesEncrypt(temp.avatar), 
							   shade: [0, '#19e64dff'],
							   shadeClose :true,
							   closeBtn: 0,
							   area: ['230px', '120px'],
							   offset: [classname.getBoundingClientRect().top-120, classname.getBoundingClientRect().left]
						    });
						    markingFlag=false;
						}else{
							layer.msg("无法操作");
							return;
						}
					});
					
					// 留言板
					layim.on('tool(msgboard)', function(insert, send, obj) { // 事件中的tool为固定字符，而code则为过滤器，对应的是工具别名（alias）
						layer.open({
							type : 2,
							title : '留言板',
							area: ['600px', '450px'],
							shadeClose: true,  
							content : host+'/socket/msgboard.html?id='+aesEncrypt(user.id)+'&host='+host,
						});
					});

					// 面板外的操作
					var active = {
						// 咨询客服
						chatKefu : function() {
							command = Command.values.COMMAND_CHAT_REQ;
							bodyData = {
									type : 'chatKefuMessage',
									data : {
										mine : {
											id : user.id,
											username : user.username,
											content : user.companyId,
										}
									}
								};
							sendPacket(socket, command, ChatReqBody, bodyData);
						},
//						kefuAdd : function() {
//							
//							socket.send(JSON.stringify({
//								type : 'kefuAddSystemMessage', // 客服进入咨询系统服务
//								data : {
//									kefuId : user.id,
//									systemId : systemId
//								}
//							}));
//						},
//						kefuExit : function() {
//							socket.send(JSON.stringify({
//								type : 'kefuExitSystemMessage', // 客服提退出咨询系统服务
//								data : {
//									kefuId : user.id,
//									systemId : systemId
//								}
//							}));
//						},
						clearReceive : function() {
							$("#tipContent").html("");//清空提示内容
							$('#recevingCount').html(0);//接待数量归零
							command = Command.values.COMMAND_CHAT_REQ;
							bodyData = {
								type : 'kefuClearReceiveMessage', // 客服清空当前正在接待的人员
								data : {
									to : {
										id : user.id
										,type : user.type
									}
								}
							};
							sendPacket(socket, command, ChatReqBody, bodyData);
						}
					};

					$('.site-demo-layim').on('click', function() {
						var type = $(this).data('type');
						active[type] ? active[type].call(this) : '';
					});
				});

			} else {
				console.log(res.msg);
			}
		}
	});
});