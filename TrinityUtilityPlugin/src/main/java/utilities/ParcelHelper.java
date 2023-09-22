package utilities;

import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@UtilityClass
public class ParcelHelper {
    public static String buildParcelTrackingCodeForToday(String lastThreeNumbers) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendLiteral("3SPWEB")
                .appendValueReduced(ChronoField.YEAR, 1, 1, 0)
                .appendPattern("MMdd")
                .appendLiteral(lastThreeNumbers)
                .toFormatter();
        return LocalDateTime.now().format(formatter);
    }
}
