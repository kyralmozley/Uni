
<?php
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/
  require "header.php"
 ?>
<main>
  <div class="message">
    <h1> Send Message </h1>
  <?php
    if (isset($_SESSION['userId'])) {
      if($_GET["message"] == "success") {
        echo '<p style="color: #8ACB88;">Message Request Sent</p>';
      }
      echo '

        <form action="includes/message.inc.php" method="post">
            <h6>Date to send message:</h6><input name="date" required="required" type="date"  placeholder="Date" min="'.date("Y-m-d") .'" >
            <h6>Time to send message: </h6><input type="time" name="time"
           min="00:00" max="23:59" required
           pattern="[0-9]{2}:[0-9]{2}">
            <h6></h6> <input name="location" placeholder="Location of Message" required="required" id="locationTextField" type="text" size="50">
            <input type="hidden" name="lat" id="lat"><input type="hidden" name="long" id="long">
          <br><br><input required="required" name="radius" type="number"  placeholder="Radius for Message (in meters)" min="1" max="50000">
          <br><br>
            <textarea name="message" required="required" placeholder="Message to Send" rows="4" cols="50"></textarea>
            <br><br>
            <button type="submit" name="message-submit">Send Message</button>

        </form>
        </div>';
        /*if(isset($_GET['error'])) {
          if($_GET["error"] == "emptymessagefields") {
            echo '<p> Fill in all the fields </p>';

          }
        } else */
    } else {
      header("Location: login.php");
      exit();
    }
   ?>

</main>

<?php require "footer.php" ?>
