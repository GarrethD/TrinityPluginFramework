package utilities.gmail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;
import java.util.Arrays;
import java.util.List;

/**
 * Connects to an email box and searches for messages.
 */
@Slf4j
public class GmailReader implements MailReader {
    private static final GmailConnectionPool connectionPool = GmailConnectionPool.getInstance();
    private final String email;
    private final String password;

    public GmailReader(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public GmailReader(String email, String password, String host, int port, String protocol) {
        this.email = email;
        this.password = password;
        connectionPool.configure(host, port, protocol);
    }

    public List<Message> getEmails(String folderName, SearchTerm searchTerm, int mode) {
        try {
            log.info("Searching messages in folder {}", folderName);
            Folder folder = connectionPool.getConnection(email, password).getFolder(folderName.toUpperCase());
            if (!folder.isOpen()) {
                folder.open(mode);
            }
            List<Message> messages = Arrays.stream(folder.search(searchTerm)).toList();
            log.info("Found {} messages corresponding to search term", messages.size());
            return messages;
        } catch (MessagingException e) {
            throw new IllegalStateException("Unable to read from mailbox folder %s: %s".formatted(folderName, e.getMessage()), e);
        }
    }
}
