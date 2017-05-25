<%--
  Created by IntelliJ IDEA.
  User: nickk
  Date: 5/23/2017
  Time: 2:21 PM
  To change this template use File | Settings | File Templates.
--%>
<!DOCTYPE html>
<html>
<head>
    <title>Place Autocomplete Address Form</title>
    <h1>Please Enter Current Location</h1><br>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
        /* Always set the map height explicitly to define the size of the div
         * element that contains the map. */
        #map {
            height: 100%;
        }
        /* Optional: Makes the sample page fill the window. */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
    </style>
    <link type="text/css" rel="stylesheet" href="https://fonts.googleapis.com/css?family=Roboto:300,400,500">
    <style>
        #locationField, #controls {
            position: relative;
            width: 480px;
        }
        #autocomplete {
            position: absolute;
            top: 0px;
            left: 0px;
            width: 99%;
        }
        .label {
            text-align: right;
            font-weight: bold;
            width: 100px;
            color: #303030;
        }
        #address {
            border: 1px solid #000090;
            background-color: #f0f0ff;
            width: 480px;
            padding-right: 2px;
        }
        #address td {
            font-size: 10pt;
        }
        .field {
            width: 99%;
        }
        .slimField {
            width: 80px;
        }
        .wideField {
            width: 200px;
        }
        #locationField {
            height: 20px;
            margin-bottom: 2px;
        }
    </style>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <style>
        /* Always set the map height explicitly to define the size of the div
         * element that contains the map. */
        #map {
            height: 100%;
        }
        /* Optional: Makes the sample page fill the window. */
        html, body {
            height: 100%;
            margin: 0;
            padding: 0;
        }
    </style>
</head>

<body>
<div id="locationField1">
    <input id="autocomplete" placeholder="Enter your address"
           onFocus="geolocate()" type="text"></input>
