<?php
$id = $_GET["id"];

connectToDatabase();

$stmt = $mysqli->prepare("select Title, Author, Publisher, Year, Price, Summary, Description from Books where Booksid=$id");
$stmt->execute();
$stmt->bind_result($title, $author, $publisher, $year, $price, $summary, $description);
$stmt->fetch();

$title = stripcslashes($title);
$author = stripcslashes($author);
$publisher = stripcslashes($publisher);
$summary = stripcslashes($summary);
$description = stripcslashes($publisher);

$stmt2 = $mysqli2->prepare("select Comment, Picture from Pictures where Bookid=$id");
$stmt2->execute();
$stmt2->bind_result($comment,$image);

$comment = stripcslashes($comment);

echo <<<HEADER
<!DOCTYPE html>
<html>
    <head><title>$title</title>
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
            <fieldset><legend><h1>$title</h1></legend>
            <p>Author: $author</p>
            <p>Publisher: $publisher</p>
            <p>Year: $year</p><br/>
            <p>Summary: $summary</p><br/>
            <p>Description: $description</p><br/>
            <p>Price: $$price</p><br/><br/>
            <table border="1" width="650px">
HEADER;
while ($stmt2->fetch()) {
    $path = './'.$image;
    echo <<<ROW
   <tr>
                        <td width="500px" align="center"><img src="$path" width="100%" height="100%"/></td>
                        <td align="center">$comment</td>
                    </tr>
ROW;
}
echo <<<FOOTER
</table></fieldset>
   <h3><input type='button' onclick="javascript:history.go(-2)" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
FOOTER;
$stmt->close();
$mysqli->close();
$stmt2->close();
$mysqli2->close();




function connectToDatabase() {
    global $mysqli,$mysqli2;
    $mysqli = new mysqli('localhost', 'root', '', 'iab');
    $mysqli2 = new mysqli('localhost', 'root', '', 'iab');

    if (mysqli_connect_errno()) {
        $problem = mysqli_connect_error();
        badinput($problem);
        exit;
    }
}


?>


