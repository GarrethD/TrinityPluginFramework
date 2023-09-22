package utilities;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@UtilityClass
public class FileHelper {
    /**
     * Retrieves a file from the given resource using the provided class loader and file name.
     *
     * @param classLoader The class loader used to load the resource.
     * @param fileName The name of the file resource.
     * @return The file retrieved from the resource.
     * @throws IllegalArgumentException If the file resource is not found.
     */
    public static File getFileFromResource(ClassLoader classLoader, String fileName) {
        File file = null;
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            try {
                file = new File(resource.toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * Retrieves the contents of a file from the given resource using the class loader of the system.
     *
     * @param fileName The name of the file resource.
     * @return The contents of the file as a {@code String}.
     * @throws IllegalArgumentException If the file resource is not found.
     * @throws RuntimeException If an error occurs while reading the file.
     */

    public String getStringFromResourceFile(String fileName) {
        URL resource = ClassLoader.getSystemClassLoader().getResource(fileName);
        if (Objects.isNull(resource)) {
            throw new IllegalArgumentException("File (" + fileName + ") not found!");
        } else {
            try {
                return Files.readString(Path.of(resource.toURI()));
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
