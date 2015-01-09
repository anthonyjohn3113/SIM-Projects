<?php

function dosearch() {
    $id = $_POST["id"];
    if($id==""){header('location: viewrecord.php');}
    $pat = '/^[0-9]+$/';
    if (!preg_match($pat, $id)) {
        badinput(" id is invalid");
        exit;
    }
    $mysqli = new mysqli('localhost', 'root', '', 'iab');
    $stmt = $mysqli->prepare("select Booksid from Books where Booksid=$id");
    $stmt->execute();
    $stmt->bind_result($booksid);
    $stmt->fetch();

    if (!($booksid)) {
        echo <<<NORECORD
          <!DOCTYPE html>
<html>
    <head>
        <title>Add Stock</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <h1>Add book</h1>
        <form>
            <fieldset>
                <legend><h1>Record request</h1></legend>
                <h2>No record found</h2>
            </fieldset>
        </form>
        <h3><input type='button' onclick="window.location='./ViewRecord.php'" value='Go Back'/></h3>
</div>        
<div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
NORECORD;
        $stmt->close();
        $mysqli->close();
    } else {
        header("Location: ./AddPicture.php?id=$id");
        $stmt->close();
        $mysqli->close();
    }
}

function display_search_form() {
    $phpself = $_SERVER["PHP_SELF"];
    echo <<<SEARCHFORM
       <!DOCTYPE html>
<html>
    <head>
        <title>Add Stock</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
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
        <h1>Add book</h1>
        <form action='$phpself' method='post'>
            <fieldset>
                <legend><h1>Record request</h1></legend>
                <h2>Book identifier</h2><input type="text" name="id" size="2"/>
                <input type="submit" value="View book record"/>
            </fieldset>
        </form>
        <h3><input type='button' onclick="window.location='./VendorLogin.php'" value='Go Back'/></h3>
</div>        
<div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
SEARCHFORM;
}


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
        <div class="body">
        <h1>Request Failed</h1>
       <fieldset> <legend><h1>Record request</h1></legend>
    <h2>Your request could not be completed because $msg.</h2>
            <h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3></fieldset>
    </div>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
    </body>
</html>
BADINPUT;
}

session_start();
$uname = $_SESSION["vendorusername"];
if (!isset($uname)) {
    header("Location: VendorLogin.php");
    exit;
}
$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    dosearch();
} else {
    display_search_form();
}
?>
