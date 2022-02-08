<%--<%@ page import="java.net.URLEncoder" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<h3>운영 파티</h3>
<div class="list-group">
    <q:forEach items="${leaderList}" var="leaderParty">
        <a href="party.pknu?party_name=${leaderParty.name}" class="list-group-item">${leaderParty.name}</a>
        <%--        <a href="party.pknu?party_name=<%=URLEncoder.encode(leaderParty.name,"UTF-8")%>">${leaderParty.name}</a><br>--%>
    </q:forEach>
</div>
<hr>
<h3>활동 파티</h3>
<div class="list-group">
    <q:forEach items="${memberList}" var="memberParty">
        <a href="party.pknu?party_name=${memberParty.name}" class="list-group-item">${memberParty.name}</a>
        <%--        <a href="party.pknu?party_name=<%=URLEncoder.encode(memberParty.name,"UTF-8")%>">${memberParty.name}</a><br>--%>
    </q:forEach>
</div>
<hr>
