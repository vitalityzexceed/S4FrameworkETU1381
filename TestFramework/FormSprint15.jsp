<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Test Invalidation Sessions</title>
    <%@ page import="javax.servlet.http.HttpSession" %>
    <%@ page import="java.util.Enumeration" %>

</head>
<body>
    <form action="URLlogoutemp.do">
        <input type="submit" value="Se deconnecter de toutes les sessions">
    </form>
    <form action="URLdelsession.do">
        <%
            HttpSession sessionObj = request.getSession();
            Enumeration<String> attributeNames = sessionObj.getAttributeNames();

            while(attributeNames.hasMoreElements())
            {
                String attributeName = attributeNames.nextElement();
        %>
                <input type="checkbox" value="<%=attributeName %>" name="FrameWorksessions"> <%=attributeName %>
        <%
            }
        %>
        <input type="submit" value="Deconnecter les sessions">
    </form>
</body>
</html>