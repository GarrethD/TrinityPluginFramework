package utilities.gmail;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.search.SearchTerm;
import java.util.List;

/**
 * An interface for searching email messages in a specified mailbox folder.
 */
public interface MailReader {

    /**
     * Searches and returns messages matching the search term connecting to the mailbox folder in the read-write mode.
     * @param folderName folder where messages will be searched.
     * @param searchTerm terms messages should comply with.
     * @return list of found messages.
     */
    default List<Message> getEmails(String folderName, SearchTerm searchTerm) {
        return getEmails(folderName, searchTerm, Folder.READ_WRITE);
    }

    /**
     * Connects to the email folder, searches and returns messages matching the search term.
     * @param folderName folder where messages will be searched.
     * @param searchTerm terms messages should comply with.
     * @param mode connection mode to the specified folder (e.g., <code>Folder.READ_WRITE</> or <code>Folder.READ_ONLY</>.
     * @return list of found messages.
     */
    List<Message> getEmails(String folderName, SearchTerm searchTerm, int mode);
}
