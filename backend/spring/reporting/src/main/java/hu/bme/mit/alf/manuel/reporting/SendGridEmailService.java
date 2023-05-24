package hu.bme.mit.alf.manuel.reporting;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SendGridEmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendEmail(String to, String subject, String content) {
        Email from = new Email("arionpap4444@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);

        Mail mail = new Mail(from, subject, toEmail, emailContent);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception ex) {
            // Handle exception
        }
    }
}

