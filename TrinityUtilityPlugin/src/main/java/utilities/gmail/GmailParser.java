package utilities.gmail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * A decorator for the Gmail Reader, returns messages with parsed body parts.
 */
@Slf4j
public class GmailParser implements MailReader {
    private final MailReader mailReader;

    public GmailParser(MailReader mailReader) {
        this.mailReader = mailReader;
    }

    /**
     * Parses email messages to a usable format.
     * @param folderName folder to search in.
     * @param searchTerm terms messages should match.
     * @return matching messages in a usable format.
     */
    public List<ParsedMessage> getParsedEmails(String folderName, SearchTerm searchTerm) {
        List<Message> messages = getEmails(folderName, searchTerm);
        log.info("Parsing received messages");
        return messages.stream().map(message -> {
            try {
                StringBuilder textContentBuilder = new StringBuilder();
                StringBuilder htmlBuilder = new StringBuilder();
                String contentType = message.getContentType();
                if (contentType.contains("multipart")) {
                    MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                    for (int i = 0; i < mimeMultipart.getCount(); i++) {
                        BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                        if (bodyPart.isMimeType("text/plain")) {
                            textContentBuilder.append(bodyPart.getContent()).append("\n");
                        } else if (bodyPart.isMimeType("text/html")) {
                            htmlBuilder.append(bodyPart.getContent()).append("\n");
                        }
                    }
                } else {
                    textContentBuilder.append(message.getContent().toString());
                }
                return new ParsedMessage(
                        Arrays.stream(message.getFrom()).map(Address::toString).toList(),
                        Arrays.stream(message.getRecipients(Message.RecipientType.TO)).map(Address::toString).toList(),
                        message.getSubject(),
                        textContentBuilder.toString(),
                        htmlBuilder.toString(),
                        message.getReceivedDate(),
                        message
                );
            } catch (MessagingException | IOException e) {
                log.error("Unable to parse message: %s".formatted(e.getMessage()), e);
            }
            return null;
        }).toList();
    }

    @Override
    public List<Message> getEmails(String folderName, SearchTerm searchTerm, int mode) {
        return mailReader.getEmails(folderName, searchTerm, mode);
    }
}
