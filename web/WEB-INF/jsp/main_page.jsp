
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>메인화면</title>
</head>
<body>
main_page.jsp<br>
hello ${user.name}!<br>
<hr>
<jsp:include page="/party-list.pknu" flush="false"></jsp:include>
<hr>
<a href="create-party.pknu">파티생성</a><br>
<a href="logout.pknu">로그아웃</a>
</body>
</html>
