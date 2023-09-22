package utilities;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import static io.appium.java_client.service.local.flags.GeneralServerFlag.LOG_LEVEL;
import static io.appium.java_client.service.local.flags.GeneralServerFlag.USE_PLUGINS;

public class AppiumServer {

    /**
     * Represents the log levels available for Appium.
     */
    public enum AppiumLogLevel {
        DEBUG,
        INFO,
        WARN,
        ERROR
    }

    /**
     * Gets the Appium driver local service.
     * This method builds the Appium service using the specified service builder,
     * which includes arguments for log level and plugins.
     *
     * @return the Appium driver local service
     */
    public static AppiumDriverLocalService getService() {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder()
                .withArgument(LOG_LEVEL, AppiumLogLevel.ERROR.name().toLowerCase())
                .withArgument(USE_PLUGINS, "appium-dashboard");
        return AppiumDriverLocalService.buildService(serviceBuilder);
    }

    /**
     * Gets the Appium driver local service with the specified log level.
     * This method builds the Appium service using the specified service builder,
     * which includes arguments for log level and plugins.
     *
     * @param logLevel the log level for the Appium service
     * @return the Appium driver local service
     */
    public static AppiumDriverLocalService getService(AppiumLogLevel logLevel) {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder()
                .withArgument(LOG_LEVEL, logLevel.name().toLowerCase())
                .withArgument(USE_PLUGINS, "appium-dashboard");
        return AppiumDriverLocalService.buildService(serviceBuilder);
    }

    /**
     * Gets the Appium driver local service with the specified IP address and port.
     * This method builds the Appium service using the specified service builder,
     * which includes arguments for IP address, port, log level, and plugins.
     *
     * @param ipAddress the IP address for the Appium service
     * @param port the port for the Appium service
     * @return the Appium driver local service
     */
    public static AppiumDriverLocalService getService(String ipAddress, int port) {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .usingPort(port)
                .withArgument(LOG_LEVEL, AppiumLogLevel.ERROR.name().toLowerCase())
                .withArgument(USE_PLUGINS, "appium-dashboard");
        return AppiumDriverLocalService.buildService(serviceBuilder);
    }

    /**
     * Gets the Appium driver local service with the specified IP address, port, and log level.
     * This method builds the Appium service using the specified service builder,
     * which includes arguments for IP address, port, log level, and plugins.
     *
     * @param ipAddress the IP address for the Appium service
     * @param port the port for the Appium service
     * @param logLevel the log level for the Appium service
     * @return the Appium driver local service
     */
    public static AppiumDriverLocalService getService(String ipAddress, int port, AppiumLogLevel logLevel) {
        AppiumServiceBuilder serviceBuilder = new AppiumServiceBuilder()
                .withIPAddress(ipAddress)
                .usingPort(port)
                .withArgument(LOG_LEVEL, logLevel.name().toLowerCase())
                .withArgument(USE_PLUGINS, "appium-dashboard");
        return AppiumDriverLocalService.buildService(serviceBuilder);
    }
}
