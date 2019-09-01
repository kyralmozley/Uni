<?php
require "includes/dbh.inc.php";

function parseToXML($htmlStr)
{
$xmlStr=str_replace('<','&lt;',$htmlStr);
$xmlStr=str_replace('>','&gt;',$xmlStr);
$xmlStr=str_replace('"','&quot;',$xmlStr);
$xmlStr=str_replace("'",'&#39;',$xmlStr);
$xmlStr=str_replace("&",'&amp;',$xmlStr);
return $xmlStr;
}

// Opens a connection to a MySQL server
$conn=mysqli_connect($servername, $dBUsername, $dBPassword, "database");
if (!$conn) {
  die('Not connected : ' . mysqli_error());
}

// Select all the rows in the markers table
$sql = "SELECT * FROM cropHealthReports WHERE 1 ORDER BY timestamp ASC";
$stmt = mysqli_stmt_init($conn);
if (!mysqli_stmt_prepare($stmt, $sql)) {
    die('Failure : ' . mysqli_error());
    exit();
} else {
    mysqli_stmt_execute($stmt);
    $result = mysqli_stmt_get_result($stmt);

    header("Content-type: text/xml");

    // Start XML file, echo parent node
    echo "<?xml version='1.0' ?>";
    echo '<markers>';
    $ind=0;
    // Iterate through the rows, printing XML nodes for each
    while ($row = @mysqli_fetch_assoc($result)){
    // Add to XML document node
    echo '<marker ';
    echo 'id="' . $row['id'] . '" ';
    echo 'url="' . parseToXML($row['url']) . '" ';
    echo 'lat="' . $row['lat'] . '" ';
    echo 'lng="' . $row['lng'] . '" ';
    echo 'phoneNumber="' . $row['phoneNumber'] . '" ';
    echo 'timestamp="' . $row['timestamp'] . '" ';
    echo '/>';
    $ind = $ind + 1;
    }

    // End XML file
    echo '</markers>';
  }
?>
