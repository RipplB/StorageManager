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


@Service
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

            System.out.println(response.getStatusCode());
            System.out.println(response.getBody());
            System.out.println(response.getHeaders());
        } catch (Exception ex) {
            // Handle exception
        }
    }

    public void sendStockReport(String to, String subject) {
        List<Integer> productIds = stockRepository.findAllProductIds();
        List<Stock> stocks = new ArrayList<>();
        Context ct = new Context();

        for (Integer id : productIds) {
            List<Stock> stocksById = stockRepository.findByid(id);
            for (Stock stock : stocksById) {
                stocks.add(stock);
            }
        }
        ct.setVariable("stocks",stocks);

        // Generálja az e-mail tartalmát a Thymeleaf sablonból
        String emailContent = "This is your daily report.\n";
        emailContent += templateEngine.process("emailTemplates.html", ct);
        this.sendEmail(to,subject,emailContent);
    }

    public void sendStockReportByName(String to, String subject, String name){
        List<Integer> productIds = stockRepository.findAllProductIdsByName(name);
        List<Stock> stocks = new ArrayList<>();

        Context ct = new Context();

        for (Integer id : productIds) {
            System.out.println(id);
            List<Stock> stocksById = stockRepository.findByid(id);
            for (Stock stock : stocksById) {
                stocks.add(stock);
                System.out.println(stock.getProduct().getName());
            }
        }
        ct.setVariable("stocks",stocks);

        // Generálja az e-mail tartalmát a Thymeleaf sablonból
        String emailContent = "This is your report of " + name + ".\n";
        emailContent += templateEngine.process("emailTemplates.html", ct);
        this.sendEmail(to,subject,emailContent);

    }
    public void sendStockReportByLocation(String to, String subject, String location){
        List<Integer> productIds = stockRepository.findAllProductIdsByLocation(location);
        List<Stock> stocks = new ArrayList<>();

        Context ct = new Context();

        for (Integer id : productIds) {
            System.out.println("Ez az id-je:"+id);
            List<Stock> stocksById = stockRepository.findByid(id);
            for (Stock stock : stocksById) {
                System.out.println("ide belep");
                stocks.add(stock);
                System.out.println(stock.getProduct().getName());
            }
        }
        ct.setVariable("stocks",stocks);

        // Generálja az e-mail tartalmát a Thymeleaf sablonból
        String emailContent = "This is your report of " + location + ".\n";
        emailContent += templateEngine.process("emailTemplates.html", ct);
        this.sendEmail(to,subject,emailContent);

    }
}

