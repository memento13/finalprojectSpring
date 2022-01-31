<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<html>
<head>
<head>
    <title>${party.name}</title>
</head>
<body>
${party.name} <br>
${party.createDate}<br>
${party.modifiedDate}<br>
<a href="create-project.pknu?party_id=${party.id}">프로젝트 생성</a>
</body>
</html>
