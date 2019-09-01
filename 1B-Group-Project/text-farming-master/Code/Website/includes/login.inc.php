<?php
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/
if (isset($_POST['login-submit'])) {
  require "dbh.inc.php";


  $uid = $_POST['userid'];
  $password = $_POST['pwd'];

  if (empty($uid) || empty($password)) {
    header("Location: ../index.php?error=emptyfields");
    exit();
  } else {
    //use prepared statmenets ot make secure system
    $sql = "SELECT * FROM userInfo WHERE uidUsers=?";
    $stmt = mysqli_stmt_init($conn);
    if (!mysqli_stmt_prepare($stmt, $sql)) {
      header("Location: ../index.php?error=sqlerror");
      exit();
    } else {
      mysqli_stmt_bind_param($stmt, "s", $uid);
      mysqli_stmt_execute($stmt);
      $result = mysqli_stmt_get_result($stmt);
      if ($row = mysqli_fetch_assoc($result)) {
        $passwordCheck = password_verify($password, $row['pwdUsers']);
        if($passwordCheck == false) {
          header("Location: ../index.php?error=wrongpassword");
          exit();
        } else if($passwordCheck == true){
          //correct password, log them in
          session_start();
          $_SESSION['userId'] = $row['idUsers'];
          $_SESSION['userUid'] = $row['uidUsers'];
          header("Location: ../message.php?login=success");
          exit();


        } else {
          header("Location: ../index.php?error=wrongpassword");
          exit();
        }
      } else {
        header("Location: ../index.php?error=nouser");
        exit();
      }
    }
  }

} else {
  header("Location: ../index.php");
  exit();
}
