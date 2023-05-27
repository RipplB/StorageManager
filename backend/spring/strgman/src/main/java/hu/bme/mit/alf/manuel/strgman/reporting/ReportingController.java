package hu.bme.mit.alf.manuel.strgman.reporting;


import hu.bme.mit.alf.manuel.reporting.SendGridEmailService;
import hu.bme.mit.alf.manuel.strgman.ValidatorBaseController;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;


@RestController
@Slf4j
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingController extends ValidatorBaseController {
    @Autowired
    private SendGridEmailService sendGridEmailService;

    @PostMapping("/report")
    public ResponseEntity<String> receiveStock(@Valid @RequestBody ReportingDto reportingDto) {
        ArrayList<String> types = new ArrayList<>(Arrays.asList("Daily", "Name", "Location"));

        if (!types.contains(reportingDto.getType())) {
            String msg = String.format("There is no reporting type called {}", reportingDto.getType());
            log.error(msg);
            return ResponseEntity.badRequest().body(msg);
        }

        if (reportingDto.getType().equals(types.get(0))) {
            for (int i =0;i<reportingDto.getReceivers().size();i++) {
                String msg = "Daily report to "+reportingDto.getReceivers().get(i)+" sent successfully";
                sendGridEmailService.sendStockReport(reportingDto.getReceivers().get(i), msg);
                log.info(msg);
            }
        }
        else if (reportingDto.getType().equals(types.get(1))) {
            for (int i =0;i<reportingDto.getReceivers().size();i++){
                String msg = "Stock report  on "+reportingDto.getParam()+" to "+reportingDto.getReceivers().get(i)+" sent successfully";
                sendGridEmailService.sendStockReportByName(reportingDto.getReceivers().get(i),reportingDto.getType().toString(),msg);
                log.info(msg);
            }
        }
        else if (reportingDto.getType().equals(types.get(2))) {
            for (int i =0;i<reportingDto.getReceivers().size();i++) {
                String msg = "Stock report on "+reportingDto.getParam()+" to "+reportingDto.getReceivers().get(i)+" sent successfully";
                sendGridEmailService.sendStockReportByLocation(reportingDto.getReceivers().get(i),reportingDto.getType().toString(),msg);
                log.info(msg);
            }
        }
        try {
            return ResponseEntity.ok("Stock report successfully sent");
        } catch (Exception e) {
            log.error("Failed sending stock report", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
