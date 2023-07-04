<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <%@ page import="com.google.gson.Gson" %>
    <%@ page import="java.util.HashMap" %>
    <%@ page import="java.lang.reflect.Type" %>
    <%@ page import="java.lang.Object" %>

</head>
<body>
    <%
        Gson gson = new Gson();
        Object myObject = request.getAttribute("dataJSON");
        String json = gson.toJson(myObject);
        out.println("JSON = " + json);
    %>
</body>
</html>