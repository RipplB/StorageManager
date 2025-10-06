# Correlation ID Implementation

## Overview
This document demonstrates the correlation ID system implemented across the StorageManager application to enable end-to-end request tracing.

## How It Works

### 1. Frontend Request Initiation (Qt C++)
When the frontend makes any HTTP request:

```cpp
// In RestAccessManager::post()
QByteArray correlationId = generateCorrelationId();  // Generates UUID
request.setRawHeader(correlationIdHeader, correlationId);
qDebug() << "POST request to" << api << "[CorrelationID:" << correlationId << "]";
```

### 2. Backend Request Processing (Spring Boot)
The CorrelationIdFilter processes every incoming request:

```java
// Extract or generate correlation ID
String correlationId = request.getHeader("X-Correlation-ID");
if (correlationId == null || correlationId.trim().isEmpty()) {
    correlationId = UUID.randomUUID().toString();
}

// Store in MDC for logging
MDC.put("correlationId", correlationId);
```

### 3. Logging Throughout the Application
All log statements automatically include the correlation ID:

```java
// Example log output with correlation ID
log.error("Failed saving stock receive [CorrelationID: {}]", getCurrentCorrelationId(), e);
```

### 4. Log Format Configuration
The log pattern includes correlation ID:
```properties
logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level [%X{correlationId:-}] %logger{36} - %msg%n
```

## Example Request Flow

### Frontend Log Output:
```
POST request to /stocks/receive [CorrelationID: a1b2c3d4-e5f6-7890-abcd-ef1234567890]
Request to path /stocks/receive finished [CorrelationID: a1b2c3d4-e5f6-7890-abcd-ef1234567890]
HTTP: 200 [CorrelationID: a1b2c3d4-e5f6-7890-abcd-ef1234567890]
```

### Backend Log Output:
```
2024-01-15 10:30:45 [http-nio-8081-exec-1] DEBUG [a1b2c3d4-e5f6-7890-abcd-ef1234567890] hu.bme.mit.alf.manuel.strgman.correlation.CorrelationIdFilter - Processing request POST /stocks/receive with correlation ID: a1b2c3d4-e5f6-7890-abcd-ef1234567890
2024-01-15 10:30:45 [http-nio-8081-exec-1] INFO  [a1b2c3d4-e5f6-7890-abcd-ef1234567890] hu.bme.mit.alf.manuel.strgman.stock.StockController - Processing stock receive [CorrelationID: a1b2c3d4-e5f6-7890-abcd-ef1234567890]
```

## Components Modified

### Frontend (Qt C++)
- **restaccessmanager.h/cpp**: Added correlation ID generation and header setting
- **All HTTP methods**: GET, POST, PUT, DELETE now include correlation ID

### Backend (Spring Boot)
- **CorrelationIdFilter**: Created for both strgman and reporting services
- **AuthorizationFilter**: Enhanced with correlation ID logging
- **StockController**: All log statements include correlation ID
- **EmailController**: Message processing includes correlation ID
- **application.properties**: Log format updated for correlation ID display

## Benefits

1. **End-to-End Tracing**: Track a single request from frontend through all backend services
2. **Debugging**: Quickly identify all log entries related to a specific request
3. **Monitoring**: Correlate errors and performance issues across distributed components
4. **Troubleshooting**: Support teams can follow exact request paths
5. **Performance Analysis**: Measure request processing time across components

## Testing

The implementation includes comprehensive tests:
- **Unit Tests**: Basic filter functionality
- **Integration Tests**: Mock-based testing of filter behavior
- **Scenarios Tested**: 
  - Correlation ID provided by client
  - Correlation ID generation when not provided
  - Proper MDC cleanup
  - Filter chain execution

## Usage

The correlation ID system works automatically once deployed:
1. No changes required in business logic
2. All existing log statements automatically include correlation ID
3. Frontend automatically generates and sends correlation IDs
4. Backend automatically processes and propagates correlation IDs

## Standards Compliance

- Uses standard `X-Correlation-ID` HTTP header
- Follows RFC guidelines for correlation tracking
- Compatible with common monitoring and logging tools
- Thread-safe implementation using Spring's MDC