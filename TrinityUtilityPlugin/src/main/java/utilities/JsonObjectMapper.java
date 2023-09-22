package utilities;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cucumber.core.internal.com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.path.json.JsonPath;
import lombok.experimental.UtilityClass;

import java.nio.file.Files;
import java.nio.file.Paths;

@UtilityClass
public class JsonObjectMapper {
    static JsonPath jsonResponse;

    /**
     * Reads a JSON file from the given path and returns the parsed JSON as a JsonPath object.
     *
     * @param PathToFile the path to the JSON file
     * @return the parsed JSON as a JsonPath object
     */
    public static JsonPath ReadJsonFile(String PathToFile) {
        try {
            String response = readFileAsString(PathToFile);
            jsonResponse = new JsonPath(response);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonResponse;
    }

    /**
     * Reads the contents of a file and returns it as a string.
     *
     * @param file the path of the file to read
     * @return the contents of the file as a string
     * @throws Exception if an error occurs while reading the file
     */
    public static String readFileAsString(String file) throws Exception {
        return new String(Files.readAllBytes(Paths.get(file)));
    }

    /**
     * Reads the contents of a JSON file and returns it as a string.
     *
     * @param jsonFileName the name of the JSON file to read
     * @return the contents of the JSON file as a string
     */
    public String readJsonFile(String jsonFileName) {
        return FileHelper.getStringFromResourceFile(jsonFileName);
    }

    /**
     * Retrieves a specific JsonObject from a JSON file based on the given memberName.
     *
     * @param jsonFileName the name of the JSON file to read
     * @param memberName   the name of the member within the JSON object to retrieve
     * @return the JsonObject corresponding to the specified memberName
     */
    public JsonObject getJsonObjectForMember(String jsonFileName, String memberName) {
        return JsonParser.parseString(readJsonFile(jsonFileName)).getAsJsonObject().get(memberName).getAsJsonObject();
    }

    /**
     * Converts a JSON string from a specific file and a specific member name to an object of the given class.
     *
     * @param jsonFileName the file name of the JSON file
     * @param memberName the name of the member in the JSON object to convert
     * @param tClass the class of the object to convert the JSON to
     * @param <T> the type of the object to convert the JSON to
     * @return the converted object
     * @throws RuntimeException if an error occurs while processing the JSON or casting the object
     */
    public <T> T jsonToObject(String jsonFileName, String memberName, Class<T> tClass) {
        Object o;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            o = objectMapper.readValue(getJsonObjectForMember(jsonFileName, memberName).toString(), tClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return tClass.cast(o);
    }

    /**
     * Converts an object to its JSON representation.
     *
     * @param o The object to be converted.
     * @return The JSON representation of the object.
     * @throws RuntimeException If there is an error while converting the object to JSON.
     */
    public String objectToJson(Object o) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(o);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
