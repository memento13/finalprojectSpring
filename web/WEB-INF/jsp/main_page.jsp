
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>����ȭ��</title>
</head>
<body>
main_page.jsp<br>
hello ${user.name}!<br>
<hr>
<jsp:include page="/party-list.pknu" flush="false"></jsp:include>
<hr>
<a href="create-party.pknu">��Ƽ����</a><br>
<a href="logout.pknu">�α׾ƿ�</a>
</body>
</html>
