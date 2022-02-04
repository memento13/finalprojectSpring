<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<!DOCTYPE html>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>

    <script>
        $(document).ready(function () {
            $("#searchBtn").click(function () {
                let keyword = encodeURIComponent($("#keyword").val());
                let xhr = new XMLHttpRequest();
                xhr.onreadystatechange = () => {
                    if (xhr.readyState == 4) {
                        if (xhr.status == 200) {
                            let rt = xhr.responseText;
                            alert(rt);
                            let jo = window.eval("(" + rt + ")");
                            $("#partyList").empty();

                            for (let party of jo.data) {
                                $("#partyList").append($("<a></a>").text(decodeURI(party.name)).attr("href","party/join.pknu?party_id="+party.id));
                                $("#partyList").append($("<br>"));
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

<input type="text" name="keyword" id="keyword"/>
<input type="button" name="searchBtn" id="searchBtn" value="검색"></input>
<div id="partyList">
</div>

</body>
</html>

