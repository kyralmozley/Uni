<?php
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/
//to make sure user got to this page by clicking sign up
if(isset($_POST['signup-submit'])) {
  require 'dbh.inc.php';

  $name = $_POST['name'];
  $username = $_POST['uid'];
  $email = $_POST['mail'];
  $password = $_POST['pwd'];
  $passwordRepeat = $_POST['pwd-repeat'];
  $organisation = $_POST['organisation'];
  $country = $_POST['country'];

  //error handlers
  if(empty($username) || empty($email) || empty($password) || empty($passwordRepeat)) {
    header("Location: ../signup.php?error=emptyfields&uid=".$username."&mail=".$email);
    exit();
  } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL) && !preg_match("/^[a-zA-Z0-9]*$/", $username)) {
    header("Location: ../signup.php?error=invalidmailuid");
    exit();
  } else if (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
    header("Location: ../signup.php?error=invalidmail&uid=".$username);
    exit();
  } else if (!preg_match("/^[a-zA-Z0-9]*$/", $username)) {
    header("Location: ../signup.php?error=invaliduid&mail=".$email);
    exit();
  } else if ($password !== $passwordRepeat) {
    header("Location: ../signup.php?error=passwordcheck&uid=".$username."&mail=".$email);
    exit();
  }else if (!preg_match("/^[a-zA-Z0-9]*$/", $organisation)) {
    header("Location: ../signup.php?error=invalidorg&uid=".$username."&mail=".$email);
    exit();
  } else if (!preg_match("/^[a-zA-Z]*$/", $country)) {
    header("Location: ../signup.php?error=invalidcountry&uid=".$username."&mail=".$email);
    exit();
  }
  else {
    //username taken
    $sql = "SELECT uidUsers FROM userInfo WHERE uidUsers=?";
    $stmt = mysqli_stmt_init($conn);
    if (!mysqli_stmt_prepare($stmt, $sql)) {
      header("Location: ../signup.php?error=sqlerror");
      exit();
    } else {
      mysqli_stmt_bind_param($stmt, "s", $username);
      mysqli_stmt_execute($stmt);
      mysqli_stmt_store_result($stmt);
      $resultCheck = mysqli_stmt_num_rows($stmt);
      if ($resultCheck > 0) {
        header("Location: ../signup.php?error=usertaken&mail=".$email);
        exit();
      } else {
        //should be no errors now
        $sql = "INSERT INTO userInfo (name, uidUsers, emailUsers, pwdUsers, org, country) VALUES (?, ?, ?, ?, ?, ?)";
        $stmt = mysqli_stmt_init($conn);
        if (!mysqli_stmt_prepare($stmt, $sql)) {
          header("Location: ../signup.php?error=sqlerror");
          exit();
        } else {
          //hash password
          $hashedPwd = password_hash($password, PASSWORD_DEFAULT);
          mysqli_stmt_bind_param($stmt, "ssssss", $name, $username, $email, $hashedPwd, $organisation, $country);
          mysqli_stmt_execute($stmt);
          header("Location: ../signup.php?signup=success");
          exit();
        }
      }

    }
  }
  mysqli_stmt_close($stmt);
  mysqli_close($conn);

}

else {
  header("Location: ../signup.php");
  exit();
}
