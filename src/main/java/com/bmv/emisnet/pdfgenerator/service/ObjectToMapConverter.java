package com.bmv.emisnet.pdfgenerator.service;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * Utilidad para convertir objetos Java a Map<String, Object>
 * Permite convertir POJOs a mapas para uso en plantillas Thymeleaf
 */
@Slf4j
public class ObjectToMapConverter {
    
    /**
     * Convierte un objeto a un Map<String, Object> usando reflexión
     * Accede a todos los campos públicos y privados del objeto
     * 
     * @param obj Objeto a convertir
     * @return Map con los campos del objeto
     */
    public static Map<String, Object> convertToMap(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        
        try {
            // Obtener todos los campos (públicos y privados)
            Field[] fields = clazz.getDeclaredFields();
            
            for (Field field : fields) {
                field.setAccessible(true); // Permitir acceso a campos privados
                
                try {
                    Object value = field.get(obj);
                    map.put(field.getName(), value);
                } catch (IllegalAccessException e) {
                    log.warn("No se pudo acceder al campo {}: {}", field.getName(), e.getMessage());
                }
            }
            
        } catch (Exception e) {
            log.error("Error al convertir objeto {} a mapa: {}", clazz.getSimpleName(), e.getMessage());
        }
        
        return map;
    }
    
    /**
     * Convierte un objeto a un Map<String, Object> incluyendo campos de superclases
     * 
     * @param obj Objeto a convertir
     * @return Map con todos los campos del objeto y sus superclases
     */
    public static Map<String, Object> convertToMapIncludingInheritance(Object obj) {
        if (obj == null) {
            return new HashMap<>();
        }
        
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        
        try {
            // Recorrer toda la jerarquía de clases
            while (clazz != null && clazz != Object.class) {
                Field[] fields = clazz.getDeclaredFields();
                
                for (Field field : fields) {
                    field.setAccessible(true);
                    
                    try {
                        Object value = field.get(obj);
                        map.put(field.getName(), value);
                    } catch (IllegalAccessException e) {
                        log.warn("No se pudo acceder al campo {}: {}", field.getName(), e.getMessage());
                    }
                }
                
                clazz = clazz.getSuperclass();
            }
            
        } catch (Exception e) {
            log.error("Error al convertir objeto a mapa con herencia: {}", e.getMessage());
        }
        
        return map;
    }
    
    /**
     * Convierte un objeto a un Map<String, Object> excluyendo campos específicos
     * 
     * @param obj Objeto a convertir
     * @param excludeFields Nombres de campos a excluir
     * @return Map con los campos del objeto (excluyendo los especificados)
     */
    public static Map<String, Object> convertToMapExcluding(Object obj, String... excludeFields) {
        Map<String, Object> map = convertToMap(obj);
        
        for (String fieldName : excludeFields) {
            map.remove(fieldName);
        }
        
        return map;
    }
}
