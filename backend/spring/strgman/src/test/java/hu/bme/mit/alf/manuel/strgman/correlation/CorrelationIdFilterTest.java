package hu.bme.mit.alf.manuel.strgman.correlation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CorrelationIdFilterTest {

    @Test
    public void testCorrelationIdFilterIsLoaded() {
        // Simple test to ensure the filter can be instantiated
        CorrelationIdFilter filter = new CorrelationIdFilter();
        assertNotNull(filter);
    }
}