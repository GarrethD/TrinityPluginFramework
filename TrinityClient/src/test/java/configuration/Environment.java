package configuration;


import com.google.gson.JsonObject;
import io.restassured.path.json.JsonPath;
import lombok.Getter;
import en.Core.enums.URLS;
import en.Core.models.Device;
import utilities.FileHelper;
import utilities.JsonObjectMapper;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentKlovReporter;
public class Environment {
    public static Properties LoginProp = new Properties();
    public static JsonPath UserCreds;
    @Getter
    private static String username;
    @Getter
    private static String password;
    @Getter
    private static String fullName;

    public static ExtentKlovReporter klov;
   public static ExtentReports extent = new ExtentReports();
    //===========================================For local testing only. All params set here will be overridden by the ci/cd pipelines=============================================
    static String browserSet = "Firefox"; //<-- Base Browser

    static String url = URLS.Example_page.getURL(); //<-- Base URL

    static String user = "User1"; //<-- Base user

    static String device = "Pixel6";//<--Base device
    //=================================================================================================================================================================================

    public static String getDevice(String deviceName) {
        String value = System.getProperty(deviceName);
        if (value == null) {
            System.out.println("No device was selected. Defaulting To standard emulator.");
            return device;
        }
        System.out.println("Booting up tests using " + value + " device");
        return value;
    }

    public static Device getDeviceObject(String deviceName) {
        return JsonObjectMapper.jsonToObject("jsonfiles/devices.json", deviceName, Device.class);
    }

    public static JsonObject getDeviceOptions(String deviceName) {
        return getDeviceObject(deviceName).toJsonObject();
    }

    public static String getBrowser(String browser) {
        String value = System.getProperty(browser);
        if (value == null) {
            System.out.println("Browser was not selected. Default browse will now launch.");
            return browserSet;
        }
        System.out.println("Using " + value + " browser for automated tests");
        return value;
    }

    public static String getEnvironment(String Url) {
        String value = System.getProperty(Url);
        if (value == null) {
            System.out.println("No URL was selected. Defaulting To Base url");
            return url;
        }
        System.out.println("Booting up tests using " + value + " Url");
        return value;
    }

    public static String getXmlFileName(String suiteXmlFile) {
        String value = System.getProperty(suiteXmlFile);
        if (value == null) {
            System.out.println("No Klov Report will be generated. A Extent Report will be generated instead");
            return "Local";
        }
        System.out.println("Klov Report name will be set to : " + suiteXmlFile);
        return value;
    }

    public static void users(String users) {
        try {
            LoginProp.load(new FileInputStream(FileHelper.getFileFromResource(ClassLoader.getSystemClassLoader(), "properties/login.properties")));
            UserCreds = JsonObjectMapper.ReadJsonFile(FileHelper.getFileFromResource(ClassLoader.getSystemClassLoader(), "jsonfiles/usercreds.json").getAbsolutePath());
            switch (users.toUpperCase()) {
                case "USER1":
                    username = LoginProp.getProperty("Username");
                    password = LoginProp.getProperty("Password");
                    fullName = UserCreds.getString("Garreth_D.Name") + " " + UserCreds.getString("Garreth_D.Surname");
                    System.out.println("DEBUG - User1 is now being used");
                    break;
                case "USER2":
                    username = LoginProp.getProperty("Username2");
                    password = LoginProp.getProperty("Password2");
                    fullName = UserCreds.getString("Garreth_D.Name") + " " + UserCreds.getString("Garreth_D.Surname");
                    System.out.println("DEBUG - User2 is now being used");
                    break;
            }
        } catch (
                IOException e) {
            e.printStackTrace();
        }
    }

public static void setupKlov() {
    // Initialize Klov
    klov = new ExtentKlovReporter();
    klov.initMongoDbConnection("localhost", 27018);
    klov.setProjectName("Vestuuren Team");
    klov.setReportName("Ui Testing framework AC");
    klov.initKlovServerConnection("http://localhost");
    extent.attachReporter(klov);
}
    public static void flushReports(ExtentReports extent) {
        if (extent != null) {
            extent.flush();
        }
    }


}
