# PDF Generator Emisnet

Generador de PDFs usando Thymeleaf y Flying Saucer para el sistema Emisnet de la BMV.

## Características

- ✅ Generación de PDFs a partir de plantillas HTML con Thymeleaf
- ✅ Estilos CSS compartidos entre todas las plantillas
- ✅ Soporte para orientación landscape y portrait
- ✅ Fragmentos reutilizables de Thymeleaf
- ✅ Compatibilidad completa con Flying Saucer
- ✅ Diseño responsivo y profesional
- ✅ Arquitectura modular de estilos

## Estructura del Proyecto

```
src/main/resources/templates/
├── fragments/
│   ├── base-styles.html      # Estilos base compartidos
│   └── header.html           # Fragmentos de header y orientación
├── css/
│   └── pdf-common.css       # Estilos CSS (referencia, no usado directamente)
├── reporte-posiciones.html # Plantilla de reporte de posiciones
├── reporte-ventas.html     # Plantilla de reporte de ventas
├── aviso-extemporaneidad.html # Plantilla de aviso de extemporaneidad
├── confirmacion-envio.html # Plantilla de confirmación de envío
└── ejemplo-plantilla.html   # Plantilla de ejemplo
```

## Ventajas del Sistema

1. **Arquitectura Modular**: Los estilos están organizados en fragmentos separados y reutilizables
2. **Estilos Compartidos**: Todas las plantillas heredan automáticamente los estilos del fragmento base
3. **Compatibilidad con Flying Saucer**: Los estilos están inyectados directamente, evitando problemas de importación
4. **Mantenibilidad**: Los cambios en estilos se aplican a todas las plantillas automáticamente
5. **Flexibilidad**: Cada plantilla puede elegir su orientación (landscape/portrait)
6. **Reutilización**: El header y otros elementos son fragmentos reutilizables
7. **Separación de Responsabilidades**: Los estilos base están separados de la configuración de orientación

## Notas Técnicas

- Los estilos están incluidos directamente en el fragmento `base-styles` para evitar problemas con Flying Saucer
- El archivo `pdf-common.css` se mantiene como referencia pero no se usa directamente
- Todas las plantillas deben incluir el fragmento `base-styles` para heredar los estilos
- La orientación se define con los fragmentos `landscape-styles` o `portrait-styles`
- El header se incluye como fragmento separado para máxima reutilización

## Guía de Uso

### 1. Configuración Inicial

Para comenzar a usar el generador de PDFs, asegúrate de tener las siguientes dependencias en tu `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-thymeleaf</artifactId>
</dependency>
<dependency>
    <groupId>org.xhtmlrenderer</groupId>
    <artifactId>flying-saucer-pdf</artifactId>
    <version>9.1.22</version>
</dependency>
```

### 2. Uso Básico del Servicio

El servicio principal `GeneradorReportesPDF` proporciona métodos para generar PDFs:

```java
@Service
public class MiServicio {
    
    @Autowired
    private GeneradorReportesPDF generadorPDF;
    
    public byte[] generarMiPDF(Map<String, Object> datos) throws IOException, DocumentException {
        return generadorPDF.generarPDF("nombre-plantilla", datos);
    }
}
```

### 3. Plantillas Disponibles

El sistema incluye las siguientes plantillas predefinidas:

- **`aviso-extemporaneidad.html`** - Para avisos de extemporaneidad
- **`confirmacion-envio.html`** - Para confirmaciones de envío
- **`reporte-posiciones.html`** - Para reportes de posiciones

### 4. Estructura de Datos

Cada plantilla espera datos específicos. Aquí tienes ejemplos de la estructura de datos para cada tipo:

#### Aviso de Extemporaneidad
```java
Map<String, Object> datos = new HashMap<>();
datos.put("fecha", "2024-01-15");
datos.put("emisor", "EMPRESA S.A.");
datos.put("serie", "A");
datos.put("motivo", "Problemas técnicos en el sistema");
datos.put("descripcion", "Detalle del problema...");
```

#### Confirmación de Envío
```java
Map<String, Object> datos = new HashMap<>();
datos.put("fechaEnvio", "2024-01-15 14:30:00");
datos.put("archivosRecibidos", Arrays.asList(
    new ArchivoRecibido("archivo1.pdf", "2024-01-15 14:25:00"),
    new ArchivoRecibido("archivo2.pdf", "2024-01-15 14:28:00")
));
datos.put("totalArchivos", 2);
```

