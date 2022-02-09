<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <title>post</title>
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<div class="container">
    <div class="row">
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 class="panel-title">${post.title} | ${post.user.name} | ${post.createDate} <a href="post/delete.pknu?post_id=${post.id}"> 삭제 </a></h3>
            </div>
            <div class="panel-body">
                ${post.content}
            </div>
        </div>
        <div class="row">
            <div class="col-md-offset-5">
                <div id="likesDisplay">
                    <p>좋아요 : ${likes}</p> <br>
                </div>
                <div>
                    <input type="button" onclick="ajaxLike('like.pknu?post_id=${post.id}')" value="좋아요" id="likebtn" class="btn btn-info"/>
                </div>
            </div>
        </div>
        <div class="row">
            <div id="likesAjaxMsg" style="margin-top: 10px"></div>
        </div>
    </div>
    <hr>
    <div class="row">
        <div id="commentWriterDisplay" class="row">
            <div class="col-sm-12 col-md-10">
                <form method="post" accept-charset="UTF-8" name="commentWriter" class="form-inline">
                    <textarea rows="3" style="width: 100%; resize: none" class="form-control" name="content" id="commentContent"></textarea>
                    <input type="hidden" name="post_id" value="${post.id}">
                </form>
            </div>
            <div class="col-sm-12 col-md-2">
                <button onclick="commentCreate()" class="btn btn-primary" style="width: 100%; margin-bottom: 5px">작성</button>
                <button onclick="ajaxCommentsShow()" class="btn btn-default" style="width: 100%">댓글 새로고침</button>
            </div>

        </div>

        <div id="commentsDisplay" class="row">

        </div>


    </div>


</div>

<%--<div id="commentWriterDisplay">--%>
<%--<form method="post" accept-charset="UTF-8" name="commentWriter">--%>
<%--    <input type="hidden" name="post_id" value="${post.id}">--%>
<%--    <input type="text" name="content" id="commentContent">--%>
<%--</form>--%>
<%--    <button onclick="commentCreate()">작성</button>--%>
<%--</div>--%>
<%--<button onclick="ajaxCommentsShow()">댓글 새로고침</button>--%>
<%--<div id="commentsDisplay">--%>

