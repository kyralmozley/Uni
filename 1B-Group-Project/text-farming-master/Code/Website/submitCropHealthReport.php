<?php
// Get params via HTTP GET
$url = $_GET['url'];
$lat = $_GET['lat'];
$lng = $_GET['lng'];
$phoneNumber = $_GET['phoneNumber'];

require "includes/dbh.inc.php";

// Opens a connection to a MySQL server
$conn=mysqli_connect($servername, $dBUsername, $dBPassword, "database");
if (!$conn) {
  die('Not connected : ' . mysqli_error());
}

// Get the id of the most recently submitted report and increment it to generate the id for the new report
$sql = "SELECT MAX(id) FROM cropHealthReports";
$stmt = mysqli_stmt_init($conn);
if (!mysqli_stmt_prepare($stmt, $sql)) {
    die('Failure : ' . mysqli_error());
    exit();
} else {
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);
    if ($row = mysqli_fetch_assoc($result)) {
        $id = (int)$row['MAX(id)'] + 1;
    }

    // Insert the values into the database
    $sql = "INSERT INTO cropHealthReports (id, url, lat, lng, phoneNumber, timestamp) VALUES (?, ?, ?, ?, ?, now());";
    $stmt = mysqli_stmt_init($conn);
    if (!mysqli_stmt_prepare($stmt, $sql)) {
        die('Failure : ' . mysqli_error());
        exit();
    } else {
        mysqli_stmt_bind_param($stmt, "sssss", $id, $url, $lat, $lng, $phoneNumber);
        mysqli_stmt_execute($stmt);
        echo "success";
        exit();
    }
}

?>
