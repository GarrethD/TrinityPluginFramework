package en.Core.models;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonInclude;
import io.cucumber.core.internal.com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import utilities.JsonObjectMapper;

import java.util.Objects;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Device {
    private String platformName;
    @JsonProperty("appium:app")
    private String app;
    @JsonProperty("appium:appActivity")
    private String appActivity;
    @JsonProperty("appium:appPackage")
    private String appPackage;
    @JsonProperty("appium:automationName")
    private String automationName;
    @JsonProperty("appium:avd")
    private String avd;
    @JsonProperty("appium:deviceName")
    private String deviceName;
    @JsonProperty("appium:fullReset")
    private boolean fullReset;
    @JsonProperty("appium:platformVersion")
    private String platformVersion;
    @JsonProperty("appium:skipUnlock")
    private boolean skipUnlock;
    @JsonProperty("appium:udid")
    private String udid;

    public String getApp() {
        return Objects.requireNonNull(ClassLoader.getSystemClassLoader().getResource("")).getPath() + app;
    }

    public String toJsonString() {
        return JsonObjectMapper.objectToJson(this);
    }

    public JsonObject toJsonObject() {
        return JsonParser.parseString(toJsonString()).getAsJsonObject();
    }
}
