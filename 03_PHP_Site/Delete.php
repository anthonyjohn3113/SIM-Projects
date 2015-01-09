<?php

$id = $_GET["id"];
deletepictures();
deleterecords();

function deleterecords() {
    global $id;
    $mysqli = new mysqli('localhost', 'root', '', 'iab');
    $query = "DELETE FROM `iab`.`books` WHERE `books`.`booksid` = $id";
    $mysqli->query($query);
    $errorreport = $mysqli->error;

    $mysqli2 = new mysqli('localhost', 'root', '', 'iab');
    $query2 = "DELETE FROM `iab`.`pictures` WHERE `pictures`.`bookid` = $id";
    $mysqli2->query($query2);
    $errorreport2 = $mysqli2->error;


    if (!(empty($errorreport) && empty($errorreport2))) {
        echo <<<FAILRESPONSE
      <html>
      <head><title>Failure Page</title></head>
      <h1>Database update failed</h1>
      <body>
      <p>$errorreport.</p>
      </body>
      </html>
FAILRESPONSE;
    } else {
        echo <<<SUCCESSRESPONSE
      <html>
      <head><title>New Picture Record Inserted</title>
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
                <h1>Record Deleted</h1>
                <p>Record #$id is deleted from database.</p><br/>
                <a href="./StockList.php?id=$id">Back to Stock List</a>
            </div>
            <div class="foot">
                <a href="./index.php">Home</a><a></a>
            </div>
        </body>
      </html>
SUCCESSRESPONSE;
    }
    $mysqli->close();
    $mysqli2->close();
}

function deletepictures() {
    global $id;
    $mysqli3 = new mysqli('localhost', 'root', '', 'iab');
    $stmt = $mysqli3->prepare("select Picture from Pictures where Bookid=$id");
    $stmt->execute();
    $stmt->bind_result($image);


    while ($stmt->fetch()) {
        $path = './' . $image;
        unlink($path);
    }
    $mysqli3->close();
}

?>
