<?php

session_start();
$uname = $_SESSION["vendorusername"];
if (!isset($uname)) {
    header("Location: VendorLogin.php");
    exit;
}
$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    delete();
} else {
    form();
    ;
}

function form() {

    $phpself = $_SERVER["PHP_SELF"];

    $mysqli = new mysqli('localhost', 'root', '', 'iab');
    $stmt = $mysqli->prepare("select offersid, email, details, submitdate from Offers");
    $stmt->execute();
    $stmt->bind_result($id, $email, $details, $time);


    echo <<<HEADER
<!DOCTYPE html>
<html>
    <head><title>Users' book offers</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="./js/multipledelete.js"></script>
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
            <h1>Offers List</h1>
            <form method="post" action="$phpself">
                  <table border="1" width="650px">
                  <thead>
                    <tr>
                        <th width="5px"></th>
                        <th>Email</th>
                        <th>Information</th>
                        <th>Submitted</th>
                    </tr>
                </thead>
                <tbody>
HEADER;
    global $count;
    global $checkbox;
    $count = 0;
    $checkbox[] = array();
    while ($stmt->fetch()) {
        $details = stripcslashes($details);
        echo <<<ROW
                    <tr>
      
                        <td><input type="checkbox" name="checkbox[]" id="checkbox[]"  value="$id" /></td>
                        <td>$email</td>
                        <td>$details</td>
                        <td>$time</td>
                    </tr>
ROW;
        $count++;
    }
    echo <<<FOOTER
                </tbody>
                </table>
                <h3><input id='delete' type='submit' name='delete' value='Delete'/></h3><br/>
                </form>
            <h3><input type='button' onclick="window.location='./VendorLogin.php'" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
FOOTER;
}

function delete() {
    error_reporting(E_ERROR|E_WARNING);
    if ($_POST['delete']) {
        if ($_POST['checkbox']) {
            $checkbox = $_POST['checkbox'];
            $countCheck = count($_POST['checkbox']);

            for ($i = 0; $i < $countCheck; $i++) {
                $del = $checkbox[$i];
                $mysqli2 = new mysqli('localhost', 'root', '', 'iab');
                $query2 = "DELETE FROM `iab`.`offers` WHERE `offers`.`offersid` = $del";
                $mysqli2->query($query2);
                $errorreport = $mysqli2->error;
            }
            if ($errorreport) {
                echo <<<FAILRESPONSE
                <!DOCTYPE html>
<html>
    <head><title>Users' book offers</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="./js/multipledelete.js"></script>
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
   <body>
                  <h1>Offers List</h1>
      <h2>Database update failed</h2>
      <h2>$errorreport.</h2>
      </body>
      </html>
FAILRESPONSE;
            } else {
                echo <<<SUCCESSRESPONSE
   <!DOCTYPE html>
<html>
    <head><title>Users' book offers</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="./js/multipledelete.js"></script>
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
            <h1>Offers List</h1>
                <p>Offers deleted from database.</p><br/>
                <h3><input type='button' onclick="window.location='./ViewOffers.php'" value='Go Back'/></h3>
            </div>
            <div class="foot">
                <a href="./index.php">Home</a>
            </div>
        </body>
      </html>
SUCCESSRESPONSE;
            }
        }
        else{
            echo <<<NONESELECTED
   <!DOCTYPE html>
<html>
    <head><title>Users' book offers</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script type="text/javascript" src="./js/multipledelete.js"></script>
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
            <h1>Offers List</h1>
    <h2>No data selected</h2>
                <h3><input type='button' onclick="window.location='./ViewOffers.php'" value='Go Back'/></h3>
            </div>
            <div class="foot">
                <a href="./index.php">Home</a>
            </div>
      </body>
      </html>
NONESELECTED;
        }
    }
}

?>