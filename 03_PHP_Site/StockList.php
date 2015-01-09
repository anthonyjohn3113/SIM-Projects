<?php
session_start();
$uname = $_SESSION["vendorusername"];
if (!isset($uname)) {
    header("Location: VendorLogin.php");
    exit;
}
$mysqli = new mysqli('localhost', 'root', '', 'iab');
$stmt = $mysqli->prepare("select Booksid, Title, Author, Price from Books");
$stmt->execute();
$stmt->bind_result($id, $title, $author, $price);


echo <<<HEADER
<!DOCTYPE html>
<html>
    <head><title>Stock List</title>
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/></head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    
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
            <h1>Stock List</h1>
            <table border="1" width="650px">
                <thead>
                    <tr>
                        <th>Id#</th>
                        <th>Title</th>
                        <th>Author</th>
                        <th>Price</th>
                        <th></th>
                    </tr>
                </thead>
                <tbody>
HEADER;
while ($stmt->fetch()) {
    $author = stripcslashes($author);
    $author = stripcslashes($author);
    echo <<<ROW
<tr>
                        <td><a href="./Details.php?id=$id">$id</a></td>
                        <td><a href="./Details.php?id=$id">$title</a></td>
                        <td>$author</td>
                        <td>$$price</td>
                        <td><a href="./AddPicture.php?id=$id">(Edit)</a></td>
                    </tr>
ROW;
}
echo <<<FOOTER
</tbody>
            </table>
            <h3><input type='button' onclick="window.location='./VendorLogin.php'" value='Go Back'/></h3>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a><a></a>
        </div>
    </body>
</html>
FOOTER;
$stmt->close();
$mysqli->close();
?>