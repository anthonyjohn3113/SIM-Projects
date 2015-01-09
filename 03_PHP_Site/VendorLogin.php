<?php

session_start();
require_once ("Session.php");
if (checkLoggedin()) {
    displayform();
} else {
    $method = $_SERVER["REQUEST_METHOD"];
    if ($method == "POST") {
        handle_login();
    } else {
        display_login_form();
    }
}

function badinput($msg) {
    echo <<<BADINPUT
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
    
    </head>
    <body>
    <div class="head">
            <table>
                <tr>
                    <th><img src="./siteimages/stack-of-books.jpg"/></th>
                    <td><h1>Illawarra Antiquarian Books</h1></td>
                </tr>
            </table>
        </div>
    <div class="body">
        <h1>Login to administer records</h1>
        <form>
            <fieldset>
                <legend><h1>Error</h1></legend>
        <h2>Input could not be used because $msg.</h2>
        <h3><input type='button' onclick="window.location='./VendorLogin.php'" value='Go Back'/></h3>
        </div>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
    </body>
</html>
BADINPUT;
}

function display_login_form() {
    $phpself = $_SERVER["PHP_SELF"];
    echo <<<DISPLAYFORM
<!DOCTYPE html>
<html>
    <head>
        <title>Login</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
    
    </head>
    <body>
    <div class="head">
            <table>
                <tr>
                    <th><img src="./siteimages/stack-of-books.jpg"/></th>
                    <td><h1>Illawarra Antiquarian Books</h1></td>
                </tr>
            </table>
        </div>
    <div class="body">
        <h1>Login to administer records</h1>
        <form action='$phpself' method='post'>
            <fieldset>
                <legend><h1>Login</h1></legend>
                <h2>User name<input type='text' name='uname' size='16'/></h2>
                <h2>Password<input type='password' name='pwd' size='8'/></h2>
                <h3><input type='submit' value='Login'/></h3>
            </fieldset>
        </form>
         </div>
    <div class="foot">
            <a href="./index.php">Home</a><a></a>
    </div>
    </body>
</html>
DISPLAYFORM;
}

function connectToDatabase() {
    global $mysqli;
    $mysqli = new mysqli('localhost', 'root', '', 'iab');

    if (mysqli_connect_errno()) {
        $problem = mysqli_connect_error();
        badinput($problem);
        exit;
    }
}

function handle_login() {
    global $mysqli;
    $uname = $_POST["uname"];
    $pwd = $_POST["pwd"];

    if (empty($uname) || empty($pwd)) {
        display_login_form();
        return;
    }

    $cryptpwd = md5($pwd);
    connectToDatabase();
    $stmt = $mysqli->prepare("select count(*) from Vendors 
        where username=? and cpwd=?");
    $err = $mysqli->error;
    if (!empty($err)) {
        badinput($err);
        exit;
    }
    $stmt->bind_param('ss', $uname, $cryptpwd);
    $stmt->execute();
    $stmt->bind_result($count);
    if ($stmt->fetch() && $count == 1) {
        createsessions($uname, $pwd);
        displayform();
        $stmt->close();
        $mysqli->close();
    } else {
        badinput(' login info is wrong');
    }
}

function displayform() {
    $uname = $_SESSION["vendorusername"];
    echo <<<WELCOME
        <!DOCTYPE html>
        <html>
    <head>
        <title>Welcome</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
    </head>
    <body>
    <div class="head">
            <table>
                <tr>
                    <th><img src="./siteimages/stack-of-books.jpg"/></th>
                    <td><h1>Illawarra Antiquarian Books</h1></td>
                </tr>
            </table>
        </div>
    <div class="body">
        <h1>Vendor tasks</h1>
        <ul>
            <li><a href ="./AddStock.php">Add Stock</a></li>
            <li><a href="./ViewRecord.php">View Records</a></li>
            <li><a href="./StockList.php">List Stocks</a></li>
            <li><a href="./ViewOffers.php">View Offers</a></li>
        </ul>
       <p>Logged in as: $uname  <a href="logout.php">(Logout)</a></p>
        </div>
    <div class="foot">
            <a href="./index.php">Home</a><a></a>
    </div>
    </body>
</html>
WELCOME;
}

?>
