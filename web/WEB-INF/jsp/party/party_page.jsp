<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <head>
        <title>${party.name}</title>

        <meta charset="UTF-8"/>
        <meta name="viewport" content="width=device-width, initial-scale=1"/>
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css"/>
        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    </head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<div class="container">
    <div class="row">
        <h1>${party.name}</h1>
    </div><hr>
    <div class="row" >
        <div class="list-group" id="project_list">

        </div>
    </div>
    <hr>
    <div class="row">
        <q:if test="${isPartyLeader}">
            <a href="create-project.pknu?party_id=${party.id}" class="btn btn-primary">프로젝트 생성</a>
        </q:if>
        <q:if test="${!isPartyLeader}">
            <a href="party/leave.pknu?party_id=${party.id}" class="btn btn-danger">파티 탈퇴</a>
        </q:if>
        <q:if test="${isPartyLeader}">
            <a href="party/delete.pknu?party_id=${party.id}" class="btn btn-danger">파티삭제</a>
        </q:if>
    </div>

</div>



<script>
    function joinProject(partyId,projectId) {
        // alert(partyId +" : "+ projectId);
        ajaxProjectList("project/join.pknu?party_id="+partyId+"&project_id="+projectId);
    }
    function leaveProject(partyId,projectId) {
        // alert(partyId +" : "+ projectId);
        ajaxProjectList("project/leave.pknu?party_id="+partyId+"&project_id="+projectId);
    }
    $(document).ready(function () {
        ajaxProjectList("project-list.pknu?party_id=${party.id}");
    });

    function ajaxProjectList(ajaxUrl) {
        let xhr = new XMLHttpRequest();
        xhr.onreadystatechange = () => {
            if (xhr.readyState == 4) {
                if (xhr.status == 200) {

                    let rt = xhr.responseText;
                    // alert(rt);
                    let jo = window.eval("(" + rt + ")");
                    $("#project_list").empty();
                    let partyId = "${party.id}"
                    for (let vo of jo.data) {
                        if (vo.joined) {
                            // $("#project_list").append($("<a></a>").text(encodeURIComponent(vo.project_name)).attr("href","project.pknu?project_id="+vo.project_id));
                            $("#project_list").append($("<a></a>").text(decodeURI(vo.project_name)).attr("href","project.pknu?project_id="+vo.project_id).addClass('list-group-item col-sm-10'));
                            $("#project_list").append($("<button>").text("참가중").click(function () {
                                leaveProject(partyId,vo.project_id);
                            }).addClass('btn btn-success col-sm-2'));
                        } else {
                            // $("#project_list").append(encodeURIComponent(vo.project_name));
                            $("#project_list").append($("<a></a>").text(decodeURI(vo.project_name)).addClass('list-group-item disabled col-sm-10'));
                            // $("#project_list").append(decodeURI(vo.project_name));
                            $("#project_list").append($("<button>").text("미참가").click(function () {
                                joinProject(partyId,vo.project_id);
                            }).addClass('btn btn-default col-sm-2'));
                        }
                        $("#project_list").append($("<br>"));
                    }

                    if(!jo.result){
                        alert(jo.msg);
                    }
                }
            }
        };
        xhr.open("get", ajaxUrl, true);
        xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        xhr.send(null);
    }
</script>

</body>
</html>
