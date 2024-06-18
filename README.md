## Ui Testing Client
Welocome to the TrinityTesting repo. In this repo you will find everything you need to get up and running with Web, mobile or api testing.

## Description
The In this repo you will find multiple projects. Each project serves a purpose.
1. TrinityClient = The project users will write, maintain and develop test cases for web, mobile and api.
2. ParentPom = This project holds ALL the maven dependencies that make it possible for UI testing as well as API testing. No other dependencies will be found in any other project in this repo. This allows for a single place to update and maintain all dependencies.
3. WebPlugin = This project holds all the core code for selenium testing. If you want to write web based UI testing then installing this plugin/Library will be needed.
4. MobilePlugin = This project hold all the core code for appium testing. If you want to write mobile bases UI tests, then this plugin needs to be installed.
5. UtilityPlugin = As the name suggests, this plugin gives you access to utilities like video recording, QR core scanning and creating among many other things.
6. ApiPlugin = This project adds RestAssured to the TrinityClient. This allows for API test.

## Supported operating systems

Mac os
Windows
Linux

## Supported mobile platforms

Androind
IOS

## Supported browsers 

Chrome
Firefox
Edge
Safari
(If there is a binary , the framework can run it.)



## Installation
# Quick start guide:

#### Installing Java JRE & JDK

Java JRE (Java Runtime Environment) and JDK (Java Development Kit) are necessary to run and develop Java applications. Here is a guide on how to install Java JRE & JDK:

