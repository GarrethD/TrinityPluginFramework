package utilities.gmail;

import lombok.extern.slf4j.Slf4j;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.search.SearchTerm;
import java.util.List;

/**
 * A decorator for the Gmail Reader. Sorts messages by the received date (ASC or DESC).
 */
@Slf4j
public class GmailSorter implements MailReader {
    private final MailReader mailReader;
    private final Direction direction;


    public GmailSorter(MailReader mailReader, Direction direction) {
        this.mailReader = mailReader;
        this.direction = direction;
    }

    /**
     * Searches and returns messages according to the search term sorted by the received date.
     * @param folderName folder where messages will be searched.
     * @param searchTerm terms messages should comply with.
     * @param mode connection mode to the specified folder (e.g., <code>Folder.READ_WRITE</> or <code>Folder.READ_ONLY</>.
     * @return list of sorted messages.
     */
    @Override
    public List<Message> getEmails(String folderName, SearchTerm searchTerm, int mode) {
        log.info("Sorting emails by received date in {} direction", direction);
        return mailReader.getEmails(folderName, searchTerm)
                .stream().sorted((m1, m2) -> {
            try {
                return direction.equals(Direction.DESC)
                        ? m2.getReceivedDate().compareTo(m1.getReceivedDate())
                        : m1.getReceivedDate().compareTo(m2.getReceivedDate());
            } catch (MessagingException e) {
                throw new IllegalStateException("Unable to sort messages by received date: %s".formatted(e.getMessage()), e);
            }
        }).toList();
    }

    public enum Direction {
        ASC, DESC
    }
}
