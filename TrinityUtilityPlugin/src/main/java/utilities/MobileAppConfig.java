package utilities;

public class MobileAppConfig {
    private static String[]appConfigDetails;

    /**
     * Sets the application configuration details.
     *
     * @param apkFilePath   the file location of the APK
     * @param appName       the app package
     * @param appActivity   the app activity
     */
    public static void setAppConfig(String apkFilePath,String appName,String appActivity)
    {
        appConfigDetails = new String[3];
        appConfigDetails[0] = apkFilePath; //<-- File location of APK
        appConfigDetails[1] = appName; //<-- App package
        appConfigDetails[2] = appActivity; //<-- App acticity
    }

    /**
     * Sets the application configuration details.
     *
     * @param appFilePath   the file location of the APK
     */
    public static void setAppConfig(String appFilePath)
    {
        appConfigDetails = new String[1];
        appConfigDetails[0] = appFilePath; //<-- File location of APK
    }

    /**
     * Gets the application configuration details.
     *
     * @return the application configuration details in a string array
     *
     * @throws NullPointerException     if the appConfigDetails is null
     */
    public static String[] getAppConfig()
    {
        if(appConfigDetails != null ) {
            return appConfigDetails;
        } else {
            System.out.println("The app configuration you are trying to get is empty. Make sure to set the app file path, name and activity before getting it.");
        }
        return null;
    }

}