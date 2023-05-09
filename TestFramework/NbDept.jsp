<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Document</title>
</head>
<body>
    <% out.println("Formulaire departement"); %>
    <form action="/urlnbdepartements.do" method="post">
        <span>ID </span><input type="text" name="id">
        <span>Nom </span><input type="text" name="nom">
        <input type="submit">
    </form>
</body>
</html>