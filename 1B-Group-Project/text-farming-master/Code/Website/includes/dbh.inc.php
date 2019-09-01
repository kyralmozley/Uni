<?php

$servername = "localhost";
$dBUsername = "papa";
$dBPassword = "textFarming1!";
$dBName = "database";

$conn = mysqli_connect($servername, $dBUsername, $dBPassword, $dBName);

if(!$conn) {
  die("Connection failed: ".mysqli_connect_error());
}
