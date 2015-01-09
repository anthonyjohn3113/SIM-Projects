 <?php
//Taken from http://go4xpert.blogspot.sg/2012/04/login-and-logout-using-sessions-and.html
function createsessions($username,$password)
{
    $_SESSION["vendorusername"] = $username;
    $_SESSION["vendorpassword"] = md5($password);
    
    if(isset($_POST['remme']))
    {
        //Add additional member to cookie array as per requirement
        setcookie("vendorusername", $_SESSION['vendorusername'], time()+60*60*24*100, "/");
        setcookie("vendorpassword", $_SESSION['vendorpassword'], time()+60*60*24*100, "/");
        return;
    }
}

function clearsessionscookies()
{
    unset($_SESSION['vendorusername']);
    unset($_SESSION['vendorpassword']);
    
    session_unset();    
    session_destroy(); 

    setcookie ("vendorusername", "",time()-60*60*24*100, "/");
    setcookie ("vendorpassword", "",time()-60*60*24*100, "/");
}

function confirmUser($username,$password)
{
    // $md5pass = md5($password); // Not needed any more as pointed by ted_chou12

    /* Validate from the database but as for now just demo username and password */
    if($username == "demo" && $password = "demo")
        return true;
    else
        return false;
}

function checkLoggedin()
{
    if(isset($_SESSION['vendorusername']) AND isset($_SESSION['vendorpassword']))
        return true;
    elseif(isset($_COOKIE['vendorusername']) && isset($_COOKIE['vendorpassword']))
    {
        if(confirmUser($_COOKIE['vendorusername'],$_COOKIE['vendorpassword']))
        {
            createsessions($_COOKIE['vendorusername'],$_COOKIE['vendorpassword']);
            return true;
        }
        else
        {
            clearsessionscookies();
            return false;
        }
    }
    else
        return false;
}
?> 