<%@page session="false"%>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss" />
    <link href="${bootstrapCss}" rel="stylesheet" />
    <spring:url value="/resources/core/js/jquery.1.10.2.min.js" var="jqueryJs" />
    <script src="${jqueryJs}"></script>
</head>
<body>
<div class="container">
<table class="table table-striped">
    <caption> <h3>${path}</h3></caption>
    <thead>
    <tr>
        <th>Файл</th>
        <th>Размер</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach var="subdirecotry" items="${subdirectories}">
        <tr>
            <td>${subdirecotry.name}</td>
            <td><c:choose>
                    <c:when test="${!subdirecotry.file}">
                        DIR
                    </c:when>
                    <c:otherwise>
                        ${subdirecotry.size}
                    </c:otherwise>
                </c:choose>
            </td>
        </tr>
    </c:forEach>
    </tbody>
</table>
</div>
</body>
</html>
