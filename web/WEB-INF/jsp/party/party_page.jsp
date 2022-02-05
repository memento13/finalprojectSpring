<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<!doctype html>
<html>
<head>
    <head>
        <meta charset="UTF-8">
        <title>${party.name}</title>

        <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.2.0/jquery.min.js"></script>
    </head>
<body>
${party.name} <br>
${party.createDate}<br>
${party.modifiedDate}<br>
<q:if test="${isPartyLeader}">
    <a href="create-project.pknu?party_id=${party.id}">프로젝트 생성</a><br>
</q:if>
<hr>
<div id="project_list">

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
                            $("#project_list").append($("<a></a>").text(decodeURI(vo.project_name)).attr("href","project.pknu?project_id="+vo.project_id));
                            $("#project_list").append($("<button>").text("참가중").click(function () {
                                leaveProject(partyId,vo.project_id);
                            }));
                        } else {
                            // $("#project_list").append(encodeURIComponent(vo.project_name));
                            $("#project_list").append(decodeURI(vo.project_name));
                            $("#project_list").append($("<button>").text("미참가").click(function () {
                                joinProject(partyId,vo.project_id);
                            }));
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