#### Reporte de Posiciones
```java
Map<String, Object> datos = new HashMap<>();
datos.put("fechaReporte", "2024-01-15");
datos.put("gruposPosiciones", Arrays.asList(
    new GrupoPosiciones("Grupo A", Arrays.asList(
        new PosicionDetalle("EMPRESA A", "A", 1000, 50000.00),
        new PosicionDetalle("EMPRESA B", "B", 2000, 75000.00)
    )),
    new GrupoPosiciones("Grupo B", Arrays.asList(
        new PosicionDetalle("EMPRESA C", "C", 1500, 45000.00)
    ))
));
```

### 5. Ejemplos de Uso Completo

#### Ejemplo 1: Generar Aviso de Extemporaneidad
```java
@RestController
public class PDFController {
    
    @Autowired
    private GeneradorReportesPDF generadorPDF;
    
    @PostMapping("/generar-aviso-extemporaneidad")
    public ResponseEntity<byte[]> generarAvisoExtemporaneidad(@RequestBody AvisoExtemporaneidadRequest request) {
        try {
            Map<String, Object> datos = new HashMap<>();
            datos.put("fecha", request.getFecha());
            datos.put("emisor", request.getEmisor());
            datos.put("serie", request.getSerie());
            datos.put("motivo", request.getMotivo());
            datos.put("descripcion", request.getDescripcion());
            
            byte[] pdfBytes = generadorPDF.generarPDF("aviso-extemporaneidad", datos);
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "aviso-extemporaneidad.pdf");
            
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
```

#### Ejemplo 2: Generar Reporte de Posiciones
```java
@PostMapping("/generar-reporte-posiciones")
public ResponseEntity<byte[]> generarReportePosiciones(@RequestBody ReportePosicionesRequest request) {
    try {
        Map<String, Object> datos = new HashMap<>();
        datos.put("fechaReporte", request.getFechaReporte());
        datos.put("gruposPosiciones", request.getGruposPosiciones());
        
        byte[] pdfBytes = generadorPDF.generarPDF("reporte-posiciones", datos);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", "reporte-posiciones.pdf");
        
        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
}
```

### 6. Crear una Nueva Plantilla

Para crear una nueva plantilla personalizada:

1. **Crear el archivo HTML** en `src/main/resources/templates/`:
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Nueva Plantilla</title>
    
    <!-- Importar estilos base -->
    <style th:replace="~{fragments/base-styles :: base-styles}"></style>
    
    <!-- Importar orientación landscape -->
    <style th:replace="~{fragments/header :: landscape-styles}"></style>
</head>
<body>
    <div class="container">
        <!-- Header reutilizable -->
        <div th:replace="~{fragments/header :: header}"></div>
        
        <!-- Tu contenido específico -->
        <div class="content-card">
            <h1 class="card-title" th:text="${titulo}">Título</h1>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">Fecha:</span>
                    <span class="info-value" th:text="${fecha}">2024-01-15</span>
                </div>
                <!-- Más contenido... -->
            </div>
        </div>
    </div>
</body>
</html>
```

2. **Usar la nueva plantilla**:
```java
Map<String, Object> datos = new HashMap<>();
datos.put("titulo", "Mi Nuevo Reporte");
datos.put("fecha", "2024-01-15");
// ... más datos

byte[] pdfBytes = generadorPDF.generarPDF("mi-nueva-plantilla", datos);
```

### 7. Configuración de Orientación

Para cambiar la orientación del PDF, usa los fragmentos correspondientes:

#### Orientación Landscape (horizontal)
```html
<style th:replace="~{fragments/header :: landscape-styles}"></style>
```

#### Orientación Portrait (vertical)
```html
<style th:replace="~{fragments/header :: portrait-styles}"></style>
```

### 8. Manejo de Errores

El servicio incluye manejo de errores básico. Para un manejo más robusto:

```java
try {
    byte[] pdfBytes = generadorPDF.generarPDF("plantilla", datos);
    // Procesar PDF generado
} catch (IOException e) {
    // Error de lectura/escritura
    logger.error("Error de I/O al generar PDF", e);
} catch (DocumentException e) {
    // Error en el documento PDF
    logger.error("Error en el documento PDF", e);
} catch (Exception e) {
    // Otros errores
    logger.error("Error inesperado al generar PDF", e);
}
```

### 9. Pruebas

El proyecto incluye pruebas unitarias para cada tipo de plantilla. Para ejecutar las pruebas:

```bash
mvn test
```

Las pruebas verifican:
- Generación correcta de PDFs
- Estructura de datos válida
- Manejo de errores
- Formato de salida

### 10. Archivos Generados

Los PDFs generados se guardan en `target/generated-pdfs/` durante el desarrollo. En producción, puedes configurar una ruta personalizada modificando el servicio.
