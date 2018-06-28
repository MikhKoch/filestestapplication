<%@page session="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tlib" uri="/WEB-INF/taglibrary.tld" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <spring:url value="/resources/core/css/bootstrap.min.css" var="bootstrapCss"/>
    <link href="${bootstrapCss}" rel="stylesheet"/>
    <spring:url value="/resources/core/js/jquery.1.10.2.min.js" var="jqueryJs"/>
    <script src="${jqueryJs}"></script>
</head>
<body>
<div class="container container-fluid">
    <h1>Директории и файлы</h1>
    <div class="row " style="display: flex; flex-flow: row wrap;">
        <div class="col-lg-2">
            <h4 style=" text-align:  center">Новая директория:</h4>
        </div>
        <div class="col-lg-10">
            <div class="input-group">
                <input id="input_path" type="text" class="form-control" placeholder="путь">
                <span class="input-group-btn">
                    <button id="button_add_directory" class="btn btn-default" type="button" onclick="addDirectory()">добавить в список</button>
                </span>
            </div>
        </div>
    </div>
    <table class="table table-striped" id="table">
        <caption><h3>Список директорий и файлов</h3></caption>
        <thead>
        <tr>
            <th>Дата</th>
            <th>Базовая директория</th>
            <th>Директорий</th>
            <th>Файлов</th>
            <th>Суммарный размер файлов</th>
            <th></th>
        </tr>
        </thead>
        <tbody id="directory_container">
        <c:forEach var="directory" items="${tlib:sortDirectories(directories)}">
            <tr id="directory">
                <td id="date">${directory.date}</td>
                <td id="path">${directory.path}</td>
                <td id="dir_count">${directory.directoriesCount}</td>
                <td id="files_count" disabled="false">${directory.filesCount}</td>
                <td id="size">${directory.size}</td>
                <td>
                    <c:if test="${!directory.file}">
                        <input id="is_directory" type="button" class="btn btn-link" value="файлы"
                               onclick="window.open('directory?path='+'${directory.path}')">
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<script>
    function generateItem(directory) {
        var item = "";
        item += ('<tr>');
        item += ('<td>' + directory.date + '</td>');
        item += ('<td>' + directory.path + '</td>');
        item += ('<td>' + directory.directoriesCount + '</td>');
        item += ('<td>' + directory.filesCount + '</td>');
        item += ('<td>' + directory.size + '</td>');
        item += ('<td>');
        if (!directory.file) {
            item += ('<input id="is_directory" type="button" class="btn btn-link" value="файлы" onclick="window.open(\'directory?path=' + directory.path + '\')">');
        }
        item += ('</td>');
        item += ('</tr>');
        return item;
    }

    function addDirectory() {
        setupInputControl(true);
        var postdata = {}
        postdata["path"] = $("#input_path").val()
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/add_directory",
            data: JSON.stringify(postdata),
            dataType: 'json',
            timeout: 100000,
            success: function (d) {
                if (d.response != null) {
                    $("#directory_container").append(generateItem(d.response));
                } else if (d.msg != null) {
                    alert("success" + d.msg);
                }
                setupInputControl(false);
            },
            error: function (e) {
                alert("Ошибка");
                setupInputControl(false);
            }
        });
    }

    function setupInputControl(isDisabled) {
        $("#button_add_directory").prop("disabled", isDisabled);
        $("#input_path").prop("disabled", isDisabled);
    }
</script>
</body>
</html>
