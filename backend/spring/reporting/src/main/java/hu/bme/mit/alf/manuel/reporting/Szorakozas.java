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
public class Szorakozas {
    private MqService mqs;
    private final SendGridEmailService emailService;


    void LogMessage(String s) {
        log.info("Az alábbi üzenet érkezett: {}", s);

        try (BufferedReader reader = new BufferedReader(new StringReader(s))) {
            String line;
            String dr = "Daily Report";
            String ReportByName = "Report By Name";
            String ReportByLoc = "Report By Loc";
            String firstline = reader.readLine();
            System.out.println(firstline);

            if (s.isEmpty()) {
                log.info("Az üzenet üres.");
                return;
            }

            if (dr.equals(firstline)) {
                log.info("Ez bizony egy Daily Report küldés");
                while ((line = reader.readLine()) != null) {
                    log.info("{} számára elküldve", line);
                    emailService.sendStockReport(line, "Report");
                }
            }
            else if (ReportByName.equals(firstline)) {
                log.info("Ez bizony egy Report By Name küldés");
                String secondLine = reader.readLine(); // Második sor kiolvasása
                while ((line = reader.readLine()) != null) {
                    log.info("{} számára elküldve", line);
                    emailService.sendStockReportByName(line, "Report By Name", secondLine);
                }
            }
            else if (ReportByLoc.equals(firstline)) {
                log.info("Ez bizony egy Report By Location küldés");
                String secondLine = reader.readLine(); // Második sor kiolvasása
                while ((line = reader.readLine()) != null) {
                    log.info("{} számára elküldve", line);
                    emailService.sendStockReportByLocation(line, "Report By Location", secondLine);
                }
            }
        } catch (IOException e) {
            log.error("Hiba történt az üzenet feldolgozása során: {}", e.getMessage());
        }
    }


    @Autowired
    Szorakozas(MqService _mqs, SendGridEmailService es){
        mqs = _mqs;
        this.emailService=es;
        mqs.setConsumer(this::LogMessage);
    }
}
