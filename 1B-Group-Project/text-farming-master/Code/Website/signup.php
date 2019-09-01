<?php
  require "header.php"
 ?>
<main>

  <?php
  if (isset($_GET['error'])) {
    if($_GET["error"] == "emptyfields") {
      echo '<p> Fill in all the fields! </p>';
    } else if($_GET["error"] == "invaliduidmail") {
      echo '<p> Invalid Username and Email</p>';
    } else if($_GET["error"] == "invaliduid") {
      echo '<p>Invalid Username </p>';
    } else if($_GET["error"] == "invalidmail") {
      echo '<p>Invalid e-mail </p>';
    } else if($_GET["error"] == "passwordcheck") {
      echo '<p>Passwords do not match </p>';
    } else if($_GET["error"] == "usertaken") {
      echo '<p>Username is already taken </p>';
    }  else if($_GET["error"] == "invalidorg") {
      echo '<p>Organisation invalid</p>';
    } else if($_GET["error"] == "invalidcountry") {
      echo '<p>Please enter a valid country</p>';
    }
  } else if($_GET["signup"] == "success") {
    echo '<p>Sign Up Successful </p>';
  }
   ?>
   <div class="signup-container">
     <h1>Sign Up</h1>
  <form class="signup" action="includes/signup.inc.php" method="post">
    <input type="text" name="name" placeholder="Name">
    <input type="text" name="uid" placeholder="Username">
    <input type="text" name="mail" placeholder="Email">
    <input type="password" name="pwd" placeholder="Password">
    <input type="password" name="pwd-repeat" placeholder="Confirm Password">
    <input type="text" name="organisation" placeholder="Organisation">
    <input type="text" name="country" placeholder="Country">
    <button type="submit" name="signup-submit">Sign Up</button>
  </form></div>
</main>

<?php require "footer.php" ?>
