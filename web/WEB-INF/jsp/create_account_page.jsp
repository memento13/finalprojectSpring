<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>ȸ������</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <h3>ȸ�� ����</h3>
    </div><br>
    <div class="row jumbotron">
        <form method="post" action="create-account/create.pknu">
            <div class="form-group">
                <label for="email">�̸���</label>
                <input type="email" name="email" id="email" class="form-control">
            </div>
            <div class="form-group">
                <label for="password">��й�ȣ</label>
                <input type="password" name="password" id="password" class="form-control">
            </div>
            <div class="form-group">
                <label for="name">�̸�</label>
                <input type="text" name="name" id="name" class="form-control">
            </div>
            <div class="form-group">
                <label for="captcha">ĸí ����</label>
                <input type="text" name="captcha" id="captcha" class="form-control"/>
                <br>
                <img src="image.jsp?fname=${fname}"><br>
            </div>
            <input type="hidden" name="key" value="${key}"/>
            <button type="submit" class="btn btn-default">���� ����</button>
        </form>

    </div>


</div>
</body>
</html>
