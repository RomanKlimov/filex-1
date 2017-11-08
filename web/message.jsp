<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
         pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
    "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <link href="buttons.css" rel="stylesheet">
        <title>Message window</title>
    </head>
    <body onload='document.getElementById("submit_button").submit();'>
        <h2>${requestScope.message}</h2>
        <form id='submit_button' method="post" action="Folder" autocomplete="off">
            <input type="hidden" name="goTo" value="${requestScope.goTo}">
            <input type="hidden" name="message" value="${requestScope.message}">
            <input type="submit" value="Open" autofocus>
        </form>
    </body>
</html>