</div>
<form name="testForm" method="post" action="/summary">
    <table id="address">
        <tr>
            <td class="label">Street address</td>
            <td class="slimField"><input class="field" id="street_number" name="streetNum"
                                         disabled="true"></input></td>
            <td class="wideField" colspan="2"><input class="field" id="route" name="routee"
                                                     disabled="true"></input></td>
        </tr>
        <tr>
            <td class="label">City</td>
            <!-- Note: Selection of address components in this example is typical.
                 You may need to adjust it for the locations relevant to your app. See
                 https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete-addressform
            -->
            <td class="wideField" colspan="3"><input class="field" id="locality" name="local"
                                                     disabled="true"></input></td>
        </tr>
        <tr>
            <td class="label">State</td>
            <td class="slimField"><input class="field"
                                         id="administrative_area_level_1" disabled="true"></input></td>
            <td class="label">Zip code</td>
            <td class="wideField"><input class="field" id="postal_code" name="postal"
                                         disabled="true"></input></td>
        </tr>
        <tr>
            <td class="label">Country</td>
            <td class="wideField" colspan="3"><input class="field" name="count"
                                                     id="country" disabled="true"></input></td>
        </tr>
        <tr>
        </tr>
    </table>

    <h1>PleasE Enter Destination</h1><br>
    <div id="locationField2">
        <input id="autocomplete1" placeholder="Enter your address"
               onFocus="geolocate()" type="text"></input>
    </div>
    <form name="destForm" method="post" action="/summary">
        <table id="address1">
            <tr>
                <td class="label">Street address</td>
                <td class="slimField"><input class="field" id="street_number1" name="strtN"
                                             disabled="true"></input></td>
                <td class="wideField" colspan="2"><input class="field" id="route1" name="rou"
                                                         disabled="true"></input></td>
            </tr>
            <tr>
                <td class="label">City</td>
                <!-- Note: Selection of address components in this example is typical.
                You may need to adjust it for the locations relevant to your app. See
                https://developers.google.com/maps/documentation/javascript/examples/places-autocomplete-addressform
                -->
                <td class="wideField" colspan="3"><input class="field" id="locality1" name="loca"
                                                         disabled="true"></input></td>
            </tr>
            <tr>
                <td class="label">State</td>
                <td class="slimField"><input class="field"
                                             id="administrative_area_level_11" disabled="true"></input></td>
                <td class="label">Zip code</td>
                <td class="wideField"><input class="field" id="postal_code1" name="posta"
                                             disabled="true"></input></td>
            </tr>
            <tr>
                <td class="label">Country</td>
                <td class="wideField" colspan="3"><input class="field" name="userCountry"
                                                         id="country1" disabled="true"></input></td>
            </tr>
            <tr>
                <td colspan="2">

                    <input type="submit" value="Submit" align="center"/>
                </td>
            </tr>
        </table>
    </form>

    <script>
        // This example displays an address form, using the autocomplete feature
        // of the Google Places API to help users fill in the information.

        // This example requires the Places library. Include the libraries=places
        // parameter when you first load the API. For example:
        // <script src="https://maps.googleapis.com/maps/api/js?key=YOUR_API_KEY&libraries=places">

        var placeSearch, autocomplete, autocomplete2;
        var componentForm = {
            street_number: 'short_name',
            route: 'long_name',
            locality: 'long_name',
            administrative_area_level_1: 'short_name',
            country: 'long_name',
            postal_code: 'short_name'
        };
        var componentForm2 = {
            street_number: 'short_name',
            route: 'long_name',
            locality: 'long_name',
            administrative_area_level_1: 'short_name',
            country: 'long_name',
            postal_code: 'short_name'
        };

        function initAutocomplete() {
            // Create the autocomplete object, restricting the search to geographical
            // location types.
            autocomplete = new google.maps.places.Autocomplete(
                /** @type {!HTMLInputElement} */(document.getElementById('autocomplete')),
                {types: ['geocode']});

            // When the user selects an address from the dropdown, populate the address
            // fields in the form.
            autocomplete.addListener('place_changed', fillInAddress);

            autocomplete2 = new google.maps.places.Autocomplete(
                /** @type {!HTMLInputElement} */(document.getElementById('autocomplete1')),
                {types: ['geocode']});

            // When the user selects an address from the dropdown, populate the address
            // fields in the form.
            autocomplete2.addListener('place_changed', fillInAddress2);
        }

        function fillInAddress() {
            // Get the place details from the autocomplete object.
            var place = autocomplete.getPlace();

            for (var component in componentForm) {
                document.getElementById(component).value = '';
                document.getElementById(component).disabled = false;
            }

            // Get each component of the address from the place details
            // and fill the corresponding field on the form.
            for (var i = 0; i < place.address_components.length; i++) {
                var addressType = place.address_components[i].types[0];
                if (componentForm[addressType]) {
                    var val = place.address_components[i][componentForm[addressType]];
                    document.getElementById(addressType).value = val;
                }
            }
        }
        function fillInAddress2() {

            // Get the place details from the autocomplete object.
            var place = autocomplete2.getPlace();
//        console.log(place);
            for (var component in componentForm2) {
//            console.log(component);
                document.getElementById(component + "1").value = '';
                document.getElementById(component + "1").disabled = false;
            }

            // Get each component of the address from the place details
            // and fill the corresponding field on the form.
            for (var i = 0; i < place.address_components.length; i++) {
                var addressType = place.address_components[i].types[0];
//            console.log(addressType);
                if (componentForm2[addressType]) {
                    var val = place.address_components[i][componentForm2[addressType]];
                    document.getElementById(addressType + "1").value = val;
                }
            }
        }

        // Bias the autocomplete object to the user's geographical location,
        // as supplied by the browser's 'navigator.geolocation' object.
        function geolocate() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(function(position) {
                    var geolocation = {
                        lat: position.coords.latitude,
                        lng: position.coords.longitude
                    };
                    var circle = new google.maps.Circle({
                        center: geolocation,
                        radius: position.coords.accuracy
                    });
                    autocomplete.setBounds(circle.getBounds());
                    autocomplete2.setBounds(circle.getBounds());
                });
            }
        }

    </script><br>
    <script src="https://maps.googleapis.com/maps/api/js?key=AIzaSyDDj_Zhpzv6SH4i-uPWy10gXtOpXfEZYw8&libraries=places&callback=initAutocomplete"
            async defer></script>
</body>
</html>