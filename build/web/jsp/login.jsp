<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
<head>
    <title>Title</title>
    <link rel="stylesheet" href="<c:url value="/css/main.css"/>">
    <link rel="stylesheet" href="<c:url value="buttons.css"/>">
    <link rel="stylesheet" href="<c:url value="links.css"/>">
</head>
<body>
<div class="header">
    <div class="signInSignUp">
        <a class="signIn" href="<c:url value="index.html"/>">To main</a>
        <a class="signIn" href="<c:url value="/jsp/register.jsp"/>">Sign Up</a>
    </div>
</div>
<div class="products">
    <form action="/filex/login" method="post">
        <h1>Login</h1>

        <p>
            <span style="color:red">${wrong}</span>
        </p>

        <p>
            <input name="email" type="text" maxlength="20" placeholder="Введите свой email">
        </p>

        <p>
            <input name="password" type="password" class="form-control input-field" placeholder="Введите свой пароль">
        </p>

        <p>
            <input type="submit" class="hollow-button" value="Sign In">
        </p>

    </form>
</div>
</body>
</html>
