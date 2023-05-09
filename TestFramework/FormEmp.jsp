<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
    <%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
</head>
<body>
    <% out.println("Formulaire Employe"); %>
    <form action="urlemployerehetra.do" method="post">
        <label for="id"> ID </label><input type="text" name="id" id="id"><br>
        <label for="nom"> Nom </label><input type="text" name="nom" id="nom"><br>
        <label for="age"> Age </label><input type="number" min="18" max="120" name="age" id="age"><br>
        <label for="option1">Celibataire</label><input type="radio" id="option1" name="engaged" value="false"> <br>
        <label for="option2">Marié(e)</label><input type="radio" id="option2" name="engaged" value="true"> <br>
        <input type="submit">
    </form>
</body>
</html>