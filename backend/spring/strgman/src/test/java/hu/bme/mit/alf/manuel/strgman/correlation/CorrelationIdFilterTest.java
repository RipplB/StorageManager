package hu.bme.mit.alf.manuel.strgman.correlation;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
public class CorrelationIdFilterTest {

    @Test
    public void testCorrelationIdFilterIsLoaded() {
        // Simple test to ensure the filter can be instantiated
        CorrelationIdFilter filter = new CorrelationIdFilter();
        assertNotNull(filter);
    }
}