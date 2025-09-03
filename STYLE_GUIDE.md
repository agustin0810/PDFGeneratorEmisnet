# 🎨 Guía del Sistema de Estilos BMV - Escalable para 200+ Reportes

## 📋 Índice
1. [Arquitectura del Sistema](#arquitectura-del-sistema)
2. [Fragmentos Disponibles](#fragmentos-disponibles)
3. [Sistema de Colores](#sistema-de-colores)
4. [Sistema de Tipografía](#sistema-de-tipografía)
5. [Sistema de Espaciado](#sistema-de-espaciado)
6. [Componentes de Tabla](#componentes-de-tabla)
7. [Componentes de UI](#componentes-de-ui)
8. [Utilidades](#utilidades)
9. [Ejemplos de Uso](#ejemplos-de-uso)
10. [Mejores Prácticas](#mejores-prácticas)

## 🏗️ Arquitectura del Sistema

### Fragmentos Principales
```
fragments/
├── base-styles.html          # Estilos base y variables CSS
├── table-components.html     # Componentes de tabla especializados
├── ui-components.html        # Componentes de interfaz de usuario
└── header.html              # Header y estilos de orientación
```

### Cómo Incluir Fragmentos
```html
<!-- Estilos base (siempre incluir) -->
<style th:replace="~{fragments/base-styles :: base-styles}"></style>

<!-- Componentes de tabla (opcional) -->
<style th:replace="~{fragments/table-components :: table-components}"></style>

<!-- Componentes de UI (opcional) -->
<style th:replace="~{fragments/ui-components :: ui-components}"></style>
```

## 🎨 Sistema de Colores

> **Nota**: Todos los elementos de texto ahora siguen automáticamente la paleta de colores. Los links también usan los colores de la paleta con estados hover y visited.

### Variables CSS Principales
```css
:root {
    --color-primary: #1D4167;        /* Azul principal */
    --color-primary-light: #345476;  /* Azul claro */
    --color-primary-dark: #1A3A5D;  /* Azul oscuro */
    --color-secondary: #4A6785;      /* Azul secundario */
    --color-accent: #173452;         /* Azul acento */
    
    /* Estados */
    --color-success: #28a745;        /* Verde */
    --color-warning: #ffc107;        /* Amarillo */
    --color-danger: #dc3545;         /* Rojo */
    --color-info: #17a2b8;           /* Azul info */
}
```

### Clases de Color
```html
<!-- Texto -->
<span class="text-primary">Texto principal</span>
<span class="text-secondary">Texto secundario</span>
<span class="text-success">Texto éxito</span>
<span class="text-warning">Texto advertencia</span>
<span class="text-danger">Texto error</span>

<!-- Estados -->
<span class="status-success">Estado exitoso</span>
<span class="status-warning">Estado de advertencia</span>
<span class="status-danger">Estado de error</span>

<!-- Links -->
<a href="#">Link normal</a>
<a href="#" class="text-secondary">Link secundario</a>
```

### Colores Automáticos
- **Texto principal**: Todos los elementos de texto usan `var(--color-primary)` automáticamente
- **Links**: Usan `var(--color-primary)` con hover en `var(--color-primary-dark)`
- **Celdas de tabla**: Usan `var(--color-primary)` para datos, `var(--color-accent)` para etiquetas
- **Headers**: Usan colores de la paleta según el tipo

## 📝 Sistema de Tipografía

> **Nota**: Los tamaños de fuente están optimizados para PDFs y reportes impresos. Los tamaños más pequeños (8px-12px) son ideales para tablas con muchos datos, mientras que los tamaños más grandes se usan para títulos y encabezados.

### Tamaños de Fuente
```css
.text-xs { font-size: 8px; }    /* Muy pequeño - Para tablas y datos */
.text-sm { font-size: 10px; }   /* Pequeño - Para texto secundario */
.text-base { font-size: 12px; } /* Base - Para texto principal */
.text-md { font-size: 14px; }   /* Mediano - Para títulos de sección */
.text-lg { font-size: 16px; }   /* Grande - Para títulos principales */
.text-xl { font-size: 18px; }   /* Extra grande - Para títulos grandes */
.text-2xl { font-size: 20px; } /* 2X grande - Para títulos de página */
.text-3xl { font-size: 24px; } /* 3X grande - Para títulos principales */
```

### Pesos de Fuente
```html
<span class="font-light">Light (300)</span>
<span class="font-normal">Normal (400)</span>
<span class="font-medium">Medium (500)</span>
<span class="font-semibold">Semibold (600)</span>
<span class="font-bold">Bold (700)</span>
```

### Alineación
```html
<p class="text-left">Izquierda</p>
<p class="text-center">Centro</p>
<p class="text-right">Derecha</p>
```

## 📏 Sistema de Espaciado

### Padding
```html
<div class="p-xs">Padding extra pequeño</div>
<div class="p-sm">Padding pequeño</div>
<div class="p-md">Padding mediano</div>
<div class="p-lg">Padding grande</div>
<div class="p-xl">Padding extra grande</div>

<!-- Padding horizontal -->
<div class="px-sm">Padding horizontal pequeño</div>
<div class="px-md">Padding horizontal mediano</div>

<!-- Padding vertical -->
<div class="py-sm">Padding vertical pequeño</div>
<div class="py-md">Padding vertical mediano</div>
```

### Margin
```html
<div class="m-xs">Margin extra pequeño</div>
<div class="m-sm">Margin pequeño</div>
<div class="m-md">Margin mediano</div>
<div class="m-lg">Margin grande</div>
<div class="m-xl">Margin extra grande</div>

<!-- Margin bottom -->
<div class="mb-sm">Margin bottom pequeño</div>
<div class="mb-md">Margin bottom mediano</div>
<div class="mb-lg">Margin bottom grande</div>
```

## 📊 Componentes de Tabla

### Tabla Base
```html
<table class="table-base">
    <thead>
        <tr>
            <th class="table-header-simple">Columna 1</th>
            <th class="table-header-simple">Columna 2</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="table-cell-base">Dato 1</td>
            <td class="table-cell-base">Dato 2</td>
        </tr>
    </tbody>
</table>
```

> **Nota**: Todas las tablas ahora tienen bordes visibles (2px en el contorno, 1px entre celdas) y todos los textos siguen la paleta de colores automáticamente.

### Tabla con Headers Multi-nivel
```html
<table class="table-base">
    <thead>
        <tr>
            <th class="table-header-main" colspan="3">Grupo Principal</th>
        </tr>
        <tr>
            <th class="table-header-sub">Sub-columna 1</th>
            <th class="table-header-sub">Sub-columna 2</th>
            <th class="table-header-sub">Sub-columna 3</th>
        </tr>
    </thead>
    <tbody>
        <tr>
            <td class="table-cell-base">Dato 1</td>
            <td class="table-cell-base">Dato 2</td>
            <td class="table-cell-base">Dato 3</td>
        </tr>
    </tbody>
</table>
```

### Tipos de Celdas
```html
<!-- Celda con texto a la izquierda -->
<td class="table-cell-left">Texto izquierda</td>

<!-- Celda con texto a la derecha -->
<td class="table-cell-right">Texto derecha</td>

<!-- Celda centrada -->
<td class="table-cell-center">Texto centro</td>

<!-- Celda con fuente monoespaciada -->
<td class="table-cell-mono">123.45</td>

<!-- Celda de total -->
<td class="table-cell-total">Total</td>

<!-- Celda destacada -->
<td class="table-cell-highlight">Destacado</td>
```

### Anchos de Columna Predefinidos
```html
<!-- Anchos específicos -->
<th class="col-width-xs">4%</th>
<th class="col-width-sm">6%</th>
<th class="col-width-md">8%</th>
<th class="col-width-lg">10%</th>
<th class="col-width-xl">12%</th>
<th class="col-width-2xl">15%</th>
<th class="col-width-3xl">18%</th>
<th class="col-width-4xl">20%</th>
<th class="col-width-5xl">25%</th>
<th class="col-width-6xl">30%</th>

<!-- Anchos para tipos de datos -->
<th class="col-date">Fecha</th>
<th class="col-time">Hora</th>
<th class="col-code">Código</th>
<th class="col-name">Nombre</th>
<th class="col-description">Descripción</th>
<th class="col-amount">Monto</th>
<th class="col-percentage">Porcentaje</th>
<th class="col-status">Estado</th>
<th class="col-action">Acción</th>
```

### Tablas Especializadas
```html
<!-- Tabla de balance -->
<table class="table-balance">
    <tr>
        <td class="account-name">Cuenta</td>
        <td class="text-monetary">$1,234.56</td>
    </tr>
</table>

<!-- Tabla de operaciones -->
<table class="table-operations">
    <tr>
        <td class="operation-buy">COMPRA</td>
        <td class="operation-sell">VENTA</td>
        <td class="operation-hold">MANTENER</td>
    </tr>
</table>

<!-- Tabla de riesgo -->
<table class="table-risk">
    <tr>
        <td class="risk-low">Bajo</td>
        <td class="risk-medium">Medio</td>
        <td class="risk-high">Alto</td>
    </tr>
</table>

<!-- Tabla de comparación -->
<table class="table-comparison">
    <tr>
        <td class="change-positive">+15.5%</td>
        <td class="change-negative">-8.2%</td>
        <td class="change-neutral">0.0%</td>
    </tr>
</table>
```

## 🎛️ Componentes de UI

### Botones
```html
<!-- Botones básicos -->
<button class="btn btn-primary">Primario</button>
<button class="btn btn-secondary">Secundario</button>
<button class="btn btn-success">Éxito</button>
<button class="btn btn-warning">Advertencia</button>
<button class="btn btn-danger">Peligro</button>
<button class="btn btn-info">Información</button>

<!-- Tamaños -->
<button class="btn btn-primary btn-sm">Pequeño</button>
<button class="btn btn-primary">Normal</button>
<button class="btn btn-primary btn-lg">Grande</button>
```

### Formularios
```html
<div class="form-group">
    <label class="form-label">Etiqueta</label>
    <input type="text" class="form-input" placeholder="Texto">
    <div class="form-help">Texto de ayuda</div>
</div>

<div class="form-group">
    <label class="form-label">Selección</label>
    <select class="form-select">
        <option>Opción 1</option>
        <option>Opción 2</option>
    </select>
</div>

<div class="form-group">
    <label class="form-label">Área de texto</label>
    <textarea class="form-textarea" placeholder="Texto largo"></textarea>
</div>
```

### Alertas
```html
<div class="alert alert-info">Información</div>
<div class="alert alert-success">Éxito</div>
<div class="alert alert-warning">Advertencia</div>
<div class="alert alert-danger">Error</div>
```

### Badges
```html
<span class="badge badge-primary">Primario</span>
<span class="badge badge-secondary">Secundario</span>
<span class="badge badge-success">Éxito</span>
<span class="badge badge-warning">Advertencia</span>
<span class="badge badge-danger">Peligro</span>
<span class="badge badge-info">Información</span>
<span class="badge badge-light">Claro</span>
```

### Cards
```html
<div class="card">
    <div class="card-header">
        <h3 class="card-title">Título de la Card</h3>
    </div>
    <div class="card-body">
        <p>Contenido de la card</p>
    </div>
    <div class="card-footer">
        <button class="btn btn-primary">Acción</button>
    </div>
</div>
```

### Navegación
```html
<div class="nav-tabs">
    <button class="nav-tab active">Tab 1</button>
    <button class="nav-tab">Tab 2</button>
    <button class="nav-tab">Tab 3</button>
</div>
```

### Progress
```html
<div class="progress">
    <div class="progress-bar" style="width: 75%"></div>
</div>

<div class="progress">
    <div class="progress-bar progress-bar-success" style="width: 60%"></div>
</div>
```

## 🛠️ Utilidades

### Layout
```html
<div class="d-flex justify-content-between align-items-center">
    <span>Izquierda</span>
    <span>Derecha</span>
</div>

<div class="d-flex flex-column">
    <div>Elemento 1</div>
    <div>Elemento 2</div>
</div>
```

### Texto Especializado
```html
<!-- Texto monetario -->
<span class="text-monetary">$1,234.56</span>

<!-- Texto porcentual -->
<span class="text-percentage">15.5%</span>

<!-- Texto de código -->
<span class="text-code">ABC123</span>

<!-- Texto de estado -->
<span class="text-status">ACTIVO</span>
```

### Estados de Fila
```html
<tr class="row-highlight">Fila destacada</tr>
<tr class="row-selected">Fila seleccionada</tr>
<tr class="row-total">Fila de total</tr>
<tr class="row-subtotal">Fila de subtotal</tr>
```

## 📋 Ejemplos de Uso

### Reporte Financiero Básico
```html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <style th:replace="~{fragments/base-styles :: base-styles}"></style>
    <style th:replace="~{fragments/table-components :: table-components}"></style>
</head>
<body>
    <div class="container">
        <div class="header">
            <img src="logo.png" class="bmv-logo" alt="BMV Logo">
        </div>
        
        <div class="content-card">
            <h1 class="card-title">Reporte Financiero</h1>
            <div class="info-grid">
                <div class="info-item">
                    <span class="info-label">Fecha</span>
                    <span class="info-value">15/01/2025</span>
                </div>
            </div>
        </div>
        
        <div class="table-container">
            <div class="section-title">Balance General</div>
            <table class="table-balance">
                <thead>
                    <tr>
                        <th class="col-name">Cuenta</th>
                        <th class="col-amount">Monto</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td class="account-name">Activos</td>
                        <td class="text-monetary">$1,000,000.00</td>
                    </tr>
                    <tr class="row-total">
                        <td class="account-name">Total</td>
                        <td class="text-monetary">$1,000,000.00</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>
</body>
</html>
```

### Reporte con Múltiples Tablas
```html
<div class="table-split">
    <table class="table-operations">
        <thead>
            <tr>
                <th class="col-date">Fecha</th>
                <th class="col-code">Código</th>
                <th class="col-amount">Monto</th>
                <th class="col-status">Estado</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>15/01/2025</td>
                <td class="text-code">ABC123</td>
                <td class="text-monetary">$1,234.56</td>
                <td class="text-status">ACTIVO</td>
            </tr>
        </tbody>
    </table>
    
    <table class="table-risk">
        <thead>
            <tr>
                <th class="col-name">Instrumento</th>
                <th class="col-percentage">Riesgo</th>
            </tr>
        </thead>
        <tbody>
            <tr>
                <td>Acciones</td>
                <td class="risk-medium">Medio</td>
            </tr>
        </tbody>
    </table>
</div>
```

## ✅ Mejores Prácticas

### 1. Estructura de Archivos
- Siempre incluir `base-styles` en todos los reportes
- Incluir `table-components` solo si necesitas tablas especializadas
- Incluir `ui-components` solo si necesitas elementos interactivos

### 2. Nomenclatura
- Usar clases predefinidas en lugar de crear estilos personalizados
- Seguir la convención: `tipo-propósito` (ej: `table-balance`, `btn-primary`)
- Usar variables CSS para colores y espaciado

### 3. Responsividad
- Usar `table-scroll` para tablas muy anchas
- Usar `table-split` para dividir tablas grandes
- Considerar `col-width-*` para control de anchos

### 4. Accesibilidad
- Usar `scope` en headers de tabla
- Proporcionar texto alternativo para imágenes
- Usar contraste adecuado en colores

### 5. Impresión
- Los estilos de impresión están incluidos automáticamente
- Los botones y elementos interactivos se ocultan en impresión
- Las tablas mantienen su estructura en PDF

### 6. Performance
- Reutilizar clases existentes
- Evitar estilos inline
- Usar selectores CSS eficientes

## 🔧 Personalización

### Agregar Nuevos Colores
```css
:root {
    --color-custom: #your-color;
}

.custom-element {
    color: var(--color-custom);
}
```

### Agregar Nuevos Tamaños
```css
.text-4xl { font-size: 32px; }
.col-width-10xl { width: 50% !important; max-width: 50% !important; }
```

### Crear Nuevos Componentes
```css
.table-custom {
    /* Extender table-base */
    @extend .table-base;
    /* Agregar estilos específicos */
    background-color: var(--color-custom);
}
```

---

## 📞 Soporte

Para dudas sobre el sistema de estilos:
1. Revisar esta guía
2. Ver ejemplos en reportes existentes
3. Consultar las variables CSS en `base-styles.html`
4. Crear un nuevo fragmento si es necesario

**¡El sistema está diseñado para escalar a 200+ reportes manteniendo consistencia y facilidad de mantenimiento!** 🚀
