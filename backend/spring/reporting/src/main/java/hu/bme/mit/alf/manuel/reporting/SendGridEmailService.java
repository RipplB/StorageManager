package hu.bme.mit.alf.manuel.reporting;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import hu.bme.mit.alf.manuel.entityservice.stock.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.util.*;
import lombok.extern.slf4j.Slf4j;



@Service
@Slf4j
public class SendGridEmailService {
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private StockRepository stockRepository;


    public SendGridEmailService() {
        // Create and configure the template engine
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);

        templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
    }

    public void sendEmail(String to, String subject, String content) {
        Email from = new Email("arionpap4444@gmail.com");
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/html", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);
        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sg.api(request);
            log.debug(String.valueOf(response.getStatusCode()));
            log.debug(response.getBody());
            log.debug(response.getHeaders().toString());

        } catch (Exception ex) {
            // Handle exception
        }
    }

    public void sendStockReport(String to, String subject) {
        List<Stock> stocks = stockRepository.findAll();
        Context ct = new Context();
        ct.setVariable("stocks",stocks);
        String emailContent = "This is your daily report.\n";
        emailContent += templateEngine.process("emailTemplates.html", ct);
        this.sendEmail(to,subject,emailContent);
    }

    public void sendStockReportByName(String to, String subject, String name){
        List<Stock> stocks = stockRepository.findAllByProduct_Name(name);
        Context ct = new Context();
        ct.setVariable("stocks",stocks);
        String emailContent = "This is your report of " + name + ".\n";
        emailContent += templateEngine.process("emailTemplates.html", ct);
        this.sendEmail(to,subject,emailContent);

    }
    public void sendStockReportByLocation(String to, String subject, String location){
        List<Stock> stocksById = stockRepository.findAllByLocation_Name(location);
        Context ct = new Context();
        ct.setVariable("stocks",stocksById);
        String emailContent = "This is your report of " + location + ".\n";
        emailContent += templateEngine.process("emailTemplates.html", ct);
        this.sendEmail(to,subject,emailContent);
    }
}

