package utilities.gmail;

import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.search.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A Builder for the search terms according to which the email messages need to be filtered.
 */
public class SearchTermBuilder {
    private final List<SearchTerm> terms = new ArrayList<>();

    public SearchTermBuilder subject(String subject) {
        terms.add(new SubjectTerm(subject));
        return this;
    }

    public SearchTermBuilder from(String sender) {
        terms.add(new FromStringTerm(sender));
        return this;
    }

    public SearchTermBuilder to(String recipient) {
        terms.add(new RecipientStringTerm(Message.RecipientType.TO, recipient));
        return this;
    }

    public SearchTermBuilder read() {
        terms.add(new FlagTerm(new Flags(Flags.Flag.SEEN), true));
        return this;
    }

    public SearchTermBuilder unread() {
        terms.add(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        return this;
    }

    public SearchTermBuilder recent() {
        terms.add(new FlagTerm(new Flags(Flags.Flag.RECENT), true));
        return this;
    }

    public SearchTermBuilder receivedAfter(Date date) {
        terms.add(new ReceivedDateTerm(DateTerm.GT, date));
        return this;
    }

    public SearchTermBuilder receivedBefore(Date date) {
        terms.add(new ReceivedDateTerm(DateTerm.LT, date));
        return this;
    }

    public SearchTermBuilder receivedToday() {
        terms.add(new ReceivedDateTerm(DateTerm.GT,
                Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusHours(1).toInstant())));
        return this;
    }

    public SearchTermBuilder bodyContains(String text) {
        terms.add(new BodyTerm(text));
        return this;
    }

    public SearchTerm allMatch() {
        return new AndTerm(terms.toArray(SearchTerm[]::new));
    }

    public SearchTerm anyMatch() {
        return new OrTerm(terms.toArray(SearchTerm[]::new));
    }
}
