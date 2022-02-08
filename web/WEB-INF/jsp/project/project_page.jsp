<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>프로젝트페이지</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>

<div class="container">
    <h2><a href="party.pknu?party_name=${project.party.name}">${project.party.name}</a>>${project.name}</h2>
    <div class="row">
        <table class="table">
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
    </div><hr>

    <div class="row">

        <form id="write" action="create-post.pknu" method="post">
            <input type="hidden" name="project_id" value="${project.id}">
            <input type="hidden" name="party_id" value="${project.party_id}">
        </form>
        <a href="#" onclick="writeSubmit()" class="btn btn-primary">글쓰기</a>
        <q:if test="${isPartyLeader}">
            <a href="project/delete.pknu?project_id=${project.id}" class="btn btn-danger">프로젝트 삭제</a>
        </q:if>
    </div>
</div>

<script>
    function writeSubmit() {
        document.getElementById("write").submit();
    }

</script>
</body>
</html>
