<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="utf-8" %>
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
    function joinProject() {
        ajaxProjectList();
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
                    alert(rt);
                    let jo = window.eval("(" + rt + ")");
                    $("#project_list").empty();

                    for (let vo of jo.data) {
                        $("#project_list").append(vo.project_name);
                        if (vo.joined) {
                            $("#project_list").append($("<button>").text("참가중").attr("onclick",joinProject()));
                        } else {
                            $("#project_list").append($("<button>").text("미참가"));
                        }
                        $("#project_list").append($("<br>"));
                    }
                }
            }
        };
        xhr.open("get", ajaxUrl, true);
        xhr.send(null);
    }
</script>

</body>
</html>
