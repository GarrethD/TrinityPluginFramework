package utilities;

import java.util.Random;

public class NumberGenerator {
    private static String randomizeNumbers(String template) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        for (char c : template.toCharArray()) {
            if (c == 'x') {
                result.append(random.nextInt(10)); // Generate a number between 0 and 9
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

}
