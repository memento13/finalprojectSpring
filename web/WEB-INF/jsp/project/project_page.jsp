<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>프로젝트페이지</title>
</head>
<body>
프로젝트 이름 : ${project.name}
<form id="write" action="create-post.pknu" method="post">
    <input type="hidden" name="project_id" value="${project.id}">
    <input type="hidden" name="party_id" value="${project.party_id}">
</form>
<hr>
<a href="#" onclick="writeSubmit()" >글쓰기</a>
<hr>
<q:forEach items="${posts}" var="post">
    <div>
        <div>${post.title}</div>
        <div>${post.content}</div>
    </div>
    <hr>
</q:forEach>
<script>
    function writeSubmit() {
        document.getElementById("write").submit();
    }

</script>
</body>
</html>
