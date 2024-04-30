package utilities.gmail;

import javax.mail.Message;
import java.util.Date;
import java.util.List;

/**
 * Mail message representation with parsed body parts.
 * @param from sender addresses.
 * @param to recipient addresses.
 * @param subject message subject.
 * @param textContent parsed text part.
 * @param html parsed html part.
 * @param receivedDate message received date.
 * @param message the original message.
 */
public record ParsedMessage(
        List<String> from,
        List<String> to,
        String subject,
        String textContent,
        String html,
        Date receivedDate,
        Message message
) {
}
