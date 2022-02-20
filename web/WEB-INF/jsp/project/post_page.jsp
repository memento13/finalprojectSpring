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
                <h3 class="panel-title">${post.title} | ${post.user.name} | ${post.createDate} <a
                        href="post/delete.pknu?post_id=${post.id}"> 삭제 </a></h3>
            </div>
            <div class="panel-body">
                ${post.content}
            </div>
        </div>
        <div class="row">
            <div class="col-sm-4"></div>
            <div class="col-sm-4">
                <div class="row">
                    <div id="likesDisplay" style="justify-content: center; display: flex">
                        <p>좋아요 : ${likes}</p>
                    </div>
                </div>
                <div class="row">
                    <div style="justify-content: center; display: flex">
                        <input type="button" onclick="ajaxLike('like.pknu?post_id=${post.id}')" value="좋아요" id="likebtn" class="btn btn-info"/>
                    </div>
                </div>
            </div>
            <div class="col-sm-4"></div>
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
                    <textarea rows="3" style="width: 100%; resize: none" class="form-control" name="content"
                              id="commentContent"></textarea>
                    <input type="hidden" name="post_id" value="${post.id}">
                </form>
            </div>
            <div class="col-sm-12 col-md-2">
                <button onclick="commentCreate()" class="btn btn-primary" style="width: 100%; margin-bottom: 5px">작성
                </button>
                <button onclick="ajaxCommentsShow()" class="btn btn-default" style="width: 100%">댓글 새로고침</button>
            </div>

        </div>

        <div id="commentsDisplay" class="row">

        </div>


    </div>


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
                    // alert(rt);
                    let jo = window.eval("(" + rt + ")");
                    let vo = jo.data;
                    if (vo.access) {
                        $("#likesDisplay").empty();
                        $("#likesDisplay").append($("<p></p>").text("좋아요 : " + decodeURI(vo.count)))
                        $("#likesDisplay").append($("<br>"));
                    }
                    $("#likesAjaxMsg").empty();
                    $("#likesAjaxMsg").append($("<p></p>").text(decodeURIComponent(vo.msg))).addClass("alert alert-warning");
                    $("#likebtn").attr("disabled", true);
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

                    if (jo.access) {
                        $("#commentsDisplay").empty();
                        for (let comment of jo.data) {
                            let commentId = comment.comment_id

                            //본 댓글
                            let commentRow = $("<div></div>").attr("id", comment.comment_id).addClass("row");
                            let panelComment = $("<div></div>").addClass("panel panel-default").attr("onclick", "createReplyForm('" + commentId + "')");

                            let panelHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(comment.user_name) + " | " + comment.created_date));
                            if (!(decodeURIComponent(comment.user_name) == '삭제된 사용자')) {
                                panelHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + commentId + "')"));
                            }

                            let panelBody = $("<div></div>").addClass("panel-body")
                                .text(decodeURIComponent(decodeURIComponent(comment.content)));

                            commentRow.append(panelComment.append(panelHead).append(panelBody));

                            let replyPanels = $("<div></div>").addClass("row");
                            for (reply of comment.replies) {
                                // let replyRow = $("<div></div>").addClass("row").append($("<div></div>").addClass("col-sm-2"));
                                let replyRow = $("<div></div>").addClass("row");
                                let panelReply = $("<div></div>").addClass("panel panel-default col-sm-offset-2");
                                let replyHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(reply.user_name) + " | " + reply.created_date));
                                if (!(decodeURIComponent(reply.user_name) == '삭제된 사용자')) {
                                    replyHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + reply.comment_id + "')"));
                                }
                                let replyBody = $("<div></div>").addClass("panel-body")
                                    .text(decodeURIComponent(decodeURIComponent(reply.content)));
                                replyRow.append(panelReply.append(replyHead).append(replyBody));
                                replyPanels.append(replyRow);
                            }

                            $("#commentsDisplay").append(commentRow).append(replyPanels);
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
        let query = '?';
        query += 'content=' + encodeURIComponent($("form[name=commentWriter]>textarea[name=content]").val());
        query += '&post_id=' + encodeURIComponent($("form[name=commentWriter]>input[name=post_id]").val());
        $.ajax({
            type: 'post',
            url: 'comment/create.pknu' + query,
            dataType: 'json',
            error: function (xhr, status, error) {
                alert("error");
                alert(status + " : " + error);
                alert(query)
            },
            success: function (jo) {
                console.log(JSON.stringify(jo));
                // alert(JSON.stringify(json));
                if (jo.access) {
                    $("#commentContent").val("");
                    $("#commentsDisplay").empty();
                    for (let comment of jo.data) {
                        let commentId = comment.comment_id

                        //본 댓글
                        let commentRow = $("<div></div>").attr("id", comment.comment_id).addClass("row");
                        let panelComment = $("<div></div>").addClass("panel panel-default").attr("onclick", "createReplyForm('" + commentId + "')");

                        let panelHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(comment.user_name) + " | " + comment.created_date));
                        if (!(decodeURIComponent(comment.user_name) == '삭제된 사용자')) {
                            panelHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + commentId + "')"));
                        }

                        let panelBody = $("<div></div>").addClass("panel-body")
                            .text(decodeURIComponent(decodeURIComponent(comment.content)));

                        commentRow.append(panelComment.append(panelHead).append(panelBody));

                        let replyPanels = $("<div></div>").addClass("row");
                        for (reply of comment.replies) {
                            // let replyRow = $("<div></div>").addClass("row").append($("<div></div>").addClass("col-sm-2"));
                            let replyRow = $("<div></div>").addClass("row");
                            let panelReply = $("<div></div>").addClass("panel panel-default col-sm-offset-2");
                            let replyHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(reply.user_name) + " | " + reply.created_date));
                            if (!(decodeURIComponent(reply.user_name) == '삭제된 사용자')) {
                                replyHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + reply.comment_id + "')"));
                            }
                            let replyBody = $("<div></div>").addClass("panel-body")
                                .text(decodeURIComponent(decodeURIComponent(reply.content)));
                            replyRow.append(panelReply.append(replyHead).append(replyBody));
                            replyPanels.append(replyRow);
                        }

                        $("#commentsDisplay").append(commentRow).append(replyPanels);
                    }
                }
            }
        })
    }

    function createReplyForm(commentId) {
        // alert(commentId);
        $("#" + commentId + ">div").removeAttr("onclick");

        let newForm = $('<form></form>').addClass("");
        newForm.attr("name", commentId + "_reply");
        newForm.attr("method", "post");
        newForm.attr("acceptCharset", "UTF-8");
        let formGroup = $("<div></div>").addClass("form-group");

        formGroup.append($('<input/>', {type: 'hidden', name: 'post_id', value: '${post.id}'}));
        formGroup.append($('<input/>', {type: 'hidden', name: 'parent_comment_id', value: commentId}));
        formGroup.append($('<input/>', {
            type: 'text',
            name: 'content'
        }).attr("id", "replyContent").addClass("form-control"));
        newForm.append(formGroup);
        newForm = $("<div></div>").addClass(" col-sm-10").append(newForm);
        // $("#" + commentId).append(newForm).append($("<button >작성</button>").attr("onclick", "replyCreate('" + commentId + "_reply" + "')").addClass("btn btn-primary col-sm-2"));
        $("#" + commentId).append(newForm).append($("<button >작성</button>").attr("onclick", "replyCreate('" + commentId + "_reply" + "')").addClass("btn btn-primary col-sm-2"));
        // $("#" + commentId).append($("<button >작성</button>").attr("onclick", "replyCreate('" + commentId + "_reply" + "')").addClass("btn btn-primary col-sm-2"));
    }

    function replyCreate(replyFormName) {
        let query = $("form[name=" + replyFormName + "]").serialize();
        $.ajax({
            type: 'post',
            url: 'comment/reply.pknu',
            data: query,
            dataType: 'json',
            error: function (xhr, status, error) {
                alert("error");
                alert(status + " : " + error);
            },
            success: function (jo) {
                // alert(JSON.stringify(json));
                if (jo.access) {
                    $("#commentsDisplay").empty();
                    for (let comment of jo.data) {
                        let commentId = comment.comment_id

                        //본 댓글
                        let commentRow = $("<div></div>").attr("id", comment.comment_id).addClass("row");
                        let panelComment = $("<div></div>").addClass("panel panel-default").attr("onclick", "createReplyForm('" + commentId + "')");

                        let panelHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(comment.user_name) + " | " + comment.created_date));
                        if (!(decodeURIComponent(comment.user_name) == '삭제된 사용자')) {
                            panelHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + commentId + "')"));
                        }

                        let panelBody = $("<div></div>").addClass("panel-body")
                            .text(decodeURIComponent(decodeURIComponent(comment.content)));

                        commentRow.append(panelComment.append(panelHead).append(panelBody));

                        let replyPanels = $("<div></div>").addClass("row");
                        for (reply of comment.replies) {
                            // let replyRow = $("<div></div>").addClass("row").append($("<div></div>").addClass("col-sm-2"));
                            let replyRow = $("<div></div>").addClass("row");
                            let panelReply = $("<div></div>").addClass("panel panel-default col-sm-offset-2");
                            let replyHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(reply.user_name) + " | " + reply.created_date));
                            if (!(decodeURIComponent(reply.user_name) == '삭제된 사용자')) {
                                replyHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + reply.comment_id + "')"));
                            }
                            let replyBody = $("<div></div>").addClass("panel-body")
                                .text(decodeURIComponent(decodeURIComponent(reply.content)));
                            replyRow.append(panelReply.append(replyHead).append(replyBody));
                            replyPanels.append(replyRow);
                        }

                        $("#commentsDisplay").append(commentRow).append(replyPanels);
                    }
                }
            }
        })
    }

    function deleteComment(commentId) {
        // alert('delete : '+commentId);
        $.ajax({
            type: 'get',
            url: 'comment/delete.pknu?post_id=${post.id}&comment_id=' + commentId,
            dataType: 'json',
            error: function (xhr, status, error) {
                alert("error");
                alert(status + " : " + error);
            },
            success: function (jo) {
                // alert(JSON.stringify(json));
                if (jo.access) {
                    $("#commentsDisplay").empty();
                    for (let comment of jo.data) {
                        let commentId = comment.comment_id

                        //본 댓글
                        let commentRow = $("<div></div>").attr("id", comment.comment_id).addClass("row");
                        let panelComment = $("<div></div>").addClass("panel panel-default").attr("onclick", "createReplyForm('" + commentId + "')");

                        let panelHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(comment.user_name) + " | " + comment.created_date));
                        if (!(decodeURIComponent(comment.user_name) == '삭제된 사용자')) {
                            panelHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + commentId + "')"));
                        }

                        let panelBody = $("<div></div>").addClass("panel-body")
                            .text(decodeURIComponent(decodeURIComponent(comment.content)));

                        commentRow.append(panelComment.append(panelHead).append(panelBody));

                        let replyPanels = $("<div></div>").addClass("row");
                        for (reply of comment.replies) {
                            // let replyRow = $("<div></div>").addClass("row").append($("<div></div>").addClass("col-sm-2"));
                            let replyRow = $("<div></div>").addClass("row");
                            let panelReply = $("<div></div>").addClass("panel panel-default col-sm-offset-2");
                            let replyHead = $("<div></div>").addClass("panel-heading").text(decodeURIComponent(decodeURIComponent(reply.user_name) + " | " + reply.created_date));
                            if (!(decodeURIComponent(reply.user_name) == '삭제된 사용자')) {
                                replyHead.append($("<a>삭제</a>").attr("onclick", "deleteComment('" + reply.comment_id + "')"));
                            }
                            let replyBody = $("<div></div>").addClass("panel-body")
                                .text(decodeURIComponent(decodeURIComponent(reply.content)));
                            replyRow.append(panelReply.append(replyHead).append(replyBody));
                            replyPanels.append(replyRow);
                        }

                        $("#commentsDisplay").append(commentRow).append(replyPanels);
                    }
                }
            }
        })
    }

</script>
</body>
</html>
