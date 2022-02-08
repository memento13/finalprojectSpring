<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

    <script>
        $(document).ready(function () {
            $("#searchBtn").click(function () {
                let keyword = encodeURIComponent($("#keyword").val());
                let xhr = new XMLHttpRequest();
                xhr.onreadystatechange = () => {
                    if (xhr.readyState == 4) {
                        if (xhr.status == 200) {
                            let rt = xhr.responseText;
                            // alert(rt);
                            let jo = window.eval("(" + rt + ")");
                            $("#partyList").empty();

                            for (let party of jo.data) {
                                $("#partyList").append($("<a></a>").text(decodeURI(party.name)).attr("href", "party/join.pknu?party_id=" + party.id).addClass('list-group-item'));
                                // $("#partyList").append($("<br>"));
                            }
                        }
                    }
                };
                xhr.open("get", "party-search/search.pknu?keyword=" + keyword, true);
                xhr.send(null);
            });

        });
        // window.onload = function () {
        //
        // }
    </script>
    <title>파티 검색</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<div class="container">
    <div class="row">
        <h3>파티 검색</h3>
    </div><br>
    <div class="row form-group">
        <div class="col-sm-10">
            <input type="text" name="keyword" id="keyword" class="form-control" placeholder="파티명을 입력하세요"/>
        </div>
        <div class="col-sm-2">
            <input type="button" name="searchBtn" id="searchBtn" value="검색" class="btn btn-primary"></input>
        </div>
    </div>
    <hr>
    <div id="partyList" class="row list-group">
    </div>
</div>


</body>
</html>

