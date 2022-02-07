<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>프로젝트페이지</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
프로젝트 이름 : ${project.name}
<q:if test="${isPartyLeader}">
    <a href="project/delete.pknu?project_id=${project.id}">프로젝트 삭제</a>
</q:if>
<form id="write" action="create-post.pknu" method="post">
    <input type="hidden" name="project_id" value="${project.id}">
    <input type="hidden" name="party_id" value="${project.party_id}">
</form>
<hr>
<a href="#" onclick="writeSubmit()" >글쓰기</a>
<hr>
<div>
    <table>
        <th>제목</th>
        <th>작성자</th>
        <th>작성일</th>
        <q:forEach items="${posts}" var="post">
            <tr>
                <td><a href="post.pknu?post_id=${post.id}">${post.title}</a></td>
                <td>${post.user.name}</td>
                <td>${post.createDate}</td>
            </tr>
        </q:forEach>
    </table>
</div>

<script>
    function writeSubmit() {
        document.getElementById("write").submit();
    }

</script>
</body>
</html>
