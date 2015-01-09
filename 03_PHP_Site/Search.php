<?php

function dosearch() {
    $usergenre = $_POST["genre"];
    $mysqli = new mysqli('localhost', 'root', '', 'iab');
    $stmt = $mysqli->prepare("select Booksid from Books where Genre=?");
    $stmt->bind_param('s', $usergenre);
    $stmt->execute();
    $stmt->bind_result($id);

    $matches = array();
    $count = 0;
    while ($stmt->fetch()) {
        $count++;
        $matches[] = $id;
    }
    if ($count == 0) {
        echo <<<NOMATCH
        <!DOCTYPE html>
<html>
    <head>
        <title>My Picture Library</title>
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
            <h1>Search the Stock of Illawarra Antiquairan Books</h1>
            <form>
                <fieldset>
                    <legend><h1>Search request</h1></legend>
                    <h2>There are no books found<h2>
                    <h3>
                    <input type='button' onclick="window.location='./Search.php'" value='Search for other genres'/></h3>
                </fieldset>
            </form>
        </div>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
    </body>
</html>
NOMATCH;
        return;
    }
    echo <<<PAGEHEAD
    <!DOCTYPE html>
<html>
    <head>
        <title>My Picture Library</title>
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
        <h1>Search the Stock of Illawarra Antiquairan Books</h1>
        <fieldset><legend><h1>Search request</h1></legend>
        <table border="1" width="650px">
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Price</th>
                </tr>
            </thead>
            <tbody>
PAGEHEAD;
    $item = 0;
    $stmt = $mysqli->prepare("select title, author, price from Books where Booksid=?");
    foreach ($matches as $matchid) {
        $stmt->bind_param('s', $matchid);
        $stmt->execute();
        $stmt->bind_result($title, $author, $price);
        $stmt->fetch();
        $item++;
        $title = stripcslashes($title);
        $author = stripcslashes($author);
        
        echo <<<TABLEROW
        <tr>
                    <td><a href="./Details.php?id=$matchid">$title</a></td>
                    <td>$author</td>
                    <td><a>$</a>$price</td>
        </tr>
TABLEROW;
    }
    echo <<<PAGEFOOT
    </tbody>
        </table></fieldset>
        <h3><input type='button' onclick="window.location='./Search.php'" value='Search for other genres'/></h3>
    </div>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
    </body>
</html>
PAGEFOOT;
    $stmt->close();
    $mysqli->close();
}

function display_search_form() {
    $phpself = $_SERVER["PHP_SELF"];
    echo <<<FORM
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <title>Illawarra Antiquarian Books</title>
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
            <h1>Search the Stock of Illawarra Antiquairan Books</h1>
            <form action="$phpself" method="POST">
                <fieldset>
                    <legend><h1>Search request</h1></legend>
                    <h2>Genre</h2>
                    <h2><select name="genre">
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
                    </select>
                    <input type='submit' value='Search By Genre'/></h2>
                </fieldset>
            </form>
            <h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3></fieldset>
        </div>
        <div class="foot">
            <a href="./index.php">Home</a>
        </div>
    </body>
</html>
FORM;
}

$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    dosearch();
} else {
    display_search_form();
}
?>


