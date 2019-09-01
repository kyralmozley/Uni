Java 10 is required to run our project (due to requirements of libraries we used), it can be downloaded from here: https://www.oracle.com/technetwork/java/javase/downloads/java-archive-javase10-4425482.html
This should just work on Windows and macOS. A note for Debian Linux: The JDK download depends on part of the GTK+ UI libararies, and requires the following command to install the required bits and pieces: sudo apt install libcanberra-gtk-module libcanberra-gtk3-module

Once Java 10 is installed, it can be run by simply executing weatherapp27.jar (e.g. from the command line with the command 'java -jar weatherapp27.jar').

Two of our team members worked on a IB group project that made an SMS/Voice calling interface for various data sources, including weather data.
We reused some of the code used to get weather data (though we needed to write quite a bit more for the mountain specific data).
From this codebase we also reused some code based on translating location names into co-ordinates and back.
All JavaFX UI code was written from scratch for this project, and the previous project had no graphical user interface.

The web-based data sources used were:
 - DarkSky.NET for basic weather forecasting for any latitude-longitude co-ordinate
 - Met Office DataPoint for mountain weather forecasts, giving textual weather descriptions for specific mountain ranges and also height-specific data across multiple heights
 - OpenStreetMap's Nominatim for converting location names into latitude-longitude points and back again.

We used several libraries:
- JavaFX, for front-end application development (the version bundled with Java 10)
- ControlsFX, a JavaFX library for making popup boxes. (Version 8.40.15: https://mvnrepository.com/artifact/org.controlsfx/controlsfx/8.40.15)
- GSon, for parsing JSON received from weather and location APIs (Version 2.8.5: https://mvnrepository.com/artifact/com.google.code.gson/gson/2.8.5)
- GMapsFX, an open source library for using Google Maps in JavaFX: https://mvnrepository.com/artifact/com.lynden/GMapsFX/2.12.0
  - Multiple dependencies for GMapsFX:
    - Logback Classic 1.2.1: https://mvnrepository.com/artifact/ch.qos.logback/logback-classic/1.2.1
      - Logback Core 1.2.1: https://mvnrepository.com/artifact/ch.qos.logback/logback-core/1.2.1
    - SLF4J API 1.7.23: https://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.23
    - Mockito Core 2.7.17: https://mvnrepository.com/artifact/org.mockito/mockito-core/2.7.17
    - JUnit 4.10: https://mvnrepository.com/artifact/junit/junit/4.10
