package hu.bme.mit.alf.manuel.strgman.reporting;

import jakarta.validation.constraints.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@AllArgsConstructor
@Setter
@Getter
public class ReportingDto {
    @Size(max = 50)
    private String type;

    @Size(max = 50)
    private String param;

    private ArrayList<String> receivers;
}
