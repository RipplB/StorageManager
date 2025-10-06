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
public class EmailController {
    private MqService mqs;
    private final SendGridEmailService emailService;


    void LogMessage(String s) {
        String correlationId = getCurrentCorrelationId();
        log.info("Incoming message: {} [CorrelationID: {}]", s, correlationId);

        try (BufferedReader reader = new BufferedReader(new StringReader(s))) {
            String line;
            String dr = "Daily Report";
            String reportByName = "Report By Name";
            String reportByLoc = "Report By Location";
            String firstline = reader.readLine();

            if (s.isEmpty()) {
                log.info("Message is empty. [CorrelationID: {}]", correlationId);
                return;
            }

            if (dr.equals(firstline)) {
                log.info("It is a Daily Report request [CorrelationID: {}]", correlationId);
                while ((line = reader.readLine()) != null) {
                    log.info("Sent to {} [CorrelationID: {}]", line, correlationId);
                    emailService.sendStockReport(line, "Report");
                }
            }
            else if (reportByName.equals(firstline)) {
                log.info("It is a Report By Name request [CorrelationID: {}]", correlationId);
                String secondLine = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    log.info("Sent to {} [CorrelationID: {}]", line, correlationId);
                    emailService.sendStockReportByName(line, "Report By Name", secondLine);
                }
            }
            else if (reportByLoc.equals(firstline)) {
                log.info("It is a Report By Location request [CorrelationID: {}]", correlationId);
                String secondLine = reader.readLine();
                while ((line = reader.readLine()) != null) {
                    log.info("Sent to {} [CorrelationID: {}]", line, correlationId);
                    emailService.sendStockReportByLocation(line, "Report By Location", secondLine);
                }
            }
        } catch (IOException e) {
            log.error("Error while processing the message: {} [CorrelationID: {}]", e.getMessage(), correlationId);
        }
    }
    
    private String getCurrentCorrelationId() {
        return org.slf4j.MDC.get("correlationId");
    }

    @Autowired
    EmailController(MqService _mqs, SendGridEmailService es){
        mqs = _mqs;
        this.emailService=es;
        mqs.setConsumer(this::LogMessage);
    }
}
