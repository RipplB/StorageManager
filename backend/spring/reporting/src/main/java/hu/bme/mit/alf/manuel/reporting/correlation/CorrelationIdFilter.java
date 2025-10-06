package hu.bme.mit.alf.manuel.reporting.correlation;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * Filter to handle correlation ID for request tracing across the reporting service.
 * This filter extracts the correlation ID from the X-Correlation-ID header
 * and sets it in the MDC for logging purposes.
 */
@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CorrelationIdFilter extends OncePerRequestFilter {

    private static final String CORRELATION_ID_HEADER = "X-Correlation-ID";
    private static final String CORRELATION_ID_MDC_KEY = "correlationId";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String correlationId = request.getHeader(CORRELATION_ID_HEADER);
        
        // Generate a new correlation ID if none provided
        if (correlationId == null || correlationId.trim().isEmpty()) {
            correlationId = UUID.randomUUID().toString();
            log.debug("No correlation ID provided, generated new one: {}", correlationId);
        }
        
        try {
            // Set correlation ID in MDC for logging
            MDC.put(CORRELATION_ID_MDC_KEY, correlationId);
            
            // Add correlation ID to response header for client reference
            response.setHeader(CORRELATION_ID_HEADER, correlationId);
            
            log.debug("Processing request {} {} with correlation ID: {}", request.getMethod(), request.getRequestURI(), correlationId);
            
            // Continue with the filter chain
            filterChain.doFilter(request, response);
            
        } finally {
            // Always clean up MDC to prevent memory leaks
            MDC.clear();
        }
    }
    
    /**
     * Get the current correlation ID from MDC
     * @return current correlation ID or null if not set
     */
    public static String getCurrentCorrelationId() {
        return MDC.get(CORRELATION_ID_MDC_KEY);
    }
}