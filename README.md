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

## Sistema de Estilos Compartidos

### Arquitectura Modular

El sistema utiliza una arquitectura modular donde los estilos están organizados en fragmentos separados:

#### 1. Estilos Base (`fragments/base-styles.html`)
Contiene todos los estilos CSS base que son compartidos por todas las plantillas:
```html
<style th:fragment="base-styles">
    /* Todos los estilos CSS base aquí */
</style>
```

#### 2. Fragmentos de Header (`fragments/header.html`)
Contiene elementos reutilizables y configuraciones de orientación:
- `header` - Logo de BMV
- `landscape-styles` - Configuración para orientación landscape
- `portrait-styles` - Configuración para orientación portrait

### Fragmentos Disponibles

#### 1. Estilos Base
```html
<style th:replace="~{fragments/base-styles :: base-styles}"></style>
```
Incluye todos los estilos CSS necesarios para las plantillas PDF.

#### 2. Header con Logo
```html
<div th:replace="~{fragments/header :: header}"></div>
```

#### 3. Orientación Landscape
```html
<style th:replace="~{fragments/header :: landscape-styles}"></style>
```
Configura la orientación landscape para el PDF.

#### 4. Orientación Portrait
```html
<style th:replace="~{fragments/header :: portrait-styles}"></style>
```
Configura la orientación portrait para el PDF.

### Cómo Usar en una Plantilla

```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org" lang="es">
<head>
    <meta charset="UTF-8">
    <title>Mi Plantilla PDF</title>

    <!-- Importar estilos base -->
    <style th:replace="~{fragments/base-styles :: base-styles}"></style>
    
    <!-- Importar orientación landscape -->
    <style th:replace="~{fragments/header :: landscape-styles}"></style>
</head>
<body>
    <div class="container">
        <!-- Header reutilizable -->
        <div th:replace="~{fragments/header :: header}"></div>
        
        <!-- Tu contenido específico aquí -->
        <div class="content-card">
            <h1 class="card-title">Mi Título</h1>
            <!-- ... resto del contenido ... -->
        </div>
    </div>
</body>
</html>
```

## Clases CSS Disponibles

### Contenedores
- `.container` - Contenedor principal
- `.container-landscape` - Para orientación landscape
- `.container-padded` - Con padding adicional

### Tarjetas de Contenido
- `.content-card` - Tarjeta de contenido básica
- `.content-card-padded` - Con padding adicional
- `.card-title` - Título de tarjeta
- `.card-title-large` - Título grande

### Tablas
- `.data-table` - Tabla genérica
- `.posiciones-table` - Tabla específica para posiciones
- `.info-table` - Tabla de información

### Celdas de Tabla
- `.emisor-cell` - Celda de emisor (alineación izquierda)
- `.serie-cell` - Celda de serie (alineación izquierda)
- `.number-cell` - Celda numérica (alineación derecha, fuente monoespaciada)
- `.total-cell` - Celda de total (fondo destacado)

### Información
- `.info-grid` - Grid de información
- `.info-item` - Item de información
- `.info-label` - Etiqueta de información
- `.info-value` - Valor de información
- `.info-text` - Texto de información

### Utilidades
- `.text-left`, `.text-right`, `.text-center` - Alineación de texto
- `.font-bold`, `.font-mono` - Estilos de fuente
- `.text-primary`, `.text-secondary` - Colores de texto

## Uso del Servicio

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
