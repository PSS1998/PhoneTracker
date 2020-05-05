lost-phone-tracker-app
========================

Phone tracker for Android - if your phone has been stolen

Find the [release build APK here](PhoneTracker.apk).

Explanation
-----------
* Send text and get location back
* Uses the GPS location, falls back to the GSM network provider and if both are unavalible will send the in range cellular towers information
* from first use of app, no app icon will be visible on the application launcher unless you send stop password


Resources
---------

Icon: https://www.iconfinder.com/icons/283048/google_maps_locate_location_map_marker_navigation_pin_pointer_position_icon

Cellular Tower
--------------
In order to find cellular tower location based on information use the following API:<br/>
http://mobile.maps.yandex.net/cellid_location/?&cellid={}&operatorid={}&countrycode={}&lac={}<br/>
the cell tower information recived will be in following format:<br/>
type:signalStrenght: <link to cell tower location><br/>
the google maps link will be in the following format:<br/>
https://maps.google.com/?q={lat},{lng}<br/>



----------------------------------------------------------------

project based on https://github.com/nohum/lost-phone-tracker-app
