<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>파티생성페이지</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<form method="post" action="create-party/create.pknu">
    파티이름 : <input type="text" name="party_name">
    <input type="submit">
</form>
</body>
</html>
