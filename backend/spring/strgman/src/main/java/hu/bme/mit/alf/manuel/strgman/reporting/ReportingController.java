package hu.bme.mit.alf.manuel.strgman.reporting;


import hu.bme.mit.alf.manuel.mqclient.MqService;
import hu.bme.mit.alf.manuel.strgman.ValidatorBaseController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;


@RestController
@Slf4j
@RequestMapping("/reporting")
@RequiredArgsConstructor
public class ReportingController extends ValidatorBaseController {
    private final MqService mqService;

    @PostMapping("/report")
    public ResponseEntity<String> receiveStock(@Valid @RequestBody ReportingDto reportingDto) {
        ArrayList<String> types = new ArrayList<>(Arrays.asList("Daily Report", "Report By Name", "Report By Location"));

        if (!types.contains(reportingDto.getType())) {
            String msg = String.format("There is no reporting type called %s", reportingDto.getType());
            log.error(msg);
            return ResponseEntity.badRequest().body(msg);
        }

        StringBuilder messageBuilder = new StringBuilder(reportingDto.getType());
        if (reportingDto.getParam() != null && !reportingDto.getParam().isBlank()) {
            messageBuilder.append("\n").append(reportingDto.getParam());
        }
        for (String email :
                reportingDto.getReceivers()) {
            messageBuilder.append("\n").append(email);
        }

        try {
            mqService.send(messageBuilder.toString());
            return ResponseEntity.ok("Stock report successfully sent");
        } catch (Exception e) {
            log.error("Failed sending stock report", e);
            return ResponseEntity.internalServerError().build();
        }
    }

}
