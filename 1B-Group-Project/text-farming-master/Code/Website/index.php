<?php error_reporting(0);
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/
?>
<?php
ob_start();
  session_start();
?>
<!DOCTYPE html>
<html>
<head>
  <title>Text Farming</title>
  <link rel="stylesheet" type="text/css" href="stylesheet.css">
</head>
<main>
  <?php
    if (isset($_SESSION['userId'])) {
      header("Location: message.php");
      exit();
    } else {
      header("Location: login.php");
      exit();
    }
   ?>

</main>

<?php require "footer.php" ?>
