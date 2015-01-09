<?php

$nprice = $_POST["nprice"];
$oprice = $_POST["price"];
$id = $_POST["id"];
$patnum = '/^[0-9]+$/';
if (empty($nprice) || !preg_match($patnum, $nprice)) {
        header("Location: ./AddPicture.php?id=$id");;
        exit;
    }
    
$mysqli = new mysqli('localhost', 'root', '', 'iab');
$query = "update Books set Price='$nprice' where Booksid='$id'";
$mysqli->query($query);
$errorreport = $mysqli->error;
if (!empty($errorreport)) {
    echo <<<FAILRESPONSE
      <html>
      <head><title>Failure Page</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/></head>
      <h1>Database update failed</h1>
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
      <p>$errorreport.</p>
             </div>
    <div class="foot">
            <a href="./index.php">Home</a><a></a>
    </div>
      </body>
      </html>
FAILRESPONSE;
} else {
    echo <<<SUCCESS
            <html>
      <head><title>Update Page</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/></head>
      
      <body>
     <div class="head"><h1>Price update successful</h1>
            <table>
                <tr>
                    <th><img src="./siteimages/stack-of-books.jpg"/></th>
                    <td><h1>Illawarra Antiquarian Books</h1></td>
                </tr>
            </table>
        </div>
    <div class="body">
      <fieldset><legend><h1>Price Changed</h1></legend>
    <h2>Old Price was $$oprice</h2>
    <h2>New price is $$nprice.</h2></fieldset>
        <h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3></fieldset>
             </div>
    <div class="foot">
            <a href="./index.php">Home</a><a></a>
    </div>
      </body>
      </html>

SUCCESS;
}

?>

