<?php error_reporting(0);
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
*/
require "header.php"
?>
<?php
ob_start();
  session_start();

if (!isset($_SESSION['userId'])) {
  header("Location: login.php");
  exit();
}
?>

<!DOCTYPE html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta http-equiv="content-type" content="text/html; charset=UTF-8"/>
    <title>Using MySQL and PHP with Google Maps</title>
    <style>
      /* Always set the map height explicitly to define the size of the div
       * element that contains the map. */
      #map {
        height: 100%;
        width: 75%;
        display: inline-block;
        float: left;
      }

      #list {
        height: 100%;
        width: 25%;
        left: 75%;
        display: inline-block;
        float: right;
        overflow: scroll;
      }

      /* Optional: Makes the sample page fill the window. */
      html, body {
        height: calc(100vh - 90px);
        margin: 0;
        padding: 0;
      }
    </style>
  </head>

<html>
  <body>
    <div id="map"></div>
    <div id="list">

      <div style="padding-left:20px;">
        <h2 style="margin-bottom:0px;">Crop Health Reports</h2>
        Ordered by date
      </div>

      <hr style="padding:0px; margin-top:10px; margin-bottom:0px">

    </div>

    <script>
        var customLabel = {
        restaurant: {
          label: 'R'
        },
        bar: {
          label: 'B'
        }
      };

        function initMap() {
        var map = new google.maps.Map(document.getElementById('map'), {
          center: new google.maps.LatLng(0.0, 0.0),
          zoom: 2
        });
        var infoWindow = new google.maps.InfoWindow;

          // Change this depending on the name of your PHP or XML file
          downloadUrl('getCropHealthReports.php', function(data) {
            var xml = data.responseXML;
            var markers = xml.documentElement.getElementsByTagName('marker');
            Array.prototype.forEach.call(markers, function(markerElem) {
              // Add marker to map
              var id = markerElem.getAttribute('id');
              var url = markerElem.getAttribute('url');
              var phoneNumber = markerElem.getAttribute('phoneNumber');
              var timestamp = markerElem.getAttribute('timestamp');
              var point = new google.maps.LatLng(
                  parseFloat(markerElem.getAttribute('lat')),
                  parseFloat(markerElem.getAttribute('lng')));

              var infowincontent = document.createElement('div');

              var a = document.createElement('a');
              a.setAttribute('href', url); 
              a.setAttribute('target', '_blank');
              a.textContent = 'Listen to report here'
              infowincontent.appendChild(a);
              infowincontent.appendChild(document.createElement('br'));

              var text = document.createElement('text');
              text.textContent = 'Phone number: +' + phoneNumber
              infowincontent.appendChild(text);
              infowincontent.appendChild(document.createElement('br'));

              var text = document.createElement('text');
              text.textContent = 'Date: ' + timestamp
              infowincontent.appendChild(text);

              var marker = new google.maps.Marker({
                map: map,
                position: point,
              });
              marker.addListener('click', function() {
                infoWindow.setContent(infowincontent);
                infoWindow.open(map, marker);
              });

              // Add report to list
              var list = document.getElementById('list')

              var div = document.createElement('div')
              div.setAttribute('style', 'padding-left:20px; padding-top:20px; padding-bottom:20px;');

              div.onmouseover = function() {
                this.style.backgroundColor = '#A9A9A9'
                document.body.style.cursor = "pointer"
              };

              div.onmouseout = function() {
                this.style.backgroundColor = '';
                document.body.style.cursor = "auto"
              };

              div.onclick = function() {
                infoWindow.setContent(infowincontent);
                infoWindow.open(map, marker);
              };

              var br = document.createElement('BR');

              var a = document.createElement('a');
              a.setAttribute('href', url); 
              a.setAttribute('target', '_blank');
              a.textContent = 'Report submitted on ' + timestamp
              div.appendChild(a);
              div.appendChild(br);

              var text = document.createElement('text')
              text.textContent = 'Phone number: +' + phoneNumber
              div.appendChild(text);
              div.appendChild(br.cloneNode());

              var text = document.createElement('text')
              text.textContent = 'Longitude: ' + parseFloat(markerElem.getAttribute('lng'))
              div.appendChild(text);
              div.appendChild(br.cloneNode());

              var text = document.createElement('text')
              text.textContent = 'Latitude: ' + parseFloat(markerElem.getAttribute('lat'))
              div.appendChild(text);
              div.appendChild(br.cloneNode());

              list.appendChild(div);
              var hr = document.createElement('hr')
              hr.setAttribute('style', 'padding: 0px; margin: 0px;');
              list.appendChild(hr);

            });
          });
        }



      function downloadUrl(url, callback) {
        var request = window.ActiveXObject ?
            new ActiveXObject('Microsoft.XMLHTTP') :
            new XMLHttpRequest;

        request.onreadystatechange = function() {
          if (request.readyState == 4) {
            request.onreadystatechange = doNothing;
            callback(request, request.status);
          }
        };

        request.open('GET', url, true);
        request.send(null);
      }

      function doNothing() {}
    </script>
    <script async defer
    src="https://maps.googleapis.com/maps/api/js?key=API_KEY&callback=initMap">
    </script>
  </body>
</html>

<?php require "footer.php" ?>
