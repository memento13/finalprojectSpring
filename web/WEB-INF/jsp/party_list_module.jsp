<%--<%@ page import="java.net.URLEncoder" %>--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <q:forEach items="${leaderList}" var="leaderParty">
        <a href="party.pknu?party_name=${leaderParty.name}">${leaderParty.name}</a><br>
        <%--        <a href="party.pknu?party_name=<%=URLEncoder.encode(leaderParty.name,"UTF-8")%>">${leaderParty.name}</a><br>--%>
    </q:forEach>
</div>
<hr>
<div>
    <q:forEach items="${memberList}" var="memberParty">
        <a href="party.pknu?party_name=${memberParty.name}">${memberParty.name}</a><br>
        <%--        <a href="party.pknu?party_name=<%=URLEncoder.encode(memberParty.name,"UTF-8")%>">${memberParty.name}</a><br>--%>
    </q:forEach>
</div>
