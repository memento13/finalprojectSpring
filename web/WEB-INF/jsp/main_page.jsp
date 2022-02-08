<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<html>
<head>
    <title>메인화면</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</head>
<body>
<jsp:include page="header.jsp"/>
<div class="container">
    <div class="row">
        <jsp:include page="/party-list.pknu" flush="false"></jsp:include>
    </div>
    <div class="row">
        <a href="create-party.pknu" class="btn btn-primary">파티생성</a>
        <a href="party-search.pknu" class="btn btn-default">파티검색</a>
    </div>
</div>
</body>
</html>
