package base;

import com.browserstack.config.BrowserStackConfig;
import com.browserstack.utils.UtilityMethods;
import com.google.gson.JsonObject;

public enum MobilePlatformName {
    ANDROID,
    IOS;

    public static MobilePlatformName getMobilePlatformName(JsonObject deviceOptions) {
        if (UtilityMethods.getBrowserstackEnabled()) {
            return MobilePlatformName.valueOf(BrowserStackConfig.getInstance().getPlatforms().get(0).getCapabilities().get("platformName").toString().toUpperCase());
        } else {
            return MobilePlatformName.valueOf(deviceOptions.get("platformName").getAsString().toUpperCase());
        }
    }
}
