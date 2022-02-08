<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>로그인 페이지</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">

    </div>
    <div class="row jumbotron">
        <form method="post" action="login/authorization.pknu">
            <div class="form-group">
                <label for="inputEmail">이메일</label>
                <input type="email" name="email" class="form-control" id="inputEmail" placeholder="Email"/><br>
            </div>
            <div class="form-group">
                <label for="inputPassword">비밀번호</label>
                <input type="password" name="password" class="form-control" id="inputPassword" placeholder="Password"/>
            </div>
            <button type="submit" class="btn btn-default">로그인</button>
            <a href="create-account.pknu" class="btn btn-default">회원가입</a>
        </form>
        <div class="row">

        </div>
    </div>

</div>
</body>
</html>
