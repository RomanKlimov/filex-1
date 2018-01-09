<%-- 
    Document   : admin
    Created on : Dec 16, 2017, 8:12:53 PM
    Author     : User
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Admin panel</title>
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/css/materialize.min.css">
        <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
        <link rel="stylesheet" href="css/buttons.css">
        <link rel="stylesheet" href="css/tables.css">
        <link rel="shortcut icon" href="ico/ico.png" type="image/png">
    </head>
    <body>
        <nav>
            <div class="nav-wrapper green lighten-2">
                <a href="index.html" class="brand-logo"><i class="fa fa-cloud" aria-hidden="true"></i>FileX Admin panel</a>
                <a href="#" data-activates="mobile-demo" class="button-collapse"><i class="material-icons">menu</i></a>
                <ul class="right hide-on-med-and-down">
                    <li><a href="getAllLogs">Dump all logs</a></li>
                    <li><a href="index.html">Main Page</a></li>
                    <li><a href="logout">Logout</a></li>
                </ul>
                <ul class="side-nav" id="mobile-demo">
                    <li><a href="getAllLogs">Dump all logs</a></li>
                    <li><a href="index.html">Main Page</a></li>
                    <li><a href="logout">Logout</a></li>
                </ul>
            </div>
        </nav>
        ${users}
        <br>
        <br>
        <br>
        <div class="products">
            <form action="DeleteUser" method="post">
                <h2>Delete user</h2>

                <p>
                    <input name="toDelete" type="email" maxlength="40" placeholder="Enter email to delete user" required>
                </p>


                <p>
                    <input type="submit" class="file-path validate" value="Delete user">
                </p>

            </form>

            <br>
            <br>
            <br>
            <form action="generateKeys" method="post">
                <h2>Generate keys</h2>

                <p>
                    <input name="value" type="number" min="1" max="500" placeholder="Enter keys`s value" required>
                </p>
                <p>
                    <input name="count" type="number" min="1" max="100" placeholder="Enter keys`s count" required>
                </p>
                <p>
                    Valid until: 
                    <input name="timestamp" type="date">
                </p>

                <p>
                    <input type="submit" class="file-path validate" value="Generate">
                </p>

            </form>
            <br>
            <br>
            <br>
            <form action="ChangeUserRole" method="post">
                <h2>Change user role</h2>

                <p>
                    <input name="email" type="email" maxlength="40" placeholder="Enter email" required>
                </p>

                <p>
                    <input name="role" type="radio" id="admin" value="true"/>
                    <label for="admin">Admin</label>
                </p>

                <p>
                    <input name="role" type="radio" id="default" value="false"/>
                    <label for="default">Default</label>
                </p>

                <p>
                    <input type="submit" class="file-path validate" value="Change user`s rights">
                </p>

            </form>
            ${keys}

        </div>
        <script type="text/javascript" src="https://code.jquery.com/jquery-2.1.1.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/0.100.2/js/materialize.min.js"></script>
    </body>
</html>
