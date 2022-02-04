<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <title>post</title>
</head>
<body>
<div>
    ${post.title} | ${post.user.name} | ${post.createDate} <a href="post/delete.pknu?post_id=${post.id}"> 삭제 </a><br>
</div>
<div>
    ${post.content}
</div>
<div>
    <div id="likesDisplay">
        <p>좋아요 : ${likes}</p> <br>
    </div>
    <div>
        <input type="button" onclick="ajaxLike('like.pknu?post_id=${post.id}')" value="좋아요"/>
    </div>
    <div id="likesAjaxMsg"></div>
</div>

<script>
    function ajaxLike(doLikeUrl) {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {

                    let rt = xhr.responseText;
                    alert(rt);
                    let jo = window.eval("(" + rt + ")");
                    let vo = jo.data;
                    if(vo.access){
                        $("#likesDisplay").empty();
                        $("#likesDisplay").append($("<p></p>").text("좋아요 : "+decodeURI(vo.count)))
                        $("#likesDisplay").append($("<br>"));
                    }
                    $("#likesAjaxMsg").empty();
                    $("#likesAjaxMsg").append($("<p></p>").text(decodeURI(vo.msg)));
                }
            }
        };
        xhr.open("get", doLikeUrl, true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send(null);
    }
</script>
</body>
</html>
