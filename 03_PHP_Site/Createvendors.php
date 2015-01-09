<?php

$mysqli = 0;

function badinput($msg) {
    echo <<<BADINPUT
    <!DOCTYPE html>
<html>
    <head>
        <title>Bad Input</title>
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
        <h1>Request Failed</h1>
        <p>Your request could not be completed because $msg.</p>
            <input type='button' onclick="window.location='./CreateVendors.php'" value='Go Back'/>
    </div>
    <div class="foot">
            <a href="./index.php">Home</a><a></a>
    </div>
    </body>
</html>
BADINPUT;
}

function display_create_form() {
    $phpself = $_SERVER["PHP_SELF"];
    echo <<<DISPLAYFORM
    <!DOCTYPE html>
    <html>
    <head>
        <title>Create user</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
    </head>
    <body>
        <h1>Create user record</h1>
        <form action='$phpself' method='post'>
            <table border='1'>
                <tr>
                    <th>User Name</th>
                    <td>
                        <input type='text' name='uname' size='16'/>
                    </td>
                </tr>
                <tr>
                    <th>Password</th>
                    <td>
                        <input type='password' name='pwd' size='8'/>
                    </td>
                </tr>
                
                <tr>
                    <td colspan='2' align='center'>
                        <input type='submit' value='Create Record'/>
                    </td>
                </tr>
            </table>
        </form>
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

function handle_create() {
    global $mysqli;
    $uname = $_POST["uname"];
    $pwd = $_POST["pwd"];

    if (empty($uname) || empty($pwd)) {
        display_create_form();
        return;
    }
    $pat = '/^[a-zA-Z0-9 -\.]{4,16}$/';
    if (!preg_match($pat, $uname)) {
        badinput('Invalid Data');
        exit;
    }

    connectToDatabase();
    $cryptpwd = md5($pwd);
    $query = "INSERT INTO Vendors VALUES (null, '$uname', '$cryptpwd')";
    $mysqli->query($query);

    $errreport = $mysqli->error;

    if (!empty($errreport)) {
        echo <<<FAILRESPONSE
        <!DOCTYPE html>
        <html>
    <head>
        <title>Failure Page</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h1>Database update failed</h1>
        <p>$errreport.</p>
    </body>
</html>
FAILRESPONSE;
    } else {
        echo <<<SUCCESSRESPONSE
        <!DOCTYPE html>
        <html>
    <head>
        <title>New User Record</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    </head>
    <body>
        <h1>New name/password record created</h1>
    </body>
</html>
SUCCESSRESPONSE;
    }
    $mysqli->close();
}

$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    handle_create();
} else {
    display_create_form();
}
?>
