package utilities.gmail;

import lombok.Getter;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * A pool of mailbox connections. Allow reusing connections (in order not to connect to the same mailbox multiple times).
 */
public class GmailConnectionPool {
    private final static String DEFAULT_HOST = "imap.gmail.com";
    private final static String DEFAULT_PROTOCOL = "imaps";
    private final static String DEFAULT_PORT = "993";

    @Getter
    private static final GmailConnectionPool instance = new GmailConnectionPool();
    private static final Map<String, Store> connections = Collections.synchronizedMap(new HashMap<>());

    private String host;
    private String port;
    private String protocol;

    private GmailConnectionPool() {
        host = DEFAULT_HOST;
        port = DEFAULT_PORT;
        protocol = DEFAULT_PROTOCOL;
    }

    /**
     * Set custom connection configuration.
     * @param host host to connect to.
     * @param port port to connect to.
     * @param protocol protocol to use for connection.
     */
    public void configure(String host, int port, String protocol) {
        this.host = host;
        this.port = String.valueOf(port);
        this.protocol = protocol;
    }

    /**
     * Connect to the specified mailbox.
     * @param email mailbox to connect to.
     * @param password password to authorize with the specified mailbox
     *                 (PLease note, in Gmail in order to connect to the mailbox, 2FA needs to be enabled.
     *                 Additionally, you need to generate an application password (in order to be able to connect with this password from the Java program without a 2FA check)
     *                 You need to pass the generated application password (not the account password)).
     * @return connection store.
     */
    public synchronized Store getConnection(String email, String password) {
        if (connections.containsKey(email) && connections.get(email).isConnected()) {
            return connections.get(email);
        }
        Properties props = new Properties();
        props.put("mail.imap.port", port);
        props.put("mail.store.protocol", protocol);
        props.put("mail.pop3.starttls.enable", "true");
        Session session = Session.getInstance(props, null);
        try {
            Store store = session.getStore(protocol);
            store.connect(host, email, password);
            connections.put(email, store);
            return store;
        } catch (MessagingException e) {
            throw new IllegalStateException("Unable to connect to mailbox %s: %s".formatted(email, e.getMessage()), e);
        }
    }
}
