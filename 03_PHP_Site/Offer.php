<?php

$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    add_offer();
} else {
    display_addoffer_form();
}

function display_addoffer_form() {
    $phpself = $_SERVER["PHP_SELF"];
    echo <<<FORM
    <!DOCTYPE html>
<html>
    <head><title>Sell your books</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/></head>
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
            <h1>See your books</h1>
            <p>We are always interested in adding to our stock of valuable books.
                If you have rare books, first editions, historical publications that 
                you wish to sell then contact us.</p>
            <p>Supply as much detail as you can on your book, and provide an email 
                address where we can contact you. We will reply with an estimate of the 
                value of your book.</p>
            <p>We will give you a final definite offer when you bring your book in for inspection.</p>
            <form action="$phpself" method="POST">
                <fieldset>
                    <legend><h1>Your book for sale</h1></legend>
                    <h2>Your email
                    <input type="text" name="email" size="40"/></h2>
                    <h2>Details</h2>
                    <textarea name="details" rows="5" cols="74"></textarea>
                    <h3><input type="submit" value="Submit details"/></h3>
                </fieldset>
            </form>
            <h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
FORM;
}

function add_offer() {
    global $mysqli;
    $email = $_POST["email"];
    $details = addslashes($_POST["details"]);
    if (empty($email) || empty($details)) {
        badinput(" email and details are needed");
        exit;
    }
    $pat = '/^[a-zA-Z0-9\s"\.@-]+$/';
    if (!preg_match($pat, $email)) {
        badinput(" invalid email");
        exit;
    }
    
    $timezone = "Asia/Singapore";
    if (function_exists('date_default_timezone_set'))
        date_default_timezone_set($timezone);

    $time = date('Y-m-d H:i:s');


    connectToDatabase();

    $query = "insert into Offers values(null, '$email', '$details', '$time')";
    $mysqli->query($query);
    $errorreport = $mysqli->error;
    if (!empty($errorreport)) {
        echo <<<FAILRESPONSE
        <html>
            <head><title>Failure Page</title></head>
            <h1>Database update failed</h1>
        <body>
            <p>$errorreport.</p>
                <a href="./Offer.php">Back</a><a></a>
            </body>
            </html>
FAILRESPONSE;
    } else {
        echo <<<SUCCESSRESPONSE
   <html>
       <head><title>Book offer</title>
       <link rel="stylesheet" type="text/css" href="./css/style1.css"/></head>
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
                <h1>Thank you for your input</h1>
                <p>We will review the details of your book and get back to you if we have 
                might be interested in purchasing it. Please note that we cannot make a binding
                offer for the book until we have inspected it at our shop.</p><br/>
            </div>
            <div class="foot">
                <a href="./index.php">Home</a>
            </div>
        </body>
    </html>
SUCCESSRESPONSE;

        $mysqli->close();
    }
}

function badinput($msg) {
    echo <<<BADINPUT
      <!DOCTYPE html>
<html>
    <head><title>Sell your books</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/></head>
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
            <form>
                <fieldset>
                    <legend><h1>Error</h1></legend>
                    <h2>Input could not be used because $msg.</h2>
                </fieldset>
            </form>
            <h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
BADINPUT;
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

?>
