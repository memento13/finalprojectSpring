<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>������Ʈ ����������</title>
</head>
<body>
${party.name}<br>
<form method="post" action="create-project/create.pknu">
    ������Ʈ�̸� : <input type="text" name="project_name">
    <input type="hidden" name="party_id" value="${party.id}">
    <input type="submit">
</form>
</body>
</html>
