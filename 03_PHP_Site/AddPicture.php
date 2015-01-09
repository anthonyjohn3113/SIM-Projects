<?php

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
        <h1>Error</h1>
        <fieldset><h2>Input could not be used because $msg.</h2></fieldset>
                </div><h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
    </body>
</html>
BADINPUT;
}

function dislay_addpic_form() {
    $id = $_GET["id"];
    $mysqli = new mysqli('localhost', 'root', '', 'iab');
    $stmt = $mysqli->prepare("select Title, Author, Publisher, Year, Price from Books where Booksid=$id");
    $stmt->execute();
    $stmt->bind_result($title, $author, $publisher, $year, $price);
    $stmt->fetch();
    $phpself = $_SERVER["PHP_SELF"];
    $title = stripcslashes($title);
    $author = stripcslashes($author);
    $publisher = stripcslashes($publisher);
    echo <<<ADDPICFORM
<!DOCTYPE html>
<html>
    <head>
        <title>$title</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" type="text/css" href="./css/style1.css"/>
        <script type="text/javascript" src="./js/confirmdelete.js"></script>
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
        <h1>$title</h1>
        <p>$author</p>
        <p>$publisher</p>
        <p>$year</p>
        <p>$$price</p>
        <p>Record id #$id</p>
        <form  action="$phpself" method="POST" enctype="multipart/form-data">
            <input type="hidden" name="id" value="$id"/>
            <input type="hidden" name="MAX_FILE_SIZE" value="1000000"/>
            <fieldset>
                <legend><h1>Add picture</h1></legend>
                <h2>Picture(.jpg only)</h2><input type="file" name="image" accept="image/jpg" />
                <h2>Comment with picture</h2><textarea name="comment" cols="75" rows="5"></textarea>
                <h3><input type="submit" value="Add Picture"/></h3>
            </fieldset>
        </form>
        <form action="Update.php" method="POST">
        <input type="hidden" name="id" value="$id"/>
    <input type="hidden" name="price" value="$price"/>
            <fieldset>
                <legend><h1>Update price</h1></legend>
                <h2>New price</h2><input type="text" name="nprice"/>
                <input type="submit" value="Update price"/>
            </fieldset>
        </form>
        <form>
        <input type="hidden" name="id" value="$id"/>
            <fieldset>
            <legend><h1>Delete record</h1></legend>
                <input type="button" onclick="return confirmdelete($id)" value="Delete record"/>
                    
            </fieldset>
        </form>
        <h3><input type='button' onclick="javascript:history.go(-2)" value='Go Back'/></h3></fieldset>
    </div>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
    </body>
</html>
ADDPICFORM;
    $stmt->close();
    $mysqli->close();
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

function add_picture() {
    global $mysqli;
    $error = $_FILES["image"]["error"];
    if ($error != UPLOAD_ERR_OK) {
        echo <<<NOLUCK
        <html>
        <head><title>File Upload Failed</title>
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
        <h1>Upload Failed</h1>
        <fieldset><h2>The upload attempt failed. The error code was $error. </h2></fieldset>
    </div><h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
        </body>
        <html>
NOLUCK;
        exit;
    }
    $filename = $_FILES['image']['tmp_name'];
    $comment = $_POST["comment"];
    if (empty($comment)) {
        badinput(" need comment");
        exit;
    }
    $pat = '/^[a-zA-Z0-9\s"\.;,:!?()]+$/';
    if (!preg_match($pat, $comment)) {
        badinput("invalid data");
        exit;
    }
    $comment = addslashes($comment);

    $numbytes = filesize($filename);
    $handle = fopen($filename, "r");
    if (!$handle) {
        badinput("Failed to open file");
        exit;
    }
    $imagedata = addslashes(fread($handle, $numbytes));
    if (!$imagedata) {
        badinput("Failed to read image");
        exit;
    }
    fclose($handle);

    connectToDatabase();
    $id = $_POST["id"];
    $stmt = $mysqli->prepare("select Genre from Books where Booksid=$id");
    $stmt->execute();
    $stmt->bind_result($genre);
    $stmt->fetch();

    $file_name = $_FILES['image']['name'];
    $random_digit = rand(0000, 9999);
    $new_file_name = $random_digit . $file_name;
    $path = "uploadedimages/" . $genre . "/" . $new_file_name;
    if ($_FILES["image"]) {
        copy($_FILES['image']['tmp_name'], $path);
    }
    $mysqli->close();

    connectToDatabase();
    $query = "insert into Pictures values(null, '$id', '$comment', '$path')";
    $mysqli->query($query);
    $errorreport = $mysqli->error;

    if (!empty($errorreport)) {
        echo <<<FAILRESPONSE
      <html>
      <head><title>Failure Page</title>
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
        <h1>Database update failed</h1>
      <fieldset><h2>$errorreport.</h2></fieldset>
              </div><h3><input type='button' onclick="javascript:history.go(-1)" value='Go Back'/></h3>
    <div class="foot">
            <a href="./index.php">Home</a>
    </div>
      </body>
      </html>
FAILRESPONSE;
    } else {
        $result = $mysqli->query("SELECT LAST_INSERT_ID()");
        $row = $result->fetch_row();
        $ident = $row[0];
        echo <<<SUCCESSRESPONSE
      <html>
      <head><title>New Picture Record Inserted</title>
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
        <input type="hidden" name="id" value="$ident"/>
                <h1>New Picture Record Created</h1>
                <p>Picture is record #$ident.</p><br/>
                <h3><input type='button' onclick="window.location.href='./AddPicture.php?id=$id'" value='Add another picture'/>
<input type='button' onclick="window.location.href='./Details.php?id=$id'" value='View Book'/></h3>
            </div>
            <div class="foot">
                <a href="./index.php">Home</a>
            </div>
        </body>
      </html>
SUCCESSRESPONSE;
    }
    $mysqli->close();
}

session_start();
$uname = $_SESSION["vendorusername"];
if (!isset($uname)) {
    header("Location: VendorLogin.php");
    exit;
}
$method = $_SERVER["REQUEST_METHOD"];
if ($method == "POST") {
    add_picture();
} else {
    dislay_addpic_form();
}
?>
