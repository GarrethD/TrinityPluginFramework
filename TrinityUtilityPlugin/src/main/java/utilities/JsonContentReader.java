package utilities;

import io.cucumber.core.internal.com.fasterxml.jackson.databind.JsonNode;
import io.cucumber.core.internal.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;

public class JsonContentReader {
    public Boolean DoesJsonContainMessage(String pathToJsonFile,String messageSentenceToCheck) {
        ObjectMapper mapper = new ObjectMapper();
        boolean found = false;
        try {
            JsonNode rootNode = mapper.readTree(new File(pathToJsonFile));
             found = rootNode.findValues("Message").stream()
                    .anyMatch(messageNode -> messageNode.asText().contains(messageSentenceToCheck));
            System.out.println(found ? "Sentence found" : "Sentence not found");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return found;
    }
}
