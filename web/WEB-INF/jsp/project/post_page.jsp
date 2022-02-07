<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <title>post</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
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
<div id="commentWriterDisplay">
<form method="post" accept-charset="UTF-8" name="commentWriter">
    <input type="hidden" name="post_id" value="${post.id}">
    <input type="text" name="content" id="commentContent">
</form>
    <button onclick="commentCreate()">작성</button>
</div>
<button onclick="ajaxCommentsShow()">댓글 새로고침</button>
<div id="commentsDisplay">

</div>

<script>
    $(document).ready(
        function () {
            ajaxCommentsShow();
        }
    );
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
    function ajaxCommentsShow() {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState == 4) {
                // alert(xhr.status);
                if (xhr.status == 200) {

                    let rt = xhr.responseText;
                    // alert(rt);
                    let jo = window.eval("(" + rt + ")");
                    // console.log(rt);

                    if(jo.access){
                        $("#commentsDisplay").empty();
                        for(let comment of jo.data){
                            let commentId = comment.comment_id
                            $("#commentsDisplay").append($("<div></div>").attr("id",comment.comment_id));
                            $("#"+comment.comment_id).append($("<p></p>")
                                .text(decodeURI(comment.user_name)+" : "+decodeURI(comment.content)+"  "+comment.created_date)
                            ).attr("onclick","createReplyForm('"+commentId+"')");
                            for(reply of comment.replies ){
                                $("#"+comment.comment_id).append($("<p></p>")
                                    .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURI(reply.user_name)+" : "+decodeURI(reply.content)+"  "+reply.created_date)
                                );
                            }
                        }
                    }
                }
            }
        };
        xhr.open("get", "comment.pknu?post_id=${post.id}", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send(null);
    }

    function commentCreate() {
        let query = $("form[name=commentWriter]").serialize();
        $.ajax({
            type : 'post',
            url : 'comment/create.pknu',
            data : query,
            dataType : 'json',
            error: function(xhr, status, error){
                alert("error");
                alert(status+ " : "+error);
            },
            success : function(jo){
                console.log(JSON.stringify(jo));
                // alert(JSON.stringify(json));
                if(jo.access){
                    // $("#commentsDisplay").empty();
                    // for(let comment of json.data){
                    //     $("#commentsDisplay").append($("<p></p>")
                    //         .text(decodeURI(comment.user_name)+" : "+decodeURI(comment.content)+"  "+comment.created_date))
                    // }
                    // $("#commentContent").val("");
                    $("#commentsDisplay").empty();
                    for(let comment of jo.data){
                        let commentId = comment.comment_id
                        $("#commentsDisplay").append($("<div></div>").attr("id",comment.comment_id));
                        $("#"+comment.comment_id).append($("<p></p>")
                            .text(decodeURI(comment.user_name)+" : "+decodeURI(comment.content)+"  "+comment.created_date)
                        ).attr("onclick","createReplyForm('"+commentId+"')");
                        for(reply of comment.replies ){
                            $("#"+comment.comment_id).append($("<p></p>")
                                .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURI(reply.user_name)+" : "+decodeURI(reply.content)+"  "+reply.created_date)
                            );
                        }
                    }
                }
            }
        })
    }

    function replyCreate(replyFormName) {
        let query = $("form[name="+replyFormName+"]").serialize();
        $.ajax({
            type : 'post',
            url : 'comment/reply.pknu',
            data : query,
            dataType : 'json',
            error: function(xhr, status, error){
                alert("error");
                alert(status+ " : "+error);
            },
            success : function(jo){
                console.log(JSON.stringify(jo));
                // alert(JSON.stringify(json));
                if(jo.access){
                    // $("#commentsDisplay").empty();
                    // for(let comment of json.data){
                    //     $("#commentsDisplay").append($("<p></p>")
                    //         .text(decodeURI(comment.user_name)+" : "+decodeURI(comment.content)+"  "+comment.created_date))
                    // }
                    // $("#commentContent").val("");
                    $("#commentsDisplay").empty();
                    for(let comment of jo.data){
                        let commentId = comment.comment_id
                        $("#commentsDisplay").append($("<div></div>").attr("id",comment.comment_id));
                        $("#"+comment.comment_id).append($("<p></p>")
                            .text(decodeURI(comment.user_name)+" : "+decodeURI(comment.content)+"  "+comment.created_date)
                        ).attr("onclick","createReplyForm('"+commentId+"')");
                        for(reply of comment.replies ){
                            $("#"+comment.comment_id).append($("<p></p>")
                                .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURI(reply.user_name)+" : "+decodeURI(reply.content)+"  "+reply.created_date)
                            );
                        }
                    }
                }
            }
        })
    }

    function createReplyForm(commentId) {
        // alert(commentId);
        $("#"+commentId).removeAttr("onclick");
        let newForm = $('<form></form>');
        newForm.attr("name",commentId+"_reply");
        newForm.attr("method","post");
        newForm.attr("acceptCharset","UTF-8");
        newForm.append($('<input/>', {type: 'hidden', name: 'post_id', value:'${post.id}' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'parent_comment_id', value: commentId }));
        newForm.append($('<input/>', {type: 'text', name: 'content'}).attr("id","replyContent"));
        $("#"+commentId).append(newForm);
        $("#"+commentId).append($("<button >작성</button>").attr("onclick","replyCreate('"+commentId+"_reply" +"')"));
    }
</script>
</body>
</html>
