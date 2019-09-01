<?php
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/
if(isset($_POST['message-submit'])) {
  $msg = $_POST['message'];
  $lat = $_POST['lat'];
  $long = $_POST['long'];
  $loc_radius = $_POST['radius'];
  $date = $_POST['date'];
  $time = $_POST['time'];
  
  $location = $_POST['location'];

  $expiryDate = $date . "T" . $time . ":00Z";


    ///////////////////////////////
    /// SEND ON IN JSON FORMAT ///
    ///////////////////////////////

  $data = array(
    'message' => $msg,
    'latitude' => (double)$lat,
    'longitude' => (double)$long,
    'locRadius' => (int)$loc_radius,
    'expiryDate' => $expiryDate
  );

  //API Url
  $url = 'http://127.0.0.1:8080/addCustomMessage';

  //Initiate cURL.
  $ch = curl_init($url);

  $json_data = json_encode($data);
  curl_setopt($ch, CURLOPT_POST, 1);

  //Attach our encoded JSON string to the POST fields.
  curl_setopt($ch, CURLOPT_POSTFIELDS, $json_data);

  //Set the content type to application/json
  curl_setopt($ch, CURLOPT_HTTPHEADER, array('Content-Type: application/json'));

  //Execute the request
  $result = curl_exec($ch);
// Further processing ...
    if ($result == True) {
      header("Location: ../message.php?message=success");
    } else {
      header("Location: ../message.php?message=failure");
     }
  /*if(file_put_contents("data.json", $json_data)) {

    header("Location: ../message.php?message=success");
  }*/
  exit();

} else {
  header("Location: ../index.php");
  exit();
}
