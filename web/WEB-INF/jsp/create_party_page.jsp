<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <title>파티생성페이지</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<div class="container">
    <div class="row">
        <h3>파티 생성</h3>
    </div><br>
    <div class="row jumbotron">
        <form method="post" action="create-party/create.pknu" class="form-horizontal">
            <div class="form-group">
                <label for="party_name" class="control-label col-sm-2">파티이름</label>
                <div class="col-sm-10">
                    <input type="text" name="party_name" id="party_name" class="form-control">
                </div>
            </div>
            <div class="form-group">
                <div class="col-sm-offset-2 col-sm-10">
                    <button type="submit" class="btn btn-default">생성하기</button>
                </div>
            </div>
        </form>
    </div>
</div>

</body>
</html>
