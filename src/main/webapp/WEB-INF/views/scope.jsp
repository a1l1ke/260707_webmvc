<%-- /WEB-INF/views/scope.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>스코프</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
</head>
<body>
<h1>Request Scope, Session Scope 비교</h1>
<h2>Request Scope</h2>
<p>${rs_uuid}</p>
<p>${rs_ct}</p>
<h2>Session Scope</h2>
<p>${ss_uuid}</p>
<p>${ss_ct}</p>
<p>${ss_count}</p>
</body>
</html>
