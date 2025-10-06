package hu.bme.mit.alf.manuel.strgman.correlation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CorrelationIdFilterIntegrationTest {

    @Mock
    private HttpServletRequest request;
    
    @Mock
    private HttpServletResponse response;
    
    @Mock
    private FilterChain filterChain;

    @Test
    public void testCorrelationIdFilterProcessing() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Setup
        String testCorrelationId = "test-correlation-id-123";
        when(request.getHeader("X-Correlation-ID")).thenReturn(testCorrelationId);
        when(request.getMethod()).thenReturn("GET");
        when(request.getRequestURI()).thenReturn("/api/test");
        
        CorrelationIdFilter filter = new CorrelationIdFilter();
        
        // Execute
        filter.doFilterInternal(request, response, filterChain);
        
        // Verify
        verify(response).setHeader("X-Correlation-ID", testCorrelationId);
        verify(filterChain).doFilter(request, response);
        
        // Note: MDC is cleared in finally block, so we can't test it here
        // but we can test that no exceptions were thrown
    }
    
    @Test
    public void testCorrelationIdGenerationWhenNotProvided() throws Exception {
        MockitoAnnotations.openMocks(this);
        
        // Setup - no correlation ID provided
        when(request.getHeader("X-Correlation-ID")).thenReturn(null);
        when(request.getMethod()).thenReturn("POST");
        when(request.getRequestURI()).thenReturn("/api/create");
        
        CorrelationIdFilter filter = new CorrelationIdFilter();
        
        // Execute
        filter.doFilterInternal(request, response, filterChain);
        
        // Verify that a correlation ID was generated and set
        verify(response).setHeader(eq("X-Correlation-ID"), any(String.class));
        verify(filterChain).doFilter(request, response);
    }
}