##### Windows:
1.Download the latest version of the Java JRE & JDK from the official website [https://www.oracle.com/java/technologies/javase-downloads.html]

2.Run the downloaded installer by double-clicking on the executable file.

3.Follow the instructions to complete the installation.

4.Once the installation is complete, open the Command Prompt and run the command java -version to verify that the JRE is correctly installed.

5.To check the JDK installation, run the command javac -version.

##### Mac:
1.Download the latest version of the Java JRE & JDK from the official website [https://www.oracle.com/java/technologies/javase-downloads.html]

2.Double-click on the .dmg file to open the installer.

3.Follow the instructions to complete the installation.

4.Once the installation is complete, open the terminal and run the command java -version to verify that the JRE is correctly installed.

5.To check the JDK installation, run the command javac -version.

##### Please note that the above instructions may vary slightly depending on your specific version of MacOS or Windows. Also, please note that Oracle JDK is no longer freely available for commercial use after January 2019, you can use OpenJDK instead.

### Installing IntelliJ IDEA
#### Windows
Download the latest version of IntelliJ IDEA from the official website: [https://www.jetbrains.com/idea/download/]
Run the downloaded executable file and follow the on-screen instructions to install the IDE.
Once the installation is complete, launch IntelliJ IDEA from the Start menu.
#### Mac
Download the latest version of IntelliJ IDEA from the official website: [https://www.jetbrains.com/idea/download/]
Open the downloaded DMG file and drag the IntelliJ IDEA icon to the Applications folder.
Once the installation is complete, launch IntelliJ IDEA from the Applications folder.
#### Configuring IntelliJ IDEA
Once IntelliJ IDEA is launched, you will be prompted to select a UI theme. Choose a theme that you prefer and click "Next".
You will then be prompted to import settings from a previous version of the IDE or start with a new project. Choose the option that is appropriate for your needs and click "Next".
In the next step, you can configure plugins for the IDE. You can choose to install recommended plugins or select your own. Once you have made your selections, click "Next".
The final step is to configure the JDK (Java Development Kit) for the IDE. If you already have JDK installed on your system, you can select it from the list. Otherwise, you can download and install JDK from the Oracle website: [https://www.oracle.com/java/technologies/javase-downloads.html]
After configuring the JDK, click "Next" and then "Finish" to complete the configuration process.

### Web Testing requirements

### Installing Maven
#### Windows
Download the latest version of Maven from the official website: [https://maven.apache.org/download.cgi]
Extract the contents of the downloaded archive to a directory on your system (e.g. C:\maven)
Add the bin directory of the extracted Maven directory to the PATH environment variable (e.g. C:\maven\bin)
Open a command prompt and run the command
```
mvn -version
```
to verify that Maven is installed and configured correctly.
#### Mac
Install Homebrew (if not already installed) by running the command

```
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/main/install.sh)"
```
Install Maven by running the command

```
brew install maven
```
Verify the installation by running the command

```
mvn -version
```

2.Add the following lines to the file, making sure to replace "path/to" with the actual path of your Java JDK installation:
```
export MAVEN_HOME=path/to/maven
export PATH=$MAVEN_HOME/bin:$PATH
```

You are now ready to use IntelliJ IDEA and Maven for your Java development projects.

#### Adding Environment Variables 
##### Mac:
1.Open terminal and run nano ~/.bash_profile to open the .bash_profile file in the nano text editor.

2.Add the following lines to the file, making sure to replace "path/to" with the actual path of your Java JDK installation:
```
export JAVA_HOME=path/to/jdk
export PATH=$JAVA_HOME/bin:$PATH
```
3.Press CTRL+X to exit the nano text editor, and then press Y to save the changes.

4.Run the command source ~/.bash_profile to apply the changes.

5.To verify that the environment variables have been set correctly, run the command echo $JAVA_HOME and check that the output is the path of your JDK installation.

##### Windows:
1.Press the Windows key + R to open the Run dialog.

2.Type sysdm.cpl and press Enter to open the System Properties dialog.

3.Click the Advanced tab, and then click the Environment Variables button.

4.In the System variables section, click the New button to add a new variable.

5.In the Variable name field, enter JAVA_HOME. In the Variable value field, enter the path of your JDK installation.

6.Click OK to close the Environment Variables dialog.

7.In the System variables section, scroll down and find the variable named Path. Click the Edit button.

8.Click the New button and add %JAVA_HOME%\bin to the variable value field.

9.Click OK to close the Environment Variables dialog.

10.Close the System Properties dialog.

11.To verify that the environment variables have been set correctly, open the Command Prompt and run the command echo %JAVA_HOME% and check that the output is the path of your JDK installation.

Note: In windows 10 you can also set the variable by following this path:
Control Panel > System and Security > System > Advanced system settings > Environment Variables > System Variables

Please note that the above instructions may vary slightly depending on your specific version of MacOS or Windows.

### Mobile Testing requirements

#### Install Node.js:
Appium is built on top of Node.js, so you will need to have Node.js installed on your machine. You can download the latest version of Node.js from the official website [https://nodejs.org/en/download/] and follow the instructions for your operating system.

#### Install Appium:
Once Node.js is installed, you can install Appium by running the following command in your terminal:
```
npm install -g appium@next
```
#### Install Appium Desktop:
Appium Desktop is a graphical front-end to Appium. It allows you to easily start and stop Appium server, inspect your application, and run tests. You can download the latest version of Appium Desktop from the official website [https://github.com/appium/appium-desktop/releases] and follow the instructions for your operating system.

#### Start Appium Server:
Once Appium is installed, you can start the Appium server by running the following command in your terminal:
```
appium
```
#### Verify Appium Installation:
To verify that Appium is installed correctly, you can run the following command in your terminal:
```
appium-doctor
```
This command will check that all necessary dependencies are installed and configured correctly.

##### Please note that this is a basic guide for installing and running Appium 2.0, and there may be additional steps required depending on your specific environment and use case.


#### Setting up Android Studio For emulation testing
Set up your mobile device or emulator: In order to run tests on a mobile device or emulator, you need to configure Appium to connect to it. Depending on the platform you are testing on, you may need to install additional software or configure settings on your device.

1.Install Android Studio: Download and install the latest version of Android Studio from the official website (https://developer.android.com/studio/).

2.Install the Android SDK: During the installation of Android Studio, you will be prompted to install the Android SDK. Make sure to select the correct version of SDK for your operating system and follow the instructions to complete the installation.

3.Install the necessary SDK Platforms and Tools: Once the SDK is installed, open the Android Studio and go to "Configure" > "SDK Manager". In the "SDK Platforms" tab, make sure to check the box next to the version of Android you want to use. 
In the "SDK Tools" tab, make sure to check the boxes next to "Android SDK Build-Tools", "Android Emulator" and "Android SDK Platform-Tools". Once you have selected the necessary tools, click "Apply" to install them.

4.Create an AVD (Android Virtual Device) : An AVD is an emulator configuration that allows you to test your application on different versions of Android without having to use real devices. To create an AVD, open the Android Studio, go to "Configure" > "AVD Manager", and click the "Create Virtual Device" button. Select the device you want to emulate, and the version of Android you want to use, and then click "Next". Select a system image, and then click "Finish".

5.Start the emulator: Once the AVD is created, you can start the emulator by selecting the AVD from the AVD Manager and click "Start" button.

##### Please note that this is a basic guide for setting up Android Studio for Appium to launch an emulator device and there may be additional steps required depending on your specific environment and use case.


## Usage
Using the UI testing framework can be as simple or as complicated as you want it to be. For simplicity all native selenium and Mobile actions have been obfuscated from the framework and can not be edited only read.

## Roadmap
If you have ideas for releases in the future, it is a good idea to list them in the README.

## Authors and acknowledgment
**Garreth Dean**

## License

## Project status
This project is a work in progress. The Trinity client is complete but the plugins are in constant development. Updating of plugins  will be regular.
