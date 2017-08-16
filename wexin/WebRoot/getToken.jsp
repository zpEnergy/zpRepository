<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ page import="com.zpinfo.wechat.common.AccessTokenInfo"%>
<html>
<head>
<title></title>
</head>
<body>
	微信学习
	<hr />
	access_token为：<%=AccessTokenInfo.accessToken.getAccessToken()%>
</body>
</html>