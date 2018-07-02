<%@page session="false" %>
<%@taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
        </tbody>
    </table>
</div>

<script>
    $(document).ready(function() {
        <c:forEach var="directory" items="${directories}">
        var dir = {};
        dir['date'] = '${directory.date}';
        dir['path'] = '${directory.path}';
        dir['directoriesCount'] = '${directory.directoriesCount}';
        dir['filesCount'] = '${directory.filesCount}';
        dir['size'] = '${directory.size}';
        dir['file'] = ${directory.file};
        addDirectoryToSortedList(dir);
        </c:forEach>
    });

    function generateItem(directory) {
        var item = "";
        item += ('<tr>');
        item += ('<td id="date">' + directory.date + '</td>');
        item += ('<td id="path">' + directory.path + '</td>');
        item += ('<td id="dir_count">' + directory.directoriesCount + '</td>');
        item += ('<td id="files_count">' + directory.filesCount + '</td>');
        item += ('<td id="size">' + directory.size + '</td>');
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
        var postdata = {};
        postdata["path"] = $("#input_path").val();
        $.ajax({
            type: "POST",
            contentType: "application/json",
            url: "/add_directory",
            data: JSON.stringify(postdata),
            dataType: 'json',
            timeout: 1000000,
            success: function (d) {
                if (d.response != null) {
                    addDirectoryToSortedList(d.response);
                } else if (d.msg != null) {
                    alert("success" + d.msg);
                }
                setupInputControl(false);
            },
            error: function () {
                alert("Ошибка");
                setupInputControl(false);
            }
        });
    }

    function addDirectoryToSortedList(newDirectory) {
        var container = $("#directory_container");
        var length = container.children().length;
        var index = 0;

        container.children().each(function() {
            var currentPath = $( this ).find('#path').text();
            var currentIsFile = ($( this ).find('#is_directory').length === 0);

            if(!currentIsFile && newDirectory.file) {
                index ++;
                return true;
            } else if(currentIsFile && !newDirectory.file) {
                return false;
            }

            //регулярное выражение для литерала только из символов и строк
            var expSplitPattern = /[A-Za-z0-9А-Яа-я]+/g;
            //регулярное выражение для разделения выражения на циферные и буквенные части
            var numberSplitPattern = /([0-9]+)|([a-zA-ZА-Яа-я]+)/g;

            //делим новое и старое значение на группы состоящие из символов и строк
            var newSplit = newDirectory.path.match(expSplitPattern);
            var currentSplit = currentPath.match(expSplitPattern);

            for (var j = 0; j < Math.min(newSplit.length, currentSplit.length); j++) {
                //делим строко-символьные литералы на цифры и символы
                var newNumSplit = newSplit[j].match(numberSplitPattern);
                var currentNumSplit = currentSplit[j].match(numberSplitPattern);
                for (var i = 0; i < Math.min(newNumSplit.length, currentNumSplit.length); i++) {
                    //если i кусок добавляемого не цифра, а i кусок текущего цифра, увеличиваем индекс
                    if(isNaN(newNumSplit[i]) && !isNaN(currentNumSplit[i])) {
                        index ++;
                        return true;
                    //если i кусок добавляемого цифра, а i кусок текущего не цифра, устанавливаем индекс и выходим из функции each
                    } else if (!isNaN(newNumSplit[i]) && isNaN(currentNumSplit[i])) {
                        return false;
                    //если i кусок добавляемого цифра, а i кусок текущего тоже цифра, сравниваем цифровые значения
                    } else if(!isNaN(newNumSplit[i]) && !isNaN(currentNumSplit[i])) {
                        var newNum = parseInt(newNumSplit[i]);
                        var currentNum = parseInt(currentNumSplit[i]);
                        //если значение добавляемого больше текущего увеличиваем индекс
                        if(newNum > currentNum) {
                            index ++;
                            return true;
                        //если значение добавляемого меньше текущего устанавливаем индекс
                        } else if(newNum < currentNum) {
                            return false;
                        //если длина добовляемого больше текущего(например: 0008 и 8) устанавливаем индекс
                        } else if (newNumSplit[i].length > currentNumSplit[i].length) {
                            return false;
                        //если длина добовляемого меньше текущего увеличиваем индекс
                        } else if (newNumSplit[i].length < currentNumSplit[i].length) {
                            index++;
                            return true;
                        }
                    //случай оба значения символьные, сравниваем в алфавитном порядке
                    } else if (newNumSplit[i].toUpperCase() > currentNumSplit[i].toUpperCase()) {
                        index++;
                        return true;
                    } else if (newNumSplit[i].toUpperCase() < currentNumSplit[i].toUpperCase()) {
                        return false;
                    }
                }
                if (newSplit[j].length > currentSplit[j].length) {
                    index++;
                    return true;
                } else if (newSplit[j].length < currentSplit[j].length) {
                    return false;
                }
            }
        });

        if(index === 0) {
            container.prepend(generateItem(newDirectory));
        } else if(index !== length) {
            $("#directory_container tr:eq(" + index + ")").before(generateItem(newDirectory));
        } else {
            $("#directory_container tr:eq(" + (index - 1 )+ ")").after(generateItem(newDirectory));
        }
    }

    function setupInputControl(isDisabled) {
        $("#button_add_directory").prop("disabled", isDisabled);
        $("#input_path").prop("disabled", isDisabled);
    }
</script>
</body>
</html>
