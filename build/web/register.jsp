<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Registration</title>
        <link rel="stylesheet" href="<c:url value="css/buttons.css"/>">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="shortcut icon" href="ico/ico.png" type="image/png">
    </head>
    <body>
        <nav>
            <div class="nav-wrapper green lighten-2">
                <a href="index.html" class="brand-logo"><i class="fa fa-cloud" aria-hidden="true"></i>FileX</a>
                <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
                <ul class="right hide-on-med-and-down">
                    <li><a href="index.html">Main Page</a></li>
                    <li><a href="login">Login</a></li>
                </ul>
                <ul class="side-nav" id="mobile-demo">
                    <li><a href="index.html">Main Page</a></li>
                    <li><a href="login">Login</a></li>
                </ul>
            </div>
        </nav>
        <div class="products">
            <h2>Join us</h2>

            <form action="registration" method="post">
                <span style="color: red">${errfirstName}</span>
                <p><input name="firstName" type="text" value="${firstName}" placeholder="First name"></p>

                <span style="color: red">${errsecondName}</span>
                <p><input name="secondName" type="text" value="${secondName}" placeholder="Second Name"></p>

                <span style="color: red">${erremail}</span>
                <p><input name="email" type="text" value="${email}" placeholder="Email Address"></p>

                <span style="color: red">${errpassword}</span>
                <p><input name="password" type="password" placeholder="Password"></p>

                <span style="color: red">${errrepassword}</span></p>
                <p><input name="repassword" type="password" placeholder="Confirm Password"></p>

                <span style="color: red">${errphoneNumber}</span>
                <p><input name="phoneNumber" type="text" value="${phoneNumber}" placeholder="Phone number"></p>

                <span style="color: red">${erraddres}</span>
                <p><input name="address" type="text" value="${addres}" placeholder="Address"></p>

                <input name="btn" type="submit" value="Create Account">
            </form>
        </div>
    </body>
</html>
