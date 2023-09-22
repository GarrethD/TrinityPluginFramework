package utilities;

import org.jsoup.Jsoup;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.search.OrTerm;
import javax.mail.search.SearchTerm;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailReaderAndExtractor {
    Properties props = new Properties();
    Store store = null;
    Message[] foundMessages;
    Folder folderInbox = null;
    FlagTerm unseenFlagTerm;
    SearchTerm searchTerm;
    String retrievedValueFromEmail;
    Message message;
    Address[] froms;
    Address[] toAddress;
    String emails;
    String ToEmail;
    Date date;
    long diff;
    long diffMinutes;
    String result = "";
    String resultHtml = "";
    String content;
    Pattern pattern;
    Matcher matcher;
    public String text;
    public String html;
    public String messageGetContent = "";

    public EmailReaderAndExtractor(String text, String html, String messageGetContent) {
        this.text = text;
        this.html = html;
        this.messageGetContent = messageGetContent;
    }
    //step 1

    /**
     * Set Mail Filters
     *
     * @param imapPortNumber This is the imap port needed (Yahoo, google, outlook have different port numbers)
     * @param mailProtocol   This is the protocol (imap,pop3,smtp)
     * @param EmailAddress   The email address of the mail account holder
     * @param Password       The is the generate password inside the email app password in gmail for example.
     */

    public void EmailConfigSetup(String imapPortNumber, String mailProtocol, String EmailAddress, String Password) {
        // Logic for connecting to Gmail server, Read mails and extract activation code
        SetMailImapSetting(imapPortNumber, mailProtocol);
        SetMailCredentials("imap.gmail.com", EmailAddress, Password);
    }
//step 2

    /**
     * Set Mail Filters
     *
     * @param inboxName Filter by folder name e.i (Inbox, starred,snoozed,sent,draft)
     */
    public void EmailInboxSetup(String inboxName) {
        SetMailInboxToReadFrom(inboxName);
    }

    //step 3

    /**
     * Set Mail Filters
     *
     * @param emailReceiverAddress Enter the To email address
     * @param emailSenderAddress   Enter the From email address
     * @param emailSubject         Enter email subject Header
     */
    public EmailReaderAndExtractor SetMailFiltersAndFindMatchedMail(String emailSubject, String emailSenderAddress, String emailReceiverAddress) {
        //create a search term for all recent messages
        Flags recent = new Flags(Flags.Flag.RECENT);
        FlagTerm recentFlagTerm = new FlagTerm(recent, false);
        searchTerm = new OrTerm(unseenFlagTerm, recentFlagTerm);
        // performs search through the folder
        try {
            foundMessages = folderInbox.search(searchTerm);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        retrievedValueFromEmail = null;
        System.out.println("Total Messages Found :" + foundMessages.length);
        for (int i = foundMessages.length - 1; i >= foundMessages.length - 10; i--) {
            message = foundMessages[i];
            try {
                froms = message.getFrom();
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            try {
                toAddress =
                        message.getRecipients(Message.RecipientType.TO);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            emails = froms == null ? null : ((InternetAddress) froms[0]).getAddress();
            try {
                if (message.getSubject() == null) {
                    continue;
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            ToEmail = toAddress == null ? null : ((InternetAddress) toAddress[0]).getAddress();
            try {
                if (message.getSubject() == null) {
                    continue;
                }
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            diffMinutes = GetLatestEmailBasedOnTime(message);
            try {
                System.out.println("Mail subject + emails + diffMinutes :" + message.getSubject() + emails + diffMinutes + ToEmail);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
            try {
                if (message.getSubject().contains(emailSubject) && emails.equals(emailSenderAddress) && diffMinutes <= 1 && ToEmail.contains(emailReceiverAddress)) {
                    System.out.println("Mail Content after filtration :" + message.getContent().toString());
                    String contentType = message.getContentType();
                    if (contentType.contains("multipart")) {
                        MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();
                        int count = mimeMultipart.getCount();
                        for (int ii = 0; ii < count; ii++) {
                            BodyPart bodyPart = mimeMultipart.getBodyPart(ii);
                            if (bodyPart.isMimeType("text/plain")) {
                                result = result + "\n" + bodyPart.getContent();
                                messageGetContent = bodyPart.getContent().toString();
                                System.out.println("Body part content 'text/plain' :" + result);
                            } else if (bodyPart.isMimeType("text/html")) {
                                content = (String) bodyPart.getContent();
                                resultHtml = resultHtml + "\n" + Jsoup.parse(content).body();
                                messageGetContent =  messageGetContent + "\n" + Jsoup.parse(content).body();
                                System.out.println("Body part content 'text/html' - result_htmlBody :" + resultHtml);
                            }
                        }
                    }
                    else {
                        messageGetContent = message.getContent().toString();
                    }
                }
            } catch (MessagingException | IOException e) {
                e.printStackTrace();
            }
        }
        return new EmailReaderAndExtractor(result, resultHtml, messageGetContent);
    }

    /**
     * Extract Values From Emails
     *
     * @param regexExpression This will allow a user to get specific values from the content of the email
     * @param resultIndex     Should there be more than 1 of the same value, The user can decide which one to retrieve
     */
    //setp 4
    public String ExtractValuesFromEmails(String Content, String regexExpression, int resultIndex) {
        //"<span.*?>(\\d+)</span>"
        System.out.println("Sent regex" + regexExpression);
        pattern = Pattern.compile(regexExpression);
        matcher = pattern.matcher(Content);
        if (matcher.find()) {
            retrievedValueFromEmail = matcher.group(resultIndex);
        }
        System.out.println("Value retrieved from email :" + retrievedValueFromEmail);
        return retrievedValueFromEmail;
    }

    private void SetMailImapSetting(String imapPortNumber, String mailProtocol) {
        props.setProperty("mail.imap.port", imapPortNumber);
        props.setProperty("mail.store.protocol", mailProtocol);
        Session session = Session.getInstance(props, null);
        try {
            store = session.getStore(mailProtocol);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
    }

    private void SetMailCredentials(String mailHost, String gmailEmailAddress, String gmailPassword) {
        try {
            store.connect(mailHost, gmailEmailAddress, gmailPassword);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private void SetMailInboxToReadFrom(String emailInboxName) {
        try {
            folderInbox = store.getFolder(emailInboxName.toUpperCase());
            folderInbox.open(Folder.READ_ONLY);
            //create a search term for all "unseen" messages
            Flags seen = new Flags(Flags.Flag.SEEN);
            unseenFlagTerm = new FlagTerm(seen, true);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private long GetLatestEmailBasedOnTime(Message message) {
        try {
            date = new Date();//Getting Present date from the system
            diff = 0;//Get The difference between two dates
            diff = date.getTime() - message.getReceivedDate().getTime();

            diffMinutes = diff / (60 * 1000) % 60; //Fetching the difference of minute
            System.out.println("Difference in Minutes b/w present time & Email Recieved time :" + diffMinutes);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return diffMinutes;
    }
}