<%--</div>--%>

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
                    // alert(rt);
                    let jo = window.eval("(" + rt + ")");
                    let vo = jo.data;
                    if(vo.access){
                        $("#likesDisplay").empty();
                        $("#likesDisplay").append($("<p></p>").text("좋아요 : "+decodeURI(vo.count)))
                        $("#likesDisplay").append($("<br>"));
                    }
                    $("#likesAjaxMsg").empty();
                    $("#likesAjaxMsg").append($("<p></p>").text(decodeURIComponent(vo.msg))).addClass("alert alert-warning");
                    $("#likebtn").attr("disabled",true);
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
                            $("#"+comment.comment_id).append($("<span></span>")
                                .text(decodeURIComponent(comment.user_name)+" : "+decodeURIComponent(comment.content)+"  "+comment.created_date)
                                .attr("onclick","createReplyForm('"+commentId+"')")
                            ).addClass("row");
                            if(!(decodeURIComponent(comment.user_name)=='삭제된 사용자')){
                                $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+commentId+"')"));
                            }
                            $("#"+comment.comment_id).append($("<br>"));
                            for(reply of comment.replies ){
                                $("#"+comment.comment_id).append($("<span></span>")
                                    .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURIComponent(reply.user_name)+" : "+decodeURIComponent(reply.content)+"  "+reply.created_date)
                                );
                                if(!(decodeURI(reply.user_name)=='삭제된 사용자')){
                                    $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+reply.comment_id+"')"));
                                }
                                $("#"+comment.comment_id).append($("<br>"));
                            }
                            $("#"+comment.comment_id).append($("<hr>"));
                            $("#commentsDisplay").append($("<br>"));
                        }
                    }
                }
            }
        };
        xhr.open("get", "comment.pknu?post_id=${post.id}", true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send(null);
    }

    /*
    <form method="post" accept-charset="UTF-8" name="commentWriter" class="form-inline">
                    <textarea rows="3" style="width: 100%; resize: none" class="form-control" name="content" id="commentContent"></textarea>
                    <input type="hidden" name="post_id" value="${post.id}">
                </form>
     */
    function commentCreate() {
        // let query = $("form[name=commentWriter]").serialize();
        let query = '?';
        query += 'content='+encodeURIComponent($("form[name=commentWriter]>textarea[name=content]").val());
        query += '&post_id='+encodeURIComponent($("form[name=commentWriter]>input[name=post_id]").val());
        // let sendJson = {
        //     "post_id":$("form[name=commentWriter]>input[name=post_id]").val(),
        //     "content":$("form[name=commentWriter]>textarea[name=content]").val()
        // }
        $.ajax({
            type : 'post',
            url : 'comment/create.pknu'+query,
            // data : JSON.stringify(sendJson),
            dataType : 'json',
            error: function(xhr, status, error){
                alert("error");
                alert(status+ " : "+error);
                alert(query)
            },
            success : function(jo){
                console.log(JSON.stringify(jo));
                // alert(JSON.stringify(json));
                if(jo.access){
                    $("#commentsDisplay").empty();
                    $("#commentContent").val("");
                    for(let comment of jo.data){
                        let commentId = comment.comment_id
                        $("#commentsDisplay").append($("<div></div>").attr("id",comment.comment_id));
                        $("#"+comment.comment_id).append($("<span></span>")
                            .text(decodeURIComponent(comment.user_name)+" : "+decodeURIComponent(comment.content)+"  "+comment.created_date)
                            .attr("onclick","createReplyForm('"+commentId+"')")
                        );
                        if(!(decodeURIComponent(comment.user_name)=='삭제된 사용자')){
                            $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+commentId+"')"));
                        }
                        $("#"+comment.comment_id).append($("<br>"));
                        for(reply of comment.replies ){
                            $("#"+comment.comment_id).append($("<span></span>")
                                .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURIComponent(reply.user_name)+" : "+decodeURIComponent(reply.content)+"  "+reply.created_date)
                            );
                            if(!(decodeURIComponent(reply.user_name)=='삭제된 사용자')){
                                $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+reply.comment_id+"')"));
                            }
                            $("#"+comment.comment_id).append($("<br>"));
                        }
                        $("#"+comment.comment_id).append($("<hr>"));
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
                // alert(JSON.stringify(json));
                if(jo.access){
                    $("#commentsDisplay").empty();
                    for(let comment of jo.data){
                        let commentId = comment.comment_id
                        $("#commentsDisplay").append($("<div></div>").attr("id",comment.comment_id));
                        $("#"+comment.comment_id).append($("<span></span>")
                            .text(decodeURIComponent(comment.user_name)+" : "+decodeURIComponent(comment.content)+"  "+comment.created_date)
                            .attr("onclick","createReplyForm('"+commentId+"')")
                        );
                        if(!(decodeURIComponent(comment.user_name)=='삭제된 사용자')){
                            $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+commentId+"')"));
                        }
                        $("#"+comment.comment_id).append($("<br>"));
                        for(reply of comment.replies ){
                            $("#"+comment.comment_id).append($("<span></span>")
                                .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURIComponent(reply.user_name)+" : "+decodeURIComponent(reply.content)+"  "+reply.created_date)
                            );
                            if(!(decodeURI(reply.user_name)=='삭제된 사용자')){
                                $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+reply.comment_id+"')"));
                            }
                            $("#"+comment.comment_id).append($("<br>"));
                        }
                        $("#"+comment.comment_id).append($("<hr>"));
                    }
                }
            }
        })
    }

    function createReplyForm(commentId) {
        // alert(commentId);
        $("#"+commentId+">span").removeAttr("onclick");
        let newForm = $('<form></form>').addClass("form-inline");
        newForm.attr("name",commentId+"_reply");
        newForm.attr("method","post");
        newForm.attr("acceptCharset","UTF-8");
        newForm.append($('<input/>', {type: 'hidden', name: 'post_id', value:'${post.id}' }));
        newForm.append($('<input/>', {type: 'hidden', name: 'parent_comment_id', value: commentId }));
        newForm.append($('<input/>', {type: 'text', name: 'content'}).attr("id","replyContent").addClass("form-control col-sm-10"));
        $("#"+commentId).append(newForm);
        $("#"+commentId).append($("<button >작성</button>").attr("onclick","replyCreate('"+commentId+"_reply" +"')").addClass("btn btn-primary col-sm-2"));
        $("#"+commentId).append($("<br><br>"));
    }

    /*
    <div id="commentWriterDisplay" class="row">
            <div class="col-sm-12 col-md-10">
                <form method="post" accept-charset="UTF-8" name="commentWriter" class="form-inline">
                    <textarea rows="3" style="width: 100%; resize: none" class="form-control" name="content" id="commentContent"></textarea>
                    <input type="hidden" name="post_id" value="${post.id}">
                </form>
            </div>
            <div class="col-sm-12 col-md-2">
                <button onclick="commentCreate()" class="btn btn-primary" style="width: 100%; margin-bottom: 5px">작성</button>
                <button onclick="ajaxCommentsShow()" class="btn btn-default" style="width: 100%">댓글 새로고침</button>
            </div>

        </div>
     */
    function deleteComment(commentId) {
        // alert('delete : '+commentId);
        $.ajax({
            type : 'get',
            url : 'comment/delete.pknu?post_id=${post.id}&comment_id='+commentId,
            dataType : 'json',
            error: function(xhr, status, error){
                alert("error");
                alert(status+ " : "+error);
            },
            success : function(jo){
                // alert(JSON.stringify(json));
                if(jo.access){
                    $("#commentsDisplay").empty();
                    for(let comment of jo.data){
                        let commentId = comment.comment_id
                        $("#commentsDisplay").append($("<div></div>").attr("id",comment.comment_id));
                        $("#"+comment.comment_id).append($("<span></span>")
                            .text(decodeURIComponent(comment.user_name)+" : "+decodeURIComponent(comment.content)+"  "+comment.created_date)
                            .attr("onclick","createReplyForm('"+commentId+"')")
                        ).addClass("row");
                        if(!(decodeURIComponent(comment.user_name)=='삭제된 사용자')){
                            $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+commentId+"')"));
                        }
                        $("#"+comment.comment_id).append($("<br>"));
                        for(reply of comment.replies ){
                            $("#"+comment.comment_id).append($("<span></span>")
                                .text("\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0\u00a0"+decodeURIComponent(reply.user_name)+" : "+decodeURIComponent(reply.content)+"  "+reply.created_date)
                            );
                            if(!(decodeURI(reply.user_name)=='삭제된 사용자')){
                                $("#"+comment.comment_id).append($("<a>삭제</a>").attr("onclick","deleteComment('"+reply.comment_id+"')"));
                            }
                            $("#"+comment.comment_id).append($("<br>"));
                        }
                        $("#"+comment.comment_id).append($("<hr>"));
                    }
                }
            }
        })
    }

</script>
</body>
</html>
