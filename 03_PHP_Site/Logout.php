<?php
session_start();
require_once ("Session.php"); 
clearsessionscookies();
header('location: index.php');
?>
