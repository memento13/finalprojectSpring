<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>회원가입</title>
</head>
<body>

<form method="post" action="create-account/create.pknu">
    이메일 : <input type="email" name="email"><br>
    비밀번호 : <input type="password" name="password"><br>
    이름 : <input type="text" name="name"><br>
    캡챠<input type="text" name="captcha"/>
    <input type="hidden" name="key" value="${key}"/>
    <input type="submit">
</form>
<img src="../../image.jsp?fname=${fname}">
<img src="/image.jsp?fname=${fname}">
<%--<img src="/image.jsp?fname=${fname}">--%>

hello create account page!
</body>
</html>
