<?php error_reporting(0);
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/ ?>
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
<body>
  <header>
    <nav>
      <a href="#">
        <img src="" alt="">
      </a>
      <ul>
        <li><a href="message.php">Send Message</a></li>
        <li><a href="analytics.php">Analytics</a></li>

      </ul>

      <div class="signin">
        <?php
        if (isset($_SESSION['userId'])) {
          echo '<form action="includes/logout.inc.php" method="post">
            <a href="index.php"><button type="submit" name="logout-submit">Log Out</button></a>
          </form>';
        } else {
          echo '  <form action="includes/login.inc.php" method="post">
              <input type="text" name="userid" placeholder="Username">
              <input type="password" name="pwd" placeholder="Password">
              <button type="submit" name="login-submit">Log In</button>

            </form>';
        }
        ?>


      </div>
    </nav>
  </header>
