
package Student;

import java.util.Properties;
import jakarta.mail.*;
import jakarta.mail.internet.*;

public class EmailService {
    
    public static void sendCombinedWarning(String toEmail, String warningText) {

    final String fromEmail = "healthhub519@gmail.com";
    final String password = "jcgm rxtn nnfe vcll";

    Properties props = new Properties();

    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");
    props.put("mail.smtp.host", "smtp.gmail.com");
    props.put("mail.smtp.port", "587");

    Session session = Session.getInstance(props,
            new Authenticator() {

        @Override
        protected PasswordAuthentication getPasswordAuthentication() {

            return new PasswordAuthentication(fromEmail, password);
        }
    });

    try {

        Message message = new MimeMessage(session);

        message.setFrom(new InternetAddress(fromEmail));

        message.setRecipients(
                Message.RecipientType.TO,
                InternetAddress.parse(toEmail)
        );

        message.setSubject("Low Attendance Warning");

        message.setText(
        "Dear Student,\n\n"
        + "Your attendance is below 75% in the following subjects:\n\n"
        + warningText
        + "\nPlease improve your attendance to avoid issues.\n\n"
        + "CMS Portal"
);

Transport.send(message);

        System.out.println("Combined Email Sent Successfully!");

    } catch (MessagingException e) {

        System.out.println("Email Error: " + e.getMessage());
    }
}

}   
    
     
