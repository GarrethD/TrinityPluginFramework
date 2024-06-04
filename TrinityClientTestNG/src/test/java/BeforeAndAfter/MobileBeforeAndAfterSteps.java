package BeforeAndAfter;

import base.MobileDriver;
import base.MobilePlatformName;
import com.google.gson.JsonObject;
import org.example.configuration.Environment;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;


import java.util.Objects;

public class MobileBeforeAndAfterSteps {
MobileDriver mobileDriver;
    public MobileBeforeAndAfterSteps() {
    }

    @BeforeClass
    public void init() {

        Environment.users("User1");
        JsonObject deviceOptions = Environment.getDeviceOptions(Environment.getDevice("device"));
        MobilePlatformName mobilePlatformName = MobilePlatformName.getMobilePlatformName(deviceOptions);
        mobileDriver  = new MobileDriver(mobilePlatformName, deviceOptions);
    }
    public MobileDriver getMobileDriver() {
        return mobileDriver;
    }

    @AfterClass
    public void tearDownApp() {
        mobileDriver.closeDriver();
//        extent.flush();
    }
}
