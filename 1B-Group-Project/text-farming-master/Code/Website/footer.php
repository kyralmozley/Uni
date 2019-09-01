
<footer>
<!--
/*
CREATED BY KYRA MOZLEY
FOR TEXT FARMING
COMPUTER SCIENCE 1B GROUP PROJECT
-->
<script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=API_KEY&libraries=places"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.1/jquery.min.js"></script>

<script type="text/javascript">
    function init() {
               var input = document.getElementById('locationTextField');
               var autocomplete = new google.maps.places.Autocomplete(input);
               google.maps.event.addListener(autocomplete, 'place_changed', function () {
                   var place = autocomplete.getPlace();
                   console.log(place.name + " " + place.geometry.location
                   + " " + place.geometry.location.lng());
                   //alert("This function is working!");
                  $('#lat').val(place.geometry.location.lat().toFixed(6));
                  $('#long').val(place.geometry.location.lng().toFixed(6));
               });
           }

           google.maps.event.addDomListener(window, 'load', init);
</script>
</footer>

</body>
</html>
