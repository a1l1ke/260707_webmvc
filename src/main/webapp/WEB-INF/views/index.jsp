<%-- /WEB-INF/views/index.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>어떤 음식인가요</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
</head>
<body>
<h1>점심 뭐 먹지</h1>
<p>${food}</p>
<p>${food.name}</p>
<p>${food.price}</p>
</body>
</html>
