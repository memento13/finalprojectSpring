<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>ȸ������</title>
</head>
<body>

<form method="post" action="create-account/create.pknu">
    �̸��� : <input type="email" name="email"><br>
    ��й�ȣ : <input type="password" name="password"><br>
    �̸� : <input type="text" name="name"><br>
    ĸí<input type="text" name="captcha"/>
    <input type="hidden" name="key" value="${key}"/>
    <input type="submit">
</form>
<img src="../../image.jsp?fname=${fname}">
<img src="/image.jsp?fname=${fname}">
<%--<img src="/image.jsp?fname=${fname}">--%>

hello create account page!
</body>
</html>
