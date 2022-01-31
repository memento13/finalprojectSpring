<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>프로젝트 생성페이지</title>
</head>
<body>
${party.name}<br>
<form method="post" action="create-project/create.pknu">
    프로젝트이름 : <input type="text" name="project_name">
    <input type="hidden" name="party_id" value="${party.id}">
    <input type="submit">
</form>
</body>
</html>
