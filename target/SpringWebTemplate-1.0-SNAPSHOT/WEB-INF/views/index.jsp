<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%--
  Created by IntelliJ IDEA.
  User: PAS8
  Date: 5/25/2017
  Time: 7:44 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Main</title>
</head>
<body>

<form:form method = "GET" action = "/redirect">
    <table>
        <tr>
            <td>
                <input type = "submit" value = "Search"/>
            </td>
        </tr>
    </table>
</form:form>

</body>
</html>
