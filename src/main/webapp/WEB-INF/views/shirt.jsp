<%-- /WEB-INF/views/index.jsp --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>셔츠 팝니다</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
</head>
<body>
<h1>셔츠 팔아요</h1>
<form method="post">
    <input name="name" placeholder="셔츠 이름 입력">
    <input name="price" type="number" placeholder="셔츠 가격 입력">
    <button>추가</button>
</form>
</body>
</html>
