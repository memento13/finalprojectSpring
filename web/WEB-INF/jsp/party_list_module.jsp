<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="EUC-KR" %>
<%@taglib prefix="q" uri="http://java.sun.com/jsp/jstl/core" %>
<div>
    <q:forEach items="${leaderList}" var="leaderParty">
        ${leaderParty.name}<br>
    </q:forEach>
</div>
<div>
    <q:forEach items="${memberList}" var="memberParty">
        ${memberParty.name}<br>
    </q:forEach>
</div>
