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
- extract the given archive browse to 'Project1'
- run `cd /%path to project Folder%/Project1/src/` in you terminal
- run `javac project1/*.java` in your terminal to compile the program
- run `java project1.Main` in your terminal to run the program


## Running the native mac or windows binary
- Both of these use Java abd JavaFX 24, however they should be bundled with the necessary packages to run without any user interaction
- extract the given archive browse to 'demo'

### For mac
- double click on the provided MustafaCart.dmg
- drag the app to the applications folder
- open launchpad or `⌘` + `Space`
- type in MustafaCart (may take a few seconds to move to Applications folder be patient)
- allow it to access requested folders
- if it says cannot open application from unverified developer
    - `Open settings` → `privacy and security`
    - scroll down and click open app from unverified developer
    - relaunch the application
- Program Files are stored in `/Users/user/Library/MustafaCart`

### For windows
- double click the provided MustafaCart.exe
- Allow app to make changes to your computer if prompted
- app should appear on your desktop
- if an error occours when running it disable windows defender or your anti-virus or allow an exception for the app
- Program Files are stored in `C:\Program Files\MustafaCart`


## Manual compilation
### for manual native binary cimpilation on mac you will need
- homebrew
- maven
- java-jdk-24.x
- javafx-sdk-24 (for mac)

- then run these two commands when in the project directory:(Note same as windows diffrent than terminal)
- `mvn clean package`
- ``` 
    jpackage \
      --type dmg \
      --name MustafaCart \
      --input "'path to package'/demo/target" \
      --main-jar demo-1.0-SNAPSHOT.jar \
      --main-class com.project.three.demo.HelloApplication \
      --java-options "--enable-native-access=ALL-UNNAMED" \
      --add-modules java.base,javafx.controls,javafx.fxml,javafx.graphics,java.logging,  \
      --module-path "$JAVA_HOME/jmods:'path to javafx-sdk'/javafx-sdk-mac-24.0.2/lib"  \
      --java-options "-Dprism.verbose=true -Djava.library.path='path to project'/demo/javafx-sdk-mac-24.0.2/lib" \
      --icon 'path to project'/demo/Mustafa.icns \
      --mac-package-identifier com.example.project3 \
      --mac-package-name MustafaCart \
      --verbose 
      ```

### for manual native binary cimpilation on windows you will need
- choco "for maven install"
- Wix toolset
- maven
- java-jdk-24.x
- javafx-sdk-24.x (for windows)
- javafx-jmods-24.x (for windows)

- then run these two commands when in the project directory:(Note same as mac diffrent than terminal)
- `mvn clean package`
- ``` 
    jpackage ^
      --type exe ^
      --name MustafaCart ^
      --input "'path to project'\demo\target" ^
      --main-jar demo-1.0-SNAPSHOT.jar ^
      --main-class com.project.three.demo.HelloApplication ^
      --java-options "--enable-native-access=ALL-UNNAMED -Dprism.verbose=true -Djava.library.path='path to javafx-sdk'\lib" ^
      --add-modules java.base,javafx.controls,javafx.fxml,javafx.graphics,java.logging ^
      --module-path "%JAVA_HOME%\jmods;C:'path to javfx-jmods'\javafx-jmods-24.0.2" ^
      --icon "'path to project'\Mustafa.ico" ^
      --win-menu ^
      --win-shortcut ^
      --verbose 
      ```
