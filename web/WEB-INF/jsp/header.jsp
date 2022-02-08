<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>

<nav class="navbar navbar-default">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="main.pknu" class="navbar-brand"> TrackA 김성수 </a>
        </div>
        <q:if test="${not empty user}">
            <p class="navbar-text">${user.name}님 환영합니다</p>
            <a href="logout.pknu" class="btn btn-default navbar-btn">로그아웃</a> <br>
        </q:if>
    </div>
</nav>

