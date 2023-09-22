package utilities;

public class PlatformDetection {
    /**
     * Determines the mobile platform of a given device name.
     *
     * @param deviceName the name of the device
     * @return the mobile platform of the device (either "IOS" or "Android")
     */
    public static String mobilePlatform(String deviceName) {
        String osPlatform = "";
        if (deviceName.toUpperCase().contains("IPHONE")) {
            osPlatform = "IOS";
        } else {
            osPlatform = "Android";
        }
        return osPlatform;
    }

}
