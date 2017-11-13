# Weather-App

About the App features:
-> This weather APP lets user to search for a city and display the weather and forecast for 5 days.
-> Consequently the user can also ask the app to show the weather conditions in his current location.
-> Stores the location of the user previously entered by storing the value unshared preferences.
->Coordinator layout used for the main activity
-> Snack bar displays the information to the user when clicked on icons in the app helping him understand what means what.
-> App displays the weather icon, temperature at the moment, humidity, wind speed, max and min temperatures throughout the day apart from the forecast data.

Edge cases that are taken care of:
-> Doesn’t crash when internet is disabled, when there is no network connectivity and when the city name entered is invalid.
-> Toast messages appears to ask the user to check for internet connection or to enter a valid city name -> Dialog box appears for clicking on the icons when there is no internet enabled.

Things to further implement:
-> I would’ve implemented and handled the cases for when the configuration changes in the app such as orientation changes (portrait to landscape and vice versa) and multi-window mode changes.
-> Currently app doesn’t support Multi-window. When user tries to re-size the app, it displays a toast message saying this app doesn’t support multi-window.
-> More time would have made me implement material design, automated tests with proper MVP pattern. ->Better UI design
