<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>


<html>
    <head>
        <title>Login</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="css/buttons.css">
        <link rel="shortcut icon" href="ico/ico.png" type="image/png">
    </head>
    <body>
        <nav>
            <div class="nav-wrapper green lighten-2">
                <a href="index.html" class="brand-logo"><i class="fa fa-cloud" aria-hidden="true"></i>FileX</a>
                <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
                <ul class="right hide-on-med-and-down">
                    <li><a href="index.html">Main Page</a></li>
                    <li><a href="registration">Registration</a></li>
                </ul>
                <ul class="side-nav" id="mobile-demo">
                    <li><a href="index.html">Main Page</a></li>
                    <li><a href="registration">Registration</a></li>
                </ul>
            </div>
        </nav>
        <div class="products">
            <form action="login" method="post">
                <h2>Welcome back</h2>

                <p>${wrong}
                    <span style="color:red"></span>
                </p>

                <p>
                    <input name="email" type="text" maxlength="40" placeholder="Enter email" value="${email}" required>
                </p>

                <p>
                    <input name="password" type="password" class="form-control input-field" placeholder="Enter password">
                </p>

                <p class="">
                    <input type="submit" class="file-path validate" value="Sign In">
                </p>

            </form>

        </div>
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
    </body>
</html>
