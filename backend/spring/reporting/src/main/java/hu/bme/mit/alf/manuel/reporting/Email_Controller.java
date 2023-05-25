package hu.bme.mit.alf.manuel.reporting;

import hu.bme.mit.alf.manuel.mqclient.MqService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.StringReader;
import java.io.IOException;


@Component
@Slf4j
public class Email_Controller {
    private MqService mqs;
    private final SendGridEmailService emailService;


    void LogMessage(String s) {
        log.info("Incoming message: {}", s);

        try (BufferedReader reader = new BufferedReader(new StringReader(s))) {
            String line;
            String dr = "Daily Report";
            String ReportByName = "Report By Name";
            String ReportByLoc = "Report By Loc";
            String firstline = reader.readLine();
            System.out.println(firstline);

            if (s.isEmpty()) {
                log.info("Message is empty.");
                return;
            }

            if (dr.equals(firstline)) {
                log.info("It is a Daily Report request");
                while ((line = reader.readLine()) != null) {
                    log.info("Sent to {}", line);
                    emailService.sendStockReport(line, "Report");
                }
            }
            else if (ReportByName.equals(firstline)) {
                log.info("It is a Report By Name request");
                String secondLine = reader.readLine(); // Második sor kiolvasása
                while ((line = reader.readLine()) != null) {
                    log.info("Sent to {}", line);
                    emailService.sendStockReportByName(line, "Report By Name", secondLine);
                }
            }
            else if (ReportByLoc.equals(firstline)) {
                log.info("It is a Report By Location request");
                String secondLine = reader.readLine(); // Második sor kiolvasása
                while ((line = reader.readLine()) != null) {
                    log.info("Sent to {}", line);
                    emailService.sendStockReportByLocation(line, "Report By Location", secondLine);
                }
            }
        } catch (IOException e) {
            log.error("Error while processing the message: {}", e.getMessage());
        }
    }


    @Autowired
    Email_Controller(MqService _mqs, SendGridEmailService es){
        mqs = _mqs;
        this.emailService=es;
        mqs.setConsumer(this::LogMessage);
    }
}
