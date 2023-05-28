package hu.bme.mit.alf.manuel.reporting;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import hu.bme.mit.alf.manuel.entityservice.stock.Stock;
import hu.bme.mit.alf.manuel.entityservice.stock.StockRepository;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.StockMovement;
import hu.bme.mit.alf.manuel.entityservice.stock.movement.StockMovementRepository;
import hu.bme.mit.alf.manuel.entityservice.users.User;
import hu.bme.mit.alf.manuel.entityservice.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;



@EnableScheduling
@Service
@Slf4j
public class SendGridEmailService {
    private static final String EMAIL_TEMPLATE = "emailTemplates.html";
    private static final String TEMPLATE_VARIABLE_STOCKS = "stocks";
    
    @Autowired
    private StockMovementRepository stockMovementRepository;
    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    @Autowired
    private TemplateEngine templateEngine;
    @Autowired
    private StockRepository stockRepository;
    @Autowired
    private UserRepository userRepository;


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
        ct.setVariable(TEMPLATE_VARIABLE_STOCKS,stocks);
        String emailContent = "This is your daily report.\n";
        emailContent += templateEngine.process(EMAIL_TEMPLATE, ct);
        this.sendEmail(to,subject,emailContent);
    }

    @Scheduled(timeUnit = TimeUnit.DAYS,fixedRate = 1)
    public void sendStockReportDaily(){ //összes user akinek a roleja menedzser
        List<User> to = userRepository.findUsersByRoles_Name("MANAGER");
        for (User who : to) {
            log.info("Daily Report sent to {}",who.getEmail());
            this.sendStockReport(who.getEmail(), "Daily report");
        }
        log.info("Daily Report sent to Managers");
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 10)
    public void sendMoveReport(){ //összes user akinek a roleja storage
        List<User> to = userRepository.findUsersByRoles_Name("STORAGE");
        List<Stock> stocks = stockRepository.getMultipleProducts();
        Context ct = new Context();
        ct.setVariable(TEMPLATE_VARIABLE_STOCKS,stocks);
        String emailContent = templateEngine.process(EMAIL_TEMPLATE, ct);
        for (User who : to) {
            log.info("Report of objects that need to move sent to {}",who.getEmail());
            this.sendEmail(who.getEmail(),"Report of objects that need to move",emailContent);
        }
        log.info("Report of objects that need to move sent to Storage");
    }

    @Scheduled(timeUnit = TimeUnit.MINUTES, fixedRate = 10)
    public void sendUnMovedReport() {
        List<User> to = userRepository.findUsersByRoles_Name("STORAGE");
        List<StockMovement> stockMovements = stockMovementRepository.findLatestStockMovementsForEachStock();
        List<Stock> stocks = new ArrayList<>();
        for (StockMovement sm : stockMovements) {
            Date stockMovementDate = sm.getTimestamp();
            LocalDateTime stockMovementTime = LocalDateTime.ofInstant(stockMovementDate.toInstant(), ZoneId.systemDefault());
            long minutesDifference = Duration.between(stockMovementTime, LocalDateTime.now()).toMinutes();
            if (minutesDifference > 12) {
                stocks.add(sm.getStock());
            }
        }
        Context ct = new Context();
        ct.setVariable(TEMPLATE_VARIABLE_STOCKS, stocks);
        String emailContent = templateEngine.process(EMAIL_TEMPLATE, ct);
        for (User who : to) {
            log.info("Report of static objects sent to {}", who.getEmail());
            this.sendEmail(who.getEmail(), "Report of static objects", emailContent);
        }
        log.info("Report of static objects sent to Storage");
    }




    public void sendStockReportByName(String to, String subject, String name){
        List<Stock> stocks = stockRepository.findAllByProduct_Name(name);
        Context ct = new Context();
        ct.setVariable(TEMPLATE_VARIABLE_STOCKS,stocks);
        String emailContent = "This is your report of " + name + ".\n";
        emailContent += templateEngine.process(EMAIL_TEMPLATE, ct);
        this.sendEmail(to,subject,emailContent);

    }
    public void sendStockReportByLocation(String to, String subject, String location){
        List<Stock> stocksById = stockRepository.findAllByLocation_Name(location);
        Context ct = new Context();
        ct.setVariable(TEMPLATE_VARIABLE_STOCKS,stocksById);
        String emailContent = "This is your report of " + location + ".\n";
        emailContent += templateEngine.process(EMAIL_TEMPLATE, ct);
        this.sendEmail(to,subject,emailContent);
    }
}

