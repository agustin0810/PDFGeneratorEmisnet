# PDF Generator - BMV EMISNET System

PDF Generator service for the EMISNET project developed by 2H Software for Bolsa Mexicana de Valores (BMV).

## Overview

This is a Spring Boot application designed to provide PDF generation capabilities as part of the larger EMISNET ecosystem. The application uses the same dependency stack as the main EMISNET project to ensure seamless integration.

## Technology Stack

- **Java 21** - Runtime environment
- **Spring Boot 3.4.4** - Application framework
- **Spring Cloud Function** - Serverless function support
- **AWS SDK** - AWS services integration
- **iText & PDFBox** - PDF generation libraries
- **PostgreSQL** - Database
- **Maven** - Build tool
- **Lombok** - Code generation
- **MapStruct** - Bean mapping

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/bmv/emisnet/pdfgenerator/
│   │       ├── PdfGeneratorApplication.java
│   │       ├── config/
│   │       ├── controller/
│   │       └── service/
│   └── resources/
│       └── application.yml
└── test/
    ├── java/
    └── resources/
```

## Configuration

The application can be configured through environment variables or the `application.yml` file:

### Database Configuration
- `DB_USERNAME` - Database username (default: emisnet_user)
- `DB_PASSWORD` - Database password (default: emisnet_pass)

### AWS Configuration
- `AWS_REGION` - AWS region (default: us-east-1)
- `AWS_S3_BUCKET` - S3 bucket for PDF storage (default: emisnet-pdf-storage)

## Building and Running

### Prerequisites
- Java 21
- Maven 3.6+
- PostgreSQL (for production)

### Build
```bash
mvn clean compile
```

### Test
```bash
mvn test
```

### Run
```bash
mvn spring-boot:run
```

### Package
```bash
mvn clean package
```

## API Endpoints

### PDF Generation
- `POST /api/pdf/generate/html` - Generate PDF from HTML content
- `POST /api/pdf/generate/template/{templateName}` - Generate PDF from template
- `GET /api/pdf/health` - Health check endpoint

## AWS Lambda Deployment

The application is configured to support AWS Lambda deployment using the AWS Serverless Java Container. Use the Maven Shade plugin to create a deployable package:

```bash
mvn clean package shade:shade
```

## Integration with EMISNET

This service is designed to integrate with the main EMISNET project. It uses the same:
- Dependency versions
- Configuration patterns
- Logging setup
- AWS services
- Database connection patterns

## Development

### Adding PDF Templates
1. Place template files in `src/main/resources/templates/`
2. Implement template processing logic in `PdfGeneratorService`
3. Add corresponding endpoint in `PdfController`

### Testing
The project includes integration tests that use H2 in-memory database for testing scenarios.

## License

© 2024 Bolsa Mexicana de Valores. All rights reserved.
