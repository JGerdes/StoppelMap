![icon](.documentation/stoppelmap_logo.png)

# StoppelMap

StoppelMap is an App for Android providing information about Stoppelmarkt, a funfair taking place
every august in the town of Vechta in northern Germany.

In addition to a map of the area which also includes a search for stalls, marquees and fun rides,
the app contains a time table for the shuttle busses as well as a schedule for events. For users to
be updated about any news, a feed of articles from the official website is also shown, with any new
article trigger a push notification.

[![icon](.documentation/google_play_badge.png)](https://play.google.com/store/apps/details?id=com.jonasgerdes.stoppelmap)

## Build

Before building the actual app, a preparation script has to be executed by running

```sh
./gradlew runPreparation
```

which processes the GeoJSON file in `preparation/src/main/resources`, copies it to the assets
folder of the app, creates a database file and fills it with data for events, bus schedules and
information about the stalls read from the GeoJSON file.
This is done to keep the data in a handy format which can also be managed in git.

After the preparation, a debug version of the app can be build and installed on a connected device
or emulator by running

```sh
./gradlew android:app:installDebug
```
