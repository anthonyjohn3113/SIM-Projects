<?php

function badinput($msg) {
    echo <<<BADINPUT
    <!DOCTYPE html>
<html>
    <head>
        <title>Add Stock</title>
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
            <h1>Error</h1>
            <form>
                <fieldset>
        <h2>Your request could not be completed because $msg.</h2>
            </fieldset>
            </form>
            <h3><input type='button' onclick="window.location='./AddStock.php'" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a><a></a>
        </div>
    </body>
</html>
BADINPUT;
}

function display_addstock_form() {
    $phpself = $_SERVER["PHP_SELF"];
    echo <<<ADDSTOCKFORM
    <!DOCTYPE html>
<html>
    <head>
        <title>Add Stock</title>
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
            <h1>Add book</h1>
            <form action='$phpself' method='post'>
                <fieldset>
                    <legend><h1>New Stock</h1></legend>
                    <h2>Genre   
                        <select name="genre">
                            <option value="null">  </option>
                            <option value="Art">Art and architecture</option>
                            <option value="Bio">Biography</option>
                            <option value="Child">Children</option>
                            <option value="Drama">Drama</option>
                            <option value="Erotic">Erotica</option>
                            <option value="History">History</option>
                            <option value="Military">Military</option>
                            <option value="Music">Music</option>
                            <option value="Foreign">Non English Language books</option>
                            <option value="Lit">Novels</option>
                            <option value="Occult">Occult</option>
                            <option value="Phil">Philosophy</option>
                            <option value="Photo">Photography</option>
                            <option value="Poet">Poetry</option>
                            <option value="Pol">Politics and  Economics</option>
                            <option value="Religion">Religion</option>
                            <option value="SciEng">Science and Engineering</option>
                            <option value="Sport">Sport</option>
                            <option value="Travel">Travel and Exploration</option>          
                        </select> Title <input type='text' name='title' size='40'/></h2>
                    <h2>Author  <input type='text' name='author' size='30'/> 
                    Publisher   <input type='text' name='publisher' size='30'/></h2>
                    <h2>Year    <input type='text' name='year' size='4'/>
                      Price   <input type='text' name='price' size='6'/></h2>
                    <h2>Summary</h2><textarea name='summary' rows="10" cols="74"></textarea>
                    <h2>Description (condition, binding etc)</h2><textarea name='description' rows="10" cols="74"></textarea><br/><br/>
                    <h3><input type='submit' value='Add record'/></h3>
                </fieldset>
            </form>
            <h3><input type='button' onclick="window.location='./VendorLogin.php'" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a><a></a>
        </div>
    </body>
</html>
ADDSTOCKFORM;
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

function add_stock() {
    global $mysqli;
    $genre = $_POST["genre"];
    $title = addslashes($_POST["title"]);
    $author = addslashes($_POST["author"]);
    $publisher = addslashes($_POST["publisher"]);
    $year = $_POST["year"];
    $price = $_POST["price"];
    $summary = addslashes($_POST["summary"]);
    $description = addslashes($_POST["description"]);
    if ($genre == "null") {
        badinput(" genre is missing");
        exit;
    }
    if (empty($title)) {
        badinput(" title is missing");
        exit;
    }
    if (empty($author)) {
        badinput(" author is missing");
        exit;
    }
    if (empty($publisher)) {
        badinput(" publisher is missing");
        exit;
    }
    if (empty($year)) {
        badinput(" year is missing");
        exit;
    }
    if (empty($price)) {
        badinput(" price is missing");
        exit;
    }
    if (empty($summary)) {
        badinput(" summary is missing");
        exit;
    }
    $pat = '/^[a-zA-Z0-9\s.,();&\\\\]+$/';
    if (!preg_match($pat, $author)) {
        badinput(" invalid author(try removing apostrophes and dashes)");
        exit;
    }
    if (!preg_match($pat, $publisher)) {
        badinput(" invalid publisher(try removing apostrophes and dashes)");
        exit;
    }
    $patnum = '/^[0-9]+$/';
    if (!preg_match($patnum, $year)) {
        badinput(" invalid year");
        exit;
    }
    $patnum2 = '/^[0-9\s.$]+$/';
    if (!preg_match($patnum2, $price)) {
        badinput(" invalid price");
        exit;
    }
    
    connectToDatabase();

    $query = "insert into Books values(null, '$genre','$title','$author','$publisher','$year','$price','$summary','$description')";
    $mysqli->query($query);
    $errorreport = $mysqli->error;
    if (!empty($errorreport)) {
        echo <<<FAILRESPONSE
        <html>
            <head><title>Failure Page</title></head>
            <h1>Database update failed</h1>
        <body>
            <p>$errorreport.</p>
                <a href="./AddStock.php">Back</a><a></a>
            </body>
            </html>
FAILRESPONSE;
    } else {
        $result = $mysqli->query("SELECT LAST_INSERT_ID()");
        $row = $result->fetch_row();
        $ident = $row[0];
        echo <<<SUCCESSRESPONSE
   <html>
       <head><title>Book record added</title>
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
                <h1>Book record added</h1>
                <p>The record was added as book number #$ident.</p><br/>
                <input type='button' onclick="window.location='./AddPicture.php?id=$ident'" value='Add a picture for this book'/>
                <input type='button' onclick="window.location='./AddStock.php?id=$ident'" value='Add another book'/>
            </div>
            <div class="foot">
                <a href="./index.php">Home</a><a></a>
            </div>
        </body>
    </html>
SUCCESSRESPONSE;
        $mysqli->close();
    }
}

session_start();
$uname = $_SESSION["vendorusername"];
if (!isset($uname)) {
    header("Location: VendorLogin.php");
    exit;
}
$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    add_stock();
} else {
    display_addstock_form();
}
?>