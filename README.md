# Hackathon Application README

## Overview

This is a JavaFX application capable of running pong, tetris, dino game, and Packman,

---

---
### Requirements

- **Java Development Kit (JDK) 24**
- All `.class` and `.txt` files should be placed in the same folder


### Running the terminal application

1. Open a terminal or command prompt.
2. Navigate to the folder containing the application files.

## How to Run the Program

### Prerequisites

- Ensure that **Java Development Kit (JDK) 24 or later** is installed.
  You can verify this by running the command:
- `java -version`
  in your terminal
- To launch:
- extract the zip archive
- extract the given archive browse to 'Hackathon'
- run `cd /%path to project Folder%/Hackathon/src/` in you terminal
- run `javac Hackathon/*.java` in your terminal to compile the program
- run `java Hackathon.Main` in your terminal to run the program


## Running the native mac or windows binary
- Both of these use Java abd JavaFX 24, however they should be bundled with the necessary packages to run without any user interaction
- extract the given archive browse to 'demo'


## Manual compilation
### for manual native binary compilation on Mac you will need
- homebrew
- maven
- java-jdk-24.x
- javafx-sdk-24 (for mac)

- then run these two commands when in the project directory:(Note same as windows diffrent than terminal)
- `mvn clean package`
- ``` 
    jpackage \
      --type dmg \
      --name Legacies \
      --input "'path to package'/demo/target" \
      --main-jar hackathon-1.0-SNAPSHOT.jar \
      --main-class com.project.hackathon.hackathon.HelloApplication \
      --java-options "--enable-native-access=ALL-UNNAMED" \
      --add-modules java.base,javafx.controls,javafx.fxml,javafx.graphics,java.logging,  \
      --module-path "$JAVA_HOME/jmods:'path to javafx-sdk'/javafx-sdk-mac-24.0.2/lib"  \
      --java-options "-Dprism.verbose=true -Djava.library.path='path to project'/demo/javafx-sdk-mac-24.0.2/lib" \
      --mac-package-identifier com.example.hackathon \
      --mac-package-name Legacies \
      --verbose 
      ```

### for manual native binary compilation on windows you will need
- choco "for maven install"
- Wix toolset
- maven
- java-jdk-24.x
- javafx-sdk-24.x (for windows)
- javafx-jmods-24.x (for windows)

- then run these two commands when in the project directory:(Note same as mac different from terminal)
- `mvn clean package`
- ``` 
    jpackage ^
      --type exe ^
      --name Legacies ^
      --input "'path to project'\Hackathon\target" ^
      --main-jar Hackathon-1.0-SNAPSHOT.jar ^
      --main-class com.project.hackathon.hackathon.HelloApplication ^
      --java-options "--enable-native-access=ALL-UNNAMED -Dprism.verbose=true -Djava.library.path='path to javafx-sdk'\lib" ^
      --add-modules java.base,javafx.controls,javafx.fxml,javafx.graphics,java.logging ^
      --module-path "%JAVA_HOME%\jmods;C:'path to javfx-jmods'\javafx-jmods-24.0.2" ^
      --win-menu ^
      --win-shortcut ^
      --verbose 
      ```
