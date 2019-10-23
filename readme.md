![icon](.gitlab/stoppelmap_logo.png)

# StoppelMap

StoppelMap is an App for Android providing information about Stoppelmarkt, a funfair taking place in every august in the town of Vechta in northern Germany.

In addition to a map of the area which also includes a search for stalls, marquees and fun rides, the app contains a time table for the shuttle busses as well as a schedule for events. For users to be updated about any news, a stream of articles from the official website is also shown, with any new articles trigger push notifications.

[![icon](.gitlab/google_play_badge.png)](https://play.google.com/store/apps/details?id=com.jonasgerdes.stoppelmap)

## Build

Before building the actual app, a preperation script has to be executed by running

```sh
./gradlew runPreparation
```

This will process the geojson file in `preparation/src/main/resources/map` and copies it to the assets folder of the app as well as creating a database file and filling it with data for events, busschedules and information about the stalls read from the geojson.
This is done to keep the data in handy formats which can also be managed by git.

After the preperation a debug version of the app can be build and installed on a connected device or emulator by running

```sh
./gradlew installDebug
```

## Architecture

The project is splitted into modules by feature, while some additional modules containing common logic, interfaces and/or data classes exist, too.
A `core` modules contains base classes as well as base interfaces needed by every feature. Everything comes together in the `app` module which.
![modules](.gitlab/modules.png)
For simplicity, the additional `androidutil` module isn't shown here as most modules depend on it. Also modules for testing (`testutil`, `datastub`) and preparation (`preparation`) are also omitted in the overview.
