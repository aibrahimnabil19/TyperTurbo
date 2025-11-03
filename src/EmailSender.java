import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class EmailSender {
    private final Session session;
    private final String fromAddress;

    /**
     * @param host       SMTP host (e.g. smtp.gmail.com)
     * @param port       SMTP port (587 for TLS)
     * @param username   your SMTP login (full email)
     * @param password   your SMTP password or app-specific password
     * @param fromAddress  the “From:” address users will see
     */
    public EmailSender(String host,
                       int port,
                       final String username,
                       final String password,
                       String fromAddress) {
        this.fromAddress = fromAddress;
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", String.valueOf(port));

        session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }

    /** Send a plain‐text email */
    public void send(String to, String subject, String body)
            throws MessagingException {
        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress(fromAddress));
        msg.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(to, false)
        );
        msg.setSubject(subject);
        msg.setText(body);
        Transport.send(msg);
    }
}
